package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.TaskEntity
import com.example.data.model.ClassRoutineItem
import com.example.data.model.RoutineDay
import com.example.data.model.RoutineSection
import com.example.data.repository.RoutineRepository
import com.example.notification.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

  private val context = application.applicationContext
  private val taskDao = AppDatabase.getDatabase(context).taskDao()
  private val prefs = NotificationHelper.getPrefs(context)

  private val _selectedSection = MutableStateFlow(
    if (prefs.getString(NotificationHelper.PREF_SELECTED_SECTION, "A1") == "B1") {
      RoutineSection.B1
    } else {
      RoutineSection.A1
    }
  )
  val selectedSection: StateFlow<RoutineSection> = _selectedSection.asStateFlow()

  private val _selectedDay = MutableStateFlow(getInitialRoutineDay())
  val selectedDay: StateFlow<RoutineDay> = _selectedDay.asStateFlow()

  private val _currentMinutes = MutableStateFlow(getCurrentDayMinutes())
  val currentMinutes: StateFlow<Int> = _currentMinutes.asStateFlow()

  private val _remindersEnabled = MutableStateFlow(
    prefs.getBoolean(NotificationHelper.PREF_REMINDERS_ENABLED, true)
  )
  val remindersEnabled: StateFlow<Boolean> = _remindersEnabled.asStateFlow()

  private val _minutesBefore = MutableStateFlow(
    prefs.getInt(NotificationHelper.PREF_MINUTES_BEFORE, 10)
  )
  val minutesBefore: StateFlow<Int> = _minutesBefore.asStateFlow()

  val tasks: StateFlow<List<TaskEntity>> = taskDao.getAllTasksFlow()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  init {
    NotificationHelper.createNotificationChannel(context)
    if (_remindersEnabled.value) {
      NotificationHelper.scheduleWeeklyReminders(
        context,
        _selectedSection.value,
        _minutesBefore.value
      )
    }

    // Periodic time tick to keep "Now Happening" & "Next Class" fresh
    viewModelScope.launch {
      while (true) {
        _currentMinutes.value = getCurrentDayMinutes()
        delay(30_000) // check every 30 seconds
      }
    }
  }

  fun setSection(section: RoutineSection) {
    _selectedSection.value = section
    prefs.edit().putString(NotificationHelper.PREF_SELECTED_SECTION, section.code).apply()
    if (_remindersEnabled.value) {
      NotificationHelper.scheduleWeeklyReminders(context, section, _minutesBefore.value)
    }
  }

  fun setDay(day: RoutineDay) {
    _selectedDay.value = day
  }

  fun getRoutineForDay(section: RoutineSection, day: RoutineDay): List<ClassRoutineItem> {
    return RoutineRepository.getRoutineForDay(section, day)
  }

  fun getCurrentAndNextClass(section: RoutineSection): Pair<ClassRoutineItem?, ClassRoutineItem?> {
    val cal = Calendar.getInstance()
    val todayRoutineDay = RoutineDay.fromCalendarDay(cal.get(Calendar.DAY_OF_WEEK))
    return RoutineRepository.getCurrentAndNextClass(section, todayRoutineDay, _currentMinutes.value)
  }

  fun isToday(day: RoutineDay): Boolean {
    val cal = Calendar.getInstance()
    return RoutineDay.fromCalendarDay(cal.get(Calendar.DAY_OF_WEEK)) == day
  }

  fun addTask(title: String, subjectName: String, notes: String, dueDate: Long, priority: String) {
    viewModelScope.launch {
      taskDao.insertTask(
        TaskEntity(
          title = title.trim(),
          subjectName = subjectName.trim(),
          notes = notes.trim(),
          dueDate = dueDate,
          section = _selectedSection.value.code,
          priority = priority
        )
      )
    }
  }

  fun toggleTaskCompletion(task: TaskEntity) {
    viewModelScope.launch {
      taskDao.toggleTaskCompletion(task.id, !task.isCompleted)
    }
  }

  fun deleteTask(task: TaskEntity) {
    viewModelScope.launch {
      taskDao.deleteTask(task)
    }
  }

  fun setRemindersEnabled(enabled: Boolean) {
    _remindersEnabled.value = enabled
    prefs.edit().putBoolean(NotificationHelper.PREF_REMINDERS_ENABLED, enabled).apply()
    if (enabled) {
      NotificationHelper.scheduleWeeklyReminders(context, _selectedSection.value, _minutesBefore.value)
    } else {
      NotificationHelper.cancelAllReminders(context)
    }
  }

  fun setMinutesBefore(minutes: Int) {
    _minutesBefore.value = minutes
    prefs.edit().putInt(NotificationHelper.PREF_MINUTES_BEFORE, minutes).apply()
    if (_remindersEnabled.value) {
      NotificationHelper.scheduleWeeklyReminders(context, _selectedSection.value, minutes)
    }
  }

  fun triggerTestNotification() {
    NotificationHelper.sendTestNotification(context, _selectedSection.value)
  }

  private fun getInitialRoutineDay(): RoutineDay {
    val cal = Calendar.getInstance()
    val today = RoutineDay.fromCalendarDay(cal.get(Calendar.DAY_OF_WEEK))
    return today ?: RoutineDay.SUN
  }

  private fun getCurrentDayMinutes(): Int {
    val cal = Calendar.getInstance()
    return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
  }
}
