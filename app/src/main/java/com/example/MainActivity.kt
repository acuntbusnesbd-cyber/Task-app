package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ContactPhone
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.core.content.ContextCompat
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.TeacherDirectoryScreen
import com.example.ui.screens.TodayScreen
import com.example.ui.screens.WeeklyRoutineScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.RoutineViewModel

enum class AppNavDestination(
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String
) {
  TODAY("Today", Icons.Filled.Today, Icons.Outlined.Today, "nav_today"),
  TIMETABLE("Timetable", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth, "nav_timetable"),
  TASKS("Tasks", Icons.Filled.Assignment, Icons.Outlined.Assignment, "nav_tasks"),
  TEACHERS("Teachers", Icons.Filled.ContactPhone, Icons.Outlined.ContactPhone, "nav_teachers")
}

class MainActivity : ComponentActivity() {

  private val viewModel: RoutineViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MainApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun MainApp(viewModel: RoutineViewModel) {
  val context = LocalContext.current
  var currentDestination by remember { mutableStateOf(AppNavDestination.TODAY) }

  // Permission launcher for Android 13+ Notification
  val notificationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    // If granted, reminders will alert as scheduled
  }

  LaunchedEffect(Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      if (ContextCompat.checkSelfPermission(
          context,
          Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
      }
    }
  }

  // Handle system back navigation to return to Today tab if elsewhere
  if (currentDestination != AppNavDestination.TODAY) {
    BackHandler {
      currentDestination = AppNavDestination.TODAY
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      NavigationBar {
        AppNavDestination.entries.forEach { destination ->
          val selected = currentDestination == destination
          NavigationBarItem(
            selected = selected,
            onClick = { currentDestination = destination },
            icon = {
              Icon(
                imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                contentDescription = destination.label
              )
            },
            label = { Text(destination.label) },
            modifier = Modifier.testTag(destination.testTag)
          )
        }
      }
    }
  ) { innerPadding ->
    when (currentDestination) {
      AppNavDestination.TODAY -> {
        TodayScreen(
          viewModel = viewModel,
          modifier = Modifier.padding(innerPadding)
        )
      }

      AppNavDestination.TIMETABLE -> {
        WeeklyRoutineScreen(
          viewModel = viewModel,
          modifier = Modifier.padding(innerPadding)
        )
      }

      AppNavDestination.TASKS -> {
        TasksScreen(
          viewModel = viewModel,
          modifier = Modifier.padding(innerPadding)
        )
      }

      AppNavDestination.TEACHERS -> {
        TeacherDirectoryScreen(
          modifier = Modifier.padding(innerPadding)
        )
      }
    }
  }
}
