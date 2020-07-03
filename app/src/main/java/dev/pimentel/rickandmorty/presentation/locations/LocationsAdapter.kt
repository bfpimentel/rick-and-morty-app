package dev.pimentel.rickandmorty.presentation.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.LocationsItemBinding
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsItem

class LocationsAdapter : ListAdapter<LocationsItem, LocationsAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LocationsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(
        private val binding: LocationsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: LocationsItem, position: Int) {
            binding.apply {
                type.text = location.type
                name.text = location.name

                val layoutParams = root.layoutParams as ViewGroup.MarginLayoutParams

                val biggerMargin = root.resources.getDimensionPixelSize(
                    R.dimen.common_item_bigger_margin
                )
                val smallerMargin = root.resources.getDimensionPixelSize(
                    R.dimen.common_item_smaller_margin
                )

                if (position % 2 == 0) {
                    layoutParams.marginStart = biggerMargin
                    layoutParams.marginEnd = smallerMargin
                } else {
                    layoutParams.marginStart = smallerMargin
                    layoutParams.marginEnd = biggerMargin
                }

                layoutParams.topMargin = biggerMargin

                if (position >= itemCount - 2) {
                    layoutParams.bottomMargin = biggerMargin
                } else {
                    layoutParams.bottomMargin = 0
                }
            }
        }
    }

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LocationsItem>() {
            override fun areItemsTheSame(
                oldItem: LocationsItem,
                newItem: LocationsItem
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: LocationsItem,
                newItem: LocationsItem
            ) = oldItem.id == newItem.id
        }
    }
}
