package com.topchu.recoverfrombreakup.presentation.meditations

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
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.data.local.daos.MeditationDao
import com.topchu.recoverfrombreakup.data.local.daos.TaskDao
import com.topchu.recoverfrombreakup.databinding.FragmentAuthBinding
import com.topchu.recoverfrombreakup.databinding.FragmentMeditationsBinding
import com.topchu.recoverfrombreakup.databinding.FragmentTasksBinding
import com.topchu.recoverfrombreakup.presentation.tasks.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MeditationsFragment : Fragment() {
    private var _binding: FragmentMeditationsBinding? = null
    private val binding get() = _binding!!


    private val viewModel: MeditationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeditationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.meditations.observe(viewLifecycleOwner) {
            it.forEach { meditation ->
                Log.d("RFB_TEST", meditation.toString())
            }
        }
    }

}