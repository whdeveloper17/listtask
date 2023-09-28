package com.wilsonhernandez.listtask.common

data class UiHomeStatus(
    val status: UiStatus = UiStatus.Loading,
    val listTask: List<TaskModel> = emptyList()
)