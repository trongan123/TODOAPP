package com.example.todoapp.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.adapter.TodoItemAdapterKotlin
import com.example.todoapp.databinding.FragmentAllItemBinding
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.TodoItemViewModel

class AllKotlinFragment(todoItemViewModel: TodoItemViewModel?, tabNumber: Int) : Fragment() {

    private var fragmentAllItemBinding: FragmentAllItemBinding? = null
    private var todoItemViewModel: TodoItemViewModel? = null
    private var todoItemAdapter: TodoItemAdapterKotlin? = null
    private val tabNumber: Int

    init {
        this.todoItemViewModel = todoItemViewModel
        this.tabNumber = tabNumber
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllItemBinding = FragmentAllItemBinding.inflate(inflater, container, false)
        return fragmentAllItemBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayListTodo()
    }

    private fun displayListTodo() {
        val rcvItem = fragmentAllItemBinding!!.rcvTodoItem
        val linearLayoutManager = LinearLayoutManager(context)
        rcvItem.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rcvItem.addItemDecoration(dividerItemDecoration)
        todoItemAdapter = todoItemViewModel?.let { TodoItemAdapterKotlin(it, requireView()) }
        todoItemAdapter!!.setHasStableIds(true)
        when (tabNumber) {
            0 -> {
                todoItemViewModel!!.allList.observe(
                    requireActivity()
                ) { items ->
                    // Update item to fragment
                    todoItemAdapter!!.submitList(items)
                }
                todoItemViewModel!!.stringMutableLiveData.observe(requireActivity()) {
                    todoItemAdapter!!.submitList(todoItemViewModel!!.searchList)
                }
            }
            1 -> {
                todoItemViewModel!!.pendingList.observe(
                    requireActivity()
                ) { items ->
                    // Update item to fragment
                    todoItemAdapter!!.submitList(items)
                }
                todoItemViewModel!!.stringMutableLiveData.observe(requireActivity()) {
                    todoItemAdapter!!.submitList(todoItemViewModel!!.searchPendingList)
                }
            }
            2 -> {
                todoItemViewModel!!.completedList.observe(
                    requireActivity()
                ) { items ->
                    // Update item to fragment
                    todoItemAdapter!!.submitList(items)
                }
                todoItemViewModel!!.stringMutableLiveData.observe(requireActivity()) {
                    todoItemAdapter!!.submitList(todoItemViewModel!!.searchCompletedList)
                }
            }
        }
        todoItemAdapter!!.setClickListenner(object : TodoItemAdapterKotlin.IClickItemToDo {
            override fun detaiItem(todoItem: TodoItem?) {
                if (todoItem != null) {
                    clickDetailItem(todoItem)
                }
            }

            override fun clearItem(todoItem: TodoItem?, id: Long, check: Boolean) {
                if (todoItem != null) {
                    todoItemViewModel!!.setClearAll(todoItem.id, check)
                }
                todoItemViewModel!!.setCheckItem(id, check)
            }
        })
        rcvItem.adapter = todoItemAdapter
    }

    private fun clickDetailItem(todoItem: TodoItem) {
        val bundle = Bundle()
        bundle.putSerializable("objectTodoItem", todoItem)
        findNavController(requireView()).navigate(R.id.updateItemKotlinFragment, bundle)
    }
}