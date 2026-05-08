package com.christian.commonlink.ui.screens.noticeboard

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.christian.commonlink.navigation.ROUT_ADD_NOTICE

// ── Brand palette ────────────────────────────────────────────────
private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val AccentGold   = Color(0xFFFFD166)
private val CardWhite    = Color(0xFFFFFFFF)
private val SubtitleGray = Color(0xFF888888)
private val BgColor      = Color(0xFFF5F3FB)
private val UrgentRed    = Color(0xFFB71C1C)
private val UrgentRedBg  = Color(0xFFFFEBEE)
private val NormalBlue   = Color(0xFF1565C0)
private val NormalBlueBg = Color(0xFFE3F2FD)

// ── Notice Card ──────────────────────────────────────────────────
@Composable
fun NoticeCard(notice: Notice, onClick: () -> Unit) {
    val tagColor    = if (notice.urgent) UrgentRed   else NormalBlue
    val tagBgColor  = if (notice.urgent) UrgentRedBg else NormalBlueBg
    val accentColor = if (notice.urgent) UrgentRed   else RoyalPurple

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            // Left accent bar
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(140.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(accentColor, AccentGold)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                // Category + urgent badge row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(tagBgColor, shape = RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = notice.category,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = tagColor
                        )
                    }

                    if (notice.urgent) {
                        Box(
                            modifier = Modifier
                                .background(UrgentRed, shape = RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🚨 URGENT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                Text(
                    text = notice.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = DeepIndigo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description
                Text(
                    text = notice.description,
                    fontSize = 13.sp,
                    color = SubtitleGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Posted by + date row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Posted by",
                            tint = SubtitleGray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = notice.postedBy,
                            fontSize = 12.sp,
                            color = SubtitleGray
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Date",
                            tint = RoyalPurple,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = notice.date,
                            fontSize = 12.sp,
                            color = RoyalPurple,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

// ── Empty State ──────────────────────────────────────────────────
@Composable
fun EmptyNoticesState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "📌", fontSize = 56.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Notices Yet",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = DeepIndigo
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Be the first to post a community notice!",
            fontSize = 14.sp,
            color = SubtitleGray
        )
    }
}

// ── Main Notice Board Screen ─────────────────────────────────────
@Composable
fun NoticeBoardScreen(
    navController: NavController,
    viewModel: NoticeViewModel = viewModel()
) {
    val notices   by viewModel.notices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error     by viewModel.errorMessage.collectAsState()

    val urgentNotices = notices.filter { it.urgent }
    val normalNotices = notices.filter { !it.urgent }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ROUT_ADD_NOTICE) },
                containerColor = RoyalPurple,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Notice")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BgColor)
                .padding(paddingValues)
        ) {

            // ── HEADER ───────────────────────────────────────────
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
                            text = "Notice Board",
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Community announcements",
                            fontSize = 13.sp,
                            color = Color(0xCCFFFFFF)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .background(AccentGold, RoundedCornerShape(50))
                                    .padding(horizontal = 12.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "${notices.size} Notices",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepIndigo
                                )
                            }

                            if (urgentNotices.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .background(UrgentRed, RoundedCornerShape(50))
                                        .padding(horizontal = 12.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = "🚨 ${urgentNotices.size}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
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
                                text = "Loading notices...",
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
                                text = "Failed to load notices",
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

                notices.isEmpty() -> EmptyNoticesState()

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        if (urgentNotices.isNotEmpty()) {
                            item {
                                Text(
                                    text = "🚨 Urgent Notices",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = UrgentRed,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            items(urgentNotices) { notice ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn() + slideInVertically(),
                                    exit = fadeOut()
                                ) {
                                    NoticeCard(notice = notice, onClick = {})
                                }
                            }
                        }

                        if (normalNotices.isNotEmpty()) {
                            item {
                                Text(
                                    text = "📌 All Notices",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = DeepIndigo,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            items(normalNotices) { notice ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn() + slideInVertically(),
                                    exit = fadeOut()
                                ) {
                                    NoticeCard(notice = notice, onClick = {})
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Preview ──────────────────────────────────────────────────────
@Preview(showBackground = true)
@Composable
fun NoticeBoardScreenPreview() {
    val mockNotices = listOf(
        Notice(
            id = "1",
            title = "Water Interruption Notice",
            description = "Water supply will be interrupted on Monday 6AM-6PM for maintenance",
            postedBy = "Nairobi Water",
            date = "Saturday, May 10 2026",
            category = "ANNOUNCEMENT",
            urgent = true
        ),
        Notice(
            id = "2",
            title = "Road Closure Alert",
            description = "Waiyaki Way will be closed this weekend for repairs",
            postedBy = "KeNHA",
            date = "Friday, May 9 2026",
            category = "ALERT",
            urgent = true
        ),
        Notice(
            id = "3",
            title = "Community Meeting",
            description = "Monthly community meeting at the local hall this Sunday at 2PM",
            postedBy = "CommonLink Team",
            date = "Sunday, May 11 2026",
            category = "GENERAL",
            urgent = false
        ),
        Notice(
            id = "4",
            title = "New Health Clinic Open",
            description = "A new free health clinic has opened in the community centre",
            postedBy = "Ministry of Health",
            date = "Monday, May 12 2026",
            category = "HEALTH",
            urgent = false
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3FB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Notice Board",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${mockNotices.size} Notices",
                        fontSize = 12.sp,
                        color = Color(0xCCFFFFFF)
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFB71C1C),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "🚨 2 Urgent",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
        mockNotices.forEach { notice ->
            NoticeCard(notice = notice, onClick = {})
        }
    }
}