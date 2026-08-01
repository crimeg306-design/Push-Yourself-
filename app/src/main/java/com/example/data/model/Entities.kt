package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: String = "local_user",
    val callsign: String = "",
    val avatarSeed: String = "ALPHA_1",
    val level: Int = 1,
    val totalXp: Int = 0,
    val xpToNextLevel: Int = 50,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActiveDate: String = "",
    val streakFreezesAvailable: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val userId: String = "local_user",
    val strength: Int = 10,
    val core: Int = 10,
    val legs: Int = 10,
    val agility: Int = 10,
    val endurance: Int = 10,
    val vitality: Int = 10
)

@Entity(tableName = "quest_template")
data class QuestTemplateEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // STR, COR, LEG, AGI, END, VIT, LIFESTYLE, MENTAL
    val statBoost: Int,
    val xpReward: Int,
    val targetValue: Int,
    val unit: String,
    val icon: String,
    val difficultyTier: Int = 1,
    val isCustom: Boolean = false
)

@Entity(tableName = "daily_quest")
data class DailyQuestEntity(
    @PrimaryKey val id: String,
    val userId: String = "local_user",
    val questTemplateId: String,
    val name: String,
    val category: String,
    val date: String, // YYYY-MM-DD
    val currentValue: Int,
    val targetValue: Int,
    val unit: String,
    val xpReward: Int,
    val statBoost: Int,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val xpEarned: Int = 0,
    val statBoostApplied: Int = 0
)

@Entity(tableName = "daily_log")
data class DailyLogEntity(
    @PrimaryKey val id: String,
    val userId: String = "local_user",
    val date: String, // YYYY-MM-DD
    val quoteOfDay: String,
    val questsCompleted: Int,
    val totalQuests: Int,
    val xpEarnedToday: Int,
    val statsSnapshotJson: String, // JSON string of STR,COR,LEG,AGI,END,VIT
    val streakActive: Boolean
)

@Entity(tableName = "achievement")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val unlockCondition: String,
    val unlockedAt: Long? = null
)

@Entity(tableName = "leaderboard_entry")
data class LeaderboardEntryEntity(
    @PrimaryKey val userId: String,
    val callsign: String,
    val level: Int,
    val totalXp: Int,
    val rankPosition: Int,
    val rankTitle: String,
    val isUser: Boolean = false
)
