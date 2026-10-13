package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClassRoutineItem
import com.example.ui.components.ClassRoutineCard
import com.example.ui.components.DayChipsRow
import com.example.ui.components.LiveStatusCard
import com.example.ui.components.SectionSelectorPill
import com.example.ui.dialogs.AddTaskDialog
import com.example.ui.dialogs.SettingsDialog
import com.example.viewmodel.RoutineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
  viewModel: RoutineViewModel,
  modifier: Modifier = Modifier
) {
  val selectedSection by viewModel.selectedSection.collectAsState()
  val selectedDay by viewModel.selectedDay.collectAsState()
  val currentMinutes by viewModel.currentMinutes.collectAsState()
  val remindersEnabled by viewModel.remindersEnabled.collectAsState()
  val minutesBefore by viewModel.minutesBefore.collectAsState()
  val developerInfo by viewModel.developerInfo.collectAsState()

  val (activeClass, nextClass) = viewModel.getCurrentAndNextClass(selectedSection)
  val dayClasses = viewModel.getRoutineForDay(selectedSection, selectedDay)

  var showSettingsDialog by remember { mutableStateOf(false) }
  var taskTargetClass by remember { mutableStateOf<ClassRoutineItem?>(null) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "CGPI Class Routine",
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            )
            Text(
              text = "Electrical Technology • 1st Shift",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        },
        actions = {
          IconButton(
            onClick = { showSettingsDialog = true },
            modifier = Modifier.testTag("open_settings_button")
          ) {
            Icon(
              imageVector = Icons.Default.NotificationsActive,
              contentDescription = "Notifications & Settings",
              tint = if (remindersEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    modifier = modifier
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Section Toggle Pill (A1 vs B1)
      item {
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Alignment.Center
        ) {
          SectionSelectorPill(
            selectedSection = selectedSection,
            onSectionSelected = { viewModel.setSection(it) }
          )
        }
      }

      // Day Chips Row (SUN - THU)
      item {
        DayChipsRow(
          selectedDay = selectedDay,
          onDaySelected = { viewModel.setDay(it) },
          isToday = { viewModel.isToday(it) }
        )
      }

      // Live Status (Active / Next Class Countdown)
      item {
        LiveStatusCard(
          activeClass = activeClass,
          nextClass = nextClass,
          currentMinutes = currentMinutes
        )
      }

      if (developerInfo.showAnnouncement && developerInfo.announcement.isNotBlank()) {
        item {
          androidx.compose.material3.Card(
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            )
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "📢", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = developerInfo.announcement,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }

      // Day Header & Count
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${selectedDay.fullName}'s Schedule",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          val totalClassesCount = dayClasses.count { !it.isFreePeriod }
          Text(
            text = "$totalClassesCount Classes Scheduled",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // List of classes for selected day
      items(dayClasses, key = { it.id }) { item ->
        val isCurrent = item.isCurrentlyHappening(selectedDay, currentMinutes)
        ClassRoutineCard(
          item = item,
          isCurrent = isCurrent,
          onAddTaskClick = { taskTargetClass = it }
        )
      }

      item {
        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }

  // Add Task Dialog
  taskTargetClass?.let { cls ->
    AddTaskDialog(
      initialSubject = cls.subjectName,
      onDismiss = { taskTargetClass = null },
      onConfirm = { title, subject, notes, dueDate, priority ->
        viewModel.addTask(title, subject, notes, dueDate, priority)
        taskTargetClass = null
      }
    )
  }

  // Settings & Notification Dialog
  if (showSettingsDialog) {
    SettingsDialog(
      remindersEnabled = remindersEnabled,
      onRemindersEnabledChange = { viewModel.setRemindersEnabled(it) },
      minutesBefore = minutesBefore,
      onMinutesBeforeChange = { viewModel.setMinutesBefore(it) },
      selectedSection = selectedSection,
      onTestNotification = { viewModel.triggerTestNotification() },
      onDismiss = { showSettingsDialog = false }
    )
  }
}
