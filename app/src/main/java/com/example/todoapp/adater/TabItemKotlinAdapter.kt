package com.example.todoapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todoapp.kotlin.AllKotlinFragment
import com.example.todoapp.viewmodel.TodoItemViewModel

//Class adapter set tab in kotlin
class TabItemKotlinAdapter(
    fragmentActivity: FragmentActivity,
    todoItemViewModel: TodoItemViewModel
) : FragmentStateAdapter(fragmentActivity) {

    private var todoItemViewModel: TodoItemViewModel? = todoItemViewModel
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllKotlinFragment(todoItemViewModel, 0)
            1 -> AllKotlinFragment(todoItemViewModel, 1)
            else -> AllKotlinFragment(todoItemViewModel, 2)
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}