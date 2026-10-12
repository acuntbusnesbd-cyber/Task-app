package com.example.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
  initialSubject: String = "Mathematics-I",
  onDismiss: () -> Unit,
  onConfirm: (title: String, subject: String, notes: String, dueDate: Long, priority: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var subject by remember { mutableStateOf(initialSubject) }
  var notes by remember { mutableStateOf("") }
  var priority by remember { mutableStateOf("MEDIUM") }
  var dueDateMillis by remember {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, 1)
    cal.set(Calendar.HOUR_OF_DAY, 8)
    cal.set(Calendar.MINUTE, 0)
    mutableLongStateOf(cal.timeInMillis)
  }

  val subjectsList = listOf(
    "Mathematics-I",
    "Basic Electricity",
    "Physics-I",
    "Bangla-I",
    "English-I",
    "Basic Workshop Practice",
    "Physical Education & Life Skills",
    "Electrical Engineering Materials",
    "General / Exam Preparation"
  )

  var subjectExpanded by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Add Class Task / Homework",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Subject Dropdown
        ExposedDropdownMenuBox(
          expanded = subjectExpanded,
          onExpandedChange = { subjectExpanded = it }
        ) {
          OutlinedTextField(
            value = subject,
            onValueChange = {},
            readOnly = true,
            label = { Text("Subject") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectExpanded) },
            modifier = Modifier
              .menuAnchor()
              .fillMaxWidth()
              .testTag("task_subject_dropdown")
          )
          ExposedDropdownMenu(
            expanded = subjectExpanded,
            onDismissRequest = { subjectExpanded = false }
          ) {
            subjectsList.forEach { s ->
              DropdownMenuItem(
                text = { Text(s) },
                onClick = {
                  subject = s
                  subjectExpanded = false
                }
              )
            }
          }
        }

        // Title Input
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Task Title (e.g. Lab Report 1, Math Ex 3.2)") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("task_title_input")
        )

        // Notes Input
        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("Notes / Details (Optional)") },
          maxLines = 3,
          modifier = Modifier.fillMaxWidth()
        )

        // Due Day Options
        Text(
          text = "Due Date",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
          val todayCal = Calendar.getInstance()
          val tomorrowCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }

          FilterChip(
            selected = isSameDay(dueDateMillis, todayCal.timeInMillis),
            onClick = {
              val cal = Calendar.getInstance()
              cal.set(Calendar.HOUR_OF_DAY, 20)
              dueDateMillis = cal.timeInMillis
            },
            label = { Text("Today") }
          )

          FilterChip(
            selected = isSameDay(dueDateMillis, tomorrowCal.timeInMillis),
            onClick = {
              val cal = Calendar.getInstance()
              cal.add(Calendar.DAY_OF_YEAR, 1)
              cal.set(Calendar.HOUR_OF_DAY, 8)
              dueDateMillis = cal.timeInMillis
            },
            label = { Text("Tomorrow") }
          )

          FilterChip(
            selected = !isSameDay(dueDateMillis, todayCal.timeInMillis) && !isSameDay(dueDateMillis, tomorrowCal.timeInMillis),
            onClick = {
              val cal = Calendar.getInstance()
              cal.add(Calendar.DAY_OF_YEAR, 3)
              dueDateMillis = cal.timeInMillis
            },
            label = { Text("Next Class") }
          )
        }

        // Priority
        Text(
          text = "Priority",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("HIGH", "MEDIUM", "LOW").forEach { p ->
            FilterChip(
              selected = priority == p,
              onClick = { priority = p },
              label = { Text(p) }
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            onConfirm(title, subject, notes, dueDateMillis, priority)
          }
        },
        enabled = title.isNotBlank(),
        modifier = Modifier.testTag("save_task_button")
      ) {
        Text("Save Task")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

private fun isSameDay(time1: Long, time2: Long): Boolean {
  val cal1 = Calendar.getInstance().apply { timeInMillis = time1 }
  val cal2 = Calendar.getInstance().apply { timeInMillis = time2 }
  return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
