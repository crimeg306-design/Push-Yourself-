package com.example.data.model

data class PresetTask(
    val id: String,
    val name: String,
    val description: String,
    val xpReward: Int = 50,
    val category: String = "MENTAL"
)

data class PresetPack(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val borderColorHex: String,
    val totalXp: Int = 250,
    val tasks: List<PresetTask>
)

data class NotificationSettings(
    val muteReset0000: Boolean = false,
    val mute0400: Boolean = false,
    val mute0800: Boolean = false,
    val mute1200: Boolean = false,
    val mute1800: Boolean = false,
    val mute2300: Boolean = false,
    val mute2345: Boolean = false,
    val soundAndHapticsEnabled: Boolean = true
)
