package com.christian.commonlink.navigation  // ✅ Correct


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.christian.commonlink.sokohub.navigation.ROUT_ABOUT
import com.christian.commonlink.sokohub.navigation.ROUT_ADD_EVENT
import com.christian.commonlink.sokohub.navigation.ROUT_EVENTS
import com.christian.commonlink.sokohub.navigation.ROUT_JOBS
import com.christian.commonlink.sokohub.navigation.ROUT_NOTICE_BOARD
import com.christian.commonlink.sokohub.navigation.ROUT_NOTIFICATIONS
import com.christian.commonlink.sokohub.navigation.ROUT_POST_DETAIL
import com.christian.commonlink.sokohub.navigation.ROUT_POST_NOTICE
import com.christian.commonlink.sokohub.navigation.ROUT_PROFILE
import com.christian.commonlink.sokohub.navigation.ROUT_SEE_ALL
import com.christian.commonlink.sokohub.navigation.ROUT_SERVICES
import com.christian.commonlink.sokohub.navigation.ROUT_VOLUNTEER
import com.christian.commonlink.ui.screens.About.AboutScreen
import com.christian.commonlink.ui.screens.EventsScreen.EventsScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.AddEventScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.NoticeBoardScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.NotificationsScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.PostDetailScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.PostNoticeScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.ProfileScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.SeeAllScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.VolunteerScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_ABOUT
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_ABOUT) {

        }
        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }

        composable(ROUT_POST_NOTICE) { PostNoticeScreen(navController) }
        composable(ROUT_ADD_EVENT)   { AddEventScreen(navController) }
        composable(ROUT_VOLUNTEER)   { VolunteerScreen(navController) }
        composable(ROUT_NOTIFICATIONS){ NotificationsScreen(navController) }
        composable(ROUT_PROFILE)     { ProfileScreen(navController) }
        composable(ROUT_NOTICE_BOARD){ NoticeBoardScreen(navController) }
        composable(ROUT_SEE_ALL)     { SeeAllScreen(navController) }
        composable(ROUT_POST_DETAIL) { PostDetailScreen(navController) }
        composable(ROUT_EVENTS) { EventsScreen(navController) }
        composable(ROUT_JOBS)     { JobsScreen(navController) }
        composable(ROUT_SERVICES) { ServicesScreen(navController) }
        composable(ROUT_ADD_EVENT) { AddEventScreen(navController) }

    }
}