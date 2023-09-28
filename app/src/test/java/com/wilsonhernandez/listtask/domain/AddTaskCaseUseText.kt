package com.wilsonhernandez.listtask.domain

import com.wilsonhernandez.listtask.common.TaskModel
import com.wilsonhernandez.listtask.data.repository.DatabaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class AddTaskCaseUseText {
    private lateinit var repository: DatabaseRepository
    private lateinit var useCase: AddTaskCaseUse

    @Before
    fun setUp() {
        repository = mock()
        useCase = AddTaskCaseUse(repository)
    }

    @Test
    fun `should add task to repository and return Right`() = runTest {
        val taskModel = TaskModel(id = "1", name = "Task 1", description = "Description 1", status = true)

        val result = useCase(taskModel)

        verify(repository).addTask(taskModel)
        assert(result.isRight())
    }

    @Test
    fun `should return Left when repository throws an exception`() = runTest {
        val taskModel = TaskModel(id = "2", name = "Task 2", description = "Description 2", status = true)

        val exception = RuntimeException("Simulated error")
        repository = mock {
            onBlocking { addTask(taskModel) } doThrow exception
        }

        useCase = AddTaskCaseUse(repository)

        val result = useCase(taskModel)

        verify(repository).addTask(taskModel)
        assert(result.isLeft())
        assert(result.fold({ it == exception }, { false }))
    }
}