package com.christian.commonlink.ui.screens.events


import com.christian.commonlink.models.Event
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

// ── Brand colors ─────────────────────────────────────────────────
private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val SoftPurple   = Color(0xFF8E63CE)
private val BgColor      = Color(0xFFF5F3FB)
private val SubtitleGray = Color(0xFF888888)
private val ErrorRed     = Color(0xFFE53935)

// ── Reusable text field ───────────────────────────────────────────
@Composable
fun EventTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DeepIndigo,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = placeholder, fontSize = 13.sp, color = SubtitleGray)
            },
            leadingIcon = leadingIcon,
            singleLine = singleLine,
            maxLines = maxLines,
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(
                    width = 1.5.dp,
                    color = when {
                        isError        -> ErrorRed
                        value.isNotEmpty() -> RoyalPurple
                        else           -> Color(0xFFE0E0E0)
                    },
                    shape = RoundedCornerShape(14.dp)
                ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor   = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor   = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor     = Color.Transparent
            ),
            shape = RoundedCornerShape(14.dp)
        )
        if (isError) {
            Text(
                text = "This field is required",
                fontSize = 11.sp,
                color = ErrorRed,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

// ── Category chip ────────────────────────────────────────────────
@Composable
fun EventCategoryChip(
    label: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onClick() }
            .background(
                if (isSelected) RoyalPurple else Color.White
            )
            .border(
                width = 1.5.dp,
                color = if (isSelected) RoyalPurple else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 9.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else DeepIndigo
            )
        }
    }
}

