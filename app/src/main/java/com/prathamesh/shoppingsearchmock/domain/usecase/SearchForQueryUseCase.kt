package com.prathamesh.shoppingsearchmock.domain.usecase

import com.prathamesh.shoppingsearchmock.domain.repository.IItemRepository
import kotlinx.coroutines.flow.Flow

class SearchForQueryUseCase(
    private val itemRepository: IItemRepository,
): ISearchForQueryUseCase {
    override fun invoke(query: String): Flow<List<String>> = itemRepository
        .getByNameSimilar(query.lowercase())
}