package com.christian.commonlink.ui.screens.jobs

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val BgColor      = Color(0xFFF5F3FB)
private val SubtitleGray = Color(0xFF888888)
private val ErrorRed     = Color(0xFFE53935)
private val JobGreen     = Color(0xFF2E7D32)

@Composable
fun JobTextField(
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
                    else if (value.isNotEmpty()) JobGreen
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
fun JobTypeChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onClick() }
            .background(
                color = if (isSelected) JobGreen else Color.White,
                shape = RoundedCornerShape(50)
            )
            .border(
                width = 1.5.dp,
                color = if (isSelected) JobGreen else Color(0xFFE0E0E0),
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
fun AddJobScreen(
    navController: NavController,
    viewModel: JobViewModel = viewModel()
) {
    var title       by remember { mutableStateOf("") }
    var company     by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location    by remember { mutableStateOf("") }
    var salary      by remember { mutableStateOf("") }
    var postedBy    by remember { mutableStateOf("") }
    var deadline    by remember { mutableStateOf("") }
    var jobType     by remember { mutableStateOf("Full-time") }

    var titleError       by remember { mutableStateOf(false) }
    var companyError     by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var locationError    by remember { mutableStateOf(false) }
    var salaryError      by remember { mutableStateOf(false) }
    var postedByError    by remember { mutableStateOf(false) }
    var deadlineError    by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val jobTypes = listOf("Full-time", "Part-time", "Contract", "Internship", "Remote")

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
                    Text(text = "🎉", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Job Posted!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = DeepIndigo
                    )
                }
            },
            text = {
                Text(
                    text = "Your job has been successfully posted to the community board.",
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
                    colors = ButtonDefaults.buttonColors(containerColor = JobGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Jobs", color = Color.White)
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
                    Brush.verticalGradient(
                        listOf(Color(0xFF1B5E20), Color(0xFF2E7D32))
                    )
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
                        text = "Post a Job",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "  Fill in the details to post your job",
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

            JobTextField(
                value = title,
                onValueChange = { title = it; titleError = false },
                label = "Job Title *",
                placeholder = "e.g. Software Developer",
                isError = titleError
            )

            JobTextField(
                value = company,
                onValueChange = { company = it; companyError = false },
                label = "Company Name *",
                placeholder = "e.g. TechCorp Nairobi",
                isError = companyError,
                leadingIcon = {
                    Text(text = "🏢", fontSize = 18.sp,
                        modifier = Modifier.padding(start = 4.dp))
                }
            )

            JobTextField(
                value = description,
                onValueChange = { description = it; descriptionError = false },
                label = "Job Description *",
                placeholder = "Describe the role and requirements...",
                isError = descriptionError,
                singleLine = false,
                maxLines = 4
            )

            JobTextField(
                value = salary,
                onValueChange = { salary = it; salaryError = false },
                label = "Salary Range *",
                placeholder = "e.g. KSH 50,000 - 80,000",
                isError = salaryError,
                leadingIcon = {
                    Text(text = "💰", fontSize = 18.sp,
                        modifier = Modifier.padding(start = 4.dp))
                }
            )

            JobTextField(
                value = location,
                onValueChange = { location = it; locationError = false },
                label = "Location *",
                placeholder = "e.g. Westlands, Nairobi",
                isError = locationError,
                leadingIcon = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = if (locationError) ErrorRed else JobGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            JobTextField(
                value = deadline,
                onValueChange = { deadline = it; deadlineError = false },
                label = "Application Deadline *",
                placeholder = "e.g. Saturday, May 30 2026",
                isError = deadlineError,
                leadingIcon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Deadline",
                        tint = if (deadlineError) ErrorRed else JobGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            JobTextField(
                value = postedBy,
                onValueChange = { postedBy = it; postedByError = false },
                label = "Posted By *",
                placeholder = "e.g. TechCorp HR",
                isError = postedByError,
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Posted By",
                        tint = if (postedByError) ErrorRed else JobGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            // Job type chips
            Column {
                Text(
                    text = "Job Type",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepIndigo,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    jobTypes.take(3).forEach { type ->
                        JobTypeChip(
                            label = type,
                            isSelected = jobType == type,
                            onClick = { jobType = type }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    jobTypes.drop(3).forEach { type ->
                        JobTypeChip(
                            label = type,
                            isSelected = jobType == type,
                            onClick = { jobType = type }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Submit button
            Button(
                onClick = {
                    titleError       = title.isBlank()
                    companyError     = company.isBlank()
                    descriptionError = description.isBlank()
                    locationError    = location.isBlank()
                    salaryError      = salary.isBlank()
                    postedByError    = postedBy.isBlank()
                    deadlineError    = deadline.isBlank()

                    val allValid = !titleError && !companyError &&
                            !descriptionError && !locationError &&
                            !salaryError && !postedByError && !deadlineError

                    if (allValid) {
                        val newJob = Job(
                            title       = title,
                            company     = company,
                            description = description,
                            location    = location,
                            salary      = salary,
                            postedBy    = postedBy,
                            deadline    = deadline,
                            type        = jobType
                        )
                        viewModel.addJob(newJob)
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JobGreen),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Post Job 💼",
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
fun AddJobScreenPreview() {
    AddJobScreen(rememberNavController())
}

