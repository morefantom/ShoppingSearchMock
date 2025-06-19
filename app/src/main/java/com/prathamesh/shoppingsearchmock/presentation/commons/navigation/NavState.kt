package com.prathamesh.shoppingsearchmock.presentation.commons.navigation

enum class NavRoute {
    SEARCH,
    ITEMDETAILS,
}

sealed class NavState(
    val route: String,
) {
    object Search : NavState(NavRoute.SEARCH.name)
    object ItemDetails : NavState(NavRoute.ITEMDETAILS.name)

    companion object {
        val START = Search.route
    }
}