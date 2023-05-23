package com.example.todoapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.adapter.TodoItemAdapter;
import com.example.todoapp.databinding.FragmentAllItemBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class AllItemFragment extends Fragment {

    private final TodoItemViewModel todoItemViewModel;
    private FragmentAllItemBinding fragmentAllItemBinding;
    private TodoItemAdapter todoItemAdapter;
    private List<TodoItem> todoItems;
    private List<TodoItem> todoItemLoads;
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 5;
    private int startItem;
    private int endItem;
    private int currentPage = 1;

    public AllItemFragment(TodoItemViewModel todoItemViewModel) {
        this.todoItemViewModel = todoItemViewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentAllItemBinding = FragmentAllItemBinding.inflate(inflater, container, false);
        return fragmentAllItemBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayListTodo();
        postponeEnterTransition();
    }

    public void displayListTodo() {
        RecyclerView rcvItem = fragmentAllItemBinding.rcvTodoItem;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvItem.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        rcvItem.addItemDecoration(dividerItemDecoration);

        todoItemAdapter = new TodoItemAdapter(new TodoItemAdapter.TodoItemDiff(), todoItemViewModel);
        todoItemAdapter.setHasStableIds(true);
        //set data to recyclerview
        todoItemViewModel.getAllList().observe(requireActivity(), this::setLoading);

        todoItemViewModel.getStringMutableLiveData().observe(requireActivity(), s ->
                setLoading(todoItemViewModel.getSearchList()));
        todoItemAdapter.setClickListener(new TodoItemAdapter.IClickItemToDo() {
            @Override
            public void detailItem(TodoItem todoItem, CardView cardView) {
                clickDetailItem(todoItem, cardView);
            }

            @Override
            public void clearItem(TodoItem todoItem, long id, boolean check) {
                todoItemViewModel.setClearAll(todoItem.getId(), check);
                todoItemViewModel.setCheckItem(id, check);
            }
        });
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(todoItemAdapter);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);

        rcvItem.setAdapter(new ScaleInAnimationAdapter(alphaInAnimationAdapter));
        rcvItem.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItems() {
                // Update item to fragment
                isLoading = true;
                currentPage += 1;
                todoItems = todoItemViewModel.getSearchList();
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

    private void setLoading(List<TodoItem> items) {
        todoItems = items;
        currentPage = 0;
        isLastPage = false;
        if (items.size() % 20 == 0) {
            totalPage = (items.size() / 20);
        } else {
            totalPage = (items.size() / 20) + 1;
        }
        setFirstData();
    }

    private void clickDetailItem(TodoItem todoItem, CardView cardView) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("objectTodoItem", todoItem);
        bundle.putString("transition", cardView.getTransitionName());

        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder().addSharedElement(cardView, cardView.getTransitionName()).build();
        Navigation.findNavController(requireView()).navigate(R.id.updateItemFragment, bundle, null, extras);
    }

    private void loadNextPage() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            List<TodoItem> list = new ArrayList<>();
            if (todoItems.size() > 20) {
                list = todoItems.subList(startItem, endItem);
                startItem = endItem;
                if ((endItem + 20) < todoItems.size()) {
                    endItem += 20;
                } else {
                    endItem = todoItems.size();
                }
            }
            todoItemAdapter.removeFooterLoading();
            todoItemLoads.addAll(list);
            todoItemAdapter.notifyItemRangeChanged(startItem - 20, todoItemLoads.size());
            isLoading = false;
            if (currentPage < totalPage) {
                todoItemAdapter.addFooterLoading();
            } else {
                isLastPage = true;
            }
        }, 2000);
    }

    private void setFirstData() {
        startItem = 0;
        endItem = 20;
        if (todoItems.size() > 20) {
            todoItemLoads = todoItems.subList(startItem, endItem);
            startItem = endItem;
            if ((endItem + 20) < todoItems.size()) {
                endItem += 20;
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