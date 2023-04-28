package com.example.todoapp.kotlin

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.todoapp.Adapter.TabItemAdapter
import com.example.todoapp.Adapter.TabItemKotlinAdapter
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentMainKotlinBinding
import com.example.todoapp.viewmodel.TodoItemViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainKotlinFragment : Fragment() {

    private var fragmentMainBinding: FragmentMainKotlinBinding? = null
    private var mView: View? = null
    private var todoItemViewModel: TodoItemViewModel? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMainBinding!!.btnAdd.setOnClickListener { view ->

            findNavController(view).navigate(R.id.addItemKotlinFragment)
        }

        val viewPager2 = requireView().findViewById<ViewPager2>(R.id.vpg)

        fragmentMainBinding!!.vpg.adapter =
            todoItemViewModel?.let { TabItemKotlinAdapter(requireActivity(), it) }

        val tabLayout = requireView()!!.findViewById<TabLayout>(R.id.tlomenu)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        val tabLayoutMediator = TabLayoutMediator(
            tabLayout, viewPager2
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "All"
                1 -> tab.text = "Pending"
                2 -> tab.text = "Completed"
            }
        }
        tabLayoutMediator.attach()


        fragmentMainBinding!!.svSearch
            .editText
            .setOnEditorActionListener { v, actionId, event ->
                fragmentMainBinding!!.searchBar.text = fragmentMainBinding!!.svSearch.text
                fragmentMainBinding!!.svSearch.hide()
                todoItemViewModel!!.stringMutableLiveData
                    .postValue(fragmentMainBinding!!.svSearch.text.toString())
                var a= todoItemViewModel!!.stringMutableLiveData.value
                false
            }

        todoItemViewModel!!.getListMutableLiveDataCheck()
            .observe(requireActivity(), object : Observer<List<Long?>?> {

                override fun onChanged(value: List<Long?>?) {
                    if (value!!.size <= 0) {
                        fragmentMainBinding!!.btnclearall.isEnabled = false
                    } else {
                        fragmentMainBinding!!.btnclearall.isEnabled = true
                    }
                }
            })


        fragmentMainBinding!!.btnclearall.setOnClickListener { clearItem() }
    }
    private fun clearItem() {
        AlertDialog.Builder(context)
            .setTitle("Confirm Clear All")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { dialogInterface, i ->
                todoItemViewModel!!.clearItem()
                Toast.makeText(activity, "Clear successfully", Toast.LENGTH_SHORT).show()
                findNavController(requireView()).navigate(R.id.mainKotlinFragment)
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentMainBinding =  FragmentMainKotlinBinding.inflate(inflater,container,false)
        mView = fragmentMainBinding!!.root
        todoItemViewModel = ViewModelProvider(this).get(TodoItemViewModel::class.java)

        return mView
    }


}