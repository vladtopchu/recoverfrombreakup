package com.topchu.recoverfrombreakup.presentation.tasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.topchu.recoverfrombreakup.data.local.daos.MeditationDao
import com.topchu.recoverfrombreakup.data.local.daos.TaskDao
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.databinding.FragmentTasksBinding
import com.topchu.recoverfrombreakup.di.ApplicationScope
import com.topchu.recoverfrombreakup.utils.SharedPref
import com.topchu.recoverfrombreakup.utils.toTimeObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TasksViewModel by viewModels()

    private lateinit var recyclerViewAdapter: TasksAdapter

    private var flag = false

    @Inject
    lateinit var navOptions: NavOptions

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var meditationDao: MeditationDao

    @ApplicationScope
    @Inject
    lateinit var applicationScope: CoroutineScope

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

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val decoration = DividerItemDecoration(
            requireContext().applicationContext, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)
        recyclerViewAdapter = TasksAdapter().apply {
            setOnItemClickListener(object : TasksAdapter.OnItemClickListener {
                override fun onItemClick(task: TaskEntity) {
                    if(task.isOpened){
                        findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToTaskFragment(task.id.toInt()), navOptions)
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

        viewModel.tasks.observe(viewLifecycleOwner) { tasksList ->
            if(tasksList != null){
                if(!flag){
                    tasksList.forEach { task ->
                        if(task.willOpenAt != null) {
                            if(task.willOpenAt < System.currentTimeMillis()) {
                                applicationScope.launch {
                                    taskDao.openTask(task.id.toInt())
                                    if(task.meditationId != null){
                                        meditationDao.openMeditationById(task.meditationId.toInt())
                                    }
                                    viewModel.updateTasks()
                                }
                            }
                        }
                    }
                    flag = true
                }
                recyclerViewAdapter.setTasks(tasksList)
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        flag = false
        viewModel.updateTasks()
    }
}