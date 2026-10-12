package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.model.RoutineSection
import com.example.notification.NotificationHelper

class ClassReminderReceiver : BroadcastReceiver() {

  companion object {
    const val ACTION_CLASS_REMINDER = "com.example.ACTION_CLASS_REMINDER"
  }

  override fun onReceive(context: Context, intent: Intent) {
    if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
      val prefs = NotificationHelper.getPrefs(context)
      val sectionName = prefs.getString(NotificationHelper.PREF_SELECTED_SECTION, "A1") ?: "A1"
      val section = if (sectionName == "B1") RoutineSection.B1 else RoutineSection.A1
      val minutesBefore = prefs.getInt(NotificationHelper.PREF_MINUTES_BEFORE, 10)
      NotificationHelper.scheduleWeeklyReminders(context, section, minutesBefore)
      return
    }

    if (intent.action == ACTION_CLASS_REMINDER) {
      val id = intent.getStringExtra("id") ?: "unknown"
      val subjectName = intent.getStringExtra("subject_name") ?: "Class"
      val roomNo = intent.getStringExtra("room_no") ?: "-"
      val teacherName = intent.getStringExtra("teacher_name") ?: ""
      val teacherPhone = intent.getStringExtra("teacher_phone") ?: ""
      val startTime = intent.getStringExtra("start_time") ?: ""
      val endTime = intent.getStringExtra("end_time") ?: ""

      NotificationHelper.showClassNotification(
        context = context,
        notificationId = id.hashCode(),
        subjectName = subjectName,
        roomNo = roomNo,
        teacherName = teacherName,
        teacherPhone = teacherPhone,
        startTime = startTime,
        endTime = endTime
      )

      // Automatically reschedule next week's occurrence
      val prefs = NotificationHelper.getPrefs(context)
      val sectionName = prefs.getString(NotificationHelper.PREF_SELECTED_SECTION, "A1") ?: "A1"
      val section = if (sectionName == "B1") RoutineSection.B1 else RoutineSection.A1
      val minutesBefore = prefs.getInt(NotificationHelper.PREF_MINUTES_BEFORE, 10)
      NotificationHelper.scheduleWeeklyReminders(context, section, minutesBefore)
    }
  }
}
