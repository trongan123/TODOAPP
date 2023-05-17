package com.example.todoapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.TodoItemAdapter;
import com.example.todoapp.databinding.FragmentPendingItemBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class PendingItemFragment extends Fragment {

    private final TodoItemViewModel todoItemViewModel;
    private FragmentPendingItemBinding fragmentPendingItemBinding;
    private TodoItemAdapter todoItemAdapter;
    private List<TodoItem> todoItems;
    private List<TodoItem> todoItemLoads;
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
        RecyclerView rcvItem = fragmentPendingItemBinding.rcvTodoitem;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        rcvItem.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        rcvItem.addItemDecoration(dividerItemDecoration);

        todoItemAdapter = new TodoItemAdapter(new TodoItemAdapter.TodoItemDiff(), todoItemViewModel);
        todoItemViewModel.getStringMutableLiveData().observe(requireActivity(), s ->
                todoItemViewModel.getPendingList().observe(requireActivity(), items -> {
                    // Update item to fragment
                    todoItems = items;
                    currentPage = 0;
                    isLastPage = false;
                    if (items.size() % 20 == 0) {
                        totalPage = (items.size() / 20);
                    } else {
                        totalPage = (items.size() / 20) + 1;
                    }
                    setFirstData();
                }));

        todoItemAdapter.setClickListenner(new TodoItemAdapter.IClickItemToDo() {
            @Override
            public void DetaiItem(TodoItem todoItem, CardView cardView) {
                clickDetailItem(todoItem,cardView);
            }

            @Override
            public void clearItem(TodoItem todoItem, long id, boolean check) {
                todoItemViewModel.setClearAll(todoItem.getId(), check);
                todoItemViewModel.setCheckItem(id, check);
            }
        });
        rcvItem.setAdapter(todoItemAdapter);
        rcvItem.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                todoItemViewModel.getPendingList().observe(requireActivity(), items -> {
                    // Update item to fragment
                    todoItems = items;
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
        new Handler().postDelayed(() -> {

            List<TodoItem> list = new ArrayList<>();
            if (todoItems.size() > 20) {
                list = todoItems.subList(startitem, enditem);

                startitem = enditem;
                if ((enditem + 20) < todoItems.size()) {
                    enditem += 20;
                } else {
                    enditem = todoItems.size();
                }
            }
            todoItemAdapter.removeFooterLoading();
            todoItemLoads.addAll(list);
            todoItemAdapter.notifyDataSetChanged();
            isLoading = false;
            if (currentPage < totalPage) {
                todoItemAdapter.addFooterLoading();
            } else {
                isLastPage = true;
            }
        }, 2000);
    }

    private void clickDetailItem(TodoItem todoItem, CardView cardView) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_TodoItem", todoItem);
        bundle.putString("transition",cardView.getTransitionName() );

        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(cardView,cardView.getTransitionName())
                .build();

        Navigation.findNavController(requireView()).navigate(R.id.updateItemFragment, bundle,null,extras);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPendingItemBinding = FragmentPendingItemBinding.inflate(inflater, container, false);
        View mView = fragmentPendingItemBinding.getRoot();

        fragmentPendingItemBinding.setAllItemViewModel(todoItemViewModel);

        return mView;
    }

    private void setFirstData() {
        startitem = 0;
        enditem = 20;
        if (todoItems.size() > 20) {
            todoItemLoads = todoItems.subList(startitem, enditem);
            startitem = enditem;
            if ((enditem + 20) < todoItems.size()) {
                enditem += 20;
            }
        } else {
            todoItemLoads = todoItems;
        }
        todoItemAdapter.submitList(todoItemLoads);
        if (currentPage < totalPage) {
            todoItemAdapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }
}