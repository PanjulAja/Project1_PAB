package com.example.project1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class AmbienceFragment : Fragment(R.layout.fragment_ambience) {

    private var playing = false
    private var selectedSound = "Hujan Deras"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playingText = view.findViewById<TextView>(R.id.txt_sound_playing)
        val playButton = view.findViewById<Button>(R.id.btn_sound_play)
        val volume = view.findViewById<SeekBar>(R.id.seek_volume)

        fun updateSound(name: String) {
            selectedSound = name
            playingText.text = name
            playing = false
            playButton.text = "▶  Putar"
        }

        view.findViewById<TextView>(R.id.sound_rain)
            .setOnClickListener { updateSound("Hujan Deras") }

        view.findViewById<TextView>(R.id.sound_cafe)
            .setOnClickListener { updateSound("Kafe Tenang") }

        view.findViewById<TextView>(R.id.sound_fire)
            .setOnClickListener { updateSound("Api Unggun") }

        view.findViewById<TextView>(R.id.sound_forest)
            .setOnClickListener { updateSound("Hutan Pinus") }

        view.findViewById<TextView>(R.id.sound_white_noise)
            .setOnClickListener { updateSound("White Noise") }

        view.findViewById<TextView>(R.id.sound_lofi)
            .setOnClickListener { updateSound("Lo-Fi Chill Beats") }

        playButton.setOnClickListener {
            playing = !playing
            playButton.text = if (playing) "Ⅱ  Jeda" else "▶  Putar"
            // Ini baru status UI, belum memutar file audio.
        }

        volume.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?, progress: Int, fromUser: Boolean
            ) {
                // Nilai volume tersedia pada progress.
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        view.findViewById<CheckBox>(R.id.check_sync_timer)
        view.findViewById<CheckBox>(R.id.check_binaural)
    }
}
