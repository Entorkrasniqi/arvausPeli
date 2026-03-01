package com.example.arvauspeli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GuessingGameScreen()
                }
            }
        }
    }
}

@Composable
fun GuessingGameScreen() {
    var game by remember { mutableStateOf(YliAliPeli()) }
    var inputText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("I'm thinking of a number between 1 and 100. Can you guess it?") }
    var gameWon by remember { mutableStateOf(false) }
    var inputError by remember { mutableStateOf(false) }

    fun submitGuess() {
        val guess = inputText.toIntOrNull()
        if (guess == null || guess !in 1..100) {
            inputError = true
            message = "Please enter a valid number between 1 and 100."
            return
        }
        inputError = false
        val result = game.makeGuess(guess)
        when (result) {
            YliAliPeli.GuessResult.TOO_LOW -> {
                message = "Too low! Try a higher number."
            }
            YliAliPeli.GuessResult.TOO_HIGH -> {
                message = "Too high! Try a lower number."
            }
            YliAliPeli.GuessResult.HIT -> {
                gameWon = true
                val guesses = game.guessCount
                message = "Correct! You guessed it in $guesses ${if (guesses == 1) "guess" else "guesses"}!"
            }
        }
        inputText = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ArvausPeli",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "By Entorkrasniqi",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (gameWon)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (!gameWon) {
            OutlinedTextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    inputError = false
                },
                label = { Text("Your guess (1-100)") },
                isError = inputError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { submitGuess() }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { submitGuess() },
                modifier = Modifier.fillMaxWidth(),
                enabled = inputText.isNotBlank()
            ) {
                Text("Guess!", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (game.guessCount > 0) {
                Text(
                    text = "Guesses so far: ${game.guessCount}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Button(
                onClick = {
                    game = YliAliPeli()
                    message = "I'm thinking of a number between 1 and 100. Can you guess it?"
                    gameWon = false
                    inputText = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Play Again", fontSize = 18.sp)
            }
        }
    }
}