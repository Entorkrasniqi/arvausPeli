package com.example.arvauspeli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arvauspeli.ui.theme.ArvausPeliTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArvausPeliTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GuessGameScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GuessGameScreen(modifier: Modifier = Modifier) {
    var gameState by remember { mutableStateOf(0) } // used to trigger recomposition on new game
    val game = remember(gameState) { YliAliPeli() }
    var inputText by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("Arvaa luku välillä 1–100!") }
    var gameOver by remember { mutableStateOf(false) }
    var feedbackColor by remember { mutableStateOf(androidx.compose.ui.graphics.Color(0xFF1565C0)) }

    fun submitGuess() {
        val guess = inputText.toIntOrNull()
        if (guess == null || guess !in 1..100) {
            feedbackMessage = "Syötä kelvollinen luku välillä 1–100."
            feedbackColor = androidx.compose.ui.graphics.Color(0xFFB71C1C)
            return
        }
        when (game.makeGuess(guess)) {
            YliAliPeli.Result.TOO_LOW -> {
                feedbackMessage = "Liian pieni! Yritä uudelleen."
                feedbackColor = androidx.compose.ui.graphics.Color(0xFFE65100)
            }
            YliAliPeli.Result.TOO_HIGH -> {
                feedbackMessage = "Liian suuri! Yritä uudelleen."
                feedbackColor = androidx.compose.ui.graphics.Color(0xFF6A1B9A)
            }
            YliAliPeli.Result.HIT -> {
                feedbackMessage = "🎉 Oikein! Arvasit luvun ${game.guessCount} arvauksella!"
                feedbackColor = androidx.compose.ui.graphics.Color(0xFF1B5E20)
                gameOver = true
            }
        }
        inputText = ""
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Arvauuspeli",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "by Entor Krasniqi",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = feedbackMessage,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = feedbackColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        if (!gameOver) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Arvauksesi (1–100)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { submitGuess() }),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { submitGuess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Arvaa", fontSize = 18.sp)
            }
        } else {
            Button(
                onClick = {
                    gameState++ // creates a fresh YliAliPeli via remember(gameState)
                    inputText = ""
                    feedbackMessage = "Arvaa luku välillä 1–100!"
                    feedbackColor = androidx.compose.ui.graphics.Color(0xFF1565C0)
                    gameOver = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Pelaa uudelleen", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GuessGamePreview() {
    ArvausPeliTheme {
        GuessGameScreen()
    }
}