package com.wilsonhernandez.listtask.domain

import com.wilsonhernandez.listtask.data.repository.DatabaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class FilledTaskCaseUseTest {
    private lateinit var repository: DatabaseRepository
    private lateinit var useCase: FilledTaskCaseUse

    @Before
    fun setUp() {
        repository = mock()
        useCase = FilledTaskCaseUse(repository)
    }

    @Test
    fun `should fill task in repository and return Right`() = runTest {
        val taskId = "1"

        val result = useCase(taskId)

        verify(repository).filledTask(taskId)
        assert(result.isRight())
    }

    @Test
    fun `should return Left when repository throws an exception`() = runTest {
        val taskId = "2"

        val exception = RuntimeException("Simulated error")
        repository = mock {
            onBlocking { filledTask(taskId) } doThrow exception
        }

        useCase = FilledTaskCaseUse(repository)

        val result = useCase(taskId)

        verify(repository).filledTask(taskId)
        assert(result.isLeft())
        assert(result.fold({ it == exception }, { false }))
    }
}