package com.christian.commonlink.ui.screens.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.christian.commonlink.navigation.ROUT_LOGIN
import com.christian.commonlink.navigation.ROUT_MAIN
import com.christian.commonlink.ui.screens.Authentification.AuthTextField

private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val SoftViolet   = Color(0xFF9B6FD4)
private val AccentGold   = Color(0xFFFFD166)
private val SubtitleGray = Color(0xFF888888)
private val ErrorRed     = Color(0xFFE53935)

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var fullName        by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible  by remember { mutableStateOf(false) }
    var isVisible       by remember { mutableStateOf(false) }

    var fullNameError        by remember { mutableStateOf(false) }
    var emailError           by remember { mutableStateOf(false) }
    var passwordError        by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var passwordMismatch     by remember { mutableStateOf(false) }

    val isLoading    by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "fade"
    )
    val contentScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.95f,
        animationSpec = tween(durationMillis = 800),
        label = "scale"
    )

    LaunchedEffect(Unit) { isVisible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepIndigo, RoyalPurple, SoftViolet)
                )
            )
    ) {

        // Decorative circles
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-60).dp)
                .background(Color(0x22FFFFFF), shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-60).dp, y = 60.dp)
                .background(Color(0x22FFFFFF), shape = CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .alpha(contentAlpha)
                .scale(contentScale)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            // Logo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
                    .border(2.dp, AccentGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🌐", fontSize = 36.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CommonLink",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGold,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Join your community today",
                fontSize = 14.sp,
                color = Color(0xCCFFFFFF),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Form Card
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Error banner
                    errorMessage?.let { error ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFEBEE))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "⚠️ $error",
                                fontSize = 13.sp,
                                color = ErrorRed,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Full Name
                    AuthTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            fullNameError = false
                        },
                        label = "Full Name",
                        placeholder = "e.g. Christian Mwangi",
                        isError = fullNameError,
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Name",
                                tint = if (fullNameError) ErrorRed else RoyalPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    // Email
                    AuthTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = false
                            viewModel.clearError()
                        },
                        label = "Email Address",
                        placeholder = "e.g. christian@email.com",
                        isError = emailError,
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Email",
                                tint = if (emailError) ErrorRed else RoyalPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    // Password
                    AuthTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = false
                            passwordMismatch = false
                        },
                        label = "Password",
                        placeholder = "Minimum 6 characters",
                        isError = passwordError,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onPasswordToggle = { passwordVisible = !passwordVisible },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = if (passwordError) ErrorRed else RoyalPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    // Confirm Password
                    AuthTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = false
                            passwordMismatch = false
                        },
                        label = "Confirm Password",
                        placeholder = "Re-enter your password",
                        isError = confirmPasswordError || passwordMismatch,
                        errorText = if (passwordMismatch) "Passwords do not match"
                        else "This field is required",
                        isPassword = true,
                        passwordVisible = confirmVisible,
                        onPasswordToggle = { confirmVisible = !confirmVisible },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Confirm",
                                tint = if (confirmPasswordError || passwordMismatch)
                                    ErrorRed else RoyalPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    // Password strength indicator
                    if (password.isNotEmpty()) {
                        val strength = when {
                            password.length < 6  -> Triple("Weak", Color(0xFFE53935), 0.33f)
                            password.length < 10 -> Triple("Medium", Color(0xFFFF8F00), 0.66f)
                            else                 -> Triple("Strong", Color(0xFF2E7D32), 1f)
                        }
                        Column {
                            LinearProgressIndicator(
                                progress = { strength.third },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = strength.second,
                                trackColor = Color(0xFFEEEEEE)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Password strength: ${strength.first}",
                                fontSize = 11.sp,
                                color = strength.second,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Register button
                    Button(
                        onClick = {
                            fullNameError        = fullName.isBlank()
                            emailError           = email.isBlank()
                            passwordError        = password.isBlank()
                            confirmPasswordError = confirmPassword.isBlank()
                            passwordMismatch     = password != confirmPassword &&
                                    confirmPassword.isNotBlank()

                            val allValid = !fullNameError && !emailError &&
                                    !passwordError && !confirmPasswordError &&
                                    !passwordMismatch

                            if (allValid) {
                                viewModel.register(email, password) {
                                    navController.navigate(ROUT_MAIN) {  // ✅ Changed from ROUT_HOME
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoyalPurple
                        ),
                        shape = RoundedCornerShape(14.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Create Account",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE))
                        Text(text = "  or  ", fontSize = 12.sp, color = SubtitleGray)
                        Divider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE))
                    }

                    // Login link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            fontSize = 14.sp,
                            color = SubtitleGray
                        )
                        Text(
                            text = "Login",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalPurple,
                            modifier = Modifier.clickable {
                                navController.navigate(ROUT_LOGIN)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Connect. Discover. Grow.",
                fontSize = 13.sp,
                color = Color(0x99FFFFFF),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1040),
                        Color(0xFF6B3FA0),
                        Color(0xFF9B6FD4)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-60).dp)
                .background(Color(0x22FFFFFF), shape = CircleShape)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
                    .border(2.dp, Color(0xFFFFD166), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🌐", fontSize = 36.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "CommonLink",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD166),
                letterSpacing = 3.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Join your community today",
                fontSize = 14.sp,
                color = Color(0xCCFFFFFF)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("Full Name", "Email Address", "Password", "Confirm Password")
                        .forEach { label ->
                            Column {
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1A1040)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFFF5F3FB))
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = if (label.contains("Password")) "••••••••"
                                        else "Enter $label",
                                        fontSize = 13.sp,
                                        color = Color(0xFF888888)
                                    )
                                }
                            }
                        }
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6B3FA0)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "Create Account",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Already have an account? ",
                            fontSize = 14.sp,
                            color = Color(0xFF888888)
                        )
                        Text(
                            text = "Login",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B3FA0)
                        )
                    }
                }
            }
        }
    }
}