package com.example.diceroll

import android.util.Log

open class Player : Game() {
    var score:Int = 0

    fun addScore(diceValues : IntArray){
        var totalDieScore = 0
        for (dieScore in diceValues) {
            totalDieScore += dieScore
        }
        this.score += totalDieScore
    }
}