package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DeveloperInfo
import com.example.data.model.ClassRoutineItem
import com.example.data.model.RoutineDay
import com.example.data.model.RoutineSection
import com.example.data.model.TeacherInfo
import com.example.ui.components.dialPhone
import com.example.viewmodel.RoutineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperScreen(
  viewModel: RoutineViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val developerInfo by viewModel.developerInfo.collectAsState()
  val isAdminUnlocked by viewModel.isAdminUnlocked.collectAsState()

  var showLoginDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (isAdminUnlocked) "Admin Control Center" else "Developer & App Info",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        actions = {
          if (!isAdminUnlocked) {
            FilledTonalButton(
              onClick = { showLoginDialog = true },
              modifier = Modifier.padding(end = 8.dp).testTag("admin_login_btn"),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("Admin Login", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          } else {
            Button(
              onClick = { viewModel.lockAdmin() },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
              modifier = Modifier.padding(end = 8.dp).testTag("admin_logout_btn"),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.LockOpen,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Lock Admin", fontSize = 12.sp)
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    modifier = modifier
  ) { paddingValues ->
    if (isAdminUnlocked) {
      AdminDashboardView(
        viewModel = viewModel,
        modifier = Modifier.padding(paddingValues)
      )
    } else {
      PublicDeveloperView(
        developerInfo = developerInfo,
        onUnlockAdminClick = { showLoginDialog = true },
        modifier = Modifier.padding(paddingValues)
      )
    }
  }

  if (showLoginDialog) {
    AdminPasswordDialog(
      onDismiss = { showLoginDialog = false },
      onConfirm = { password ->
        if (viewModel.unlockAdmin(password)) {
          showLoginDialog = false
          Toast.makeText(context, "Admin Unlocked Successfully!", Toast.LENGTH_SHORT).show()
        } else {
          Toast.makeText(context, "Incorrect Password! Try again.", Toast.LENGTH_LONG).show()
        }
      }
    )
  }
}

@Composable
fun PublicDeveloperView(
  developerInfo: DeveloperInfo,
  onUnlockAdminClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Developer Profile Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(44.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = developerInfo.name,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = developerInfo.role,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
          )

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "${developerInfo.department} • ${developerInfo.institute}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = developerInfo.bio,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Action row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            if (developerInfo.phone.isNotBlank()) {
              FilledTonalButton(
                onClick = { dialPhone(context, developerInfo.phone) },
                modifier = Modifier.weight(1f)
              ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Call", fontSize = 12.sp)
              }
            }

            if (developerInfo.email.isNotBlank()) {
              OutlinedButton(
                onClick = {
                  val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${developerInfo.email}"))
                  try { context.startActivity(intent) } catch (_: Exception) {}
                },
                modifier = Modifier.weight(1f)
              ) {
                Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Email", fontSize = 12.sp)
              }
            }

            IconButton(
              onClick = {
                val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clip.setPrimaryClip(ClipData.newPlainText("Developer Contact", "${developerInfo.name}: ${developerInfo.phone} (${developerInfo.email})"))
                Toast.makeText(context, "Contact info copied!", Toast.LENGTH_SHORT).show()
              }
            ) {
              Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy")
            }
          }
        }
      }
    }

    // Announcement / Notice Card
    if (developerInfo.showAnnouncement && developerInfo.announcement.isNotBlank()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSecondaryContainer,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Department Notice",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
              )
              Text(
                text = developerInfo.announcement,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.9f)
              )
            }
          }
        }
      }
    }

    // App & Institute Specs Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(text = "App & Institution Details", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          Text(text = "🏛 Institute: Chattogram Government Polytechnic Institute", fontSize = 13.sp)
          Text(text = "⚡ Department: Electrical Technology (1st Shift)", fontSize = 13.sp)
          Text(text = "📅 Routine: 2026 Academic Calendar (Total Load: 63)", fontSize = 13.sp)
          Text(text = "🛠 Built with: Kotlin, Jetpack Compose, Material Design 3, Room DB", fontSize = 13.sp)
          Text(text = "🔐 Password-Protected Admin Control System Included", fontSize = 13.sp)
        }
      }
    }

    // Admin unlock button banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onUnlockAdminClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AdminPanelSettings,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Admin Control Panel",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Text(
                text = "Authorized access for schedule, teachers & info management",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
              )
            }
          }
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardView(
  viewModel: RoutineViewModel,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("Developer & Notice", "Teachers", "Class Routine", "Reset")

  Column(modifier = modifier.fillMaxSize()) {
    PrimaryTabRow(
      selectedTabIndex = selectedTab,
      modifier = Modifier.fillMaxWidth()
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = { Text(title, fontSize = 12.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
        )
      }
    }

    when (selectedTab) {
      0 -> AdminEditDeveloperTab(viewModel = viewModel)
      1 -> AdminEditTeachersTab(viewModel = viewModel)
      2 -> AdminEditRoutineTab(viewModel = viewModel)
      3 -> AdminResetTab(viewModel = viewModel)
    }
  }
}

