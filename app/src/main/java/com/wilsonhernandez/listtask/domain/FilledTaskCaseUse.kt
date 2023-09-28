package com.wilsonhernandez.listtask.domain

import arrow.core.Either
import com.wilsonhernandez.listtask.data.repository.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilledTaskCaseUse(private val repository: DatabaseRepository) {
    suspend operator fun invoke(id:String): Either<Throwable, Unit> {
        return withContext(Dispatchers.IO) {
            try {
                repository.filledTask(id)
                Either.Right(Unit)
            } catch (e: Exception) {
                Either.Left(e)
            }
        }
    }
}