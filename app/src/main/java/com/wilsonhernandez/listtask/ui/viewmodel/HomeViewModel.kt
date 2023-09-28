package com.wilsonhernandez.listtask.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.wilsonhernandez.listtask.common.UiStatus
import com.wilsonhernandez.listtask.domain.DeleteTaskCaseUse
import com.wilsonhernandez.listtask.domain.FilledTaskCaseUse
import com.wilsonhernandez.listtask.domain.GetListTaskCaseUse
import com.wilsonhernandez.listtask.ui.action.HomeSideEffect
import com.wilsonhernandez.listtask.ui.states.HomeState
import com.wilsonhernandez.listtask.util.isIdNullOrEmpty
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class HomeViewModel(val getListTaskCaseUse: GetListTaskCaseUse,val filledTaskCaseUse: FilledTaskCaseUse,val deleteTaskCaseUse: DeleteTaskCaseUse) :
    ContainerHost<HomeState, HomeSideEffect>, ViewModel() {
    override val container = container<HomeState, HomeSideEffect>(
        HomeState()
    )

    init {
        intent { getAll() }
    }

    private  fun getAll(filter:String="") {
        intent {
            reduce {
                state.copy(status = UiStatus.Loading, listTask = emptyList())
            }
            val list = getListTaskCaseUse.invoke(filter)
            if (list.isNotEmpty()) {
                reduce {
                    state.copy(status = UiStatus.Success, listTask = list)
                }
            } else {
                reduce {
                    state.copy(
                        status = UiStatus.Failed("Not Found"),
                        listTask = list
                    )
                }
            }
        }

    }

     fun reload(filter:String=""){
        getAll(filter)
    }

    fun filledTask(id:String,filter:String){
        intent {
            reduce {
                state.copy(status = UiStatus.Loading)
            }
            if (!isIdNullOrEmpty(id)){
                if (filledTaskCaseUse(id).isRight()){
                    reload(filter)
                    reduce { state.copy(status = UiStatus.Success) }
                }else{
                    reduce { state.copy(status = UiStatus.Failed()) }
                }
            }else{
                reduce { state.copy(status = UiStatus.Failed()) }
            }
        }
    }

    fun deleteTask(id:String,filter:String){
        intent {
            reduce {
                state.copy(status = UiStatus.Loading)
            }
            if (!isIdNullOrEmpty(id)){
                if (deleteTaskCaseUse(id).isRight()){
                    reload(filter)
                    reduce { state.copy(status = UiStatus.Success) }
                }else{
                    reduce { state.copy(status = UiStatus.Failed()) }
                }
            }else{
                reduce { state.copy(status = UiStatus.Failed()) }
            }
        }
    }
}