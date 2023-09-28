package com.wilsonhernandez.listtask.domain

import arrow.core.Either
import arrow.core.right
import arrow.core.left
import com.wilsonhernandez.listtask.common.TaskModel
import com.wilsonhernandez.listtask.data.repository.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddTaskCaseUse(private val repository: DatabaseRepository){


    suspend operator fun invoke(taskModel: TaskModel): Either<Throwable,Unit>{
        return withContext(Dispatchers.IO){
            try {
                repository.addTask(taskModel)
                Either.Right(Unit)
            }catch (e:Exception){
                Either.Left(e)
            }
        }
    }
}