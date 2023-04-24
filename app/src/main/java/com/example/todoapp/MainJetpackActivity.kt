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
import com.example.todoapp.viewmodel.TodoItemViewModel


class MainJetpackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewModel: TodoItemViewModel =
            ViewModelProvider(this).get(TodoItemViewModel::class.java)
        setContent {
            MainApp(this, viewModel)
        }


    }
}

@Composable
fun MainApp(
    owner: LifecycleOwner,
    viewModel: TodoItemViewModel
) {
    val navController = rememberNavController()

    MaterialTheme() {
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
                    owner = owner
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

