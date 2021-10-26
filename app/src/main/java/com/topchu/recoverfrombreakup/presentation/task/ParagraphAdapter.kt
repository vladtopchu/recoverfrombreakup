package com.topchu.recoverfrombreakup.presentation.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.topchu.recoverfrombreakup.databinding.ItemParagraphBinding

class ParagraphAdapter(): RecyclerView.Adapter<ParagraphAdapter.ParagraphViewHolder>() {

    private var textList: List<String>? = null

    fun setTexts(textList: List<String>?) {
        this.textList = textList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParagraphViewHolder {
        lateinit var taskViewHolder: ParagraphViewHolder
        val binding = ItemParagraphBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        taskViewHolder = ParagraphViewHolder(binding).apply {
            setIsRecyclable(false)
        }
        return taskViewHolder
    }

    override fun onBindViewHolder(holder: ParagraphViewHolder, position: Int) {
        holder.bind(textList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(textList == null) return 0
        return textList?.size!!
    }

    class ParagraphViewHolder(private val binding: ItemParagraphBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(paragraph: String){
            binding.paragraph.text = paragraph
        }
    }
}