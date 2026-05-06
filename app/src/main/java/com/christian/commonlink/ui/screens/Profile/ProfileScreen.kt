package com.christian.commonlink.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.christian.commonlink.navigation.ROUT_EDIT_PROFILE
import com.christian.commonlink.navigation.ROUT_LOGIN
import com.christian.commonlink.ui.screens.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val SoftViolet   = Color(0xFF9B6FD4)
private val AccentGold   = Color(0xFFFFD166)
private val CardWhite    = Color(0xFFFFFFFF)
private val SubtitleGray = Color(0xFF888888)
private val BgColor      = Color(0xFFF5F3FB)

// ── Reusable profile info row ────────────────────────────────────
@Composable
fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFEDE7F6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = RoyalPurple,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = SubtitleGray
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = DeepIndigo
            )
        }
    }
}

// ── Reusable stat box ────────────────────────────────────────────
@Composable
fun ProfileStatBox(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFEDE7F6))
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = RoyalPurple
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = SubtitleGray
        )
    }
}

// ── Main Profile Screen ──────────────────────────────────────────
@Composable
fun ProfileScreen(navController: NavController) {

    // ✅ Firebase user data
    val auth        = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userEmail   = currentUser?.email ?: "No email found"
    val userName    = currentUser?.displayName?.ifBlank { null }
        ?: userEmail.substringBefore("@").replaceFirstChar { it.uppercase() }

    // ✅ ViewModels
    val authViewModel:    AuthViewModel    = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    // ✅ Observe profile image from Firebase
    val profileImageUrl by profileViewModel.profileImageUrl.collectAsState()

    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .verticalScroll(rememberScrollState())
    ) {

        // ── GRADIENT HEADER ──────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(DeepIndigo, RoyalPurple))
                )
                .padding(start = 8.dp, top = 48.dp, end = 20.dp, bottom = 48.dp)
        ) {
            Column {

                // Back + Edit row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    // ✅ Edit Profile button navigates to EditProfileScreen
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0x33FFFFFF))
                            .clickable {
                                navController.navigate(ROUT_EDIT_PROFILE)
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Edit Profile",
                                fontSize = 13.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Avatar + name
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ✅ Avatar — shows real photo or first letter
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(SoftViolet, DeepIndigo)
                                )
                            )
                            .border(3.dp, AccentGold, CircleShape)
                            .clickable {
                                navController.navigate(ROUT_EDIT_PROFILE)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUrl != null) {
                            // ✅ Show real profile image from Firebase Storage
                            AsyncImage(
                                model = profileImageUrl,
                                contentDescription = "Profile Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            // ✅ Show first letter as fallback
                            Text(
                                text = userName.first().uppercase(),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // ✅ Real name from Firebase
                    Text(
                        text = userName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // ✅ Real email from Firebase
                    Text(
                        text = userEmail,
                        fontSize = 13.sp,
                        color = Color(0xCCFFFFFF)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Member badge
                    Box(
                        modifier = Modifier
                            .background(AccentGold, shape = RoundedCornerShape(50))
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "⭐ Community Member",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepIndigo
                        )
                    }
                }
            }
        }

        // ── STATS ROW ────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStatBox("5", "Events")
            ProfileStatBox("3", "Jobs")
            ProfileStatBox("8", "Notices")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── PROFILE INFO CARD ────────────────────────────────────
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Account Information",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DeepIndigo
                )

                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color(0xFFEEEEEE))
                Spacer(modifier = Modifier.height(8.dp))

                ProfileInfoRow(
                    icon = Icons.Default.Person,
                    label = "Username",
                    value = userName
                )
                Divider(color = Color(0xFFEEEEEE))

                ProfileInfoRow(
                    icon = Icons.Default.Email,
                    label = "Email Address",
                    value = userEmail
                )
                Divider(color = Color(0xFFEEEEEE))

                ProfileInfoRow(
                    icon = Icons.Default.DateRange,
                    label = "Member Since",
                    value = "May 2026"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── SETTINGS CARD ────────────────────────────────────────
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Settings",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DeepIndigo
                )

                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color(0xFFEEEEEE))
                Spacer(modifier = Modifier.height(8.dp))

                // Notifications toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEDE7F6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = RoyalPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Push Notifications",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepIndigo
                            )
                            Text(
                                text = "Get notified about new posts",
                                fontSize = 12.sp,
                                color = SubtitleGray
                            )
                        }
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = RoyalPurple
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── LOGOUT BUTTON ────────────────────────────────────────
        Button(
            onClick = {
                authViewModel.logout {
                    navController.navigate(ROUT_LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(horizontal = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFEBEE)
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "🚪 Log Out",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB71C1C)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ── Preview ──────────────────────────────────────────────────────
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3FB))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1A1040), Color(0xFF6B3FA0))
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF9B6FD4))
                        .border(3.dp, Color(0xFFFFD166), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "C",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Christian Mwangi",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "christian@email.com",
                    fontSize = 13.sp,
                    color = Color(0xCCFFFFFF)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFFFD166),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text(
                        "⭐ Community Member",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1040)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStatBox("5", "Events")
            ProfileStatBox("3", "Jobs")
            ProfileStatBox("8", "Notices")
        }
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Account Information",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1A1040)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color(0xFFEEEEEE))
                ProfileInfoRow(
                    icon = Icons.Default.Person,
                    label = "Username",
                    value = "Christian Mwangi"
                )
                Divider(color = Color(0xFFEEEEEE))
                ProfileInfoRow(
                    icon = Icons.Default.Email,
                    label = "Email Address",
                    value = "christian@email.com"
                )
                Divider(color = Color(0xFFEEEEEE))
                ProfileInfoRow(
                    icon = Icons.Default.DateRange,
                    label = "Member Since",
                    value = "May 2026"
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(horizontal = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFEBEE)
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "🚪 Log Out",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB71C1C)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}