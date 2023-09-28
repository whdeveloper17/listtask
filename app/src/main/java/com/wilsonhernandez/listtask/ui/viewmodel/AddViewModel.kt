package com.wilsonhernandez.listtask.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.wilsonhernandez.listtask.common.TaskModel
import com.wilsonhernandez.listtask.common.UiStatus
import com.wilsonhernandez.listtask.domain.AddTaskCaseUse
import com.wilsonhernandez.listtask.ui.action.AddSideEffect
import com.wilsonhernandez.listtask.ui.states.AddState
import com.wilsonhernandez.listtask.util.validateField
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class AddViewModel(private val addTaskCaseUse: AddTaskCaseUse) :
    ContainerHost<AddState, AddSideEffect>, ViewModel(
) {
    override val container = container<AddState, AddSideEffect>(
        AddState()
    )

    fun addTask(nameTask: String, description: String, image: Bitmap?) = intent {
        reduce { state.copy(status = UiStatus.Loading) }
        val validation = validateField(nameTask = nameTask, description = description)
        val taskModel = TaskModel( name = nameTask, description = description, image = image)
        if (validation.isRight()) {
            if (addTaskCaseUse(taskModel).isRight()) {
                reduce { state.copy(status = UiStatus.Success) }
            } else {
                reduce { state.copy(status = UiStatus.Failed()) }
            }
        } else {
            reduce { state.copy(status = UiStatus.Failed()) }
        }
    }

    fun successAdd(){
        intent {
            postSideEffect(AddSideEffect.SuccessAdd)
        }
    }

    fun failedAdd(){
        intent {
            postSideEffect(AddSideEffect.Error)
        }
    }
}