package com.example.todoapp

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}