package com.christian.commonlink.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.christian.commonlink.ui.screens.About.AboutScreen
import com.christian.commonlink.ui.screens.Authentification.LoginScreen
import com.christian.commonlink.ui.screens.Authentification.RegisterScreen
import com.christian.commonlink.ui.screens.EventsScreen.EventsScreen
import com.christian.commonlink.ui.screens.NoticeBoard.AddNoticeScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.AddEventScreen

import com.christian.commonlink.ui.screens.home.Home
import com.christian.commonlink.ui.screens.jobs.AddJobScreen
import com.christian.commonlink.ui.screens.jobs.JobsScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.NoticeBoardScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.NotificationsScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.PostDetailScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.PostNoticeScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.ProfileScreen
import com.christian.commonlink.ui.screens.PlaceholderScreens.SeeAllScreen

import com.christian.commonlink.ui.screens.PlaceholderScreens.VolunteerScreen
import com.christian.commonlink.ui.screens.ServiceScreen.AddServiceScreen
import com.christian.commonlink.ui.screens.services.ServicesScreen
import com.christian.commonlink.ui.screens.splash.SplashScreen

// ── All routes defined in one place ─────────────────────────────
// ── All routes defined in one place ─────────────────────────────
const val ROUT_LOGIN         = "login"       // ✅ Added
const val ROUT_ABOUT         = "about"
const val ROUT_HOME          = "home"
const val ROUT_EVENTS        = "events"
const val ROUT_ADD_EVENT     = "add_event"
const val ROUT_JOBS          = "jobs"
const val ROUT_ADD_JOB       = "add_job"
const val ROUT_SERVICES      = "services"
const val ROUT_POST_NOTICE   = "post_notice"
const val ROUT_VOLUNTEER     = "volunteer"
const val ROUT_NOTIFICATIONS = "notifications"
const val ROUT_PROFILE       = "profile"
const val ROUT_NOTICE_BOARD  = "notice_board"
const val ROUT_SEE_ALL       = "see_all"
const val ROUT_POST_DETAIL   = "post_detail"
const val ROUT_ADD_SERVICE = "add_service"

const val ROUT_SPLASH = "splash"
const val ROUT_ADD_NOTICE = "add_notice"
const val ROUT_REGISTER = "register"




@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH  // ✅ change to ROUT_HOME once login is ready
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ✅ Every route registered exactly once
        composable(ROUT_ABOUT)         { AboutScreen(navController) }
        composable(ROUT_HOME)          { Home(navController) }          // ✅ Added
        composable(ROUT_EVENTS)        { EventsScreen(navController) }
        composable(ROUT_ADD_EVENT)     { AddEventScreen(navController) }
        composable(ROUT_JOBS)          { JobsScreen(navController) }
        composable(ROUT_ADD_JOB)       { AddJobScreen(navController) }
        composable(ROUT_POST_NOTICE)   { PostNoticeScreen(navController) }
        composable(ROUT_VOLUNTEER)     { VolunteerScreen(navController) }
        composable(ROUT_NOTIFICATIONS) { NotificationsScreen(navController) }
        composable(ROUT_PROFILE)       { ProfileScreen(navController) }
        composable(ROUT_NOTICE_BOARD)  { NoticeBoardScreen(navController) }
        composable(ROUT_SEE_ALL)       { SeeAllScreen(navController) }
        composable(ROUT_POST_DETAIL)   { PostDetailScreen(navController) } // ✅ removed duplicate ROUT_POST_NOTICE
        composable(ROUT_SERVICES)     { ServicesScreen(navController) }
        composable(ROUT_ADD_SERVICE)  { AddServiceScreen(navController) }
        composable(ROUT_SPLASH) { SplashScreen(navController) }
        composable(ROUT_NOTICE_BOARD) { NoticeBoardScreen(navController) }
        composable(ROUT_ADD_NOTICE)   { AddNoticeScreen(navController) }
        composable(ROUT_NOTIFICATIONS) { NotificationsScreen(navController) }
        composable(ROUT_PROFILE)       { ProfileScreen(navController) }
        composable(ROUT_NOTICE_BOARD)  { NoticeBoardScreen(navController) }
        composable(ROUT_ADD_NOTICE)    { AddNoticeScreen(navController) }
        composable(ROUT_REGISTER)  { RegisterScreen(navController) }
        composable(ROUT_LOGIN)     { LoginScreen(navController) }
    }
}