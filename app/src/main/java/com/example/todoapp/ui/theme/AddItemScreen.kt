package com.example.todoapp.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.*
import com.example.todoapp.viewmodel.AddItemFragmentViewModal

@Composable
fun AddItemScreen(viewModel: AddItemFragmentViewModal){
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
    LayoutAddButton(viewModel)
}