package com.example.utils

import com.example.data.model.AchievementEntity
import com.example.data.model.LeaderboardEntryEntity
import com.example.data.model.PresetPack
import com.example.data.model.PresetTask
import com.example.data.model.QuestTemplateEntity

object PresetData {

    val presetPacks = listOf(
        PresetPack(
            id = "preset_discipline",
            name = "Discipline Mastery",
            description = "Habit and willpower protocols designed for iron focus.",
            icon = "⚡",
            borderColorHex = "#FFD700",
            tasks = listOf(
                PresetTask("disc_1", "Early Wake-up", "Wake up at 6 AM", 50, "VIT"),
                PresetTask("disc_2", "Water Challenge", "Drink 2L of water today", 50, "VIT"),
                PresetTask("disc_3", "Social Media Fast", "No social media for 24 hours", 50, "COR"),
                PresetTask("disc_4", "Daily Journal", "Write in your journal today", 50, "COR"),
                PresetTask("disc_5", "Planning", "Create and follow a daily schedule", 50, "COR")
            )
        ),
        PresetPack(
            id = "preset_focus",
            name = "Focus Training",
            description = "Mental clarity and deep attention drills.",
            icon = "🎯",
            borderColorHex = "#00B0FF",
            tasks = listOf(
                PresetTask("foc_1", "Meditation", "Meditate for 10 minutes", 50, "VIT"),
                PresetTask("foc_2", "Deep Work", "Work without distractions for 1 hour", 50, "COR"),
                PresetTask("foc_3", "Mindful Reading", "Read with full attention for 20 minutes", 50, "COR"),
                PresetTask("foc_4", "Jigsaw Puzzle", "Complete a jigsaw puzzle", 50, "AGI"),
                PresetTask("foc_5", "Drawing Exercise", "Draw a detailed picture for 30 minutes", 50, "AGI")
            )
        ),
        PresetPack(
            id = "preset_mental",
            name = "Mental Challenge",
            description = "Cognitive stimulation and intelligence training.",
            icon = "🧠",
            borderColorHex = "#AA00FF",
            tasks = listOf(
                PresetTask("men_1", "Read a Book", "Read for 30 minutes", 50, "COR"),
                PresetTask("men_2", "Solve Puzzles", "Complete 3 brain teasers", 50, "AGI"),
                PresetTask("men_3", "Learn Something New", "Watch an educational video", 50, "COR"),
                PresetTask("men_4", "Language Practice", "Practice a new language for 20 minutes", 50, "COR"),
                PresetTask("men_5", "Chess Challenge", "Play a game of chess", 50, "AGI")
            )
        ),
        PresetPack(
            id = "preset_agility",
            name = "Agility Training",
            description = "Speed, reflexes, and coordination routines.",
            icon = "🏃",
            borderColorHex = "#00B0FF",
            tasks = listOf(
                PresetTask("agi_1", "Jump Rope", "Jump rope for 5 minutes", 50, "AGI"),
                PresetTask("agi_2", "Ladder Drills", "Complete agility ladder routine", 50, "AGI"),
                PresetTask("agi_3", "Quick Feet", "Do 2 minutes of high knees", 50, "AGI"),
                PresetTask("agi_4", "Cone Weaving", "Complete cone weaving drill", 50, "AGI"),
                PresetTask("agi_5", "Reaction Ball", "Practice with reaction ball for 10 minutes", 50, "AGI")
            )
        ),
        PresetPack(
            id = "preset_endurance",
            name = "Endurance Challenge",
            description = "Stamina, cardio, and systemic aerobic conditioning.",
            icon = "🔥",
            borderColorHex = "#AA00FF",
            tasks = listOf(
                PresetTask("end_1", "Long Run", "Run for 30 minutes at steady pace", 50, "END"),
                PresetTask("end_2", "Swimming", "Swim for 20 minutes", 50, "END"),
                PresetTask("end_3", "Cycling", "Cycle for 45 minutes", 50, "END"),
                PresetTask("end_4", "HIIT Workout", "Complete 20-minute HIIT session", 50, "END"),
                PresetTask("end_5", "Stair Climbing", "Climb stairs for 15 minutes", 50, "END")
            )
        ),
        PresetPack(
            id = "preset_strength",
            name = "Strength Training",
            description = "Power, resistance, and muscular development.",
            icon = "💪",
            borderColorHex = "#FF4444",
            tasks = listOf(
                PresetTask("str_1", "Push-ups", "Complete 20 push-ups", 50, "STR"),
                PresetTask("str_2", "Squats", "Complete 30 squats", 50, "LEG"),
                PresetTask("str_3", "Plank", "Hold plank for 1 minute", 50, "COR"),
                PresetTask("str_4", "Dumbbell Rows", "Complete 3 sets of 12 dumbbell rows", 50, "STR"),
                PresetTask("str_5", "Walking Lunges", "Complete 20 walking lunges", 50, "LEG")
            )
        )
    )

