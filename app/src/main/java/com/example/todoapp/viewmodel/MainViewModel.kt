package com.example.todoapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.example.todoapp.DefaultPaginator
import com.example.todoapp.model.TodoItem

import com.example.todoapp.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository(application)

    private var state by mutableStateOf(ScreenState())

    private val paginator = DefaultPaginator(initialKey = state.page, onLoadUpdated = {
        state = state.copy(isLoading = it)
    }, onRequest = { nextPage ->
        repository.getItems(nextPage, 10)
    }, getNextKey = {
        state.page + 1
    }, onError = {
        state = state.copy(error = it?.localizedMessage)
    }, onSuccess = { items, newKey ->
        state = state.copy(
            items = state.items + items, page = newKey, endReached = items.isEmpty()
        )
    })

    init {
        loadNextItems()
    }

    private fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }
}

data class ScreenState(
    val isLoading: Boolean = false,
    val items: List<TodoItem> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0
)