package com.topchu.recoverfrombreakup.presentation.meditations.meditation

import android.media.MediaPlayer
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

    private var uri: String? = null

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
                    sharedViewModel.setPlayerUri(uri!!)
                } else {
                    binding.hint.text = "Чтобы получить доступ к данной медитации, откройте соответствующий день или приобретите полную версию"
                    binding.locked.visibility = View.VISIBLE
                    binding.parentView.setBackgroundResource(R.drawable.background_meditation_locked)
                }
            }
        })

        sharedViewModel.playerState.observe(viewLifecycleOwner, {
            if(it == MediaPlayerState.LOADING) {
                if(binding.progressCircular.visibility == View.GONE)
                    binding.progressCircular.visibility = View.VISIBLE
            } else {
                if(binding.progressCircular.visibility == View.VISIBLE)
                    binding.progressCircular.visibility = View.GONE
            }
        })

        binding.play.setOnClickListener {
            when(sharedViewModel.playerState.value) {
                MediaPlayerState.URI_SET, MediaPlayerState.PAUSED, MediaPlayerState.READY -> {
                    binding.play.setImageResource(R.drawable.button_pause_meditation)
                    sharedViewModel.startPlayer()
                }
                MediaPlayerState.PLAYING -> {
                    binding.play.setImageResource(R.drawable.button_play_meditation)
                    sharedViewModel.pausePlayer()
                }
                else -> {
                    throw Exception("Illegal state!: ".plus(sharedViewModel.playerState.value.toString()))
                }
            }
        }

        binding.stop.setOnClickListener {
            when(sharedViewModel.playerState.value) {
                MediaPlayerState.PLAYING -> {
                    sharedViewModel.stopPlayer()
                    binding.play.setImageResource(R.drawable.button_play_meditation)
                }
                MediaPlayerState.PAUSED -> {
                    sharedViewModel.stopPlayer()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(uri != null){
            sharedViewModel.setPlayerUri(uri!!)
        }
    }

    override fun onPause() {
        super.onPause()
        sharedViewModel.resetPlayer()
        binding.play.setImageResource(R.drawable.button_play_meditation)
    }
}