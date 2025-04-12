package com.mensinator.app

import android.content.Context
import android.hardware.biometrics.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*

@Composable
fun EnableBiometricAuth() {
    val context = LocalContext.current
    val activity = context as AppCompatActivity

    // Initialize the BiometricPrompt
    val executor = ContextCompat.getMainExecutor(context)
    val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            // Handle successful authentication
            Toast.makeText(context, "Authentication Succeeded", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            // Handle failed authentication
            Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show()
        }
    })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setSubtitle("Log in with your fingerprint or face")
        .setNegativeButtonText("Cancel")
        .build()

    Button(
        onClick = {
            biometricPrompt.authenticate(promptInfo)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Enable Biometric Authentication")
    }
}

fun isBiometricAvailable(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    return when (biometricManager.canAuthenticate()) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
        else -> false
    }
}

@Composable
fun BiometricSetup() {
    val context = LocalContext.current
    val activity = context as AppCompatActivity

    if (isBiometricAvailable(context)) {
        Button(
            onClick = {
                // Call the biometric authentication function (same as EnableBiometricAuth)
                EnableBiometricAuth()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Set Up Biometric Authentication")
        }
    } else {
        Text(
            text = "Your device doesn't support biometric authentication.",
            modifier = Modifier.fillMaxWidth()
        )
    }
}
