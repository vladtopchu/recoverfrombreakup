package com.topchu.recoverfrombreakup.presentation.welcome

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.databinding.*
import com.topchu.recoverfrombreakup.presentation.MainActivity
import com.topchu.recoverfrombreakup.presentation.StartActivity
import com.topchu.recoverfrombreakup.utils.ParagraphAdapter
import com.topchu.recoverfrombreakup.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment: Fragment() {

    @Inject
    lateinit var sharedPref: SharedPref

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ParagraphAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val texts = listOf(R.string.intro_a, R.string.intro_b, R.string.intro_c, R.string.intro_d, R.string.intro_e, R.string.intro_f).map { getString(it) }
        adapter.setTexts(texts)
        binding.recyclerView.adapter = adapter

        binding.startApp.setOnClickListener {
            sharedPref.setNotFirstLaunch()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

}