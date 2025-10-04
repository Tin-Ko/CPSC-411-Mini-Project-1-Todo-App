package com.example.mini_project_1_todo_app

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.util.Date
import java.util.UUID

val TodoItemSaver = Saver<TodoItem, List<Any>>(
    save = { todoItem ->
        listOf(
            todoItem.title,
            todoItem.createdAt.time,
            todoItem.isCompleted,
            todoItem.id.toString()
        )
    },
    restore = { list ->
        TodoItem(
            title = list[0] as String,
            createdAt = Date(list[1] as Long),
            isCompleted = list[2] as Boolean,
            id = UUID.fromString(list[3] as String)
        )
    }
)
val TodoListSaver = listSaver<SnapshotStateList<TodoItem>, Any>(
    save = { todoList ->
        todoList.map { todoItem ->
            with(TodoItemSaver) { save(todoItem)!! }
        }
    },
    restore = { savedList ->
        mutableStateListOf<TodoItem>().apply {
            savedList.forEach { saved ->
                @Suppress("UNCHECKED_CAST")
                val item = with(TodoItemSaver) { restore(saved as List<Any>)!! }
                add(item)
            }
        }
    }
)

//@Parcelize
//data class TodoItem(
//    var title: String,
//    var createdAt: Date,
//    val isCompleted: Boolean = false,
//    var id: UUID = UUID.randomUUID(),
//) : Parcelable


@Composable
fun TodoListPage() {
    val activeItems = rememberSaveable(saver = TodoListSaver) { mutableStateListOf<TodoItem>() }
    val completedItems = rememberSaveable(saver = TodoListSaver) { mutableStateListOf<TodoItem>() }
    var inputText by rememberSaveable { mutableStateOf("") }
    var showError by rememberSaveable { mutableStateOf(false) }

    val handleAddItem = {
        val trimmedInputText = inputText.trim()
        if (trimmedInputText.isNotBlank()) {
            activeItems.add(TodoItem(
                title = trimmedInputText,
                createdAt = Date.from(Instant.now())
            ))
            inputText = ""
            showError = false

        } else {
            showError = true
        }
    }

    fun handleCheckedChange(item: TodoItem, isChecked: Boolean) {
        if (isChecked) {
            activeItems.remove(item)
            completedItems.add(item.copy(isCompleted = true))
        } else {
            completedItems.remove(item)
            activeItems.add(item.copy(isCompleted = false))
        }
    }

    fun handleDelete(item: TodoItem) {
        activeItems.remove(item)
        completedItems.remove(item)
    }

    Column(
        modifier = Modifier.fillMaxHeight()
            .padding(8.dp)
    ) {
        TodoInputBar(
            text = inputText,
            showError = showError,
            onValueChange = {
                inputText = it
                if (it.isNotBlank()) showError = false
            },
            onAddItem = handleAddItem
        )
        Spacer(modifier = Modifier.height(24.dp))

        TodoListSection(
            title = "Todo Items",
            todoItems = activeItems,
            emptyMessage = "You have no current todo items.",
            onItemCheckedChange = ::handleCheckedChange,
            onItemDelete = ::handleDelete,
        )

        Spacer(modifier = Modifier.height(24.dp))

        TodoListSection(
            title = "Completed Items",
            todoItems = completedItems,
            emptyMessage = "No items completed yet.",
            onItemCheckedChange = ::handleCheckedChange,
            onItemDelete = ::handleDelete,
        )
    }
}

