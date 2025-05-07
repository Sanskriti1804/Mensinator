package com.mensinator.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.mensinator.app.NotificationChannelConstants.channelDescription
import com.mensinator.app.NotificationChannelConstants.channelId
import com.mensinator.app.NotificationChannelConstants.channelName
import com.mensinator.app.ui.navigation.MensinatorApp
import com.mensinator.app.ui.theme.MensinatorTheme
import org.koin.androidx.compose.KoinAndroidContext

@Suppress("ConstPropertyName")
object NotificationChannelConstants {
    const val channelId = "1"
    const val channelName = "Mensinator"
    const val channelDescription = "Reminders about upcoming periods"
}

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
//        / Remove splash screen before setContentView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.remove() // Remove immediately
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MensinatorTheme {
                KoinAndroidContext {
                    MensinatorApp(
                        onScreenProtectionChanged = ::handleScreenProtection
                    )
                }
            }
        }
        createNotificationChannel(this)
    }

    private fun createNotificationChannel(context: Context) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun handleScreenProtection(isScreenProtectionEnabled: Boolean) {
        Log.d("screenProtectionUI", "protect screen value $isScreenProtectionEnabled")
        // Sets the flags for screen protection if
        // isScreenProtectionEnabled == true
        // If isScreenProtectionEnabled == false it removes the flags
        if (isScreenProtectionEnabled) {
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}