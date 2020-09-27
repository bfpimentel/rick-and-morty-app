package dev.pimentel.data.models

import dev.pimentel.domain.models.FilterModel

data class FilterModelImpl(
    override val value: String,
    override val type: FilterModel.Type
) : FilterModel
