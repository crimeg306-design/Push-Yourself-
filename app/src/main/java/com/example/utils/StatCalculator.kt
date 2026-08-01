package com.example.utils

object StatCalculator {

    fun getRankTitle(level: Int): String {
        return when (level) {
            in 1..4 -> "Recruit"
            in 5..9 -> "Private"
            in 10..14 -> "Corporal"
            in 15..19 -> "Sergeant"
            in 20..29 -> "Lieutenant"
            in 30..39 -> "Captain"
            in 40..49 -> "Major"
            else -> "Vanguard"
        }
    }

    fun getStatColorHex(category: String): String {
        return when (category.uppercase()) {
            "STR" -> "#FF4444"
            "COR" -> "#FFA500"
            "LEG" -> "#00C853"
            "AGI" -> "#00B0FF"
            "END" -> "#AA00FF"
            "VIT" -> "#00F5D4"
            else -> "#00F5D4"
        }
    }

    fun getStatFullName(category: String): String {
        return when (category.uppercase()) {
            "STR" -> "STRENGTH"
            "COR" -> "CORE"
            "LEG" -> "LEGS"
            "AGI" -> "AGILITY"
            "END" -> "ENDURANCE"
            "VIT" -> "VITALITY"
            else -> category.uppercase()
        }
    }
}
