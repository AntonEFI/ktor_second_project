package com.example.model

object TaskRepository {

    val tasks = mutableListOf<Task>(
        Task("cleaning", "Clean the house", Priority.Low),
        Task("gardening", "Mow the lawn", Priority.Medium),
        Task("shopping", "Buy the groceries", Priority.High),
        Task("painting", "Paint the fence", Priority.Medium)
    )

    fun allTasks(): List<Task> = tasks.toList()

    fun tasksByPriority(priority: Priority) = tasks.filter { task ->
        task.priority == priority
    }

    fun taskByName(name: String) = tasks.find { it ->
        it.name.equals(name, ignoreCase = true)
    }

    fun addTask(task: Task){
        if (taskByName(name = task.name) != null){
            throw IllegalStateException("Cannot duplicate task names!")
        }
        tasks.add(task)
    }

}