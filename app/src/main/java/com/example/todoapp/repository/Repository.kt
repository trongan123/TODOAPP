package com.example.todoapp.repository

import android.app.Application
import com.example.todoapp.Database.TodoItemDAO
import com.example.todoapp.Database.TodoItemDatabase
import com.example.todoapp.model.TodoItem
import kotlinx.coroutines.delay
import java.util.*


data class ListItem(
    val title: String,
    val description: String
)

class Repository(private val application: Application) {


    val db = TodoItemDatabase.getInstance(application)
    private var mTodoItemDAO: TodoItemDAO = db.todoItemDAO()

    private var list = mTodoItemDAO.resultlistTodoItem
    private var list2 = list

    private fun getlist(): List<TodoItem> {
        val item: MutableList<TodoItem> = ArrayList()
        val n = 50
        for (i in 0 until n) {
            item.add(TodoItem("Pending : $i", "Renew", "pending", Date(), Date()))
        }
        for (i in 50..99) {
            item.add(TodoItem("Completed : $i", "Write", "completed", Date(), Date()))
        }
        return item
    }
    suspend fun getItems(page: Int, pageSize: Int): Result<List<TodoItem>> {
        delay(2000L)
        val startingIndex = page * pageSize
        return if (startingIndex + pageSize <= list.size) {
            Result.success(
                list.slice(startingIndex until startingIndex + pageSize)
            )
        } else {
            Result.success(emptyList())
        }
    }
}