// ── Main Add Event Screen ────────────────────────────────────────
@Composable
fun AddEventScreen(navController: NavController) {

    // ── Form state ───────────────────────────────────────────────
    var title        by remember { mutableStateOf("") }
    var description  by remember { mutableStateOf("") }
    var date         by remember { mutableStateOf("") }
    var time         by remember { mutableStateOf("") }
    var location     by remember { mutableStateOf("") }
    var organizer    by remember { mutableStateOf("") }
    var category     by remember { mutableStateOf("EVENT") }
    var imageUri     by remember { mutableStateOf<Uri?>(null) }

    // ── Validation errors ────────────────────────────────────────
    var titleError       by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var dateError        by remember { mutableStateOf(false) }
    var timeError        by remember { mutableStateOf(false) }
    var locationError    by remember { mutableStateOf(false) }
    var organizerError   by remember { mutableStateOf(false) }

    // ── Loading / dialog states ──────────────────────────────────
    var isLoading         by remember { mutableStateOf(false) }
    var errorMessage      by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val categories = listOf(
        Pair("EVENT",   "📅"),
        Pair("JOBS",    "💼"),
        Pair("SERVICE", "🔧"),
        Pair("NEWS",    "📰"),
        Pair("HEALTH",  "💊")
    )

    // ── Image picker ─────────────────────────────────────────────
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    // ── Save event function ──────────────────────────────────────
    fun saveEvent(imageUrl: String) {
        // Firebase initialization moved inside functions to avoid crash in Android Studio Preview
        val dbRef = FirebaseDatabase.getInstance().reference.child("events")
        val uid   = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        
        val newRef = dbRef.push()
        val event  = Event(
            id          = newRef.key ?: "",
            title       = title,
            description = description,
            imageUrl    = imageUrl,
            date        = date,
            time        = time,
            location    = location,
            organizer   = organizer,
            category    = category,
            createdBy   = uid
        )
        newRef.setValue(event)
            .addOnSuccessListener {
                isLoading         = false
                showSuccessDialog = true
            }
            .addOnFailureListener { e ->
                isLoading    = false
                errorMessage = e.message
            }
    }

    // ── Upload image then save ───────────────────────────────────
    fun uploadAndSave() {
        isLoading = true
        if (imageUri != null) {
            // Firebase initialization moved inside functions to avoid crash in Android Studio Preview
            val storageRef = FirebaseStorage.getInstance().reference.child("event_images")
            val uid        = FirebaseAuth.getInstance().currentUser?.uid ?: ""

            val imageRef = storageRef.child("${uid}_${System.currentTimeMillis()}.jpg")
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        saveEvent(downloadUri.toString())
                    }
                }
                .addOnFailureListener { e ->
                    isLoading    = false
                    errorMessage = e.message
                }
        } else {
            saveEvent("")
        }
    }

    // ── Success dialog ───────────────────────────────────────────
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor   = Color.White,
            shape            = RoundedCornerShape(20.dp),
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🎉", fontSize = 52.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text       = "Event Posted!",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 22.sp,
                        color      = DeepIndigo
                    )
                }
            },
            text = {
                Text(
                    text     = "Your event has been successfully added to the community board.",
                    color    = SubtitleGray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RoyalPurple
                    ),
                    shape    = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Events", color = Color.White)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {

        // ── HEADER ───────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(DeepIndigo, SoftPurple))
                )
                .padding(start = 8.dp, top = 48.dp, end = 20.dp, bottom = 28.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text       = "Create Event",
                        fontSize   = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text     = "  Share an event with your community",
                    fontSize = 13.sp,
                    color    = Color(0xCCFFFFFF)
                )
            }
        }

        // ── FORM ─────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // Image picker
            Column {
                Text(
                    text       = "Event Image (Optional)",
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = DeepIndigo,
                    modifier   = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(
                            width  = 1.5.dp,
                            color  = if (imageUri != null) RoyalPurple
                            else Color(0xFFE0E0E0),
                            shape  = RoundedCornerShape(18.dp)
                        )
                        .background(Color.White)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        AsyncImage(
                            model            = imageUri,
                            contentDescription = null,
                            contentScale     = ContentScale.Crop,
                            modifier         = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🖼️", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text     = "Tap to upload event image",
                                fontSize = 13.sp,
                                color    = SubtitleGray
                            )
                        }
                    }
                }
            }

            // Title
            EventTextField(
                value         = title,
                onValueChange = { title = it; titleError = false },
                label         = "Event Title *",
                placeholder   = "e.g. Community Clean-Up",
                isError       = titleError
            )

            // Description
            EventTextField(
                value         = description,
                onValueChange = { description = it; descriptionError = false },
                label         = "Description *",
                placeholder   = "Describe your event...",
                isError       = descriptionError,
                singleLine    = false,
                maxLines      = 4
            )

            // Date + Time
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    EventTextField(
                        value         = date,
                        onValueChange = { date = it; dateError = false },
                        label         = "Date *",
                        placeholder   = "May 10, 2026",
                        isError       = dateError,
                        leadingIcon   = {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = null,
                                tint = RoyalPurple
                            )
                        }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    EventTextField(
                        value         = time,
                        onValueChange = { time = it; timeError = false },
                        label         = "Time *",
                        placeholder   = "8:00 AM",
                        isError       = timeError,
                        leadingIcon   = { Text("⏰") }
                    )
                }
            }

            // Location
            EventTextField(
                value         = location,
                onValueChange = { location = it; locationError = false },
                label         = "Location *",
                placeholder   = "e.g. Westlands Park, Nairobi",
                isError       = locationError,
                leadingIcon   = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = RoyalPurple
                    )
                }
            )

            // Organizer
            EventTextField(
                value         = organizer,
                onValueChange = { organizer = it; organizerError = false },
                label         = "Organizer *",
                placeholder   = "e.g. CommonLink Team",
                isError       = organizerError,
                leadingIcon   = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = RoyalPurple
                    )
                }
            )

            // Category chips
            Column {
                Text(
                    text       = "Category",
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = DeepIndigo,
                    modifier   = Modifier.padding(bottom = 10.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.take(3).forEach { (label, emoji) ->
                        EventCategoryChip(
                            label      = label,
                            emoji      = emoji,
                            isSelected = category == label,
                            onClick    = { category = label }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.drop(3).forEach { (label, emoji) ->
                        EventCategoryChip(
                            label      = label,
                            emoji      = emoji,
                            isSelected = category == label,
                            onClick    = { category = label }
                        )
                    }
                }
            }

            // Error banner
            errorMessage?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFEBEE), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(text = "⚠️ $it", color = ErrorRed, fontSize = 13.sp)
                }
            }

            // Submit button
            Button(
                onClick = {
                    titleError       = title.isBlank()
                    descriptionError = description.isBlank()
                    dateError        = date.isBlank()
                    timeError        = time.isBlank()
                    locationError    = location.isBlank()
                    organizerError   = organizer.isBlank()

                    val valid = !titleError && !descriptionError &&
                            !dateError  && !timeError &&
                            !locationError && !organizerError

                    if (valid) uploadAndSave()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors  = ButtonDefaults.buttonColors(
                    containerColor = RoyalPurple
                ),
                shape   = RoundedCornerShape(14.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    Row(
                        verticalAlignment    = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier    = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color       = Color.White
                        )
                        Text(text = "Posting Event...", color = Color.White)
                    }
                } else {
                    Text(
                        text       = "Post Event 📣",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEventScreenPreview() {
    AddEventScreen(rememberNavController())
}