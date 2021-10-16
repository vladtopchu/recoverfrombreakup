package com.topchu.recoverfrombreakup.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.databinding.FragmentAuthBinding
import javax.inject.Inject

class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var launcher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding.signIn.setOnClickListener {
            signInGoogle()
        }
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
        auth.signInWithCredential(credentials).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("RFB_TEST", "3")
                startMain()
            } else {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startMain(){

    }
}