package com.example.diceroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

open class Game : AppCompatActivity(){
    var winningScore : Int = 101
    var humanWins:Int = 0
    var computerWins:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)


    }



    fun generateRandomIntegers(diceCount: Int): MutableList<Int> {
        val result = mutableListOf<Int>()
        for (i in 0 until diceCount) {
            result.add(Random.nextInt(1,6+1))
        }
        return result
    }


}