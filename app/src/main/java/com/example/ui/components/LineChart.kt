package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyLogEntity
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BgCard
import com.example.ui.theme.BgElevated
import com.example.ui.theme.TextMuted

@Composable
fun LineChart(
    logs: List<DailyLogEntity>,
    modifier: Modifier = Modifier
) {
    val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
    // Use earned XP or fallback mock progression if new user
    val xpValues = if (logs.isNotEmpty()) {
        logs.take(7).map { it.xpEarnedToday }.reversed()
    } else {
        listOf(30, 50, 80, 110, 140, 180, 220)
    }

    val maxVal = (xpValues.maxOrNull() ?: 100).coerceAtLeast(100)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BgCard)
            .border(1.dp, BgElevated, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "COGNITIVE OUTPUT TREND (7-DAY XP)",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val paddingLeft = 30.dp.toPx()
                val paddingBottom = 30.dp.toPx()
                val width = size.width - paddingLeft - 20.dp.toPx()
                val height = size.height - paddingBottom - 20.dp.toPx()

                // Draw horizontal grid lines
                for (i in 0..3) {
                    val y = height - (height * (i / 3f)) + 10.dp.toPx()
                    drawLine(
                        color = BgElevated,
                        start = Offset(paddingLeft, y),
                        end = Offset(paddingLeft + width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Plot trend line
                val points = mutableListOf<Offset>()
                val stepX = width / (days.size - 1)

                for (i in days.indices) {
                    val valXp = if (i < xpValues.size) xpValues[i] else 0
                    val ratio = valXp.toFloat() / maxVal.toFloat()
                    val x = paddingLeft + (i * stepX)
                    val y = height - (height * ratio) + 10.dp.toPx()
                    points.add(Offset(x, y))

                    // Draw Day Labels
                    val textPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.parseColor("#8B8B9E")
                        textSize = 24f
                        isAntiAlias = true
                        typeface = android.graphics.Typeface.MONOSPACE
                        textAlign = android.graphics.Paint.Align.CENTER
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        days[i],
                        x,
                        size.height - 5.dp.toPx(),
                        textPaint
                    )
                }

                val linePath = Path()
                for (i in points.indices) {
                    if (i == 0) linePath.moveTo(points[i].x, points[i].y)
                    else linePath.lineTo(points[i].x, points[i].y)
                }

                drawPath(
                    path = linePath,
                    color = AccentCyan,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Draw points
                for (pt in points) {
                    drawCircle(
                        color = AccentGold,
                        radius = 4.dp.toPx(),
                        center = pt
                    )
                }
            }
        }
    }
}
