package com.example.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.theme.screem.*
import com.example.todoapp.viewmodel.UpdateItemFragmentViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat

class UpdateItemJetpackFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val data = arguments
        todoItem = data?.getSerializable("object_TodoItem") as TodoItem
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        if (todoItem != null) {
            titleItem = todoItem.title
            descriptionItem = todoItem.description
            statusItem = todoItem.status
            createdDateItem = dateFormat.format(todoItem.createdDate)
            completedDateItem = dateFormat.format(todoItem.completedDate)
        }
        var viewModel: UpdateItemFragmentViewModel =
            ViewModelProvider(requireActivity()).get(UpdateItemFragmentViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme() {
                    Column(modifier = Modifier.padding(30.dp)) {
                        Texttitle()
                        CommonSpace()
                        TextDescription()
                        CommonSpace()
                        TextCreatedDate()
                        CommonSpace()
                        TextCompletedDate()
                        CommonSpace()
                        dropDownMenuStatus()
                    }
                    LayoutUpdateButton(viewModel,requireView())

                }

            }

        }
    }
}