package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TaskEntity
import com.example.ui.dialogs.AddTaskDialog
import com.example.viewmodel.RoutineViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
  viewModel: RoutineViewModel,
  modifier: Modifier = Modifier
) {
  val tasks by viewModel.tasks.collectAsState()
  var filter by remember { mutableStateOf("ALL") } // ALL, PENDING, COMPLETED
  var showAddTaskDialog by remember { mutableStateOf(false) }

  val filteredTasks = when (filter) {
    "PENDING" -> tasks.filter { !it.isCompleted }
    "COMPLETED" -> tasks.filter { it.isCompleted }
    else -> tasks
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Class Tasks & Homework",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAddTaskDialog = true },
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.testTag("add_task_fab")
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add Task"
        )
      }
    },
    modifier = modifier
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // Filter Chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf("ALL" to "All (${tasks.size})", "PENDING" to "Pending (${tasks.count { !it.isCompleted }})", "COMPLETED" to "Completed (${tasks.count { it.isCompleted }})").forEach { (key, label) ->
          FilterChip(
            selected = filter == key,
            onClick = { filter = key },
            label = { Text(label, fontSize = 12.sp) }
          )
        }
      }

      if (filteredTasks.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.Assignment,
              contentDescription = null,
              modifier = Modifier.size(64.dp),
              tint = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = if (filter == "COMPLETED") "No completed tasks yet" else "No tasks added yet",
              fontWeight = FontWeight.SemiBold,
              fontSize = 16.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "Tap + below to add homework, assignments, or lab reports",
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.outline
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(filteredTasks, key = { it.id }) { task ->
            TaskItemCard(
              task = task,
              onToggle = { viewModel.toggleTaskCompletion(task) },
              onDelete = { viewModel.deleteTask(task) }
            )
          }

          item {
            Spacer(modifier = Modifier.height(60.dp))
          }
        }
      }
    }
  }

  if (showAddTaskDialog) {
    AddTaskDialog(
      onDismiss = { showAddTaskDialog = false },
      onConfirm = { title, subject, notes, dueDate, priority ->
        viewModel.addTask(title, subject, notes, dueDate, priority)
        showAddTaskDialog = false
      }
    )
  }
}

@Composable
fun TaskItemCard(
  task: TaskEntity,
  onToggle: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  val dateFormat = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
  val dueDateString = dateFormat.format(Date(task.dueDate))

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (task.isCompleted) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
      } else {
        MaterialTheme.colorScheme.surface
      }
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = if (task.isCompleted) 0.dp else 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.Top
    ) {
      // Complete toggle icon
      IconButton(
        onClick = onToggle,
        modifier = Modifier.size(28.dp).padding(top = 2.dp)
      ) {
        Icon(
          imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
          contentDescription = "Toggle completion",
          tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = task.title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = if (task.isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
          )

          // Priority badge
          val (pBg, pText) = when (task.priority) {
            "HIGH" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
            "LOW" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
            else -> Color(0xFFFFF3E0) to Color(0xFFEF6C00)
          }
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = pBg
          ) {
            Text(
              text = task.priority,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = pText,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Text(
          text = "📖 ${task.subjectName} • Due: $dueDateString",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 4.dp)
        )

        if (task.notes.isNotBlank()) {
          Text(
            text = task.notes,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 4.dp)
          )
        }
      }

      IconButton(
        onClick = onDelete,
        modifier = Modifier.size(28.dp).padding(top = 2.dp)
      ) {
        Icon(
          imageVector = Icons.Default.DeleteOutline,
          contentDescription = "Delete Task",
          tint = MaterialTheme.colorScheme.outline
        )
      }
    }
  }
}
