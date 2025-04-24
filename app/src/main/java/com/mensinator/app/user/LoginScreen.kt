package com.mensinator.app.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mensinator.app.R
import com.mensinator.app.ui.theme.Black
import com.mensinator.app.ui.theme.appDRed
import com.mensinator.app.ui.theme.appWhite
import com.mensinator.app.ui.theme.isDarkMode

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isDarkMode = isDarkMode()

    val headingFont = FontFamily(
        Font(R.font.secfont) // Your custom font for the heading
    )

    // Generate login UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = appWhite)
            .padding(horizontal = 24.dp, vertical = 12.dp)
//            .align(Alignment.CenterHorizontally)
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = headingFont,
                color = appDRed,
                fontSize = 40.sp
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Username Field
        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text(text = "Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
//            colors = TextFieldDefaults.textFieldColors(
//                backgroundColor = if (isDarkMode) Color.Gray else Color.White,
//                focusedIndicatorColor = appDRed,
//                unfocusedIndicatorColor = Black
//            )
        )

        // Password Field
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
//            colors = TextFieldDefaults.textFieldColors(
//                backgroundColor = if (isDarkMode) Color.Gray else Color.White,
//                focusedIndicatorColor = appDRed,
//                unfocusedIndicatorColor = Black
//            ),
            visualTransformation = PasswordVisualTransformation() // For password masking
        )

        // Login Button
        Button(
            onClick = {
                if (username.value.isNotEmpty() && password.value.isNotEmpty()) {
                    // Call Toast inside Composable
                    onLoginSuccess() // Navigate to home or dashboard
                } else {
                    // Call Toast inside Composable
//                    Toast.makeText(
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )
        }

        // Forgot Password Button
        Text(
            text = "Forgot Password?",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = appDRed,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable {
                    // Handle forgot password logic
                },
            textAlign = TextAlign.Center
        )

        // Create Account Button
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Black,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable {
                    // Handle create account logic
                },
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showBackground = true, name = "Login Preview", widthDp = 360, heightDp = 800)
fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = {
        // Preview ke liye no-op function daala hai bhai
    })
}


