package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                ToDoListPage(todoViewModel)
            }
        }
    }
}
