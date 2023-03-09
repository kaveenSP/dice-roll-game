package com.example.diceroll

import kotlin.random.Random

open class Game {
    var winningScore : Int = 101
    var humanWins:Int = 0
    var computerWins:Int = 0

    var computerScore:Int = 0

    fun generateRandomIntegers(n: Int): Array<Int> {
        val result = Array(n) { 0 }
        for (i in 0 until n) {
            result[i] = (1..6).random()
        }
        return result
    }


}