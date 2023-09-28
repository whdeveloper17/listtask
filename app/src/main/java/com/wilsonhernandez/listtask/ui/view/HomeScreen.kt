package com.wilsonhernandez.listtask.ui.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wilsonhernandez.listtask.R
import com.wilsonhernandez.listtask.common.UiStatus
import com.wilsonhernandez.listtask.ui.states.HomeState
import com.wilsonhernandez.listtask.ui.theme.Purple40
import com.wilsonhernandez.listtask.util.AlertDialogFilledDelete
enum class FilterOption {
    TODOS,
    COMPLETADOS,
    PENDIENTES
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    status: HomeState,
    onClickButtonAdd: () -> Unit,
    onClickButtonFilled: (String,String) -> Unit,
    onClickButtonDelete: (String,String) -> Unit,
    onClickReload:(String)->Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(FilterOption.TODOS) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Lista de tareas")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                actions = {
                    IconButton(
                        onClick = { expanded = true }
                    ) {
                        Icon(imageVector = Icons.Default.List, contentDescription = "Filter")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        FilterOption.values().forEach { option ->
                            DropdownMenuItem(
                                text = {Text(text = option.name)},
                                onClick = {
                                    expanded = false
                                    selectedOption=option
                                    onClickReload.invoke(selectedOption.name)
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onClickButtonAdd.invoke() }, shape = CircleShape) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar"
                )
            }
        }
    ) {
        val list = status.listTask
        val itemCount = list.size
       if (itemCount!=0){
           LazyColumn(
               contentPadding = PaddingValues(
                   top = 80.dp,
                   start = 5.dp,
                   end = 5.dp
               )
           ) {

               
               
               items(itemCount) { item ->
                   ColumnItem(
                       id = list[item].id!!,
                       painter = list[item].image,
                       title = list[item].name,
                       description = list[item].description,
                       status =  list[item].status,
                       onClickFilled = {
                           onClickButtonFilled.invoke(it,selectedOption.name)
                       },
                       onClickShowDelete = {
                           onClickButtonDelete.invoke(it,selectedOption.name)
                       }
                   )
               }
           }
       }else{
           Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
               Text(text = "No hay tareas registradas ")
           }
       }

        when (val status = status.status) {
            is UiStatus.Loading -> {
                BlockingCircularProgress()
            }
            else -> Unit
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    /*HomeScreen(){

    }*/
}


@Composable
fun ColumnItem(
    id: String,
    painter: Bitmap?,
    title: String,
    description: String,
    status:Boolean,
    onClickFilled: (String) -> Unit,
    onClickShowDelete: (String) -> Unit
) {
    val openAlertDialogFilled = remember { mutableStateOf(false) }
    val openAlertDialogDelete = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            if (painter != null) {
                val imageBitmap = painter.asImageBitmap()

                Image(
                    bitmap = imageBitmap,
                    contentDescription = title,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.icon_galery),
                    modifier = Modifier.size(100.dp),
                    contentDescription = "Image item"
                )
            }


            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
                Text(
                    text = description,
                    fontSize = 15.sp,
                    modifier = Modifier,
                    maxLines = 3
                )

                Box(
                    contentAlignment = Alignment.BottomCenter, modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Box(
                        contentAlignment = Alignment.BottomStart, modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Row() {
                            Text(text = "Completada : ", fontWeight = FontWeight.Bold)
                            Text(text = if (status) "Si" else "No")

                        }
                    }
                    Box(
                        contentAlignment = Alignment.BottomEnd, modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Row {
                           if (!status){
                               IconButton(onClick = { openAlertDialogFilled.value = true }) {
                                   Icon(
                                       imageVector = Icons.Outlined.Check,
                                       contentDescription = "Confirm",
                                       tint = Color.Green,
                                       modifier = Modifier.size(36.dp)
                                   )
                               }
                           }
                            IconButton(onClick = { openAlertDialogDelete.value = true }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                        }
                    }
                }

            }


        }
        if (openAlertDialogFilled.value) {
            AlertDialogFilledDelete(
                title = "¿Esta seguro que desea completar esta tarea?",
                onConfirmation = {
                    openAlertDialogFilled.value = false
                    onClickFilled.invoke(id)
                }) {
                openAlertDialogFilled.value = false
            }
        }
        if (openAlertDialogDelete.value) {
            AlertDialogFilledDelete(
                title = "¿Esta seguro que desea eliminar la tarea?",
                onConfirmation = {
                    openAlertDialogDelete.value = false
                    onClickShowDelete.invoke(id)
                }) {
                openAlertDialogDelete.value = false
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ColumnItemPreview() {
    ColumnItem(
        "",
        null,
        "Tarea 11",
        "desde el año 1500,",true, {}, {}
    )
}

