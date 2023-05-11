package com.example.todoapp.ui.theme.screem

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import com.example.todoapp.R
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.TodoItemViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.text.SimpleDateFormat
import java.util.*


var titleItem: String = ""
var descriptionItem: String = ""
var createdDateItem: String = ""
var completedDateItem: String = ""
var statusItem: String = ""

var errordescriptionItem: String = ""
var errorcreatedDateItem: String = ""
var errorcompletedDateItem: String = ""
var errorstatusItem: String = ""


var todoItem: TodoItem = TodoItem()

data class appListTodo(val todolist: List<TodoItem>)

val localListTodo = compositionLocalOf { appListTodo(todolist = ArrayList()) }

var checkValidate = false

@Composable
fun Texttitle() {
    var title by remember {
        mutableStateOf(titleItem)
    }
    var error by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = title,
        onValueChange = {
            title = it
            titleItem = title
            if (it.equals("")) {
                error = "Field title can't empty"
            } else {
                error = ""
            }
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Title") },
        label = { Text("Title") },
    )
    Text(text = error, color = Color.Red)
}

@Composable
fun TextDescription() {
    var description by remember {
        mutableStateOf(descriptionItem)
    }
    var error by remember {
        mutableStateOf(errordescriptionItem)
    }
    OutlinedTextField(
        value = description,
        onValueChange = {
            description = it
            descriptionItem = description
            if (it.equals("")) {
                error = "Field description can't empty"
            } else {
                error = ""
            }
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Description") },
        label = { Text("Description") },
    )
    Text(text =error , color = Color.Red)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextCreatedDate() {
    val calendarState = rememberSheetState()
    var createdDate by remember {
        mutableStateOf(createdDateItem)
    }
    var error by remember {
        mutableStateOf(errorcreatedDateItem)
    }
    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { date ->
            createdDate = date.toString()
            createdDateItem = createdDate
            if (createdDate.equals("")) {
                error = "Field created date can't empty"
            } else {
                error = ""
            }
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
    Text(text = error, color = Color.Red)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextCompletedDate() {
    var completedDate by remember {
        mutableStateOf(completedDateItem)
    }
    val calendarState = rememberSheetState()
    var error by remember {
        mutableStateOf(errorcompletedDateItem)
    }
    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { date ->
            completedDate = date.toString()
            completedDateItem = completedDate
            if (completedDate.equals("")) {
                error = "Field completed date can't empty"
            } else {
                error = ""
            }
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
    Text(text = error, color = Color.Red)
}


@Composable
fun CommonSpace() {
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun AddButton(viewModel: TodoItemViewModel, backHome: () -> Unit) {

    Button(
        onClick = {
            checkValidate()
            if (checkValidate) {
                todoItem = TodoItem()
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                //update database
                todoItem.title = titleItem
                todoItem.description = descriptionItem
                todoItem.createdDate = formatter.parse(createdDateItem)
                todoItem.completedDate = formatter.parse(completedDateItem)
                todoItem.status = statusItem

                viewModel.addItem(todoItem)
                backHome()
            }
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(bottom = 100.dp)
    ) {
        Text("ADD")
    }
}

@Composable
fun DeleteButton(
    viewModel: TodoItemViewModel,
    backHome: () -> Unit,
    context: Context = LocalContext.current.applicationContext,
    spaceBetweenElements: Dp = 18.dp,
    negativeButtonColor: Color = Color(0xFF35898F),
    positiveButtonColor: Color = Color(0xFFFF0000)
) {
    //   var showDeleteConfirm by remember { mutableStateOf(false) }
    var dialogOpen by remember {
        mutableStateOf(false)
    }
    Button(
        onClick = {
            dialogOpen = true
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(bottom = 100.dp)
    ) {
        Text("Delete")
    }
    if (dialogOpen) {
        Dialog(onDismissRequest = {
            dialogOpen = false
        }
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.92f),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    // text and buttons
                    Column(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(percent = 10)
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(height = 36.dp))

                        Text(
                            text = "Delete?",
                            fontSize = 24.sp
                        )

                        Spacer(modifier = Modifier.height(height = spaceBetweenElements))
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "Are you sure, you want to delete the item?",
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(height = spaceBetweenElements))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            DialogButton(buttonColor = negativeButtonColor, buttonText = "No") {
                                dialogOpen = false
                            }
                            DialogButton(
                                buttonColor = positiveButtonColor,
                                buttonText = "Yes"
                            ) {
                                dialogOpen = false
                                Toast
                                    .makeText(
                                        context,
                                        "Delete item successfull",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                viewModel.deleteItem(todoItem)
                                backHome()
                            }
                        }
                        Spacer(modifier = Modifier.height(height = spaceBetweenElements * 2))
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.trast),
                        contentDescription = "Delete Icon",
                        tint = positiveButtonColor,
                        modifier = Modifier
                            .background(color = Color.White, shape = CircleShape)
                            .border(
                                width = 2.dp,
                                shape = CircleShape,
                                color = positiveButtonColor
                            )
                            .padding(all = 16.dp)
                            .align(alignment = Alignment.TopCenter)
                    )
                }
            }
        }
    }
//    if (showDeleteConfirm) {
//        ConfirmDialog(
//            content = "Confirm Delete?",
//            onDismiss = { showDeleteConfirm = false },
//            onConfirm = {
//                showDeleteConfirm = false
//                viewModel.deleteItem(todoItem)
//                backHome()
//            }
//        )
//    }
}

fun checkValidate() {
    checkValidate = true
    if (titleItem.isEmpty()) {
        checkValidate = false
    } else {

    }
    if (descriptionItem.isEmpty()) {
        errordescriptionItem = R.string.errordescription.toString()
        checkValidate = false
    } else {
        errordescriptionItem = ""
    }
    if (createdDateItem.isEmpty()) {
        errorcreatedDateItem = R.string.errorcreateddate.toString()
        checkValidate = false
    } else {
        errorcreatedDateItem = ""
    }
    if (completedDateItem.isEmpty()) {
        errorcompletedDateItem = R.string.errorcompleteddate.toString()
        checkValidate = false
    } else {
        errorcompletedDateItem = ""
    }
    if (statusItem.isEmpty()) {
        errorstatusItem = R.string.errorstatus.toString()
        checkValidate = false
    } else {
        errorstatusItem = ""
    }
}


@Composable
fun UpdateButton(viewModel: TodoItemViewModel, backHome: () -> Unit) {
    Button(
        onClick = {
            checkValidate()
            if (checkValidate) {
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                //update database
                todoItem.title = titleItem
                todoItem.description = descriptionItem
                todoItem.createdDate = formatter.parse(createdDateItem)
                todoItem.completedDate = formatter.parse(completedDateItem)
                todoItem.status = statusItem

                viewModel.updateItem(todoItem)
                backHome()
            }
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(bottom = 100.dp)
    ) {
        Text("Update")
    }
}


@Composable
fun ClearButton() {

    Button(
        onClick = {
            titleItem = ""
            descriptionItem =""
            statusItem =""
            completedDateItem =""
            createdDateItem =""
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
    var error by remember {
        mutableStateOf(errorstatusItem)
    }
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
            }, readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { Text("Status") },
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                expanded = !expanded
                            }
                        }
                    }
                },
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
    Text(text = error, color = Color.Red)
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