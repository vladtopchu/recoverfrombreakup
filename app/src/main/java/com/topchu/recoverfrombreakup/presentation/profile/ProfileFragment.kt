package com.topchu.recoverfrombreakup.presentation.profile

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.databinding.FragmentMeditationsBinding
import com.topchu.recoverfrombreakup.databinding.FragmentProfileBinding
import com.topchu.recoverfrombreakup.utils.MediaPlayerCommand
import com.topchu.recoverfrombreakup.utils.MediaPlayerState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.sign

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var launcher: ActivityResultLauncher<Intent>

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var glide: RequestManager

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
                binding.logOut.setOnClickListener {
                    binding.progressCircular.visibility = View.VISIBLE
                    auth?.signOut()
                    signOutUi()
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
        auth?.signInWithCredential(credentials)?.addOnCompleteListener {
            if(it.isSuccessful){
                viewModel
            } else {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(auth != null) {
            Log.d("TESTTEST", auth!!.currentUser.toString())
        }
    }
}