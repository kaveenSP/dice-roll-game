package com.example.diceroll

open class Player : GameActivity() {
    var score:Int = 0

    fun addScore(diceValues : MutableList<Int>){
        var totalDieScore = 0
        for (dieScore in diceValues) {
            totalDieScore += dieScore
        }
        this.score += totalDieScore
    }
}