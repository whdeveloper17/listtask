package com.wilsonhernandez.listtask.data.repository

import com.wilsonhernandez.listtask.common.TaskModel
import com.wilsonhernandez.listtask.data.database.dao.TaskDao
import com.wilsonhernandez.listtask.util.TaskMapper

class DatabaseRepositoryImpl(private val dao: TaskDao) :DatabaseRepository {
    override suspend fun addTask(taskModel: TaskModel) {
        val taskEntity = TaskMapper.fromTaskModel(taskModel)
        dao.insertTask(taskEntity)
    }

    override suspend fun getAll(): List<TaskModel> {
        val data = dao.getAllTasks()
        return TaskMapper.toTaskModelList(data)
    }

    override suspend fun filledTask(id: String) {
        dao.updateTaskStateToTrue(id.toLong())
    }

    override suspend fun deleteTask(id: String) {

        dao.deleteTaskById(id.toLong())
    }

}