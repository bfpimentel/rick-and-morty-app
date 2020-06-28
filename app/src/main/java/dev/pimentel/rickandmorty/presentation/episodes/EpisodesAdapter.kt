package dev.pimentel.rickandmorty.presentation.episodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.pimentel.rickandmorty.databinding.EpisodesItemBinding
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesItem

class EpisodesAdapter : ListAdapter<EpisodesItem, EpisodesAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            EpisodesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(
        private val binding: EpisodesItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: EpisodesItem, position: Int) {
            binding.apply {
                number.text = episode.number
                name.text = episode.name
                date.text = episode.airDate
                divider.isVisible = position != itemCount - 1
            }
        }
    }

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EpisodesItem>() {
            override fun areItemsTheSame(
                oldItem: EpisodesItem,
                newItem: EpisodesItem
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: EpisodesItem,
                newItem: EpisodesItem
            ) = oldItem.id == newItem.id
        }
    }
}
