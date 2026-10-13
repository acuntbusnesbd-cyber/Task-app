package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClassRoutineItem
import com.example.data.model.RoutineDay
import com.example.data.repository.RoutineRepository
import com.example.ui.components.ClassRoutineCard
import com.example.ui.components.SectionSelectorPill
import com.example.viewmodel.RoutineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyRoutineScreen(
  viewModel: RoutineViewModel,
  modifier: Modifier = Modifier
) {
  val selectedSection by viewModel.selectedSection.collectAsState()
  var selectedTabDayIndex by remember { mutableIntStateOf(0) }
  val days = RoutineDay.entries

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Full Class Routine 2026",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    modifier = modifier
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // Section Selector
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
      ) {
        SectionSelectorPill(
          selectedSection = selectedSection,
          onSectionSelected = { viewModel.setSection(it) }
        )
      }

      // Institute Banner Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(
              text = "Chattogram Govt. Polytechnic Institute",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
              text = "Class Started: 11/10/2026 • 7 Periods Daily",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            )
          }
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primary
          ) {
            Text(
              text = "LOAD = 63",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onPrimary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }

      // Day Tabs
      PrimaryTabRow(
        selectedTabIndex = selectedTabDayIndex,
        modifier = Modifier.fillMaxWidth()
      ) {
        days.forEachIndexed { index, day ->
          Tab(
            selected = selectedTabDayIndex == index,
            onClick = { selectedTabDayIndex = index },
            text = {
              Text(
                text = day.shortName,
                fontWeight = if (selectedTabDayIndex == index) FontWeight.Bold else FontWeight.Normal
              )
            }
          )
        }
      }

      val activeDay = days[selectedTabDayIndex]
      val activeClasses = viewModel.getRoutineForDay(selectedSection, activeDay)

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${activeDay.fullName} Schedule",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
            Text(
              text = "${selectedSection.code} Section",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )
          }
        }

        items(activeClasses.size) { index ->
          val item = activeClasses[index]
          ClassRoutineCard(
            item = item,
            isCurrent = false,
            onAddTaskClick = { /* handeled in today screen */ }
          )
        }

        item {
          Spacer(modifier = Modifier.height(30.dp))
        }
      }
    }
  }
}
