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
            requireActivity(),
            R.layout.dropdown_menu_popup_item, R.id.txtstyle,
            type
        )
        fragmentUpdateItemBinding!!.dropdownstatus.setAdapter(adapter)
        initUi()
        fragmentUpdateItemBinding!!.btnupdate.setOnClickListener {
            try {
                updateItem()
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }
        }
        fragmentUpdateItemBinding!!.btndelete.setOnClickListener {
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds()
        fragmentUpdateItemBinding =
            inflater.let { FragmentUpdateItemKotlinBinding.inflate(it, container, false) }
        mView = fragmentUpdateItemBinding!!.root
        todoItemViewModel = ViewModelProvider(this)[TodoItemViewModel::class.java]
        fragmentUpdateItemBinding!!.todoItemViewModel =todoItemViewModel
        datePickerCreated = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePickerCompleted = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        return mView
    }

    @Throws(ParseException::class)
    private fun updateItem() {
        if (validation()) {
            val strtitle: String = fragmentUpdateItemBinding!!.edttitle.text.toString().trim()
            val strDes: String =
                fragmentUpdateItemBinding!!.edtdescription.text.toString().trim()
            val credate = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(
                fragmentUpdateItemBinding!!.edtcreatedDate.text.toString().trim()
            )
            val comdate = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(
                fragmentUpdateItemBinding!!.edtcompletedDate.text.toString().trim()
            )
            val strStt: String =
                fragmentUpdateItemBinding!!.dropdownstatus.text.toString().trim()

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
        val strtitle: String = fragmentUpdateItemBinding!!.edttitle.text.toString().trim()
        val strDes: String = fragmentUpdateItemBinding!!.edtdescription.text.toString().trim()
        val credate = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(
            fragmentUpdateItemBinding!!.edtcreatedDate.text.toString().trim()
        )
        val comdate = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(
            fragmentUpdateItemBinding!!.edtcompletedDate.text.toString().trim()
        )
        val strStt: String = fragmentUpdateItemBinding!!.dropdownstatus.text.toString().trim()

        //update database
        todoItem!!.title = strtitle
        todoItem!!.description = strDes
        todoItem!!.createdDate = credate
        todoItem!!.completedDate = comdate
        todoItem!!.status = strStt
        AlertDialog.Builder(context)
            .setTitle("Confirm delete")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { _, _ ->
                todoItemViewModel!!.deleteItem(todoItem)
                Toast.makeText(activity, "Delete successfully", Toast.LENGTH_SHORT).show()
                findNavController(requireView()).navigate(R.id.mainKotlinFragment)
            }
            .setNegativeButton("No", null)
            .show()
        Toast.makeText(activity, "Delete success", Toast.LENGTH_SHORT).show()
    }

    private fun initUi() {
        todoItem = requireArguments().getSerializable("object_TodoItem") as TodoItem?

        val transition = requireArguments().getString("transition")
        fragmentUpdateItemBinding!!.constraint.transitionName = transition

        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        if (todoItem != null) {
            fragmentUpdateItemBinding!!.edttitle.setText(todoItem!!.title)
            fragmentUpdateItemBinding!!.edtdescription.setText(todoItem!!.description)
            fragmentUpdateItemBinding!!.edtcreatedDate.setText(dateFormat.format(todoItem!!.createdDate))
            fragmentUpdateItemBinding!!.edtcompletedDate.setText(dateFormat.format(todoItem!!.completedDate))
            fragmentUpdateItemBinding!!.dropdownstatus.setText(todoItem!!.status, false)
        }
        fragmentUpdateItemBinding!!.edtcreatedDate.setOnClickListener {
            datePickerCreated!!.show(parentFragmentManager, "Material_Date_Picker")
            datePickerCreated!!.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = (selection as Long)
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = format.format(calendar.time)
                fragmentUpdateItemBinding!!.edtcreatedDate.setText(formattedDate)
            }
        }

        //create datePicker
        fragmentUpdateItemBinding!!.edtcompletedDate.setOnClickListener {
            datePickerCompleted!!.show(parentFragmentManager, "Material_Date_Picker")
            datePickerCompleted!!.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = (selection as Long)
                val format = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
                val formattedDate = format.format(calendar.time)
                fragmentUpdateItemBinding!!.edtcompletedDate.setText(formattedDate)
            }
        }
    }

    @Throws(ParseException::class)
    private fun validation(): Boolean {
        var check = true
        if (fragmentUpdateItemBinding!!.edttitle.text.toString().trim().isEmpty()) {
            fragmentUpdateItemBinding!!.edttitle.error = "Field title can't empty"
            check = false
        }
        if (fragmentUpdateItemBinding!!.edtdescription.text.toString().trim().isEmpty()) {
            fragmentUpdateItemBinding!!.edtdescription.error = "Field description can't empty"
            check = false
        }
        if (fragmentUpdateItemBinding!!.edtcreatedDate.text.toString().trim().isEmpty()) {
            fragmentUpdateItemBinding!!.edtcreatedDate.error = "Field created date can't empty"
            check = false
        }
        if (fragmentUpdateItemBinding!!.edtcompletedDate.text.toString().trim().isEmpty()) {
            fragmentUpdateItemBinding!!.edtcompletedDate.error = "Field completed date can't empty"
            check = false
        }
        if (fragmentUpdateItemBinding!!.dropdownstatus.text.toString().trim().isEmpty()) {
            fragmentUpdateItemBinding!!.dropdownstatus.error = "Please choice a status"
            check = false
        }
        if (!check) {
            return false
        }
        val credate = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
            .parse(fragmentUpdateItemBinding!!.edtcreatedDate.text.toString().trim())
        val comdate = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
            .parse(fragmentUpdateItemBinding!!.edtcompletedDate.text.toString().trim())
        if (credate != null) {
            if (credate > comdate) {
                fragmentUpdateItemBinding!!.edtcompletedDate.error = "Completed date must be after created date"
                check = false
            }
        }
        return check
    }

}