package com.topchu.recoverfrombreakup.presentation.tasks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.topchu.recoverfrombreakup.databinding.FragmentTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TasksViewModel by viewModels()

    private lateinit var recyclerViewAdapter: BasicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Kotlin syntax allow us to put callback after parameters
        // if it's suppose to be the last parameter of function

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val decoration = DividerItemDecoration(
            requireContext().applicationContext, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)
        recyclerViewAdapter = BasicAdapter()
        binding.recyclerView.adapter = recyclerViewAdapter

        viewModel.tasks.observe(viewLifecycleOwner) {
            if(it != null){
                recyclerViewAdapter.setTasks(it)
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }

    }

}