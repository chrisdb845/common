package com.christian.commonlink.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.christian.commonlink.ui.screens.About.AboutScreen
import com.christian.commonlink.ui.screens.Profile.EditProfileScreen
import com.christian.commonlink.ui.screens.Mainscreen.MainScreen
import com.christian.commonlink.ui.screens.auth.LoginScreen
import com.christian.commonlink.ui.screens.auth.RegisterScreen
import com.christian.commonlink.ui.screens.splash.SplashScreen

// ── All routes ───────────────────────────────────────────────────
const val ROUT_SPLASH        = "splash"
const val ROUT_LOGIN         = "login"
const val ROUT_REGISTER      = "register"
const val ROUT_ABOUT         = "about"
const val ROUT_HOME          = "home"
const val ROUT_MAIN          = "main"
const val ROUT_EVENTS        = "events"
const val ROUT_ADD_EVENT     = "add_event"
const val ROUT_JOBS          = "jobs"
const val ROUT_ADD_JOB       = "add_job"
const val ROUT_SERVICES      = "services"
const val ROUT_ADD_SERVICE   = "add_service"
const val ROUT_POST_NOTICE   = "post_notice"
const val ROUT_VOLUNTEER     = "volunteer"
const val ROUT_NOTIFICATIONS = "notifications"
const val ROUT_PROFILE       = "profile"
const val ROUT_NOTICE_BOARD  = "notice_board"
const val ROUT_ADD_NOTICE    = "add_notice"
const val ROUT_SEE_ALL       = "see_all"
const val ROUT_POST_DETAIL   = "post_detail"
const val ROUT_EDIT_PROFILE = "edit_profile"

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ── Entry screens — NO bottom nav ────────────────────────
        composable(ROUT_SPLASH)    { SplashScreen(navController) }
        composable(ROUT_LOGIN)     { LoginScreen(navController) }
        composable(ROUT_REGISTER)  { RegisterScreen(navController) }
        composable(ROUT_ABOUT)     { AboutScreen(navController) }

        // ── Main app — HAS bottom nav ────────────────────────────
        // ✅ All screens inside MainScreen now
        composable(ROUT_MAIN) { MainScreen(navController) }
        composable(ROUT_EDIT_PROFILE) { EditProfileScreen(navController) }
    }
}