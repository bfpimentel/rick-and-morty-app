package dev.pimentel.rickandmorty.presentation.characters.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.pimentel.rickandmorty.databinding.CharactersDetailsEpisodesItemBinding
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesItem

class CharactersDetailsEpisodesAdapter :
    ListAdapter<EpisodesItem, CharactersDetailsEpisodesAdapter.ViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            CharactersDetailsEpisodesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(
        private val binding: CharactersDetailsEpisodesItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EpisodesItem, position: Int) {
            binding.apply {
                number.text = item.number
                name.text = item.name
                date.text = item.airDate
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
