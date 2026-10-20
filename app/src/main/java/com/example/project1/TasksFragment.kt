package com.example.project1

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class TasksFragment : Fragment(R.layout.fragment_tasks) {

    private var filter = "Semua"
    private lateinit var list: LinearLayout
    private lateinit var progressText: TextView
    private lateinit var progressBar: ProgressBar

    private val dataListener = {
        renderTasks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = view.findViewById(R.id.task_list)
        progressText = view.findViewById(R.id.txt_task_progress)
        progressBar = view.findViewById(R.id.task_progress)

        view.findViewById<Button>(R.id.btn_add_task)
            .setOnClickListener { addTask() }

        view.findViewById<Button>(R.id.filter_all)
            .setOnClickListener { applyFilter("Semua") }

        view.findViewById<Button>(R.id.filter_today)
            .setOnClickListener { applyFilter("Hari Ini") }

        view.findViewById<Button>(R.id.filter_priority)
            .setOnClickListener { applyFilter("Prioritas") }

        view.findViewById<Button>(R.id.filter_done)
            .setOnClickListener { applyFilter("Selesai") }

        FocusRepository.addListener(dataListener)
        renderTasks()
    }

    override fun onDestroyView() {
        FocusRepository.removeListener(dataListener)
        super.onDestroyView()
    }

    private fun applyFilter(value: String) {
        filter = value
        renderTasks()
    }

    private fun addTask() {
        val input = EditText(requireContext()).apply {
            hint = "Masukkan nama tugas"
            setPadding(40, 24, 40, 24)
            setTextColor(resources.getColor(R.color.fz_text, null))
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Fokus")
            .setView(input)
            .setNegativeButton("Batal", null)
            .setPositiveButton("Tambah") { _, _ ->
                val title = input.text.toString().trim()
                if (title.isNotEmpty()) {
                    FocusRepository.addTask(title)
                    filter = "Semua"
                } else {
                    Toast.makeText(
                        requireContext(), "Nama tugas wajib diisi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .show()
    }

    private fun renderTasks() {
        if (!::list.isInitialized) return
        list.removeAllViews()

        val tasks = FocusRepository.tasks
        val completed = tasks.count { it.done }
        progressText.text = "$completed/${tasks.size} Selesai"
        progressBar.max = tasks.size.coerceAtLeast(1)
        progressBar.progress = completed

        val filtered = tasks.filter {
            when (filter) {
                "Hari Ini" -> !it.done
                "Prioritas" -> it.priority && !it.done
                "Selesai" -> it.done
                else -> true
            }
        }

        filtered.forEach { task ->
            val card = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 20, 24, 20)
                background = resources.getDrawable(
                    R.drawable.bg_card, requireContext().theme
                )
            }

            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            val check = CheckBox(requireContext()).apply {
                isChecked = task.done
                buttonTintList = android.content.res.ColorStateList.valueOf(
                    resources.getColor(R.color.fz_mint, null)
                )
                setOnCheckedChangeListener { _, _ ->
                    FocusRepository.toggleTask(task.id)
                }
            }

            val title = TextView(requireContext()).apply {
                text = task.title
                textSize = 17f
                setTextColor(resources.getColor(R.color.fz_text, null))
                if (task.done) paintFlags =
                    paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            }

            row.addView(check)
            row.addView(title, LinearLayout.LayoutParams(0, -2, 1f))
            if (task.priority) {
                row.addView(TextView(requireContext()).apply {
                    text = "☆"
                    textSize = 24f
                    setTextColor(resources.getColor(R.color.fz_orange, null))
                })
            }

            card.addView(row)
            card.addView(TextView(requireContext()).apply {
                text = if (task.done) "✓ Selesai" else "⌛ 4 Sesi Pomodoro"
                textSize = 13f
                setTextColor(resources.getColor(R.color.fz_mint, null))
                setPadding(48, 4, 0, 0)
            })

            val params = LinearLayout.LayoutParams(-1, -2)
            params.bottomMargin = 16
            list.addView(card, params)
        }
    }
}
