package com.mensinator.app.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator

/**
 * Responsive layout that adapts to different screen sizes
 */
@Composable
fun ResponsiveLayout(
    modifier: Modifier = Modifier,
    content: @Composable (ResponsiveLayoutInfo) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    
    val layoutInfo = ResponsiveLayoutInfo(
        screenWidth = screenWidth,
        screenHeight = screenHeight,
        isLandscape = isLandscape,
        isTablet = screenWidth >= 600.dp,
        isPhone = screenWidth < 600.dp,
        isLargeScreen = screenWidth >= 840.dp
    )
    
    content(layoutInfo)
}

/**
 * Information about the current screen layout
 */
data class ResponsiveLayoutInfo(
    val screenWidth: androidx.compose.ui.unit.Dp,
    val screenHeight: androidx.compose.ui.unit.Dp,
    val isLandscape: Boolean,
    val isTablet: Boolean,
    val isPhone: Boolean,
    val isLargeScreen: Boolean
)

/**
 * Responsive column that adjusts spacing based on screen size
 */
@Composable
fun ResponsiveColumn(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    ResponsiveLayout { layoutInfo ->
        val spacing = when {
            layoutInfo.isTablet -> 24.dp
            layoutInfo.isLargeScreen -> 32.dp
            else -> 16.dp
        }
        
        Column(
            modifier = modifier,
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = Arrangement.spacedBy(spacing),
            content = content
        )
    }
}

/**
 * Responsive row that adjusts spacing based on screen size
 */
@Composable
fun ResponsiveRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit
) {
    ResponsiveLayout { layoutInfo ->
        val spacing = when {
            layoutInfo.isTablet -> 24.dp
            layoutInfo.isLargeScreen -> 32.dp
            else -> 16.dp
        }
        
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = verticalAlignment,
            content = content
        )
    }
}

/**
 * Responsive card that adjusts padding and elevation based on screen size
 */
@Composable
fun ResponsiveCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ResponsiveLayout { layoutInfo ->
        val padding = when {
            layoutInfo.isTablet -> 24.dp
            layoutInfo.isLargeScreen -> 32.dp
            else -> 16.dp
        }
        
        val elevation = when {
            layoutInfo.isTablet -> 4.dp
            layoutInfo.isLargeScreen -> 6.dp
            else -> 2.dp
        }
        
        Card(
            modifier = modifier,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
        ) {
            Box(modifier = Modifier.padding(padding)) {
                content()
            }
        }
    }
}

/**
 * Responsive text that adjusts size based on screen size
 */
@Composable
fun ResponsiveText(
    text: String,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium
) {
    ResponsiveLayout { layoutInfo ->
        val responsiveStyle = when {
            layoutInfo.isTablet -> style.copy(fontSize = style.fontSize * 1.2f)
            layoutInfo.isLargeScreen -> style.copy(fontSize = style.fontSize * 1.4f)
            else -> style
        }
        
        Text(
            text = text,
            modifier = modifier,
            style = responsiveStyle
        )
    }
}

/**
 * Responsive button that adjusts size based on screen size
 */
@Composable
fun ResponsiveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    variant: ButtonVariant = ButtonVariant.Primary
) {
    ResponsiveLayout { layoutInfo ->
        val buttonModifier = when {
            layoutInfo.isTablet -> modifier.height(56.dp)
            layoutInfo.isLargeScreen -> modifier.height(64.dp)
            else -> modifier.height(48.dp)
        }
        
        MensinatorButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            text = text,
            icon = icon,
            variant = variant
        )
    }
}

/**
 * Adaptive layout that shows different content based on screen size
 */
@Composable
fun AdaptiveLayout(
    phoneContent: @Composable () -> Unit,
    tabletContent: @Composable () -> Unit,
    largeScreenContent: @Composable () -> Unit
) {
    ResponsiveLayout { layoutInfo ->
        when {
            layoutInfo.isLargeScreen -> largeScreenContent()
            layoutInfo.isTablet -> tabletContent()
            else -> phoneContent()
        }
    }
}
