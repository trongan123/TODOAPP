package com.example.todoapp.repository

import android.app.Application
import android.util.Log
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
    private final var mTodoItemDAO: TodoItemDAO = db.todoItemDAO()




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
       // Log.e("Log", "page: "+page+"|pageSize:"+ pageSize )
        var list = mTodoItemDAO.resultlistTodoItem
        val startingIndex = page * pageSize
        Log.e("Log", "page: "+page+"|pageSize:"+ pageSize+" |list.size:"+ list.size+ "|startingIndex:"+startingIndex )

        return if (startingIndex + pageSize <= mTodoItemDAO.resultlistTodoItem.size) {
            Result.success(
                list.slice(startingIndex until startingIndex + pageSize)
            )
        } else {
            Result.success(
                //emptyList()
                list.slice(startingIndex until list.size)
            )
        }
    }
}