@Composable
fun AdminEditDeveloperTab(viewModel: RoutineViewModel) {
  val current by viewModel.developerInfo.collectAsState()
  val context = LocalContext.current

  var name by remember(current) { mutableStateOf(current.name) }
  var role by remember(current) { mutableStateOf(current.role) }
  var institute by remember(current) { mutableStateOf(current.institute) }
  var department by remember(current) { mutableStateOf(current.department) }
  var email by remember(current) { mutableStateOf(current.email) }
  var phone by remember(current) { mutableStateOf(current.phone) }
  var github by remember(current) { mutableStateOf(current.github) }
  var bio by remember(current) { mutableStateOf(current.bio) }
  var announcement by remember(current) { mutableStateOf(current.announcement) }
  var showAnnouncement by remember(current) { mutableStateOf(current.showAnnouncement) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(text = "Edit Developer & Notice Information", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }

    item {
      OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Developer Name") }, modifier = Modifier.fillMaxWidth())
    }
    item {
      OutlinedTextField(value = role, onValueChange = { role = it }, label = { Text("Role / Designation") }, modifier = Modifier.fillMaxWidth())
    }
    item {
      OutlinedTextField(value = institute, onValueChange = { institute = it }, label = { Text("Institute") }, modifier = Modifier.fillMaxWidth())
    }
    item {
      OutlinedTextField(value = department, onValueChange = { department = it }, label = { Text("Department / Shift") }, modifier = Modifier.fillMaxWidth())
    }
    item {
      OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Contact Email") }, modifier = Modifier.fillMaxWidth())
    }
    item {
      OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Contact Phone") }, modifier = Modifier.fillMaxWidth())
    }
    item {
      OutlinedTextField(value = github, onValueChange = { github = it }, label = { Text("GitHub / Portfolio URL") }, modifier = Modifier.fillMaxWidth())
    }
    item {
      OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Developer Bio") }, maxLines = 4, modifier = Modifier.fillMaxWidth())
    }
    item {
      OutlinedTextField(value = announcement, onValueChange = { announcement = it }, label = { Text("Department Notice / Announcement") }, maxLines = 3, modifier = Modifier.fillMaxWidth())
    }
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Show Department Notice", fontSize = 14.sp)
        Switch(checked = showAnnouncement, onCheckedChange = { showAnnouncement = it })
      }
    }

    item {
      Button(
        onClick = {
          viewModel.updateDeveloperInfo(
            DeveloperInfo(
              name = name,
              role = role,
              institute = institute,
              department = department,
              email = email,
              phone = phone,
              github = github,
              bio = bio,
              announcement = announcement,
              showAnnouncement = showAnnouncement
            )
          )
          Toast.makeText(context, "Developer & Notice Info Updated!", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier.fillMaxWidth().testTag("save_dev_info_btn")
      ) {
        Icon(imageVector = Icons.Default.Check, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Save Developer & Notice Changes")
      }
    }
  }
}

@Composable
fun AdminEditTeachersTab(viewModel: RoutineViewModel) {
  val teachers by viewModel.teachers.collectAsState()
  var editingTeacher by remember { mutableStateOf<TeacherInfo?>(null) }
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(
        text = "Manage Faculty Directory (Tap Edit to Change)",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
      )
    }

    items(teachers, key = { it.id }) { teacher ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Row(
          modifier = Modifier.padding(14.dp).fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(text = teacher.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(text = "${teacher.subject} • Code: ${teacher.shortCode}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = "📱 ${if (teacher.phone.isNotBlank()) teacher.phone else "No number"}", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
          }

          FilledTonalButton(
            onClick = { editingTeacher = teacher },
            modifier = Modifier.height(34.dp)
          ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Edit", fontSize = 12.sp)
          }
        }
      }
    }
  }

  editingTeacher?.let { teacher ->
    var name by remember { mutableStateOf(teacher.name) }
    var code by remember { mutableStateOf(teacher.shortCode) }
    var phone by remember { mutableStateOf(teacher.phone) }
    var subject by remember { mutableStateOf(teacher.subject) }

    AlertDialog(
      onDismissRequest = { editingTeacher = null },
      title = { Text("Edit Teacher Info") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Teacher Name") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Short Code (e.g. FA)") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Mobile Number") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject") }, modifier = Modifier.fillMaxWidth())
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.updateTeacher(
              teacher.copy(name = name.trim(), shortCode = code.trim(), phone = phone.trim(), subject = subject.trim())
            )
            editingTeacher = null
            Toast.makeText(context, "Teacher Updated!", Toast.LENGTH_SHORT).show()
          }
        ) {
          Text("Save")
        }
      },
      dismissButton = {
        TextButton(onClick = { editingTeacher = null }) { Text("Cancel") }
      }
    )
  }
}

