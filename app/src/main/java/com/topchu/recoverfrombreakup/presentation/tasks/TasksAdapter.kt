package com.topchu.recoverfrombreakup.presentation.tasks

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.databinding.ItemTaskBinding

class TasksAdapter(): RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    private var _tasksList: List<TaskEntity>? = null
    fun setTasks(tasksList: List<TaskEntity>?) {
        this._tasksList = tasksList
    }

    private lateinit var _listener: OnItemClickListener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        _listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TasksViewHolder {
        lateinit var taskViewHolder: TasksViewHolder
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        taskViewHolder = TasksViewHolder(binding, _listener).apply {
            setIsRecyclable(false)
        }
        return taskViewHolder
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(_tasksList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(_tasksList == null) return 0
        return _tasksList?.size!!
    }

    class TasksViewHolder(private val binding: ItemTaskBinding, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskEntity){
            binding.itemTask.setOnClickListener {
                listener.onItemClick(task)
            }
            if(task.isBlocked){
                binding.iconTaskState.setImageResource(R.drawable.ic_lock)
            } else {
                if(task.isActive){
                    binding.iconTaskState.setImageResource(R.drawable.ic_active)
                    binding.title.typeface = Typeface.DEFAULT_BOLD
                    binding.subtitle.typeface = Typeface.DEFAULT_BOLD
                } else if(task.isOpened){
                    binding.iconTaskState.setImageResource(R.drawable.ic_done)
                }
                if(task.willOpenAt != null){
                    binding.iconTaskState.setImageResource(R.drawable.ic_clock)
                }
            }
            binding.title.text = task.title
            binding.subtitle.text = task.subtitle
//            binding.createdAt.text = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ROOT).format(Date(payment.created!! * 1000))
        }
    }


    interface OnItemClickListener {
        fun onItemClick(task: TaskEntity)
    }
}