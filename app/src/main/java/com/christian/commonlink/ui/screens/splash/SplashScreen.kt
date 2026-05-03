package com.christian.commonlink.ui.screens.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.christian.commonlink.R
import com.christian.commonlink.navigation.ROUT_HOME
import com.christian.commonlink.navigation.ROUT_LOGIN
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

// ── Brand colors ─────────────────────────────────────────────────
private val DeepIndigo = Color(0xFF1A1040)
private val RoyalPurple = Color(0xFF6B3FA0)
private val SoftViolet = Color(0xFF9B6FD4)
private val AccentGold = Color(0xFFFFD166)
private val PureWhite = Color(0xFFFFFFFF)
private val FrostWhite = Color(0xCCFFFFFF)

@Composable
fun SplashScreen(navController: NavController) {

    // ✅ Firebase auth instance
    val auth = FirebaseAuth.getInstance()

    var isVisible by remember { mutableStateOf(false) }

    // ✅ Check auth state and navigate accordingly
    LaunchedEffect(Unit) {
        isVisible = true
        delay(2000)
        if (auth.currentUser != null) {
            // ✅ Already logged in — go straight to Home
            navController.navigate(ROUT_HOME) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            // ✅ Not logged in — go to Login
            navController.navigate(ROUT_LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // Fade-in animation
    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "contentFade"
    )

    // Scale-up animation for logo
    val logoScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.7f,
        animationSpec = tween(durationMillis = 800),
        label = "logoScale"
    )

    // Infinite pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // 🌌 Full-screen gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepIndigo, RoyalPurple, SoftViolet)
                )
            )
    ) {

        // Decorative blurred circle — top left
        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.TopStart)
                .alpha(0.25f)
                .blur(60.dp)
                .background(AccentGold, shape = CircleShape)
        )

        // Decorative blurred circle — bottom right
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomEnd)
                .alpha(0.2f)
                .blur(80.dp)
                .background(SoftViolet, shape = CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .alpha(contentAlpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Pulsing glow ring + logo
            Box(contentAlignment = Alignment.Center) {

                // Pulse ring
                Box(
                    modifier = Modifier
                        .size(148.dp)
                        .scale(pulseScale)
                        .alpha(pulseAlpha)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(AccentGold, Color.Transparent)
                            ),
                            shape = CircleShape
                        )
                )

                // Frosted glass circle container
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(logoScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0x33FFFFFF),
                                    Color(0x11FFFFFF)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // ✅ Circular image
                    Image(
                        painter = painterResource(R.drawable.community),
                        contentDescription = "App Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App name badge
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0x22FFFFFF),
                                Color(0x11FFFFFF)
                            )
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "CommonLink",
                    color = AccentGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title
            Text(
                text = "Connect. Discover. Grow.",
                color = PureWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = "Your community, all in one place.",
                color = FrostWhite,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Loader
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = AccentGold,
                strokeWidth = 3.dp,
                trackColor = Color(0x33FFFFFF)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Loading your world...",
                color = Color(0x99FFFFFF),
                fontSize = 11.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}