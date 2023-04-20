package com.example.todoapp;

import android.os.Bundle;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.TodoItemAdapter;
import com.example.todoapp.databinding.FragmentAllItemBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;

public class AllItemFragment extends Fragment {

    private FragmentAllItemBinding fragmentAllItemBinding;
    private TodoItemViewModel todoItemViewModel;
    private TodoItemAdapter todoItemAdapter;

    public AllItemFragment(TodoItemViewModel todoItemViewModel) {
        this.todoItemViewModel = todoItemViewModel;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayListTodo();
    }

    public void displayListTodo(){

        RecyclerView rcvItem = fragmentAllItemBinding.rcvTodoitem;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvItem.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvItem.addItemDecoration(dividerItemDecoration);

        todoItemAdapter = new TodoItemAdapter(new TodoItemAdapter.TodoItemDiff(),todoItemViewModel);
        todoItemAdapter.setHasStableIds(true);

        todoItemViewModel.getStringMutableLiveData().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                todoItemViewModel.getAllList(todoItemViewModel.getStringMutableLiveData().getValue()).observe(getActivity(), items -> {
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

    public void receiveData(String search){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAllItemBinding = FragmentAllItemBinding.inflate(inflater,container,false);
        View mView = fragmentAllItemBinding.getRoot();


        fragmentAllItemBinding.setAllItemViewModel(todoItemViewModel);
        // Inflate the layout for this fragment
        return mView;

    }


}