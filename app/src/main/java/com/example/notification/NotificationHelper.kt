package com.example.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.RoutineDay
import com.example.data.model.RoutineSection
import com.example.data.repository.RoutineRepository
import com.example.receiver.ClassReminderReceiver
import java.util.Calendar

object NotificationHelper {

  const val CHANNEL_ID = "cgpi_class_reminders"
  const val CHANNEL_NAME = "Class Routine & Task Reminders"
  const val PREFS_NAME = "cgpi_routine_prefs"
  const val PREF_REMINDERS_ENABLED = "reminders_enabled"
  const val PREF_MINUTES_BEFORE = "minutes_before"
  const val PREF_SELECTED_SECTION = "selected_section"

  fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_HIGH
      ).apply {
        description = "Alerts before scheduled classes and tasks"
        enableVibration(true)
        vibrationPattern = longArrayOf(0, 250, 250, 250)
      }
      notificationManager.createNotificationChannel(channel)
    }
  }

  fun showClassNotification(
    context: Context,
    notificationId: Int,
    subjectName: String,
    roomNo: String,
    teacherName: String,
    teacherPhone: String,
    startTime: String,
    endTime: String
  ) {
    createNotificationChannel(context)

    val openAppIntent = Intent(context, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val contentPendingIntent = PendingIntent.getActivity(
      context,
      notificationId,
      openAppIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setContentTitle("⏰ Class in 10 mins: $subjectName")
      .setContentText("Room: $roomNo • $startTime - $endTime • $teacherName")
      .setStyle(
        NotificationCompat.BigTextStyle()
          .bigText("📖 Subject: $subjectName\n🏢 Room: $roomNo ($startTime - $endTime)\n👨‍🏫 Teacher: $teacherName\n⚡ Electrical Technology CGPI")
      )
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setDefaults(NotificationCompat.DEFAULT_ALL)
      .setAutoCancel(true)
      .setContentIntent(contentPendingIntent)

    if (teacherPhone.isNotBlank()) {
      val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$teacherPhone"))
      val callPendingIntent = PendingIntent.getActivity(
        context,
        notificationId + 1000,
        callIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
      )
      notificationBuilder.addAction(
        android.R.drawable.ic_menu_call,
        "Call Teacher",
        callPendingIntent
      )
    }

    val notificationManager =
      context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notificationId, notificationBuilder.build())
  }

  fun sendTestNotification(context: Context, section: RoutineSection) {
    val sampleClass = RoutineRepository.routineList.firstOrNull { it.section == section && !it.isFreePeriod }
      ?: RoutineRepository.routineList.first()

    showClassNotification(
      context = context,
      notificationId = 99999,
      subjectName = sampleClass.subjectName,
      roomNo = sampleClass.roomNo,
      teacherName = sampleClass.teacherName,
      teacherPhone = sampleClass.teacherPhone,
      startTime = sampleClass.startTime,
      endTime = sampleClass.endTime
    )
  }

  fun scheduleWeeklyReminders(context: Context, section: RoutineSection, minutesBefore: Int = 10) {
    val prefs = getPrefs(context)
    if (!prefs.getBoolean(PREF_REMINDERS_ENABLED, true)) {
      cancelAllReminders(context)
      return
    }

    try {
      val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
      val classes = RoutineRepository.routineList.filter { it.section == section && !it.isFreePeriod }
      val now = Calendar.getInstance()

      for (item in classes) {
        val dayCal = getNextOccurrenceCalendar(item.day, item.startMinutes, minutesBefore)
        if (dayCal.timeInMillis <= now.timeInMillis) {
          dayCal.add(Calendar.WEEK_OF_YEAR, 1)
        }

        val intent = Intent(context, ClassReminderReceiver::class.java).apply {
          action = ClassReminderReceiver.ACTION_CLASS_REMINDER
          putExtra("id", item.id)
          putExtra("subject_name", item.subjectName)
          putExtra("room_no", item.roomNo)
          putExtra("teacher_name", item.teacherName)
          putExtra("teacher_phone", item.teacherPhone)
          putExtra("start_time", item.startTime)
          putExtra("end_time", item.endTime)
        }

        val requestCode = item.id.hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
          context,
          requestCode,
          intent,
          PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
              alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                dayCal.timeInMillis,
                pendingIntent
              )
            } else {
              alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                dayCal.timeInMillis,
                pendingIntent
              )
            }
          } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
              AlarmManager.RTC_WAKEUP,
              dayCal.timeInMillis,
              pendingIntent
            )
          } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, dayCal.timeInMillis, pendingIntent)
          }
        } catch (e: Exception) {
          try {
            alarmManager.set(AlarmManager.RTC_WAKEUP, dayCal.timeInMillis, pendingIntent)
          } catch (_: Exception) {}
        }
      }
    } catch (_: Exception) {}
  }

  fun cancelAllReminders(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    for (item in RoutineRepository.routineList) {
      val intent = Intent(context, ClassReminderReceiver::class.java).apply {
        action = ClassReminderReceiver.ACTION_CLASS_REMINDER
      }
      val pendingIntent = PendingIntent.getBroadcast(
        context,
        item.id.hashCode(),
        intent,
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
      )
      if (pendingIntent != null) {
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
      }
    }
  }

  private fun getNextOccurrenceCalendar(
    day: RoutineDay,
    startMinutes: Int,
    minutesBefore: Int
  ): Calendar {
    val cal = Calendar.getInstance()
    val currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
    var daysUntilTarget = day.calendarDay - currentDayOfWeek
    if (daysUntilTarget < 0) {
      daysUntilTarget += 7
    }

    cal.add(Calendar.DAY_OF_YEAR, daysUntilTarget)
    cal.set(Calendar.HOUR_OF_DAY, startMinutes / 60)
    cal.set(Calendar.MINUTE, startMinutes % 60)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    cal.add(Calendar.MINUTE, -minutesBefore)

    return cal
  }

  fun getPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  }
}
