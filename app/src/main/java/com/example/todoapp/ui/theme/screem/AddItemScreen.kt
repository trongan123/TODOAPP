package com.example.todoapp.ui.theme.screem

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.*
import com.example.todoapp.viewmodel.AddItemFragmentViewModal
import com.example.todoapp.viewmodel.TodoItemViewModel

@Composable
fun AddItemScreen(viewModel: TodoItemViewModel, backHome: () -> Unit) {
    titleItem = ""
    descriptionItem =""
    statusItem =""
    completedDateItem =""
    createdDateItem =""
    Column(modifier = Modifier.padding(30.dp)) {
        Texttitle()
        CommonSpace()
        TextDescription()
        CommonSpace()
        TextCreatedDate()
        CommonSpace()
        TextCompletedDate()
        CommonSpace()
        dropDownMenuStatus()

    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {

        ClearButton()
        AddButton(viewModel,
            backHome = {
                backHome()
            }
        )

    }
}