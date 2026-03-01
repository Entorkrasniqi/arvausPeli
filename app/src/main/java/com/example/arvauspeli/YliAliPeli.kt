package com.example.arvauspeli

class YliAliPeli {

    private var secretNumber: Int = (1..100).random()

    var guessCount: Int = 0
        private set

    enum class GuessResult {
        TOO_LOW,
        TOO_HIGH,
        HIT
    }

    fun makeGuess(guess: Int): GuessResult {
        guessCount++
        return when {
            guess < secretNumber -> GuessResult.TOO_LOW
            guess > secretNumber -> GuessResult.TOO_HIGH
            else -> GuessResult.HIT
        }
    }

    fun reset() {
        secretNumber = (1..100).random()
        guessCount = 0
    }
}