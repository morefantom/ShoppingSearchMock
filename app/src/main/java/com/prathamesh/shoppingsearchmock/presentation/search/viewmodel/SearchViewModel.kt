package com.prathamesh.shoppingsearchmock.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathamesh.shoppingsearchmock.domain.usecase.ISearchForQueryUseCase
import com.prathamesh.shoppingsearchmock.presentation.commons.usecase.IDebounceQueryUseCase
import com.prathamesh.shoppingsearchmock.presentation.search.SearchConstants
import com.prathamesh.shoppingsearchmock.presentation.search.intent.SearchAction
import com.prathamesh.shoppingsearchmock.presentation.search.intent.SearchEvent
import com.prathamesh.shoppingsearchmock.presentation.search.intent.SearchState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val debounceQueryUseCase: IDebounceQueryUseCase,
    private val searchForQueryUseCase: ISearchForQueryUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _event: MutableSharedFlow<SearchEvent> = MutableSharedFlow()
    val event: SharedFlow<SearchEvent> = _event.asSharedFlow()

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    private var shouldSearchQueryPipelineStart = false

    private fun startSearchQueryPipeline()  {
        if (!shouldSearchQueryPipelineStart) {
            shouldSearchQueryPipelineStart = true

            debounceQueryUseCase
                .invoke(
                    timeoutMillis = SearchConstants.DEBOUNCE_TIME_IN_MILLIS,
                    query = _searchQuery,
                )
                .flatMapLatest { latestQuery ->
                    if (latestQuery.isBlank()) {
                        flowOf(emptyList())
                    } else {
                        searchForQueryUseCase.invoke(query = latestQuery)
                    }
                }
                .onEach { result ->
                    _state.update { currentState ->
                        currentState.copy(
                            searchQuery = _searchQuery.value,
                            searchedList = result,
                            isSearching = false,
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.SearchQueryChanged -> searchForQuery(action.query)
            is SearchAction.SearchedItemClicked -> openItemDetails()
        }
    }

    private fun searchForQuery(query: String) = viewModelScope.launch {
        _state.value = _state.value.copy(
            searchQuery = query,
            isSearching = true,
        )
        startSearchQueryPipeline()
        _searchQuery.value = query
    }

    private fun openItemDetails() = viewModelScope.launch {
        _event.emit(SearchEvent.Navigation.ItemDetailScreen)
    }
}