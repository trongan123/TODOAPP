package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.theme.screem.AddItemScreen
import com.example.todoapp.ui.theme.screem.HomeScreen
import com.example.todoapp.ui.theme.screem.UpdateItemScreen
import com.example.todoapp.viewmodel.MainViewModel
import com.example.todoapp.viewmodel.TodoItemViewModel


class MainJetpackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: TodoItemViewModel =
            ViewModelProvider(this)[TodoItemViewModel::class.java]
        val viewModelLoad: MainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

            setContent {
                MainApp(this, viewModel, viewModelLoad)
            }
        }
}
@Composable
fun MainApp(
    owner: LifecycleOwner,
    viewModel: TodoItemViewModel,
    viewModelLoad: MainViewModel
) {
    val navController = rememberNavController()
    MaterialTheme {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    openAddItemScreen = {
                        navController.navigate("additem")
                    },
                    openUpdateItemScreen = {
                        navController.navigate("updateitem")
                    },
                    viewModel = viewModel,
                    owner = owner,
                    viewModelLoad = viewModelLoad
                )
            }
            composable("additem") {
                AddItemScreen(viewModel = viewModel,
                    backHome = {
                        navController.popBackStack(
                            route = "home",
                            inclusive = false,
                            saveState = true
                        )
                    }
                )
            }
            composable("updateitem") {
                UpdateItemScreen(viewModel = viewModel,
                    backHome = {
                        navController.popBackStack(
                            route = "home",
                            inclusive = false,
                            saveState = true
                        )
                    }
                )
            }

        }
    }
}

