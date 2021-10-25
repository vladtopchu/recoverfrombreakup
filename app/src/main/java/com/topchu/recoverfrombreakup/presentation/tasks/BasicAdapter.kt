package com.topchu.recoverfrombreakup.presentation.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.databinding.ItemTaskBinding

class BasicAdapter(): RecyclerView.Adapter<BasicAdapter.BasicViewHolder>() {

    private var tasksList: List<TaskEntity>? = null

    fun setTasks(tasksList: List<TaskEntity>?) {
        this.tasksList = tasksList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BasicViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BasicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BasicViewHolder, position: Int) {
        holder.bind(tasksList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(tasksList == null) return 0
        return tasksList?.size!!
    }

    class BasicViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskEntity){
            binding.title.text = task.title
            binding.subtitle.text = task.subtitle
//            binding.createdAt.text = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ROOT).format(Date(payment.created!! * 1000))
        }
    }
}