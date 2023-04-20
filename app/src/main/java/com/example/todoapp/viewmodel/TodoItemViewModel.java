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

    public MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();

    public List<Integer> clearItem = new ArrayList<>();

    public List<Long> idItem = new ArrayList<>();

    public MutableLiveData<List<Long>> listMutableLiveDataCheck = new MutableLiveData<>();
    public MutableLiveData<List<Integer>> listMutableCheck = new MutableLiveData<>();
    public TodoItemViewModel(Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        stringMutableLiveData.postValue("");
      //  List<Long> item = new ArrayList<>();
        listMutableLiveDataCheck.postValue(new ArrayList<>());
        listMutableCheck.postValue(new ArrayList<>());
    }

    public LiveData<List<TodoItem>> getAllList(String key) {
        mTodoItems = mRepository.getAllList(key);
        return mTodoItems;
    }

    public MutableLiveData<String> getStringMutableLiveData() {
        return stringMutableLiveData;
    }
    public MutableLiveData<List<Integer>> getListMutableCheck() {
        return listMutableCheck;
    }
    public MutableLiveData<List<Long>> getListMutableLiveDataCheck() {
        return listMutableLiveDataCheck;
    }

    public LiveData<List<TodoItem>> getPendingList() {
        mTodoItems = mRepository.getlistTodoItemByStatus("pending",stringMutableLiveData.getValue());

        return mTodoItems;
    }



    public LiveData<List<TodoItem>> getCompletedList() {
        mTodoItems = mRepository.getlistTodoItemByStatus("completed",stringMutableLiveData.getValue());
        return mTodoItems;
    }

    public List<Long> getIdItem() {
        return idItem;
    }

    public void setClearAll(int id, boolean check) {
        if (check == true) {
            if (clearItem.contains(id)) {
            } else {
                clearItem.add(id);
            }
        } else {
            clearItem.removeIf(a -> a == id);
        }
    }
    public void setCheckData(int id, boolean check) {
        List<Integer> item= listMutableCheck.getValue();
        if (check == true) {
            if (item.contains(id)) {
            } else {
                item.add(id);
            }
        } else {
            item.removeIf(a -> a == id);
        }
        listMutableCheck.postValue(item);
    }
    public void setCheckItem(long id, boolean check) {
        List<Long> item= listMutableLiveDataCheck.getValue();
        if (check == true) {
            if (item.contains(id)) {
            } else {
                item.add(id);
            }
        } else {
            item.removeIf(a -> a == id);
        }
        listMutableLiveDataCheck.postValue(item);
    }
    public void setitem(long id, boolean check) {
        if (check == true) {
            if (idItem.contains(id)) {
            } else {
                idItem.add(id);
            }
        } else {
            idItem.removeIf(a -> a == id);
        }

    }

    public void clearItem() {
        mRepository.clear(clearItem);
    }
}
