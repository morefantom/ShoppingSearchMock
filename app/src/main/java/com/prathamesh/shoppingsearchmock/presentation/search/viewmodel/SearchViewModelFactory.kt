package com.prathamesh.shoppingsearchmock.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prathamesh.shoppingsearchmock.domain.usecase.ISearchForQueryUseCase
import com.prathamesh.shoppingsearchmock.presentation.commons.usecase.IDebounceQueryUseCase

class SearchViewModelFactory(
    private val debounceQueryUseCase: IDebounceQueryUseCase,
    private val searchForQueryUseCase: ISearchForQueryUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(
                debounceQueryUseCase = debounceQueryUseCase,
                searchForQueryUseCase = searchForQueryUseCase,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}