package com.example.todoapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;

public class TodoItemViewModel extends AndroidViewModel {

    private final TodoRepository mRepository;
    private LiveData<List<TodoItem>> mTodoItems;
    private TodoItem todoItem;
    public MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
    public List<Integer> clearItem = new ArrayList<>();
    public MutableLiveData<List<Long>> listMutableLiveDataCheck = new MutableLiveData<>();

    public TodoItemViewModel(Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        stringMutableLiveData.postValue("");
        listMutableLiveDataCheck.postValue(new ArrayList<>());

    }

    public LiveData<List<TodoItem>> getAllList(String key) {
        mTodoItems = mRepository.getAllList(key);
        return mTodoItems;
    }

    public MutableLiveData<String> getStringMutableLiveData() {
        return stringMutableLiveData;
    }

    public MutableLiveData<List<Long>> getListMutableLiveDataCheck() {
        return listMutableLiveDataCheck;
    }

    public LiveData<List<TodoItem>> getPendingList() {
        mTodoItems = mRepository.getlistTodoItemByStatus("pending", stringMutableLiveData.getValue());

        return mTodoItems;
    }

    public void addItem(TodoItem todoItem) {
        mRepository.insert(todoItem);
    }

    public LiveData<List<TodoItem>> getCompletedList() {
        mTodoItems = mRepository.getlistTodoItemByStatus("completed", stringMutableLiveData.getValue());
        return mTodoItems;
    }

    public TodoItem getTodoItem() {
        return todoItem;
    }

    public void setTodoItem(TodoItem todoItem) {
        this.todoItem = todoItem;
    }

    public void updateItem(TodoItem todoItem) {
        mRepository.update(todoItem);
    }

    public void deleteItem(TodoItem todoItem) {
        mRepository.delete(todoItem);
    }


    public void setClearAll(int id, boolean check) {
        if (check) {
            if (!clearItem.contains(id)) {
                clearItem.add(id);
            }
        } else {
            clearItem.removeIf(a -> a == id);
        }
    }


    public void setCheckItem(long id, boolean check) {
        List<Long> item = listMutableLiveDataCheck.getValue();
        assert item != null;
        if (check) {
            if (!item.contains(id)) {

                item.add(id);
            }
        } else {
            item.removeIf(a -> a == id);
        }
        listMutableLiveDataCheck.postValue(item);
    }



    public void clearItem() {
        mRepository.clear(clearItem);
    }
}