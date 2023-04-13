package com.example.todoapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todoapp.Adapter.TodoItemAdapter;
import com.example.todoapp.databinding.FragmentCompletedItemBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;


public class CompletedItemFragment extends Fragment {



    private FragmentCompletedItemBinding fragmentCompletedItemBinding;

    private TodoItemViewModel todoItemViewModel;
    private View mView;

    public CompletedItemFragment(TodoItemViewModel todoItemViewModel) {
        this.todoItemViewModel = todoItemViewModel;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayListTodo();

    }

    public void displayListTodo(){
        RecyclerView rcvItem = fragmentCompletedItemBinding.rcvTodoitem;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvItem.setLayoutManager(linearLayoutManager);

        //set rach chan field
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvItem.addItemDecoration(dividerItemDecoration);

        final TodoItemAdapter todoItemAdapter = new TodoItemAdapter(new TodoItemAdapter.TodoItemDiff(),todoItemViewModel);
        todoItemViewModel.getStringMutableLiveData().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                todoItemViewModel.getCompletedList().observe(getActivity(), items -> {
                    // Update item to fragment
                    todoItemAdapter.submitList(items);
                });
            }
        });

        todoItemAdapter.setClickListenner(new TodoItemAdapter.IClickItemToDo() {
            @Override
            public void DetaiItem(TodoItem todoItem) {
                clickDetailItem(todoItem);
            }
            @Override
            public void clearItem(TodoItem todoItem,long id, boolean check) {
                todoItemViewModel.setClearAll(todoItem.getId(),check);
                todoItemViewModel.setCheckItem(id,check);
            }
        });
        rcvItem.setAdapter(todoItemAdapter);
    }
    private void clickDetailItem(TodoItem todoItem) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_TodoItem", todoItem);
        Navigation.findNavController(getView()).navigate(R.id.updateItemFragment, bundle);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentCompletedItemBinding = FragmentCompletedItemBinding.inflate(inflater,container,false);
        mView = fragmentCompletedItemBinding.getRoot();


        fragmentCompletedItemBinding.setAllItemViewModel(todoItemViewModel);
        // Inflate the layout for this fragment
        return mView;
    }
}