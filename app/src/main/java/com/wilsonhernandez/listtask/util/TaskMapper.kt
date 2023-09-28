package com.wilsonhernandez.listtask.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.wilsonhernandez.listtask.common.TaskModel
import com.wilsonhernandez.listtask.data.database.entities.TaskEntity
import java.io.ByteArrayOutputStream

object TaskMapper{
    fun fromTaskModel(taskModel: TaskModel): TaskEntity {
        val stream = ByteArrayOutputStream()
        taskModel.image?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val imageByteArray = stream.toByteArray()

        return TaskEntity(
            nameTask = taskModel.name,
            description = taskModel.description,
            image = imageByteArray
        )
    }
    fun toTaskModelList(taskEntities: List<TaskEntity>): List<TaskModel> {
        return taskEntities.map { taskEntity ->
            val imageBitmap = BitmapFactory.decodeByteArray(taskEntity.image, 0, taskEntity.image.size)

            TaskModel(
                id = taskEntity.id.toString(),
                name = taskEntity.nameTask,
                description = taskEntity.description,
                image = imageBitmap,
                status = taskEntity.state
            )
        }
    }
    fun toTaskModel(taskEntity: TaskEntity): TaskModel {
        val imageBitmap = BitmapFactory.decodeByteArray(taskEntity.image, 0, taskEntity.image.size)

        return TaskModel(
            id = taskEntity.id.toString(),
            name = taskEntity.nameTask,
            description = taskEntity.description,
            image = imageBitmap,
            status = taskEntity.state

        )
    }
}