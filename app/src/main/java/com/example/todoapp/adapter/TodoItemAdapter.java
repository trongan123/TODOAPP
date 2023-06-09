package com.example.todoapp.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.databinding.ItemLoadingBinding;
import com.example.todoapp.databinding.ItemTodoBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TodoItemAdapter extends ListAdapter<TodoItem, RecyclerView.ViewHolder> {

    private static final String STRING_DATE_FORMAT = "yyyy-MM-dd";
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private final TodoItemViewModel todoItemViewModel;
    private final FragmentActivity activity;
    private IClickItemToDo iClickItem;
    private boolean isLoadingAdd;

    public TodoItemAdapter(@NonNull DiffUtil.ItemCallback<TodoItem> diffCallback, TodoItemViewModel todoItemViewModel, FragmentActivity activity) {
        super(diffCallback);
        this.todoItemViewModel = todoItemViewModel;
        this.activity = activity;
    }

    public void setClickListener(IClickItemToDo iClickItem) {
        this.iClickItem = iClickItem;
    }

    @Override
    public long getItemId(int position) {
        return getCurrentList().get(position).getId();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType) {
            ItemTodoBinding itemTodoBinding = ItemTodoBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new TodoItemViewHolder(itemTodoBinding);
        } else {
            ItemLoadingBinding itemLoadingBinding = ItemLoadingBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new LoadingViewHolder(itemLoadingBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            TodoItem todoItem = getItem(position);
            TodoItemViewHolder todoItemViewHolder = (TodoItemViewHolder) holder;
            List<Integer> checkItem = todoItemViewModel.getClearItem();
            if (checkItem != null) {
                if (checkItem.contains(todoItem.getId())) {
                    todoItemViewHolder.itemTodoBinding.txtTitle.setChecked(true);
                    todoItemViewHolder.itemTodoBinding.txtTitle.setPaintFlags(todoItemViewHolder
                            .itemTodoBinding.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    todoItemViewHolder.itemTodoBinding.txtTitle.setChecked(false);
                    todoItemViewHolder.itemTodoBinding.txtTitle.setPaintFlags(todoItemViewHolder
                            .itemTodoBinding.txtTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
            // set date to item
            DateFormat dateFormat = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault());
            todoItemViewHolder.itemTodoBinding.txtDate.setText(dateFormat.format(todoItem.getCompletedDate()));
            todoItemViewHolder.itemTodoBinding.txtTitle.setText(todoItem.getTitle());
            todoItemViewHolder.itemTodoBinding.txtDescription.setText(todoItem.getDescription());
            todoItemViewHolder.itemTodoBinding.cardItem.setTransitionName("update_" + position);
            todoItemViewHolder.itemTodoBinding.cardItem.setOnClickListener(view ->
                    iClickItem.detailItem(todoItem, todoItemViewHolder.itemTodoBinding.cardItem));
            todoItemViewHolder.itemTodoBinding.txtTitle.setOnClickListener(view ->
                    setCheckBox(todoItemViewHolder, todoItem, position));
        }
    }

    //method set status for check box
    private void setCheckBox(TodoItemViewHolder todoItemViewHolder, TodoItem todoItem, long id) {
        if (todoItemViewHolder.itemTodoBinding.txtTitle.isChecked()) {
            todoItemViewHolder.itemTodoBinding.txtTitle.setPaintFlags(todoItemViewHolder
                    .itemTodoBinding.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            iClickItem.clearItem(todoItem, id, true);
        } else {
            todoItemViewHolder.itemTodoBinding.txtTitle.setPaintFlags(todoItemViewHolder
                    .itemTodoBinding.txtTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            iClickItem.clearItem(todoItem, id, false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        List<TodoItem> todoItems = getCurrentList();
        if (position == todoItems.size() - 1 && isLoadingAdd) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;
    }

    public interface IClickItemToDo {
        void detailItem(TodoItem todoItem, CardView cardView);

        void clearItem(TodoItem todoItem, long id, boolean check);
    }

    public static class TodoItemDiff extends DiffUtil.ItemCallback<TodoItem> {
        @Override
        public boolean areItemsTheSame(@NonNull TodoItem oldItem, @NonNull TodoItem newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull TodoItem oldItem, @NonNull TodoItem newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }

    public static class TodoItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemTodoBinding itemTodoBinding;

        public TodoItemViewHolder(@NonNull ItemTodoBinding itemTodoBinding) {
            super(itemTodoBinding.getRoot());
            this.itemTodoBinding = itemTodoBinding;
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(@NonNull ItemLoadingBinding itemLoadingBinding) {
            super(itemLoadingBinding.getRoot());
        }
    }
}

