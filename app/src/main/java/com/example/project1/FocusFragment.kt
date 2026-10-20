package com.example.project1

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.Locale

class FocusFragment : Fragment(R.layout.fragment_focus) {

    private var duration = 25 * 60 * 1000L
    private var remaining = duration
    private var timer: CountDownTimer? = null
    private var running = false
    private var session = 1

    private lateinit var timeText: TextView
    private lateinit var sessionText: TextView
    private lateinit var subtitle: TextView
    private lateinit var startButton: Button
    private lateinit var currentTaskText: TextView

    private val dataListener = {
        updateCurrentTask()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeText = view.findViewById(R.id.txt_timer)
        sessionText = view.findViewById(R.id.txt_session)
        subtitle = view.findViewById(R.id.txt_timer_subtitle)
        startButton = view.findViewById(R.id.btn_start)
        currentTaskText = view.findViewById(R.id.txt_current_task)

        view.findViewById<Button>(R.id.btn_focus_mode).setOnClickListener {
            selectMode(25, "Ruang Fokus Penuh")
        }
        view.findViewById<Button>(R.id.btn_break_mode).setOnClickListener {
            selectMode(5, "Waktunya beristirahat")
        }
        view.findViewById<Button>(R.id.btn_long_break_mode).setOnClickListener {
            selectMode(15, "Istirahat panjang")
        }

        startButton.setOnClickListener {
            if (running) pauseTimer() else startTimer()
        }

        view.findViewById<Button>(R.id.btn_reset).setOnClickListener {
            pauseTimer()
            remaining = duration
            updateDisplay()
        }

        view.findViewById<Button>(R.id.btn_next).setOnClickListener {
            pauseTimer()
            session = if (session >= 4) 1 else session + 1
            remaining = duration
            updateDisplay()
        }

        FocusRepository.addListener(dataListener)
        updateDisplay()
        updateCurrentTask()
    }

    override fun onDestroyView() {
        FocusRepository.removeListener(dataListener)
        pauseTimer()
        super.onDestroyView()
    }

    private fun updateCurrentTask() {
        if (::currentTaskText.isInitialized) {
            currentTaskText.text = FocusRepository.getActiveTask()
        }
    }

    private fun selectMode(minutes: Int, label: String) {
        pauseTimer()
        duration = minutes * 60 * 1000L
        remaining = duration
        subtitle.text = label
        updateDisplay()
    }

    private fun startTimer() {
        timer?.cancel()
        running = true
        startButton.text = "Ⅱ  Jeda"

        timer = object : CountDownTimer(remaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remaining = millisUntilFinished
                updateDisplay()
            }

            override fun onFinish() {
                remaining = 0
                running = false
                startButton.text = "▶  Mulai"
                FocusRepository.completedSessions++
                FocusRepository.totalFocusHours += duration / (1000f * 60f * 60f)
                updateDisplay()
            }
        }.start()
    }

    private fun pauseTimer() {
        timer?.cancel()
        timer = null
        running = false
        if (::startButton.isInitialized) startButton.text = "▶  Mulai"
    }

    private fun updateDisplay() {
        if (!::timeText.isInitialized) return

        val totalSeconds = remaining / 1000
        timeText.text = String.format(
            Locale.getDefault(), "%02d:%02d",
            totalSeconds / 60, totalSeconds % 60
        )
        sessionText.text = "● SESI $session DARI 4"
    }
}
