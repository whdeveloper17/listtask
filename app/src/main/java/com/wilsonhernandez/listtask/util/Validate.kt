package com.wilsonhernandez.listtask.util

import arrow.core.Either
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right

fun validateField(nameTask: String?, description: String?): Either<String, Unit> {
    return when {
        nameTask.isNullOrBlank() -> "El nombre de la tarea no puede estar vacío.".left()
        description.isNullOrBlank() -> "La descripción de la tarea no puede estar vacía.".left()
        else -> Unit.right()    }

}

fun isIdNullOrEmpty(id: String?): Boolean {
    return Option.fromNullable(id)
        .map { it.isEmpty() }
        .getOrElse { true }
}