package com.christian.commonlink.ui.screens.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        label = "Home",
        icon = Icons.Default.Home,
        route = "home"
    ),
    BottomNavItem(
        label = "Events",
        icon = Icons.Default.DateRange,
        route = "events"
    ),
    BottomNavItem(
        label = "Jobs",
        icon = Icons.Default.Work,
        route = "jobs"
    ),
    BottomNavItem(
        label = "Services",
        icon = Icons.Default.Star,
        route = "services"
    ),
    BottomNavItem(
        label = "Notices",
        icon = Icons.Default.Notifications,
        route = "notice_board"
    )
)