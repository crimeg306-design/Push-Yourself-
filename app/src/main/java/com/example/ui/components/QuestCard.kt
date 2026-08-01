package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyQuestEntity
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.BgCard
import com.example.ui.theme.BgElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.utils.StatCalculator

@Composable
fun QuestCard(
    quest: DailyQuestEntity,
    onProgressChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val statColorHex = StatCalculator.getStatColorHex(quest.category)
    val statColor = try {
        Color(android.graphics.Color.parseColor(statColorHex))
    } catch (_: Exception) {
        AccentCyan
    }

    val is100 = quest.currentValue >= quest.targetValue

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BgCard)
            .border(
                1.dp,
                if (is100) AccentGreen else BgElevated,
                RoundedCornerShape(10.dp)
            )
            .padding(14.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(statColor)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = quest.category,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = quest.name,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                }

                // Completion status checkbox / badge
                if (is100) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AccentGreen)
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.Black,
                            modifier = Modifier.width(14.dp).height(14.dp)
                        )
                    }
                } else {
                    Text(
                        text = "${quest.currentValue}/${quest.targetValue} ${quest.unit}",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Proportional XP and Stat reward preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "REWARD: +${quest.xpEarned}/${quest.xpReward} XP",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = AccentCyan
                )

                Text(
                    text = "${quest.category} +${quest.statBoostApplied}/${quest.statBoost}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = statColor
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Interactive Drag-Slider for proportional progress
            Slider(
                value = quest.currentValue.toFloat(),
                onValueChange = { newValue ->
                    onProgressChange(newValue.toInt())
                },
                valueRange = 0f..quest.targetValue.toFloat(),
                steps = if (quest.targetValue > 1) quest.targetValue - 1 else 0,
                colors = SliderDefaults.colors(
                    thumbColor = if (is100) AccentGreen else statColor,
                    activeTrackColor = if (is100) AccentGreen else statColor,
                    inactiveTrackColor = BgElevated
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
