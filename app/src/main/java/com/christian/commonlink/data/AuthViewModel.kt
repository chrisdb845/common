package com.christian.commonlink.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    // ── Check if user is already logged in ──────────────────────
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    // ── Register with email and password ────────────────────────
    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    _currentUser.value = auth.currentUser
                    _isLoading.value = false
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    _isLoading.value = false
                    _errorMessage.value = when {
                        exception.message?.contains("email") == true ->
                            "Invalid email address"
                        exception.message?.contains("password") == true ->
                            "Password must be at least 6 characters"
                        exception.message?.contains("already") == true ->
                            "An account with this email already exists"
                        else -> exception.message ?: "Registration failed"
                    }
                }
        }
    }

    // ── Login with email and password ────────────────────────────
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    _currentUser.value = auth.currentUser
                    _isLoading.value = false
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    _isLoading.value = false
                    _errorMessage.value = when {
                        exception.message?.contains("password") == true ->
                            "Incorrect password"
                        exception.message?.contains("no user") == true ->
                            "No account found with this email"
                        exception.message?.contains("email") == true ->
                            "Invalid email address"
                        else -> exception.message ?: "Login failed"
                    }
                }
        }
    }

    // ── Logout ───────────────────────────────────────────────────
    fun logout(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            FirebaseAuth.getInstance().signOut()
            onSuccess()
        } catch (e: Exception) {
            onError(e.message ?: "Logout failed")
        }
    }

    // ── Clear error message ──────────────────────────────────────
    fun clearError() {
        _errorMessage.value = null
    }
}