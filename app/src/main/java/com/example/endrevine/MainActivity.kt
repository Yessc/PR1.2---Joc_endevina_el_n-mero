package com.example.endrevine

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.endrevine.R
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var inputNumber: EditText
    private lateinit var btnGuess: Button
    private lateinit var tvAttempts: TextView
    private lateinit var tvHistory: TextView
    private lateinit var scrollView: ScrollView

    private var randomNumber = 0
    private var attempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vinculo con el  alayout
        inputNumber = findViewById(R.id.inputNumber)
        btnGuess = findViewById(R.id.btnGuess)
        tvAttempts = findViewById(R.id.tvAttempts)
        tvHistory = findViewById(R.id.tvHistory)
        scrollView = findViewById(R.id.scrollView)

        startNewGame()

        // Listener del botón
        btnGuess.setOnClickListener {
            checkGuess()
        }

        // Listener para Enter del teclado
        inputNumber.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                checkGuess()
                true
            } else {
                false
            }
        }
    }

    private fun startNewGame() {
        randomNumber = Random.nextInt(1, 101) // número de 1 a 100
        attempts = 0
        tvAttempts.text = "Intents: 0"
        tvHistory.text = ""
    }

    private fun checkGuess() {
        val input = inputNumber.text.toString().trim()
        if (input.isEmpty()) {
            Toast.makeText(this, "Escriu un número!", Toast.LENGTH_SHORT).show()
            return
        }

        val guess = input.toInt()
        attempts++
        tvAttempts.text = "Intentos: $attempts"

        when {
            guess == randomNumber -> {
                tvHistory.append("🎉 Has encertat amb $guess en $attempts intents!\n")
                scrollToBottom()
                showWinDialog()
            }
            guess < randomNumber -> {
                tvHistory.append("Has dit $guess → El número és MÉS GRAN\n")
                scrollToBottom()
            }
            else -> {
                tvHistory.append("Has dit $guess → El número és MÉS PETIT\n")
                scrollToBottom()
            }
        }

        inputNumber.setText("")
        inputNumber.requestFocus()
    }

    private fun scrollToBottom() {
        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun showWinDialog() {
        AlertDialog.Builder(this)
            .setTitle("Felicitats!")
            .setMessage("Has endevinat el número en $attempts intents.\nVols jugar de nou?")
            .setPositiveButton("Sí") { _, _ -> startNewGame() }
            .setNegativeButton("No") { _, _ -> finish() }
            .show()
    }
}
