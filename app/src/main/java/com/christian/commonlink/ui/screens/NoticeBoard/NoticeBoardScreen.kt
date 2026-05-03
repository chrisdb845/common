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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange
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
import androidx.navigation.compose.rememberNavController
import com.christian.commonlink.navigation.ROUT_ADD_NOTICE

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

@Composable
fun NoticeBoardScreen(
    navController: NavController,
    viewModel: NoticeViewModel? = null
) {
    val isPreview = androidx.compose.ui.platform.LocalInspectionMode.current

    // ✅ Use real ViewModel only in app, not in preview
    val vm = viewModel ?: if (!isPreview) viewModel() else null

    // ✅ Data handling (preview vs real)
    val notices = if (isPreview) {
        listOf(
            Notice(
                id = "1",
                title = "Water Interruption Notice",
                description = "Water supply will be interrupted on Monday 6AM-6PM",
                postedBy = "Nairobi Water",
                date = "Saturday, May 10 2026",
                category = "ANNOUNCEMENT",
                urgent = true
            ),
            Notice(
                id = "2",
                title = "Community Meeting",
                description = "Monthly community meeting at the local hall",
                postedBy = "CommonLink Team",
                date = "Sunday, May 11 2026",
                category = "GENERAL",
                urgent = false
            )
        )
    } else {
        vm!!.notices.collectAsState().value
    }

    val isLoading = if (isPreview) false else vm!!.isLoading.collectAsState().value
    val error = if (isPreview) null else vm!!.errorMessage.collectAsState().value

    // ── Separate urgent from normal notices ──────────────────────
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

            // ── HEADER ─────────────────────────────────────────
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
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
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

            // ── STATES ─────────────────────────────────────────
            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = RoyalPurple)
                    }
                }

                error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: $error")
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
                                    "🚨 Urgent Notices",
                                    fontWeight = FontWeight.Bold,
                                    color = UrgentRed
                                )
                            }
                            items(urgentNotices) {
                                NoticeCard(it, onClick = {})
                            }
                        }

                        if (normalNotices.isNotEmpty()) {
                            item {
                                Text(
                                    "📌 All Notices",
                                    fontWeight = FontWeight.Bold,
                                    color = DeepIndigo
                                )
                            }
                            items(normalNotices) {
                                NoticeCard(it, onClick = {})
                            }
                        }
                    }
                }
            }
        }
    }
}