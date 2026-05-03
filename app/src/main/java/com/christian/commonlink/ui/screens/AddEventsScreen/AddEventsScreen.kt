package com.christian.commonlink.ui.screens.AddEventsScreen

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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.christian.commonlink.ui.screens.events.EventViewModel

// ── Brand palette ────────────────────────────────────────────────
private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val SoftViolet   = Color(0xFF9B6FD4)
private val AccentGold   = Color(0xFFFFD166)
private val BgColor      = Color(0xFFF5F3FB)
private val SubtitleGray = Color(0xFF888888)
private val ErrorRed     = Color(0xFFE53935)

// ── Reusable styled text field ───────────────────────────────────
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
                Text(
                    text = placeholder,
                    fontSize = 13.sp,
                    color = SubtitleGray
                )
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
                    color = if (isError) ErrorRed
                    else if (value.isNotEmpty()) RoyalPurple
                    else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(14.dp)
                ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
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

// ── Category selector chip ───────────────────────────────────────
@Composable
fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onClick() }
            .background(
                color = if (isSelected) RoyalPurple else Color.White,
                shape = RoundedCornerShape(50)
            )
            .border(
                width = 1.5.dp,
                color = if (isSelected) RoyalPurple else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 9.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else DeepIndigo
        )
    }
}

// ── Main Add Event Screen ────────────────────────────────────────
@Composable
fun AddEventScreen(
    navController: NavController,
    viewModel: EventViewModel = viewModel()
) {

    // ── Form fields ──────────────────────────────────────────────
    var title       by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date        by remember { mutableStateOf("") }
    var time        by remember { mutableStateOf("") }
    var location    by remember { mutableStateOf("") }
    var organizer   by remember { mutableStateOf("") }
    var category    by remember { mutableStateOf("EVENT") }

    // ── Validation error states ──────────────────────────────────
    var titleError       by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var dateError        by remember { mutableStateOf(false) }
    var timeError        by remember { mutableStateOf(false) }
    var locationError    by remember { mutableStateOf(false) }
    var organizerError   by remember { mutableStateOf(false) }

    // ── Success dialog state ─────────────────────────────────────
    var showSuccessDialog by remember { mutableStateOf(false) }

    // ── Category options ─────────────────────────────────────────
    val categoryOptions = listOf("EVENT", "JOBS", "SERVICE", "NEWS", "HEALTH")

    // ── Success dialog ───────────────────────────────────────────
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "🎉", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Event Posted!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = DeepIndigo
                    )
                }
            },
            text = {
                Text(
                    text = "Your event has been successfully added to the community board.",
                    fontSize = 14.sp,
                    color = SubtitleGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalPurple),
                    shape = RoundedCornerShape(12.dp),
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

        // ── GRADIENT HEADER ──────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(DeepIndigo, RoyalPurple))
                )
                .padding(start = 8.dp, top = 48.dp, end = 20.dp, bottom = 28.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add New Event",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "  Fill in the details to post your event",
                    fontSize = 13.sp,
                    color = Color(0xCCFFFFFF)
                )
            }
        }

        // ── FORM BODY ────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // ── Event Title ──────────────────────────────────────
            EventTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = false
                },
                label = "Event Title *",
                placeholder = "e.g. Community Clean-Up",
                isError = titleError
            )

            // ── Description ──────────────────────────────────────
            EventTextField(
                value = description,
                onValueChange = {
                    description = it
                    descriptionError = false
                },
                label = "Description *",
                placeholder = "Describe your event...",
                isError = descriptionError,
                singleLine = false,
                maxLines = 4
            )

            // ── Date ─────────────────────────────────────────────
            EventTextField(
                value = date,
                onValueChange = {
                    date = it
                    dateError = false
                },
                label = "Date *",
                placeholder = "e.g. Saturday, May 10 2026",
                isError = dateError,
                leadingIcon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = if (dateError) ErrorRed else RoyalPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            // ── Time ─────────────────────────────────────────────
            EventTextField(
                value = time,
                onValueChange = {
                    time = it
                    timeError = false
                },
                label = "Time *",
                placeholder = "e.g. 8:00 AM",
                isError = timeError,
                leadingIcon = {
                    Text(
                        text = "⏰",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            )

            // ── Location ─────────────────────────────────────────
            EventTextField(
                value = location,
                onValueChange = {
                    location = it
                    locationError = false
                },
                label = "Location *",
                placeholder = "e.g. Westlands Park, Nairobi",
                isError = locationError,
                leadingIcon = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = if (locationError) ErrorRed else RoyalPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            // ── Organizer ────────────────────────────────────────
            EventTextField(
                value = organizer,
                onValueChange = {
                    organizer = it
                    organizerError = false
                },
                label = "Organizer *",
                placeholder = "e.g. CommonLink Team",
                isError = organizerError,
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Organizer",
                        tint = if (organizerError) ErrorRed else RoyalPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            // ── Category selector ────────────────────────────────
            Column {
                Text(
                    text = "Category",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepIndigo,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categoryOptions.forEach { option ->
                        CategoryChip(
                            label = option,
                            isSelected = category == option,
                            onClick = { category = option }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Submit button ────────────────────────────────────
            Button(
                onClick = {
                    // Validate all fields
                    titleError       = title.isBlank()
                    descriptionError = description.isBlank()
                    dateError        = date.isBlank()
                    timeError        = time.isBlank()
                    locationError    = location.isBlank()
                    organizerError   = organizer.isBlank()

                    // If all valid — save to Firebase
                    val allValid = !titleError && !descriptionError &&
                            !dateError  && !timeError &&
                            !locationError && !organizerError

                    if (allValid) {
                        val newEvent = Event(
                            title       = title,
                            description = description,
                            date        = date,
                            time        = time,
                            location    = location,
                            organizer   = organizer,
                            category    = category
                        )
                        viewModel.addEvent(newEvent)  // ✅ saves to Firebase
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalPurple),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Post Event 📣",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEventScreenPreview() {
    AddEventScreen(rememberNavController())
}