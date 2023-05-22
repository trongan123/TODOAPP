package com.example.todoapp.ui.theme.screem

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.example.todoapp.*
import com.example.todoapp.R
import com.example.todoapp.model.TodoItem
import com.example.todoapp.viewmodel.TodoItemViewModel
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabScreen(
    owner: LifecycleOwner,
    openAddItemScreen: () -> Unit,
    openUpdateItemScreen: () -> Unit,
    viewModel: TodoItemViewModel,
) {

    val tabs: MutableList<TabItem> = ArrayList<TabItem>().toMutableList()
    tabs += TabItem("All") {
        AllItemScreen(
            owner, openUpdateItemScreen = {
                openUpdateItemScreen()
            }, viewModel = viewModel
        )
    }
    tabs += TabItem("Pending") {
        PendingItemScreen(owner, openUpdateItemScreen = {
            openUpdateItemScreen()
        }, viewModel = viewModel)
    }
    tabs += TabItem("Completed") {
        CompletedItemScreen(
            owner, openUpdateItemScreen = {
                openUpdateItemScreen()
            }, viewModel = viewModel
        )
    }
    val pagerState = rememberPagerState()
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { openAddItemScreen() }) {
            Icon(Icons.Filled.Add, "")
        }
    }) { padding ->
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        Column(
            modifier = Modifier.padding(padding)
        ) {
            SearchView(state = textState, viewModel)
            Spacer(modifier = Modifier.size(16.dp))
            Tabs(
                tabs = tabs, pagerState = pagerState, viewModel = viewModel
            )
            TabsContent(tabs = tabs, pagerState = pagerState)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(
    tabs: List<TabItem>,
    pagerState: PagerState,
    viewModel: TodoItemViewModel,
    negativeButtonColor: Color = Color(0xFF35898F),
    positiveButtonColor: Color = Color(0xFFFF0000),
    spaceBetweenElements: Dp = 18.dp,
    context: Context = LocalContext.current.applicationContext
) {
    var dialogOpen by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    Row {
        ScrollableTabRow(modifier = Modifier.weight(1f), edgePadding = 0.dp,

            selectedTabIndex = pagerState.currentPage,

            backgroundColor = Color.White, contentColor = Color.Black, indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }) {

            tabs.forEachIndexed { index, tab ->

                LeadingIconTab(
                    icon = { },
                    text = { Text(tab.title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }
        if (dialogOpen) {
            Dialog(onDismissRequest = {
                dialogOpen = false
            }) {
                Surface(
                    modifier = Modifier.fillMaxWidth(0.92f), color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        // text and buttons
                        Column(
                            modifier = Modifier
                                .padding(top = 30.dp)
                                .fillMaxWidth()
                                .background(
                                    color = Color.White, shape = RoundedCornerShape(percent = 10)
                                ), horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(height = 36.dp))

                            Text(
                                text = "Delete?", fontSize = 24.sp
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
                                    buttonColor = positiveButtonColor, buttonText = "Yes"
                                ) {
                                    dialogOpen = false
                                    Toast.makeText(
                                        context, "Clear all successfull", Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.clearAllItem()
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
                                    width = 2.dp, shape = CircleShape, color = positiveButtonColor
                                )
                                .padding(all = 16.dp)
                                .align(alignment = Alignment.TopCenter)
                        )
                    }
                }
            }
        }
        Button(
            onClick = {
                dialogOpen = true
            }, shape = RoundedCornerShape(20.dp)
        ) {
            Text("ClearAll")
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen()
    }
}

@Composable
fun AllItemScreen(
    owner: LifecycleOwner,
    openUpdateItemScreen: () -> Unit,
    viewModel: TodoItemViewModel
) {
    var items by remember { mutableStateOf(ArrayList<TodoItem>()) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            viewModel.stringMutableLiveData.observe(owner) {
                viewModel.allList
                    .observe(owner) { item: List<TodoItem> ->
                        items = item as ArrayList<TodoItem>
                    }
            }
            items(count = items.size, key = {
                items[it].id
            }, itemContent = { index ->
                val cartItemData = items[index]
                ItemList(
                    cartItemData, openUpdateItemScreen = {
                        openUpdateItemScreen()
                    }, viewModel
                )
            })
        }
    }
}

@Composable
fun PendingItemScreen(
    owner: LifecycleOwner, openUpdateItemScreen: () -> Unit, viewModel: TodoItemViewModel
) {
    var items by remember { mutableStateOf(ArrayList<TodoItem>()) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn {
            viewModel.stringMutableLiveData.observe(owner) {
                viewModel.pendingList.observe(owner) { item: List<TodoItem> ->
                    items = item as ArrayList<TodoItem>
                }
            }
            items(count = items.size, key = {
                items[it].id
            }, itemContent = { index ->
                val cartItemData = items[index]
                ItemList(
                    cartItemData, openUpdateItemScreen = {
                        openUpdateItemScreen()
                    }, viewModel
                )
            })
        }
    }
}


@Composable
fun CompletedItemScreen(
    owner: LifecycleOwner, openUpdateItemScreen: () -> Unit, viewModel: TodoItemViewModel
) {
    var items by remember { mutableStateOf(ArrayList<TodoItem>()) }
    //   items = localListTodo.current.todolist as ArrayList<TodoItem>
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn {
            viewModel.stringMutableLiveData.observe(owner) {
                viewModel.completedList.observe(owner) { item: List<TodoItem> ->
                    items = item as ArrayList<TodoItem>
                }
            }
            items(count = items.size, key = {
                items[it].id
            }, itemContent = { index ->
                val cartItemData = items[index]
                ItemList(
                    cartItemData, openUpdateItemScreen = {
                        openUpdateItemScreen()
                    }, viewModel
                )
            })
        }
    }
}

@Composable
fun ItemList(
    i: TodoItem, openUpdateItemScreen: () -> Unit, viewModel: TodoItemViewModel
) {
    val isChecked = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                if (isChecked.value) {
                    isChecked.value = false
                    viewModel.setClearAll(i.id, false)
                    viewModel.setCheckItem(i.id.toLong(), false)
                } else {
                    isChecked.value = true
                    viewModel.setClearAll(i.id, true)
                    viewModel.setCheckItem(i.id.toLong(), true)
                }
            }, elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            Row {
                val check: List<Long> = viewModel.listMutableLiveDataCheck.value as List<Long>
                isChecked.value = check.contains(i.id.toLong())
                androidx.compose.material3.Checkbox(checked = isChecked.value, onCheckedChange = {
                    isChecked.value = it
                    viewModel.setClearAll(i.id, it)
                    viewModel.setCheckItem(i.id.toLong(), it)
                })
                Spacer(modifier = Modifier.size(16.dp))
                if (!isChecked.value) {
                    androidx.compose.material3.Text(
                        i.title, modifier = Modifier.padding(top = 10.dp)

                    )
                } else {
                    androidx.compose.material3.Text(
                        i.title,
                        modifier = Modifier.padding(top = 10.dp),
                        style = TextStyle(textDecoration = TextDecoration.LineThrough)
                    )
                }
                Spacer(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                Image(painter = painterResource(id = R.drawable.ellipsis),
                    contentDescription = null,

                    modifier = Modifier
                        .clickable {
                            todoItem = i
                            openUpdateItemScreen()
                        }
                        .size(50.dp))
                Spacer(modifier = Modifier.size(10.dp))
            }
            androidx.compose.material3.Text(
                i.description, modifier = Modifier.padding(start = 16.dp)
            )
            androidx.compose.material3.Text(
                "Created Date:    " + SimpleDateFormat(
                    stringDateFormat, Locale.getDefault()
                ).format(i.createdDate),
                modifier = Modifier.padding(top = 10.dp, start = 16.dp),
                style = MaterialTheme.typography.overline
            )
            androidx.compose.material3.Text(
                "Completed Date:" + SimpleDateFormat(
                    stringDateFormat, Locale.getDefault()
                ).format(i.completedDate),
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 16.dp),
                style = MaterialTheme.typography.overline
            )
        }
    }
}

