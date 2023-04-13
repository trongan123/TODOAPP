package com.example.todoapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.repository.TodoRepository;

public class UpdateItemFragmentViewModel extends AndroidViewModel {
    private final TodoRepository mRepository;
    private TodoItem todoItem;
    public UpdateItemFragmentViewModel(Application application) {
        super(application);
        mRepository = new TodoRepository(application);

    }
    public TodoItem getTodoItem() {
        return todoItem;
    }

    public void setTodoItem(TodoItem todoItem) {
        this.todoItem = todoItem;
    }

    public void updateItem(TodoItem todoItem){
        mRepository.update(todoItem);
    }

    public void deleteItem(TodoItem todoItem){
        mRepository.delete(todoItem);
    }

}
