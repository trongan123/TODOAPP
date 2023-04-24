package com.example.todoapp.repository

import android.app.Application
import com.example.todoapp.Database.TodoItemDAO
import com.example.todoapp.Database.TodoItemDatabase
import com.example.todoapp.model.TodoItem
import kotlinx.coroutines.delay

class Repository {

    private var mTodoItemDAO: TodoItemDAO

    constructor(application:Application) {
        val db = TodoItemDatabase.getInstance(application)
        mTodoItemDAO = db.todoItemDAO()
    }

    suspend fun getItems(page: Int, pageSize: Int): Result<List<TodoItem>> {
        delay(2000L)
        val startingIndex = page * pageSize
        return if(startingIndex + pageSize <=  mTodoItemDAO.resultlistTodoItem.size) {
            Result.success(
                mTodoItemDAO.resultlistTodoItem.slice(startingIndex until startingIndex + pageSize)
            )
        } else Result.success(emptyList())
    }
}