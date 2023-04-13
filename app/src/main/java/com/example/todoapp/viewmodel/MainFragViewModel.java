package com.example.todoapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.todoapp.repository.TodoRepository;

import java.util.List;

public class MainFragViewModel extends AndroidViewModel {
    private final TodoRepository mRepository;

    public MainFragViewModel(Application application) {
        super(application);
        mRepository = new TodoRepository(application);
    }

    public void clearItem(List<Integer> id){
        mRepository.clear(id);
    }

}
