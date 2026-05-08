package com.christian.commonlink.ui.screens.jobs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.christian.commonlink.navigation.ROUT_ADD_JOB
import androidx.compose.foundation.layout.Arrangement


private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val SoftViolet   = Color(0xFF9B6FD4)
private val AccentGold   = Color(0xFFFFD166)
private val CardWhite    = Color(0xFFFFFFFF)
private val SubtitleGray = Color(0xFF888888)
private val BgColor      = Color(0xFFF5F3FB)
private val GreenTag     = Color(0xFF2E7D32)
private val GreenTagBg   = Color(0xFFE8F5E9)

@Composable
fun JobCard(job: Job, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            // Left accent bar
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(160.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF43A047), RoyalPurple)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                // Job type badge + posted by
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(GreenTagBg, shape = RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = job.type,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenTag
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Posted by",
                            tint = SubtitleGray,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = job.postedBy,
                            fontSize = 11.sp,
                            color = SubtitleGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Job title
                Text(
                    text = job.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = DeepIndigo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Company name
                Text(
                    text = job.company,
                    fontSize = 13.sp,
                    color = RoyalPurple,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description
                Text(
                    text = job.description,
                    fontSize = 13.sp,
                    color = SubtitleGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Salary
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💰", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = job.salary,
                        fontSize = 12.sp,
                        color = GreenTag,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Location
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = SubtitleGray,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = job.location,
                            fontSize = 12.sp,
                            color = SubtitleGray
                        )
                    }

                    // Deadline
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Deadline",
                            tint = SubtitleGray,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = job.deadline,
                            fontSize = 12.sp,
                            color = SubtitleGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyJobsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "💼", fontSize = 56.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Jobs Yet",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = DeepIndigo
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Be the first to post a job opportunity!",
            fontSize = 14.sp,
            color = SubtitleGray
        )
    }
}

@Composable
fun JobsScreen(
    navController: NavController,
    viewModel: JobViewModel = viewModel()
) {
    val jobs      by viewModel.jobs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error     by viewModel.errorMessage.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ROUT_ADD_JOB) },
                containerColor = Color(0xFF2E7D32),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Job")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BgColor)
                .padding(paddingValues)
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
                            text = "Job Opportunities",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Find opportunities near you",
                            fontSize = 13.sp,
                            color = Color(0xCCFFFFFF)
                        )
                        Box(
                            modifier = Modifier
                                .background(AccentGold, shape = RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${jobs.size} Jobs",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepIndigo
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = Color(0xFF2E7D32),
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Loading jobs...",
                                fontSize = 13.sp,
                                color = SubtitleGray
                            )
                        }
                    }
                }

                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "⚠️", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Failed to load jobs",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DeepIndigo
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = error ?: "",
                                fontSize = 13.sp,
                                color = SubtitleGray
                            )
                        }
                    }
                }

                jobs.isEmpty() -> EmptyJobsState()

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(jobs) { job ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically(),
                                exit = fadeOut()
                            ) {
                                JobCard(job = job, onClick = {})
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JobsScreenPreview() {
    val mockJobs = listOf(
        Job(
            id = "1",
            title = "Software Developer",
            company = "TechCorp Nairobi",
            description = "Looking for a junior Android developer with Kotlin experience",
            location = "Westlands, Nairobi",
            salary = "KSH 50,000 - 80,000",
            type = "Full-time",
            postedBy = "TechCorp HR",
            deadline = "May 30 2026"
        ),
        Job(
            id = "2",
            title = "Graphic Designer",
            company = "Creative Studio",
            description = "Talented designer needed for branding and social media",
            location = "CBD, Nairobi",
            salary = "KSH 30,000 - 50,000",
            type = "Part-time",
            postedBy = "Studio Manager",
            deadline = "June 5 2026"
        ),
        Job(
            id = "3",
            title = "Marketing Intern",
            company = "StartupKE",
            description = "Exciting internship opportunity at a fast growing startup",
            location = "Remote",
            salary = "KSH 15,000",
            type = "Internship",
            postedBy = "HR StartupKE",
            deadline = "June 10 2026"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3FB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Preview header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1B5E20), Color(0xFF2E7D32))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Job Opportunities",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${mockJobs.size} Jobs Available",
                    fontSize = 12.sp,
                    color = Color(0xCCFFFFFF)
                )
            }
        }
        mockJobs.forEach { job ->
            JobCard(job = job, onClick = {})
        }
    }
}
