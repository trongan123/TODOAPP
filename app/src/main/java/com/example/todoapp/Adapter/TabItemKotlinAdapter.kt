package com.example.todoapp.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todoapp.kotlin.AllKotlinFragment
import com.example.todoapp.kotlin.CompletedKotlinFragment
import com.example.todoapp.kotlin.PendingKotlinFragment
import com.example.todoapp.viewmodel.TodoItemViewModel

//Class adapter kotlen
class TabItemKotlinAdapter(fragmentActivity: FragmentActivity,todoItemViewModel: TodoItemViewModel) : FragmentStateAdapter(fragmentActivity){

    private var todoItemViewModel: TodoItemViewModel? = todoItemViewModel

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllKotlinFragment(todoItemViewModel)
            1 -> PendingKotlinFragment(todoItemViewModel)
            else -> CompletedKotlinFragment(todoItemViewModel)
        }
    }

    override fun getItemCount(): Int {
        return 3
    }






}