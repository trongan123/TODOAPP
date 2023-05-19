package com.example.todoapp.kotlin

import android.app.AlertDialog
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentUpdateItemKotlinBinding
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.TodoItemViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class UpdateItemKotlinFragment : Fragment() {

    private val stringDateFormat: String = "yyyy-MM-dd"
    private var fragmentUpdateItemBinding: FragmentUpdateItemKotlinBinding? = null
    private var todoItemViewModel: TodoItemViewModel? = null
    private var datePickerCompleted: MaterialDatePicker<*>? = null
    private var datePickerCreated: MaterialDatePicker<*>? = null
    private var todoItem: TodoItem? = TodoItem()
    private var mView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arrayOf("pending", "completed")
        val adapter = ArrayAdapter(
            requireActivity(), R.layout.dropdown_menu_popup_item, R.id.txtStyle, type
        )
        fragmentUpdateItemBinding!!.dropDownStatus.setAdapter(adapter)
        initUi()
        fragmentUpdateItemBinding!!.btnUpdate.setOnClickListener {
            try {
                updateItem()
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }
        }
        fragmentUpdateItemBinding!!.btnDelete.setOnClickListener {
            try {
                deleteItem()
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds()
        fragmentUpdateItemBinding =
            inflater.let { FragmentUpdateItemKotlinBinding.inflate(it, container, false) }
        mView = fragmentUpdateItemBinding!!.root
        todoItemViewModel = ViewModelProvider(this)[TodoItemViewModel::class.java]
        fragmentUpdateItemBinding!!.todoItemViewModel = todoItemViewModel
        datePickerCreated = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()
        datePickerCompleted = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()
        return mView
    }

    @Throws(ParseException::class)
    private fun updateItem() {
        if (validation()) {
            val strtitle: String = fragmentUpdateItemBinding!!.edtTitle.text.toString().trim()
            val strDes: String = fragmentUpdateItemBinding!!.edtDescription.text.toString().trim()
            val credate = SimpleDateFormat(stringDateFormat, Locale.getDefault()).parse(
                fragmentUpdateItemBinding!!.edtCreatedDate.text.toString().trim()
            )
            val comdate = SimpleDateFormat(stringDateFormat, Locale.getDefault()).parse(
                fragmentUpdateItemBinding!!.edtCompletedDate.text.toString().trim()
            )
            val strStt: String = fragmentUpdateItemBinding!!.dropDownStatus.text.toString().trim()

            //update database
            todoItem!!.title = strtitle
            todoItem!!.description = strDes
            todoItem!!.createdDate = credate
            todoItem!!.completedDate = comdate
            todoItem!!.status = strStt
            todoItemViewModel!!.updateItem(todoItem)
            Toast.makeText(activity, "Update success", Toast.LENGTH_SHORT).show()
            findNavController(requireView()).navigate(R.id.mainKotlinFragment)
        }
    }

    @Throws(ParseException::class)
    private fun deleteItem() {
        val strtitle: String = fragmentUpdateItemBinding!!.edtTitle.text.toString().trim()
        val strDes: String = fragmentUpdateItemBinding!!.edtDescription.text.toString().trim()
        val credate = SimpleDateFormat(stringDateFormat, Locale.getDefault()).parse(
            fragmentUpdateItemBinding!!.edtCreatedDate.text.toString().trim()
        )
        val comdate = SimpleDateFormat(stringDateFormat, Locale.getDefault()).parse(
            fragmentUpdateItemBinding!!.edtCompletedDate.text.toString().trim()
        )
        val strStt: String = fragmentUpdateItemBinding!!.dropDownStatus.text.toString().trim()

        //update database
        todoItem!!.title = strtitle
        todoItem!!.description = strDes
        todoItem!!.createdDate = credate
        todoItem!!.completedDate = comdate
        todoItem!!.status = strStt
        AlertDialog.Builder(context).setTitle("Confirm delete").setMessage("Are you sure?")
            .setPositiveButton("Yes") { _, _ ->
                todoItemViewModel!!.deleteItem(todoItem)
                Toast.makeText(activity, "Delete successfully", Toast.LENGTH_SHORT).show()
                findNavController(requireView()).navigate(R.id.mainKotlinFragment)
            }.setNegativeButton("No", null).show()
        Toast.makeText(activity, "Delete success", Toast.LENGTH_SHORT).show()
    }

    private fun initUi() {
        todoItem = requireArguments().getSerializable("objectTodoItem") as TodoItem?

        val transition = requireArguments().getString("transition")
        fragmentUpdateItemBinding!!.constraintLayout.transitionName = transition

        val dateFormat: DateFormat = SimpleDateFormat(stringDateFormat, Locale.getDefault())
        if (todoItem != null) {
            fragmentUpdateItemBinding!!.edtTitle.setText(todoItem!!.title)
            fragmentUpdateItemBinding!!.edtDescription.setText(todoItem!!.description)
            fragmentUpdateItemBinding!!.edtCreatedDate.setText(dateFormat.format(todoItem!!.createdDate))
            fragmentUpdateItemBinding!!.edtCompletedDate.setText(dateFormat.format(todoItem!!.completedDate))
            fragmentUpdateItemBinding!!.dropDownStatus.setText(todoItem!!.status, false)
        }
        fragmentUpdateItemBinding!!.edtCreatedDate.setOnClickListener {
            datePickerCreated!!.show(parentFragmentManager, "Material_Date_Picker")
            datePickerCreated!!.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = (selection as Long)
                val format = SimpleDateFormat(stringDateFormat, Locale.getDefault())
                val formattedDate = format.format(calendar.time)
                fragmentUpdateItemBinding!!.edtCreatedDate.setText(formattedDate)
            }
        }

        //create datePicker
        fragmentUpdateItemBinding!!.edtCompletedDate.setOnClickListener {
            datePickerCompleted!!.show(parentFragmentManager, "Material_Date_Picker")
            datePickerCompleted!!.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = (selection as Long)
                val format = SimpleDateFormat(stringDateFormat, Locale.getDefault())
                val formattedDate = format.format(calendar.time)
                fragmentUpdateItemBinding!!.edtCompletedDate.setText(formattedDate)
            }
        }
    }

    @Throws(ParseException::class)
    private fun validation(): Boolean {
        var check = true
        if (this.fragmentUpdateItemBinding!!.edtTitle.text.toString().trim().isEmpty()) {
            fragmentUpdateItemBinding!!.edtTitle.error = "Field title can't empty"
            check = false
        }
        if (fragmentUpdateItemBinding!!.edtDescription.text.toString().trim().isEmpty()) {
            fragmentUpdateItemBinding!!.edtDescription.error = "Field description can't empty"
            check = false
        }
        if (fragmentUpdateItemBinding!!.edtCreatedDate.text.toString().trim().isEmpty()) {
            fragmentUpdateItemBinding!!.edtCreatedDate.error = "Field created date can't empty"
            check = false
        }
        if (fragmentUpdateItemBinding!!.edtCompletedDate.text.toString().trim().isEmpty()) {
            fragmentUpdateItemBinding!!.edtCompletedDate.error = "Field completed date can't empty"
            check = false
        }
        if (fragmentUpdateItemBinding!!.dropDownStatus.text.toString().trim().isEmpty()) {
            fragmentUpdateItemBinding!!.dropDownStatus.error = "Please choice a status"
            check = false
        }
        if (!check) {
            return false
        }
        val createdDate = SimpleDateFormat(
            stringDateFormat, Locale.getDefault()
        ).parse(fragmentUpdateItemBinding!!.edtCreatedDate.text.toString().trim())
        val completedDate = SimpleDateFormat(
            stringDateFormat, Locale.getDefault()
        ).parse(fragmentUpdateItemBinding!!.edtCompletedDate.text.toString().trim())
        if (createdDate != null && createdDate > completedDate) {
            fragmentUpdateItemBinding!!.edtCompletedDate.error =
                "Completed date must be after created date"
            check = false
        }
        return check
    }

}