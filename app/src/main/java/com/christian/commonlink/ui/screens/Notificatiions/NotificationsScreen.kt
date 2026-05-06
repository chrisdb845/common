// ✅ Must be exactly this
package com.christian.commonlink.ui.screens.noticeboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val AccentGold   = Color(0xFFFFD166)
private val CardWhite    = Color(0xFFFFFFFF)
private val SubtitleGray = Color(0xFF888888)
private val BgColor      = Color(0xFFF5F3FB)

// ── Notification data class ──────────────────────────────────────
data class NotificationItem(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val time: String = "",
    val emoji: String = "",
    val isRead: Boolean = false
)

// ── Single Notification Card ─────────────────────────────────────
@Composable
fun NotificationCard(notification: NotificationItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(if (notification.isRead) 2.dp else 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color(0xFFF0EEF8) else CardWhite
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji icon circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (notification.isRead) Color(0xFFE0E0E0)
                        else Color(0xFFEDE7F6)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = notification.emoji, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontWeight = if (notification.isRead) FontWeight.Normal
                        else FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DeepIndigo
                    )
                    Text(
                        text = notification.time,
                        fontSize = 11.sp,
                        color = SubtitleGray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    fontSize = 13.sp,
                    color = SubtitleGray,
                    lineHeight = 18.sp
                )
            }

            // Unread dot indicator
            if (!notification.isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(RoyalPurple)
                )
            }
        }
    }
}

// ── Main Notifications Screen ────────────────────────────────────
@Composable
fun NotificationsScreen(navController: NavController) {

    // ── Mock notifications — replace with Firebase later ─────────
    val notifications = listOf(
        NotificationItem(
            id = "1",
            title = "New Event Posted",
            message = "Community Clean-Up has been added to events",
            time = "2m ago",
            emoji = "📅",
            isRead = false
        ),
        NotificationItem(
            id = "2",
            title = "Urgent Notice",
            message = "Water interruption scheduled for Monday 6AM-6PM",
            time = "1h ago",
            emoji = "🚨",
            isRead = false
        ),
        NotificationItem(
            id = "3",
            title = "New Job Posted",
            message = "Software Developer role at TechCorp Nairobi",
            time = "3h ago",
            emoji = "💼",
            isRead = false
        ),
        NotificationItem(
            id = "4",
            title = "New Service Listed",
            message = "John Kamau listed Plumbing Services",
            time = "5h ago",
            emoji = "🔧",
            isRead = true
        ),
        NotificationItem(
            id = "5",
            title = "Community Update",
            message = "Monthly community meeting this Sunday at 2PM",
            time = "1d ago",
            emoji = "📌",
            isRead = true
        ),
        NotificationItem(
            id = "6",
            title = "New Member Joined",
            message = "Welcome Jane Wanjiku to the community!",
            time = "2d ago",
            emoji = "👋",
            isRead = true
        )
    )

    val unreadCount = notifications.count { !it.isRead }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {

        // ── GRADIENT HEADER ──────────────────────────────────────
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
                        text = "Notifications",
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
                        text = "Stay up to date",
                        fontSize = 13.sp,
                        color = Color(0xCCFFFFFF)
                    )
                    // Unread count badge
                    if (unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .background(AccentGold, shape = RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "$unreadCount Unread",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepIndigo
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── NOTIFICATIONS LIST ───────────────────────────────────
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Unread section
            val unread = notifications.filter { !it.isRead }
            val read   = notifications.filter { it.isRead }

            if (unread.isNotEmpty()) {
                item {
                    Text(
                        text = "🔔 New",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = RoyalPurple,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(unread) { notification ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut()
                    ) {
                        NotificationCard(notification = notification)
                    }
                }
            }

            if (read.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "✅ Earlier",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = SubtitleGray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(read) { notification ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut()
                    ) {
                        NotificationCard(notification = notification)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    val mockNotifications = listOf(
        NotificationItem(
            id = "1",
            title = "New Event Posted",
            message = "Community Clean-Up has been added to events",
            time = "2m ago",
            emoji = "📅",
            isRead = false
        ),
        NotificationItem(
            id = "2",
            title = "Urgent Notice",
            message = "Water interruption scheduled for Monday 6AM-6PM",
            time = "1h ago",
            emoji = "🚨",
            isRead = false
        ),
        NotificationItem(
            id = "3",
            title = "New Job Posted",
            message = "Software Developer role at TechCorp Nairobi",
            time = "3h ago",
            emoji = "💼",
            isRead = false
        ),
        NotificationItem(
            id = "4",
            title = "New Service Listed",
            message = "John Kamau listed Plumbing Services",
            time = "5h ago",
            emoji = "🔧",
            isRead = true
        ),
        NotificationItem(
            id = "5",
            title = "Community Update",
            message = "Monthly community meeting this Sunday at 2PM",
            time = "1d ago",
            emoji = "📌",
            isRead = true
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3FB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Notifications",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "3 Unread",
                        fontSize = 12.sp,
                        color = Color(0xCCFFFFFF)
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFFFD166),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "3 New",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1040)
                    )
                }
            }
        }
        mockNotifications.forEach { notification ->
            NotificationCard(notification = notification)
        }
    }
}