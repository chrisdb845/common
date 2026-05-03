package com.christian.commonlink.ui.screens.Authentification

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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.christian.commonlink.navigation.ROUT_HOME
import com.christian.commonlink.navigation.ROUT_LOGIN
import com.christian.commonlink.ui.screens.auth.AuthViewModel

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

    var fullNameError        by remember { mutableStateOf(false) }
    var emailError           by remember { mutableStateOf(false) }
    var passwordError        by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var passwordMismatch     by remember { mutableStateOf(false) }

    val isLoading    by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3FB))
            .verticalScroll(rememberScrollState())
    ) {

        // ── GRADIENT HEADER ──────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(DeepIndigo, RoyalPurple))
                )
                .padding(top = 60.dp, bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Logo circle
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
                    text = "Create Account",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Join your community today",
                    fontSize = 14.sp,
                    color = Color(0xCCFFFFFF)
                )
            }
        }

        // ── FORM ─────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Firebase error banner
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
                onValueChange = { fullName = it; fullNameError = false },
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
                onValueChange = { email = it; emailError = false
                    viewModel.clearError() },
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
                onValueChange = { password = it; passwordError = false
                    passwordMismatch = false },
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
                onValueChange = { confirmPassword = it
                    confirmPasswordError = false
                    passwordMismatch = false },
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
                        contentDescription = "Confirm Password",
                        tint = if (confirmPasswordError || passwordMismatch)
                            ErrorRed else RoyalPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                            navController.navigate(ROUT_HOME) {
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

            // Already have account
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
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(rememberNavController())
}