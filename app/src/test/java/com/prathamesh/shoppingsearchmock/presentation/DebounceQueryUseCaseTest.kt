package com.prathamesh.shoppingsearchmock.presentation

import com.prathamesh.shoppingsearchmock.presentation.commons.usecase.DebounceQueryUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class DebounceQueryUseCaseTest {

    val debounceQueryUseCase = DebounceQueryUseCase()

    @Test
    fun testInvokeDebounce() = runTest {
        val timeoutMillis = 300L
        val item1 = "Cat"
        val item2 = "Dog"
        val item3 = "Mouse"
        val queryFlow = flow {
            emit(item1)
            delay(100L)
            emit(item2)
            delay(350L)
            emit(item3)
        }

        val result = debounceQueryUseCase
            .invoke(
                timeoutMillis = timeoutMillis,
                query = queryFlow,
            ).toList()

        Assert.assertEquals(listOf(item2, item3), result)
    }

    @Test
    fun testInvokeDistinct() = runTest {
        val timeoutMillis = 300L
        val item1 = "Cat"
        val item2 = "Dog"
        val item3 = "Mouse"
        val queryFlow = flow {
            emit(item1)
            delay(350L)
            emit(item2)
            delay(350L)
            emit(item2)
            delay(350L)
            emit(item2)
            delay(350L)
            emit(item3)
            delay(350L)
            emit(item3)
        }

        val result = debounceQueryUseCase
            .invoke(
                timeoutMillis = timeoutMillis,
                query = queryFlow,
            ).toList()

        Assert.assertEquals(listOf(item1, item2, item3), result)
    }
}