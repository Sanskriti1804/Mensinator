package com.mensinator.app.ui.navigation

//import kotlinx.coroutines.launch
import android.os.Build
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.mensinator.app.R
import com.mensinator.app.article.ArticleBrowsingScreen
import com.mensinator.app.article.headerItems
import com.mensinator.app.business.IPeriodDatabaseHelper
import com.mensinator.app.calendar.CalendarScreen
import com.mensinator.app.settings.SettingsScreen
import com.mensinator.app.splash.SplashScreen
import com.mensinator.app.statistics.StatisticsScreen
import com.mensinator.app.symptoms.ManageSymptomScreen
import com.mensinator.app.ui.theme.UiConstants
import com.mensinator.app.user.LoginScreen
import com.mensinator.app.user.SignUpScreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

enum class Screen(@StringRes val titleRes: Int) {
    Splash(R.string.app_name),
    Calendar(R.string.calendar_title),
    Symptoms(R.string.symptoms_page),
    Statistic(R.string.statistics_title),
    Settings(R.string.settings_page),
    Login(R.string.login_title),     // Add this
    SignUp(R.string.signup_title),
    Article(R.string.article_title),
    BrowsingArticle(R.string.browsing_article_title)
}

/**
 * The displayCutout insets are necessary for landscape usage, so that the UI is not behind the camera.
 *
 * At MensinatorTopBar, the statusBars insets are used. But as it's only a siblings view,
 * it is not treated as consumed. Thus, on the individual screens, we have to exclude it.
 */
@Composable
fun Modifier.displayCutoutExcludingStatusBarsPadding() =
    windowInsetsPadding(WindowInsets.displayCutout.exclude(WindowInsets.statusBars))

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MensinatorApp(
    navController: NavHostController = rememberNavController(),
    onScreenProtectionChanged: (Boolean) -> Unit?,
) {
    val dbHelper: IPeriodDatabaseHelper = koinInject()

    LaunchedEffect(Unit) {
        launch {
            // If protectScreen is 1, it should protect the screen
            // If protectScreen is 0, should not protect screen (allows screenshots and screen visibility in recent apps)
            val protectScreen =
                (dbHelper.getSettingByKey("screen_protection")?.value?.toIntOrNull() ?: 1) == 1
            onScreenProtectionChanged(protectScreen)
        }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Calendar.name
    )

    val activity = LocalActivity.current ?: return
    val windowSizeClass = calculateWindowSizeClass(activity)
    val isMediumExpandedWWSC by remember(windowSizeClass) {
        derivedStateOf {
            windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
        }
    }

    MainScaffold(
        currentScreen = currentScreen,
        navController = navController,
        onScreenProtectionChanged = onScreenProtectionChanged,
        isMediumExpandedWWSC = isMediumExpandedWWSC,
        onItemClick = { item ->
            navController.navigate(item.screen.name) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun MainScaffold(
    currentScreen: Screen,
    navController: NavHostController,
    onScreenProtectionChanged: (Boolean) -> Unit?,
    isMediumExpandedWWSC: Boolean,
    onItemClick: (NavigationItem) -> Unit
) {
    Row {
        if (isMediumExpandedWWSC) {
            NavigationRail(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Start)),
                    verticalArrangement = Arrangement.Center
                ) {
                    navigationItems.forEach { item ->
                        NavigationRailItem(
                            selected = currentScreen == item.screen,
                            onClick = { onItemClick(item) },
                            icon = { NavigationItemIcon(currentScreen, item) },
                        )
                    }
                }
            }
        }

        Scaffold(
            bottomBar = {
                if (!isMediumExpandedWWSC) {
                    NavigationBar(
                        //modifier = Modifier.consumeWindowInsets(NavigationBarDefaults.windowInsets)
                    ) {
                        navigationItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentScreen == item.screen,
                                onClick = { onItemClick(item) },
                                icon = { NavigationItemIcon(currentScreen, item) }
                            )
                        }
                    }
                }
            },
        ) { rootPaddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.BrowsingArticle.name,
                modifier = Modifier.padding(rootPaddingValues),
                enterTransition = { fadeIn(animationSpec = tween(50)) },
                exitTransition = { fadeOut(animationSpec = tween(50)) },
            ) {
                composable(Screen.Splash.name) {
                    SplashScreen(navController) // 👈 THIS LINE, cutie! Your splash is HERE 🫦
                }
                composable(route = Screen.Login.name) {
                    LoginScreen(
                        authViewModel = koinInject(), // if you're using Koin for DI
                        onNavigateToSignUp = {
                            navController.navigate(Screen.SignUp.name)
                        },
                        onSignInSuccess = {
                            navController.navigate(Screen.Calendar.name) {
                                popUpTo(Screen.Login.name) { inclusive = true }
                            }
                        }
                    )
                }

                composable(route = Screen.SignUp.name) {
                    SignUpScreen(
                        authViewModel = koinInject(),
                        onSignUpSuccess = {
                            navController.navigate(Screen.Calendar.name) {
                                popUpTo(Screen.SignUp.name) { inclusive = true }
                            }
                        },
                        onNavigateToLogin = {
                            navController.navigate(Screen.Login.name)
                        }
                    )
                }

                composable(route = Screen.Article.name) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { /* No text, just leave this empty */ },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.White, // or your custom white from Color.kt
                                    titleContentColor = Color.Transparent // makes sure even accidental title stays invisible
                                )
                            )
                        },
                        contentWindowInsets = WindowInsets(0.dp),
                    ) { topBarPadding ->
                        Column(modifier = Modifier.padding(topBarPadding)) {
//                            com.mensinator.app.article.ArticleNavScreen()
                        }
                    }
                }


                composable(route = Screen.Calendar.name) {
                    // Adapted from https://stackoverflow.com/a/71191082/3991578
                    val (toolbarOnClick, setToolbarOnClick) = remember {
                        mutableStateOf<(() -> Unit)?>(
                            null
                        )
                    }
                    Scaffold(
                        topBar = {
                            MensinatorTopBar(
                                titleStringId = currentScreen.titleRes,
                                onTitleClick = toolbarOnClick,
                                textColor = Color.Red,
                                textStyle = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.appfont)),
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        contentWindowInsets = WindowInsets(0.dp),
                    ) { topBarPadding ->
                        CalendarScreen(
                            modifier = Modifier.padding(topBarPadding),
                            setToolbarOnClick = setToolbarOnClick
                        )
                    }
                }
                composable(route = Screen.Statistic.name) {
                    Scaffold(
                        topBar = {
                            MensinatorTopBar(
                                currentScreen.titleRes,
                                textColor = Color.Red
                            )
                        },
                        contentWindowInsets = WindowInsets(0.dp),
                    ) { topBarPadding ->
                        StatisticsScreen(modifier = Modifier.padding(topBarPadding))
                    }
                }
