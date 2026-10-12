package com.example.data.repository

import com.example.data.model.ClassRoutineItem
import com.example.data.model.RoutineDay
import com.example.data.model.RoutineSection
import com.example.data.model.TeacherInfo
import java.util.Calendar

object RoutineRepository {

  val teachers: List<TeacherInfo> = listOf(
    TeacherInfo("T1", "FALAK NAZ SAFA", "FNS", "01572911214", "Physics-I"),
    TeacherInfo("T2", "MAINUL H SIRAJI", "MHS", "01819918238", "Bangla-I"),
    TeacherInfo("T3", "SABIHA CHY", "SC", "01971588855", "English-I"),
    TeacherInfo("T4", "MOHAMMAD ILIAS", "MI", "01815652904", "Physical Education & Life Skills"),
    TeacherInfo("T5", "MD. FAKHRUL AREFIN", "FA", "01675630770", "Mathematics-I"),
    TeacherInfo("T6", "JAMIL AHMED", "JAM", "01743039937", "Basic Electricity"),
    TeacherInfo("T7", "ET3 Instructor", "ET3", "", "Electrical Engineering Materials"),
    TeacherInfo("T8", "ET4 Instructor", "ET4", "", "Basic Workshop Practice")
  )

  // Color mappings
  private const val COLOR_MATH = 0xFF4F46E5       // Indigo
  private const val COLOR_ELECTRICITY = 0xFFD97706// Amber/Orange
  private const val COLOR_PHYSICS = 0xFFDB2777    // Pink
  private const val COLOR_BANGLA = 0xFF059669     // Emerald
  private const val COLOR_ENGLISH = 0xFF0284C7    // Sky Blue
  private const val COLOR_WORKSHOP = 0xFF7C3AED   // Purple
  private const val COLOR_PE = 0xFF2563EB         // Royal Blue
  private const val COLOR_MATERIALS = 0xFFEA580C  // Deep Orange
  private const val COLOR_FREE = 0xFF64748B       // Slate

