package com.mensinator.app.ui.navigation

import ArticleListScreen
import HormoneCycleChart
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mensinator.app.R
import com.mensinator.app.article.ArticleBrowsingScreen
import com.mensinator.app.article.ArticleLayout
import com.mensinator.app.article.articles
import com.mensinator.app.article.headers
import com.mensinator.app.business.IPeriodDatabaseHelper
import com.mensinator.app.calendar.CalendarScreen
import com.mensinator.app.questionnaire.Constants
import com.mensinator.app.questionnaire.QuestionnaireScreen
import com.mensinator.app.settings.SettingsScreen
import com.mensinator.app.splash.SplashScreen
import com.mensinator.app.statistics.StatisticsScreen
import com.mensinator.app.statistics.StatisticsViewModel
import com.mensinator.app.symptoms.ManageSymptomScreen
import com.mensinator.app.ui.theme.UiConstants
import com.mensinator.app.ui.theme.appDRed
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.time.LocalDate

enum class Screen(@StringRes val titleRes: Int) {
    Splash(R.string.app_name),
    Calendar(R.string.calendar_title),
    Symptoms(R.string.symptoms_page),
    HormoneGraph(R.string.hormone_chart_title),
    Statistic(R.string.statistics_title),
    Settings(R.string.settings_page),
    ArticleList(R.string.article_list_title),
    Article(R.string.article_title),
    BrowsingArticle(R.string.browsing_article_title),
    Questionnaire(R.string.questionnaire_title)
}

@Composable
fun Modifier.displayCutoutExcludingStatusBarsPadding() =
    windowInsetsPadding(WindowInsets.displayCutout.exclude(WindowInsets.statusBars))

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MensinatorApp(
    navController: NavHostController = rememberNavController(),
    onScreenProtectionChanged: (Boolean) -> Unit?
) {
    val dbHelper: IPeriodDatabaseHelper = koinInject()

    LaunchedEffect(Unit) {
        launch {
            val protectScreen =
                (dbHelper.getSettingByKey("screen_protection")?.value?.toIntOrNull() ?: 1) == 1
            onScreenProtectionChanged(protectScreen)
        }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route?.let { route ->
        Screen.values().find { it.name == route.substringBefore('/') } ?: Screen.Calendar
    } ?: Screen.Calendar

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                if (!isMediumExpandedWWSC && currentScreen != Screen.Splash) {
                    NavigationBar(
                        containerColor = appDRed,
                        modifier = Modifier
                            .height(70.dp)
                            .shadow(elevation = 8.dp)
                    ) {
                        navigationItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentScreen == item.screen,
                                onClick = { onItemClick(item) },
                                icon = {
                                    Icon(
                                        painter = painterResource(
                                            id = if (currentScreen == item.screen) item.imageSelected
                                            else item.imageUnSelected
                                        ),
                                        contentDescription = stringResource(item.screen.titleRes),
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                    indicatorColor = Color.Red.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                }
            }
        ) { rootPaddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.name,
                modifier = Modifier.padding(rootPaddingValues),
                enterTransition = { fadeIn(animationSpec = tween(50)) },
                exitTransition = { fadeOut(animationSpec = tween(50)) },
            ) {
                composable(Screen.Splash.name) {
                        SplashScreen(navController)
                }

                composable(Screen.Questionnaire.name) {
                    val questions = Constants.getQuestions()
                    val onSubmit: (Map<String, String>) -> Unit = { answers ->
                        answers.forEach { (questionId, answer) ->
                            Log.d("FormSubmission", "QID: $questionId -> Ans: $answer")
                        }
                    }

                    Scaffold(
                        topBar = {
                            MensinatorTopBar(
                                titleStringId = currentScreen.titleRes,
                                textColor = appDRed
                            )
                        },
                        contentWindowInsets = WindowInsets(0.dp),
                    ) { topBarPadding ->
                        QuestionnaireScreen(
                            modifier = Modifier.padding(topBarPadding),
                            questions = questions,
                            onSubmit = onSubmit
                        )
                    }
                }

                composable(Screen.Calendar.name) {
                    val (toolbarOnClick, setToolbarOnClick) = remember {
                        mutableStateOf<(() -> Unit)?>(null)
                    }
                    Scaffold(
                        topBar = {
                            MensinatorTopBar(
                                titleStringId = currentScreen.titleRes,
                                onTitleClick = toolbarOnClick,
                                textColor = appDRed,
                                textStyle = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.appfont)),
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                backgroundColor = Color.White
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

                composable(Screen.HormoneGraph.name) {
                    HormoneCycleChart(
                        periodStartDate = LocalDate.now(),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                composable(Screen.Statistic.name) {
                    val viewModel: StatisticsViewModel = koinViewModel()
                    val state by viewModel.viewState.collectAsStateWithLifecycle()

                    Scaffold(
                        topBar = {
                            MensinatorTopBar(
                                currentScreen.titleRes,
                                textColor = Color.Red
                            )
                        },
                        contentWindowInsets = WindowInsets(0.dp),
                    ) { topBarPadding ->
                        StatisticsScreen(
                            modifier = Modifier.padding(topBarPadding),
                        )
                    }
                }

                composable(Screen.BrowsingArticle.name) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "FAQ and Guides",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = appDRed,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )

                        ArticleBrowsingScreen(
                            navController = navController,
                            headers = headers,
                            onCardClick = { articleId ->  // Add this parameter
                                navController.navigate("${Screen.Article.name}/$articleId")
                            }
                        )
                    }
                }

                composable(Screen.ArticleList.name) {
                    ArticleListScreen(
                        articles = articles,  // Your predefined list of articles
                        onArticleClick = { articleId ->
                            navController.navigate("${Screen.Article.name}/$articleId")
                        }
                    )
                }

                composable("${Screen.Article.name}/{articleId}") { backStackEntry ->
                    val articleId = backStackEntry.arguments?.getString("articleId")
                    val article = articles.find { it.articleId == articleId }
                    if (article != null) {
                        ArticleLayout(article = article)
                    } else {
                        Text("Article not found!")
                    }
                }

                composable(Screen.Symptoms.name) {
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

                composable(Screen.Settings.name) {
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

    if (imageResource == 0) {
        Log.e("NavigationItem", "Invalid resource ID: $imageResource for screen: ${item.screen}")
    }

    val validImageResource = if (imageResource != 0) imageResource else R.drawable.logo

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
        R.drawable.icappcalendar
    ),
    NavigationItem(
        screen = Screen.Statistic,
        R.drawable.icappstats2,
        R.drawable.icappstats2
    ),
    NavigationItem(
        screen = Screen.BrowsingArticle,
        R.drawable.icapparticle,
        R.drawable.icapparticle
    ),
    NavigationItem(
        screen = Screen.Settings,
        R.drawable.icappuser,
        R.drawable.icappuser
    ),
)