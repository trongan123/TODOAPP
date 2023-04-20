package com.example.todoapp.ui.theme.screem


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.TodoItemViewModel

@Composable
fun HomeScreen(
    openAddItemScreen: () -> Unit,
    openUpdateItemScreen: () -> Unit,
    list: List<TodoItem>,
    viewModel: TodoItemViewModel
) {
    Column() {
        TabScreen(openAddItemScreen ={
            openAddItemScreen()
        },openUpdateItemScreen ={
            openUpdateItemScreen()
        },listAll = list,
            viewModel=viewModel
        )
    }

}