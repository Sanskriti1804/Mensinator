import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials

@Composable
fun LoginScreen(account: Auth0) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Google Login Button
        Button(
            onClick = {
                loginWithGoogle(context, account)
            }
        ) {
            Text("Login with Google")
        }
    }
}

private fun loginWithGoogle(context: Context, account: Auth0) {
    WebAuthProvider.login(account)
        .withScheme("demo") // Must match auth0Scheme in build.gradle
        .withConnection("google-oauth2") // Force Google login
        .start(context, object : Callback<Credentials, AuthenticationException> {
            override fun onSuccess(credentials: Credentials) {
                Log.d("Auth0", "Google Login Success: ${credentials.accessToken}")
                // Handle successful login (e.g., navigate to home)
            }

            override fun onFailure(error: AuthenticationException) {
                Log.e("Auth0", "Google Login Failed: ${error.message}")
            }
        })
}