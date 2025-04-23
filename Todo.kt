package com.example.todoapp

import java.util.Date

data class Todo(
    var id: Int,
    var title: String,
    var deadline: Date,
    var isDone: Boolean = false
)

