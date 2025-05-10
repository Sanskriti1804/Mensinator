package com.mensinator.app.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate(Screen.Calendar.name) {
            popUpTo(Screen.Splash.name) { inclusive = true }
        }
    }

    val customFont = FontFamily(Font(R.font.appfont, FontWeight.Normal))

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                text = "Mahavari",
                fontFamily = customFont,
                fontSize = 56.sp,
                fontWeight = FontWeight.Normal,
                color = appWhite
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Google Sign-In Button
            Button(
                onClick = {
                    navController.navigate(Screen.Calendar.name)
                },
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appWhite,
                    contentColor = appDRed
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Login with Google")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    val dummyNavController = rememberNavController()
    SplashScreen(navController = dummyNavController)
}
