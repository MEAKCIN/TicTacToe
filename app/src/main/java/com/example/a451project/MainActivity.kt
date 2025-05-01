package com.example.a451project

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var turnIndicator: TextView
    private lateinit var xScoreText: TextView
    private lateinit var oScoreText: TextView
    private lateinit var drawsText: TextView
    private lateinit var playAgainButton: Button

    private lateinit var boardButtons: Array<Button>
    private var board = Array(9) { "" }

    private var xTurn = true
    private var gameFinished = false

    private var xWins = 0
    private var oWins = 0
    private var draws = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        turnIndicator = findViewById(R.id.turnIndicator)
        xScoreText = findViewById(R.id.xScore)
        oScoreText = findViewById(R.id.oScore)
        drawsText = findViewById(R.id.draws)
        playAgainButton = findViewById(R.id.playAgain)

        boardButtons = Array(9) { i ->
            findViewById(resources.getIdentifier("cell$i", "id", packageName))
        }

        boardButtons.forEachIndexed { index, button ->
            button.setOnClickListener { onCellClicked(index) }
        }

        playAgainButton.setOnClickListener { resetBoard() }
    }

    private fun onCellClicked(index: Int) {
        if (board[index].isNotEmpty() || gameFinished) return

        // mark cell with correct symbol
        board[index] = if (xTurn) "X" else "O"
        boardButtons[index].text = board[index]

        // check for winner or draw
        val winnerCells = checkWinner()
        if (winnerCells != null) {
            // highlight winner and update score
            winnerCells.forEach { boardButtons[it].setTextColor(Color.RED) }
            gameFinished = true
            val winner = board[index]
            if (winner == "X") {
                xWins++
                xScoreText.text = "X Wins: $xWins"
            } else {
                oWins++
                oScoreText.text = "O Wins: $oWins"
            }
            turnIndicator.text = "Player '$winner' wins!"
            playAgainButton.isEnabled = true
        } else if (board.all { it.isNotEmpty() }) {
            // draw case
            draws++
            drawsText.text = "Draws: $draws"
            gameFinished = true
            turnIndicator.text = "It's a draw!"
            playAgainButton.isEnabled = true
        } else {
            // switch player
            xTurn = !xTurn
            turnIndicator.text = "Player '${if(xTurn) "X" else "O"}' turn"
        }
    }

    private fun checkWinner(): List<Int>? {
        val winningPositions = arrayOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )
        for (line in winningPositions) {
            val (a, b, c) = line
            if (board[a].isNotEmpty() && board[a] == board[b] && board[a] == board[c]) {
                return line
            }
        }
        return null
    }

    private fun resetBoard() {
        board = Array(9) { "" }
        boardButtons.forEach {
            it.text = ""
            it.setTextColor(Color.BLACK)
        }
        xTurn = true
        gameFinished = false
        turnIndicator.text = "Player 'X' turn"
        playAgainButton.isEnabled = false
    }
}