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

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todoapp.Adapter.TodoItemAdapter;
import com.example.todoapp.databinding.FragmentPendingItemBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class PendingItemFragment extends Fragment {

    private TodoItemViewModel todoItemViewModel;

    private FragmentPendingItemBinding fragmentPendingItemBinding;
    private View mView;

    private RecyclerView rcvItem;
    private TodoItemAdapter todoItemAdapter;
    private List<TodoItem> todoItemList;
    private List<TodoItem> todoItemload;

    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 5;

    private int startitem;
    private int enditem;
    private int currentPage = 1;

    public PendingItemFragment(TodoItemViewModel todoItemViewModel) {
        this.todoItemViewModel = todoItemViewModel;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
           displayListTodo();
    }

    public void displayListTodo() {
         rcvItem = fragmentPendingItemBinding.rcvTodoitem;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvItem.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvItem.addItemDecoration(dividerItemDecoration);


        todoItemAdapter = new TodoItemAdapter(new TodoItemAdapter.TodoItemDiff(),todoItemViewModel);
        todoItemViewModel.getStringMutableLiveData().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                todoItemViewModel.getPendingList().observe(getActivity(), items -> {
                    // Update item to fragment
                    todoItemList = items;
                    currentPage = 0;
                    isLastPage = false;
                    if (items.size() % 20 == 0) {
                        totalPage = (items.size() / 20);
                    } else {
                        totalPage = (items.size() / 20) + 1;
                    }
                    setFirstData();
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
        rcvItem.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                todoItemViewModel.getPendingList().observe(getActivity(), items -> {
                    // Update item to fragment
                    todoItemList = items;
                    if (items.size() % 20 == 0) {
                        totalPage = (items.size() / 20);
                    } else {
                        totalPage = (items.size() / 20) + 1;
                    }
                });
                loadNextPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
    }
    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                List<TodoItem> list = new ArrayList<>();
                if (todoItemList.size() > 20) {
                    list = todoItemList.subList(startitem, enditem);

                    startitem = enditem;
                    if ((enditem + 20) < todoItemList.size()) {
                        enditem += 20;
                    } else {
                        enditem = todoItemList.size();
                    }
                }
                todoItemAdapter.removeFooterLoading();
                todoItemload.addAll(list);
                todoItemAdapter.notifyDataSetChanged();
                isLoading = false;
                if (currentPage < totalPage) {
                    todoItemAdapter.addFooterLoading();
                } else {
                    isLastPage = true;
                }
            }
        }, 2000);
    }
    private void clickDetailItem(TodoItem todoItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_TodoItem", todoItem);
        Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_updateItemFragment, bundle);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPendingItemBinding = FragmentPendingItemBinding.inflate(inflater,container,false);
        mView = fragmentPendingItemBinding.getRoot();

        fragmentPendingItemBinding.setAllItemViewModel(todoItemViewModel);

        return mView;
    }
    private void setFirstData() {
        startitem = 0;
        enditem = 20;
        if (todoItemList.size() > 20) {
            todoItemload = todoItemList.subList(startitem, enditem);
            startitem = enditem;
            if ((enditem + 20) < todoItemList.size()) {
                enditem += 20;
            }
        } else {
            todoItemload = todoItemList;
        }
        todoItemAdapter.submitList(todoItemload);
        if (currentPage < totalPage) {
            todoItemAdapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }
}