package com.wilsonhernandez.listtask.ui.view

import AlertDialogInformation
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.wilsonhernandez.listtask.ui.theme.Purple40
import com.wilsonhernandez.listtask.R
import com.wilsonhernandez.listtask.common.UiStatus
import com.wilsonhernandez.listtask.ui.theme.Purple80
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(status: UiStatus?, onClickSuccess: () -> Unit, onClickButtonBack: () -> Unit,add:(String,String,Bitmap?)->Unit,onClickFailed: () -> Unit) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onClickButtonBack.invoke() }) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Atras")
                    }
                },
                title = {
                    Text(text = "Crear tareas")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
            )
        },
    ) {

        FormAddTask(){ name,description,image->
            add.invoke(name,description,image)
        }
        when(status){
            is UiStatus.Loading ->{
                BlockingCircularProgress()
            }
            is UiStatus.Success ->{
                AlertDialogInformation(title = "Registro exitoso", message = "Su tarea se encuetra en base de datos ya podra visualizarla") {
                    onClickSuccess.invoke()
                }
            }
            is UiStatus.Failed->{
                AlertDialogInformation(title = "Ocurrio", message = "No puede dejar campos vacios") {
                    onClickFailed.invoke()
                }
            }

            else -> {}
        }
    }
}

@Composable
fun FormAddTask(add:(String,String,Bitmap?)->Unit){
    var name by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    var image by remember {
        mutableStateOf<Bitmap?>(null)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        ImageItem(){value->
            image=value
        }
        TextField(value = name, onTextChanged = {value->
            name =value

        })
        DescriptionOutlinedTextField(value = description, onValueChange = {value->
            description=value
        })
        Spacer(modifier = Modifier.height(50.dp))
        ButtonAction(enabled = true) {
          add( name, description,image)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddScreenPreview() {
   /* val container: Container<AddState, AddASideEffect> ?= null
    val addTaskCaseUse :AddTaskCaseUse? = null
    val viewModel = AddViewModel(addTaskCaseUse!!)
    AddScreen(viewModel) {

    }*/
}

@Composable
fun ImageItem( onImageChanged: (Bitmap) -> Unit) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageUri!=null){
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri!!)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = "Image item",
                    modifier = Modifier.size(200    .dp)
                )
                onImageChanged.invoke(btm)
            }

        }else{
            Image(painter = painterResource(id = R.drawable.icon_galery), modifier = Modifier.size(200.dp), contentDescription = "Image item")
        }

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Selecionar imagen")
        }
    }

}
@Preview(showBackground = true)
@Composable
fun ImageItemPreview(){
    ImageItem(){

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(value: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { onTextChanged.invoke(it) },
        label = { Text("Nombre de la tarea") },
        maxLines = 1,
        textStyle = TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.icon_item),
                contentDescription = ""
            )
        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
    )

}

@Preview(showBackground = true)
@Composable
fun TextFieldPreview(){
    TextField(value = "", onTextChanged = {})
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DescriptionOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf(value) }

    val density = LocalDensity.current.density
    val textHeight = (24.dp.value * density).roundToInt()

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        label = { Text("Descripción") },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = (3 * textHeight).dp)
            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        shape = RectangleShape,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray
        )
    )
}

@Preview(showBackground = true)
@Composable
fun DescriptionOutlinedTextFieldPreview(){
    DescriptionOutlinedTextField(value = "", onValueChange = {})
}

@Composable
fun ButtonAction(enabled: Boolean, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = { onClick.invoke() }, enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple40,
                disabledContainerColor = Purple80
            ),
        )

        {
            Text(text = "Crear tarea", color = Color.White)
        }
    }

}
@Preview(showBackground = true)
@Composable
fun ButtonActionPreview(){
    ButtonAction(enabled = true) {
        
    }
}

@Composable
fun BlockingCircularProgress() {
        var isShowingDialog by remember { mutableStateOf(true) }

        DisposableEffect(true) {
            onDispose {
                isShowingDialog = false
            }
        }

        if (isShowingDialog) {
            Dialog(
                onDismissRequest = { /* Handle dialog dismissal if needed */ },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

}