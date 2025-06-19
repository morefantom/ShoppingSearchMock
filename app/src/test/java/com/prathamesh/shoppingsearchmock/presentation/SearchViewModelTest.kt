package com.prathamesh.shoppingsearchmock.presentation

import androidx.lifecycle.viewModelScope
import com.prathamesh.shoppingsearchmock.domain.usecase.ISearchForQueryUseCase
import com.prathamesh.shoppingsearchmock.presentation.commons.usecase.IDebounceQueryUseCase
import com.prathamesh.shoppingsearchmock.presentation.search.intent.SearchAction
import com.prathamesh.shoppingsearchmock.presentation.search.intent.SearchEvent
import com.prathamesh.shoppingsearchmock.presentation.search.intent.SearchState
import com.prathamesh.shoppingsearchmock.presentation.search.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SearchViewModelTest {

    private val debounceQueryUseCase: IDebounceQueryUseCase = mock()
    private val searchForQueryUseCase: ISearchForQueryUseCase = mock()

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setUp() {
        whenever(debounceQueryUseCase.invoke(any(), any<Flow<String>>()))
            .thenAnswer { invocation ->
                @Suppress("UNCHECKED_CAST")
                invocation.arguments[1] as Flow<String>
            }

        whenever(searchForQueryUseCase.invoke(any()))
            .thenReturn(flowOf(emptyList()))

        searchViewModel = SearchViewModel(
            debounceQueryUseCase = debounceQueryUseCase,
            searchForQueryUseCase = searchForQueryUseCase
        )
    }

    @Test
    fun testInitialStateValue() {
        val (searchQuery, searchedList, isSearching) = searchViewModel.state.value

        Assert.assertTrue(searchQuery.isEmpty())
        Assert.assertTrue(searchedList.isEmpty())
        Assert.assertFalse(isSearching)
    }

    @Test
    fun testOnActionSearchQueryChangedWithBlankQuery() {
        val query = " "
        val action = SearchAction.SearchQueryChanged(query)

        searchViewModel.onAction(action)

        verify(debounceQueryUseCase).invoke(any(), any())
        verify(searchForQueryUseCase, never()).invoke(query)
    }

    @Test
    fun testOnActionSearchQueryChangedWithNonBlankQuery() {
        val query = "Cat"
        val action = SearchAction.SearchQueryChanged(query)

        whenever(searchForQueryUseCase.invoke(query))
            .thenReturn(flowOf(listOf(query)))

        val emissions = mutableListOf<SearchState>()
        val job = searchViewModel.state
            .onEach { emissions += it }
            .launchIn(searchViewModel.viewModelScope)

        searchViewModel.onAction(action)

        job.cancel()

        Assert.assertEquals(3, emissions.size)

        Assert.assertEquals(
            SearchState(searchQuery = "", searchedList = emptyList(), isSearching = false),
            emissions[0]
        )

        Assert.assertEquals(
            SearchState(searchQuery = query, searchedList = emptyList(), isSearching = true),
            emissions[1]
        )

        Assert.assertEquals(
            SearchState(searchQuery = query, searchedList = listOf(query), isSearching = false),
            emissions[2]
        )

        verify(debounceQueryUseCase).invoke(any(), any())
        verify(searchForQueryUseCase).invoke(query)
    }

    @Test
    fun testOnActionSearchedItemClicked() {
        val item = "Cat"
        val action = SearchAction.SearchedItemClicked(item)

        val events = mutableListOf<SearchEvent>()

        val job = searchViewModel.event
            .onEach { events += it }
            .launchIn(searchViewModel.viewModelScope)

        searchViewModel.onAction(action)

        job.cancel()

        Assert.assertEquals(
            SearchEvent.Navigation.ItemDetailScreen,
            events[0]
        )
    }
}