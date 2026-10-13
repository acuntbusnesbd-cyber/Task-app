package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.ClassRoutineItem
import com.example.data.model.RoutineDay
import com.example.data.model.RoutineSection
import com.example.data.model.TeacherInfo
import com.example.data.repository.RoutineRepository
import org.json.JSONArray
import org.json.JSONObject

data class DeveloperInfo(
  val name: String = "Tanzim Khan",
  val role: String = "Lead App Developer & Electrical Engineer",
  val institute: String = "Chattogram Govt. Polytechnic Institute",
  val department: String = "Electrical Technology • 1st Shift",
  val email: String = "rktanzimkhanpro@gmail.com",
  val phone: String = "01572911214",
  val github: String = "https://github.com/rktanzimkhanpro",
  val bio: String = "Developed for Electrical Technology students of Chattogram Government Polytechnic Institute. All classes, teachers, and timings can be configured dynamically.",
  val announcement: String = "📢 Welcome to CGPI Class Routine App 2026! Check today's schedule and upcoming notification alerts.",
  val showAnnouncement: Boolean = true
)

object AdminManager {

  const val ADMIN_PASSWORD = "272426"
  private const val PREFS_ADMIN = "cgpi_admin_prefs"

  private const val KEY_DEV_NAME = "dev_name"
  private const val KEY_DEV_ROLE = "dev_role"
  private const val KEY_DEV_INSTITUTE = "dev_institute"
  private const val KEY_DEV_DEPT = "dev_dept"
  private const val KEY_DEV_EMAIL = "dev_email"
  private const val KEY_DEV_PHONE = "dev_phone"
  private const val KEY_DEV_GITHUB = "dev_github"
  private const val KEY_DEV_BIO = "dev_bio"
  private const val KEY_DEV_ANNOUNCEMENT = "dev_announcement"
  private const val KEY_DEV_SHOW_ANNOUNCEMENT = "dev_show_announcement"

  private const val KEY_TEACHERS_JSON = "teachers_custom_json"
  private const val KEY_ROUTINE_OVERRIDES_JSON = "routine_overrides_json"

  private fun getPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences(PREFS_ADMIN, Context.MODE_PRIVATE)
  }

  fun checkPassword(input: String): Boolean {
    return input.trim() == ADMIN_PASSWORD
  }

  fun getDeveloperInfo(context: Context): DeveloperInfo {
    val prefs = getPrefs(context)
    val default = DeveloperInfo()
    return DeveloperInfo(
      name = prefs.getString(KEY_DEV_NAME, default.name) ?: default.name,
      role = prefs.getString(KEY_DEV_ROLE, default.role) ?: default.role,
      institute = prefs.getString(KEY_DEV_INSTITUTE, default.institute) ?: default.institute,
      department = prefs.getString(KEY_DEV_DEPT, default.department) ?: default.department,
      email = prefs.getString(KEY_DEV_EMAIL, default.email) ?: default.email,
      phone = prefs.getString(KEY_DEV_PHONE, default.phone) ?: default.phone,
      github = prefs.getString(KEY_DEV_GITHUB, default.github) ?: default.github,
      bio = prefs.getString(KEY_DEV_BIO, default.bio) ?: default.bio,
      announcement = prefs.getString(KEY_DEV_ANNOUNCEMENT, default.announcement) ?: default.announcement,
      showAnnouncement = prefs.getBoolean(KEY_DEV_SHOW_ANNOUNCEMENT, default.showAnnouncement)
    )
  }

  fun saveDeveloperInfo(context: Context, info: DeveloperInfo) {
    getPrefs(context).edit().apply {
      putString(KEY_DEV_NAME, info.name)
      putString(KEY_DEV_ROLE, info.role)
      putString(KEY_DEV_INSTITUTE, info.institute)
      putString(KEY_DEV_DEPT, info.department)
      putString(KEY_DEV_EMAIL, info.email)
      putString(KEY_DEV_PHONE, info.phone)
      putString(KEY_DEV_GITHUB, info.github)
      putString(KEY_DEV_BIO, info.bio)
      putString(KEY_DEV_ANNOUNCEMENT, info.announcement)
      putBoolean(KEY_DEV_SHOW_ANNOUNCEMENT, info.showAnnouncement)
      apply()
    }
  }

  fun getTeachers(context: Context): List<TeacherInfo> {
    val prefs = getPrefs(context)
    val jsonString = prefs.getString(KEY_TEACHERS_JSON, null)
    if (jsonString.isNullOrBlank()) {
      return RoutineRepository.teachers
    }

    return try {
      val jsonArray = JSONArray(jsonString)
      val list = mutableListOf<TeacherInfo>()
      for (i in 0 until jsonArray.length()) {
        val obj = jsonArray.getJSONObject(i)
        list.add(
          TeacherInfo(
            id = obj.getString("id"),
            name = obj.getString("name"),
            shortCode = obj.getString("shortCode"),
            phone = obj.getString("phone"),
            subject = obj.getString("subject")
          )
        )
      }
      if (list.isEmpty()) RoutineRepository.teachers else list
    } catch (e: Exception) {
      RoutineRepository.teachers
    }
  }

  fun saveTeacher(context: Context, updated: TeacherInfo) {
    val current = getTeachers(context).toMutableList()
    val index = current.indexOfFirst { it.id == updated.id }
    if (index >= 0) {
      current[index] = updated
    } else {
      current.add(updated)
    }

    val array = JSONArray()
    for (t in current) {
      val obj = JSONObject().apply {
        put("id", t.id)
        put("name", t.name)
        put("shortCode", t.shortCode)
        put("phone", t.phone)
        put("subject", t.subject)
      }
      array.put(obj)
    }
    getPrefs(context).edit().putString(KEY_TEACHERS_JSON, array.toString()).apply()
  }

  fun getRoutineList(context: Context): List<ClassRoutineItem> {
    val prefs = getPrefs(context)
    val jsonString = prefs.getString(KEY_ROUTINE_OVERRIDES_JSON, null) ?: return RoutineRepository.routineList

    return try {
      val root = JSONObject(jsonString)
      RoutineRepository.routineList.map { original ->
        if (root.has(original.id)) {
          val obj = root.getJSONObject(original.id)
          original.copy(
            subjectName = obj.optString("subjectName", original.subjectName),
            roomNo = obj.optString("roomNo", original.roomNo),
            teacherName = obj.optString("teacherName", original.teacherName),
            teacherCode = obj.optString("teacherCode", original.teacherCode),
            teacherPhone = obj.optString("teacherPhone", original.teacherPhone),
            isFreePeriod = obj.optBoolean("isFreePeriod", original.isFreePeriod)
          )
        } else {
          original
        }
      }
    } catch (e: Exception) {
      RoutineRepository.routineList
    }
  }

  fun saveClassItem(context: Context, item: ClassRoutineItem) {
    val prefs = getPrefs(context)
    val jsonString = prefs.getString(KEY_ROUTINE_OVERRIDES_JSON, null)
    val root = if (!jsonString.isNullOrBlank()) JSONObject(jsonString) else JSONObject()

    val obj = JSONObject().apply {
      put("subjectName", item.subjectName)
      put("roomNo", item.roomNo)
      put("teacherName", item.teacherName)
      put("teacherCode", item.teacherCode)
      put("teacherPhone", item.teacherPhone)
      put("isFreePeriod", item.isFreePeriod)
    }
    root.put(item.id, obj)
    prefs.edit().putString(KEY_ROUTINE_OVERRIDES_JSON, root.toString()).apply()
  }

  fun resetAllToDefault(context: Context) {
    getPrefs(context).edit().clear().apply()
  }
}
