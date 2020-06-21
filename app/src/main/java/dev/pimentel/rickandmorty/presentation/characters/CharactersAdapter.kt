package dev.pimentel.rickandmorty.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersItemBinding
import dev.pimentel.rickandmorty.presentation.characters.data.CharacterDisplay

class CharactersAdapter : ListAdapter<CharacterDisplay, CharactersAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            CharactersItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(
        private val binding: CharactersItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: CharacterDisplay, position: Int) {
            binding.apply {
                image.load(character.image)
                status.text = character.status
                name.text = character.name

                val layoutParams = root.layoutParams as ViewGroup.MarginLayoutParams

                val biggerMargin = root.resources.getDimensionPixelSize(
                    R.dimen.characters_item_bigger_margin
                )
                val smallerMargin = root.resources.getDimensionPixelSize(
                    R.dimen.characters_item_smaller_margin
                )

                if (position % 2 == 0) {
                    layoutParams.marginStart = biggerMargin
                    layoutParams.marginEnd = smallerMargin
                } else {
                    layoutParams.marginStart = biggerMargin
                    layoutParams.marginEnd = smallerMargin
                }
            }
        }
    }

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CharacterDisplay>() {
            override fun areItemsTheSame(
                oldItem: CharacterDisplay,
                newItem: CharacterDisplay
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CharacterDisplay,
                newItem: CharacterDisplay
            ) = oldItem.id == newItem.id
        }
    }
}
