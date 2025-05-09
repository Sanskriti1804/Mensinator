package com.mensinator.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.provider.WebAuthProvider
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
    private lateinit var account: Auth0

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
//        / Remove splash screen before setContentView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.remove() // Remove immediately
            }
        }
        super.onCreate(savedInstanceState)
        account = Auth0(
            "H3Ef5JXQUiWrCriYFHRNkIORsoSilqoj",
            "dev-at7fp1npd0avhgjf.us.auth0.com"
        )
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == Intent.ACTION_VIEW) {
            WebAuthProvider.resume(intent)
        }
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