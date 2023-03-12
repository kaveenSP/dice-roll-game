package com.example.diceroll

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var game = Game()
    private var humanPlayer = Player()
    private var computerPlayer = Player()
    private val delayTime = 20
    private val rollAnimationCount = 40
    private val rollCountPerPlayer = 3
    private var hRollNumber = 1
    private var cRollNumber = 1
    private lateinit var humanDice:IntArray
    private lateinit var humanScoreSet:IntArray
    private lateinit var computerDice:IntArray
    private val diceImages = intArrayOf(R.drawable.die_face_1, R.drawable.die_face_2, R.drawable.die_face_3, R.drawable.die_face_4, R.drawable.die_face_5, R.drawable.die_face_6)
    private val hDiceImageViews = mutableListOf<ImageView>()
    private val cDiceImageViews = mutableListOf<ImageView>()
    private var hNotSelectedImageViews = mutableListOf<ImageView>()
    private var cNotSelectedImageViews = mutableListOf<ImageView>()
    private lateinit var hDie01:ImageView
    private lateinit var hDie02:ImageView
    private lateinit var hDie03:ImageView
    private lateinit var hDie04:ImageView
    private lateinit var hDie05:ImageView
    private lateinit var cDie01:ImageView
    private lateinit var cDie02:ImageView
    private lateinit var cDie03:ImageView
    private lateinit var cDie04:ImageView
    private lateinit var cDie05:ImageView

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

        // Set a click listener for the close button
        closeButton.setOnClickListener {
            // Dismiss the popup window
            scorePopupWindow.dismiss()
        }

        startGameButton.setOnClickListener {
            try {
                game.winningScore = Integer.parseInt(scoreText.text.toString())
            } catch (e : java.lang.NumberFormatException) {
                e.printStackTrace()
            }
            scorePopupWindow.dismiss()

            // Inflate the popup_layout.xml file
            val gameView = layoutInflater.inflate(R.layout.game, null)

            // Create a PopupWindow object
            val gameWindow = PopupWindow(
                gameView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true
            )

            val humanScore = gameView.findViewById<TextView>(R.id.human_score)
            val throwButton = gameView.findViewById<Button>(R.id.throwDice)
            val scoreButton = gameView.findViewById<Button>(R.id.score)
            val closeGameScreen = gameView.findViewById<Button>(R.id.close_game_screen)

            var diceThrown = false
            var gameStarted = false

            hDie01 = gameView.findViewById(R.id.hd_1)
            hDie02 = gameView.findViewById(R.id.hd_2)
            hDie03 = gameView.findViewById(R.id.hd_3)
            hDie04 = gameView.findViewById(R.id.hd_4)
            hDie05 = gameView.findViewById(R.id.hd_5)

            cDie01 = gameView.findViewById(R.id.cd_1)
            cDie02 = gameView.findViewById(R.id.cd_2)
            cDie03 = gameView.findViewById(R.id.cd_3)
            cDie04 = gameView.findViewById(R.id.cd_4)
            cDie05 = gameView.findViewById(R.id.cd_5)
            hDiceImageViews.addAll(mutableListOf(hDie01,hDie02,hDie03,hDie04,hDie05))
            cDiceImageViews.addAll(mutableListOf(cDie01,cDie02,cDie03,cDie04,cDie05))

            throwButton.setOnClickListener {
                gameStarted = true
                when(hRollNumber) {
                    1 -> {
                        for (cImageView in cDiceImageViews) {
                            cImageView.isSelected = false
                        }
                        cNotSelectedImageViews = mutableListOf()
                        hNotSelectedImageViews.addAll(hDiceImageViews)
                        cNotSelectedImageViews.addAll(cDiceImageViews)
                        rollDice(hDiceImageViews.size,cDiceImageViews.size)
                        hRollNumber += 1
                        cRollNumber += 1
                    }
                    2 -> {
                        for(i in 0 until hDiceImageViews.size) {
                            if(!hDiceImageViews[i].isSelected) {
                                hNotSelectedImageViews.add(hDiceImageViews[i])
                            }
                        }
                        val cRollTaken = Random.nextBoolean()
                        Log.d("Computer Roll Taken", cRollTaken.toString())
                        if (cRollTaken) {
                            val iter = cNotSelectedImageViews.listIterator()
                            while (iter.hasNext()) {
                                val imageView = iter.next()
                                if (!imageView.isSelected) {
                                    iter.remove()
                                }
                            }
                        }
                        rollDice(hNotSelectedImageViews.size,cNotSelectedImageViews.size)
                        for(i in 0 until hDiceImageViews.size) {
                            if(hDiceImageViews[i].isSelected) {
                                hDiceImageViews[i].setBackgroundColor(Color.TRANSPARENT)
                                hDiceImageViews[i].isSelected = !hDiceImageViews[i].isSelected
                            }
                        }

                        hRollNumber += 1
                        cRollNumber += 1
                    }
                    3 -> {
                        for(i in 0 until hDiceImageViews.size) {
                            if(!hDiceImageViews[i].isSelected) {
                                hNotSelectedImageViews.add(hDiceImageViews[i])
                            }
                        }
                        val cRollTaken = Random.nextBoolean()
                        Log.d("Computer Roll Taken", cRollTaken.toString())
                        if (cRollTaken) {
                            val iter = cNotSelectedImageViews.listIterator()
                            while (iter.hasNext()) {
                                val imageView = iter.next()
                                if (!imageView.isSelected) {
                                    iter.remove()
                                }
                            }
                        }
                        rollDice(hNotSelectedImageViews.size,cNotSelectedImageViews.size)
                        for(i in 0 until hDiceImageViews.size) {
                            if(hDiceImageViews[i].isSelected) {
                                hDiceImageViews[i].setBackgroundColor(Color.TRANSPARENT)
                                hDiceImageViews[i].isSelected = !hDiceImageViews[i].isSelected
                            }
                        }
                        humanPlayer.addScore(humanDice)
                        humanScore.text = humanPlayer.score.toString()
                        if(humanPlayer.score >= game.winningScore) {

                        } else if(computerPlayer.score >= game.winningScore) {

                        }
                        hRollNumber = 1
                        cRollNumber = 1
                    }
                }
                diceThrown = true
            }

            scoreButton.setOnClickListener {
                hNotSelectedImageViews = mutableListOf()
                if(diceThrown && gameStarted && hRollNumber != 3) {
                    humanPlayer.addScore(humanDice)
                    humanScore.text = humanPlayer.score.toString()
                }
                for(i in 0 until rollCountPerPlayer - cRollNumber) {
                    val iter = cNotSelectedImageViews.listIterator()
                    while (iter.hasNext()) {
                        val imageView = iter.next()
                        if (!imageView.isSelected) {
                            iter.remove()
                        }
                    }
                    rollDice(0, cNotSelectedImageViews.size)
                }

                if(humanPlayer.score >= game.winningScore) {

                } else if(computerPlayer.score >= game.winningScore) {

                }
                diceThrown = false
                hRollNumber = 1
            }

            closeGameScreen.setOnClickListener {
                gameStarted = false
                gameWindow.dismiss()
            }

            hDie01.setOnClickListener {
                if(hRollNumber != 1) {
                    hDie01.isSelected = !hDie01.isSelected
                    if (hDie01.isSelected) {
                        hDie01.setBackgroundColor(Color.rgb(11, 70, 134))
                    } else {
                        hDie01.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
            hDie02.setOnClickListener {
                if(hRollNumber != 1) {
                    hDie02.isSelected = !hDie02.isSelected
                    if (hDie02.isSelected) {
                        hDie02.setBackgroundColor(Color.rgb(11, 70, 134))
                    } else {
                        hDie02.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
            hDie03.setOnClickListener {
                if(hRollNumber != 1) {
                    hDie03.isSelected = !hDie03.isSelected
                    if (hDie03.isSelected) {
                        hDie03.setBackgroundColor(Color.rgb(11, 70, 134))
                    } else {
                        hDie03.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
            hDie04.setOnClickListener {
                if(hRollNumber != 1) {
                    hDie04.isSelected = !hDie04.isSelected
                    if (hDie04.isSelected) {
                        hDie04.setBackgroundColor(Color.rgb(11, 70, 134))
                    } else {
                        hDie04.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
            hDie05.setOnClickListener {
                if(hRollNumber != 1) {
                    hDie05.isSelected = !hDie05.isSelected
                    if (hDie05.isSelected) {
                        hDie05.setBackgroundColor(Color.rgb(11, 70, 134))
                    } else {
                        hDie05.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }

            gameWindow.showAtLocation(gameView, Gravity.NO_GRAVITY, 0, 0)
        }

        // Show the popup window
        scorePopupWindow.showAtLocation(scorePopupView, Gravity.CENTER, 0, 0)
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

    private fun rollDice(hDiceCount:Int, cDiceCount:Int) {
        val diceAnimationThread = Thread {
            //Code to be executed in the new thread
            for (i in 1..rollAnimationCount) {
                humanDice = game.generateRandomIntegers(hDiceCount)
                computerDice = game.generateRandomIntegers(cDiceCount)
                for (j in 0 until hDiceCount) {
                    hNotSelectedImageViews[j].setImageResource(diceImages[humanDice[j]-1])
                }
                for (k in 0 until cDiceCount) {
                    cNotSelectedImageViews[k].setImageResource(diceImages[computerDice[k]-1])
                }
                for (l in 0 until cNotSelectedImageViews.size) {
                    cNotSelectedImageViews[l].isSelected = computerDice[l] <= 3
                }

                try {
                    Thread.sleep(delayTime.toLong())
                } catch(e : InterruptedException) {
                    e.printStackTrace()
                }
            }
            hNotSelectedImageViews = mutableListOf()
        }
        diceAnimationThread.start()
    }
}