package com.arvl.fasimagiland.ui.screen.liststory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arvl.fasimagiland.data.response.StoryResponseItem
import com.arvl.fasimagiland.databinding.ItemStoryBinding

class StoryAdapter(private val itemClickListener: (StoryResponseItem) -> Unit) :
    ListAdapter<StoryResponseItem, StoryAdapter.StoryViewHolder>(StoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val storyItem = getItem(position)
        holder.bind(storyItem)
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryResponseItem) {
            binding.tvStoryTitle.text = story.title
            binding.tvStoryDifficulty.text = story.difficulty
            binding.root.setOnClickListener {
                itemClickListener.invoke(story)
            }
        }
    }

    class StoryDiffCallback : DiffUtil.ItemCallback<StoryResponseItem>() {
        override fun areItemsTheSame(oldItem: StoryResponseItem, newItem: StoryResponseItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StoryResponseItem, newItem: StoryResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}

