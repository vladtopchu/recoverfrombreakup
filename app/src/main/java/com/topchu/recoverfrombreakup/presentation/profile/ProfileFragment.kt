package com.topchu.recoverfrombreakup.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.RequestManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.data.local.daos.MeditationDao
import com.topchu.recoverfrombreakup.data.local.daos.TaskDao
import com.topchu.recoverfrombreakup.databinding.FragmentProfileBinding
import com.topchu.recoverfrombreakup.di.ApplicationScope
import com.topchu.recoverfrombreakup.presentation.BuyActivity
import com.topchu.recoverfrombreakup.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var launcher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var firestoreUsers: CollectionReference

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var meditationDao: MeditationDao

    @Inject
    lateinit var sharedPref: SharedPref

    var auth: FirebaseAuth? = null

    var counter: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFirebaseGoogleAuth()
    }

    private fun initFirebaseGoogleAuth() {
        auth = Firebase.auth
        auth!!.addAuthStateListener {
            if(it.currentUser == null){
                binding.signIn.setOnClickListener {
                    signInGoogle()
                }
            } else if(it.currentUser != null) {
                binding.signIn.visibility = View.GONE
                binding.profileParent.visibility = View.VISIBLE
                binding.email.text = auth?.currentUser?.email
                glide.load(auth?.currentUser?.photoUrl).into(binding.profilePicture)
                if(sharedPref.isContentBought()){
                   binding.statusFree.visibility = View.GONE
                   binding.statusFull.visibility = View.VISIBLE
                }
                binding.buy.setOnClickListener {
                    startActivity(Intent(requireActivity(), BuyActivity::class.java))
                }
                if(sharedPref.isContentBought()){
                    binding.statusFree.visibility = View.GONE
                    binding.statusFull.visibility = View.VISIBLE
                }
                binding.logOut.setOnClickListener { _ ->
                    binding.progressCircular.visibility = View.VISIBLE
                    applicationScope.launch {
                        meditationDao.lockMeditations()
                        taskDao.lockTasks()
                        sharedPref.setContentBought(false)
                        firestoreUsers.document(auth?.uid!!)
                            .set(hashMapOf("progress" to sharedPref.getUserProgress()), SetOptions.merge())
                            .addOnSuccessListener {
                                Timber.d("User's progress was updated successfully")
                            }
                            .addOnFailureListener { exception ->
                                Timber.d("Can't update User's progress.. ".plus(exception.message))
                            }
                        auth?.signOut()
                        signOutUi()
                    }
                }
            }
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                } else {
                    Toast.makeText(requireContext(), "Account is null", Toast.LENGTH_SHORT).show()
                }
            } catch(e: ApiException){
                Toast.makeText(requireContext(), "ApiException: " + e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signOutUi() {
        counter++
        Handler(Looper.getMainLooper()).postDelayed({
            if(auth?.currentUser == null) {
                binding.progressCircular.visibility = View.GONE
                binding.profileParent.visibility = View.GONE
                binding.signIn.visibility = View.VISIBLE
            } else if(counter < 2) {
                Toast.makeText(requireContext(), "Не удалось совершить выход, попытка ".plus((counter + 1).toString()), Toast.LENGTH_SHORT).show()
                signOutUi()
            }
        }, 1000)
    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signInGoogle(){
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credentials = GoogleAuthProvider.getCredential(idToken, null)
        auth?.signInWithCredential(credentials)?.addOnCompleteListener { it ->
            if(it.isSuccessful){
                val userUid = auth!!.currentUser!!.uid
                firestoreUsers.document(userUid).get()
                    .addOnSuccessListener { documentSnapshot ->
                        if(documentSnapshot.data == null) {
                            Timber.d("snapshot data null")
                            firestoreUsers.document(userUid)
                                .set(hashMapOf(
                                    "progress" to sharedPref.getLocalProgress(),
                                    "paid" to 0,
                                    "createdAt" to System.currentTimeMillis()
                                ))
                                .addOnSuccessListener {
                                    sharedPref.setUserProgress(sharedPref.getLocalProgress())
                                    Timber.d("User was successfully created")
                                }
                                .addOnFailureListener {
                                    Timber.d("Failed to create User record")
                                }
                        } else {
                            Timber.d("snapshot data is not null")
                            val userContentStatus = documentSnapshot.getLong("paid")?.toInt()
                            val userProgress = documentSnapshot.getLong("progress")?.toInt()!!
                            if(userContentStatus == 1){
                                applicationScope.launch {
                                    taskDao.unlockTasks()
                                    meditationDao.unlockMeditations()
                                    sharedPref.setContentBought(true)
                                    binding.statusFree.visibility = View.GONE
                                    binding.statusFull.visibility = View.VISIBLE
                                }
                            }
                            if(userProgress > sharedPref.getLocalProgress()) {
                                applicationScope.launch {
                                    taskDao.openTasksUpTo(userProgress)
                                    meditationDao.openMeditationsUpTo(userProgress)
                                    sharedPref.setUserProgress(userProgress)
                                }
                            } else if(userProgress < sharedPref.getLocalProgress()){
                                applicationScope.launch {
                                    firestoreUsers.document(Firebase.auth.currentUser?.uid!!)
                                        .set(hashMapOf("progress" to sharedPref.getLocalProgress()), SetOptions.merge())
                                        .addOnSuccessListener {
                                            Timber.d("User progress updated successfully!")
                                        }
                                        .addOnFailureListener {
                                            Timber.d("Exception: ${it.message}")
                                        }
                                    sharedPref.setUserProgress(sharedPref.getLocalProgress())
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Timber.d(exception.message)
                    }
            } else {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}