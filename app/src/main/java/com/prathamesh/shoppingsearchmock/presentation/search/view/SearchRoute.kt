package com.prathamesh.shoppingsearchmock.presentation.search.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prathamesh.shoppingsearchmock.domain.usecase.ISearchForQueryUseCase
import com.prathamesh.shoppingsearchmock.presentation.commons.navigation.NavState
import com.prathamesh.shoppingsearchmock.presentation.commons.usecase.IDebounceQueryUseCase
import com.prathamesh.shoppingsearchmock.presentation.search.intent.SearchEvent
import com.prathamesh.shoppingsearchmock.presentation.search.viewmodel.SearchViewModel
import com.prathamesh.shoppingsearchmock.presentation.search.viewmodel.SearchViewModelFactory

@Composable
fun SearchRoute(
    debounceQueryUseCase: IDebounceQueryUseCase,
    searchForQueryUseCase: ISearchForQueryUseCase,
    navigateTo: (NavState) -> Unit,
    onError: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val viewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory(
            debounceQueryUseCase = debounceQueryUseCase,
            searchForQueryUseCase = searchForQueryUseCase,
        )
    )

    val state = viewModel.state.collectAsState()

    LaunchedEffect(viewModel.event) {
        lifecycleOwner
            .lifecycle
            .repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .event
                    .collect { event ->
                        when (event) {
                            is SearchEvent.Error -> onError(event.message)
                            SearchEvent.Navigation.ItemDetailScreen -> navigateTo(NavState.ItemDetails)
                        }
                    }
            }
    }

    SearchScreen(
        state = state.value,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}