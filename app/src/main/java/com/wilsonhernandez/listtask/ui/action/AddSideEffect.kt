package com.wilsonhernandez.listtask.ui.action

sealed class AddSideEffect {

    object SuccessAdd : AddSideEffect()
    object Error : AddSideEffect()

}