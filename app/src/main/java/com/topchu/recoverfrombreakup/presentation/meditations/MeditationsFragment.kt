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
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
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

    private var mediaPlayer: SimpleExoPlayer? = null

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

//        mediaPlayer?.setMediaItem(MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"))
//        mediaPlayer?.prepare()
//        mediaPlayer?.play()

        viewModel.meditations.observe(viewLifecycleOwner) {
            viewPagerAdapter.meditationsList = it
            binding.viewPager.adapter = viewPagerAdapter
            if(args.meditationId != -1) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.viewPager.setCurrentItem(args.meditationId - 1, true)
                }, 500)
            }
        }

        sharedViewModel.uri.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()) {
                mediaPlayer?.setMediaItem(MediaItem.fromUri(it))
                sharedViewModel.statePlayerUriSet()
            }
        })

        sharedViewModel.playerCommand.observe(viewLifecycleOwner, {
            when(it){
                MediaPlayerCommand.START -> {
                    when(sharedViewModel.playerState.value){
                        MediaPlayerState.URI_SET -> {
                            sharedViewModel.statePlayerLoading()
                            mediaPlayer?.prepare()
                            sharedViewModel.statePlayerReady()
                            mediaPlayer?.play()
                            sharedViewModel.statePlayerPlaying()
                        }
                        MediaPlayerState.PAUSED, MediaPlayerState.READY -> {
                            mediaPlayer?.play()
                            sharedViewModel.statePlayerPlaying()
                        }
                    }
                }
                MediaPlayerCommand.PAUSE -> {
                    when(sharedViewModel.playerState.value) {
                        MediaPlayerState.PLAYING -> {
                            mediaPlayer?.pause()
                            sharedViewModel.statePlayerPaused()
                        }
                        MediaPlayerState.LOADING -> {
                            mediaPlayer?.stop()
                            sharedViewModel.statePlayerUriSet()
                        }
                        else -> {
                            throw Exception("Illegal state: TRYING TO PAUSE WHEN NOT PLAYING")
                        }
                    }
                }
                MediaPlayerCommand.STOP -> {
                    mediaPlayer?.pause()
                    mediaPlayer?.seekTo(0)
                    if(sharedViewModel.playerState.value == MediaPlayerState.PLAYING ||
                        sharedViewModel.playerState.value == MediaPlayerState.PAUSED) {
                        sharedViewModel.statePlayerReady()
                    } else {
                        sharedViewModel.statePlayerReleased()
                    }
                }
                MediaPlayerCommand.RESET -> {
                    mediaPlayer?.stop()
                    mediaPlayer?.clearMediaItems()
                    sharedViewModel.statePlayerInitialized()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if(mediaPlayer == null){
            mediaPlayer = SimpleExoPlayer.Builder(requireContext()).build()
            sharedViewModel.statePlayerInitialized()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}