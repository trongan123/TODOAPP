package com.example.todoapp.ui.theme.screem

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.*
import com.example.todoapp.viewmodel.TodoItemViewModel

import com.example.todoapp.viewmodel.UpdateItemFragmentViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.text.DateFormat
import java.text.SimpleDateFormat

@Composable
fun UpdateItemScreen(viewModel: TodoItemViewModel, backHome: () -> Unit){
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    titleItem = todoItem.title
    descriptionItem = todoItem.description
    statusItem = todoItem.status
    createdDateItem = dateFormat.format(todoItem.createdDate)
    completedDateItem = dateFormat.format(todoItem.completedDate)
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