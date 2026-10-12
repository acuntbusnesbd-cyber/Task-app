package com.example.data.model

import java.util.Calendar

enum class RoutineSection(val displayName: String, val code: String) {
  A1("First Shift - A1", "A1"),
  B1("First Shift - B1", "B1")
}

enum class RoutineDay(val shortName: String, val fullName: String, val calendarDay: Int) {
  SUN("SUN", "Sunday", Calendar.SUNDAY),
  MON("MON", "Monday", Calendar.MONDAY),
  TUE("TUE", "Tuesday", Calendar.TUESDAY),
  WED("WED", "Wednesday", Calendar.WEDNESDAY),
  THU("THU", "Thursday", Calendar.THURSDAY);

  companion object {
    fun fromCalendarDay(dayOfWeek: Int): RoutineDay? {
      return entries.find { it.calendarDay == dayOfWeek }
    }
  }
}

data class TeacherInfo(
  val id: String,
  val name: String,
  val shortCode: String,
  val phone: String,
  val subject: String
)

data class ClassRoutineItem(
  val id: String,
  val section: RoutineSection,
  val day: RoutineDay,
  val periodLabel: String,
  val periodStart: Int,
  val periodEnd: Int,
  val startTime: String,
  val endTime: String,
  val startMinutes: Int, // e.g., 8 * 60 = 480
  val endMinutes: Int,   // e.g., 9 * 60 + 30 = 570
  val subjectName: String,
  val roomNo: String,
  val teacherName: String,
  val teacherCode: String,
  val teacherPhone: String,
  val isFreePeriod: Boolean = false,
  val colorHex: Long = 0xFF0D47A1
) {
  fun isCurrentlyHappening(currentDay: RoutineDay?, currentMinutes: Int): Boolean {
    if (currentDay != day || isFreePeriod) return false
    return currentMinutes in startMinutes until endMinutes
  }

  fun isUpcomingToday(currentDay: RoutineDay?, currentMinutes: Int): Boolean {
    if (currentDay != day || isFreePeriod) return false
    return currentMinutes < startMinutes
  }
}
