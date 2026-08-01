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
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.model.UserStatsEntity
import com.example.ui.theme.BgCard
import com.example.ui.theme.BgElevated
import com.example.ui.theme.StatAGI
import com.example.ui.theme.StatCOR
import com.example.ui.theme.StatEND
import com.example.ui.theme.StatLEG
import com.example.ui.theme.StatSTR
import com.example.ui.theme.StatVIT
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun StatsGrid(
    stats: UserStatsEntity?,
    modifier: Modifier = Modifier
) {
    val s = stats ?: UserStatsEntity()

    val statItems = listOf(
        StatItem("STR", "STRENGTH", s.strength, StatSTR),
        StatItem("COR", "CORE", s.core, StatCOR),
        StatItem("LEG", "LEGS", s.legs, StatLEG),
        StatItem("AGI", "AGILITY", s.agility, StatAGI),
        StatItem("END", "ENDURANCE", s.endurance, StatEND),
        StatItem("VIT", "VITALITY", s.vitality, StatVIT)
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "WARRIOR STATS SNAPSHOT",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 2 Column Grid
        for (i in statItems.indices step 2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(item = statItems[i], modifier = Modifier.weight(1f))
                if (i + 1 < statItems.size) {
                    StatCard(item = statItems[i + 1], modifier = Modifier.weight(1f))
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

private data class StatItem(
    val code: String,
    val name: String,
    val value: Int,
    val color: Color
)

@Composable
private fun StatCard(
    item: StatItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(BgCard)
            .border(1.dp, BgElevated, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(item.color)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.code,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.name,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                Text(
                    text = "${item.value}",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Cap stat progress meter at 100
            val progress = (item.value.toFloat() / 100f).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = item.color,
                trackColor = BgElevated
            )
        }
    }
}
