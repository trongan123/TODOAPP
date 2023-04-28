package com.example.todoapp.kotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.Adapter.TodoItemAdapterKotlin
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAllKotlinBinding
import com.example.todoapp.databinding.FragmentPendingKotlinBinding
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.TodoItemViewModel


class PendingKotlinFragment(todoItemViewModel: TodoItemViewModel?) : Fragment() {

    private var fragmentPendingKotlinBinding: FragmentPendingKotlinBinding? = null
    private var todoItemViewModel: TodoItemViewModel? = null
    private var todoItemAdapter: TodoItemAdapterKotlin? = null

    init {
        this.todoItemViewModel = todoItemViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentPendingKotlinBinding =  FragmentPendingKotlinBinding.inflate(inflater,container,false)
        var mView = fragmentPendingKotlinBinding!!.root
        todoItemViewModel = ViewModelProvider(this).get(TodoItemViewModel::class.java)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayListTodo()
    }

    fun displayListTodo() {
        val rcvItem = fragmentPendingKotlinBinding!!.rcvTodoitem
        val linearLayoutManager = LinearLayoutManager(context)
        rcvItem.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rcvItem.addItemDecoration(dividerItemDecoration)
        todoItemAdapter = todoItemViewModel?.let { TodoItemAdapterKotlin(it,requireView()) }
        todoItemAdapter!!.setHasStableIds(true)
        todoItemViewModel!!.getStringMutableLiveData()
            .observe(requireActivity(), object : Observer<String?> {
                override fun onChanged(value: String?) {
                    todoItemViewModel!!.pendingList
                        .observe(
                            activity!!
                        ) { items ->
                            // Update item to fragment
                            todoItemAdapter!!.submitList(items)
                        }
                }
            })
        todoItemAdapter!!.setClickListenner(object : TodoItemAdapterKotlin.IClickItemToDo {
            override fun DetaiItem(todoItem: TodoItem?) {
                if (todoItem != null) {
                    clickDetailItem(todoItem)
                }
            }

            override fun clearItem(todoItem: TodoItem?, id: Long, check: Boolean) {
                if (todoItem != null) {
                    todoItemViewModel!!.setClearAll( todoItem.id, check)
                }
                todoItemViewModel!!.setCheckItem(id, check)
            }
        })
        rcvItem.adapter = todoItemAdapter
    }
    private fun clickDetailItem(todoItem: TodoItem) {
        val bundle = Bundle()
        bundle.putSerializable("object_TodoItem", todoItem)
        Navigation.findNavController(requireView()!!).navigate(R.id.updateItemKotlinFragment, bundle)
    }

}