package com.prathamesh.shoppingsearchmock.presentation.commons.usecase

import kotlinx.coroutines.flow.Flow

interface IDebounceQueryUseCase {
    fun invoke(
        timeoutMillis: Long,
        query: Flow<String>,
    ): Flow<String>
}