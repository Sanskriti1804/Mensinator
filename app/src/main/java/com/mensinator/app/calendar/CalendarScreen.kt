package com.mensinator.app.calendar

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.mensinator.app.R
import com.mensinator.app.business.IPeriodDatabaseHelper
import com.mensinator.app.calendar.CalendarViewModel.UiAction
import com.mensinator.app.extensions.stringRes
import com.mensinator.app.settings.ColorSetting
import com.mensinator.app.ui.navigation.displayCutoutExcludingStatusBarsPadding
import com.mensinator.app.ui.theme.Black
import com.mensinator.app.ui.theme.DarkGrey
import com.mensinator.app.ui.theme.MensinatorTheme
import com.mensinator.app.ui.theme.appWhite
import com.mensinator.app.ui.theme.isDarkMode
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.minus
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CalendarScreen(
    modifier: Modifier,
    viewModel: CalendarViewModel = koinViewModel(),
    setToolbarOnClick: (() -> Unit) -> Unit
) {
    val onAction = { uiAction: UiAction -> viewModel.onAction(uiAction) }

    val state = viewModel.viewState.collectAsStateWithLifecycle()
    val isDarkMode = isDarkMode()
    val todayDate = LocalDate.now()
    val menstrualPhase = "MenstrualPhase"

    val currentYearMonth = YearMonth.now()
    val calendarState = rememberCalendarState(
        startMonth = currentYearMonth.minusMonths(30),
        endMonth = currentYearMonth.plusMonths(30),
        firstVisibleMonth = currentYearMonth,
    )
    val coroutineScope = rememberCoroutineScope()
    val showSymptomsDialog = remember { mutableStateOf(false) }


    LaunchedEffect(isDarkMode) { viewModel.updateDarkModeStatus(isDarkMode) }

    LaunchedEffect(Unit) {
        setToolbarOnClick {
            coroutineScope.launch {
                calendarState.animateScrollToMonth(YearMonth.now())
            }
        }
    }


    // Generate placement for calendar and buttons
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = appWhite)
            .displayCutoutExcludingStatusBarsPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        CalendarTopHeader(
            todayDate = LocalDate.now(),
            location = "India",
            state = state
        )
        LaunchedEffect(calendarState.firstVisibleMonth) {
            viewModel.onAction(UiAction.UpdateFocusedYearMonth(calendarState.firstVisibleMonth.yearMonth))
        }

        VerticalCalendar(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true) // Make this row occupy the maximum remaining height
                .padding(top = 16.dp),
            state = calendarState,
            dayContent = { day ->
                key(day.date) {
                    Day(
                        viewState = state,
                        onAction = onAction,
                        day = day,
                    )
                }
            },
            monthHeader = {
                MonthTitle(yearMonth = it.yearMonth)
                Spacer(modifier = Modifier.height(4.dp))
                DaysOfWeekTitle(daysOfWeek = daysOfWeek().toPersistentList())
            }
        )

        Spacer(modifier = Modifier.height(2.dp))

        FlowRow(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            val buttonModifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
            PeriodButton(state, onAction, buttonModifier)
            SymptomButton(showSymptomsDialog, state, buttonModifier)
            OvulationButton(state, onAction, buttonModifier)
        }

        // Show the SymptomsDialog
        if (showSymptomsDialog.value && state.value.selectedDays.isNotEmpty()) {
            val activeSymptoms = state.value.activeSymptoms
            val date = state.value.selectedDays.last()

            EditSymptomsForDaysDialog(
                date = date,
                symptoms = activeSymptoms,
                currentlyActiveSymptomIds = state.value.activeSymptomIdsForLatestSelectedDay,
                onSave = { selectedSymptoms ->
                    val selectedSymptomIds = selectedSymptoms.map { it.id }
                    showSymptomsDialog.value = false
                    viewModel.onAction(
                        UiAction.UpdateSymptomDates(
                            days = state.value.selectedDays,
                            selectedSymptomIds = selectedSymptomIds.toPersistentList()
                        )
                    )
                },
                onCancel = {
                    showSymptomsDialog.value = false
                    viewModel.onAction(UiAction.SelectDays(persistentSetOf()))
                }
            )
        }
    }
}

