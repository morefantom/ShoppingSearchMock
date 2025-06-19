package com.prathamesh.shoppingsearchmock.domain.usecase

import kotlinx.coroutines.flow.Flow

interface ISearchForQueryUseCase {
    fun invoke(query: String): Flow<List<String>>
}