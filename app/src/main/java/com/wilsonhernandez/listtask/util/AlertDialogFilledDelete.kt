@file:JvmName("AlertDialogInformationKt")

package com.wilsonhernandez.listtask.util

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AlertDialogFilledDelete(
    title:String,
    onConfirmation: () -> Unit,
    onClosed:()->Unit
) {
    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(true) }
            if (openDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    text = {
                        Text(title)
                    },
                    confirmButton = {
                        Button(

                            onClick = {
                                onConfirmation()
                            }) {
                            Text("Si")
                        }
                    },
                    dismissButton = {
                        Button(

                            onClick = {
                                onClosed.invoke()
                                openDialog.value=false
                            }) {
                            Text("No")
                        }
                    }

                    )
            }
        }

    }
}
@Preview
@Composable
fun AlertDialogDeletePreview(){
    AlertDialogFilledDelete("¿Esta seguro que desea eliminar la tarea?",{},{})
}