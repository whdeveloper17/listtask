package com.wilsonhernandez.listtask.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, val nameTask: String, val description: String, val image: ByteArray,val state: Boolean=false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaskEntity

        if (id != other.id) return false
        if (nameTask != other.nameTask) return false
        if (description != other.description) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + nameTask.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}