package com.prathamesh.shoppingsearchmock.presentation.search.intent

sealed class SearchAction {
    data class SearchQueryChanged(val query: String) : SearchAction()
    data class SearchedItemClicked(val item: String): SearchAction()
}
