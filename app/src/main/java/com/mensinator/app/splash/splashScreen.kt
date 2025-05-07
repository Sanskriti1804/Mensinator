package com.mensinator.app.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mensinator.app.R
import com.mensinator.app.ui.navigation.Screen
import com.mensinator.app.ui.theme.appDRed
import com.mensinator.app.ui.theme.appWhite
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {
    // Bina Firebase ke chill scene 🍹
    LaunchedEffect(Unit) {
        delay(3000)
        // Direct Login page pe bhej do
        navController.navigate(Screen.Calendar.name) {
            popUpTo(Screen.Splash.name) { inclusive = true }
        }
    }

    val customFont = FontFamily(
        Font(R.font.appfont, FontWeight.Normal)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets(0.dp))
            .background(appDRed),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Icon",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
//                text = "महावरी",
                text = "Mahavari",
                fontFamily = customFont,
                fontSize = 56.sp,
                fontWeight = FontWeight.Normal,
                color = appWhite
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    val dummyNavController = rememberNavController()
    SplashScreen(navController = dummyNavController)
}
