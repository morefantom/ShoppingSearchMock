package com.prathamesh.shoppingsearchmock.presentation.search.intent

data class SearchState(
    val searchQuery: String = "",
    val searchedList: List<String> = emptyList(),
    val isSearching: Boolean = false,
)
