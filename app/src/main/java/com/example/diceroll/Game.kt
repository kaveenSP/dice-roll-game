package com.example.diceroll

import kotlin.random.Random

open class Game {
    var winningScore : Int = 101
    var humanWins:Int = 0
    var computerWins:Int = 0

    var computerScore:Int = 0

    fun generateRandomIntegers(diceCount: Int): IntArray {
        val result = IntArray(diceCount)
        for (i in 0 until diceCount) {
            result[i] = Random.nextInt(1,6+1)
        }
        return result
    }


}