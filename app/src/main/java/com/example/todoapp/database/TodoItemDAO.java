package com.example.todoapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoapp.model.TodoItem;

import java.util.List;

@Dao
public interface TodoItemDAO {
    @Insert
    void insertTodoItem(TodoItem todoItem);

    @Query("SELECT * FROM todoItem WHERE title LIKE '%' || :key || '%'")
    LiveData<List<TodoItem>> getListTodoItem(String key);

    @Query("SELECT * FROM todoItem ")
    List<TodoItem> getAllListTodoItem();

    @Query("SELECT * FROM todoItem WHERE status=:status AND title LIKE '%' || :key || '%'")
    LiveData<List<TodoItem>> getListTodoItemByStatus(String status, String key);

    @Query("SELECT * FROM todoItem WHERE title LIKE '%' || :key || '%'")
    List<TodoItem> getSearchTodoItem(String key);

    @Query("SELECT * FROM todoItem WHERE status=:status AND title LIKE '%' || :key || '%'")
    List<TodoItem> getSearchTodoItemByStatus(String status, String key);

    @Update
    void updateTodoItem(TodoItem item);

    @Delete
    void deleteTodoItem(TodoItem item);

    @Query("delete from todoItem where id in (:id)")
    void clearTodoItem(List<Integer> id);
}