  val routineList: List<ClassRoutineItem> = listOf(
    // ----------------- SECTION A1 -----------------
    // SUN - A1
    ClassRoutineItem(
      id = "A1_SUN_1_2",
      section = RoutineSection.A1,
      day = RoutineDay.SUN,
      periodLabel = "Period 1 - 2",
      periodStart = 1, periodEnd = 2,
      startTime = "08:00", endTime = "09:30",
      startMinutes = 8 * 60, endMinutes = 9 * 60 + 30,
      subjectName = "Mathematics-I",
      roomNo = "Room 324",
      teacherName = "MD. FAKHRUL AREFIN",
      teacherCode = "FA",
      teacherPhone = "01675630770",
      colorHex = COLOR_MATH
    ),
    ClassRoutineItem(
      id = "A1_SUN_3_4",
      section = RoutineSection.A1,
      day = RoutineDay.SUN,
      periodLabel = "Period 3 - 4",
      periodStart = 3, periodEnd = 4,
      startTime = "09:30", endTime = "11:00",
      startMinutes = 9 * 60 + 30, endMinutes = 11 * 60,
      subjectName = "Basic Electricity",
      roomNo = "Room 304",
      teacherName = "JAMIL AHMED",
      teacherCode = "JAM",
      teacherPhone = "01743039937",
      colorHex = COLOR_ELECTRICITY
    ),
    ClassRoutineItem(
      id = "A1_SUN_5",
      section = RoutineSection.A1,
      day = RoutineDay.SUN,
      periodLabel = "Period 5",
      periodStart = 5, periodEnd = 5,
      startTime = "11:00", endTime = "11:45",
      startMinutes = 11 * 60, endMinutes = 11 * 60 + 45,
      subjectName = "Free Period",
      roomNo = "-",
      teacherName = "-",
      teacherCode = "",
      teacherPhone = "",
      isFreePeriod = true,
      colorHex = COLOR_FREE
    ),
    ClassRoutineItem(
      id = "A1_SUN_6",
      section = RoutineSection.A1,
      day = RoutineDay.SUN,
      periodLabel = "Period 6",
      periodStart = 6, periodEnd = 6,
      startTime = "11:45", endTime = "12:30",
      startMinutes = 11 * 60 + 45, endMinutes = 12 * 60 + 30,
      subjectName = "Electrical Engineering Materials",
      roomNo = "Room 312",
      teacherName = "ET3 Instructor",
      teacherCode = "ET3",
      teacherPhone = "",
      colorHex = COLOR_MATERIALS
    ),
    ClassRoutineItem(
      id = "A1_SUN_7",
      section = RoutineSection.A1,
      day = RoutineDay.SUN,
      periodLabel = "Period 7",
      periodStart = 7, periodEnd = 7,
      startTime = "12:30", endTime = "13:15",
      startMinutes = 12 * 60 + 30, endMinutes = 13 * 60 + 15,
      subjectName = "Free Period",
      roomNo = "-",
      teacherName = "-",
      teacherCode = "",
      teacherPhone = "",
      isFreePeriod = true,
      colorHex = COLOR_FREE
    ),

    // MON - A1
    ClassRoutineItem(
      id = "A1_MON_1",
      section = RoutineSection.A1,
      day = RoutineDay.MON,
      periodLabel = "Period 1",
      periodStart = 1, periodEnd = 1,
      startTime = "08:00", endTime = "08:45",
      startMinutes = 8 * 60, endMinutes = 8 * 60 + 45,
      subjectName = "Electrical Engineering Materials",
      roomNo = "Room 303",
      teacherName = "ET3 Instructor",
      teacherCode = "ET3",
      teacherPhone = "",
      colorHex = COLOR_MATERIALS
    ),
    ClassRoutineItem(
      id = "A1_MON_2",
      section = RoutineSection.A1,
      day = RoutineDay.MON,
      periodLabel = "Period 2",
      periodStart = 2, periodEnd = 2,
      startTime = "08:45", endTime = "09:30",
      startMinutes = 8 * 60 + 45, endMinutes = 9 * 60 + 30,
      subjectName = "Basic Electricity",
      roomNo = "Room 312",
      teacherName = "JAMIL AHMED",
      teacherCode = "JAM",
      teacherPhone = "01743039937",
      colorHex = COLOR_ELECTRICITY
    ),
    ClassRoutineItem(
      id = "A1_MON_3",
      section = RoutineSection.A1,
      day = RoutineDay.MON,
      periodLabel = "Period 3",
      periodStart = 3, periodEnd = 3,
      startTime = "09:30", endTime = "10:15",
      startMinutes = 9 * 60 + 30, endMinutes = 10 * 60 + 15,
      subjectName = "Physics-I",
      roomNo = "Room 306",
      teacherName = "FALAK NAZ SAFA",
      teacherCode = "FNS",
      teacherPhone = "01572911214",
      colorHex = COLOR_PHYSICS
    ),
    ClassRoutineItem(
      id = "A1_MON_4",
      section = RoutineSection.A1,
      day = RoutineDay.MON,
      periodLabel = "Period 4",
      periodStart = 4, periodEnd = 4,
      startTime = "10:15", endTime = "11:00",
      startMinutes = 10 * 60 + 15, endMinutes = 11 * 60,
      subjectName = "Bangla-I",
      roomNo = "Room 324",
      teacherName = "MAINUL H SIRAJI",
      teacherCode = "MHS",
      teacherPhone = "01819918238",
      colorHex = COLOR_BANGLA
    ),
    ClassRoutineItem(
      id = "A1_MON_5_7",
      section = RoutineSection.A1,
      day = RoutineDay.MON,
      periodLabel = "Period 5 - 7",
      periodStart = 5, periodEnd = 7,
      startTime = "11:00", endTime = "13:15",
      startMinutes = 11 * 60, endMinutes = 13 * 60 + 15,
      subjectName = "Basic Workshop Practice",
      roomNo = "Room 216",
      teacherName = "ET4 Instructor",
      teacherCode = "ET4",
      teacherPhone = "",
      colorHex = COLOR_WORKSHOP
    ),

    // TUE - A1
    ClassRoutineItem(
      id = "A1_TUE_1_3",
      section = RoutineSection.A1,
      day = RoutineDay.TUE,
      periodLabel = "Period 1 - 3",
      periodStart = 1, periodEnd = 3,
      startTime = "08:00", endTime = "10:15",
      startMinutes = 8 * 60, endMinutes = 10 * 60 + 15,
      subjectName = "Basic Electricity",
      roomNo = "BES",
      teacherName = "JAMIL AHMED",
      teacherCode = "JAM",
      teacherPhone = "01743039937",
      colorHex = COLOR_ELECTRICITY
    ),
    ClassRoutineItem(
      id = "A1_TUE_4",
      section = RoutineSection.A1,
      day = RoutineDay.TUE,
      periodLabel = "Period 4",
      periodStart = 4, periodEnd = 4,
      startTime = "10:15", endTime = "11:00",
      startMinutes = 10 * 60 + 15, endMinutes = 11 * 60,
      subjectName = "Bangla-I",
      roomNo = "Room 324",
      teacherName = "MAINUL H SIRAJI",
      teacherCode = "MHS",
      teacherPhone = "01819918238",
      colorHex = COLOR_BANGLA
    ),
    ClassRoutineItem(
      id = "A1_TUE_5",
      section = RoutineSection.A1,
      day = RoutineDay.TUE,
      periodLabel = "Period 5",
      periodStart = 5, periodEnd = 5,
      startTime = "11:00", endTime = "11:45",
      startMinutes = 11 * 60, endMinutes = 11 * 60 + 45,
      subjectName = "English-I",
      roomNo = "Room 312",
      teacherName = "SABIHA CHY",
      teacherCode = "SC",
      teacherPhone = "01971588855",
      colorHex = COLOR_ENGLISH
    ),
    ClassRoutineItem(
      id = "A1_TUE_6_7",
      section = RoutineSection.A1,
      day = RoutineDay.TUE,
      periodLabel = "Period 6 - 7",
      periodStart = 6, periodEnd = 7,
      startTime = "11:45", endTime = "13:15",
      startMinutes = 11 * 60 + 45, endMinutes = 13 * 60 + 15,
      subjectName = "Physics-I",
      roomNo = "Room 324",
      teacherName = "FALAK NAZ SAFA",
      teacherCode = "FNS",
      teacherPhone = "01572911214",
      colorHex = COLOR_PHYSICS
    ),

    // WED - A1
    ClassRoutineItem(
      id = "A1_WED_1_3",
      section = RoutineSection.A1,
      day = RoutineDay.WED,
      periodLabel = "Period 1 - 3",
      periodStart = 1, periodEnd = 3,
      startTime = "08:00", endTime = "10:15",
      startMinutes = 8 * 60, endMinutes = 10 * 60 + 15,
      subjectName = "Mathematics-I",
      roomNo = "Room 324",
      teacherName = "MD. FAKHRUL AREFIN",
      teacherCode = "FA",
      teacherPhone = "01675630770",
      colorHex = COLOR_MATH
    ),
    ClassRoutineItem(
      id = "A1_WED_4",
      section = RoutineSection.A1,
      day = RoutineDay.WED,
      periodLabel = "Period 4",
      periodStart = 4, periodEnd = 4,
      startTime = "10:15", endTime = "11:00",
      startMinutes = 10 * 60 + 15, endMinutes = 11 * 60,
      subjectName = "Free Period",
      roomNo = "-",
      teacherName = "-",
      teacherCode = "",
      teacherPhone = "",
      isFreePeriod = true,
      colorHex = COLOR_FREE
    ),
    ClassRoutineItem(
      id = "A1_WED_5",
      section = RoutineSection.A1,
      day = RoutineDay.WED,
      periodLabel = "Period 5",
      periodStart = 5, periodEnd = 5,
      startTime = "11:00", endTime = "11:45",
      startMinutes = 11 * 60, endMinutes = 11 * 60 + 45,
      subjectName = "English-I",
      roomNo = "Room 312",
      teacherName = "SABIHA CHY",
      teacherCode = "SC",
      teacherPhone = "01971588855",
      colorHex = COLOR_ENGLISH
    ),
    ClassRoutineItem(
      id = "A1_WED_6_7",
      section = RoutineSection.A1,
      day = RoutineDay.WED,
      periodLabel = "Period 6 - 7",
      periodStart = 6, periodEnd = 7,
      startTime = "11:45", endTime = "13:15",
      startMinutes = 11 * 60 + 45, endMinutes = 13 * 60 + 15,
      subjectName = "Physics-I",
      roomNo = "PHY LAB",
      teacherName = "FALAK NAZ SAFA",
      teacherCode = "FNS",
      teacherPhone = "01572911214",
      colorHex = COLOR_PHYSICS
    ),

    // THU - A1
    ClassRoutineItem(
      id = "A1_THU_1_3",
      section = RoutineSection.A1,
      day = RoutineDay.THU,
      periodLabel = "Period 1 - 3",
      periodStart = 1, periodEnd = 3,
      startTime = "08:00", endTime = "10:15",
      startMinutes = 8 * 60, endMinutes = 10 * 60 + 15,
      subjectName = "Basic Workshop Practice",
      roomNo = "Room 216",
      teacherName = "ET4 Instructor",
      teacherCode = "ET4",
      teacherPhone = "",
      colorHex = COLOR_WORKSHOP
    ),
    ClassRoutineItem(
      id = "A1_THU_4",
      section = RoutineSection.A1,
      day = RoutineDay.THU,
      periodLabel = "Period 4",
      periodStart = 4, periodEnd = 4,
      startTime = "10:15", endTime = "11:00",
      startMinutes = 10 * 60 + 15, endMinutes = 11 * 60,
      subjectName = "Mathematics-I",
      roomNo = "Room 324",
      teacherName = "MD. FAKHRUL AREFIN",
      teacherCode = "FA",
      teacherPhone = "01675630770",
      colorHex = COLOR_MATH
    ),
    ClassRoutineItem(
      id = "A1_THU_5_7",
      section = RoutineSection.A1,
      day = RoutineDay.THU,
      periodLabel = "Period 5 - 7",
      periodStart = 5, periodEnd = 7,
      startTime = "11:00", endTime = "13:15",
      startMinutes = 11 * 60, endMinutes = 13 * 60 + 15,
      subjectName = "Physical Education & Life Skills",
      roomNo = "FIELD",
      teacherName = "MOHAMMAD ILIAS",
      teacherCode = "MI",
      teacherPhone = "01815652904",
      colorHex = COLOR_PE
    ),

    // ----------------- SECTION B1 -----------------
    // SUN - B1
    ClassRoutineItem(
      id = "B1_SUN_1_2",
      section = RoutineSection.B1,
      day = RoutineDay.SUN,
      periodLabel = "Period 1 - 2",
      periodStart = 1, periodEnd = 2,
      startTime = "08:00", endTime = "09:30",
      startMinutes = 8 * 60, endMinutes = 9 * 60 + 30,
      subjectName = "Mathematics-I",
      roomNo = "Room 324",
      teacherName = "MD. FAKHRUL AREFIN",
      teacherCode = "FA",
      teacherPhone = "01675630770",
      colorHex = COLOR_MATH
    ),
    ClassRoutineItem(
      id = "B1_SUN_3",
      section = RoutineSection.B1,
      day = RoutineDay.SUN,
      periodLabel = "Period 3",
      periodStart = 3, periodEnd = 3,
      startTime = "09:30", endTime = "10:15",
      startMinutes = 9 * 60 + 30, endMinutes = 10 * 60 + 15,
      subjectName = "English-I",
      roomNo = "Room 312",
      teacherName = "SABIHA CHY",
      teacherCode = "SC",
      teacherPhone = "01971588855",
      colorHex = COLOR_ENGLISH
    ),
    ClassRoutineItem(
      id = "B1_SUN_4",
      section = RoutineSection.B1,
      day = RoutineDay.SUN,
      periodLabel = "Period 4",
      periodStart = 4, periodEnd = 4,
      startTime = "10:15", endTime = "11:00",
      startMinutes = 10 * 60 + 15, endMinutes = 11 * 60,
      subjectName = "Free Period",
      roomNo = "-",
      teacherName = "-",
      teacherCode = "",
      teacherPhone = "",
      isFreePeriod = true,
      colorHex = COLOR_FREE
    ),
    ClassRoutineItem(
      id = "B1_SUN_5_7",
      section = RoutineSection.B1,
      day = RoutineDay.SUN,
      periodLabel = "Period 5 - 7",
      periodStart = 5, periodEnd = 7,
      startTime = "11:00", endTime = "13:15",
      startMinutes = 11 * 60, endMinutes = 13 * 60 + 15,
      subjectName = "Basic Workshop Practice",
      roomNo = "Room 216",
      teacherName = "ET4 Instructor",
      teacherCode = "ET4",
      teacherPhone = "",
      colorHex = COLOR_WORKSHOP
    ),

    // MON - B1
    ClassRoutineItem(
      id = "B1_MON_1",
      section = RoutineSection.B1,
      day = RoutineDay.MON,
      periodLabel = "Period 1",
      periodStart = 1, periodEnd = 1,
      startTime = "08:00", endTime = "08:45",
      startMinutes = 8 * 60, endMinutes = 8 * 60 + 45,
      subjectName = "Basic Electricity",
      roomNo = "Room 324",
      teacherName = "JAMIL AHMED",
      teacherCode = "JAM",
      teacherPhone = "01743039937",
      colorHex = COLOR_ELECTRICITY
    ),
    ClassRoutineItem(
      id = "B1_MON_2",
      section = RoutineSection.B1,
      day = RoutineDay.MON,
      periodLabel = "Period 2",
      periodStart = 2, periodEnd = 2,
      startTime = "08:45", endTime = "09:30",
      startMinutes = 8 * 60 + 45, endMinutes = 9 * 60 + 30,
      subjectName = "Electrical Engineering Materials",
      roomNo = "Room 304",
      teacherName = "ET3 Instructor",
      teacherCode = "ET3",
      teacherPhone = "",
      colorHex = COLOR_MATERIALS
    ),
    ClassRoutineItem(
      id = "B1_MON_3",
      section = RoutineSection.B1,
      day = RoutineDay.MON,
      periodLabel = "Period 3",
      periodStart = 3, periodEnd = 3,
      startTime = "09:30", endTime = "10:15",
      startMinutes = 9 * 60 + 30, endMinutes = 10 * 60 + 15,
      subjectName = "Physics-I",
      roomNo = "Room 306",
      teacherName = "FALAK NAZ SAFA",
      teacherCode = "FNS",
      teacherPhone = "01572911214",
      colorHex = COLOR_PHYSICS
    ),
    ClassRoutineItem(
      id = "B1_MON_4",
      section = RoutineSection.B1,
      day = RoutineDay.MON,
      periodLabel = "Period 4",
      periodStart = 4, periodEnd = 4,
      startTime = "10:15", endTime = "11:00",
      startMinutes = 10 * 60 + 15, endMinutes = 11 * 60,
      subjectName = "Bangla-I",
      roomNo = "Room 324",
      teacherName = "MAINUL H SIRAJI",
      teacherCode = "MHS",
      teacherPhone = "01819918238",
      colorHex = COLOR_BANGLA
    ),
    ClassRoutineItem(
      id = "B1_MON_5_7",
      section = RoutineSection.B1,
      day = RoutineDay.MON,
      periodLabel = "Period 5 - 7",
      periodStart = 5, periodEnd = 7,
      startTime = "11:00", endTime = "13:15",
      startMinutes = 11 * 60, endMinutes = 13 * 60 + 15,
      subjectName = "Physical Education & Life Skills",
      roomNo = "FIELD",
      teacherName = "MOHAMMAD ILIAS",
      teacherCode = "MI",
      teacherPhone = "01815652904",
      colorHex = COLOR_PE
    ),

    // TUE - B1
    ClassRoutineItem(
      id = "B1_TUE_1_3",
      section = RoutineSection.B1,
      day = RoutineDay.TUE,
      periodLabel = "Period 1 - 3",
      periodStart = 1, periodEnd = 3,
      startTime = "08:00", endTime = "10:15",
      startMinutes = 8 * 60, endMinutes = 10 * 60 + 15,
      subjectName = "Basic Workshop Practice",
      roomNo = "Room 210",
      teacherName = "ET4 Instructor",
      teacherCode = "ET4",
      teacherPhone = "",
      colorHex = COLOR_WORKSHOP
    ),
    ClassRoutineItem(
      id = "B1_TUE_4",
      section = RoutineSection.B1,
      day = RoutineDay.TUE,
      periodLabel = "Period 4",
      periodStart = 4, periodEnd = 4,
      startTime = "10:15", endTime = "11:00",
      startMinutes = 10 * 60 + 15, endMinutes = 11 * 60,
      subjectName = "Free Period",
      roomNo = "-",
      teacherName = "-",
      teacherCode = "",
      teacherPhone = "",
      isFreePeriod = true,
      colorHex = COLOR_FREE
    ),
    ClassRoutineItem(
      id = "B1_TUE_5",
      section = RoutineSection.B1,
      day = RoutineDay.TUE,
      periodLabel = "Period 5",
      periodStart = 5, periodEnd = 5,
      startTime = "11:00", endTime = "11:45",
      startMinutes = 11 * 60, endMinutes = 11 * 60 + 45,
      subjectName = "Bangla-I",
      roomNo = "Room 324",
      teacherName = "MAINUL H SIRAJI",
      teacherCode = "MHS",
      teacherPhone = "01819918238",
      colorHex = COLOR_BANGLA
    ),
    ClassRoutineItem(
      id = "B1_TUE_6",
      section = RoutineSection.B1,
      day = RoutineDay.TUE,
      periodLabel = "Period 6",
      periodStart = 6, periodEnd = 6,
      startTime = "11:45", endTime = "12:30",
      startMinutes = 11 * 60 + 45, endMinutes = 12 * 60 + 30,
      subjectName = "Basic Electricity",
      roomNo = "Room 315",
      teacherName = "JAMIL AHMED",
      teacherCode = "JAM",
      teacherPhone = "01743039937",
      colorHex = COLOR_ELECTRICITY
    ),
    ClassRoutineItem(
      id = "B1_TUE_7",
      section = RoutineSection.B1,
      day = RoutineDay.TUE,
      periodLabel = "Period 7",
      periodStart = 7, periodEnd = 7,
      startTime = "12:30", endTime = "13:15",
      startMinutes = 12 * 60 + 30, endMinutes = 13 * 60 + 15,
      subjectName = "Physics-I",
      roomNo = "Room 324",
      teacherName = "FALAK NAZ SAFA",
      teacherCode = "FNS",
      teacherPhone = "01572911214",
      colorHex = COLOR_PHYSICS
    ),

    // WED - B1
    ClassRoutineItem(
      id = "B1_WED_1_3",
      section = RoutineSection.B1,
      day = RoutineDay.WED,
      periodLabel = "Period 1 - 3",
      periodStart = 1, periodEnd = 3,
      startTime = "08:00", endTime = "10:15",
      startMinutes = 8 * 60, endMinutes = 10 * 60 + 15,
      subjectName = "Mathematics-I",
      roomNo = "Room 324",
      teacherName = "MD. FAKHRUL AREFIN",
      teacherCode = "FA",
      teacherPhone = "01675630770",
      colorHex = COLOR_MATH
    ),
    ClassRoutineItem(
      id = "B1_WED_4",
      section = RoutineSection.B1,
      day = RoutineDay.WED,
      periodLabel = "Period 4",
      periodStart = 4, periodEnd = 4,
      startTime = "10:15", endTime = "11:00",
      startMinutes = 10 * 60 + 15, endMinutes = 11 * 60,
      subjectName = "Basic Electricity",
      roomNo = "BWS1",
      teacherName = "JAMIL AHMED",
      teacherCode = "JAM",
      teacherPhone = "01743039937",
      colorHex = COLOR_ELECTRICITY
    ),
    ClassRoutineItem(
      id = "B1_WED_5",
      section = RoutineSection.B1,
      day = RoutineDay.WED,
      periodLabel = "Period 5",
      periodStart = 5, periodEnd = 5,
      startTime = "11:00", endTime = "11:45",
      startMinutes = 11 * 60, endMinutes = 11 * 60 + 45,
      subjectName = "Free Period",
      roomNo = "-",
      teacherName = "-",
      teacherCode = "",
      teacherPhone = "",
      isFreePeriod = true,
      colorHex = COLOR_FREE
    ),
    ClassRoutineItem(
      id = "B1_WED_6",
      section = RoutineSection.B1,
      day = RoutineDay.WED,
      periodLabel = "Period 6",
      periodStart = 6, periodEnd = 6,
      startTime = "11:45", endTime = "12:30",
      startMinutes = 11 * 60 + 45, endMinutes = 12 * 60 + 30,
      subjectName = "Free Period",
      roomNo = "-",
      teacherName = "-",
      teacherCode = "",
      teacherPhone = "",
      isFreePeriod = true,
      colorHex = COLOR_FREE
    ),
    ClassRoutineItem(
      id = "B1_WED_7",
      section = RoutineSection.B1,
      day = RoutineDay.WED,
      periodLabel = "Period 7",
      periodStart = 7, periodEnd = 7,
      startTime = "12:30", endTime = "13:15",
      startMinutes = 12 * 60 + 30, endMinutes = 13 * 60 + 15,
      subjectName = "English-I",
      roomNo = "Room 312",
      teacherName = "SABIHA CHY",
      teacherCode = "SC",
      teacherPhone = "01971588855",
      colorHex = COLOR_ENGLISH
    ),

    // THU - B1
    ClassRoutineItem(
      id = "B1_THU_1_3",
      section = RoutineSection.B1,
      day = RoutineDay.THU,
      periodLabel = "Period 1 - 3",
      periodStart = 1, periodEnd = 3,
      startTime = "08:00", endTime = "10:15",
      startMinutes = 8 * 60, endMinutes = 10 * 60 + 15,
      subjectName = "Basic Electricity",
      roomNo = "BES",
      teacherName = "JAMIL AHMED",
      teacherCode = "JAM",
      teacherPhone = "01743039937",
      colorHex = COLOR_ELECTRICITY
    ),
    ClassRoutineItem(
      id = "B1_THU_4",
      section = RoutineSection.B1,
      day = RoutineDay.THU,
      periodLabel = "Period 4",
      periodStart = 4, periodEnd = 4,
      startTime = "10:15", endTime = "11:00",
      startMinutes = 10 * 60 + 15, endMinutes = 11 * 60,
      subjectName = "Mathematics-I",
      roomNo = "Room 324",
      teacherName = "MD. FAKHRUL AREFIN",
      teacherCode = "FA",
      teacherPhone = "01675630770",
      colorHex = COLOR_MATH
    ),
    ClassRoutineItem(
      id = "B1_THU_5",
      section = RoutineSection.B1,
      day = RoutineDay.THU,
      periodLabel = "Period 5",
      periodStart = 5, periodEnd = 5,
      startTime = "11:00", endTime = "11:45",
      startMinutes = 11 * 60, endMinutes = 11 * 60 + 45,
      subjectName = "Electrical Engineering Materials",
      roomNo = "Room 303",
      teacherName = "ET3 Instructor",
      teacherCode = "ET3",
      teacherPhone = "",
      colorHex = COLOR_MATERIALS
    ),
    ClassRoutineItem(
      id = "B1_THU_6_7",
      section = RoutineSection.B1,
      day = RoutineDay.THU,
      periodLabel = "Period 6 - 7",
      periodStart = 6, periodEnd = 7,
      startTime = "11:45", endTime = "13:15",
      startMinutes = 11 * 60 + 45, endMinutes = 13 * 60 + 15,
      subjectName = "Physics-I",
      roomNo = "PHY LAB",
      teacherName = "FALAK NAZ SAFA",
      teacherCode = "FNS",
      teacherPhone = "01572911214",
      colorHex = COLOR_PHYSICS
    )
  )

  fun getRoutineForDay(section: RoutineSection, day: RoutineDay): List<ClassRoutineItem> {
    return routineList.filter { it.section == section && it.day == day }
  }

  fun getCurrentAndNextClass(
    section: RoutineSection,
    day: RoutineDay?,
    currentMinutes: Int
  ): Pair<ClassRoutineItem?, ClassRoutineItem?> {
    if (day == null) return Pair(null, null)
    val dayClasses = getRoutineForDay(section, day).filter { !it.isFreePeriod }

    val current = dayClasses.find { currentMinutes in it.startMinutes until it.endMinutes }
    val next = dayClasses.filter { it.startMinutes > currentMinutes }.minByOrNull { it.startMinutes }

    return Pair(current, next)
  }
}
