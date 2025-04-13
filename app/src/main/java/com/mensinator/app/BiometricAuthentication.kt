//package com.mensinator.app
//
//import android.content.Context
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.biometric.BiometricManager
//import androidx.biometric.BiometricPrompt
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.core.content.ContextCompat
//
//@Composable
//fun EnableBiometricAuth(context: Context) {
//    val activity = context as AppCompatActivity
//    val executor = ContextCompat.getMainExecutor(context)
//
//    val biometricPrompt =
//        BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
//            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                super.onAuthenticationSucceeded(result)
//                Toast.makeText(context, "Authentication Succeeded", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAuthenticationFailed() {
//                super.onAuthenticationFailed()
//                Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//    val promptInfo = BiometricPrompt.PromptInfo.Builder()
//        .setTitle("Biometric Authentication")
//        .setSubtitle("Log in with your fingerprint or face")
//        .setNegativeButtonText("Cancel")
//        .build()
//
//    biometricPrompt.authenticate(promptInfo)
//}
//
//fun isBiometricAvailable(context: Context): Boolean {
//    val biometricManager = BiometricManager.from(context)
//    return when (biometricManager.canAuthenticate()) {
//        BiometricManager.BIOMETRIC_SUCCESS -> true
//        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false
//        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false
//        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
//        else -> false
//    }
//}
//
//@Composable
//fun BiometricSetup() {
//    val context = LocalContext.current
//
//    if (isBiometricAvailable(context)) {
//        Button(
//            onClick = {
////                EnableBiometricAuth(context)
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = "Set Up Biometric Authentication")
//        }
//    } else {
//        Text(
//            text = "Your device doesn't support biometric authentication.",
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}
//
