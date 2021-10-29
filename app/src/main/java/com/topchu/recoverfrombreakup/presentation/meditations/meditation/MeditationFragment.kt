package com.topchu.recoverfrombreakup.presentation.meditations.meditation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.databinding.ItemMeditationBinding
import com.topchu.recoverfrombreakup.presentation.meditations.MeditationItemViewModel
import com.topchu.recoverfrombreakup.presentation.meditations.SharedViewModel
import com.topchu.recoverfrombreakup.utils.MediaPlayerState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeditationFragment : Fragment() {
    private var _binding: ItemMeditationBinding? = null
    private val binding get() = _binding!!

    private val itemViewModel: MeditationItemViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var uri: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemMeditationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemViewModel.meditation.observe(viewLifecycleOwner, {
            if(it != null){
                binding.title.text = it.name
                uri = it.uri
            }
        })

        sharedViewModel.playerState.observe(viewLifecycleOwner, {
          when(it) {
              MediaPlayerState.LOADING -> {
                  if(binding.progressCircular.visibility == View.GONE)
                    binding.progressCircular.visibility = View.VISIBLE
              }
              MediaPlayerState.PLAYING, MediaPlayerState.IDLE -> {
                  if(binding.progressCircular.visibility == View.VISIBLE)
                      binding.progressCircular.visibility = View.GONE
              }
          }
        })

        binding.play.setOnClickListener {
            if(sharedViewModel.playerState.value != MediaPlayerState.PLAYING) {
                Log.d("TESTTEST", "STATE NOT PLAYING")
                binding.play.setImageResource(R.drawable.button_pause_meditation)
                if(sharedViewModel.uri.value != uri) {
                    Log.d("TESTTEST", "?1")
                    sharedViewModel.setPlayerUri(uri)
                } else {
                    Log.d("TESTTEST", "?2")
                    sharedViewModel.startPlayer()
                }
            } else if(sharedViewModel.playerState.value == MediaPlayerState.PLAYING) {
                Log.d("TESTTEST", "STATE PLAYIN")
                binding.play.setImageResource(R.drawable.button_play_meditation)
                sharedViewModel.pausePlayer()
            } else {
                Log.d("TESTTEST", "STATE WTF")
            }
        }

        binding.stop.setOnClickListener {
            binding.play.setImageResource(R.drawable.button_play_meditation)
            sharedViewModel.stopPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.play.setImageResource(R.drawable.button_play_meditation)
        sharedViewModel.resetPlayer()
    }
}