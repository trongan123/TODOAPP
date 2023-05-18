package com.example.todoapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.todoapp.AllItemFragment;
import com.example.todoapp.CompletedItemFragment;
import com.example.todoapp.PendingItemFragment;
import com.example.todoapp.viewmodel.TodoItemViewModel;

public class TabItemAdapter extends FragmentStateAdapter {
    private final TodoItemViewModel todoItemViewModel;

    public TabItemAdapter(@NonNull FragmentActivity fragmentActivity, TodoItemViewModel todoItemViewModel) {
        super(fragmentActivity);
        this.todoItemViewModel = todoItemViewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                //Create new fragment for tab
                return new AllItemFragment(todoItemViewModel);
            case 1:
                return new PendingItemFragment(todoItemViewModel);
            default:
                return new CompletedItemFragment(todoItemViewModel);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
