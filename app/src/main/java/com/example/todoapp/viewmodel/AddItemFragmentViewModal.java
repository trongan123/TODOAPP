package com.example.todoapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.repository.TodoRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddItemFragmentViewModal extends AndroidViewModel {
    private final TodoRepository mRepository;
    private TodoItem todoItem;
    public AddItemFragmentViewModal(Application application) {
        super(application);
        mRepository = new TodoRepository(application);
    }
    public TodoItem getTodoItem() {
        return todoItem;
    }

    public void setTodoItem(TodoItem todoItem) {
        this.todoItem = todoItem;
    }

    public void addItem(TodoItem todoItem){
//        for (TodoItem i :getlist()
//             ) {
//            mRepository.insert(i);
//        }
            mRepository.insert(todoItem);
    }


//    private List<TodoItem> getlist(){
//        List<TodoItem> item = new ArrayList<>();
//        int n = 50;
//        for(int i =0 ;i < n; i++){
//            item.add(new TodoItem("Pending : "+ i,"Renew","pending",new Date(),new Date()));
//        }
//        for(int i =50 ;i < 100; i++){
//            item.add(new TodoItem("Completed : "+ i,"Write","completed",new Date(),new Date()));
//        }
//        return item;
//    }


}

