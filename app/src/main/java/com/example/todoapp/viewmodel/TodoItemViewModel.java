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

    public static final MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
    public static final MutableLiveData<List<Long>> listMutableLiveDataCheck = new MutableLiveData<>();
    protected static List<Integer> clearItem = new ArrayList<>();
    private final TodoRepository mRepository;
    private LiveData<List<TodoItem>> todoItems;
    private TodoItem todoItem;

    public TodoItemViewModel(Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        stringMutableLiveData.setValue("");
        clearItem = new ArrayList<>();
        listMutableLiveDataCheck.postValue(new ArrayList<>());
    }

    public LiveData<List<TodoItem>> getAllList() {
        todoItems = mRepository.getAllList(stringMutableLiveData.getValue());
        return todoItems;
    }

    public List<TodoItem> getSearchList() {
        return mRepository.getSearchTodoItem(stringMutableLiveData.getValue());
    }

    public MutableLiveData<String> getStringMutableLiveData() {
        return stringMutableLiveData;
    }

    public MutableLiveData<List<Long>> getListMutableLiveDataCheck() {
        return listMutableLiveDataCheck;
    }

    public LiveData<List<TodoItem>> getPendingList() {
        todoItems = mRepository.getListTodoItemByStatus("pending", stringMutableLiveData.getValue());
        return todoItems;
    }

    public List<TodoItem> getSearchPendingList() {
        return mRepository.getSearchTodoItemByStatus("pending", stringMutableLiveData.getValue());
    }

    public LiveData<List<TodoItem>> getCompletedList() {
        todoItems = mRepository.getListTodoItemByStatus("completed", stringMutableLiveData.getValue());
        return todoItems;
    }

    public List<TodoItem> getSearchCompletedList() {
        return mRepository.getSearchTodoItemByStatus("completed", stringMutableLiveData.getValue());
    }

    public void addItem(TodoItem todoItem) {
        mRepository.insert(todoItem);
    }

    public List<Integer> getClearItem() {
        return clearItem;
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

    public void clearAllItem() {
        mRepository.clear(clearItem);
    }
}