package com.example.myapplication.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

enum class Hand(
    val title: String
) {
    SCISSORS("가위"),
    ROCK("바위"),
    PAPER("보")
}

enum class GameResult(
    val title: String
) {
    WIN("내가 이겼다!"),
    LOSE("컴퓨터 승리!"),
    DRAW("무승부!"),
    NONE("버튼을 눌러 선택하세요!")
}

data class RpsUiState(
    val round: Int = 1,

    val userScore: Int = 0,
    val computerScore: Int = 0,

    val user: Hand? = null,
    val computer: Hand? = null,

    val result: GameResult = GameResult.NONE
)

private fun calculateResult(user: Hand, computer: Hand): GameResult {
    return when {
        user == computer -> GameResult.DRAW
        (user == Hand.ROCK && computer == Hand.SCISSORS) ||
        (user == Hand.SCISSORS && computer == Hand.PAPER) ||
        (user == Hand.PAPER && computer == Hand.ROCK) -> GameResult.WIN
        else -> GameResult.LOSE
    }
}

private fun playRound(
    state: RpsUiState,
    userChoice: Hand
): RpsUiState {
    val computer = Hand.entries[Random.nextInt(Hand.entries.size)]
    val result = calculateResult(userChoice, computer)

    val userScore = if (result == GameResult.WIN) {
        state.userScore + 1
    } else {
        state.userScore
    }

    val computerScore = if (result == GameResult.LOSE) {
        state.computerScore + 1
    } else {
        state.computerScore
    }

    return RpsUiState(
        round = state.round + 1,

        userScore = userScore,
        computerScore = computerScore,

        user = userChoice,
        computer = computer,

        result = result
    )
}

@Composable
private fun RandomRpsApp() {
    var state by remember { mutableStateOf(RpsUiState()) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "사용자 ${state.userScore} 점",
                color = Color.Black,
                fontSize = 24.sp
            )
            Text(
                text = "컴퓨터 ${state.computerScore} 점",
                color = Color.Black,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "컴퓨터의 선택: ${state.computer?.title ?: "선택 없음"}",
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(
                text = "당신의 선택: ${state.user?.title ?: "선택 없음"}",
                fontSize = 20.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = state.result.title,
                color = Color.Black,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    10.dp,
                    Alignment.CenterHorizontally
                )
            ) {
                RpsButton("가위") { state = playRound(state, Hand.SCISSORS) }
                RpsButton("바위") { state = playRound(state, Hand.ROCK) }
                RpsButton("보") { state = playRound(state, Hand.PAPER) }
            }
        }
    }
}

@Composable
private fun RpsButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp, 40.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(Color.Black)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        RandomRpsApp()
    }
}