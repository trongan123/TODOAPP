package com.example.todoapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.todoapp.database.TodoItemDAO;
import com.example.todoapp.database.TodoItemDatabase;
import com.example.todoapp.model.TodoItem;

import java.util.List;

public class TodoRepository {

    private final TodoItemDAO mTodoItemDAO;
    private LiveData<List<TodoItem>> mTodoItems;

    public TodoRepository(Application application) {
        TodoItemDatabase db = TodoItemDatabase.getInstance(application);
        mTodoItemDAO = db.todoItemDAO();
    }

    public LiveData<List<TodoItem>> getAllList(String key) {
        mTodoItems = mTodoItemDAO.getListTodoItem(key);
        return mTodoItems;
    }

    public LiveData<List<TodoItem>> getListTodoItemByStatus(String status, String key) {
        mTodoItems = mTodoItemDAO.getListTodoItemByStatus(status, key);
        return mTodoItems;
    }

    public List<TodoItem> getSearchTodoItem(String key) {
        return mTodoItemDAO.getSearchTodoItem(key);
    }

    public List<TodoItem> getSearchTodoItemByStatus(String status, String key) {
        return mTodoItemDAO.getSearchTodoItemByStatus(status, key);
    }

    public void insert(TodoItem todoItem) {
        mTodoItemDAO.insertTodoItem(todoItem);
    }

    public void update(TodoItem todoItem) {
        mTodoItemDAO.updateTodoItem(todoItem);
    }

    public void delete(TodoItem todoItem) {
        mTodoItemDAO.deleteTodoItem(todoItem);
    }

    public void clear(List<Integer> id) {
        mTodoItemDAO.clearTodoItem(id);
    }
}
