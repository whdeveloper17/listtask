package com.wilsonhernandez.listtask.ui.states

import com.wilsonhernandez.listtask.common.TaskModel
import com.wilsonhernandez.listtask.common.UiHomeStatus
import com.wilsonhernandez.listtask.common.UiStatus

data class HomeState( val status: UiStatus = UiStatus.Loading,
                      val listTask: List<TaskModel> = emptyList())