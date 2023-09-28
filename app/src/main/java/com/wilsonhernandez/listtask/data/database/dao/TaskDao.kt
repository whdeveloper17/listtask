package com.wilsonhernandez.listtask.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wilsonhernandez.listtask.data.database.entities.TaskEntity

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM task")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("UPDATE task SET state = 1 WHERE id = :taskId")
    suspend fun updateTaskStateToTrue(taskId: Long)

    @Query("DELETE FROM task WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)
}