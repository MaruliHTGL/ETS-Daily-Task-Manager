package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date

class TodoViewModel: ViewModel() {
    private var _todolist = MutableLiveData<List<Todo>>()
    val todoList: LiveData<List<Todo>> = _todolist

    fun getAllTodo(){
        _todolist.value = TodoManager.getAllTodo().reversed()
    }

    fun addTodo(title: String, deadline: Date){
        TodoManager.addTodo(title, deadline)
        getAllTodo()
    }

    fun deleteTodo(id: Int) {
        TodoManager.deleteTodo(id)
        getAllTodo()
    }

    fun toggleTodo(id: Int) {
        TodoManager.toggleTodo(id)
        _todolist.value = _todolist.value?.map {
            if (it.id == id) it.copy(isDone = !it.isDone) else it
        }
    }
}