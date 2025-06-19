package com.prathamesh.shoppingsearchmock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prathamesh.shoppingsearchmock.data.remote.MockItemApiService
import com.prathamesh.shoppingsearchmock.domain.repository.ItemRepository
import com.prathamesh.shoppingsearchmock.domain.usecase.SearchForQueryUseCase
import com.prathamesh.shoppingsearchmock.presentation.commons.navigation.NavState
import com.prathamesh.shoppingsearchmock.presentation.commons.theme.ShoppingSearchMockTheme
import com.prathamesh.shoppingsearchmock.presentation.commons.usecase.DebounceQueryUseCase
import com.prathamesh.shoppingsearchmock.presentation.itemdetails.view.ItemDetailsRoute
import com.prathamesh.shoppingsearchmock.presentation.search.view.SearchRoute
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val searchForQueryUseCase = SearchForQueryUseCase(
            itemRepository = ItemRepository(
                itemApiService = MockItemApiService(),
            ),
        )

        val debounceQueryUseCase = DebounceQueryUseCase()

        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()

            ShoppingSearchMockTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavState.START,
                        modifier = Modifier
                            .padding(innerPadding),
                    ) {
                        composable(NavState.Search.route) {
                            SearchRoute(
                                debounceQueryUseCase = debounceQueryUseCase,
                                searchForQueryUseCase = searchForQueryUseCase,
                                navigateTo = { navState ->
                                    navController.navigate(navState.route)
                                },
                                onError = { error ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(error)
                                    }
                                },
                            )
                        }
                        composable(NavState.ItemDetails.route) {
                            ItemDetailsRoute()
                        }
                    }
                }
            }
        }
    }
}