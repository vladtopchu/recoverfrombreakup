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
import com.topchu.recoverfrombreakup.databinding.FragmentMeditationBinding
import com.topchu.recoverfrombreakup.presentation.meditations.MeditationItemViewModel
import com.topchu.recoverfrombreakup.presentation.meditations.SharedViewModel
import com.topchu.recoverfrombreakup.utils.MediaPlayerState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeditationFragment : Fragment() {
    private var _binding: FragmentMeditationBinding? = null
    private val binding get() = _binding!!

    private val itemViewModel: MeditationItemViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var uri: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeditationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemViewModel.meditation.observe(viewLifecycleOwner, {
            if(it != null){
                binding.title.text = it.name
                if(it.isOpened && !it.isBlocked) {
                    binding.hint.text = "Для достижения эффекта, медитации рекомендуется слушать непрерывно"
                    binding.player.visibility = View.VISIBLE
                    uri = it.uri
                } else {
                    binding.hint.text = "Чтобы получить доступ к данной медитации, откройте соответствующий день или приобретите полную версию"
                    binding.locked.visibility = View.VISIBLE
                    binding.parentView.setBackgroundResource(R.drawable.background_meditation_locked)
                }
            }
        })

        sharedViewModel.playerState.observe(viewLifecycleOwner, {
          when(it) {
              MediaPlayerState.LOADING -> {
                  Log.d("TESTTEST", "STATE LOADING")
                  if(binding.progressCircular.visibility == View.GONE)
                    binding.progressCircular.visibility = View.VISIBLE
              }
              MediaPlayerState.PLAYING, MediaPlayerState.IDLE -> {
                  Log.d("TESTTEST", "STATE PLAYING/IDLE")
                  if(binding.progressCircular.visibility == View.VISIBLE)
                      binding.progressCircular.visibility = View.GONE
              }
          }
        })

        binding.play.setOnClickListener {
            if(sharedViewModel.playerState.value != MediaPlayerState.PLAYING) {
                Log.d("TESTTEST", "CLICK: STATE NOT PLAYING")
                binding.play.setImageResource(R.drawable.button_pause_meditation)
                if(sharedViewModel.uri.value != uri) {
                    Log.d("TESTTEST", "?1")
                    sharedViewModel.setPlayerUri(uri)
                } else {
                    Log.d("TESTTEST", "?2")
                    sharedViewModel.startPlayer()
                }
            } else if(sharedViewModel.playerState.value == MediaPlayerState.PLAYING) {
                Log.d("TESTTEST", "CLICK: STATE PLAYING")
                binding.play.setImageResource(R.drawable.button_play_meditation)
                sharedViewModel.pausePlayer()
            } else {
                Log.d("TESTTEST", "CLICk: STATE WTF")
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