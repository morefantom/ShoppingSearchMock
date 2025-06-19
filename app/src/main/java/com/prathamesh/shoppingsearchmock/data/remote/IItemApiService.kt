package com.prathamesh.shoppingsearchmock.data.remote

import kotlinx.coroutines.flow.Flow

interface IItemApiService {
    fun fetchAll(): Flow<List<String>>
    fun fetchByNameEqual(name: String): Flow<String?>
    fun fetchByNameSimilar(name: String): Flow<List<String>>
}