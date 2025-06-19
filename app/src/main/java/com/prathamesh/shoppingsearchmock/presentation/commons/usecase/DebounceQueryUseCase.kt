package com.prathamesh.shoppingsearchmock.presentation.commons.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

class DebounceQueryUseCase: IDebounceQueryUseCase {
    override fun invoke(
        timeoutMillis: Long,
        query: Flow<String>,
    ) = query
        .debounce(timeoutMillis)
        .distinctUntilChanged()
}