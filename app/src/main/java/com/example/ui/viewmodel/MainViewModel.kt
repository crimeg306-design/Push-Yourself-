package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AchievementEntity
import com.example.data.model.DailyLogEntity
import com.example.data.model.DailyQuestEntity
import com.example.data.model.LeaderboardEntryEntity
import com.example.data.model.NotificationSettings
import com.example.data.model.PresetTask
import com.example.data.model.UserEntity
import com.example.data.model.UserStatsEntity
import com.example.data.repository.PushYourselfRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository = PushYourselfRepository(application)

    val todayDateString: String = repository.getTodayString()

    val user: StateFlow<UserEntity?> = repository.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userStats: StateFlow<UserStatsEntity?> = repository.getUserStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val todayQuests: StateFlow<List<DailyQuestEntity>> = repository.getTodayQuests(todayDateString)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dailyLogs: StateFlow<List<DailyLogEntity>> = repository.getAllDailyLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val achievements: StateFlow<List<AchievementEntity>> = repository.getAllAchievements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val leaderboard: StateFlow<List<LeaderboardEntryEntity>> = repository.getLeaderboard()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _timeToReset = MutableStateFlow("00:00:00")
    val timeToReset: StateFlow<String> = _timeToReset.asStateFlow()

    private val _notificationSettings = MutableStateFlow(NotificationSettings())
    val notificationSettings: StateFlow<NotificationSettings> = _notificationSettings.asStateFlow()

    private val _showOnboarding = MutableStateFlow(false)
    val showOnboarding: StateFlow<Boolean> = _showOnboarding.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureInitialized()
            val current = user.value
            if (current != null && current.callsign.isBlank()) {
                _showOnboarding.value = true
            }
        }
        startCountdownTimer()
    }

    private fun startCountdownTimer() {
        viewModelScope.launch {
            while (true) {
                val now = Calendar.getInstance()
                val midnight = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val diffMillis = midnight.timeInMillis - now.timeInMillis
                if (diffMillis > 0) {
                    val hours = (diffMillis / (1000 * 60 * 60)) % 24
                    val minutes = (diffMillis / (1000 * 60)) % 60
                    val seconds = (diffMillis / 1000) % 60
                    _timeToReset.value = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                } else {
                    _timeToReset.value = "00:00:00"
                    repository.ensureInitialized()
                }
                delay(1000)
            }
        }
    }

    fun submitCallsign(callsign: String) {
        if (callsign.isBlank()) return
        viewModelScope.launch {
            repository.setCallsign(callsign)
            _showOnboarding.value = false
        }
    }

    fun updateQuestProgress(quest: DailyQuestEntity, newProgress: Int) {
        viewModelScope.launch {
            repository.updateQuestProgress(quest, newProgress)
        }
    }

    fun completePresetTask(task: PresetTask) {
        viewModelScope.launch {
            repository.completePresetTask(task)
        }
    }

    fun createCustomQuest(name: String, category: String, targetValue: Int, unit: String, xpReward: Int) {
        viewModelScope.launch {
            repository.createCustomQuest(name, category, targetValue, unit, xpReward)
        }
    }

    fun toggleNotificationMute(type: String) {
        val current = _notificationSettings.value
        val updated = when (type) {
            "00:00" -> current.copy(muteReset0000 = !current.muteReset0000)
            "04:00" -> current.copy(mute0400 = !current.mute0400)
            "08:00" -> current.copy(mute0800 = !current.mute0800)
            "12:00" -> current.copy(mute1200 = !current.mute1200)
            "18:00" -> current.copy(mute1800 = !current.mute1800)
            "23:00" -> current.copy(mute2300 = !current.mute2300)
            "23:45" -> current.copy(mute2345 = !current.mute2345)
            "haptics" -> {
                val newHaptics = !current.soundAndHapticsEnabled
                repository.hapticHelper.isEnabled = newHaptics
                current.copy(soundAndHapticsEnabled = newHaptics)
            }
            else -> current
        }
        _notificationSettings.value = updated
    }

    fun exportData(onResult: (String) -> Unit) {
        viewModelScope.launch {
            val json = repository.exportDataJson()
            onResult(json)
        }
    }

    fun importData(json: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.importDataJson(json)
            if (success) {
                repository.ensureInitialized()
            }
            onResult(success)
        }
    }
}