//Date and time component
@Composable
private fun CalendarTopHeader(
    todayDate: LocalDate = LocalDate.now(),
    location: String = "Indonesia",
    state: State<CalendarViewModel.ViewState>
//    time: String = "10:00 AM"
) {
    val headingFont = FontFamily(
        Font(R.font.secfont) // Your custom font
    )

    // Determine current menstrual phase
    val currentPhase = remember(todayDate, state.value) {
        when {
            todayDate in state.value.periodDates.keys -> "Menstrual"
            state.value.ovulationPredictionDate?.isEqual(todayDate) == true -> "Ovulation"
            todayDate in state.value.ovulationDates -> "Ovulation"
            state.value.periodPredictionDate?.let { predDate ->
                todayDate.isAfter(predDate.minusDays(5)) && todayDate.isBefore(predDate)
            } == true -> "Premenstrual"

            else -> "Follicular"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        // Weekday name
        Text(
            text = todayDate.dayOfWeek.name.lowercase()
                .replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontFamily = headingFont,
                color = Color.Gray,
                fontSize = 20.sp
            )
        )

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.5f)) {
                Text(
                    text = todayDate.dayOfMonth.toString(),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.W700,
                        fontSize = 50.sp,
                        fontFamily = headingFont
                    )
                )
                Text(
                    text = todayDate.month.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp, // Increased font size
                        letterSpacing = 1.sp,
                        fontFamily = headingFont,
                        color = com.mensinator.app.ui.theme.appDRed
                    )
                )
            }



            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = currentPhase,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = headingFont,
                        fontSize = 16.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
//                Text(
//                    text = location,
//                    style = MaterialTheme.typography.bodySmall.copy(
//                        color = Color.Red,
//                        fontFamily = headingFont,
//                        fontSize = 15.sp
//                    )
//                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp)) // Slightly reduced from original 12.dp


    }
}


