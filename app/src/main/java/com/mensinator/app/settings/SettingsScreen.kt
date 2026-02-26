package com.mensinator.app.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.mensinator.app.R
import com.mensinator.app.data.ColorSource
import com.mensinator.app.ui.ResourceMapper
import com.mensinator.app.ui.navigation.displayCutoutExcludingStatusBarsPadding
import com.mensinator.app.ui.theme.MensinatorTheme
import com.mensinator.app.ui.theme.appDRed
import com.mensinator.app.ui.theme.appGray
import com.mensinator.app.ui.theme.isDarkMode
import org.koin.androidx.compose.koinViewModel

private val colorCircleSize = 24.dp

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    onSwitchProtectionScreen: (Boolean) -> Unit,
) {
    val textAppStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.textfont)),
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = FontWeight.Bold
    )

    val cardColor = Color(0xFFFFEDED)

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val viewState = viewModel.viewState.collectAsStateWithLifecycle().value
    val isDarkMode = isDarkMode()
    val context = LocalContext.current

    LaunchedEffect(isDarkMode) {
        viewModel.updateDarkModeStatus(isDarkMode)
    }
    LaunchedEffect(Unit) {
        viewModel.init()
    }
    LaunchedEffect("resume") {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.onResume()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
            .displayCutoutExcludingStatusBarsPadding()
    ) {
        Spacer(Modifier.height(16.dp))

        SettingSectionHeader(text = stringResource(R.string.colors))
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = cardColor)
        ) {
            Column(Modifier.padding(16.dp)) {
                ColorSection(viewState, viewModel)
            }
        }

        Spacer(Modifier.height(16.dp))

        SettingSectionHeader(text = stringResource(R.string.reminders))
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = cardColor)
        ) {
            Column(Modifier.padding(16.dp)) {
                SettingNumberSelection(
                    intSetting = IntSetting.REMINDER_DAYS,
                    text = viewState.daysBeforeReminder,
                    openIntPickerForSetting = viewState.openIntPickerForSetting,
                    onClosePicker = { viewModel.showIntPicker(null) },
                    onNumberChange = { intSetting, newNumber ->
                        viewModel.updateIntSetting(intSetting, newNumber)
                        if (!viewModel.areNotificationsEnabled(context)) {
                            Log.d("SettingsDialog", "Notifications are not enabled")
                            openNotificationSettings(context)
                        }
                    },
                    onOpenIntPicker = { viewModel.showIntPicker(it) }
                )
                SettingText(
                    text = stringResource(StringSetting.PERIOD_NOTIFICATION_MESSAGE.stringResId),
                    onClick = { viewModel.showPeriodNotificationDialog(true) }
                )
                OutlinedButton(
                    onClick = { viewModel.triggerTestNotification() },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = appDRed,
                        containerColor = Color.White
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                ) {
                    Text(text = stringResource(R.string.test_notification))
                }
            }
        }

        if (viewState.showPeriodNotificationDialog) {
            val initPeriodKeyOrCustomMessage = viewState.periodNotificationMessage
            val messageText = ResourceMapper.getStringResourceOrCustom(initPeriodKeyOrCustomMessage)
            NotificationDialog(
                messageText = messageText,
                onSave = {
                    viewModel.updateStringSetting(StringSetting.PERIOD_NOTIFICATION_MESSAGE, it)
                },
                onDismissRequest = { viewModel.showPeriodNotificationDialog(false) }
            )
        }

        Spacer(Modifier.height(16.dp))

        SettingSectionHeader(text = stringResource(R.string.other_settings))
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = cardColor)
        ) {
            Column(Modifier.padding(16.dp)) {
                SettingSwitch(
                    text = stringResource(BooleanSetting.LUTEAL_PHASE_CALCULATION.stringResId),
                    checked = viewState.lutealPhaseCalculationEnabled,
                    onCheckedChange = {
                        viewModel.updateBooleanSetting(BooleanSetting.LUTEAL_PHASE_CALCULATION, it)
                    }
                )
                
                SettingLanguagePicker()
                SettingSwitch(
                    text = stringResource(BooleanSetting.SHOW_CYCLE_NUMBERS.stringResId),
                    checked = viewState.showCycleNumbers,
                    onCheckedChange = {
                        viewModel.updateBooleanSetting(BooleanSetting.SHOW_CYCLE_NUMBERS, it)
                    }
                )
                SettingSwitch(
                    text = stringResource(BooleanSetting.PREVENT_SCREENSHOTS.stringResId),
                    checked = viewState.preventScreenshots,
                    onCheckedChange = { newValue ->
                        viewModel.updateBooleanSetting(BooleanSetting.PREVENT_SCREENSHOTS, newValue)
                        onSwitchProtectionScreen(newValue)
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        SettingSectionHeader(text = stringResource(R.string.data_settings))
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = cardColor)
        ) {
            Column(Modifier.padding(16.dp)) {
                ExportRow(viewModel)
            }
        }

        Spacer(Modifier.height(32.dp))
        AboutSection(viewModel, viewState)

        if (viewState.showFaqDialog) {
            FaqDialog(onDismissRequest = { viewModel.showFaqDialog(false) })
        }

        if (viewState.showExportDialog) {
            ExportDialog(
                exportFilePath = viewState.exportFilePath,
                onDismissRequest = { viewModel.showExportDialog(false) },
                onExportClick = { exportPath -> viewModel.handleExport(exportPath) }
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun AboutSection(
    viewModel: SettingsViewModel,
    viewState: SettingsViewModel.ViewState
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        TextButton(
            onClick = { viewModel.showFaqDialog(true) },
            colors = ButtonDefaults.elevatedButtonColors(),
        ) {
            Text(
                text = stringResource(R.string.about_app),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Text(
            text = "App Version: ${viewState.appVersion}",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Text(
            text = "DB Version: ${viewState.dbVersion}",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}

@Composable
private fun ColorSection(
    viewState: SettingsViewModel.ViewState,
    viewModel: SettingsViewModel
) {
    SettingColorSelection(
        colorSetting = ColorSetting.PERIOD,
        currentColor = viewState.periodColor,
        openColorPickerForSetting = viewState.openColorPickerForSetting,
        onClosePicker = { viewModel.showColorPicker(null) },
        onColorChange = { colorSetting, newColor ->
            viewModel.updateColorSetting(colorSetting, newColor)
        },
        onOpenColorPicker = { viewModel.showColorPicker(it) },
    )
    SettingColorSelection(
        colorSetting = ColorSetting.SELECTION,
        currentColor = viewState.selectionColor,
        openColorPickerForSetting = viewState.openColorPickerForSetting,
        onClosePicker = { viewModel.showColorPicker(null) },
        onColorChange = { colorSetting, newColor ->
            viewModel.updateColorSetting(colorSetting, newColor)
        },
        onOpenColorPicker = { viewModel.showColorPicker(it) },
    )
//    SettingColorSelection(
//        colorSetting = ColorSetting.PERIOD_SELECTION,
//        currentColor = viewState.periodSelectionColor,
//        openColorPickerForSetting = viewState.openColorPickerForSetting,
//        onClosePicker = { viewModel.showColorPicker(null) },
//        onColorChange = { colorSetting, newColor ->
//            viewModel.updateColorSetting(colorSetting, newColor)
//        },
//        onOpenColorPicker = { viewModel.showColorPicker(it) },
//    )
    SettingColorSelection(
        colorSetting = ColorSetting.EXPECTED_PERIOD,
        currentColor = viewState.expectedPeriodColor,
        openColorPickerForSetting = viewState.openColorPickerForSetting,
        onClosePicker = { viewModel.showColorPicker(null) },
        onColorChange = { colorSetting, newColor ->
            viewModel.updateColorSetting(colorSetting, newColor)
        },
        onOpenColorPicker = { viewModel.showColorPicker(it) },
    )
    SettingColorSelection(
        colorSetting = ColorSetting.OVULATION,
        currentColor = viewState.ovulationColor,
        openColorPickerForSetting = viewState.openColorPickerForSetting,
        onClosePicker = { viewModel.showColorPicker(null) },
        onColorChange = { colorSetting, newColor ->
            viewModel.updateColorSetting(colorSetting, newColor)
        },
        onOpenColorPicker = { viewModel.showColorPicker(it) },
    )
    SettingColorSelection(
        colorSetting = ColorSetting.EXPECTED_OVULATION,
        currentColor = viewState.expectedOvulationColor,
        openColorPickerForSetting = viewState.openColorPickerForSetting,
        onClosePicker = { viewModel.showColorPicker(null) },
        onColorChange = { colorSetting, newColor ->
            viewModel.updateColorSetting(colorSetting, newColor)
        },
        onOpenColorPicker = { viewModel.showColorPicker(it) },
    )
}

@Composable
private fun SettingSectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .semantics { heading() },
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.textfont)),
            fontSize = 24.sp,  // Increased from default size
            fontWeight = FontWeight.Bold
        ),
        fontWeight = FontWeight.Bold
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingSectionHeaderPreview() {
    MensinatorTheme {
        SettingSectionHeader(text = stringResource(R.string.colors))
    }
}

@Composable
private fun SettingColorSelection(
    colorSetting: ColorSetting,
    currentColor: Color,
    openColorPickerForSetting: ColorSetting?,
    modifier: Modifier = Modifier,
    onClosePicker: () -> Unit,
    onColorChange: (setting: ColorSetting, colorName: String) -> Unit,
    onOpenColorPicker: (setting: ColorSetting) -> Unit,
) {
    Row(

        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(colorSetting.stringResId),
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(4.dp))
        Row {
            Box(
                modifier = Modifier
                    .size(colorCircleSize)
                    .background(color = currentColor, shape = CircleShape)
                    .clip(CircleShape)
                    .clickable { onOpenColorPicker(colorSetting) }
            )

            if (openColorPickerForSetting != colorSetting) return
            ColorPicker(
                colorSetting = colorSetting,
                onClosePicker = onClosePicker,
                onSelectColor = onColorChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingColorSelectionPreview() {
    MensinatorTheme {
        SettingColorSelection(
            colorSetting = ColorSetting.PERIOD,
            currentColor = Color.Red,
            openColorPickerForSetting = null,
            onClosePicker = {},
            onColorChange = { _, _ -> },
            onOpenColorPicker = {},
        )
    }
}

@Composable
private fun ColorPicker(
    colorSetting: ColorSetting,
    onClosePicker: () -> Unit,
    onSelectColor: (setting: ColorSetting, colorName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = { onClosePicker() },
        modifier = modifier.wrapContentSize()
    ) {
        val colorMap = ColorSource.getColorMap(isDarkMode())

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ColorSource.colorsGroupedByHue.forEach { colorGroup ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    colorGroup.forEach InnerLoop@{ colorName ->
                        val colorValue = colorMap[colorName] ?: return@InnerLoop
                        DropdownMenuItem(
                            text = {
                                Box(
                                    modifier = Modifier
                                        .size(colorCircleSize)
                                        .background(colorValue, CircleShape)
                                )
                            },
                            modifier = Modifier.size(colorCircleSize * 2),
                            onClick = { onSelectColor(colorSetting, colorName) },
                        )
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 200, heightDp = 300, showBackground = true)
@Composable
private fun ColorPickerPreview() {
    MensinatorTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ColorPicker(
                colorSetting = ColorSetting.PERIOD,
                onClosePicker = {},
                onSelectColor = { _, _ -> },
            )
        }
    }
}

@Composable
private fun SettingText(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.weight(1f), maxLines = 1)
        Spacer(Modifier.width(4.dp))
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_reminder),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SettingNumberSelection(
    intSetting: IntSetting,
    text: String,
    openIntPickerForSetting: IntSetting?,
    modifier: Modifier = Modifier,
    onClosePicker: () -> Unit,
    onNumberChange: (IntSetting, Int) -> Unit,
    onOpenIntPicker: (setting: IntSetting) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = stringResource(intSetting.stringResId), modifier = Modifier.weight(1f))
        Spacer(Modifier.width(4.dp))
        IconButton(
            onClick = { onOpenIntPicker(intSetting) },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        if (openIntPickerForSetting != intSetting) return
        Column {
            IntPicker(
                intSetting = intSetting,
                onClosePicker = onClosePicker,
                onNumberChange = onNumberChange,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun IntPicker(
    intSetting: IntSetting,
    onClosePicker: () -> Unit,
    onNumberChange: (IntSetting, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pickableNumbers = 1..12
    DropdownMenu(
        expanded = true,
        onDismissRequest = { onClosePicker() },
        modifier = modifier
    ) {
        pickableNumbers.forEach {
            DropdownMenuItem(
                text = { Text(it.toString()) },
                onClick = { onNumberChange(intSetting, it) },
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SettingNumberSelectionPreview() {
    MensinatorTheme {
        SettingNumberSelection(
            intSetting = IntSetting.PERIOD_HISTORY,
            text = "3",
            openIntPickerForSetting = null,
            onClosePicker = { },
            onNumberChange = { _: IntSetting, _: Int -> },
            onOpenIntPicker = { }
        )
    }
}

@Composable
private fun SettingSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (newValue: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(4.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = appDRed,
                checkedTrackColor = appDRed.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun SettingLanguagePicker() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        /**
         *  On lower Android versions, there is no possibility to
         *  set app-specific languages.
         *  The device language list is used automatically.
         */
        return
    }

    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.language), modifier = Modifier.weight(1f))
        Spacer(Modifier.width(4.dp))
        IconButton(
            onClick = {
                val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_language),
                contentDescription = stringResource(R.string.change_language),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExportRow(viewModel: SettingsViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.data),
            modifier = Modifier.widthIn(max = 200.dp)
        )
        Spacer(Modifier.width(4.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        ) {
            OutlinedButton(
                onClick = { viewModel.showExportDialog(true) },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
            ) {
                Text(text = stringResource(R.string.data_export))
            }
            OutlinedButton(
                onClick = { viewModel.exportPeriodsToPdf() },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
            ) {
                Text(text = stringResource(R.string.export_periods_pdf))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingTextPreview() {
    MensinatorTheme {
        SettingText(text = "Example settings text", onClick = {})
    }
}

private fun openNotificationSettings(context: Context) {
    val intent = Intent().apply {
        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
private fun NewScreenPreview() {
    MensinatorTheme {
        // Doesn't work yet, we can't preview when depending on ViewModel
        SettingsScreen(onSwitchProtectionScreen = {})
    }
}

