package com.example.diceroll

import kotlin.random.Random

open class Game {
    var winningScore : Int = 101
    var humanWins:Int = 0
    var computerWins:Int = 0

    fun generateRandomIntegers(diceCount: Int): MutableList<Int> {
        val result = mutableListOf<Int>()
        for (i in 0 until diceCount) {
            result.add(Random.nextInt(1,6+1))
        }
        return result
    }


}