package com.example.diceroll

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val changeScorePopup = findViewById<Button>(R.id.newGame)
        val aboutPopup = findViewById<Button>(R.id.about)

        changeScorePopup.setOnClickListener {
            showChangeScorePopup()
        }

        aboutPopup.setOnClickListener {
            showAboutPopup()
        }
    }

    @SuppressLint("MissingInflatedId", "UseCompatLoadingForDrawables")
    private fun showChangeScorePopup() {
        // Inflate the popup_layout.xml file
        val popupView = layoutInflater.inflate(R.layout.change_score, null)

        // Create a PopupWindow object
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Get references to the views in the popup window
        val closeButton = popupView.findViewById<Button>(R.id.cancel_button)
        val startGame_Button = popupView.findViewById<Button>(R.id.start_game)

        // Set a click listener for the close button
        closeButton.setOnClickListener {
            // Dismiss the popup window
            popupWindow.dismiss()
        }

        startGame_Button.setOnClickListener {
            // Inflate the popup_layout.xml file
            val gameView = layoutInflater.inflate(R.layout.game, null)

            // Create a PopupWindow object
            val gameWindow = PopupWindow(
                gameView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true
            )

            val throwButton = gameView.findViewById<Button>(R.id.throwDice)
            val scoreButton = gameView.findViewById<Button>(R.id.score)
            val die01 = gameView.findViewById<ImageView>(R.id.hd_1)
            val die02 = gameView.findViewById<ImageView>(R.id.hd_2)
            val die03 = gameView.findViewById<ImageView>(R.id.hd_3)
            val die04 = gameView.findViewById<ImageView>(R.id.hd_4)
            val die05 = gameView.findViewById<ImageView>(R.id.hd_5)

            var beginGame = true
            lateinit var diceHuman:Array<Int>
            lateinit var diceComputer:Array<Int>
            var game = Game()
            var humanPlayer = Player()
            var computerPlayer = Player()

//            val selectedImageBorder = resources.getDrawable(R.drawable.selected_image_border) as ShapeDrawable




            throwButton.setOnClickListener {
                if (beginGame) {
                    beginGame = false
                    diceHuman = game.generateRandomIntegers(6)
                    diceComputer = game.generateRandomIntegers(6)

                } else {
                    die01.setOnClickListener {
                        die01.isSelected = !die01.isSelected
                        if (die01.isSelected) {
                            die01.setBackgroundColor(Color.BLACK)
                        } else {
                            die01.setBackgroundColor(Color.TRANSPARENT)
                        }
                    }
                    die02.setOnClickListener {
                        die02.isSelected = !die02.isSelected
                        if (die02.isSelected) {
                            die02.setBackgroundColor(Color.BLACK)
                        } else {
                            die02.setBackgroundColor(Color.TRANSPARENT)
                        }
                    }
                    die03.setOnClickListener {
                        die03.isSelected = !die03.isSelected
                        if (die03.isSelected) {
                            die03.setBackgroundColor(Color.BLACK)
                        } else {
                            die03.setBackgroundColor(Color.TRANSPARENT)
                        }
                    }
                    die04.setOnClickListener {
                        die04.isSelected = !die04.isSelected
                        if (die04.isSelected) {
                            die04.setBackgroundColor(Color.BLACK)
                        } else {
                            die04.setBackgroundColor(Color.TRANSPARENT)
                        }
                    }
                    die05.setOnClickListener {
                        die05.isSelected = !die05.isSelected
                        if (die05.isSelected) {
                            die05.setBackgroundColor(Color.BLACK)
                        } else {
                            die05.setBackgroundColor(Color.TRANSPARENT)
                        }
                    }
                }

            }

            scoreButton.setOnClickListener {
                print("LOLLA")
            }

            gameWindow.showAtLocation(gameView, Gravity.NO_GRAVITY, 0, 0)
        }

        // Show the popup window
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
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
//        val textView = popupView.findViewById<TextView>(R.id.text_view)
//        val editText = popupView.findViewById<EditText>(R.id.edit_text)
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