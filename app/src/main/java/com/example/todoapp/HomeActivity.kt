package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.theme.ToDoAppTheme
import com.google.accompanist.pager.ExperimentalPagerApi

class HomeActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.material.Text(text = "Home Screen")
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    val intent = Intent(this@HomeActivity, MainActivity::class.java)
                    startActivity(intent)
                }) {
                    androidx.compose.material.Text("Material")
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    val intent = Intent(this@HomeActivity, MainJetpackActivity::class.java)
                    startActivity(intent)
                }) {
                    androidx.compose.material.Text(text = "Jetpack")
                }

            }
        }
    }
}