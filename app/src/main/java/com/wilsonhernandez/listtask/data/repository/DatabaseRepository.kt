package com.wilsonhernandez.listtask.data.repository

import com.wilsonhernandez.listtask.common.TaskModel

interface DatabaseRepository {
    suspend fun addTask(taskModel: TaskModel)
    suspend fun getAll():List<TaskModel>
    suspend fun filledTask(id:String)
    suspend fun deleteTask(id:String)
}