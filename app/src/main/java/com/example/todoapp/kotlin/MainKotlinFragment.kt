package com.example.todoapp.kotlin

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.viewpager2.widget.ViewPager2
import com.example.todoapp.R
import com.example.todoapp.adapter.TabItemKotlinAdapter
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.viewmodel.TodoItemViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainKotlinFragment : Fragment() {

    private var fragmentMainBinding: FragmentMainBinding? = null
    private var mView: View? = null
    private var todoItemViewModel: TodoItemViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        mView = fragmentMainBinding!!.root
        todoItemViewModel = ViewModelProvider(this)[TodoItemViewModel::class.java]
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //set shared element back for recyclerview
        postponeEnterTransition()
        val parentView = view.parent as ViewGroup
        val preDrawListener = object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                parentView.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        }
        parentView.viewTreeObserver.addOnPreDrawListener(preDrawListener)
        super.onViewCreated(view, savedInstanceState)

        fragmentMainBinding!!.btnAdd.setOnClickListener {
            val extras: FragmentNavigator.Extras = FragmentNavigator.Extras.Builder()
                .addSharedElement(fragmentMainBinding!!.btnAdd, "add_fragment").build()
            findNavController(it).navigate(R.id.addItemKotlinFragment, null, null, extras)
        }
        val viewPager2 = requireView().findViewById<ViewPager2>(R.id.vpg)
        fragmentMainBinding!!.vpg.adapter =
            todoItemViewModel?.let { TabItemKotlinAdapter(requireActivity(), it) }
        val tabLayout = requireView().findViewById<TabLayout>(R.id.tloMenu)
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
        fragmentMainBinding!!.svSearch.editText.setOnEditorActionListener { _, _, _ ->
            fragmentMainBinding!!.sbSearchBar.text = fragmentMainBinding!!.svSearch.text
            fragmentMainBinding!!.svSearch.hide()
            todoItemViewModel!!.stringMutableLiveData.postValue(fragmentMainBinding!!.svSearch.text.toString())
            false
        }

        todoItemViewModel!!.listMutableLiveDataCheck.observe(
            requireActivity()
        ) { value -> fragmentMainBinding!!.btnClearAll.isEnabled = value!!.size > 0 }

        fragmentMainBinding!!.btnClearAll.setOnClickListener { clearItem() }
    }

    private fun clearItem() {
        MaterialAlertDialogBuilder(
            requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog
        ).setTitle("Confirm Clear All").setMessage("Are you sure?")
            .setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                todoItemViewModel!!.clearAllItem()
                Toast.makeText(activity, "Clear successfully", Toast.LENGTH_SHORT).show()
            }.setNegativeButton("No", null).show()
    }
}