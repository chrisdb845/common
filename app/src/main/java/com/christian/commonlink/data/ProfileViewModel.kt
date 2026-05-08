package com.christian.commonlink.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val auth    = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance().reference
    private val db      = FirebaseDatabase.getInstance().reference

    // ── States ───────────────────────────────────────────────────
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl: StateFlow<String?> = _profileImageUrl

    init {
        loadProfileImage()
    }

    // ── Load profile image from Firebase ────────────────────────
    private fun loadProfileImage() {
        val uid = auth.currentUser?.uid ?: return
        db.child("users").child(uid).child("profileImage")
            .get()
            .addOnSuccessListener { snapshot ->
                _profileImageUrl.value = snapshot.getValue(String::class.java)
            }
    }

    // ── Update display name ──────────────────────────────────────
    fun updateName(newName: String, onSuccess: () -> Unit) {
        if (newName.isBlank()) {
            _errorMessage.value = "Name cannot be empty"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val user = auth.currentUser ?: return@launch
            val uid  = user.uid

            // Update Firebase Auth display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build()

            user.updateProfile(profileUpdates)
                .addOnSuccessListener {
                    // Also save to Realtime Database
                    db.child("users").child(uid).child("name")
                        .setValue(newName)
                        .addOnSuccessListener {
                            _isLoading.value    = false
                            _successMessage.value = "Name updated successfully!"
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            _isLoading.value  = false
                            _errorMessage.value = e.message
                        }
                }
                .addOnFailureListener { e ->
                    _isLoading.value  = false
                    _errorMessage.value = e.message
                }
        }
    }

    // ── Upload profile image ─────────────────────────────────────
    fun uploadProfileImage(imageUri: Uri, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        _isLoading.value = true

        val imageRef = storage.child("profile_images/$uid.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Get download URL
                imageRef.downloadUrl
                    .addOnSuccessListener { downloadUri ->
                        val imageUrl = downloadUri.toString()

                        // Save URL to Realtime Database
                        db.child("users").child(uid).child("profileImage")
                            .setValue(imageUrl)
                            .addOnSuccessListener {
                                // Update Firebase Auth photo
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri)
                                    .build()
                                auth.currentUser?.updateProfile(profileUpdates)

                                _profileImageUrl.value  = imageUrl
                                _isLoading.value        = false
                                _successMessage.value   = "Profile photo updated!"
                                onSuccess()
                            }
                    }
                    .addOnFailureListener { e ->
                        _isLoading.value    = false
                        _errorMessage.value = e.message
                    }
            }
            .addOnFailureListener { e ->
                _isLoading.value    = false
                _errorMessage.value = e.message
            }
    }

    fun clearMessages() {
        _successMessage.value = null
        _errorMessage.value   = null
    }
}