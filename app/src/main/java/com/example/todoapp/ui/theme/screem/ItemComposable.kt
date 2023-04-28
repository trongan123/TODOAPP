package com.example.todoapp.ui.theme.screem

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.TodoItemViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.text.SimpleDateFormat


var titleItem: String = ""
var descriptionItem: String = ""
var createdDateItem: String = ""
var completedDateItem: String = ""
var statusItem: String = ""
var todoItem: TodoItem = TodoItem()

data class appListTodo(val todolist : List<TodoItem>)

val localListTodo = compositionLocalOf { appListTodo(todolist = ArrayList()) }


@Composable
fun Texttitle() {
    var title by remember {
        mutableStateOf(titleItem)
    }
    OutlinedTextField(
        value = title,
        onValueChange = {
            title = it
            titleItem = title
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Title") },
        label = { Text("Title") },
    )
}

@Composable
fun TextDescription() {
    var description by remember {
        mutableStateOf(descriptionItem)
    }
    OutlinedTextField(
        value = description,
        onValueChange = {
            description = it
            descriptionItem = description
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Description") },
        label = { Text("Description") },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextCreatedDate() {
    val calendarState = rememberSheetState()
    var createdDate by remember {
        mutableStateOf(createdDateItem)
    }
    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { date ->
            createdDate = date.toString()
            createdDateItem = createdDate
        }
    )
    OutlinedTextField(

        value = createdDate,
        onValueChange = {
            createdDate = it
        },
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()

            .clickable(
                onClick = { calendarState.show() }),
        placeholder = { Text("CreatedDate") },
        label = { Text("CreatedDate") },
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium)
        )

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextCompletedDate() {
    var completedDate by remember {
        mutableStateOf(completedDateItem)
    }
    val calendarState = rememberSheetState()

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { date ->
            completedDate = date.toString()
            completedDateItem = completedDate
        }
    )
    OutlinedTextField(
        value = completedDate,
        onValueChange = {
            completedDate = it
        },
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { calendarState.show() }),

        placeholder = { Text("CompletedDate") },
        label = { Text("CompletedDate") },
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium)
        )

    )
}






@Composable
fun CommonSpace() {
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun AddButton(viewModel : TodoItemViewModel,backHome:()->Unit ) {

    Button(
        onClick = {
            todoItem = TodoItem()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            //update database
            todoItem.title = titleItem
            todoItem.description = descriptionItem
            todoItem.createdDate = formatter.parse(createdDateItem)
            todoItem.completedDate = formatter.parse(completedDateItem)
            todoItem.status = statusItem

            viewModel.addItem(todoItem)
            backHome()
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(bottom = 100.dp)
    ) {
        Text("ADD")
    }
}
@Composable
fun DeleteButton(viewModel : TodoItemViewModel,backHome:()->Unit) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    Button(
        onClick = {
            showDeleteConfirm = true
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(bottom = 100.dp)
    ) {
        Text("Delete")
    }
    if (showDeleteConfirm) {
        ConfirmDialog(
            content = "Confirm Delete?",
            onDismiss = { showDeleteConfirm = false },
            onConfirm = {
                showDeleteConfirm = false
                viewModel.deleteItem(todoItem)
                backHome()
            }
        )
    }
}


@Composable
fun UpdateButton(viewModel : TodoItemViewModel,backHome:()->Unit) {
    Button(
        onClick = {

            val formatter = SimpleDateFormat("yyyy-MM-dd")
            //update database
            todoItem.title = titleItem
            todoItem.description = descriptionItem
            todoItem.createdDate = formatter.parse(createdDateItem)
            todoItem.completedDate = formatter.parse(completedDateItem)
            todoItem.status = statusItem

            viewModel.updateItem(todoItem)
            backHome()
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(bottom = 100.dp)
    ) {
        Text("Update")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClearButton() {
    val calendarState = rememberSheetState()

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { date ->
            Log.e("TAG", "ClearButton: $date")
        }
    )

    Button(
        onClick = {
            calendarState.show()
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(bottom = 100.dp)
    ) {
        Text("CLEAR")
    }
}






@Composable
fun DropDownMenuStatus() {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("pending", "completed")
    var selectedText by remember { mutableStateOf(statusItem) }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {
                selectedText = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { Text("Status") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    statusItem = label
                    expanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>, viewModel: TodoItemViewModel) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
            viewModel.stringMutableLiveData.postValue(value.text.trim())
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = androidx.compose.material.MaterialTheme.colors.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}