package com.prathamesh.shoppingsearchmock.data

import com.prathamesh.shoppingsearchmock.data.remote.MockItemApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class MockItemApiServiceTest {

    val validMockItem = "Cat"
    val invalidMockItem = "Zebra"

    val mockItemList = listOf<String>(validMockItem, "Dog", "Mouse")
    val mockItemApiService: MockItemApiService = MockItemApiService(mockItemList)

    @Test
    fun testFetchAllReturnItemList(): Unit = runTest {
        val result = mockItemApiService
            .fetchAll()
            .firstOrNull()

        Assert.assertEquals(mockItemList, result)
    }

    @Test
    fun testFetchByNameEqualIfNameValidReturnSameNamedItem(): Unit = runTest {
        val result = mockItemApiService
            .fetchByNameEqual(validMockItem)
            .firstOrNull()

        Assert.assertEquals(validMockItem, result)
    }

    @Test
    fun testFetchByNameEqualIfNameInValidReturnNull(): Unit = runTest {
        val result = mockItemApiService
            .fetchByNameEqual(invalidMockItem)
            .firstOrNull()

        Assert.assertNull(result)
    }

    @Test
    fun testFetchByNameSimilarIfNameValidReturnItemList(): Unit = runTest {
        val halfLengthValidMockItem = validMockItem.length / 2
        val subValidMockItem = validMockItem
            .substring(0 until halfLengthValidMockItem)

        val result = mockItemApiService
            .fetchByNameSimilar(subValidMockItem)
            .firstOrNull()

        Assert.assertNotNull(result)
    }

    @Test
    fun testFetchByNameSimilarIfNameInValidReturnEmptyList(): Unit = runTest {
        val halfLengthInvalidMockItem = invalidMockItem.length / 2
        val subInvalidMockItem = invalidMockItem
            .substring(0 until halfLengthInvalidMockItem)

        val result = mockItemApiService
            .fetchByNameSimilar(subInvalidMockItem)
            .first()

        Assert.assertTrue(result.isEmpty())
    }
}