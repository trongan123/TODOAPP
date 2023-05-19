package com.example.todoapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.bottomsheet.MainBottomSheetFragment;

public class MainBottomSheetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom_sheet);
        setTitle("List To Do Item");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, new MainBottomSheetFragment())
                .commit();
    }
}