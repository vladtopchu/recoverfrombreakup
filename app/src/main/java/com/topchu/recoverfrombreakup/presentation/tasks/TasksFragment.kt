package com.topchu.recoverfrombreakup.presentation.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.databinding.FragmentTasksBinding
import com.topchu.recoverfrombreakup.utils.SharedPref
import com.topchu.recoverfrombreakup.utils.toTimeObject
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TasksViewModel by viewModels()

    private lateinit var recyclerViewAdapter: TasksAdapter

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        if(!sharedPref.isHintHided()) {
            binding.hint.visibility = View.VISIBLE
            binding.hint.setOnClickListener {
                it.visibility = View.GONE
                sharedPref.hideHint()
            }
        }
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
        recyclerViewAdapter = TasksAdapter().apply {
            setOnItemClickListener(object : TasksAdapter.OnItemClickListener {
                override fun onItemClick(task: TaskEntity) {
                    if(task.isOpened){
                        findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToTaskFragment(task.id.toInt()))
                    } else if(task.isBlocked) {
                        Toast.makeText(requireContext(), "Приобретите полную версию для доступа к данному дню", Toast.LENGTH_LONG).show()
                    } else if(task.willOpenAt != null) {
                        Toast.makeText(requireContext(), task.willOpenAt.toTimeObject().toString() , Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), "День откроется после прохождения предыдущего", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        binding.recyclerView.adapter = recyclerViewAdapter

        viewModel.tasks.observe(viewLifecycleOwner) {
            if(it != null){
                recyclerViewAdapter.setTasks(it)
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }

}