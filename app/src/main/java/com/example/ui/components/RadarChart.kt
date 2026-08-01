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
import com.example.data.model.UserStatsEntity
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BgCard
import com.example.ui.theme.BgElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarChart(
    stats: UserStatsEntity?,
    modifier: Modifier = Modifier
) {
    val s = stats ?: UserStatsEntity()
    val values = listOf(
        s.strength.coerceIn(0, 100),
        s.core.coerceIn(0, 100),
        s.legs.coerceIn(0, 100),
        s.agility.coerceIn(0, 100),
        s.endurance.coerceIn(0, 100),
        s.vitality.coerceIn(0, 100)
    )

    val labels = listOf("STR", "COR", "LEG", "AGI", "END", "VIT")

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
                text = "BIOMETRIC RADAR SNAPSHOT",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = (minOf(size.width, size.height) / 2f) - 40.dp.toPx()
                val numAxes = 6

                // Draw concentric webs (25%, 50%, 75%, 100%)
                for (level in 1..4) {
                    val webRadius = radius * (level / 4f)
                    val webPath = Path()
                    for (i in 0 until numAxes) {
                        val angle = (Math.PI / 3) * i - (Math.PI / 2)
                        val x = center.x + webRadius * cos(angle).toFloat()
                        val y = center.y + webRadius * sin(angle).toFloat()
                        if (i == 0) webPath.moveTo(x, y) else webPath.lineTo(x, y)
                    }
                    webPath.close()
                    drawPath(
                        path = webPath,
                        color = BgElevated,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                // Draw radial axis lines & labels
                for (i in 0 until numAxes) {
                    val angle = (Math.PI / 3) * i - (Math.PI / 2)
                    val x = center.x + radius * cos(angle).toFloat()
                    val y = center.y + radius * sin(angle).toFloat()

                    drawLine(
                        color = BgElevated,
                        start = center,
                        end = Offset(x, y),
                        strokeWidth = 1.dp.toPx()
                    )

                    // Draw stat labels
                    val labelRadius = radius + 22.dp.toPx()
                    val lx = center.x + labelRadius * cos(angle).toFloat()
                    val ly = center.y + labelRadius * sin(angle).toFloat()

                    val textPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 28f
                        isAntiAlias = true
                        typeface = android.graphics.Typeface.MONOSPACE
                        textAlign = android.graphics.Paint.Align.CENTER
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        "${labels[i]} ${values[i]}",
                        lx,
                        ly + 8f,
                        textPaint
                    )
                }

                // Draw filled polygon for user stats
                val polyPath = Path()
                for (i in 0 until numAxes) {
                    val angle = (Math.PI / 3) * i - (Math.PI / 2)
                    val statRatio = values[i] / 100f
                    val r = radius * statRatio
                    val px = center.x + r * cos(angle).toFloat()
                    val py = center.y + r * sin(angle).toFloat()

                    if (i == 0) polyPath.moveTo(px, py) else polyPath.lineTo(px, py)
                }
                polyPath.close()

                drawPath(
                    path = polyPath,
                    color = AccentCyan.copy(alpha = 0.35f)
                )

                drawPath(
                    path = polyPath,
                    color = AccentCyan,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Draw points on vertices
                for (i in 0 until numAxes) {
                    val angle = (Math.PI / 3) * i - (Math.PI / 2)
                    val statRatio = values[i] / 100f
                    val r = radius * statRatio
                    val px = center.x + r * cos(angle).toFloat()
                    val py = center.y + r * sin(angle).toFloat()

                    drawCircle(
                        color = AccentGold,
                        radius = 4.dp.toPx(),
                        center = Offset(px, py)
                    )
                }
            }
        }
    }
}
