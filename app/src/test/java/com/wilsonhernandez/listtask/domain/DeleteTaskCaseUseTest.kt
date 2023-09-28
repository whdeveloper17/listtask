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
class DeleteTaskCaseUseTest {
    private lateinit var repository: DatabaseRepository
    private lateinit var useCase: DeleteTaskCaseUse

    @Before
    fun setUp() {
        repository = mock()
        useCase = DeleteTaskCaseUse(repository)
    }

    @Test
    fun `should delete task from repository and return Right`() = runTest {
        val taskId = "1"

        val result = useCase(taskId)

        verify(repository).deleteTask(taskId)
        assert(result.isRight())
    }

    @Test
    fun `should return Left when repository throws an exception`() = runTest {
        val taskId = "2"

        val exception = RuntimeException("Simulated error")
        repository = mock {
            onBlocking { deleteTask(taskId) } doThrow exception
        }

        useCase = DeleteTaskCaseUse(repository)

        val result = useCase(taskId)

        verify(repository).deleteTask(taskId)
        assert(result.isLeft())
        assert(result.fold({ it == exception }, { false }))
    }
}