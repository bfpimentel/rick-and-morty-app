package dev.pimentel.data.models

import dev.pimentel.domain.models.CharacterModel

data class CharacterModelImpl(
    override val id: Int,
    override val name: String,
    override val status: String,
    override val image: String
) : CharacterModel
