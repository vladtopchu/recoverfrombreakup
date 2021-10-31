package com.topchu.recoverfrombreakup.presentation.tasks.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.databinding.FragmentTaskBinding
import com.topchu.recoverfrombreakup.presentation.MainActivity
import com.topchu.recoverfrombreakup.utils.ParagraphAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskFragment : Fragment() {
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()

    @Inject
    lateinit var navOptions: NavOptions

    private lateinit var recyclerViewAdapter: ParagraphAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewAdapter = ParagraphAdapter()
        binding.recyclerView.adapter = recyclerViewAdapter

        viewModel.task.observe(viewLifecycleOwner, {
            if(it != null){
                binding.content.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE

                binding.title.text = it.title
                binding.subtitle.text = it.subtitle
                recyclerViewAdapter.setTexts(it.texts.map { text ->
                    getString(text)
                })
                recyclerViewAdapter.notifyDataSetChanged()
                if(it.hasMeditation){
                    val medId = it.meditationId
                    binding.toMeditation.visibility = View.VISIBLE
                    binding.toMeditation.setOnClickListener {
                        if (medId != null) {
                            (requireActivity() as MainActivity).toggleButton(R.id.meditationsFragment)
//                            (requireActivity() as MainActivity).currentFragment = R.id.meditationsFragment
                            findNavController().navigate(TaskFragmentDirections.actionTaskFragmentToMeditationsFragment(medId.toInt()), navOptions)
                        }
                    }
                }
            }
        })

    }

}