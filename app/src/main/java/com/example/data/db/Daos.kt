package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AchievementEntity
import com.example.data.model.DailyLogEntity
import com.example.data.model.DailyQuestEntity
import com.example.data.model.LeaderboardEntryEntity
import com.example.data.model.QuestTemplateEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    fun getUser(userId: String = "local_user"): Flow<UserEntity?>

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    suspend fun getUserOnce(userId: String = "local_user"): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)
}

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE userId = :userId LIMIT 1")
    fun getUserStats(userId: String = "local_user"): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE userId = :userId LIMIT 1")
    suspend fun getUserStatsOnce(userId: String = "local_user"): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStats(stats: UserStatsEntity)
}

@Dao
interface QuestTemplateDao {
    @Query("SELECT * FROM quest_template")
    fun getAllTemplates(): Flow<List<QuestTemplateEntity>>

    @Query("SELECT * FROM quest_template")
    suspend fun getAllTemplatesOnce(): List<QuestTemplateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: QuestTemplateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(templates: List<QuestTemplateEntity>)
}

@Dao
interface DailyQuestDao {
    @Query("SELECT * FROM daily_quest WHERE date = :date AND userId = :userId")
    fun getQuestsForDate(date: String, userId: String = "local_user"): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quest WHERE date = :date AND userId = :userId")
    suspend fun getQuestsForDateOnce(date: String, userId: String = "local_user"): List<DailyQuestEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuests(quests: List<DailyQuestEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: DailyQuestEntity)

    @Update
    suspend fun updateQuest(quest: DailyQuestEntity)

    @Query("DELETE FROM daily_quest WHERE date = :date AND userId = :userId")
    suspend fun deleteForDate(date: String, userId: String = "local_user")
}

@Dao
interface DailyLogDao {
    @Query("SELECT * FROM daily_log WHERE userId = :userId ORDER BY date DESC")
    fun getAllLogs(userId: String = "local_user"): Flow<List<DailyLogEntity>>

    @Query("SELECT * FROM daily_log WHERE date = :date AND userId = :userId LIMIT 1")
    suspend fun getLogForDate(date: String, userId: String = "local_user"): DailyLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: DailyLogEntity)
}

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievement")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(achievements: List<AchievementEntity>)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)
}

@Dao
interface LeaderboardDao {
    @Query("SELECT * FROM leaderboard_entry ORDER BY totalXp DESC, level DESC")
    fun getLeaderboard(): Flow<List<LeaderboardEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<LeaderboardEntryEntity>)

    @Query("DELETE FROM leaderboard_entry")
    suspend fun clear()
}
