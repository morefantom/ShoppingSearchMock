package com.prathamesh.shoppingsearchmock.presentation.search.intent

sealed class SearchEvent {
    sealed class Navigation(
    ): SearchEvent() {
        object ItemDetailScreen: Navigation()
    }
    data class Error(
        val message: String,
    ): SearchEvent()
}