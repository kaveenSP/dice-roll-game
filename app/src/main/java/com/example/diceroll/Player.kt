package com.example.diceroll

class Player() : Game() {
    var score : Int = 0

    fun addScore(diceScores : Array<Int>){
        var totalDieScore = 0
        for (dieScore in diceScores) {
            totalDieScore += dieScore
        }
        score += totalDieScore
    }
}