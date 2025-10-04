package com.example.mini_project_1_todo_app

import java.util.Date
import java.util.UUID

data class TodoItem(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val createdAt: Date,
    val isCompleted: Boolean = false,
)