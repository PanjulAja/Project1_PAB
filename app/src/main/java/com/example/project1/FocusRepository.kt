package com.example.project1

object FocusRepository {
    data class TaskItem(
        val id: Int,
        var title: String,
        var done: Boolean = false,
        var priority: Boolean = false
    )

    val tasks = mutableListOf(
        TaskItem(1, "Tugas Besar Pemrograman", priority = true),
        TaskItem(2, "Resume Bab 4 Jaringan Komputer"),
        TaskItem(3, "Latihan Soal Matematika Diskrit", priority = true),
        TaskItem(4, "Submit Proposal Skripsi", done = true)
    )

    var completedSessions = 9
    var totalFocusHours = 4.5f
    var streakDays = 5

    private val listeners = mutableListOf<() -> Unit>()

    fun addListener(listener: () -> Unit) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it() }
    }

    fun addTask(title: String, priority: Boolean = false) {
        tasks.add(0, TaskItem(tasks.size + 1, title, priority = priority))
        notifyListeners()
    }

    fun toggleTask(id: Int) {
        tasks.find { it.id == id }?.let {
            it.done = !it.done
            if (it.done) {
                completedSessions++
                totalFocusHours += 0.5f
            } else {
                completedSessions = (completedSessions - 1).coerceAtLeast(0)
            }
        }
        notifyListeners()
    }

    fun getActiveTask(): String {
        return tasks.firstOrNull { !it.done }?.title ?: "Semua tugas selesai! 🎉"
    }
}
