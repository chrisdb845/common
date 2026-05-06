package com.christian.commonlink.ui.screens.Mainscreen

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*

import com.christian.commonlink.navigation.*
import com.christian.commonlink.ui.screens.EditProfileScreen
import com.christian.commonlink.ui.screens.EventsScreen.EventsScreen
import com.christian.commonlink.ui.screens.home.Home
import com.christian.commonlink.ui.screens.jobs.AddJobScreen
import com.christian.commonlink.ui.screens.jobs.JobsScreen

import com.christian.commonlink.ui.screens.PlaceholderScreens.*
import com.christian.commonlink.ui.screens.ServiceScreen.AddServiceScreen
import com.christian.commonlink.ui.screens.services.ServicesScreen
import com.christian.commonlink.ui.screens.profile.ProfileScreen
import com.christian.commonlink.ui.screens.noticeboard.AddNoticeScreen
import com.christian.commonlink.ui.screens.navigation.bottomNavItems

// Colors
private val RoyalPurple = Color(0xFF6B3FA0)
private val AccentGold  = Color(0xFFFFD166)

@Composable
fun MainScreen(outerNavController: NavController) {

    val innerNavController = rememberNavController()
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val hideBottomNavRoutes = listOf(
        ROUT_ADD_EVENT,
        ROUT_ADD_JOB,
        ROUT_ADD_SERVICE,
        ROUT_ADD_NOTICE,
        ROUT_PROFILE,
        ROUT_NOTIFICATIONS,
        ROUT_POST_DETAIL,
        ROUT_SEE_ALL,
        ROUT_POST_NOTICE,
        ROUT_VOLUNTEER
    )

    val showBottomNav = currentRoute !in hideBottomNavRoutes

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomNav,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        innerNavController.navigate(route) {
                            popUpTo(innerNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = innerNavController,
            startDestination = ROUT_HOME,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(ROUT_HOME)         { Home(innerNavController) }
            composable(ROUT_EVENTS)       { EventsScreen(innerNavController) }
            composable(ROUT_JOBS)         { JobsScreen(innerNavController) }
            composable(ROUT_SERVICES)     { ServicesScreen(innerNavController) }
            composable(ROUT_NOTICE_BOARD) { NoticeBoardScreen(innerNavController) }

            composable(ROUT_ADD_EVENT)    { AddEventScreen(innerNavController) }
            composable(ROUT_ADD_JOB)      { AddJobScreen(innerNavController) }
            composable(ROUT_ADD_SERVICE)  { AddServiceScreen(innerNavController) }
            composable(ROUT_ADD_NOTICE)   { AddNoticeScreen(innerNavController) }
            composable(ROUT_PROFILE)      { ProfileScreen(innerNavController) }
            composable(ROUT_NOTIFICATIONS){ NotificationsScreen(innerNavController) }
            composable(ROUT_POST_DETAIL)  { PostDetailScreen(innerNavController) }
            composable(ROUT_SEE_ALL)      { SeeAllScreen(innerNavController) }
            composable(ROUT_POST_NOTICE)  { PostNoticeScreen(innerNavController) }
            composable(ROUT_VOLUNTEER)    { VolunteerScreen(innerNavController) }
            composable(ROUT_EDIT_PROFILE) { EditProfileScreen(innerNavController) }
        }
    }
}

// 🔥 PREMIUM Bottom Nav
@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White.copy(alpha = 0.9f),
        tonalElevation = 10.dp
    ) {
        bottomNavItems.forEach { item ->

            val isSelected = currentRoute == item.route

            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.2f else 1f,
                label = "scale"
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item.route) },

                icon = {
                    Box(contentAlignment = Alignment.Center) {

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                RoyalPurple.copy(alpha = 0.2f),
                                                RoyalPurple.copy(alpha = 0.05f)
                                            )
                                        )
                                    )
                            )
                        }

                        BadgedBox(
                            badge = {
                                if (item.route == ROUT_NOTIFICATIONS) {
                                    Badge(containerColor = AccentGold) {
                                        Text("3", fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier
                                    .size(24.dp)
                                    .scale(scale),
                                tint = if (isSelected) RoyalPurple else Color(0xFFBBBBBB)
                            )
                        }
                    }
                },

                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) RoyalPurple else Color(0xFFBBBBBB)
                    )
                },

                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}