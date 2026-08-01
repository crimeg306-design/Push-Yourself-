package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CountdownTimer
import com.example.ui.components.HistoryCalendarModal
import com.example.ui.components.ProfileCard
import com.example.ui.components.QuestCard
import com.example.ui.components.StatsGrid
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BgCard
import com.example.ui.theme.BgElevated
import com.example.ui.theme.BgPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.user.collectAsState()
    val stats by viewModel.userStats.collectAsState()
    val quests by viewModel.todayQuests.collectAsState()
    val dailyLogs by viewModel.dailyLogs.collectAsState()
    val timeToReset by viewModel.timeToReset.collectAsState()

    var showHistoryCalendar by remember { mutableStateOf(false) }

    val todayQuote = dailyLogs.firstOrNull()?.quoteOfDay ?: "Discipline is the bridge between goals and results."

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BgPrimary)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Profile Card
        item {
            ProfileCard(user = user)
        }

        // Daily Quote Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(BgCard)
                    .border(1.dp, BgElevated, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "DAILY BRIEFING / QUOTE",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = AccentGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"$todayQuote\"",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Countdown Timer & Streak Counter
        item {
            CountdownTimer(timeToReset = timeToReset, user = user)
        }

        // History Calendar Launch Button
        item {
            Button(
                onClick = { showHistoryCalendar = true },
                colors = ButtonDefaults.buttonColors(containerColor = BgElevated),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "📅 VIEW QUEST HISTORY CALENDAR",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = AccentCyan
                )
            }
        }

        // Warrior Stats Grid
        item {
            StatsGrid(stats = stats)
        }

        // Daily Training Log Section Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "DAILY TRAINING LOG (6 QUESTS)",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = TextMuted
                )

                val completedCount = quests.count { it.isCompleted }
                Text(
                    text = "$completedCount/6 COMPLETED",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (completedCount == 6) AccentCyan else TextSecondary
                )
            }
        }

        // List of 6 Draggable Quest Cards
        items(quests, key = { it.id }) { quest ->
            QuestCard(
                quest = quest,
                onProgressChange = { newProgress ->
                    viewModel.updateQuestProgress(quest, newProgress)
                }
            )
        }
    }

    if (showHistoryCalendar) {
        HistoryCalendarModal(
            dailyLogs = dailyLogs,
            onDismiss = { showHistoryCalendar = false }
        )
    }
}
