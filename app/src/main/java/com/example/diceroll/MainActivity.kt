package com.example.diceroll

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlin.properties.Delegates
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var game: Game
    private lateinit var humanPlayer: Player
    private lateinit var computerPlayer: Player
    private val delayTime = 20
    private val rollAnimationCount = 40
    private val rollCountPerPlayer = 3
    private var hRollNumber by Delegates.notNull<Int>()
    private var cRollNumber by Delegates.notNull<Int>()
    private lateinit var humanDice: MutableList<Int>
    private lateinit var computerDice: MutableList<Int>
    private val diceImages = intArrayOf(R.drawable.dieface_1, R.drawable.dieface_2, R.drawable.dieface_3, R.drawable.dieface_4, R.drawable.dieface_5, R.drawable.dieface_6)
    private lateinit var hDiceImageViews: MutableList<ImageView>
    private lateinit var cDiceImageViews: MutableList<ImageView>
    private lateinit var hNotSelectedImageViews: MutableList<ImageView>
    private lateinit var cNotSelectedImageViews: MutableList<ImageView>
    private lateinit var hSelectedDieScores: MutableList<Int>
    private lateinit var cSelectedDieScores: MutableList<Int>
    private lateinit var humanScore:TextView
    private lateinit var computerScore:TextView
    private lateinit var humanWins:TextView
    private lateinit var computerWins:TextView
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
            game = Game()
            humanPlayer = Player()
            computerPlayer = Player()
            hRollNumber = 1
            cRollNumber = 1
            humanDice = mutableListOf()
            computerDice = mutableListOf()
            hDiceImageViews = mutableListOf()
            cDiceImageViews = mutableListOf()
            hNotSelectedImageViews = mutableListOf()
            cNotSelectedImageViews = mutableListOf()
            hSelectedDieScores = mutableListOf()
            cSelectedDieScores = mutableListOf()
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

            val throwButton = gameView.findViewById<Button>(R.id.throwDice)
            val scoreButton = gameView.findViewById<Button>(R.id.score)
            val closeGameScreen = gameView.findViewById<Button>(R.id.close_game_screen)

            scoreButton.isEnabled = false

            val delayMillis: Long = 1000

            humanWins= gameView.findViewById(R.id.human_wins)
            computerWins = gameView.findViewById(R.id.computer_wins)
            humanScore = gameView.findViewById(R.id.human_score)
            computerScore = gameView.findViewById(R.id.computer_score)
            val roll = gameView.findViewById<TextView>(R.id.roll)

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
                when(hRollNumber) {
                    1 -> {
                        hSelectedDieScores = mutableListOf()
                        for (cImageView in cDiceImageViews) {
                            cImageView.isSelected = false
                        }
                        cNotSelectedImageViews = mutableListOf()
                        hNotSelectedImageViews.addAll(hDiceImageViews)
                        cNotSelectedImageViews.addAll(cDiceImageViews)
                        rollDice(hDiceImageViews.size,cDiceImageViews.size)
                        hRollNumber += 1
                        cRollNumber += 1
                        scoreButton.isEnabled = true
                        roll.text = hRollNumber.toString()
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
                        } else {
                            cNotSelectedImageViews = mutableListOf()
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
                        roll.text = hRollNumber.toString()
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
                        } else {
                            cNotSelectedImageViews = mutableListOf()
                        }
                        rollDice(hNotSelectedImageViews.size,cNotSelectedImageViews.size)
                        for(i in 0 until hDiceImageViews.size) {
                            if(hDiceImageViews[i].isSelected) {
                                hDiceImageViews[i].setBackgroundColor(Color.TRANSPARENT)
                                hDiceImageViews[i].isSelected = !hDiceImageViews[i].isSelected
                            }
                        }
                        humanPlayer.addScore(hSelectedDieScores)
                        computerPlayer.addScore(cSelectedDieScores)
                        humanScore.text = humanPlayer.score.toString()
                        computerScore.text = computerPlayer.score.toString()
                        hRollNumber = 1
                        cRollNumber = 1
                        roll.text = hRollNumber.toString()
                        scoreButton.isEnabled = false
                        if (humanPlayer.score == computerPlayer.score) {
                            while (humanPlayer.score != computerPlayer.score) {
                                rollDice(hDiceImageViews.size, cDiceImageViews.size)
                                humanPlayer.addScore(hSelectedDieScores)
                                humanScore.text = humanPlayer.score.toString()
                                computerPlayer.addScore(cSelectedDieScores)
                                computerScore.text = computerPlayer.score.toString()
                            }
                        }
                        if (humanPlayer.score >= game.winningScore && humanPlayer.score > computerPlayer.score) {
                            game.humanWins += 1
                            humanWins.text = game.humanWins.toString()
                            humanPlayer.score = 0
                            humanScore.text = humanPlayer.score.toString()
                            showWinScreen("You Won", true)
                        } else if (computerPlayer.score >= game.winningScore && humanPlayer.score < computerPlayer.score) {
                            game.computerWins += 1
                            computerWins.text = game.computerWins.toString()
                            computerPlayer.computerWins = 0
                            computerScore.text = computerPlayer.score.toString()
                            showWinScreen("You Lose", false)
                        }
                    }
                }
            }

            scoreButton.setOnClickListener {
                scoreButton.isEnabled = false
                hNotSelectedImageViews = mutableListOf()
//                if(diceThrown && gameStarted && hRollNumber != 3) {
//                    humanPlayer.addScore(hSelectedDieScores)
//                    humanScore.text = humanPlayer.score.toString()
//                }
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
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    humanPlayer.addScore(hSelectedDieScores)
                    humanScore.text = humanPlayer.score.toString()
                    computerPlayer.addScore(cSelectedDieScores)
                    computerScore.text = computerPlayer.score.toString()
                    if (humanPlayer.score == computerPlayer.score) {
                        while (humanPlayer.score != computerPlayer.score) {
                            rollDice(hDiceImageViews.size, cDiceImageViews.size)
                            humanPlayer.addScore(hSelectedDieScores)
                            humanScore.text = humanPlayer.score.toString()
                            computerPlayer.addScore(cSelectedDieScores)
                            computerScore.text = computerPlayer.score.toString()
                        }
                    }
                    if (humanPlayer.score >= game.winningScore && humanPlayer.score > computerPlayer.score) {
                        game.humanWins += 1
                        humanWins.text = game.humanWins.toString()
                        humanPlayer.score = 0
                        humanScore.text = humanPlayer.score.toString()
                        showWinScreen("You Won", true)
                    } else if (computerPlayer.score >= game.winningScore && humanPlayer.score < computerPlayer.score) {
                        game.computerWins += 1
                        computerWins.text = game.computerWins.toString()
                        computerPlayer.computerWins = 0
                        computerScore.text = computerPlayer.score.toString()
                        showWinScreen("You Lose", false)
                    }

                }, delayMillis)
                hRollNumber = 1
                cRollNumber = 1
                roll.text = hRollNumber.toString()
            }

            closeGameScreen.setOnClickListener {
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
            showHowToPlay()
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

    @SuppressLint("MissingInflatedId")
    private fun showWinScreen(text:String, won:Boolean) {
        // Inflate the popup_layout.xml file
        val winView = layoutInflater.inflate(R.layout.win_screen, null)

        // Create a PopupWindow object
        val winWindow = PopupWindow(
            winView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val closeButtonWinScreen = winView.findViewById<Button>(R.id.close_win_screen)
        val winText = winView.findViewById<TextView>(R.id.win_text)

        winText.text = text
        if (won) {
            winText.setTextColor(Color.GREEN)
        } else {
            winText.setTextColor(Color.RED)
        }


        // Set a click listener for the close button
        closeButtonWinScreen.setOnClickListener {
            // Dismiss the popup window
            humanPlayer.score = 0
            humanScore.text = humanPlayer.score.toString()
            computerPlayer.score = 0
            computerScore.text = computerPlayer.score.toString()
            winWindow.dismiss()
        }

        // Show the popup window
        winWindow.showAtLocation(winView, Gravity.CENTER, 0, 0)
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

    private fun rollDice(hDiceCount:Int, cDiceCount:Int) {
        val diceAnimationThread = Thread {
            //Code to be executed in the new thread
            for (i in 1..rollAnimationCount) {
                humanDice = game.generateRandomIntegers(hDiceCount)
                computerDice = game.generateRandomIntegers(cDiceCount)
                for (j in 0 until hDiceCount) {
                    hNotSelectedImageViews[j].setImageResource(diceImages[humanDice[j]-1])
                    hNotSelectedImageViews[j].tag = diceImages[humanDice[j]-1]
                }
                for (k in 0 until cDiceCount) {
                    cNotSelectedImageViews[k].setImageResource(diceImages[computerDice[k]-1])
                    cNotSelectedImageViews[k].tag = diceImages[computerDice[k]-1]
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
            hSelectedDieScores = mutableListOf()
            cSelectedDieScores = mutableListOf()
            for (imageView in hDiceImageViews) {
                val parts = resources.getResourceName((imageView.tag as Int)).toString().split("_")
                hSelectedDieScores.add(parts[1].toInt())
            }
            for (imageView in cDiceImageViews) {
                val parts = resources.getResourceName((imageView.tag as Int)).toString().split("_")
                cSelectedDieScores.add(parts[1].toInt())
            }
            hNotSelectedImageViews = mutableListOf()
        }
        diceAnimationThread.start()
    }
}