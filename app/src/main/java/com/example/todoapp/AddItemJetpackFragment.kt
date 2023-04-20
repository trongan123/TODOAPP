package com.example.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.AddItemFragmentViewModal

class AddItemJetpackFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var viewModel: AddItemFragmentViewModal =
            ViewModelProvider(requireActivity()).get(AddItemFragmentViewModal::class.java)
        return ComposeView(requireContext()).apply {
//            titleItem = ""
//            createdDateItem =""
//            completedDateItem =""
//            statusItem =""
//            descriptionItem= ""
//            todoItem = TodoItem()
//            setContent {
//                Column(modifier = Modifier.padding(30.dp)) {
//                    Texttitle()
//                    CommonSpace()
//                    TextDescription()
//                    CommonSpace()
//                    TextCreatedDate()
//                    CommonSpace()
//                    TextCompletedDate()
//                    CommonSpace()
//                    dropDownMenuStatus()
//
//                }
//              //  LayoutAddButton(viewModel, requireView())
//
//            }

        }
    }
}
