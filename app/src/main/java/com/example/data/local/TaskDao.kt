package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

  @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, dueDate ASC, id DESC")
  fun getAllTasksFlow(): Flow<List<TaskEntity>>

  @Query("SELECT * FROM tasks WHERE section = :section OR section = 'ALL' ORDER BY isCompleted ASC, dueDate ASC")
  fun getTasksBySectionFlow(section: String): Flow<List<TaskEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTask(task: TaskEntity): Long

  @Update
  suspend fun updateTask(task: TaskEntity)

  @Delete
  suspend fun deleteTask(task: TaskEntity)

  @Query("UPDATE tasks SET isCompleted = :completed WHERE id = :taskId")
  suspend fun toggleTaskCompletion(taskId: Long, completed: Boolean)
}
