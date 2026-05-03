package com.christian.commonlink.ui.screens.PlaceholderScreens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val DeepIndigo  = Color(0xFF1A1040)
private val RoyalPurple = Color(0xFF6B3FA0)
private val AccentGold  = Color(0xFFFFD166)

// ── Reusable shell every placeholder uses ──────────────────────
@Composable
fun PlaceholderScreen(
    navController: NavController,
    emoji: String,
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3FB))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(DeepIndigo, RoyalPurple))
                )
                .padding(start = 8.dp, top = 48.dp, end = 20.dp, bottom = 28.dp)
        ) {
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
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 64.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DeepIndigo
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalPurple),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go Back", color = Color.White)
            }
        }
    }
}

@Composable fun PostNoticeScreen(navController: NavController) =
    PlaceholderScreen(navController, "📣", "Post a Notice", "Share announcements with your community")

@Composable fun AddEventScreen(navController: NavController) =
    PlaceholderScreen(navController, "🗓️", "Add an Event", "Create and schedule community events")

@Composable fun VolunteerScreen(navController: NavController) =
    PlaceholderScreen(navController, "🤝", "Volunteer", "Find volunteering opportunities near you")

@Composable fun NotificationsScreen(navController: NavController) =
    PlaceholderScreen(navController, "🔔", "Notifications", "You have no new notifications yet")

@Composable fun ProfileScreen(navController: NavController) =
    PlaceholderScreen(navController, "👤", "My Profile", "View and edit your community profile")

@Composable fun NoticeBoardScreen(navController: NavController) =
    PlaceholderScreen(navController, "📌", "Notice Board", "All announcements from your area")

@Composable fun SeeAllScreen(navController: NavController) =
    PlaceholderScreen(navController, "🌐", "All Posts", "Browse everything trending in your community")

@Composable fun PostDetailScreen(navController: NavController) =
    PlaceholderScreen(navController, "📄", "Post Details", "Full details about this community post")