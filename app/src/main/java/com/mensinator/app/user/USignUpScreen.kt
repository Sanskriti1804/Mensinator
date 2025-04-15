package com.mensinator.app.user

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mensinator.app.R

@Composable
fun SignUpLogo(
    assetRes: String,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    isLooping: Boolean = true,
    isPlaying: Boolean = true
) {
    // Implement your logo animation here (e.g., LottieAnimation)
}

@Composable
fun SHeader(
    text: String,
    fontSize: TextUnit = 24.sp,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier.padding(bottom = 16.dp),
    textColor: Color = Color.Red
) {
    Text(
        text = text,
        fontSize = fontSize,
        modifier = modifier,
        fontFamily = fontFamily,
        color = textColor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextSignUpField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textfieldColor: Color = Color.Black,
    iconColor: Color = Color.Red,
    labelColor: Color = Color.Red,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
    shape: Shape = RoundedCornerShape(8.dp)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = labelColor) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier,
        visualTransformation = if (isPassword) PasswordVisualTransformation()
        else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            unfocusedLeadingIconColor = iconColor,
            unfocusedTrailingIconColor = iconColor,
            unfocusedContainerColor = textfieldColor,
            focusedContainerColor = textfieldColor,
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black
        ),
        shape = shape
    )
}

@Composable
fun SCustomButton(
    text: String,
    buttonColors: Color = Color.Red,
    textColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(8.dp),
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColors
        ),
        modifier = modifier,
        shape = shape
    ) {
        Text(
            text = text,
            color = textColor
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    val AppHeaderFont = FontFamily(Font(R.font.appfont))

    var passwordVisibility by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    val result by authViewModel.authResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignUpLogo(
            assetRes = "heart_icon.json", // Update asset as required
            size = 120.dp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Header(
            text = "ChatRoom",
            fontFamily = AppHeaderFont,
            fontSize = 42.sp
        )

        CustomTextSignUpField(
            label = "E-Mail",
            value = email,
            onValueChange = { email = it },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_email),
                    contentDescription = "email",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            labelColor = Color.Red,
            iconColor = Color.Red,
        )

        CustomTextSignUpField(
            label = "Username",
            value = firstName,
            onValueChange = { firstName = it },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_username),
                    contentDescription = "username",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            labelColor = Color.Red,
            iconColor = Color.Red,
        )

        CustomTextSignUpField(
            label = "Name",
            value = lastName,
            onValueChange = { lastName = it },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_user),
                    contentDescription = "name",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            labelColor = Color.Red,
            iconColor = Color.Red,
        )

        CustomTextSignUpField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_password),
                    contentDescription = "password",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                Icon(painter = painterResource(id = R.drawable.icon_password_visibility),
                    contentDescription = "password visibility",
                    tint = Color.Red,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(12.dp)
                        .clickable { passwordVisibility = !passwordVisibility }
                )
            },
            isPassword = !passwordVisibility,
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            labelColor = Color.Red,
            iconColor = Color.Red,
        )

        // Show loading state if sign-up is in progress
        when (result) {
            is Result.Loading -> {
                CustomButton(
                    text = "Signing Up...",
                    textColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    onClick = {}
                )
            }

            is Result.Success -> {
                CustomButton(
                    text = "SIGN UP",
                    textColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    onClick = {
                        onSignUpSuccess()
                    }
                )
            }

            is Result.Error -> {
                CustomButton(
                    text = "SIGN UP",
                    textColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    onClick = {
                        // Retry logic if needed
                    }
                )
                Text(
                    text = "Sign up failed. Please try again.",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            else -> {
                CustomButton(
                    text = "SIGN UP",
                    textColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    onClick = {
                        authViewModel.signUp(email, password, firstName, lastName)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Already have an account? Sign in.",
            color = Color.Black,
            modifier = Modifier.clickable { onNavigateToLogin() }
        )
    }
}
