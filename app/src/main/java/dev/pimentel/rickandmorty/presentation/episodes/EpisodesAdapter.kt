package dev.pimentel.rickandmorty.presentation.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.EpisodesGroupItemBinding
import dev.pimentel.rickandmorty.databinding.EpisodesItemBinding
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesItem

class EpisodesAdapter : ListAdapter<EpisodesItem, EpisodesAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun getItemViewType(position: Int): Int =
        if (getItem(position).id == EpisodesItem.GROUP_IDENTIFIER) GROUP_VIEW_TYPE
        else ITEM_VIEW_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        if (viewType == GROUP_VIEW_TYPE) {
            GroupViewHolder(
                EpisodesGroupItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ItemViewHolder(
                EpisodesItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    abstract inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(item: EpisodesItem, position: Int)
    }

    inner class GroupViewHolder(
        private val binding: EpisodesGroupItemBinding
    ) : ViewHolder(binding.root) {

        override fun bind(item: EpisodesItem, position: Int) {
            binding.apply {
                season.text = root.context.getString(R.string.episodes_item_group_title, item.name)
            }
        }
    }

    inner class ItemViewHolder(
        private val binding: EpisodesItemBinding
    ) : ViewHolder(binding.root) {

        override fun bind(item: EpisodesItem, position: Int) {
            binding.apply {
                number.text = item.number
                name.text = item.name
                date.text = item.airDate
                divider.isVisible = position != itemCount - 1
            }
        }
    }

    private companion object {
        const val GROUP_VIEW_TYPE = 1
        const val ITEM_VIEW_TYPE = 2

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
