package com.christian.commonlink.ui.screens.noticeboard

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


private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val BgColor      = Color(0xFFF5F3FB)
private val SubtitleGray = Color(0xFF888888)
private val ErrorRed     = Color(0xFFE53935)
private val UrgentRed    = Color(0xFFB71C1C)

@Composable
fun NoticeTextField(
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

@Composable
fun NoticeCategoryChip(
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

@Composable
fun AddNoticeScreen(
    navController: NavController,
    viewModel: NoticeViewModel = viewModel()
) {
    var title        by remember { mutableStateOf("") }
    var description  by remember { mutableStateOf("") }
    var postedBy     by remember { mutableStateOf("") }
    var date         by remember { mutableStateOf("") }
    var category     by remember { mutableStateOf("ANNOUNCEMENT") }
    var isUrgent     by remember { mutableStateOf(false) }

    var titleError       by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var postedByError    by remember { mutableStateOf(false) }
    var dateError        by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val noticeCategories = listOf(
        "ANNOUNCEMENT", "ALERT", "EVENT",
        "HEALTH", "SECURITY", "GENERAL"
    )

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "📌", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Notice Posted!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = DeepIndigo
                    )
                }
            },
            text = {
                Text(
                    text = "Your notice has been successfully posted to the community board.",
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
                    Text("Back to Notice Board", color = Color.White)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {

        // Header
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
                        text = "Post a Notice",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "  Share an announcement with your community",
                    fontSize = 13.sp,
                    color = Color(0xCCFFFFFF)
                )
            }
        }

        // Form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            NoticeTextField(
                value = title,
                onValueChange = { title = it; titleError = false },
                label = "Notice Title *",
                placeholder = "e.g. Water Interruption Notice",
                isError = titleError
            )

            NoticeTextField(
                value = description,
                onValueChange = { description = it; descriptionError = false },
                label = "Description *",
                placeholder = "Describe the notice in detail...",
                isError = descriptionError,
                singleLine = false,
                maxLines = 4
            )

            NoticeTextField(
                value = postedBy,
                onValueChange = { postedBy = it; postedByError = false },
                label = "Posted By *",
                placeholder = "e.g. Nairobi Water",
                isError = postedByError,
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Posted By",
                        tint = if (postedByError) ErrorRed else RoyalPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            NoticeTextField(
                value = date,
                onValueChange = { date = it; dateError = false },
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

            // Category chips
            Column {
                Text(
                    text = "Category",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepIndigo,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    noticeCategories.take(3).forEach { cat ->
                        NoticeCategoryChip(
                            label = cat,
                            isSelected = category == cat,
                            onClick = { category = cat }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    noticeCategories.drop(3).forEach { cat ->
                        NoticeCategoryChip(
                            label = cat,
                            isSelected = category == cat,
                            onClick = { category = cat }
                        )
                    }
                }
            }

            // Urgent toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isUrgent) Color(0xFFFFEBEE) else Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Mark as Urgent 🚨",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = if (isUrgent) UrgentRed else DeepIndigo
                    )
                    Text(
                        text = "Urgent notices appear at the top",
                        fontSize = 12.sp,
                        color = SubtitleGray
                    )
                }
                Switch(
                    checked = isUrgent,
                    onCheckedChange = { isUrgent = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = UrgentRed
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Submit button
            Button(
                onClick = {
                    titleError       = title.isBlank()
                    descriptionError = description.isBlank()
                    postedByError    = postedBy.isBlank()
                    dateError        = date.isBlank()

                    val allValid = !titleError && !descriptionError &&
                            !postedByError && !dateError

                    if (allValid) {
                        val newNotice = Notice(
                            title = title,
                            description = description,
                            postedBy = postedBy,
                            date = date,
                            category = category,
                            urgent = isUrgent
                        )
                        viewModel.addNotice(newNotice)
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
                    text = "Post Notice 📌",
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
fun AddNoticeScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3FB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1A1040), Color(0xFF6B3FA0))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Post a Notice",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}