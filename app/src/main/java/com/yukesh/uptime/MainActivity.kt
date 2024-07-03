package com.yukesh.uptime

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UptimeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text("Uptime") })
                    }
                ) { innerPadding ->
                    App(modifier = Modifier.padding(innerPadding), applicationContext)
                }
            }
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun App(modifier: Modifier = Modifier, context: Context) {
        val sharedPreferences = context.getSharedPreferences("PREF", Context.MODE_PRIVATE)
        val scope = rememberCoroutineScope()
        val isLockEnabled = remember { mutableStateOf(false) }

        var isLocked by remember { mutableStateOf(isLockEnabled.value) }

        LaunchedEffect(Unit) {
            isLockEnabled.value = withContext(Dispatchers.IO) {
                sharedPreferences.getBoolean("isLocked", false)
            }
            isLocked = isLockEnabled.value
        }

        Column(modifier = modifier) {
            if (isLockEnabled.value && isLocked) {
                LockScreen { value -> isLocked = value }
            } else {
                Home(isLockEnabled, sharedPreferences,context)
            }
        }
    }


    @Composable
    fun LockScreen(onLockChanged: (Boolean) -> Unit) {
        var pinValue by remember { mutableStateOf("") }
        val correctPin = "1234"

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                OutlinedTextField(value = pinValue, onValueChange = {
                    pinValue = it
                    if (it == correctPin) {
                        onLockChanged(false)
                    }
                })
            }
        }
    }

    @Composable
    fun Home(isLockEnabled: MutableState<Boolean>, sharedPreferences: SharedPreferences,context: Context) {
        val scope = rememberCoroutineScope()
        val packageManager = context.packageManager
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val packageNames = mutableListOf<String>()
        // Populate packageNames with package names of installed apps
        apps.forEach { app ->
            packageNames.add(app.packageName)
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Lock enabled")
                Switch(checked = isLockEnabled.value, onCheckedChange = {
                    isLockEnabled.value = it
                    Log.d("AppDebug", "Lock state changed: $it")
                    scope.launch(Dispatchers.IO) {
                        sharedPreferences.edit().putBoolean("isLocked", it).apply()
                        Log.d("AppDebug", "Lock state saved to SharedPreferences")
                    }
                })
            }

            LazyColumn {
                items(packageNames.size) { i ->
                    ListItem(headlineContent = { Text(text = "${packageNames[i]}") } )
                }

            }
        }
    }

}



