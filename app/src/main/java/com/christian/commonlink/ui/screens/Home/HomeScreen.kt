package com.christian.commonlink.ui.screens.home

import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.christian.commonlink.R
import com.christian.commonlink.navigation.ROUT_EVENTS
import com.christian.commonlink.navigation.ROUT_JOBS
import com.christian.commonlink.navigation.ROUT_ADD_EVENT
import com.christian.commonlink.navigation.ROUT_SERVICES
import com.christian.commonlink.navigation.ROUT_NOTICE_BOARD
import com.christian.commonlink.navigation.ROUT_NOTIFICATIONS
import com.christian.commonlink.navigation.ROUT_PROFILE
import com.christian.commonlink.navigation.ROUT_SEE_ALL
import com.christian.commonlink.navigation.ROUT_POST_DETAIL
import com.christian.commonlink.navigation.ROUT_POST_NOTICE
import com.christian.commonlink.navigation.ROUT_VOLUNTEER


// ── Brand palette ───────────────────────────────────────────────
private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val SoftViolet   = Color(0xFF9B6FD4)
private val AccentGold   = Color(0xFFFFD166)
private val SubtitleGray = Color(0xFF888888)

// ── Data classes ────────────────────────────────────────────────
data class Category(val label: String, val emoji: String)
data class QuickAction(val emoji: String, val label: String, val route: String)

// ── Category list ───────────────────────────────────────────────
val categories = listOf(
    Category("All",      "🌐"),
    Category("Events",   "📅"),
    Category("Jobs",     "💼"),
    Category("Services", "🔧"),
    Category("News",     "📰"),
    Category("Health",   "💊"),
)

// ── Reusable Post Card ──────────────────────────────────────────
@Composable
fun PostCard(
    title: String,
    subtitle: String,
    tag: String,
    image: Int,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() }
    ) {
        Column {
            Box {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(image),
                        contentDescription = title,
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xAA000000))
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .background(AccentGold, shape = RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = tag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepIndigo
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DeepIndigo
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = SubtitleGray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalPurple),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Details", fontSize = 13.sp, color = Color.White)
                }
            }
        }
    }
}

// ── Stat Item ───────────────────────────────────────────────────
@Composable
fun StatItem(value: String, label: String, isLoading: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0x44FFFFFF))
            )
        } else {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xCCFFFFFF)
        )
    }
}

// ── Main Home Screen ────────────────────────────────────────────
@Composable
fun Home(
    navcontroller: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val membersCount by viewModel.membersCount.collectAsState()
    val eventsCount  by viewModel.eventsCount.collectAsState()
    val jobsCount    by viewModel.jobsCount.collectAsState()
    val isLoading    by viewModel.isLoading.collectAsState()

    var search by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val quickActions = listOf(
        QuickAction("📣", "Post\nNotice", ROUT_POST_NOTICE),
        QuickAction("🗓️", "Add\nEvent",   ROUT_ADD_EVENT),
        QuickAction("🤝", "Volunteer",    ROUT_VOLUNTEER)
    )
    val quickActionColors = listOf(RoyalPurple, DeepIndigo, SoftViolet)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3FB))
            .verticalScroll(rememberScrollState())
    ) {

        // ── GRADIENT HEADER ─────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(DeepIndigo, RoyalPurple)))
                .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 28.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Hello 👋", fontSize = 13.sp, color = Color(0xCCFFFFFF))
                        Text(
                            text = "Community Hub",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box {
                            IconButton(onClick = { navcontroller.navigate(ROUT_NOTIFICATIONS) }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                            }
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .background(AccentGold, CircleShape)
                                    .align(Alignment.TopEnd)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF))
                                .clickable { navcontroller.navigate(ROUT_PROFILE) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x22FFFFFF), shape = RoundedCornerShape(16.dp))
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(membersCount, "Members", isLoading)
                    Divider(modifier = Modifier.height(32.dp).width(1.dp), color = Color(0x44FFFFFF))
                    StatItem(eventsCount, "Events", isLoading)
                    Divider(modifier = Modifier.height(32.dp).width(1.dp), color = Color(0x44FFFFFF))
                    StatItem(jobsCount, "Jobs", isLoading)
                }

                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = SubtitleGray) },
                    placeholder = { Text("Search events, jobs, services...", fontSize = 13.sp, color = SubtitleGray) },
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── CATEGORY CHIPS ───────────────────────────────────────
        // ✅ Only ONE forEach loop, inside the Row
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category.label
                Box(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .background(
                            color = if (isSelected) RoyalPurple else Color.White,
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            selectedCategory = category.label
                            when (category.label) {
                                "Events"   -> navcontroller.navigate(ROUT_EVENTS)
                                "Jobs"     -> navcontroller.navigate(ROUT_JOBS)
                                "Services" -> navcontroller.navigate(ROUT_SERVICES)
                                "News"     -> navcontroller.navigate(ROUT_NOTICE_BOARD)
                                "Health"   -> navcontroller.navigate(ROUT_NOTICE_BOARD)
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 9.dp)
                ) {
                    Text(
                        text = "${category.emoji}  ${category.label}",
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else DeepIndigo
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── NOTICE BOARD BANNER ──────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { navcontroller.navigate(ROUT_NOTICE_BOARD) }
                .background(Brush.horizontalGradient(colors = listOf(AccentGold, Color(0xFFFFB830))))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "📌", fontSize = 26.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Notice Board", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepIndigo)
                    Text(text = "3 new announcements from your area", fontSize = 12.sp, color = Color(0xFF4A3000))
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "›", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DeepIndigo)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── SECTION TITLE + SEE ALL ──────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Trending in Your Community", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DeepIndigo)
            Text(
                text = "See all",
                fontSize = 13.sp,
                color = RoyalPurple,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { navcontroller.navigate(ROUT_SEE_ALL) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── HORIZONTAL POST CARDS ────────────────────────────────
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            PostCard(
                title = "Community Clean-Up",
                subtitle = "📅 Saturday • 8AM • Westlands Park",
                tag = "EVENT",
                image = R.drawable.community
            ) { navcontroller.navigate(ROUT_POST_DETAIL) }

            Spacer(modifier = Modifier.width(16.dp))

            PostCard(
                title = "Job Opportunities",
                subtitle = "💼 Part-time • Students welcome",
                tag = "JOBS",
                image = R.drawable.community
            ) { navcontroller.navigate(ROUT_POST_DETAIL) }

            Spacer(modifier = Modifier.width(16.dp))

            PostCard(
                title = "Local Services",
                subtitle = "🔧 Trusted technicians available",
                tag = "SERVICE",
                image = R.drawable.community
            ) { navcontroller.navigate(ROUT_POST_DETAIL) }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ── QUICK ACTIONS ────────────────────────────────────────
        Text(
            text = "Quick Actions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DeepIndigo,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            quickActions.forEachIndexed { index, action ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { navcontroller.navigate(action.route) }
                        .background(quickActionColors[index])
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = action.emoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = action.label,
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

    } // ✅ Closes Column
} // ✅ Closes Home()

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Home(rememberNavController())
}