@Composable
fun ItemListRecycle(
    i: TodoItem, view: View?, viewModel: TodoItemViewModel, myItem: MyComposeView
) {
    val isChecked = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                todoItem = i
                val bundle = Bundle()
                bundle.putSerializable("objectTodoItem", todoItem)
                bundle.putString("transition", myItem.transitionName)

                val extras: FragmentNavigator.Extras = FragmentNavigator.Extras
                    .Builder()
                    .addSharedElement(myItem, myItem.transitionName)
                    .build()
                findNavController(view!!).navigate(
                    R.id.updateItemKotlinFragment, bundle, null, extras
                )
            }, elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            Row {
                val check: List<Long> = viewModel.listMutableLiveDataCheck.value as List<Long>
                isChecked.value = check.contains(i.id.toLong())
                androidx.compose.material3.Checkbox(checked = isChecked.value, onCheckedChange = {
                    isChecked.value = it
                    viewModel.setClearAll(i.id, it)
                    viewModel.setCheckItem(i.id.toLong(), it)
                })
                Spacer(modifier = Modifier.size(16.dp))
                if (!isChecked.value) {
                    androidx.compose.material3.Text(
                        i.title, modifier = Modifier.padding(top = 10.dp)
                    )
                } else {
                    androidx.compose.material3.Text(
                        i.title,
                        modifier = Modifier.padding(top = 10.dp),
                        style = TextStyle(textDecoration = TextDecoration.LineThrough)
                    )
                }
                Spacer(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            androidx.compose.material3.Text(
                i.description, modifier = Modifier.padding(start = 16.dp)
            )
            androidx.compose.material3.Text(
                "Created Date    :" + SimpleDateFormat(
                    stringDateFormat, Locale.getDefault()
                ).format(i.createdDate),
                modifier = Modifier.padding(top = 10.dp, start = 16.dp),
                style = MaterialTheme.typography.overline
            )

            androidx.compose.material3.Text(
                "Completed Date:" + SimpleDateFormat(
                    stringDateFormat, Locale.getDefault()
                ).format(i.completedDate),
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 16.dp),
                style = MaterialTheme.typography.overline
            )
        }
    }
}