package com.prathamesh.shoppingsearchmock.domain.repository

import com.prathamesh.shoppingsearchmock.data.remote.IItemApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class ItemRepository(
    private val itemApiService: IItemApiService,
): IItemRepository {
    override fun getAll(): Flow<List<String>> = itemApiService
        .fetchAll()
        .flowOn(Dispatchers.IO)

    override fun getByNameEqual(name: String) = itemApiService
        .fetchByNameEqual(name)
        .flowOn(Dispatchers.IO)

    override fun getByNameSimilar(name: String) = itemApiService
        .fetchByNameSimilar(name)
        .flowOn(Dispatchers.IO)
}