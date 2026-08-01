package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AchievementEntity
import com.example.data.model.DailyLogEntity
import com.example.data.model.DailyQuestEntity
import com.example.data.model.LeaderboardEntryEntity
import com.example.data.model.QuestTemplateEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserStatsEntity

@Database(
    entities = [
        UserEntity::class,
        UserStatsEntity::class,
        QuestTemplateEntity::class,
        DailyQuestEntity::class,
        DailyLogEntity::class,
        AchievementEntity::class,
        LeaderboardEntryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userStatsDao(): UserStatsDao
    abstract fun questTemplateDao(): QuestTemplateDao
    abstract fun dailyQuestDao(): DailyQuestDao
    abstract fun dailyLogDao(): DailyLogDao
    abstract fun achievementDao(): AchievementDao
    abstract fun leaderboardDao(): LeaderboardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "push_yourself.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
