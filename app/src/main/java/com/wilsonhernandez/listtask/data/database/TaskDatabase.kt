package com.wilsonhernandez.listtask.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wilsonhernandez.listtask.data.database.dao.TaskDao
import com.wilsonhernandez.listtask.data.database.entities.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract val dao : TaskDao

    companion object {
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            if (INSTANCE == null) {
                synchronized(TaskDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            TaskDatabase::class.java,
                            "task_database"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}

