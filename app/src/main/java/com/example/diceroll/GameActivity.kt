package com.example.diceroll

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import kotlin.random.Random

open class GameActivity : AppCompatActivity() {

    private var winningScore : Int = 101
    private var gameMode = "easy"
    private var humanWins:Int = 0
    private var computerWins:Int = 0
    private lateinit var humanPlayer: Player
    private lateinit var computerPlayer: Player
    private val delayTime = 20
    private val rollAnimationCount = 40
    private val rollCountPerPlayer = 3
    private var hRollNumber = 1
    private var cRollNumber = 1
    private lateinit var humanDice: MutableList<Int>
    private lateinit var computerDice: MutableList<Int>
    private val diceImages = intArrayOf(R.drawable.dieface_1, R.drawable.dieface_2, R.drawable.dieface_3, R.drawable.dieface_4, R.drawable.dieface_5, R.drawable.dieface_6)
    private lateinit var hDiceImageViews: MutableList<ImageView>
    private lateinit var cDiceImageViews: MutableList<ImageView>
    private lateinit var hNotSelectedImageViews: MutableList<ImageView>
    private lateinit var cNotSelectedImageViews: MutableList<ImageView>
    private lateinit var hSelectedDieScores: MutableList<Int>
    private lateinit var cSelectedDieScores: MutableList<Int>
    private lateinit var humanScore: TextView
    private lateinit var computerScore: TextView
    private lateinit var tvHumanWins: TextView
    private lateinit var tvComputerWins: TextView
    private lateinit var throwButton: Button

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_activity)

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

        val scoreButton = findViewById<Button>(R.id.score)
        val closeGameScreen = findViewById<Button>(R.id.close_game_screen)
        val wScoreView = findViewById<TextView>(R.id.w_score)
        throwButton = findViewById(R.id.throwDice)
        val delayMillis: Long = 1000

        winningScore = intent.getIntExtra(MainActivity.WINNING_SCORE, 0)
        gameMode = intent.getStringExtra(MainActivity.GAME_MODE).toString()
        Log.d("mode", gameMode)
        wScoreView.text = winningScore.toString()

        scoreButton.isEnabled = false

        tvHumanWins = findViewById(R.id.human_wins)
        tvComputerWins = findViewById(R.id.computer_wins)
        humanScore = findViewById(R.id.human_score)
        computerScore = findViewById(R.id.computer_score)
        val roll = findViewById<TextView>(R.id.roll)

        val totalWins = getPreferences(Context.MODE_PRIVATE) ?: return
        tvComputerWins.text = (totalWins.getInt("COMPUTER-SCORE", 0).toString())
        tvHumanWins.text = (totalWins.getInt("HUMAN-SCORE", 0).toString())

        hDiceImageViews.addAll(mutableListOf(findViewById(R.id.hd_1),findViewById(R.id.hd_2),findViewById(R.id.hd_3),findViewById(R.id.hd_4),findViewById(R.id.hd_5)))
        cDiceImageViews.addAll(mutableListOf(findViewById(R.id.cd_1),findViewById(R.id.cd_2),findViewById(R.id.cd_3),findViewById(R.id.cd_4),findViewById(R.id.cd_5)))

        for (hDieImageView in hDiceImageViews) {
            hDieImageView.setOnClickListener {
                if(hRollNumber != 1) {
                    hDieImageView.isSelected = !hDieImageView.isSelected
                    if (hDieImageView.isSelected) {
                        hDieImageView.setBackgroundColor(Color.rgb(255, 193, 7))
                    } else {
                        hDieImageView.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
        }

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
                    if (gameMode == "easy") {
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
                    } else {
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
                    roll.text = hRollNumber.toString()
                }
                3 -> {
                    for(i in 0 until hDiceImageViews.size) {
                        if(!hDiceImageViews[i].isSelected) {
                            hNotSelectedImageViews.add(hDiceImageViews[i])
                        }
                    }
                    if (gameMode == "easy") {
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
                    } else {
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
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        if (humanPlayer.score == computerPlayer.score) {
                            while (humanPlayer.score != computerPlayer.score) {
                                rollDice(hDiceImageViews.size, cDiceImageViews.size)
                                humanPlayer.addScore(hSelectedDieScores)
                                humanScore.text = humanPlayer.score.toString()
                                computerPlayer.addScore(cSelectedDieScores)
                                computerScore.text = computerPlayer.score.toString()
                            }
                        }
                        if (humanPlayer.score >= winningScore && humanPlayer.score > computerPlayer.score) {
                            humanWins += 1
                            tvHumanWins.text = humanWins.toString()
                            humanPlayer.score = 0
                            humanScore.text = humanPlayer.score.toString()
                            showWinScreen("You Won", true)
                        } else if (computerPlayer.score >= winningScore && humanPlayer.score < computerPlayer.score) {
                            computerWins += 1
                            tvComputerWins.text = computerWins.toString()
                            showWinScreen("You Lose", false)
                        }
                        humanPlayer.addScore(hSelectedDieScores)
                        computerPlayer.addScore(cSelectedDieScores)
                        humanScore.text = humanPlayer.score.toString()
                        computerScore.text = computerPlayer.score.toString()
                    }, delayMillis)
                    hRollNumber = 1
                    cRollNumber = 1
                    roll.text = hRollNumber.toString()
                    scoreButton.isEnabled = false
                }
            }
        }

        scoreButton.setOnClickListener {
            scoreButton.isEnabled = false
            hNotSelectedImageViews = mutableListOf()

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
                if (humanPlayer.score >= winningScore && humanPlayer.score > computerPlayer.score) {
                    humanWins += 1
                    tvHumanWins.text = humanWins.toString()
                    humanPlayer.score = 0
                    humanScore.text = humanPlayer.score.toString()
                    showWinScreen("You Won", true)
                } else if (computerPlayer.score >= winningScore && humanPlayer.score < computerPlayer.score) {
                    computerWins += 1
                    tvComputerWins.text = computerWins.toString()
                    showWinScreen("You Lose", false)
                }
            }, delayMillis)
            hRollNumber = 1
            cRollNumber = 1
            roll.text = hRollNumber.toString()
        }
        closeGameScreen.setOnClickListener {
            finish()
        }
    }

    private fun generateRandomIntegers(diceCount: Int): MutableList<Int> {
        val result = mutableListOf<Int>()
        for (i in 0 until diceCount) {
            result.add(Random.nextInt(1,6+1))
        }
        return result
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

        val winText = winView.findViewById<TextView>(R.id.win_text)

        winText.text = text
        if (won) {
            winText.setTextColor(Color.GREEN)
            storeWins(0, 1)
        } else {
            winText.setTextColor(Color.RED)
            storeWins(1, 0)
        }


            // Dismiss the popup window
            humanPlayer.score = 0
            humanScore.text = humanPlayer.score.toString()
            computerPlayer.score = 0
            computerScore.text = computerPlayer.score.toString()
            winWindow.dismiss()

        // Show the popup window
        winWindow.showAtLocation(winView, Gravity.CENTER, 0, 0)
    }



    private fun rollDice(hDiceCount:Int, cDiceCount:Int) {
        val diceAnimationThread = Thread {
            //Code to be executed in the new thread
            for (i in 1..rollAnimationCount) {
                humanDice = generateRandomIntegers(hDiceCount)
                computerDice = generateRandomIntegers(cDiceCount)
                for (j in 0 until hDiceCount) {
                    hNotSelectedImageViews[j].setImageResource(diceImages[humanDice[j]-1])
                    hNotSelectedImageViews[j].tag = diceImages[humanDice[j]-1]
                }
                for (k in 0 until cDiceCount) {
                    cNotSelectedImageViews[k].setImageResource(diceImages[computerDice[k]-1])
                    cNotSelectedImageViews[k].tag = diceImages[computerDice[k]-1]
                }
                for (l in 0 until cNotSelectedImageViews.size) {
                    if (gameMode == "easy") {
                        cNotSelectedImageViews[l].isSelected = Random.nextBoolean()
                    } else {
                        cNotSelectedImageViews[l].isSelected = computerDice[l] <= 3
                    }
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

    //locally store the data in the device
    private fun storeWins(computerPoint:Int,humanPoint:Int){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            val currentComputerPoint = sharedPref.getInt("COMPUTER-SCORE", 0)
            val currentHumanPoint = sharedPref.getInt("HUMAN-SCORE", 0)
            putInt("COMPUTER-SCORE",currentComputerPoint+computerPoint)
            putInt("HUMAN-SCORE",currentHumanPoint+humanPoint)
            apply()
        }
    }
}