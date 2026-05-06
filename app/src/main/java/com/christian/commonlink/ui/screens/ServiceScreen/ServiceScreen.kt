package com.christian.commonlink.ui.screens.services

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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.christian.commonlink.navigation.ROUT_ADD_SERVICE
import androidx.compose.foundation.layout.Arrangement


private val DeepIndigo   = Color(0xFF1A1040)
private val RoyalPurple  = Color(0xFF6B3FA0)
private val AccentGold   = Color(0xFFFFD166)
private val CardWhite    = Color(0xFFFFFFFF)
private val SubtitleGray = Color(0xFF888888)
private val BgColor      = Color(0xFFF5F3FB)
private val TealColor    = Color(0xFF00695C)
private val TealBg       = Color(0xFFE0F2F1)

@Composable
fun ServiceCard(service: Service, onClick: () -> Unit) {
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
                            colors = listOf(TealColor, RoyalPurple)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                // Category badge + rating row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category badge
                    Box(
                        modifier = Modifier
                            .background(TealBg, shape = RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = service.category,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealColor
                        )
                    }

                    // Star rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⭐", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = service.rating,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepIndigo
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Service title
                Text(
                    text = service.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = DeepIndigo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Provider name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Provider",
                        tint = TealColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = service.provider,
                        fontSize = 13.sp,
                        color = TealColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Description
                Text(
                    text = service.description,
                    fontSize = 13.sp,
                    color = SubtitleGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

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
                            text = service.location,
                            fontSize = 12.sp,
                            color = SubtitleGray
                        )
                    }

                    // Phone
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = "Phone",
                            tint = TealColor,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = service.phone,
                            fontSize = 12.sp,
                            color = TealColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyServicesState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🔧", fontSize = 56.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Services Yet",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = DeepIndigo
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Be the first to list a service!",
            fontSize = 14.sp,
            color = SubtitleGray
        )
    }
}

@Composable
fun ServicesScreen(
    navController: NavController,
    viewModel: ServiceViewModel = viewModel()
) {
    val services  by viewModel.services.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error     by viewModel.errorMessage.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ROUT_ADD_SERVICE) },
                containerColor = TealColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Service")
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
                            listOf(Color(0xFF004D40), TealColor)
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
                            text = "Local Services",
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
                            text = "Trusted providers near you",
                            fontSize = 13.sp,
                            color = Color(0xCCFFFFFF)
                        )
                        Box(
                            modifier = Modifier
                                .background(AccentGold, shape = RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${services.size} Services",
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
                                color = TealColor,
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Loading services...",
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
                                text = "Failed to load services",
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

                services.isEmpty() -> EmptyServicesState()

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(services) { service ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically(),
                                exit = fadeOut()
                            ) {
                                ServiceCard(service = service, onClick = {})
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
fun ServicesScreenPreview() {
    val mockServices = listOf(
        Service(
            id = "1",
            title = "Plumbing Services",
            provider = "John Kamau",
            description = "Professional plumbing repairs and installations across Nairobi",
            location = "Nairobi Wide",
            phone = "0712345678",
            category = "Plumbing",
            rating = "4.8"
        ),
        Service(
            id = "2",
            title = "Electrical Repairs",
            provider = "Peter Odhiambo",
            description = "Certified electrician for home and office wiring and repairs",
            location = "Westlands, Nairobi",
            phone = "0723456789",
            category = "Electric",
            rating = "4.6"
        ),
        Service(
            id = "3",
            title = "Home Cleaning",
            provider = "Mary Wanjiru",
            description = "Thorough home and office cleaning services at affordable rates",
            location = "Kileleshwa, Nairobi",
            phone = "0734567890",
            category = "Cleaning",
            rating = "4.9"
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
                        listOf(Color(0xFF004D40), Color(0xFF00695C))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Local Services",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${mockServices.size} Services Available",
                    fontSize = 12.sp,
                    color = Color(0xCCFFFFFF)
                )
            }
        }
        mockServices.forEach { service ->
            ServiceCard(service = service, onClick = {})
        }
    }
}