package com.target.supermarket.domain.models

data class SearchResult(
    val depts:List<Tag> = emptyList(),
    val products:List<LocalProduct> = emptyList()
)
