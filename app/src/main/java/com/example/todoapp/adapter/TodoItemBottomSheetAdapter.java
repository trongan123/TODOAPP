package com.example.todoapp.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.databinding.ItemTodoBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TodoItemBottomSheetAdapter extends ListAdapter<TodoItem, RecyclerView.ViewHolder> {

    private static final String STRING_DATE_FORMAT = "yyyy-MM-dd";
    private static final int TYPE_ITEM = 1;
    private final TodoItemViewModel todoItemViewModel;
    private TodoItemBottomSheetAdapter.IClickItemToDo iClickItem;

    public TodoItemBottomSheetAdapter(@NonNull DiffUtil.ItemCallback<TodoItem> diffCallback, TodoItemViewModel todoItemViewModel) {
        super(diffCallback);
        this.todoItemViewModel = todoItemViewModel;
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
        ItemTodoBinding itemTodoBinding = ItemTodoBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TodoItemViewHolder(itemTodoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TodoItem todoItem = getItem(position);
        TodoItemViewHolder todoItemViewHolder = (TodoItemViewHolder) holder;
        long id = getItemId(position);
        List<Long> checkItem = todoItemViewModel.getListMutableLiveDataCheck().getValue();
        if (checkItem != null) {
            if (checkItem.contains(id)) {
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
        todoItemViewHolder.itemTodoBinding.cardItem.setOnClickListener(view -> iClickItem.detailItem(todoItem));
        todoItemViewHolder.itemTodoBinding.txtTitle.setOnClickListener(view -> setCheckBox(todoItemViewHolder, todoItem, id));
        
        todoItemViewHolder.itemTodoBinding.getRoot().setAnimation(AnimationUtils.loadAnimation(todoItemViewHolder.itemTodoBinding.getRoot().getContext(), R.anim.recycler_item_arrive));
    }

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
        return TYPE_ITEM;
    }

    public interface IClickItemToDo {
        void detailItem(TodoItem todoItem);

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
}
