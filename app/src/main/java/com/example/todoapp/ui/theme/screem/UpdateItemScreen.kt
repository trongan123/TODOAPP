package com.example.todoapp.ui.theme.screem

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.viewmodel.TodoItemViewModel

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun UpdateItemScreen(viewModel: TodoItemViewModel, backHome: () -> Unit){
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    titleItem = todoItem.title
    descriptionItem = todoItem.description
    statusItem = todoItem.status
    createdDateItem = dateFormat.format(todoItem.createdDate)
    completedDateItem = dateFormat.format(todoItem.completedDate)
    Column(modifier = Modifier.padding(30.dp)) {
        Texttitle()

        TextDescription()

        TextCreatedDate()

        TextCompletedDate()

        DropDownMenuStatus()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {

        DeleteButton(viewModel,
            backHome = {
                backHome()
            }
        )
        UpdateButton(viewModel,
            backHome = {
                backHome()
            }
        )

    }
}