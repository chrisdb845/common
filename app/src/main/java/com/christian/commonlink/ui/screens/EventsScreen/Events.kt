package com.christian.commonlink.ui.screens.EventsScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.christian.commonlink.ui.screens.events.Event        // ✅ Fix 1 — import Event model
import com.christian.commonlink.ui.screens.events.EventViewModel
import androidx.compose.foundation.layout.Arrangement
import com.christian.commonlink.navigation.ROUT_ADD_EVENT


// ── Brand palette ────────────────────────────────────────────────
private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val SoftViolet   = Color(0xFF9B6FD4)
private val AccentGold   = Color(0xFFFFD166)
private val CardWhite    = Color(0xFFFFFFFF)
private val SubtitleGray = Color(0xFF888888)
private val BgColor      = Color(0xFFF5F3FB)

// ── Single Event Card ────────────────────────────────────────────
@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit  // ✅ Fix 2 — removed the 's' from Units
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            // Left color accent bar
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(140.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(AccentGold, RoyalPurple)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                // Category tag + organizer row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = AccentGold,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = event.category,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepIndigo
                        )
                    }

                    // Organizer
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Organizer",
                            tint = SubtitleGray,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = event.organizer,
                            fontSize = 11.sp,
                            color = SubtitleGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Event title
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = DeepIndigo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description
                Text(
                    text = event.description,
                    fontSize = 13.sp,
                    color = SubtitleGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Date + Time row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = RoyalPurple,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${event.date} • ${event.time}",
                        fontSize = 12.sp,
                        color = RoyalPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Location row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = SubtitleGray,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = event.location,
                        fontSize = 12.sp,
                        color = SubtitleGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// ── Empty state ──────────────────────────────────────────────────
@Composable
fun EmptyEventsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "📅", fontSize = 56.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Events Yet",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = DeepIndigo
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Be the first to add a community event!",
            fontSize = 14.sp,
            color = SubtitleGray
        )
    }
}

// ── Main Events Screen ───────────────────────────────────────────
@Composable
fun EventsScreen(
    navController: NavController,
    viewModel: EventViewModel = viewModel()
) {

    val events    by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error     by viewModel.errorMessage.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ROUT_ADD_EVENT) },
                containerColor = RoyalPurple,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BgColor)
                .padding(paddingValues)
        ) {

            // ── GRADIENT HEADER ──────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(DeepIndigo, RoyalPurple))
                    )
                    .padding(start = 8.dp, top = 48.dp, end = 20.dp, bottom = 28.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Community Events",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Upcoming events near you",
                            fontSize = 13.sp,
                            color = Color(0xCCFFFFFF)
                        )
                        Box(
                            modifier = Modifier
                                .background(AccentGold, shape = RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${events.size} Events",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepIndigo
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── BODY ─────────────────────────────────────────────
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = RoyalPurple,
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Loading events...",
                                fontSize = 13.sp,
                                color = SubtitleGray
                            )
                        }
                    }
                }

                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "⚠️", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Failed to load events",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DeepIndigo
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = error ?: "",
                                fontSize = 13.sp,
                                color = SubtitleGray
                            )
                        }
                    }
                }

                events.isEmpty() -> {
                    EmptyEventsState()
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(events) { event ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically(),
                                exit = fadeOut()
                            ) {
                                EventCard(
                                    event = event,
                                    onClick = {
                                        // Navigate to event detail screen later
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventsScreenPreview() {
    val mockEvents = listOf(
        Event(
            id = "1",
            title = "Community Clean-Up",
            description = "Join us to clean up Westlands Park this Saturday morning",
            date = "Saturday, May 10 2026",
            time = "8:00 AM",
            location = "Westlands Park, Nairobi",
            category = "EVENT",
            organizer = "CommonLink Team"
        ),
        Event(
            id = "2",
            title = "Job Fair 2026",
            description = "Meet top employers in your area and find your dream job",
            date = "Sunday, May 11 2026",
            time = "10:00 AM",
            location = "KICC, Nairobi",
            category = "EVENT",
            organizer = "Nairobi Chamber"
        ),
        Event(
            id = "3",
            title = "Health Awareness Day",
            description = "Free health checkups and medical advice from professionals",
            date = "Monday, May 12 2026",
            time = "9:00 AM",
            location = "Uhuru Park, Nairobi",
            category = "HEALTH",
            organizer = "Nairobi Health Dept"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3FB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Preview header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1A1040), Color(0xFF6B3FA0))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Community Events",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${mockEvents.size} Events",
                    fontSize = 12.sp,
                    color = Color(0xCCFFFFFF)
                )
            }
        }
        mockEvents.forEach { event ->
            EventCard(event = event, onClick = {})
        }
    }
}