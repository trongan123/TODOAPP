package com.example.todoapp.Adapter;

import android.graphics.Paint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.databinding.ItemTodoBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TodoItemAdapter extends ListAdapter<TodoItem, TodoItemAdapter.TodoItemViewHoldel> {


    private IClickItemToDo iClickItem;

    private TodoItemViewModel todoItemViewModel;

    public interface IClickItemToDo {
        void DetaiItem(TodoItem todoItem);

        void clearItem(TodoItem todoItem, long id, boolean check);
    }

    public void setClickListenner(IClickItemToDo iClickItem) {
        this.iClickItem = iClickItem;
    }


    public TodoItemAdapter(@NonNull DiffUtil.ItemCallback<TodoItem> diffCallback, TodoItemViewModel todoItemViewModel) {
        super(diffCallback);
        this.todoItemViewModel = todoItemViewModel;
    }

    @Override
    public long getItemId(int position) {
        return getCurrentList().get(position).getId();
    }

    @NonNull
    @Override
    public TodoItemViewHoldel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTodoBinding itemTodoBinding = ItemTodoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TodoItemViewHoldel(itemTodoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoItemViewHoldel holder, int position) {
        TodoItem todoItem = getItem(position);
        long id = getItemId(position);
        // holder.setIsRecyclable(false);
        List<Long> checkItem = todoItemViewModel.getListMutableLiveDataCheck().getValue();
        if (checkItem != null) {
            if (checkItem.contains(id)) {
                holder.itemTodoBinding.txttitle.setChecked(true);
                holder.itemTodoBinding.txttitle.setPaintFlags(
                        holder.itemTodoBinding.txttitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                );
            } else {
                holder.itemTodoBinding.txttitle.setChecked(false);
                holder.itemTodoBinding.txttitle.setPaintFlags(
                        holder.itemTodoBinding.txttitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG
                );
            }
        }

        // set date to item
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        holder.itemTodoBinding.txtDate.setText(dateFormat.format(todoItem.getCompletedDate()));
        holder.itemTodoBinding.btndetail.setTransitionName("update_"+position);
        holder.itemTodoBinding.setTodoItem(todoItem);
        holder.itemTodoBinding.btndetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItem.DetaiItem(todoItem);
            }
        });


        holder.itemTodoBinding.txttitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.itemTodoBinding.txttitle.isChecked()){
                    holder.itemTodoBinding.txttitle.setPaintFlags(
                            holder.itemTodoBinding.txttitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                    );
                    iClickItem.clearItem(todoItem,id,true);

                }else{
                    holder.itemTodoBinding.txttitle.setPaintFlags(
                            holder.itemTodoBinding.txttitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG
                    );
                    iClickItem.clearItem(todoItem,id,false);

                }
            }
        });

    }



    @Override
    public int getItemViewType(int position) {
        return 1;
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

    public class TodoItemViewHoldel extends RecyclerView.ViewHolder {
        private ItemTodoBinding itemTodoBinding;

        public TodoItemViewHoldel(@NonNull ItemTodoBinding itemTodoBinding) {
            super(itemTodoBinding.getRoot());
            this.itemTodoBinding = itemTodoBinding;
        }
    }


}

