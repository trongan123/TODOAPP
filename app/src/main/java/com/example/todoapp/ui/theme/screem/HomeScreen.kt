package com.example.todoapp.ui.theme.screem


import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.lifecycle.LifecycleOwner
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.MainViewModel
import com.example.todoapp.viewmodel.TodoItemViewModel

@Composable
fun HomeScreen(
    owner: LifecycleOwner,
    openAddItemScreen: () -> Unit,
    openUpdateItemScreen: () -> Unit,
    viewModel: TodoItemViewModel,
    viewModelLoad: MainViewModel
) {

    var items by remember { mutableStateOf(ArrayList<TodoItem>()) }
    viewModel.getStringMutableLiveData().observe(owner) { s: String ->
        viewModel.getAllList(viewModel.stringMutableLiveData.value)
            .observe(owner) { item: List<TodoItem> ->
                items = item as ArrayList<TodoItem>
            }
    }
    CompositionLocalProvider(localListTodo provides appListTodo(todolist =items)) {
        Column() {
            TabScreen(openAddItemScreen = {
                openAddItemScreen()
            }, openUpdateItemScreen = {
                openUpdateItemScreen()
            },
                viewModel = viewModel,
                owner = owner,
                viewModelLoad = viewModelLoad

            )
        }

    }
}