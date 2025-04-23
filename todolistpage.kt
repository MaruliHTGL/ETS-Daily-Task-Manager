package com.example.todoapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import java.util.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import java.util.Calendar

@Composable
fun ToDoListPage(viewModel: TodoViewModel) {
    val todoList by viewModel.todoList.observeAsState()
    var inputText by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf<Date?>(null) }
    var selectedFilter by remember { mutableStateOf("All") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Column(modifier = Modifier.fillMaxHeight().padding(36.dp)) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = {Text("Tugas") }
                )

                Button(
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                TimePickerDialog(
                                    context,
                                    { _, hourOfDay, minute ->
                                        calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                                        deadline = calendar.time
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    true
                                ).show()
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                ) {
                    Text(
                        text = if (deadline == null)
                            "Deadline"
                        else
                            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(deadline!!)
                    )
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (inputText.isNotBlank() && deadline != null) {
                        viewModel.addTodo(inputText, deadline!!)
                        inputText = ""
                        deadline = null
                    } else {
                        Toast.makeText(context, "Isi to-do dan pilih deadline", Toast.LENGTH_SHORT).show()
                    }
                }) {
                Text("Add")
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)) {

            Text("Filter:", fontSize = 14.sp, color = Color.Gray)

            Row(verticalAlignment = Alignment.CenterVertically) {
                listOf("All", "Complete", "Uncomplete").forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedFilter == option,
                            onClick = { selectedFilter = option }
                        )
                        Text(option)
                    }
                }
            }
        }

        val filteredList = todoList?.filter {
            when (selectedFilter) {
                "Complete" -> it.isDone
                "Uncomplete" -> !it.isDone
                else -> true
            }
        }?.sortedBy { it.deadline }

        filteredList?.let { sortedList ->
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(sortedList) { _: Int, item: Todo ->
                    TodoItem(item = item, onToggleDone = { viewModel.toggleTodo(item.id) }, onDelete = { viewModel.deleteTodo(item.id) })
                }
            }
        } ?: Text(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            textAlign = TextAlign.Center,
            text = "No To Do List",
            fontSize = 16.sp
        )
    }
}

@Composable
fun TodoItem(item: Todo, onToggleDone: () -> Unit, onDelete: () -> Unit) {
    val isOverdue = item.deadline.before(Date())
    val backgroundColor = when {
        item.isDone -> Color.Gray
        isOverdue -> Color.Red
        else -> MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Deadline: " + SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.ENGLISH).format(item.deadline),
                fontSize = 12.sp,
                color = Color.LightGray
            )
            Text(
                text = item.title,
                fontSize = 20.sp,
                color = Color.White
            )
        }
        Checkbox(
            checked = item.isDone,
            onCheckedChange = { onToggleDone() },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                checkmarkColor = Color.Black,
                uncheckedColor = Color.White
            )
        )
        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}