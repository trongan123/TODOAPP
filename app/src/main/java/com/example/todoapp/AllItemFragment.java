package com.example.todoapp;

import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.TodoItemAdapter;
import com.example.todoapp.databinding.FragmentAllItemBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AllItemFragment extends Fragment {

    private FragmentAllItemBinding fragmentAllItemBinding;
    private final TodoItemViewModel todoItemViewModel;
    private TodoItemAdapter todoItemAdapter;
    private List<TodoItem> todoItemList;
    private List<TodoItem> todoItemload;
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 5;

    private int startitem;
    private int enditem;
    private int currentPage = 1;

    public AllItemFragment(TodoItemViewModel todoItemViewModel) {
        this.todoItemViewModel = todoItemViewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayListTodo();
    }

    public void displayListTodo() {

        RecyclerView rcvItem = fragmentAllItemBinding.rcvTodoitem;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvItem.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvItem.addItemDecoration(dividerItemDecoration);

        todoItemAdapter = new TodoItemAdapter(new TodoItemAdapter.TodoItemDiff(), todoItemViewModel);
        todoItemAdapter.setHasStableIds(true);

        todoItemViewModel.getStringMutableLiveData().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                todoItemViewModel.getAllList(todoItemViewModel.getStringMutableLiveData().getValue()).observe(getActivity(), items -> {
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
                todoItemViewModel.getAllList(todoItemViewModel.getStringMutableLiveData().getValue()).observe(getActivity(), items -> {
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

    private void clickDetailItem(TodoItem todoItem, CardView cardView) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_TodoItem", todoItem);

        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(cardView, "update_edttitle")
                .addSharedElement(cardView,"update_fragment")
                .build();

        postponeEnterTransition(2500, TimeUnit.MILLISECONDS);

        Navigation.findNavController(getView()).navigate(R.id.updateItemFragment, bundle,null,extras);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAllItemBinding = FragmentAllItemBinding.inflate(inflater, container, false);
        View mView = fragmentAllItemBinding.getRoot();
        fragmentAllItemBinding.setAllItemViewModel(todoItemViewModel);
        // Inflate the layout for this fragment
        return mView;

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