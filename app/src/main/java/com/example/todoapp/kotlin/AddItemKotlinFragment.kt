package com.example.todoapp.kotlin

import android.os.Bundle
import android.text.Editable
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
import com.example.todoapp.databinding.FragmentAddItemBinding
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.TodoItemViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AddItemKotlinFragment : Fragment() {

    private val stringDateFormat: String = "yyyy-MM-dd"
    private var fragmentAddItemBinding: FragmentAddItemBinding? = null
    private var todoItemViewModel: TodoItemViewModel? = null
    private val todoItem = TodoItem()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        sharedElementEnterTransition = ChangeBounds()
        fragmentAddItemBinding = FragmentAddItemBinding.inflate(inflater, container, false)
        val mView: View = fragmentAddItemBinding!!.root
        todoItemViewModel = ViewModelProvider(this)[TodoItemViewModel::class.java]
        // Inflate the layout for this fragment
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arrayOf("pending", "completed")
        val adapter = ArrayAdapter(
            requireActivity(), R.layout.dropdown_menu_popup_item, R.id.txtStyle, type
        )
        fragmentAddItemBinding!!.dropDownStatus.setAdapter(adapter)
        fragmentAddItemBinding!!.btnAdd.isEnabled = false

        fragmentAddItemBinding!!.btnAdd.setOnClickListener {
            try {
                addItem()
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }
        }
        fragmentAddItemBinding!!.btnClear.setOnClickListener {
            clearText()
        }
        fragmentAddItemBinding!!.edtCreatedDate.setOnClickListener {
            addDatePicker(fragmentAddItemBinding!!.edtCreatedDate)
        }

        //create datePicker
        fragmentAddItemBinding!!.edtCompletedDate.setOnClickListener {
            addDatePicker(fragmentAddItemBinding!!.edtCompletedDate)
        }
        fragmentAddItemBinding!!.edtTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {//
                //Method empty
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //Method empty
            }

            override fun afterTextChanged(editable: Editable) {
                setErrorEditText(
                    fragmentAddItemBinding!!.edtTitle, "Field title can't empty"
                )
            }
        })
        fragmentAddItemBinding!!.edtDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //Method empty
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //Method empty
            }

            override fun afterTextChanged(editable: Editable) {
                setErrorEditText(
                    fragmentAddItemBinding!!.edtDescription, "Field description can't empty"
                )
            }
        })
        fragmentAddItemBinding!!.edtCreatedDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //Method empty
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //Method empty
            }

            override fun afterTextChanged(editable: Editable) {
                setErrorEditText(
                    fragmentAddItemBinding!!.edtCreatedDate, "Field created date can't empty"
                )
            }
        })
        fragmentAddItemBinding!!.edtCompletedDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //Method empty
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //Method empty
            }

            override fun afterTextChanged(editable: Editable) {
                setErrorEditText(
                    fragmentAddItemBinding!!.edtCompletedDate, "Field completed date can't empty"
                )
            }
        })
        fragmentAddItemBinding!!.dropDownStatus.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //Method empty
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //Method empty
            }

            override fun afterTextChanged(editable: Editable) {
                if (Objects.requireNonNull(fragmentAddItemBinding!!.dropDownStatus.text).toString()
                        .trim { it <= ' ' }.isEmpty()
                ) {
                    fragmentAddItemBinding!!.dropDownStatus.error = "Please choice a status"
                } else {
                    fragmentAddItemBinding!!.dropDownStatus.error = null
                }
                val check = checkValidate()
                fragmentAddItemBinding!!.btnAdd.isEnabled = check
            }
        })
    }

    @Throws(ParseException::class)
    private fun addItem() {
        if (validation()) {
            val strtitle: String =
                Objects.requireNonNull(fragmentAddItemBinding?.edtTitle?.text)
                    .toString().trim { it <= ' ' }
            val strDes: String =
                Objects.requireNonNull(fragmentAddItemBinding!!.edtDescription.text).toString()
                    .trim { it <= ' ' }
            val credate = SimpleDateFormat(
                stringDateFormat, Locale.getDefault()
            ).parse(Objects.requireNonNull(
                fragmentAddItemBinding!!.edtCreatedDate.text
            ).toString().trim { it <= ' ' })
            val comdate = SimpleDateFormat(
                stringDateFormat, Locale.getDefault()
            ).parse(Objects.requireNonNull(
                fragmentAddItemBinding!!.edtCompletedDate.text
            ).toString().trim { it <= ' ' })
            val strStt: String = fragmentAddItemBinding!!.dropDownStatus.text.toString().trim()

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
        if (fragmentAddItemBinding!!.edtTitle.text.toString().trim().isEmpty()) {
            fragmentAddItemBinding!!.edtTitle.error = "Field title can't empty"
            check = false
        }
        if (fragmentAddItemBinding!!.edtDescription.text.toString().trim().isEmpty()) {
            fragmentAddItemBinding!!.edtDescription.error = "Field description can't empty"
            check = false
        }
        if (fragmentAddItemBinding!!.edtCreatedDate.text.toString().trim().isEmpty()) {
            fragmentAddItemBinding!!.edtCreatedDate.error = "Field created date can't empty"
            check = false
        }
        if (fragmentAddItemBinding!!.edtCompletedDate.text.toString().trim().isEmpty()) {
            fragmentAddItemBinding!!.edtCompletedDate.error = "Field completed date can't empty"
            check = false
        }
        if (fragmentAddItemBinding!!.dropDownStatus.text.toString().trim().isEmpty()) {
            fragmentAddItemBinding!!.dropDownStatus.error = "Please choice a status"
            check = false
        }
        if (!check) {
            return false
        }
        val createdDate = SimpleDateFormat(
            stringDateFormat, Locale.getDefault()
        ).parse(fragmentAddItemBinding!!.edtCreatedDate.text.toString().trim())
        val completedDate = SimpleDateFormat(
            stringDateFormat, Locale.getDefault()
        ).parse(fragmentAddItemBinding!!.edtCompletedDate.text.toString().trim())
        if (createdDate != null && createdDate > completedDate) {
            fragmentAddItemBinding!!.tilCompletedDate.error =
                "Completed date must be after created date"
            check = false
        }
        return check
    }

    private fun clearText() {
        fragmentAddItemBinding!!.edtTitle.setText("")
        fragmentAddItemBinding!!.edtTitle.error = null
        fragmentAddItemBinding!!.edtDescription.setText("")
        fragmentAddItemBinding!!.edtDescription.error = null
        fragmentAddItemBinding!!.edtCreatedDate.setText("")
        fragmentAddItemBinding!!.edtCreatedDate.error = null
        fragmentAddItemBinding!!.edtCompletedDate.setText("")
        fragmentAddItemBinding!!.edtCompletedDate.error = null
        fragmentAddItemBinding!!.dropDownStatus.setText("", false)
        fragmentAddItemBinding!!.dropDownStatus.error = null
    }

    //create date picker
    private fun addDatePicker(textDate: TextInputEditText) {
        val datePickerCreated: MaterialDatePicker<Long>
        MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()
            .also { datePickerCreated = it }
        if (datePickerCreated.isAdded) {
            return
        }
        datePickerCreated.show(parentFragmentManager, "Material_Date_Picker")
        datePickerCreated.addOnPositiveButtonClickListener { selection: Long? ->
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = selection!!
            val format = SimpleDateFormat(stringDateFormat, Locale.getDefault())
            val formattedDate = format.format(calendar.time)
            textDate.setText(formattedDate)
        }
    }

    private fun setErrorEditText(textEdit: TextInputEditText, errorMessage: String) {
        if (Objects.requireNonNull(textEdit.text).toString().trim { it <= ' ' }.isEmpty()) {
            textEdit.error = errorMessage
        } else {
            textEdit.error = null
        }
        val check: Boolean = checkValidate()
        fragmentAddItemBinding!!.btnAdd.isEnabled = check
    }

    private fun checkValidate(): Boolean {
        var check: Boolean =
            Objects.requireNonNull(fragmentAddItemBinding!!.edtTitle.text).toString()
                .trim { it <= ' ' }.isNotEmpty()
        if (Objects.requireNonNull(fragmentAddItemBinding!!.edtDescription.text).toString()
                .trim { it <= ' ' }.isEmpty()
        ) {
            check = false
        }
        if (Objects.requireNonNull(fragmentAddItemBinding!!.edtCreatedDate.text).toString()
                .trim { it <= ' ' }.isEmpty()
        ) {
            check = false
        }
        if (Objects.requireNonNull(fragmentAddItemBinding!!.edtCompletedDate.text).toString()
                .trim { it <= ' ' }.isEmpty()
        ) {
            check = false
        }
        if (Objects.requireNonNull(fragmentAddItemBinding!!.dropDownStatus.text).toString()
                .trim { it <= ' ' }.isEmpty()
        ) {
            check = false
        }
        return check
    }
}