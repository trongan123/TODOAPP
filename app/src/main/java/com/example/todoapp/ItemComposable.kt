package com.example.todoapp

import android.util.Log
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.AddItemFragmentViewModal
import com.example.todoapp.viewmodel.UpdateItemFragmentViewModel
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
@Composable
fun Texttitle() {
    var title by remember {
        mutableStateOf(titleItem)
    }

    OutlinedTextField(
        value = title,
        onValueChange = {
            title = it
            titleItem = title.toString()
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { androidx.compose.material.Text("Title") },
        label = { androidx.compose.material.Text("Title") },


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
            descriptionItem = description.toString()
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { androidx.compose.material.Text("Description") },
        label = { androidx.compose.material.Text("Description") },


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
            createdDateItem = createdDate.toString()
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
        placeholder = { androidx.compose.material.Text("CreatedDate") },
        label = { androidx.compose.material.Text("CreatedDate") },
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

        placeholder = { androidx.compose.material.Text("CompletedDate") },
        label = { androidx.compose.material.Text("CompletedDate") },
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium)
        )

    )
}

@Composable
fun TextFieldWithDropdown(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    setValue: (TextFieldValue) -> Unit,
    onDismissRequest: () -> Unit,
    dropDownExpanded: Boolean,
    list: List<String>,
    label: String = ""
) {
    Box(modifier) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused)
                        onDismissRequest()
                },
            value = value,
            onValueChange = setValue,
            label = { Text(label) },
            colors = TextFieldDefaults.outlinedTextFieldColors()
        )
        DropdownMenu(
            expanded = dropDownExpanded,
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            onDismissRequest = onDismissRequest
        ) {
            list.forEach { text ->
                DropdownMenuItem(onClick = {
                    setValue(
                        TextFieldValue(
                            text,
                            TextRange(text.length)
                        )
                    )
                }) {
                    Text(text = text)
                }
            }
        }
    }
}

val all = listOf("pending", "completed")

val dropDownOptions = mutableStateOf(listOf<String>())
val textFieldValue = mutableStateOf(TextFieldValue())
val dropDownExpanded = mutableStateOf(false)
fun onDropdownDismissRequest() {
    dropDownExpanded.value = false
}

fun onValueChanged(value: TextFieldValue) {
    dropDownExpanded.value = true
    textFieldValue.value = value
    dropDownOptions.value = all.filter { it.startsWith(value.text) && it != value.text }.take(3)
}

@Composable
fun TextFieldWithDropdownUsage() {
    TextFieldWithDropdown(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue.value,
        setValue = ::onValueChanged,
        onDismissRequest = ::onDropdownDismissRequest,
        dropDownExpanded = dropDownExpanded.value,
        list = dropDownOptions.value,
        label = "Status"
    )
}

@Composable
fun CommonSpace() {
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun AddButton(viewModel : AddItemFragmentViewModal) {
    var navController : NavController
    Button(
        onClick = {
            var todoItem: TodoItem = TodoItem()
            var formatter = SimpleDateFormat("yyyy-MM-dd")
            //update database
            todoItem.title = titleItem
            todoItem.description = descriptionItem
            todoItem.createdDate = formatter.parse(createdDateItem)
            todoItem.completedDate = formatter.parse(completedDateItem)
            todoItem.status = statusItem

            viewModel.addItem(todoItem)


        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(bottom = 100.dp)
    ) {
        androidx.compose.material.Text("ADD")
    }
}
@Composable
fun UpdateButton(viewModel : UpdateItemFragmentViewModel, view : View) {
    Button(
        onClick = {

            var formatter = SimpleDateFormat("yyyy-MM-dd")
            //update database
            todoItem.title = titleItem
            todoItem.description = descriptionItem
            todoItem.createdDate = formatter.parse(createdDateItem)
            todoItem.completedDate = formatter.parse(completedDateItem)
            todoItem.status = statusItem

            viewModel.updateItem(todoItem)
            Navigation.findNavController(view).navigate(R.id.mainFragment2)
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(bottom = 100.dp)
    ) {
        androidx.compose.material.Text("Update")
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
        androidx.compose.material.Text("CLEAR")
    }
}

@Composable
fun LayoutUpdateButton(viewModel : UpdateItemFragmentViewModel, view: View) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        ClearButton()
        UpdateButton(viewModel,view)
    }


}

@Composable
fun LayoutAddButton(viewModel : AddItemFragmentViewModal) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {

        ClearButton()
        AddButton(viewModel)

    }


}

@Composable
fun dropDownMenuStatus() {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("pending","completed")
    var selectedText by remember { mutableStateOf(statusItem) }

    var textfieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column() {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { androidx.compose.material.Text("Status") },
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textfieldSize.width.toDp()})
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    statusItem = label
                    expanded = false
                }) {
                    androidx.compose.material.Text(text = label)
                }
            }
        }
    }

}
