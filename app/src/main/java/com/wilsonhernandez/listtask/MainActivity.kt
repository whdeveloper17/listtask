package com.wilsonhernandez.listtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wilsonhernandez.listtask.ui.action.AddSideEffect
import com.wilsonhernandez.listtask.ui.theme.ListTaskTheme
import com.wilsonhernandez.listtask.ui.view.AddScreen
import com.wilsonhernandez.listtask.ui.view.HomeScreen
import com.wilsonhernandez.listtask.ui.viewmodel.AddViewModel
import com.wilsonhernandez.listtask.ui.viewmodel.HomeViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = getComposeViewModel<AddViewModel>()
            val viewModelHome = getComposeViewModel<HomeViewModel>()
            val status by viewModel.collectAsState()
            val statusHome by viewModelHome.collectAsState()
            val navController = rememberNavController()
            ListTaskTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                status = statusHome,
                                onClickButtonAdd = {
                                    navController.navigate("add")
                                },
                                onClickButtonFilled = { id, filter ->
                                    viewModelHome.filledTask(
                                        id,
                                        filter
                                    )
                                },
                                onClickButtonDelete = { id, filter ->
                                    viewModelHome.deleteTask(
                                        id,
                                        filter
                                    )
                                },
                                onClickReload = {viewModelHome.reload(it)})
                        }
                        composable("add") {
                            viewModel.collectSideEffect {
                                when (it) {
                                    is AddSideEffect.SuccessAdd -> {
                                        viewModelHome.reload()
                                        navController.popBackStack()
                                    }

                                    else -> {}
                                }
                            }
                            AddScreen(status = status.status, onClickSuccess = {
                                viewModel.successAdd()
                            }, onClickButtonBack = {
                                navController.navigate("home")
                            }, add = { name, description, image ->
                                viewModel.addTask(
                                    nameTask = name,
                                    description = description,
                                    image = image
                                )
                            }, onClickFailed = {
                                viewModel.failedAdd()
                            })
                        }
                    }
                }
            }
        }
    }
}
