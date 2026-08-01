package com.example.utils

import kotlin.math.pow
import kotlin.math.roundToInt

object XpCalculator {

    /**
     * Calculate XP required for next level using formula:
     * xp_to_next_level = 50 * 1.4^(level - 1), rounded to nearest 10
     */
    fun calculateXpToNextLevel(level: Int): Int {
        if (level < 1) return 50
        val raw = 50.0 * 1.4.pow((level - 1).toDouble())
        val roundedToNearest10 = ((raw / 10.0).roundToInt()) * 10
        return if (roundedToNearest10 < 50) 50 else roundedToNearest10
    }

    /**
     * Calculate proportional XP earned based on current vs target progress.
     */
    fun calculateProportionalXp(current: Int, target: Int, rewardXp: Int): Int {
        if (target <= 0) return rewardXp
        val ratio = (current.coerceAtMost(target).toFloat() / target.toFloat()).coerceIn(0f, 1f)
        return (ratio * rewardXp).roundToInt()
    }

    /**
     * Calculate proportional stat boost earned based on current vs target progress.
     */
    fun calculateProportionalStatBoost(current: Int, target: Int, statBoost: Int): Int {
        if (target <= 0) return statBoost
        val ratio = (current.coerceAtMost(target).toFloat() / target.toFloat()).coerceIn(0f, 1f)
        return (ratio * statBoost).roundToInt()
    }
}
