package com.example.todoapp

import android.content.Context
import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.theme.ToDoAppTheme
import com.example.todoapp.ui.theme.screem.AddItemScreen
import com.example.todoapp.ui.theme.screem.HomeScreen
import com.example.todoapp.ui.theme.screem.UpdateItemScreen

import com.example.todoapp.viewmodel.AddItemFragmentViewModal
import com.example.todoapp.viewmodel.TodoItemViewModel
import com.example.todoapp.viewmodel.UpdateItemFragmentViewModel


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

