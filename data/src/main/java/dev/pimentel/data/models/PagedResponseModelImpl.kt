package dev.pimentel.data.models

import dev.pimentel.domain.models.PagedResponseModel

data class PagedResponseModelImpl<T>(
    override val pages: Int,
    override val results: List<T>
) : PagedResponseModel<T>
