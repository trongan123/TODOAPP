package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.viewmodel.TodoItemViewModel

class MainLoadMostJetpackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewModel: TodoItemViewModel =
            ViewModelProvider(this).get(TodoItemViewModel::class.java)
        setContent {

        }
    }
}
