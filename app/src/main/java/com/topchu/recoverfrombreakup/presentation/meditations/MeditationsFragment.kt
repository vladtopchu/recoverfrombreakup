package com.topchu.recoverfrombreakup.presentation.meditations

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.topchu.recoverfrombreakup.databinding.FragmentMeditationsBinding
import com.topchu.recoverfrombreakup.utils.MediaPlayerCommand
import com.topchu.recoverfrombreakup.utils.MediaPlayerState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeditationsFragment : Fragment() {
    private var _binding: FragmentMeditationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MeditationsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var viewPagerAdapter: MeditationsAdapter

    var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeditationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = MeditationsAdapter(requireActivity())

        viewModel.meditations.observe(viewLifecycleOwner) {
            viewPagerAdapter.meditationsList = it
            binding.viewPager.adapter = viewPagerAdapter
        }

        sharedViewModel.playerCommand.observe(viewLifecycleOwner, {
            when(it) {
                MediaPlayerCommand.START -> {
                    when(sharedViewModel.playerState.value) {
                        MediaPlayerState.IDLE -> {
                            sharedViewModel.statePlayerLoading()
                            mediaPlayer?.prepareAsync()
                        }
                        MediaPlayerState.PAUSED -> {
                            sharedViewModel.statePlayerPlaying()
                            mediaPlayer?.start()
                        }
                        else -> {
                            throw Exception("WHAT THE ACTUAL FUCK")
                        }
                    }
                }
                MediaPlayerCommand.STOP -> {
                    sharedViewModel.statePlayerReleased()
                    mediaPlayer?.stop()
                }
                MediaPlayerCommand.RELEASE -> {
                    sharedViewModel.statePlayerReleased()
                    mediaPlayer?.release()
                }
                MediaPlayerCommand.RESET -> {
                    sharedViewModel.statePlayerReleased()
                    mediaPlayer?.reset()
                }
                MediaPlayerCommand.PAUSE -> {
                    sharedViewModel.statePlayerPaused()
                    mediaPlayer?.pause()
                }
            }
        })

        sharedViewModel.uri.observe(viewLifecycleOwner, {
            if(it != null) {
                sharedViewModel.statePlayerLoading()
                mediaPlayer?.setDataSource(it)
                mediaPlayer?.prepareAsync()
            }
        })


    }

    override fun onPause() {
        super.onPause()
        sharedViewModel.statePlayerReleased()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                sharedViewModel.statePlayerPlaying()
                it.start()
            }
        }
    }

}