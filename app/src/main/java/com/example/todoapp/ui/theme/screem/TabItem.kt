package com.example.todoapp.ui.theme.screem

import androidx.compose.runtime.Composable

typealias ComposableFun = @Composable () -> Unit
class TabItem(var title: String, var screen: ComposableFun)