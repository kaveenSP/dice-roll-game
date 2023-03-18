package com.example.diceroll

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    companion object {
        const val WINNING_SCORE = "com.example.application.example.WINNING_SCORE"
        const val GAME_MODE = "com.example.application.example.GAME_MODE"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val changeScorePopup = findViewById<Button>(R.id.newGame)
        val aboutPopup = findViewById<Button>(R.id.about)
        val htpPopup = findViewById<Button>(R.id.htp)

        changeScorePopup.setOnClickListener {
            showChangeScorePopup()
        }
        aboutPopup.setOnClickListener {
            showAboutPopup()
        }
        htpPopup.setOnClickListener {
            showHowToPlay()
        }
    }

    @SuppressLint("MissingInflatedId", "UseCompatLoadingForDrawables",
        "UseSwitchCompatOrMaterialCode"
    )
    private fun showChangeScorePopup() {
        // Inflate the popup_layout.xml file
        val scorePopupView = layoutInflater.inflate(R.layout.change_score, null)

        // Create a PopupWindow object
        val scorePopupWindow = PopupWindow(
            scorePopupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Get references to the views in the popup window
        val scoreText = scorePopupView.findViewById<TextInputEditText>(R.id.current_game_score)
        val closeButton = scorePopupView.findViewById<Button>(R.id.cancel_button)
        val startGameButton = scorePopupView.findViewById<Button>(R.id.start_game)
        val gameModeSwitch = scorePopupView.findViewById<Switch>(R.id.game_mode)

        // Set a click listener for the close button
        closeButton.setOnClickListener {
            // Dismiss the popup window
            scorePopupWindow.dismiss()
        }

        var gameMode = "easy"
        gameModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            gameMode = if (isChecked) "hard" else "easy"
        }

        startGameButton.setOnClickListener {
            var winningScore = 0
            try {
                winningScore = Integer.parseInt(scoreText.text.toString())
            } catch (e : java.lang.NumberFormatException) {
                e.printStackTrace()
            }

            val gameActivityIntent = Intent(this, GameActivity::class.java)
            gameActivityIntent.putExtra(WINNING_SCORE, winningScore)
            gameActivityIntent.putExtra(GAME_MODE, gameMode)
            startActivity(gameActivityIntent)
            scorePopupWindow.dismiss()
        }

        // Show the popup window
        scorePopupWindow.showAtLocation(scorePopupView, Gravity.CENTER, 0, 0)
    }

    @SuppressLint("MissingInflatedId")
    private fun showHowToPlay() {
        // Inflate the popup_layout.xml file
        val howToPlayView = layoutInflater.inflate(R.layout.how_to_play, null)

        // Create a PopupWindow object
        val howToPlayWindow = PopupWindow(
            howToPlayView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        val closeButtonHTP = howToPlayView.findViewById<Button>(R.id.close_howtoplay)

        // Set a click listener for the close button
        closeButtonHTP.setOnClickListener {
            // Dismiss the popup window
            howToPlayWindow.dismiss()
        }

        // Show the popup window
        howToPlayWindow.showAtLocation(howToPlayView, Gravity.CENTER, 0, 0)
    }
    @SuppressLint("MissingInflatedId")
    private fun showAboutPopup() {
        // Inflate the popup_layout.xml file
        val popupView = layoutInflater.inflate(R.layout.about, null)

        // Create a PopupWindow object
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        // Get references to the views in the popup window
        val closeButton = popupView.findViewById<Button>(R.id.cancel_about)

        // Set a click listener for the close button
        closeButton.setOnClickListener {
            // Dismiss the popup window
            popupWindow.dismiss()
        }
        // Show the popup window
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }
}