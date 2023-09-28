package com.wilsonhernandez.listtask.common

import android.graphics.Bitmap

data class TaskModel(val id:String?="", val name:String,val description:String,val image:Bitmap?=null,val status : Boolean=false)
