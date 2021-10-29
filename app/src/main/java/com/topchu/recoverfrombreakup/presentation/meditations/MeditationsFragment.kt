package com.topchu.recoverfrombreakup.presentation.meditations

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
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

    private val args: MeditationsFragmentArgs by navArgs()

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
            if(args.meditationId != -1) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.viewPager.setCurrentItem(args.meditationId - 1, true)
                }, 500)
            }
        }

        sharedViewModel.playerCommand.observe(viewLifecycleOwner, {
            when(it) {
                MediaPlayerCommand.START -> {
                    Log.d("TESTTEST", "COMMAND START")
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
                    Log.d("TESTTEST", "COMMAND STOP")
                    sharedViewModel.statePlayerReleased()
                    mediaPlayer?.stop()
                }
                MediaPlayerCommand.RELEASE -> {
                    Log.d("TESTTEST", "COMMAND RELEASE")
                    sharedViewModel.statePlayerReleased()
                    mediaPlayer?.release()
                }
                MediaPlayerCommand.RESET -> {
                    Log.d("TESTTEST", "COMMAND RESET")
                    sharedViewModel.statePlayerReleased()
                    mediaPlayer?.reset()
                }
                MediaPlayerCommand.PAUSE -> {
                    Log.d("TESTTEST", "COMMAND PAUSE")
                    sharedViewModel.statePlayerPaused()
                    mediaPlayer?.pause()
                }
            }
        })

        sharedViewModel.uri.observe(viewLifecycleOwner, {
            if(it != null) {
                Log.d("TESTTEST", "URI NOT NULL")
                sharedViewModel.statePlayerLoading()
                mediaPlayer?.setDataSource(it)
                mediaPlayer?.prepareAsync()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        Log.d("TESTTEST", "meditationS PAUSE")
        sharedViewModel.clearPlayerUri()
        sharedViewModel.statePlayerReleased()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onResume() {
        super.onResume()
        Log.d("TESTTEST", "meditationS RESUME")
        mediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                Log.d("TESTTEST", "PLAYER PREPARED")
                sharedViewModel.statePlayerPlaying()
                it.start()
            }
        }
    }

}