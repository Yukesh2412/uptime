package com.yukesh.uptime

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.yukesh.uptime.ui.theme.UptimeTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yukesh.uptime.ui.theme.UptimeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.provider.Settings
import android.view.WindowManager
import androidx.compose.ui.text.input.PasswordVisualTransformation

class LockScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        setContent {
            UptimeTheme {
                var isLocked by remember { mutableStateOf(true) }
                if (isLocked) {
                    LockScreen { isLocked = false }
                } else {
                    finish()
                }
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        navigateToHome()
    }
    override fun onDestroy() {
        super.onDestroy()
        resetLockScreenFlag()
    }

    private fun resetLockScreenFlag() {
        val serviceIntent = Intent(this, AppLockAccessibilityService::class.java)
        serviceIntent.action = "RESET_LOCK_SCREEN_FLAG"
        startService(serviceIntent)
    }

    private fun navigateToHome() {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
//        finish()
        resetLockScreenFlag()
    }
}

@Composable
fun LockScreen(onLockChanged: (Boolean) -> Unit) {
    var pinValue by remember { mutableStateOf("") }
    val correctPin = "1234"
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "hello")
//            OutlinedTextField(
//                value = pinValue,
//                onValueChange = {
//                    pinValue = it
//                    errorMessage = if (it != correctPin && it.length >= correctPin.length) "Incorrect PIN" else ""
//                    if (it == correctPin) {
//                        onLockChanged(false)
//                    }
//                },
//                visualTransformation = PasswordVisualTransformation(),
//                label = { Text("Enter PIN") }
//            )
//            if (errorMessage.isNotEmpty()) {
//                Text(
//                    text = errorMessage,
//                    color = MaterialTheme.colorScheme.error,
//                    modifier = Modifier.padding(top = 8.dp)
//                )
//            }
        }
    }
}

