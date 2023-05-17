package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.todoapp.bottomsheet.MainBottomSheetFragment;

public class MainBottomSheetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom_sheet);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, new MainBottomSheetFragment())
                .commit();
    }
}