    val defaultQuestTemplates = listOf(
        QuestTemplateEntity("tmpl_str_1", "Heavy Push-ups", "STR", 8, 30, 25, "reps", "💪"),
        QuestTemplateEntity("tmpl_str_2", "Pull-up Protocol", "STR", 10, 40, 10, "reps", "🏋️"),
        QuestTemplateEntity("tmpl_cor_1", "Plank Hold", "COR", 7, 20, 60, "sec", "⏱️"),
        QuestTemplateEntity("tmpl_cor_2", "Ab Crunches", "COR", 6, 25, 30, "reps", "🔥"),
        QuestTemplateEntity("tmpl_leg_1", "Bodyweight Squats", "LEG", 8, 30, 35, "reps", "🦵"),
        QuestTemplateEntity("tmpl_leg_2", "Jump Lunges", "LEG", 9, 35, 20, "reps", "⚡"),
        QuestTemplateEntity("tmpl_agi_1", "Interval Sprinting", "AGI", 8, 30, 5, "sprints", "🏃"),
        QuestTemplateEntity("tmpl_agi_2", "Agility Ladder", "AGI", 7, 25, 3, "sets", "👟"),
        QuestTemplateEntity("tmpl_end_1", "Cardio Run", "END", 10, 45, 15, "min", "🫀"),
        QuestTemplateEntity("tmpl_end_2", "Rowing / Cycling", "END", 9, 35, 20, "min", "🚴"),
        QuestTemplateEntity("tmpl_vit_1", "Hydration Protocol", "VIT", 5, 20, 3, "L", "💧"),
        QuestTemplateEntity("tmpl_vit_2", "Cold Shower / Sleep", "VIT", 6, 25, 8, "hrs", "🌙")
    )

    val dailyQuotes = listOf(
        "Discipline is the bridge between goals and results.",
        "Suffer the pain of discipline or suffer the pain of regret.",
        "Under pressure, you don't rise to the occasion. You sink to the level of your training.",
        "Victory belongs to those who endure past the point of comfort.",
        "The mind is the primary weapon. The body is the delivery system.",
        "Excuses build zero strength. Execute the protocol.",
        "Consistency turns ambition into unstoppable momentum."
    )

    val defaultAchievements = listOf(
        AchievementEntity("ach_1", "First Step", "Complete your first daily quest.", "🎖️", "QUESTS_1"),
        AchievementEntity("ach_2", "Iron Will", "Maintain a 7-day streak.", "🔥", "STREAK_7"),
        AchievementEntity("ach_3", "Unstoppable", "Maintain a 30-day streak.", "⚡", "STREAK_30"),
        AchievementEntity("ach_4", "Vanguard Rising", "Reach Level 10 Corporal rank.", "👑", "LEVEL_10"),
        AchievementEntity("ach_5", "Master Explorer", "Complete tasks across all 6 preset packs.", "🌐", "ALL_PRESETS"),
        AchievementEntity("ach_6", "Warrior Core", "Reach 50+ in any single Warrior Stat.", "🛡️", "STAT_50"),
        AchievementEntity("ach_7", "Comeback", "Protect your streak using a Streak Freeze.", "🛡️", "FREEZE_USED")
    )

    val initialLeaderboard = listOf(
        LeaderboardEntryEntity("user_1", "VANGUARD_ALPHA", 54, 18500, 1, "Vanguard"),
        LeaderboardEntryEntity("user_2", "CYBER_TITAN", 48, 14200, 2, "Major"),
        LeaderboardEntryEntity("user_3", "GHOST_COMMANDER", 38, 9800, 3, "Captain"),
        LeaderboardEntryEntity("user_4", "STEEL_SPECTRE", 26, 5400, 4, "Lieutenant"),
        LeaderboardEntryEntity("user_5", "IRON_SERGEANT", 18, 2900, 5, "Sergeant"),
        LeaderboardEntryEntity("user_6", "RAVEN_CORP", 12, 1450, 6, "Corporal"),
        LeaderboardEntryEntity("user_7", "SHADOW_PVT", 7, 650, 7, "Private")
    )
}
