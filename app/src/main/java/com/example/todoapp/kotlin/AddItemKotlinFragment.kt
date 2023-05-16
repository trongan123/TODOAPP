package com.example.todoapp.kotlin

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddItemKotlinBinding
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.TodoItemViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AddItemKotlinFragment : Fragment() {

    private var fragmentAddItemBinding: FragmentAddItemKotlinBinding? = null
    private var todoItemViewModel: TodoItemViewModel? = null
    private var datePickerCompleted: MaterialDatePicker<*>? = null
    private var datePickerCreated: MaterialDatePicker<*>? = null
    private val todoItem = TodoItem()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arrayOf("pending", "completed")
        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.dropdown_menu_popup_item, R.id.txtstyle,
            type
        )
        fragmentAddItemBinding!!.dropdownstatus.setAdapter(adapter)
        if (datePickerCreated!!.isAdded) {
            return
        }
        fragmentAddItemBinding!!.btnAdd.isEnabled = false
        fragmentAddItemBinding!!.btnAdd.setOnClickListener {
            try {
                addItem()
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }
        }
        fragmentAddItemBinding!!.btnclear.setOnClickListener {
            fragmentAddItemBinding!!.edttitle.setText("")
            this.fragmentAddItemBinding!!.edtdescription.setText("")
            fragmentAddItemBinding!!.edtcreatedDate.setText("")
            fragmentAddItemBinding!!.edtcompletedDate.setText("")
            fragmentAddItemBinding!!.dropdownstatus.setText("", false)
        }
        fragmentAddItemBinding!!.edtcreatedDate.setOnClickListener {
            if (datePickerCreated!!.isAdded) {
                return@setOnClickListener
            }
            datePickerCreated!!.show(parentFragmentManager, "Material_Date_Picker")
            datePickerCreated!!.addOnPositiveButtonClickListener { selection: Any? ->
                val calendar =
                    Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = (selection as Long?)!!
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = format.format(calendar.time)
                fragmentAddItemBinding!!.edtcreatedDate.setText(formattedDate)
            }
        }

        //create datePicker
        fragmentAddItemBinding!!.edtcompletedDate.setOnClickListener {
            if (datePickerCompleted!!.isAdded) {
                return@setOnClickListener
            }
            datePickerCompleted!!.show(parentFragmentManager, "Material_Date_Picker")
            datePickerCompleted!!.addOnPositiveButtonClickListener { selection ->
                val calendar =
                    Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = (selection as Long)
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = format.format(calendar.time)
                fragmentAddItemBinding!!.edtcompletedDate.setText(formattedDate)
            }
        }
        fragmentAddItemBinding!!.edttitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (Objects.requireNonNull(fragmentAddItemBinding!!.edttitle.text).toString()
                        .trim { it <= ' ' }
                        .isEmpty()
                ) {
                    fragmentAddItemBinding!!.tiltitle.error = "Field title can't empty"
                } else {
                    fragmentAddItemBinding!!.tiltitle.error = null
                }
                val check = checkvalidate()
                fragmentAddItemBinding!!.btnAdd.isEnabled = check
            }
        })
        fragmentAddItemBinding!!.edtdescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (Objects.requireNonNull(fragmentAddItemBinding!!.edtdescription.text).toString()
                        .trim { it <= ' ' }
                        .isEmpty()
                ) {
                    fragmentAddItemBinding!!.tildescription.error = "Field description can't empty"
                } else {
                    fragmentAddItemBinding!!.tildescription.error = null
                }
                val check = checkvalidate()
                fragmentAddItemBinding!!.btnAdd.isEnabled = check
            }
        })
        fragmentAddItemBinding!!.edtcreatedDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (Objects.requireNonNull(fragmentAddItemBinding!!.edtcreatedDate.text).toString()
                        .trim { it <= ' ' }
                        .isEmpty()
                ) {
                    fragmentAddItemBinding!!.tilcreatedDate.error = "Field created date can't empty"
                } else {
                    fragmentAddItemBinding!!.tilcreatedDate.error = null
                }
                val check = checkvalidate()
                fragmentAddItemBinding!!.btnAdd.isEnabled = check
            }
        })
        fragmentAddItemBinding!!.edtcompletedDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (Objects.requireNonNull(fragmentAddItemBinding!!.edtcompletedDate.text).toString()
                        .trim { it <= ' ' }
                        .isEmpty()
                ) {
                    fragmentAddItemBinding!!.tilcompletedDate.error = "Field completed date can't empty"
                } else {
                    fragmentAddItemBinding!!.tilcompletedDate.error = null
                }
                val check = checkvalidate()
                fragmentAddItemBinding!!.btnAdd.isEnabled = check
            }
        })
        fragmentAddItemBinding!!.dropdownstatus.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (Objects.requireNonNull(fragmentAddItemBinding!!.dropdownstatus.text).toString()
                        .trim { it <= ' ' }
                        .isEmpty()
                ) {
                    fragmentAddItemBinding!!.tilstatus.error = "Please choice a status"
                } else {
                    fragmentAddItemBinding!!.tilstatus.error = null
                }
                val check = checkvalidate()
                fragmentAddItemBinding!!.btnAdd.isEnabled = check
            }
        })
    }

    @Throws(ParseException::class)
    private fun addItem() {
        if (validation()) {
            val strtitle: String =
                Objects.requireNonNull(/* obj = */ fragmentAddItemBinding?.edttitle?.text)
                    .toString()
                    .trim { it <= ' ' }
            val strDes: String =
                Objects.requireNonNull(fragmentAddItemBinding!!.edtdescription.text).toString()
                    .trim { it <= ' ' }
            val credate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(
                    Objects.requireNonNull(fragmentAddItemBinding!!.edtcreatedDate.text)
                        .toString().trim { it <= ' ' })
            val comdate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(
                    Objects.requireNonNull(fragmentAddItemBinding!!.edtcompletedDate.text)
                        .toString().trim { it <= ' ' })
            val strStt: String = fragmentAddItemBinding!!.dropdownstatus.text.toString().trim()

            //update database
            todoItem.title = strtitle
            todoItem.description = strDes
            todoItem.createdDate = credate
            todoItem.completedDate = comdate
            todoItem.status = strStt
            todoItemViewModel!!.addItem(todoItem)
            Toast.makeText(activity, "Add success", Toast.LENGTH_SHORT).show()
            findNavController(requireView()).navigate(R.id.mainKotlinFragment)
        }
    }

    @Throws(ParseException::class)
    private fun validation(): Boolean {
        var check = true
        if (fragmentAddItemBinding!!.edttitle.text.toString().trim().isEmpty()) {
            fragmentAddItemBinding!!.tiltitle.error = "Field title can't empty"
            check = false
        }
        if (fragmentAddItemBinding!!.edtdescription.text.toString().trim().isEmpty()) {
            fragmentAddItemBinding!!.tildescription.error = "Field description can't empty"
            check = false
        }
        if (fragmentAddItemBinding!!.edtcreatedDate.text.toString().trim().isEmpty()) {
            fragmentAddItemBinding!!.tilcreatedDate.error = "Field created date can't empty"
            check = false
        }
        if (fragmentAddItemBinding!!.edtcompletedDate.text.toString().trim().isEmpty()) {
            fragmentAddItemBinding!!.tilcompletedDate.error = "Field completed date can't empty"
            check = false
        }
        if (fragmentAddItemBinding!!.dropdownstatus.text.toString().trim().isEmpty()) {
            fragmentAddItemBinding!!.tilstatus.error = "Please choice a status"
            check = false
        }
        if (!check) {
            return false
        }
        val credate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .parse(fragmentAddItemBinding!!.edtcreatedDate.text.toString().trim())
        val comdate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .parse(fragmentAddItemBinding!!.edtcompletedDate.text.toString().trim())
        if (credate != null) {
            if (credate > comdate) {
                fragmentAddItemBinding!!.tilcompletedDate.error =
                    "Completed date must be after created date"
                check = false
            }
        }
        return check
    }

    private fun checkvalidate(): Boolean {
        var check: Boolean = Objects.requireNonNull(fragmentAddItemBinding!!.edttitle.text)
            .toString().trim { it <= ' ' }.isNotEmpty()
        if (Objects.requireNonNull(fragmentAddItemBinding!!.edtdescription.text)
                .toString().trim { it <= ' ' }.isEmpty()
        ) {
            check = false
        }
        if (Objects.requireNonNull(fragmentAddItemBinding!!.edtcreatedDate.text)
                .toString().trim { it <= ' ' }.isEmpty()
        ) {
            check = false
        }
        if (Objects.requireNonNull(fragmentAddItemBinding!!.edtcompletedDate.text)
                .toString().trim { it <= ' ' }.isEmpty()
        ) {
            check = false
        }
        if (Objects.requireNonNull(fragmentAddItemBinding!!.dropdownstatus.text)
                .toString().trim { it <= ' ' }.isEmpty()
        ) {
            check = false
        }
        return check
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedElementEnterTransition = ChangeBounds()
        fragmentAddItemBinding = FragmentAddItemKotlinBinding.inflate(inflater, container, false)
        val mView: View = fragmentAddItemBinding!!.root
        todoItemViewModel = ViewModelProvider(this)[TodoItemViewModel::class.java]
        fragmentAddItemBinding!!.todoItemViewModel = todoItemViewModel

        datePickerCreated = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePickerCompleted = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        fragmentAddItemBinding!!.edtcompletedDate.inputType = (InputType.TYPE_CLASS_DATETIME
                or InputType.TYPE_DATETIME_VARIATION_DATE)
        fragmentAddItemBinding!!.edtcreatedDate.inputType = (InputType.TYPE_CLASS_DATETIME
                or InputType.TYPE_DATETIME_VARIATION_DATE)

        // Inflate the layout for this fragment
        return mView
    }

}