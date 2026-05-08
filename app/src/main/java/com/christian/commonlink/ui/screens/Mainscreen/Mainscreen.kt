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
import com.christian.commonlink.ui.screens.EventsScreen.EventsScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.AddEventScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.NotificationsScreen

import com.christian.commonlink.ui.screens.home.Home
import com.christian.commonlink.ui.screens.jobs.AddJobScreen
import com.christian.commonlink.ui.screens.jobs.JobsScreen
import com.christian.commonlink.ui.screens.noticeboard.AddNoticeScreen
import com.christian.commonlink.ui.screens.noticeboard.NoticeBoardScreen

import com.christian.commonlink.ui.screens.PlaceholderScreens.PostDetailScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.PostNoticeScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.SeeAllScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.VolunteerScreen
import com.christian.commonlink.ui.screens.Profile.EditProfileScreen
import com.christian.commonlink.ui.screens.ServiceScreen.AddServiceScreen

import com.christian.commonlink.ui.screens.profile.ProfileScreen

import com.christian.commonlink.ui.screens.services.ServicesScreen
import com.christian.commonlink.ui.screens.navigation.bottomNavItems

private val RoyalPurple = Color(0xFF6B3FA0)
private val AccentGold  = Color(0xFFFFD166)

@Composable
fun MainScreen(outerNavController: NavController) {

    val innerNavController = rememberNavController()
    val navBackStackEntry  by innerNavController.currentBackStackEntryAsState()
    val currentRoute       = navBackStackEntry?.destination?.route

    // ── Screens that hide the bottom nav ─────────────────────────
    val hideBottomNavRoutes = listOf(
        ROUT_ADD_EVENT,
        ROUT_ADD_JOB,
        ROUT_ADD_SERVICE,
        ROUT_ADD_NOTICE,
        ROUT_POST_DETAIL,
        ROUT_EDIT_PROFILE,
        ROUT_NOTIFICATIONS,
        ROUT_SEE_ALL,
        ROUT_POST_NOTICE,
        ROUT_VOLUNTEER,
        ROUT_PROFILE
    )

    val showBottomNav = currentRoute !in hideBottomNavRoutes

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomNav,
                enter = fadeIn() + slideInVertically { it },
                exit  = fadeOut() + slideOutVertically { it }
            ) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 10.dp
                ) {
                    bottomNavItems.forEach { item ->

                        val isSelected = currentRoute == item.route

                        val scale by animateFloatAsState(
                            targetValue = if (isSelected) 1.2f else 1f,
                            label = "navScale"
                        )

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                innerNavController.navigate(item.route) {
                                    popUpTo(
                                        innerNavController.graph
                                            .findStartDestination().id
                                    ) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            },
                            icon = {
                                Box(contentAlignment = Alignment.Center) {

                                    // Active indicator background
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

                                    // Badge for notifications
                                    BadgedBox(
                                        badge = {
                                            if (item.route == ROUT_NOTIFICATIONS) {
                                                Badge(
                                                    containerColor = AccentGold
                                                ) {
                                                    Text(
                                                        text = "3",
                                                        fontSize = 10.sp
                                                    )
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector    = item.icon,
                                            contentDescription = item.label,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .scale(scale),
                                            tint = if (isSelected) RoyalPurple
                                            else Color(0xFFBBBBBB)
                                        )
                                    }
                                }
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold
                                    else FontWeight.Normal,
                                    color = if (isSelected) RoyalPurple
                                    else Color(0xFFBBBBBB)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController    = innerNavController,
            startDestination = ROUT_HOME,
            modifier         = Modifier.padding(paddingValues)
        ) {

            // ── Bottom nav screens ───────────────────────────────
            composable(ROUT_HOME) {
                Home(innerNavController)
            }

            composable(ROUT_EVENTS) {
                EventsScreen(innerNavController)
            }

            composable(ROUT_JOBS) {
                JobsScreen(innerNavController)
            }

            composable(ROUT_SERVICES) {
                ServicesScreen(innerNavController)
            }

            composable(ROUT_NOTICE_BOARD) {
                NoticeBoardScreen(innerNavController)
            }

            // ── Add screens ──────────────────────────────────────
            composable(ROUT_ADD_EVENT) {
                AddEventScreen(innerNavController)
            }

            composable(ROUT_ADD_JOB) {
                AddJobScreen(innerNavController)
            }

            composable(ROUT_ADD_SERVICE) {
                AddServiceScreen(innerNavController)
            }

            composable(ROUT_ADD_NOTICE) {
                AddNoticeScreen(innerNavController)
            }

            // ── Profile screens ──────────────────────────────────
            composable(ROUT_PROFILE) {
                ProfileScreen(navController = innerNavController)
            }

            composable(ROUT_EDIT_PROFILE) {
                EditProfileScreen(innerNavController)
            }

            // ── Other screens ────────────────────────────────────
            composable(ROUT_NOTIFICATIONS) {
                NotificationsScreen(innerNavController)
            }

            composable(ROUT_SEE_ALL) {
                SeeAllScreen(innerNavController)
            }

            composable(ROUT_POST_NOTICE) {
                PostNoticeScreen(innerNavController)
            }

            composable(ROUT_VOLUNTEER) {
                VolunteerScreen(innerNavController)
            }

            composable(ROUT_POST_DETAIL) {
                PostDetailScreen(innerNavController)
            }
        }
    }
}