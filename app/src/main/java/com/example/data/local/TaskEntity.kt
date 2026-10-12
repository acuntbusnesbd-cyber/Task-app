package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val subjectName: String,
  val notes: String = "",
  val dueDate: Long, // timestamp in millis
  val section: String, // "A1" or "B1" or "ALL"
  val isCompleted: Boolean = false,
  val priority: String = "MEDIUM", // HIGH, MEDIUM, LOW
  val createdAt: Long = System.currentTimeMillis()
)
