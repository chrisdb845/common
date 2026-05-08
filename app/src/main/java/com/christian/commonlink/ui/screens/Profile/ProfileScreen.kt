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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.christian.commonlink.navigation.ROUT_EDIT_PROFILE
import com.christian.commonlink.navigation.ROUT_LOGIN
import com.christian.commonlink.ui.screens.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val SoftViolet   = Color(0xFF9B6FD4)
private val AccentGold   = Color(0xFFFFD166)
private val CardWhite    = Color.White
private val SubtitleGray = Color(0xFF888888)
private val BgColor      = Color(0xFFF5F3FB)
private val LightPurple  = Color(0xFFF1EBFF)

// ── Profile Info Row ─────────────────────────────────────────────
@Composable
fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(LightPurple),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = RoyalPurple,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = SubtitleGray)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = DeepIndigo
            )
        }
    }
}

// ── Profile Action Item ──────────────────────────────────────────
@Composable
fun ProfileActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color = RoyalPurple,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CardWhite)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = iconColor)
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = DeepIndigo
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = subtitle, fontSize = 12.sp, color = SubtitleGray)
        }
        Icon(
            Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = SubtitleGray
        )
    }
}

// ── Profile Stat Card ────────────────────────────────────────────
@Composable
fun ProfileStatCard(title: String, value: String) {
    Card(
        modifier = Modifier.width(110.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalPurple
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 12.sp, color = SubtitleGray)
        }
    }
}

// ── Main Profile Screen ──────────────────────────────────────────
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val auth        = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userEmail   = currentUser?.email ?: "No Email"
    val userName    = currentUser?.displayName
        ?: userEmail.substringBefore("@")
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }

    // ✅ Wrap everything in ONE Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .verticalScroll(rememberScrollState())
    ) {

        // ── HEADER ───────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(DeepIndigo, RoyalPurple))
                )
                .padding(
                    start   = 20.dp,
                    end     = 20.dp,
                    top     = 50.dp,
                    bottom  = 30.dp
                )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Top bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    TextButton(onClick = { navController.navigate(ROUT_EDIT_PROFILE) }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            tint = AccentGold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Edit",
                            color = AccentGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Avatar
                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .clip(CircleShape)
                        .background(SoftViolet)
                        .border(4.dp, AccentGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.takeIf { it.isNotBlank() }
                            ?.first()?.uppercase() ?: "?",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = userName,
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = userEmail,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = AccentGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Community Member ✨",
                        color = AccentGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical   = 8.dp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ── STATS ────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileStatCard(title = "Posts",       value = "12")
            ProfileStatCard(title = "Events",      value = "5")
            ProfileStatCard(title = "Connections", value = "248")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── ACCOUNT INFORMATION ──────────────────────────────────
        Text(
            text = "Account Information",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = DeepIndigo
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                ProfileInfoRow(
                    icon  = Icons.Default.Person,
                    label = "Username",
                    value = userName
                )
                HorizontalDivider()
                ProfileInfoRow(
                    icon  = Icons.Default.Email,
                    label = "Email Address",
                    value = userEmail
                )
                HorizontalDivider()
                ProfileInfoRow(
                    icon  = Icons.Default.VerifiedUser,
                    label = "Account Status",
                    value = "Verified Account"
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // ── SETTINGS ─────────────────────────────────────────────
        Text(
            text = "Settings & More",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = DeepIndigo
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileActionItem(
                icon     = Icons.Default.Edit,
                title    = "Edit Profile",
                subtitle = "Update your personal information"
            ) { navController.navigate(ROUT_EDIT_PROFILE) }

            ProfileActionItem(
                icon      = Icons.Default.Notifications,
                title     = "Notifications",
                subtitle  = "Manage your notification settings",
                iconColor = Color(0xFFFF9800)
            ) {}

            ProfileActionItem(
                icon      = Icons.Default.Security,
                title     = "Privacy & Security",
                subtitle  = "Keep your account secure",
                iconColor = Color(0xFF2196F3)
            ) {}

            ProfileActionItem(
                icon      = Icons.Default.Help,
                title     = "Help & Support",
                subtitle  = "Get help with the app",
                iconColor = Color(0xFF4CAF50)
            ) {}
        }

        Spacer(modifier = Modifier.height(30.dp))

        // ── LOGOUT BUTTON ────────────────────────────────────────
        // ✅ Fixed — properly signs out then navigates
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(58.dp),
            shape  = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB71C1C)
            )
        ) {
            Icon(
                Icons.Default.Logout,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Logout", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(30.dp))

        // ── APP VERSION ──────────────────────────────────────────
        Text(
            text      = "CommonLink v1.0.0",
            modifier  = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize  = 12.sp,
            color     = SubtitleGray
        )

        Spacer(modifier = Modifier.height(20.dp))

    } // ✅ Single closing brace for the main Column
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3FB))
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
                Spacer(modifier = Modifier.height(20.dp))
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
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}