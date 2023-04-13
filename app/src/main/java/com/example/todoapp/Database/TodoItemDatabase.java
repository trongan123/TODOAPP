package com.example.todoapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todoapp.model.TodoItem;

@Database(entities = {TodoItem.class}, version = 1)
public abstract class TodoItemDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "todoItem.db";
    private static TodoItemDatabase instance;

    public static synchronized TodoItemDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TodoItemDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract TodoItemDAO todoItemDAO();


}