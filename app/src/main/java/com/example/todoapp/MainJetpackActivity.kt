package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.todoapp.ui.theme.screem.AddItemScreen
import com.example.todoapp.ui.theme.screem.HomeScreen
import com.example.todoapp.ui.theme.screem.UpdateItemScreen
import com.example.todoapp.viewmodel.TodoItemViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainJetpackActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: TodoItemViewModel = ViewModelProvider(this)[TodoItemViewModel::class.java]
        setContent {
            val navController = rememberAnimatedNavController()
            MainApp(navController, this, viewModel)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainApp(
    navController: NavHostController,
    owner: LifecycleOwner,
    viewModel: TodoItemViewModel
) {
    MaterialTheme {
        AnimatedNavHost(navController = navController, startDestination = "home") {
            composable("home",
                enterTransition = {
                when (initialState.destination.route) {
                    "addItem", "updateItem" -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)
                    )
                    else -> null
                }
            }, exitTransition = {
                when (targetState.destination.route) {
                    "addItem", "updateItem" -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)
                    )
                    else -> null
                }
            }, popEnterTransition = {
                when (initialState.destination.route) {
                    "addItem", "updateItem" -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)
                    )
                    else -> null
                }
            }, popExitTransition = {
                when (targetState.destination.route) {
                    "addItem", "updateItem" -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)
                    )
                    else -> null
                }
            }) {
                HomeScreen(openAddItemScreen = {
                    navController.navigate("addItem")
                }, openUpdateItemScreen = {
                    navController.navigate("updateItem")
                }, viewModel = viewModel, owner = owner
                )
            }
            composable("addItem", enterTransition = {
                when (initialState.destination.route) {
                    "home" -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)
                    )
                    else -> null
                }
            }, exitTransition = {
                when (targetState.destination.route) {
                    "home" -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)
                    )
                    else -> null
                }
            }, popEnterTransition = {
                when (initialState.destination.route) {
                    "home" -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)
                    )
                    else -> null
                }
            }, popExitTransition = {
                when (targetState.destination.route) {
                    "home" -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)
                    )
                    else -> null
                }
            }) {
                AddItemScreen(viewModel = viewModel, backHome = {
                    navController.popBackStack(
                        route = "home", inclusive = false, saveState = true
                    )
                })
            }
            composable("updateItem", enterTransition = {
                when (initialState.destination.route) {
                    "home" -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)
                    )
                    else -> null
                }
            }, exitTransition = {
                when (targetState.destination.route) {
                    "home" -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)
                    )
                    else -> null
                }
            }, popEnterTransition = {
                when (initialState.destination.route) {
                    "home" -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)
                    )
                    else -> null
                }
            }, popExitTransition = {
                when (targetState.destination.route) {
                    "home" -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)
                    )
                    else -> null
                }
            }) {
                UpdateItemScreen(viewModel = viewModel, backHome = {
                    navController.popBackStack(
                        route = "home", inclusive = false, saveState = true
                    )
                })
            }
        }
    }
}

