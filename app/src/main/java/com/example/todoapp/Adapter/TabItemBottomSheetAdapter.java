package com.example.todoapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.todoapp.bottomsheet.AllItemBottomSheetFragment;
import com.example.todoapp.viewmodel.TodoItemViewModel;

public class TabItemBottomSheetAdapter  extends FragmentStateAdapter {
    private final TodoItemViewModel todoItemViewModel;
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0 :
                //Create new fragment for tab
                return new AllItemBottomSheetFragment(todoItemViewModel,1);
            case 1 :
                return new AllItemBottomSheetFragment(todoItemViewModel,2);
            default:
                return new AllItemBottomSheetFragment(todoItemViewModel,3);
        }
    }
    @Override
    public int getItemCount() {
        return 3;
    }

    public TabItemBottomSheetAdapter(@NonNull FragmentActivity fragmentActivity, TodoItemViewModel todoItemViewModel) {
        super(fragmentActivity);
        this.todoItemViewModel = todoItemViewModel;
    }
}


