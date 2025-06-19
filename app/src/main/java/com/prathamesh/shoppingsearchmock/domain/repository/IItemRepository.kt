package com.prathamesh.shoppingsearchmock.domain.repository

import kotlinx.coroutines.flow.Flow

interface IItemRepository {
    fun getAll(): Flow<List<String>>
    fun getByNameEqual(name: String): Flow<String?>
    fun getByNameSimilar(name: String): Flow<List<String>>
}