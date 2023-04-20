package com.example.todoapp

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.theme.ToDoAppTheme
import com.example.todoapp.ui.theme.screem.AddItemScreen
import com.example.todoapp.ui.theme.screem.HomeScreen
import com.example.todoapp.ui.theme.screem.list
import com.example.todoapp.viewmodel.AddItemFragmentViewModal
import com.example.todoapp.viewmodel.TodoItemViewModel

class MainJetpackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewModel: TodoItemViewModel =
            ViewModelProvider(this).get(TodoItemViewModel::class.java)
        var addviewModel: AddItemFragmentViewModal =
            ViewModelProvider(this).get(AddItemFragmentViewModal::class.java)

        viewModel.getStringMutableLiveData()
            .observe(this) { s: String ->
                viewModel.getAllList(viewModel.getStringMutableLiveData().value)
                    .observe(this) { item: List<TodoItem> ->
                        list = item

                        setContent {
                            MainApp(item, viewModel, addviewModel)
                        }
                    }
            }
    }
}


@Composable
fun MainApp(
    list: List<TodoItem>,
    viewModel: TodoItemViewModel,
    addviewModel: AddItemFragmentViewModal
) {
    val navController = rememberNavController()
    MaterialTheme() {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    openAddItemScreen = {
                        navController.navigate("additem")
                    },
                    list = list,
                    viewModel = viewModel
                )
            }
            composable("additem") {
                AddItemScreen(viewModel = addviewModel,
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