@Composable
private fun PeriodButton(
    state: State<CalendarViewModel.ViewState>,
    onAction: (uiAction: UiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isPeriodButtonEnabled by remember {
        derivedStateOf { state.value.selectedDays.isNotEmpty() }
    }
    val successSaved = stringResource(id = R.string.successfully_saved_alert)
    var isRecentlyClicked by remember { mutableStateOf(false) }
    var showPeriodConfirm by remember { mutableStateOf(false) }

    val rangeSize by remember {
        derivedStateOf { state.value.selectedDaysRange.size }
    }
    val selectedIsPeriod by remember {
        derivedStateOf {
            state.value.selectedDaysRange.any { it in state.value.periodDates }
        }
    }

    // Reset recentlyClicked state when selected days change (unless the new selection contains period dates)
    LaunchedEffect(state.value.selectedDays) {
        if (!selectedIsPeriod) {
            isRecentlyClicked = false
        }
    }

    // Colors
    val defaultBorderColor = com.mensinator.app.ui.theme.appDRed.copy(alpha = 0.3f)
    val defaultTextColor = com.mensinator.app.ui.theme.appDRed
    val activeColor = com.mensinator.app.ui.theme.appDRed
    val activeTextColor = Color.White
    val isActive = isRecentlyClicked || selectedIsPeriod

    val performPeriodSave = {
        onAction(
            UiAction.UpdatePeriodDates(
                currentPeriodDays = state.value.periodDates,
                selectedDays = state.value.selectedDays,
                selectedDaysRange = state.value.selectedDaysRange
            )
        )
        Toast.makeText(context, successSaved, Toast.LENGTH_SHORT).show()
        isRecentlyClicked = true
    }

    if (showPeriodConfirm) {
        val message = when {
            rangeSize < 2 -> stringResource(R.string.period_confirm_less_days, 2)
            else -> stringResource(R.string.period_confirm_more_days, rangeSize)
        }
        AlertDialog(
            onDismissRequest = { showPeriodConfirm = false },
            title = { Text(stringResource(R.string.period_button)) },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = {
                    performPeriodSave()
                    showPeriodConfirm = false
                }) {
                    Text(stringResource(R.string.confirm), color = com.mensinator.app.ui.theme.appDRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPeriodConfirm = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isActive) activeColor else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.5.dp,
                color = if (isActive) activeColor else defaultBorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                enabled = isPeriodButtonEnabled,
                onClick = {
                    when {
                        rangeSize in 2..9 -> performPeriodSave()
                        rangeSize < 2 || rangeSize > 9 -> {
                            showPeriodConfirm = true
                        }
                        else -> { /* no selection */ }
                    }
                }
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        val text = when {
            selectedIsPeriod && isPeriodButtonEnabled -> {
                stringResource(id = R.string.period_button_selected)
            }

            !selectedIsPeriod && isPeriodButtonEnabled -> {
                stringResource(id = R.string.period_button_not_selected)
            }

            else -> stringResource(id = R.string.period_button)
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                color = if (isActive) activeTextColor else defaultTextColor
            ),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}

@Composable
private fun SymptomButton(
    showSymptomsDialog: MutableState<Boolean>,
    state: State<CalendarViewModel.ViewState>,
    modifier: Modifier = Modifier
) {
    val isButtonEnabled by remember {
        derivedStateOf { state.value.selectedDays.isNotEmpty() }
    }

    // Check if any selected date has symptoms
    val hasSymptoms by remember {
        derivedStateOf {
            state.value.selectedDays.any { date ->
                state.value.symptomDates.containsKey(date)
            }
        }
    }

    // Colors
    val defaultBorderColor = com.mensinator.app.ui.theme.appDRed.copy(alpha = 0.3f)
    val defaultTextColor = com.mensinator.app.ui.theme.appDRed
    val activeColor = com.mensinator.app.ui.theme.appDRed
    val activeTextColor = Color.White

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (hasSymptoms) activeColor else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.5.dp,
                color = if (hasSymptoms) activeColor else defaultBorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                enabled = isButtonEnabled,
                onClick = { showSymptomsDialog.value = true }
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.symptoms_button),
            style = MaterialTheme.typography.labelMedium.copy(
                color = if (hasSymptoms) activeTextColor else defaultTextColor
            ),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}

@Composable
private fun OvulationButton(
    state: State<CalendarViewModel.ViewState>,
    onAction: (uiAction: UiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val ovulationButtonEnabled by remember {
        derivedStateOf { state.value.selectedDays.isNotEmpty() }
    }
    val successSavedOvulation = stringResource(id = R.string.success_saved_ovulation)
    var showOvulationConfirm by remember { mutableStateOf(false) }

    val rangeSize by remember {
        derivedStateOf { state.value.selectedDaysRange.size }
    }
    val isOvulationDate by remember {
        derivedStateOf {
            state.value.selectedDaysRange.any { it in state.value.ovulationDates }
        }
    }

    val performOvulationSave = {
        if (state.value.selectedDaysRange.size == 1) {
            onAction(UiAction.UpdateOvulationDay(state.value.selectedDaysRange.first()))
        } else {
            onAction(UiAction.UpdateOvulationDates(state.value.selectedDaysRange))
        }
        Toast.makeText(context, successSavedOvulation, Toast.LENGTH_SHORT).show()
    }

    if (showOvulationConfirm) {
        AlertDialog(
            onDismissRequest = { showOvulationConfirm = false },
            title = { Text(stringResource(R.string.ovulation_button)) },
            text = { Text(stringResource(R.string.ovulation_confirm_more_days, rangeSize)) },
            confirmButton = {
                TextButton(onClick = {
                    performOvulationSave()
                    showOvulationConfirm = false
                }) {
                    Text(stringResource(R.string.confirm), color = com.mensinator.app.ui.theme.appDRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showOvulationConfirm = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // Colors
    val defaultBorderColor = com.mensinator.app.ui.theme.appDRed.copy(alpha = 0.3f)
    val defaultTextColor = com.mensinator.app.ui.theme.appDRed
    val activeColor = com.mensinator.app.ui.theme.appDRed
    val activeTextColor = Color.White

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isOvulationDate) activeColor else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.5.dp,
                color = if (isOvulationDate) activeColor else defaultBorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                enabled = ovulationButtonEnabled,
                onClick = {
                    when {
                        rangeSize <= 7 -> performOvulationSave()
                        else -> showOvulationConfirm = true
                    }
                }
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        val text = when {
            isOvulationDate -> stringResource(id = R.string.ovulation_button_selected)
            ovulationButtonEnabled -> stringResource(id = R.string.ovulation_button_not_selected)
            else -> stringResource(id = R.string.ovulation_button)
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                color = if (isOvulationDate) activeTextColor else defaultTextColor
            ),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}

@Composable
private fun ButtonText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        textAlign = TextAlign.Center,
        color = Color.Black,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2
    )
}


/**
 * Display the days of the week.
 */
@Composable
private fun DaysOfWeekTitle(daysOfWeek: PersistentList<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                textAlign = TextAlign.Center,
                text = stringResource(id = dayOfWeek.stringRes),
            )
        }
    }
}

/**
 * Display the month title.
 */
@Composable
private fun MonthTitle(yearMonth: YearMonth) {
    val headingFont = FontFamily(
        Font(R.font.secfont) // use your actual file
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            textAlign = TextAlign.Center,
            text = "${stringResource(id = yearMonth.month.stringRes)} ",
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = headingFont,
                color = com.mensinator.app.ui.theme.appDRed,
                fontSize = 30.sp
            )
        )
        HorizontalDivider(
            color = Color.LightGray,
            thickness = 2.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        val headingFont = FontFamily(
            Font(R.font.secfont) // use your actual file
        )
    }
}

/**
 * Display a day in the calendar.
 */
@Composable
fun Day(
    viewState: State<CalendarViewModel.ViewState>,
    onAction: (uiAction: UiAction) -> Unit,
    day: CalendarDay,
) {
    val state = viewState.value
    val localDateNow = remember { LocalDate.now() }

    val settingColors = state.calendarColors.settingColors
    val dbHelper: IPeriodDatabaseHelper = koinInject()

    /**
     * Make sure the cells don't occupy so much space in landscape or on big (wide) screens.
     */
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val wideWindow =
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    val aspectRatioModifier = when {
        wideWindow -> {
            Modifier.aspectRatio(2f) // Make cells less tall
        }

        else -> {
            Modifier.aspectRatio(1f) // Ensure cells remain square
        }
    }

    if (day.position != DayPosition.MonthDate) {
        // Exclude dates that are not part of the current month
        Box(
            modifier = aspectRatioModifier // Maintain grid structure with empty space
        )
        return
    }

    val fallbackColors = if (isDarkMode()) {
        ColorCombination(DarkGrey, Color.White)
    } else {
        ColorCombination(Color.Transparent, Black)
    }
    val dayColors = when {
        day.date in state.selectedDaysRange -> settingColors[ColorSetting.SELECTION]
        day.date in state.periodDates.keys -> settingColors[ColorSetting.PERIOD]
        state.periodPredictionDate?.isEqual(day.date) == true -> settingColors[ColorSetting.EXPECTED_PERIOD]
        day.date in state.ovulationDates -> settingColors[ColorSetting.OVULATION]
        state.ovulationPredictionDate?.isEqual(day.date) == true -> settingColors[ColorSetting.EXPECTED_OVULATION]
        else -> null
    } ?: fallbackColors

    val border = if (day.date.isEqual(localDateNow)) {
        BorderStroke(1.5.dp, fallbackColors.textColor.copy(alpha = 0.5f))
    } else null

    val fontStyleType = when {
        day.date.isEqual(localDateNow) -> FontWeight.Bold
        else -> FontWeight.Normal
    }

    val nextMonth = YearMonth.now().plusMonths(1)
    val isPredictedPeriodNextMonth = remember(state.periodPredictionDate, day.date, nextMonth) {
        state.periodPredictionDate?.isEqual(day.date) == true &&
                state.periodPredictionDate?.yearMonth == nextMonth
    }
    val dayTextColor = if (isPredictedPeriodNextMonth) com.mensinator.app.ui.theme.appDRed else dayColors.textColor

    // Dates to track (use range so all days between start and end are highlighted)
    val isSelected = day.date in state.selectedDaysRange
    val hasSymptomDate = day.date in state.symptomDates

    val shape = MaterialTheme.shapes.small
    Surface(
        modifier = aspectRatioModifier
            .padding(2.dp)
            .clip(shape)
            .clickable {
                val newSelectedDates = if (isSelected) {
                    state.selectedDays - day.date
                } else {
                    state.selectedDays + day.date
                }.toPersistentSet()
                onAction(UiAction.SelectDays(newSelectedDates))
            },
        shape = shape,
        color = dayColors.backgroundColor,
        border = border
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = day.date.dayOfMonth.toString(),
                fontWeight = fontStyleType,
                color = dayTextColor
            )

            // Add symptom circles
            if (hasSymptomDate) {
                val symptomsForDay = state.symptomDates.getOrDefault(day.date, setOf())

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 1.dp),
                    horizontalArrangement = Arrangement.spacedBy((-5).dp) // Overlap circles
                ) {
                    symptomsForDay.forEach { symptom ->
                        val symptomColor = state.calendarColors.symptomColors[symptom] ?: Color.Red

                        Box(
                            modifier = Modifier
                                .size(11.dp)
                                .background(symptomColor, CircleShape)
                                .border(1.dp, Color.LightGray.copy(alpha = 0.25f), CircleShape)
                        )
                    }
                }
            }

            if (state.showCycleNumbers) {
                calculateCycleNumber(day.date, localDateNow, dbHelper)?.let { cycleNumber ->
                    Surface(
                        shape = shape,
                        color = Color.Transparent,
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = cycleNumber.toString(),
                            fontSize = 8.sp,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            color = dayTextColor,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Calculate the cycle number for a given date.
 */
fun calculateCycleNumber(day: LocalDate, now: LocalDate, dbHelper: IPeriodDatabaseHelper): Int? {
    // Don't generate cycle numbers for future dates
    if (day > now) return null

    val lastPeriodStartDate = dbHelper.getFirstPreviousPeriodDate(day) ?: return null

    return ChronoUnit.DAYS.between(lastPeriodStartDate, day).toInt() + 1
}


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun CalendarScreenPreview() {
    MensinatorTheme {
        Surface {
            CalendarScreen(
                modifier = Modifier,
                setToolbarOnClick = {}
            )
        }
    }
}