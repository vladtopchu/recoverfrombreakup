package com.topchu.recoverfrombreakup.presentation.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.topchu.recoverfrombreakup.databinding.FragmentTaskBinding
import com.topchu.recoverfrombreakup.presentation.tasks.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment() {
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()

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
        val decoration = DividerItemDecoration(
            requireContext().applicationContext, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)
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
                    binding.toMeditation.visibility = View.VISIBLE
                }
            }
        })

    }

}