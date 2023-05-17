package com.example.todoapp.repository

import android.app.Application
import com.example.todoapp.Database.TodoItemDAO
import com.example.todoapp.Database.TodoItemDatabase
import com.example.todoapp.model.TodoItem
import kotlinx.coroutines.delay


class Repository(application: Application) {

    private val db = TodoItemDatabase.getInstance(application)
    private var mTodoItemDAO: TodoItemDAO = db.todoItemDAO()

    suspend fun getItems(page: Int, pageSize: Int): Result<List<TodoItem>> {
        delay(2000L)
        val list = mTodoItemDAO.allListTodoItem
        val startingIndex = page * pageSize

        return if (startingIndex + pageSize <= list.size) {
            Result.success(
                list.slice(startingIndex until startingIndex + pageSize)
            )
        } else {
            Result.success(
                list.slice(startingIndex until list.size)
            )
        }
    }
}