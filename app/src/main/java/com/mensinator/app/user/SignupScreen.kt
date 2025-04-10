package com.mensinator.app.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mensinator.app.R

@Composable
fun BackgroundColor(modifier: Modifier= Modifier, backgroundColor: Color = Color.White){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    )
}

@Composable
fun Heading(
    text : String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp,
    fontFamily: FontFamily = FontFamily.Default,
    textColor: Color = Color.Black
){
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        fontFamily = fontFamily,
        color = textColor
    )
}

@Composable
fun Logo(
    assetRes : String,
    modifier: Modifier = Modifier,
    size : Dp = 100.dp,
    isLooping : Boolean = true,
    isPlaying : Boolean = true
){
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    label : String,
    value : String,
    //call back function (lambda function)
    //string - i/p, return - void
    onValueChange : (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword : Boolean = false,
    //takes no parameter and returns nothing
    //@composable - provides flexibility(as icon can be a vector, or string or anything?)
    leadingIcon : @Composable (() -> Unit)? = null,
    trailingIcon : @Composable (() -> Unit)? = null,
    labelColor : Color = Color.Black,
    iconColor : Color = Color.Black,
    textFieldColor : Color = Color.Black,
    shape : Shape = RoundedCornerShape(8.dp),
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = labelColor) },
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = textFieldColor,
            unfocusedContainerColor = textFieldColor,
            focusedLeadingIconColor = iconColor,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation =
        if (isPassword) PasswordVisualTransformation()
        else VisualTransformation.None,
        shape = shape
    )
}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text : String,
    textColor : Color = Color.White,
    buttonColor: Color,
    shape: Shape = RoundedCornerShape(8.dp),
    onClick: () -> Unit,
){
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
    ) {
        Text(
            text = text,
            color = textColor
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    //anonymous fun(no parameter no return) to provide navigation
    //when calling LoginScreen - need to pass a function
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess:()->Unit

    //EXAMPLE -
    // composable(Screen.LoginScreen.route) {
    //    LoginScreen(
    //        onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) },
    //    )}
) {
    val AppHeaderFont = FontFamily(
        Font(R.font.appfont)
    )

    var passwordVisibility by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    //firstName - username
    var firstName by remember { mutableStateOf("") }
    //lastname - name of user
    var lastName by remember { mutableStateOf("") }

//    BackgroundColor(backgroundColor = ChatWhite) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo(
            assetRes = "heart_icon.json",
//                assetRes = "smiley_icon.json",
            size = 120.dp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Heading(
            text = "ChatRoom",
            modifier = Modifier.padding(20.dp),
            fontSize = 42.sp,
            fontFamily = AppHeaderFont,
            textColor = Color.Red
        )

        CustomTextField(
            label = "E-Mail",
            value = email,
            onValueChange = { email = it },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.icon_email),
                    contentDescription = "email",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                ) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textFieldColor = Color.Red,
            labelColor = Color.Red,
            iconColor = Color.Red,
        )

        CustomTextField(
            label = "Username",
            value = firstName,
            onValueChange = { firstName = it },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.icon_username),
                    contentDescription = "username",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                ) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textFieldColor = Color.Red,
            labelColor = Color.Red,
            iconColor = Color.Red,
        )

        CustomTextField(
            label = "Name",
            value = lastName,
            onValueChange = { lastName = it },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.icon_user),
                    contentDescription = "name",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                ) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textFieldColor = Color.Red,
            labelColor = Color.Red,
            iconColor = Color.Red,
        )

        CustomTextField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.icon_password),
                    contentDescription = "password",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                ) },
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
                .padding(12.dp),
            textFieldColor = Color.Red,
            labelColor = Color.Red,
            iconColor = Color.Red,
        )

        CustomButton(
            text = "SIGN UP",
            buttonColor = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            textColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            onClick = {
                //signUp function
                authViewModel.signUp(email, password, firstName, lastName)
                email = ""
                password = ""
                firstName = ""
                lastName = ""
                onSignUpSuccess()

            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Already have an account? Sign in.", color = Color.Black,
            modifier = Modifier.clickable { onNavigateToLogin() }
        )
    }
}


//fun isUserAlreadySignIn(navController: NavController) {
//    if(FirebaseAuth.getInstance().currentUser != null){
//        navController.navigate(Screen.SignupScreen.route)
//    }
//}


