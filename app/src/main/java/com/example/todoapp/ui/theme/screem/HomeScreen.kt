package com.example.todoapp.ui.theme.screem


import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.TodoItemViewModel

@Composable
fun HomeScreen(
    owner: LifecycleOwner,
    openAddItemScreen: () -> Unit,
    openUpdateItemScreen: () -> Unit,

    viewModel: TodoItemViewModel
) {
    Column() {
        TabScreen(openAddItemScreen ={
            openAddItemScreen()
        },openUpdateItemScreen ={
            openUpdateItemScreen()
        },
            viewModel=viewModel,

            owner =owner

        )
    }

}