package com.prathamesh.shoppingsearchmock.domain

import com.prathamesh.shoppingsearchmock.domain.repository.IItemRepository
import com.prathamesh.shoppingsearchmock.domain.usecase.SearchForQueryUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SearchForQueryUseCaseTest {

    val mockItemRepository: IItemRepository = mock()
    val searchForQueryUseCase = SearchForQueryUseCase(mockItemRepository)

    @Test
    fun testInvokeIfQueryIsEmptyReturnEmptyList(): Unit = runTest {
        val query = ""

        whenever(mockItemRepository.getByNameSimilar(query))
            .thenReturn(flowOf(emptyList()))

        val result = searchForQueryUseCase
            .invoke(query)
            .first()

        Assert.assertTrue(result.isEmpty())
        verify(mockItemRepository).getByNameSimilar(query)
    }

    @Test
    fun testInvokeIfQueryIsBlankReturnEmptyList(): Unit = runTest {
        val query = " "

        whenever(mockItemRepository.getByNameSimilar(query))
            .thenReturn(flowOf(emptyList()))

        val result = searchForQueryUseCase
            .invoke(query)
            .first()

        Assert.assertTrue(result.isEmpty())
        verify(mockItemRepository).getByNameSimilar(query)
    }

    @Test
    fun testInvokeIfQueryIsLoweredCase(): Unit = runTest {
        val query = "Cat"

        whenever(mockItemRepository.getByNameSimilar(any()))
            .thenReturn(flowOf(emptyList()))

        searchForQueryUseCase.invoke(query).first()

        val captor = argumentCaptor<String>()
        verify(mockItemRepository).getByNameSimilar(captor.capture())

        Assert.assertEquals(query.lowercase(), captor.firstValue)
    }
}