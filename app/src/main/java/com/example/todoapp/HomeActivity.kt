package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                androidx.compose.material.Text(
                    text = "Home Screen", style = MaterialTheme.typography.h4
                )
                Spacer(modifier = Modifier.height(40.dp))
                Button(
                    modifier = Modifier.width(150.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        val intent = Intent(this@HomeActivity, MainActivity::class.java)
                        startActivity(intent)
                    }) {
                    androidx.compose.material.Text("Material")
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    modifier = Modifier.width(200.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        val intent = Intent(this@HomeActivity, MainJetpackActivity::class.java)
                        startActivity(intent)
                    }) {
                    androidx.compose.material.Text(text = "Jetpack")
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    modifier = Modifier.width(250.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        val intent = Intent(this@HomeActivity, MainBottomSheetActivity::class.java)
                        startActivity(intent)
                    }) {
                    androidx.compose.material.Text(text = "Bottom Sheet")
                }
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier.width(300.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        val intent = Intent(this@HomeActivity, MainKotlinActivity::class.java)
                        startActivity(intent)
                    }) {
                    androidx.compose.material.Text(text = "Kotlin With Jetpack Item")
                }

            }
        }
    }
}