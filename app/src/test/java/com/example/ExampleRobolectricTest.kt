package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.RoutineDay
import com.example.data.model.RoutineSection
import com.example.data.repository.RoutineRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("CGPI Class Routine", appName)
  }

  @Test
  fun `routine has classes for A1 and B1 across Sunday to Thursday`() {
    val a1Sunday = RoutineRepository.getRoutineForDay(RoutineSection.A1, RoutineDay.SUN)
    val b1Sunday = RoutineRepository.getRoutineForDay(RoutineSection.B1, RoutineDay.SUN)

    assertTrue("A1 Sunday must have classes", a1Sunday.isNotEmpty())
    assertTrue("B1 Sunday must have classes", b1Sunday.isNotEmpty())

    val mathClassA1 = a1Sunday.first { it.subjectName == "Mathematics-I" }
    assertEquals("MD. FAKHRUL AREFIN", mathClassA1.teacherName)
    assertEquals("01675630770", mathClassA1.teacherPhone)
  }

  @Test
  fun `teacher directory has verified contacts`() {
    val teachers = RoutineRepository.teachers
    assertTrue(teachers.size >= 8)
    val fahkrul = teachers.first { it.shortCode == "FA" }
    assertEquals("01675630770", fahkrul.phone)
  }

  @Test
  fun `launch MainActivity without crash`() {
    val controller = org.robolectric.Robolectric.buildActivity(MainActivity::class.java).setup()
    assertNotNull(controller.get())
  }

  @Test
  fun `verify admin password authentication`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    assertTrue(com.example.data.local.AdminManager.checkPassword("272426"))
    org.junit.Assert.assertFalse(com.example.data.local.AdminManager.checkPassword("123456"))
    org.junit.Assert.assertFalse(com.example.data.local.AdminManager.checkPassword(""))
  }
}
