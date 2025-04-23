package com.example.todoapp

import java.util.Date

object TodoManager {
    private val todoList = mutableListOf<Todo>()

    fun getAllTodo(): List<Todo>{
        return todoList
    }

    fun addTodo(title: String, deadline: Date){
        todoList.add(Todo(System.currentTimeMillis().toInt(), title, deadline))
    }

    fun deleteTodo(id: Int) {
        todoList.removeAll { it.id == id }
    }

    fun toggleTodo(id: Int) {
        val index = todoList.indexOfFirst { it.id == id }
        if (index != -1) {
            val todo = todoList[index]
            todoList[index] = todo.copy(isDone = !todo.isDone)
        }
    }
}