package com.example.simplecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplecalculator.ui.theme.SimpleCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var display by remember { mutableStateOf("0") }
    var firstNumber by remember { mutableStateOf(0.0) }
    var operation by remember { mutableStateOf("") }
    var isNewNumber by remember { mutableStateOf(true) }
    var history by remember { mutableStateOf(listOf<String>()) }

    fun formatResult(number: Double): String {
        return if (number % 1 == 0.0) {
            number.toInt().toString()
        } else {
            String.format("%.2f", number)
        }
    }

    fun onNumberClick(number: String) {
        if (isNewNumber) {
            display = number
            isNewNumber = false
        } else {
            if (display == "0") {
                display = number
            } else {
                display += number
            }
        }
    }

    fun onOperationClick(op: String) {
        firstNumber = display.toDoubleOrNull() ?: 0.0
        operation = op
        isNewNumber = true
    }

    fun calculateResult() {
        val secondNumber = display.toDoubleOrNull() ?: 0.0
        val result = when (operation) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "×" -> firstNumber * secondNumber
            "÷" -> if (secondNumber != 0.0) firstNumber / secondNumber else Double.POSITIVE_INFINITY
            else -> secondNumber
        }

        val calculation = "$firstNumber $operation $secondNumber = ${formatResult(result)}"
        history = listOf(calculation) + history
        display = formatResult(result)
        isNewNumber = true
        operation = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Historique
        LazyColumn(
            modifier = Modifier
                .weight(4f)
                .fillMaxWidth()
        ) {
            items(history) { calculation ->
                Text(
                    text = calculation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    textAlign = TextAlign.End,
                    color = Color.Gray
                )
            }
        }

        // Écran d'affichage
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = display,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                textAlign = TextAlign.End,
                fontSize = 40.sp
            )
        }

        // Grille de boutons
        Column(
            modifier = Modifier.weight(6f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("7", Modifier.weight(1f)) { onNumberClick(it) }
                CalculatorButton("8", Modifier.weight(1f)) { onNumberClick(it) }
                CalculatorButton("9", Modifier.weight(1f)) { onNumberClick(it) }
                CalculatorButton("÷", Modifier.weight(1f), Color(0xFFFF9800)) { onOperationClick(it) }
            }
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("4", Modifier.weight(1f)) { onNumberClick(it) }
                CalculatorButton("5", Modifier.weight(1f)) { onNumberClick(it) }
                CalculatorButton("6", Modifier.weight(1f)) { onNumberClick(it) }
                CalculatorButton("×", Modifier.weight(1f), Color(0xFFFF9800)) { onOperationClick(it) }
            }
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("1", Modifier.weight(1f)) { onNumberClick(it) }
                CalculatorButton("2", Modifier.weight(1f)) { onNumberClick(it) }
                CalculatorButton("3", Modifier.weight(1f)) { onNumberClick(it) }
                CalculatorButton("-", Modifier.weight(1f), Color(0xFFFF9800)) { onOperationClick(it) }
            }
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("C", Modifier.weight(1f), Color(0xFFF44336)) {
                    display = "0"
                    firstNumber = 0.0
                    operation = ""
                    isNewNumber = true
                }
                CalculatorButton("0", Modifier.weight(1f)) { onNumberClick(it) }
                CalculatorButton("=", Modifier.weight(1f), Color(0xFF4CAF50)) {
                    calculateResult()
                }
                CalculatorButton("+", Modifier.weight(1f), Color(0xFFFF9800)) { onOperationClick(it) }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    onClick: (String) -> Unit
) {
    Button(
        onClick = { onClick(text) },
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1f),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            color = if (backgroundColor == MaterialTheme.colorScheme.surface)
                MaterialTheme.colorScheme.onSurface
            else
                Color.White
        )
    }
}