//                composable(Screen.BrowseArticles.name) {
//                    val headers = listOf(
//                        HeaderItem(
//                            title = "For You",
//                            cards = listOf(
//                                CardItem("Boost Your Sleep", "sleep"),
//                                CardItem("Hydration Myths", "hydration")
//                            )
//                        ),
//                        HeaderItem(
//                            title = "Popular Reads",
//                            cards = listOf(
//                                CardItem("Workout Tips", "workout"),
//                                CardItem("Mental Fitness", "mental")
//                            )
//                        )
//                    )
//
//                    StickyHeaderPage(
//                        headers = headers,
//                        onCardClick = {
////                                        articleId ->
////                            navController.navigate("articleDetail/$articleId")
//                        }
//                    )
//                }

                composable(route = Screen.BrowsingArticle.name) {
                    ArticleBrowsingScreen(
                        headers = headerItems,
                        onCardClick = { item ->
                            // Handle card click here
                        }
                    )
                }

                composable(route = Screen.Symptoms.name) {
                    // Adapted from https://stackoverflow.com/a/71191082/3991578
                    // Needed so that the action button can cause the dialog to be shown
                    val (fabOnClick, setFabOnClick) = remember { mutableStateOf<(() -> Unit)?>(null) }
                    Scaffold(
                        floatingActionButton = {
                            if (currentScreen == Screen.Symptoms) {
                                FloatingActionButton(
                                    onClick = { fabOnClick?.invoke() },
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .displayCutoutPadding()
                                        .size(UiConstants.floatingActionButtonSize)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = stringResource(R.string.delete_button)
                                    )
                                }
                            }
                        },
                        topBar = {
                            MensinatorTopBar(
                                currentScreen.titleRes,
                                textColor = Color.Red,
                            )
                        },
                        contentWindowInsets = WindowInsets(0.dp),
                    ) { topBarPadding ->
                        ManageSymptomScreen(
                            modifier = Modifier.padding(topBarPadding),
                            setFabOnClick = setFabOnClick
                        )
                    }
                }
                composable(route = Screen.Settings.name) {
                    Scaffold(
                        topBar = {
                            MensinatorTopBar(
                                currentScreen.titleRes,
                                textColor = Color.Red
                            )
                        },
                        contentWindowInsets = WindowInsets(0.dp),
                    ) { topBarPadding ->
                        Column {
                            SettingsScreen(
                                onSwitchProtectionScreen = { newValue ->
                                    onScreenProtectionChanged(newValue)
                                },
                                modifier = Modifier.padding(topBarPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationItemIcon(
    currentScreen: Screen,
    item: NavigationItem
) {
    val imageResource = if (currentScreen == item.screen) {
        item.imageSelected
    } else {
        item.imageUnSelected
    }

    // Log if the resource ID is invalid (0x0)
    if (imageResource == 0) {
        Log.e("NavigationItem", "Invalid resource ID: $imageResource for screen: ${item.screen}")
    }

    // Use a fallback if the resource is invalid
    val validImageResource = if (imageResource != 0) imageResource else R.drawable.logo

    // Render the icon with the resource ID
    Icon(
        painter = painterResource(id = validImageResource),
        contentDescription = stringResource(item.screen.titleRes),
        modifier = Modifier
            .size(42.dp)
            .padding(4.dp)
    )
}


private val navigationItems = listOf(
    NavigationItem(
        screen = Screen.Calendar,
        R.drawable.icappcalendar,
        R.drawable.icappcalendar //here you can add not_field icon if you want. when its not selected
    ),
    NavigationItem(
        screen = Screen.Statistic,
        R.drawable.icappstats2,
        R.drawable.icappstats2
    ),
    NavigationItem(
        screen = Screen.Symptoms,
        R.drawable.icapparticle,
        R.drawable.icapparticle
    ),
    NavigationItem(
        screen = Screen.Settings,
        R.drawable.icappuser,
        R.drawable.icappuser
    ),
)