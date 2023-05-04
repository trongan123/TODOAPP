package com.example.todoapp.repository

import android.app.Application
import android.util.Log
import com.example.todoapp.Database.TodoItemDAO
import com.example.todoapp.Database.TodoItemDatabase
import com.example.todoapp.model.TodoItem
import kotlinx.coroutines.delay



class Repository(application: Application) {

    private val db = TodoItemDatabase.getInstance(application)
    private var mTodoItemDAO: TodoItemDAO = db.todoItemDAO()

    suspend fun getItems(page: Int, pageSize: Int): Result<List<TodoItem>> {
        delay(2000L)
        var list = mTodoItemDAO.resultlistTodoItem
        val startingIndex = page * pageSize

//        Log.e("Log", "page: "+page+"|pageSize:"+ pageSize+" |list.size:"+ list.size+ "|startingIndex:"+startingIndex )

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