package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClassRoutineItem
import com.example.data.model.RoutineDay
import com.example.data.model.RoutineSection

@Composable
fun SectionSelectorPill(
  selectedSection: RoutineSection,
  onSectionSelected: (RoutineSection) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .clip(RoundedCornerShape(24.dp))
      .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp)),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
  ) {
    Row(
      modifier = Modifier.padding(4.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      RoutineSection.entries.forEach { section ->
        val isSelected = section == selectedSection
        val bg by animateColorAsState(
          if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
          label = "section_bg"
        )
        val textColor by animateColorAsState(
          if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
          label = "section_text"
        )

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .clickable { onSectionSelected(section) }
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .testTag("section_pill_${section.code}"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = section.displayName,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 13.sp
          )
        }
      }
    }
  }
}

@Composable
fun DayChipsRow(
  selectedDay: RoutineDay,
  onDaySelected: (RoutineDay) -> Unit,
  isToday: (RoutineDay) -> Boolean,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    RoutineDay.entries.forEach { day ->
      val selected = day == selectedDay
      val today = isToday(day)

      FilterChip(
        selected = selected,
        onClick = { onDaySelected(day) },
        label = {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 2.dp)
          ) {
            Text(
              text = day.shortName,
              fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
              fontSize = 13.sp
            )
            if (today) {
              Box(
                modifier = Modifier
                  .size(4.dp)
                  .clip(CircleShape)
                  .background(if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
              )
            }
          }
        },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = MaterialTheme.colorScheme.primary,
          selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("day_chip_${day.shortName}")
      )
    }
  }
}

@Composable
fun LiveStatusCard(
  activeClass: ClassRoutineItem?,
  nextClass: ClassRoutineItem?,
  currentMinutes: Int,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (activeClass != null) {
        MaterialTheme.colorScheme.primaryContainer
      } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
      }
    )
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      if (activeClass != null) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(Color(0xFF10B981))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "NOW IN SESSION",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary,
              letterSpacing = 1.sp
            )
          }
          Text(
            text = "${activeClass.startTime} - ${activeClass.endTime}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = activeClass.subjectName,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Row(
          modifier = Modifier.padding(top = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "🏢 ${activeClass.roomNo}",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
          )
          Text(
            text = "👨‍🏫 ${activeClass.teacherName} (${activeClass.teacherCode})",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
          )
        }

        val totalDuration = (activeClass.endMinutes - activeClass.startMinutes).coerceAtLeast(1)
        val elapsed = (currentMinutes - activeClass.startMinutes).coerceIn(0, totalDuration)
        val progress = elapsed.toFloat() / totalDuration.toFloat()
        val remaining = (activeClass.endMinutes - currentMinutes).coerceAtLeast(0)

        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = MaterialTheme.colorScheme.primary,
          trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )
        Text(
          text = "$remaining mins remaining",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
          modifier = Modifier
            .align(Alignment.End)
            .padding(top = 4.dp)
        )

      } else if (nextClass != null) {
        val minutesUntil = (nextClass.startMinutes - currentMinutes).coerceAtLeast(0)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.HourglassTop,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "NEXT CLASS",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary,
              letterSpacing = 1.sp
            )
          }
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
          ) {
            Text(
              text = "Starts in $minutesUntil mins",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = nextClass.subjectName,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "${nextClass.startTime} - ${nextClass.endTime} • ${nextClass.roomNo} • ${nextClass.teacherName}",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 2.dp)
        )

      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Schedule,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSecondaryContainer,
              modifier = Modifier.size(20.dp)
            )
          }
          Column {
            Text(
              text = "No Ongoing or Remaining Classes",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "All classes finished or today is a break. Have a great day!",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

@Composable
fun ClassRoutineCard(
  item: ClassRoutineItem,
  isCurrent: Boolean,
  onAddTaskClick: (ClassRoutineItem) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  if (item.isFreePeriod) {
    Card(
      modifier = modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
      )
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Free Period (${item.periodLabel})",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "${item.startTime} - ${item.endTime}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outline
          )
        }
        Text(
          text = "BREAK",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.outline,
          letterSpacing = 1.sp
        )
      }
    }
    return
  }

  val cardBorder = if (isCurrent) {
    Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
  } else {
    Modifier
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .then(cardBorder)
      .testTag("class_card_${item.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrent) 4.dp else 1.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Time & Period Badge
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(item.colorHex).copy(alpha = 0.15f)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Schedule,
              contentDescription = null,
              tint = Color(item.colorHex),
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${item.startTime} - ${item.endTime} (${item.periodLabel})",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(item.colorHex)
            )
          }
        }

        // Room Badge
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = item.roomNo,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Subject Name
      Text(
        text = item.subjectName,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      // Teacher Info
      Row(
        modifier = Modifier.padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (item.teacherCode.isNotBlank()) {
            "${item.teacherName} (${item.teacherCode})"
          } else {
            item.teacherName
          },
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      // Teacher phone contact and actions
      Spacer(modifier = Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (item.teacherPhone.isNotBlank()) {
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalButton(
              onClick = { dialPhone(context, item.teacherPhone) },
              modifier = Modifier.height(34.dp).testTag("call_btn_${item.id}"),
              contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call",
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "Call", fontSize = 12.sp)
            }

            OutlinedButton(
              onClick = { sendSms(context, item.teacherPhone) },
              modifier = Modifier.height(34.dp).testTag("sms_btn_${item.id}"),
              contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Message,
                contentDescription = "SMS",
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "SMS", fontSize = 12.sp)
            }
          }
        } else {
          Text(
            text = "Instructor ET Dept.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.outline
          )
        }

        // Add Task for this class
        OutlinedButton(
          onClick = { onAddTaskClick(item) },
          modifier = Modifier.height(34.dp).testTag("add_task_btn_${item.id}"),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Task",
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "Task", fontSize = 12.sp)
        }
      }
    }
  }
}

fun dialPhone(context: Context, phone: String) {
  try {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open dialer: $phone", Toast.LENGTH_SHORT).show()
  }
}

fun sendSms(context: Context, phone: String) {
  try {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phone"))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open SMS: $phone", Toast.LENGTH_SHORT).show()
  }
}
