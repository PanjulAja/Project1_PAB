package com.example.project1

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.Locale

class StatsFragment : Fragment(R.layout.fragment_stats) {

    private lateinit var totalFocusText: TextView
    private lateinit var totalSessionsText: TextView
    private lateinit var streakText: TextView

    private val dataListener = {
        updateStats()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalFocusText = view.findViewById(R.id.txt_total_focus)
        totalSessionsText = view.findViewById(R.id.txt_total_sessions)
        streakText = view.findViewById(R.id.txt_streak)

        val chart = view.findViewById<LinearLayout>(R.id.chart_bars)
        val values = listOf(90, 125, 60, 100, 165, 75, 35)
        val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")

        values.forEachIndexed { index, height ->
            val column = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            }

            val bar = View(requireContext()).apply {
                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 24f
                    setColor(
                        if (index == 4) Color.rgb(65, 220, 169)
                        else Color.rgb(34, 45, 68)
                    )
                }
            }

            column.addView(
                bar,
                LinearLayout.LayoutParams(dp(22), dp(height))
            )

            val label = TextView(requireContext()).apply {
                text = days[index]
                textSize = 11f
                gravity = Gravity.CENTER
                setTextColor(
                    if (index == 4) Color.rgb(65, 220, 169)
                    else Color.rgb(177, 191, 200)
                )
            }

            val labelParams = LinearLayout.LayoutParams(-1, dp(28))
            column.addView(label, labelParams)

            chart.addView(
                column,
                LinearLayout.LayoutParams(0, -1, 1f)
            )
        }

        FocusRepository.addListener(dataListener)
        updateStats()
    }

    override fun onDestroyView() {
        FocusRepository.removeListener(dataListener)
        super.onDestroyView()
    }

    private fun updateStats() {
        if (::totalFocusText.isInitialized) {
            totalFocusText.text = String.format(Locale.getDefault(), "%.1fj", FocusRepository.totalFocusHours)
        }
        if (::totalSessionsText.isInitialized) {
            totalSessionsText.text = "${FocusRepository.completedSessions} sesi"
        }
        if (::streakText.isInitialized) {
            streakText.text = "${FocusRepository.streakDays} hari"
        }
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()
}
