package com.example.todoapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.theme.screem.MyComposeView
import com.example.todoapp.viewmodel.TodoItemViewModel

class TodoItemAdapterKotlin(todoItemViewModel: TodoItemViewModel, view: View) :
    ListAdapter<TodoItem, TodoItemAdapterKotlin.TodoItemViewHolder>(DiffCallback()) {

    private var iClickItem: IClickItemToDo? = null
    private var todoItemViewModel: TodoItemViewModel? = null
    private var view: View? = null

    init {
        this.todoItemViewModel = todoItemViewModel
        this.view = view
    }

    interface IClickItemToDo {
        fun detaiItem(todoItem: TodoItem?)
        fun clearItem(todoItem: TodoItem?, id: Long, check: Boolean)
    }

    fun setClickListenner(iClickItem: IClickItemToDo) {
        this.iClickItem = iClickItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_jetpack, parent, false)
        return TodoItemViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return currentList[position].id.toLong()
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val todoItem = getItem(position)
        holder.myitem.i = todoItem
        holder.myitem.todoViewModel = todoItemViewModel
        holder.myitem.view = view
        holder.myitem.transitionName = "update_$position"
        holder.myitem.disposeComposition()
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    class DiffCallback : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.equals(newItem)
        }
    }

    class TodoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var myitem: MyComposeView

        init {
            myitem = itemView.findViewById(R.id.myItem)
        }
    }
}