@Composable
fun AdminEditRoutineTab(viewModel: RoutineViewModel) {
  val section by viewModel.selectedSection.collectAsState()
  val day by viewModel.selectedDay.collectAsState()
  val routine = viewModel.getRoutineForDay(section, day)
  val context = LocalContext.current

  var editingClass by remember { mutableStateOf<ClassRoutineItem?>(null) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(text = "Modify Class Routine (${section.code} - ${day.fullName})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }

    items(routine, key = { it.id }) { item ->
      Card(
        modifier = Modifier.fillMaxWidth().clickable { editingClass = item },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Row(
          modifier = Modifier.padding(14.dp).fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (item.isFreePeriod) "FREE PERIOD (${item.periodLabel})" else "${item.subjectName} (${item.periodLabel})",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
            Text(text = "${item.startTime} - ${item.endTime} • Room: ${item.roomNo}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (!item.isFreePeriod) {
              Text(text = "Teacher: ${item.teacherName} (${item.teacherCode})", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            }
          }

          Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
        }
      }
    }
  }

  editingClass?.let { cls ->
    var subject by remember { mutableStateOf(cls.subjectName) }
    var room by remember { mutableStateOf(cls.roomNo) }
    var teacherName by remember { mutableStateOf(cls.teacherName) }
    var teacherPhone by remember { mutableStateOf(cls.teacherPhone) }
    var isFree by remember { mutableStateOf(cls.isFreePeriod) }

    AlertDialog(
      onDismissRequest = { editingClass = null },
      title = { Text("Edit Class: ${cls.periodLabel}") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Mark as Free / Cancelled")
            Switch(checked = isFree, onCheckedChange = { isFree = it })
          }

          if (!isFree) {
            OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = room, onValueChange = { room = it }, label = { Text("Room Number (e.g. Room 324)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = teacherName, onValueChange = { teacherName = it }, label = { Text("Teacher Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = teacherPhone, onValueChange = { teacherPhone = it }, label = { Text("Teacher Phone") }, modifier = Modifier.fillMaxWidth())
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.updateClassItem(
              cls.copy(
                subjectName = if (isFree) "Free Period" else subject.trim(),
                roomNo = if (isFree) "-" else room.trim(),
                teacherName = if (isFree) "-" else teacherName.trim(),
                teacherPhone = if (isFree) "" else teacherPhone.trim(),
                isFreePeriod = isFree
              )
            )
            editingClass = null
            Toast.makeText(context, "Class Updated!", Toast.LENGTH_SHORT).show()
          }
        ) {
          Text("Save")
        }
      },
      dismissButton = {
        TextButton(onClick = { editingClass = null }) { Text("Cancel") }
      }
    )
  }
}

@Composable
fun AdminResetTab(viewModel: RoutineViewModel) {
  val context = LocalContext.current
  var showConfirmDialog by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = Icons.Default.Warning,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.error,
      modifier = Modifier.size(56.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = "Reset All Custom Changes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = "This will restore the original official Chattogram Polytechnic routine, official teacher contacts, and developer info back to default.",
      fontSize = 13.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(24.dp))
    Button(
      onClick = { showConfirmDialog = true },
      colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
    ) {
      Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
      Spacer(modifier = Modifier.width(6.dp))
      Text("Reset Everything to Official Default")
    }
  }

  if (showConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showConfirmDialog = false },
      title = { Text("Confirm Full Reset") },
      text = { Text("Are you sure you want to revert all routine, teacher, and info customizations back to default?") },
      confirmButton = {
        Button(
          onClick = {
            viewModel.resetAllAdminOverrides()
            showConfirmDialog = false
            Toast.makeText(context, "Reset complete!", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Yes, Reset")
        }
      },
      dismissButton = {
        TextButton(onClick = { showConfirmDialog = false }) { Text("Cancel") }
      }
    )
  }
}

@Composable
fun AdminPasswordDialog(
  onDismiss: () -> Unit,
  onConfirm: (String) -> Unit
) {
  var password by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Admin Access", fontWeight = FontWeight.Bold)
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Enter the 6-digit admin password to unlock customization controls:")
        OutlinedTextField(
          value = password,
          onValueChange = { password = it },
          label = { Text("Admin Password") },
          singleLine = true,
          visualTransformation = PasswordVisualTransformation(),
          modifier = Modifier.fillMaxWidth().testTag("admin_password_input")
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirm(password) },
        enabled = password.isNotBlank(),
        modifier = Modifier.testTag("admin_submit_password_btn")
      ) {
        Text("Unlock")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}
