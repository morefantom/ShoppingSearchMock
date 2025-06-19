package com.prathamesh.shoppingsearchmock.domain

import com.prathamesh.shoppingsearchmock.data.remote.IItemApiService
import com.prathamesh.shoppingsearchmock.domain.repository.ItemRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ItemRepositoryTest {

    val mockItemApiService: IItemApiService = mock()

    val itemRepository = ItemRepository(mockItemApiService)

    @Test
    fun testGetAll() : Unit = runTest {
        val itemList = listOf("Cat", "Dog", "Mouse")

        whenever(mockItemApiService.fetchAll())
            .thenReturn(flowOf(itemList))

        val result = itemRepository.getAll().first()

        Assert.assertEquals(itemList, result)
        verify(mockItemApiService).fetchAll()
    }

    @Test
    fun testGetByNameEqual() : Unit = runTest {
        val item = "Foo"
        val query = item

        whenever(mockItemApiService.fetchByNameEqual(query))
            .thenReturn(flowOf(item))

        val result = itemRepository.getByNameEqual(query).first()

        Assert.assertEquals(item, result)
        verify(mockItemApiService).fetchByNameEqual(query)
    }

    @Test
    fun testGetByNameSimilar() : Unit = runTest {
        val item = "Foo"
        val query = item.substring(0 until item.length / 2)

        whenever(mockItemApiService.fetchByNameSimilar(query))
            .thenReturn(flowOf(listOf(item)))

        val result = itemRepository.getByNameSimilar(query).first()

        Assert.assertEquals(listOf(item), result)
        verify(mockItemApiService).fetchByNameSimilar(query)
    }
}