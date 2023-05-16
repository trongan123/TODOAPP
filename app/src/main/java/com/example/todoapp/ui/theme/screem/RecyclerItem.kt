package com.example.todoapp.ui.theme.screem

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.TodoItemViewModel

class MyComposeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AbstractComposeView(context, attrs) {

    var i : TodoItem = TodoItem()
    var todoViewModel :TodoItemViewModel? = null
    var view :View? = null
    @Composable
    override fun Content() {
        todoViewModel?.let { ItemListRecycle(i =i , view = view, viewModel = it, this) }
    }
}