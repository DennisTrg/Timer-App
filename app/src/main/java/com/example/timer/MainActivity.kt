package com.example.timer

import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var textView1: TextView
    lateinit var chronometer: Chronometer
    lateinit var startButton: ImageButton
    lateinit var pauseButton: ImageButton
    lateinit var stopButton: ImageButton
    lateinit var textView2: TextView
    lateinit var editText: EditText
    private var running: Boolean = false
    private var pause: Long = 0
    private var resultSecond: Long = 0
    private var resultMinute: Long = 0
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView1 = findViewById<TextView>(R.id.textView1)
        chronometer = findViewById<Chronometer>(R.id.chronometer)
        startButton = findViewById<ImageButton>(R.id.startButton)
        pauseButton = findViewById<ImageButton>(R.id.pauseButton)
        stopButton = findViewById<ImageButton>(R.id.stopButton)
        textView2 = findViewById<TextView>(R.id.textView2)
        editText = findViewById(R.id.editText)
        sharedPreferences =  getSharedPreferences("com.example.timer",MODE_PRIVATE)
        startButton.setOnClickListener(this)
        pauseButton.setOnClickListener(this)
        stopButton.setOnClickListener(this)
        checkSharedPreferences()

        if (savedInstanceState != null) {
            chronometer.base = savedInstanceState.getLong("key")
            chronometer.start()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("key", chronometer.base)
    }

//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        chronometer.base = savedInstanceState.getLong("key", 0)
//
//    }

    override fun onClick(p0: View?) {
        when( p0?.id) {
            R.id.startButton -> {
                if (!running) {
                    chronometer.base = SystemClock.elapsedRealtime() - pause
                    chronometer.start()
                    running = true
                }
            }
            R.id.pauseButton -> {
                if(running){
                    chronometer.stop()
                    pause = SystemClock.elapsedRealtime() -  chronometer.base
                    running = false
                    showElapsedTime()
                }
            }
            R.id.stopButton -> {
                if (!running) {
                    resultSecond = (pause) / 1000
                    resultMinute = ((pause) / 1000) / 30
                    chronometer.stop()
                    chronometer.base = SystemClock.elapsedRealtime()
                    var editor = sharedPreferences.edit()
                    editor.putString("com.example.timer.seconds", resultSecond.toString()).apply()
                    editor.putString("com.example.timer.minutes", resultMinute.toString()).apply()
                    editor.putString("com.example.timer.EditText", editText.text.toString()).apply()
                    pause = 0
                    running = false
                } else {
                    resultSecond = (SystemClock.elapsedRealtime()  - chronometer.base)/1000
                    resultMinute = ((SystemClock.elapsedRealtime()  - chronometer.base)/1000)/30
                    chronometer.stop()
                    chronometer.base = SystemClock.elapsedRealtime()
                    var editor = sharedPreferences.edit()
                    editor.putString("com.example.timer.seconds", resultSecond.toString()).apply()
                    editor.putString("com.example.timer.minutes", resultMinute.toString()).apply()
                    editor.putString("com.example.timer.EditText", editText.text.toString()).apply()
                    pause = 0
                    running = false
                }
            }
        }
    }

    private fun checkSharedPreferences( ) {
        var seconds = sharedPreferences.getString("com.example.timer.seconds", "00")
        var minutes = sharedPreferences.getString("com.example.timer.minutes", "00")
        var task = sharedPreferences.getString("com.example.timer.EditText","")
        textView1.setText("You spend $minutes:$seconds on $task last time")
    }
    private fun showElapsedTime() {
        val elapsedMillis: Long = SystemClock.elapsedRealtime() - chronometer.base
        Toast.makeText(
            this, "Elapsed milliseconds: $elapsedMillis",
            Toast.LENGTH_SHORT
        ).show()
    }
}