package com.wilsonhernandez.listtask.domain

import com.wilsonhernandez.listtask.common.TaskModel
import com.wilsonhernandez.listtask.data.repository.DatabaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetListTaskCaseUseTest {
    private lateinit var repository: DatabaseRepository
    private lateinit var useCase: GetListTaskCaseUse

    @Before
    fun setUp() {
        repository = mock()
        useCase = GetListTaskCaseUse(repository)
    }

    @Test
    fun `should return all tasks when filter is empty`() = runTest {
        val filter = ""

        val tasks = listOf(
            TaskModel(id = "1", name = "Task 1", description = "Description 1", status = true),
            TaskModel(id = "2", name = "Task 2", description = "Description 2", status = false)
        )

        whenever(repository.getAll()).thenReturn(tasks)

        val result = useCase(filter)

        assert(result == tasks)
    }

    @Test
    fun `should return completed tasks when filter is "COMPLETADOS"`() = runTest {
        val filter = "COMPLETADOS"

        val tasks = listOf(
            TaskModel(id = "1", name = "Task 1", description = "Description 1", status = true),
            TaskModel(id = "2", name = "Task 2", description = "Description 2", status = false)
        )

        whenever(repository.getAll()).thenReturn(tasks)

        val result = useCase(filter)

        assert(result == tasks.filter { it.status })
    }

    @Test
    fun `should return pending tasks when filter is "PENDIENTES"`() = runTest {
        val filter = "PENDIENTES"

        val tasks = listOf(
            TaskModel(id = "1", name = "Task 1", description = "Description 1", status = true),
            TaskModel(id = "2", name = "Task 2", description = "Description 2", status = false)
        )

        whenever(repository.getAll()).thenReturn(tasks)

        val result = useCase(filter)

        assert(result == tasks.filter { !it.status })
    }
}