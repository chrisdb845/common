package com.christian.commonlink.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.christian.commonlink.R
import com.christian.commonlink.navigation.ROUT_LOGIN
import com.christian.commonlink.navigation.ROUT_MAIN
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

// ── Colors ─────────────────────────────────────
private val DeepIndigo = Color(0xFF1A1040)
private val RoyalPurple = Color(0xFF6B3FA0)
private val SoftViolet = Color(0xFF9B6FD4)
private val AccentGold = Color(0xFFFFD166)
private val PureWhite = Color(0xFFFFFFFF)
private val FrostWhite = Color(0xCCFFFFFF)

@Composable
fun SplashScreen(navController: NavController) {

    val auth = remember { FirebaseAuth.getInstance() }
    var isVisible by remember { mutableStateOf(false) }

    val isPreview = LocalInspectionMode.current

    // ✅ Safe navigation (runs once)
    LaunchedEffect(Unit) {
        if (!isPreview) {
            isVisible = true
            delay(2000)

            val destination = if (auth.currentUser != null) {
                ROUT_MAIN
            } else {
                ROUT_LOGIN
            }

            navController.navigate(destination) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        } else {
            isVisible = true
        }
    }

    // Animations
    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(900),
        label = ""
    )

    val logoScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.7f,
        animationSpec = tween(800),
        label = ""
    )

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // ── UI ─────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(DeepIndigo, RoyalPurple, SoftViolet)
                )
            )
    ) {

        // Background blobs
        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.TopStart)
                .alpha(0.25f)
                .blur(60.dp)
                .background(AccentGold, CircleShape)
        )

        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomEnd)
                .alpha(0.2f)
                .blur(80.dp)
                .background(SoftViolet, CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .alpha(contentAlpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(contentAlignment = Alignment.Center) {

                Box(
                    modifier = Modifier
                        .size(148.dp)
                        .scale(pulseScale)
                        .alpha(pulseAlpha)
                        .background(
                            Brush.radialGradient(
                                listOf(AccentGold, Color.Transparent)
                            ),
                            CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(logoScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0x33FFFFFF), Color(0x11FFFFFF))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.community),
                        contentDescription = "Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0x22FFFFFF), Color(0x11FFFFFF))
                        ),
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text(
                    "CommonLink",
                    color = AccentGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                "Connect. Discover. Grow.",
                color = PureWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Your community, all in one place.",
                color = FrostWhite,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(48.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = AccentGold,
                strokeWidth = 3.dp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Loading your world...",
                color = Color(0x99FFFFFF),
                fontSize = 11.sp
            )
        }
    }
}