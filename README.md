# CPSC-411-Mini-Project-1-Todo-App
## App Overview
The Application provides a straightforward interface for managing a list of tasks. Users can:
- **Add new todo items**
  - An input field at the top allows users to type in a new task and add it to their list.
- **View active tasks**
  - All uncompleted tasks are displayed in the "Todo Items" section.
- **Mark tasks as complete**
  - Users can check off tasks, which then move them to the "Completed Items" section.
- **Delete tasks**
  - Any task can be removed from the list
- **Data persistence**
  - The list of todo items is saved and will be restored even when the application is closed or if the screen is rotated.

## Concepts Used
- **Data Class**
  - The `TodoItem` data class
- **State and Remember**
  - `rememberSaveable` is used for keeping the state in memory across recompositions and configuration changes.
  - `mutableStateListOf` is used for storing the active and completed items.
  - `mutableStateOf` is used for storing the input text and `showError` variable.
- **State Hoisting**
  - The todo items are passed down to the stateless composables like `TodoInputBar` and `TodoListSection`, etc.
- **Composable Functions**
  - LazyColumn
  - Scaffold
