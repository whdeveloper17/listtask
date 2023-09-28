package com.wilsonhernandez.listtask.common

import com.wilsonhernandez.listtask.ui.states.AddState

sealed class UiStatus{
    object Loading : UiStatus()
    object Success : UiStatus()
    data class Failed(val message: String = "") : UiStatus()
}
