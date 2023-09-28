package com.wilsonhernandez.listtask.domain

import com.wilsonhernandez.listtask.common.TaskModel
import com.wilsonhernandez.listtask.data.repository.DatabaseRepository

class GetListTaskCaseUse (private val repository: DatabaseRepository) {
    suspend operator fun invoke(filter: String):List<TaskModel>{
        return if (filter.isEmpty()){
            repository.getAll()
        }else if (filter =="COMPLETADOS"){
            return repository.getAll().filter{it.status}

        }else return if (filter=="PENDIENTES"){
            repository.getAll().filter{!it.status}
        }else{
            repository.getAll()
        }
    }
}