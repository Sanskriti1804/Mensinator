//package com.mensinator.app.user
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//
//@Composable
//fun LoginScreen(
//    viewModel: AuthViewModel,
//    onNavigateToCalender: () -> Unit,
//    onNavigateToSignUp: () -> Unit
//) {
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = viewModel.email,
//            onValueChange = { viewModel.email = it },
//            label = { Text("Email") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = viewModel.password,
//            onValueChange = { viewModel.password = it },
//            label = { Text("Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                viewModel.login()
//            },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = !viewModel.isLoading
//        ) {
//            Text(if (viewModel.isLoading) "Logging in..." else "Login")
//        }
//
//        TextButton(onClick = onNavigateToSignUp) {
//            Text("Don't have an account? Sign Up")
//        }
//
//        viewModel.errorMessage?.let {
//            Text(it, color = Color.Red)
//        }
//
//        if (viewModel.isSuccess) {
//            LaunchedEffect(Unit) {
//                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
//                onNavigateToCalender()
//            }
//        }
//    }
//}
