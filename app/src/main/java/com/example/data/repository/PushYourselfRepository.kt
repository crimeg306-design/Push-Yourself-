package com.example.data.repository

import android.content.Context
import com.example.data.db.AppDatabase
import com.example.data.model.AchievementEntity
import com.example.data.model.DailyLogEntity
import com.example.data.model.DailyQuestEntity
import com.example.data.model.LeaderboardEntryEntity
import com.example.data.model.PresetTask
import com.example.data.model.QuestTemplateEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserStatsEntity
import com.example.utils.HapticSoundHelper
import com.example.utils.NotificationHelper
import com.example.utils.PresetData
import com.example.utils.StatCalculator
import com.example.utils.XpCalculator
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PushYourselfRepository(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val userDao = db.userDao()
    private val statsDao = db.userStatsDao()
    private val templateDao = db.questTemplateDao()
    private val dailyQuestDao = db.dailyQuestDao()
    private val dailyLogDao = db.dailyLogDao()
    private val achievementDao = db.achievementDao()
    private val leaderboardDao = db.leaderboardDao()

    val notificationHelper = NotificationHelper(context)
    val hapticHelper = HapticSoundHelper(context)

    fun getUser(): Flow<UserEntity?> = userDao.getUser()
    fun getUserStats(): Flow<UserStatsEntity?> = statsDao.getUserStats()
    fun getTodayQuests(date: String): Flow<List<DailyQuestEntity>> = dailyQuestDao.getQuestsForDate(date)
    fun getAllDailyLogs(): Flow<List<DailyLogEntity>> = dailyLogDao.getAllLogs()
    fun getAllAchievements(): Flow<List<AchievementEntity>> = achievementDao.getAllAchievements()
    fun getLeaderboard(): Flow<List<LeaderboardEntryEntity>> = leaderboardDao.getLeaderboard()

    fun getTodayString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    suspend fun ensureInitialized() {
        val today = getTodayString()

        // 1. Initialize user if missing
        var user = userDao.getUserOnce()
        if (user == null) {
            user = UserEntity(
                id = "local_user",
                callsign = "",
                level = 1,
                totalXp = 0,
                xpToNextLevel = 50,
                currentStreak = 0,
                longestStreak = 0,
                lastActiveDate = today,
                streakFreezesAvailable = 1
            )
            userDao.insertOrUpdateUser(user)
        }

        // 2. Initialize stats if missing
        var stats = statsDao.getUserStatsOnce()
        if (stats == null) {
            stats = UserStatsEntity("local_user", 10, 10, 10, 10, 10, 10)
            statsDao.insertOrUpdateStats(stats)
        }

        // 3. Initialize quest templates if missing
        val templates = templateDao.getAllTemplatesOnce()
        if (templates.isEmpty()) {
            templateDao.insertAll(PresetData.defaultQuestTemplates)
        }

        // 4. Initialize achievements if missing
        achievementDao.insertAll(PresetData.defaultAchievements)

        // 5. Initialize leaderboard if missing
        leaderboardDao.insertAll(PresetData.initialLeaderboard)

        // Ensure user entry is in leaderboard
        updateLeaderboardUserEntry(user)

        // 6. Handle daily reset logic
        if (user.lastActiveDate != today) {
            handleDailyReset(user, today)
        } else {
            // Ensure today's quests exist
            val todayQuests = dailyQuestDao.getQuestsForDateOnce(today)
            if (todayQuests.isEmpty()) {
                generateQuestsForDate(today)
            }
        }
    }

    private suspend fun handleDailyReset(currentUser: UserEntity, today: String) {
        val prevDate = currentUser.lastActiveDate
        if (prevDate.isNotEmpty()) {
            val prevQuests = dailyQuestDao.getQuestsForDateOnce(prevDate)
            val allCompleted = prevQuests.isNotEmpty() && prevQuests.all { it.isCompleted }

            var newStreak = currentUser.currentStreak
            var newLevel = currentUser.level
            var newXpToNext = currentUser.xpToNextLevel
            var freezes = currentUser.streakFreezesAvailable

            if (allCompleted) {
                newStreak += 1
            } else {
                // Streak broken check
                if (freezes > 0) {
                    freezes -= 1
                    notificationHelper.showEventNotification(
                        101,
                        "🛡️ STREAK PROTECTED",
                        "Streak Freeze automatically used. Day protected!"
                    )
                } else {
                    newStreak = 0
                    newLevel = 1
                    newXpToNext = 50
                    notificationHelper.showEventNotification(
                        102,
                        "⚠️ PROTOCOL BROKEN",
                        "Streak broken. Level reset to 1. Rebuild your warrior stats!"
                    )
                }
            }

            val newLongest = maxOf(currentUser.longestStreak, newStreak)

            val updatedUser = currentUser.copy(
                level = newLevel,
                xpToNextLevel = newXpToNext,
                currentStreak = newStreak,
                longestStreak = newLongest,
                lastActiveDate = today,
                streakFreezesAvailable = freezes
            )
            userDao.insertOrUpdateUser(updatedUser)
        } else {
            userDao.insertOrUpdateUser(currentUser.copy(lastActiveDate = today))
        }

        generateQuestsForDate(today)
    }

    private suspend fun generateQuestsForDate(date: String) {
        val templates = templateDao.getAllTemplatesOnce().ifEmpty { PresetData.defaultQuestTemplates }
        val categories = listOf("STR", "COR", "LEG", "AGI", "END", "VIT")
        val dailyQuests = mutableListOf<DailyQuestEntity>()

        for (cat in categories) {
            val catTemplates = templates.filter { it.category.equals(cat, ignoreCase = true) }
            val template = catTemplates.randomOrNull() ?: templates.random()
            dailyQuests.add(
                DailyQuestEntity(
                    id = UUID.randomUUID().toString(),
                    userId = "local_user",
                    questTemplateId = template.id,
                    name = template.name,
                    category = template.category,
                    date = date,
                    currentValue = 0,
                    targetValue = template.targetValue,
                    unit = template.unit,
                    xpReward = template.xpReward,
                    statBoost = template.statBoost
                )
            )
        }

        dailyQuestDao.insertQuests(dailyQuests)

        // Save daily log
        val randomQuote = PresetData.dailyQuotes.random()
        val stats = statsDao.getUserStatsOnce() ?: UserStatsEntity()
        val statsJson = "{\"STR\":${stats.strength},\"COR\":${stats.core},\"LEG\":${stats.legs},\"AGI\":${stats.agility},\"END\":${stats.endurance},\"VIT\":${stats.vitality}}"

        val dailyLog = DailyLogEntity(
            id = UUID.randomUUID().toString(),
            userId = "local_user",
            date = date,
            quoteOfDay = randomQuote,
            questsCompleted = 0,
            totalQuests = dailyQuests.size,
            xpEarnedToday = 0,
            statsSnapshotJson = statsJson,
            streakActive = true
        )
        dailyLogDao.insertLog(dailyLog)
    }

    suspend fun setCallsign(callsign: String) {
        val user = userDao.getUserOnce() ?: return
        val updated = user.copy(callsign = callsign.trim())
        userDao.insertOrUpdateUser(updated)
        updateLeaderboardUserEntry(updated)
    }

    suspend fun updateQuestProgress(quest: DailyQuestEntity, newProgress: Int) {
        val clampedProgress = newProgress.coerceIn(0, quest.targetValue)
        val wasCompleted = quest.isCompleted
        val isNowCompleted = clampedProgress >= quest.targetValue

        val previousXpEarned = quest.xpEarned
        val previousStatBoost = quest.statBoostApplied

        val newXpEarned = XpCalculator.calculateProportionalXp(clampedProgress, quest.targetValue, quest.xpReward)
        val newStatBoost = XpCalculator.calculateProportionalStatBoost(clampedProgress, quest.targetValue, quest.statBoost)

        val deltaXp = newXpEarned - previousXpEarned
        val deltaStat = newStatBoost - previousStatBoost

        val updatedQuest = quest.copy(
            currentValue = clampedProgress,
            isCompleted = isNowCompleted,
            completedAt = if (isNowCompleted) System.currentTimeMillis() else quest.completedAt,
            xpEarned = newXpEarned,
            statBoostApplied = newStatBoost
        )

        dailyQuestDao.updateQuest(updatedQuest)

        // Apply XP gain & stat gains to user
        if (deltaXp != 0 || deltaStat != 0) {
            awardXpAndStats(deltaXp, quest.category, deltaStat)
        }

        if (!wasCompleted && isNowCompleted) {
            hapticHelper.playQuestComplete()
            notificationHelper.showEventNotification(
                201,
                "✅ QUEST COMPLETED!",
                "${quest.name} finished! +${quest.xpReward} XP — ${quest.category} +${quest.statBoost}"
            )
            checkAchievements()
        } else {
            hapticHelper.playClick()
        }
    }

    suspend fun completePresetTask(task: PresetTask) {
        awardXpAndStats(task.xpReward, task.category, 5)
        hapticHelper.playQuestComplete()
        notificationHelper.showEventNotification(
            202,
            "🎯 PRESET TASK COMPLETE",
            "${task.name} completed! +${task.xpReward} XP"
        )
        checkAchievements()
    }

    suspend fun createCustomQuest(name: String, category: String, targetValue: Int, unit: String, xpReward: Int) {
        val cappedXp = xpReward.coerceIn(5, 50)
        val validTarget = if (targetValue <= 0) 1 else targetValue
        val validUnit = if (unit.isBlank()) "reps" else unit.trim()

        val template = QuestTemplateEntity(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            category = category.uppercase(),
            statBoost = 5,
            xpReward = cappedXp,
            targetValue = validTarget,
            unit = validUnit,
            icon = "⚡",
            isCustom = true
        )

        templateDao.insertTemplate(template)

        // Add to today's active daily quests
        val today = getTodayString()
        val dailyQuest = DailyQuestEntity(
            id = UUID.randomUUID().toString(),
            userId = "local_user",
            questTemplateId = template.id,
            name = template.name,
            category = template.category,
            date = today,
            currentValue = 0,
            targetValue = template.targetValue,
            unit = template.unit,
            xpReward = template.xpReward,
            statBoost = template.statBoost
        )

        dailyQuestDao.insertQuest(dailyQuest)
        hapticHelper.playClick()
    }

    private suspend fun awardXpAndStats(xpGain: Int, statCategory: String, statGain: Int) {
        val user = userDao.getUserOnce() ?: return
        var currentLevel = user.level
        var currentTotalXp = user.totalXp + xpGain
        var currentXpThreshold = user.xpToNextLevel

        // Level up loop
        var leveledUp = false
        while (currentTotalXp >= currentXpThreshold) {
            currentLevel += 1
            currentXpThreshold += XpCalculator.calculateXpToNextLevel(currentLevel)
            leveledUp = true
        }

        val updatedUser = user.copy(
            level = currentLevel,
            totalXp = currentTotalXp,
            xpToNextLevel = currentXpThreshold
        )
        userDao.insertOrUpdateUser(updatedUser)
        updateLeaderboardUserEntry(updatedUser)

        if (leveledUp) {
            hapticHelper.playLevelUp()
            val rankTitle = StatCalculator.getRankTitle(currentLevel)
            notificationHelper.showEventNotification(
                301,
                "👑 LEVEL UP!",
                "You reached Level $currentLevel ($rankTitle)! Warrior stats enhanced."
            )
        }

        // Award Stats
        if (statGain != 0) {
            val stats = statsDao.getUserStatsOnce() ?: UserStatsEntity()
            val updatedStats = when (statCategory.uppercase()) {
                "STR" -> stats.copy(strength = (stats.strength + statGain).coerceAtLeast(1))
                "COR" -> stats.copy(core = (stats.core + statGain).coerceAtLeast(1))
                "LEG" -> stats.copy(legs = (stats.legs + statGain).coerceAtLeast(1))
                "AGI" -> stats.copy(agility = (stats.agility + statGain).coerceAtLeast(1))
                "END" -> stats.copy(endurance = (stats.endurance + statGain).coerceAtLeast(1))
                "VIT" -> stats.copy(vitality = (stats.vitality + statGain).coerceAtLeast(1))
                else -> stats.copy(vitality = (stats.vitality + statGain).coerceAtLeast(1))
            }
            statsDao.insertOrUpdateStats(updatedStats)
        }
    }

    private suspend fun updateLeaderboardUserEntry(user: UserEntity) {
        val rankTitle = StatCalculator.getRankTitle(user.level)
        val entry = LeaderboardEntryEntity(
            userId = "local_user",
            callsign = if (user.callsign.isNotBlank()) user.callsign else "RECRUIT_YOU",
            level = user.level,
            totalXp = user.totalXp,
            rankPosition = 1,
            rankTitle = rankTitle,
            isUser = true
        )
        leaderboardDao.insertAll(listOf(entry))
    }

    private suspend fun checkAchievements() {
        val user = userDao.getUserOnce() ?: return
        val stats = statsDao.getUserStatsOnce() ?: return

        val now = System.currentTimeMillis()

        if (user.level >= 10) {
            achievementDao.updateAchievement(
                AchievementEntity("ach_4", "Vanguard Rising", "Reach Level 10 Corporal rank.", "👑", "LEVEL_10", now)
            )
        }
        if (user.currentStreak >= 7) {
            achievementDao.updateAchievement(
                AchievementEntity("ach_2", "Iron Will", "Maintain a 7-day streak.", "🔥", "STREAK_7", now)
            )
        }
        if (user.currentStreak >= 30) {
            achievementDao.updateAchievement(
                AchievementEntity("ach_3", "Unstoppable", "Maintain a 30-day streak.", "⚡", "STREAK_30", now)
            )
        }
        if (stats.strength >= 50 || stats.core >= 50 || stats.legs >= 50 || stats.agility >= 50 || stats.endurance >= 50 || stats.vitality >= 50) {
            achievementDao.updateAchievement(
                AchievementEntity("ach_6", "Warrior Core", "Reach 50+ in any single Warrior Stat.", "🛡️", "STAT_50", now)
            )
        }
    }

    suspend fun exportDataJson(): String {
        val user = userDao.getUserOnce()
        val stats = statsDao.getUserStatsOnce()
        val root = JSONObject()

        if (user != null) {
            val uObj = JSONObject()
            uObj.put("callsign", user.callsign)
            uObj.put("level", user.level)
            uObj.put("totalXp", user.totalXp)
            uObj.put("currentStreak", user.currentStreak)
            uObj.put("longestStreak", user.longestStreak)
            root.put("user", uObj)
        }

        if (stats != null) {
            val sObj = JSONObject()
            sObj.put("strength", stats.strength)
            sObj.put("core", stats.core)
            sObj.put("legs", stats.legs)
            sObj.put("agility", stats.agility)
            sObj.put("endurance", stats.endurance)
            sObj.put("vitality", stats.vitality)
            root.put("stats", sObj)
        }

        return root.toString(2)
    }

    suspend fun importDataJson(jsonStr: String): Boolean {
        return try {
            val root = JSONObject(jsonStr)
            if (root.has("user")) {
                val uObj = root.getJSONObject("user")
                val existing = userDao.getUserOnce() ?: UserEntity()
                val importedUser = existing.copy(
                    callsign = uObj.optString("callsign", existing.callsign),
                    level = uObj.optInt("level", existing.level),
                    totalXp = uObj.optInt("totalXp", existing.totalXp),
                    currentStreak = uObj.optInt("currentStreak", existing.currentStreak),
                    longestStreak = uObj.optInt("longestStreak", existing.longestStreak)
                )
                userDao.insertOrUpdateUser(importedUser)
            }
            if (root.has("stats")) {
                val sObj = root.getJSONObject("stats")
                val importedStats = UserStatsEntity(
                    userId = "local_user",
                    strength = sObj.optInt("strength", 10),
                    core = sObj.optInt("core", 10),
                    legs = sObj.optInt("legs", 10),
                    agility = sObj.optInt("agility", 10),
                    endurance = sObj.optInt("endurance", 10),
                    vitality = sObj.optInt("vitality", 10)
                )
                statsDao.insertOrUpdateStats(importedStats)
            }
            true
        } catch (_: Exception) {
            false
        }
    }
}
