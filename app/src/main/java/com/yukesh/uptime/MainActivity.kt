package com.yukesh.uptime

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yukesh.uptime.ui.theme.UptimeTheme
//import com.yukesh.uptime.utils.PreferenceManager

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val sharedPreferences= PreferenceManager(applicationContext)
        setContent {
            UptimeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text("Uptime") })
                    }
                ) { innerPadding ->
                    App(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier ) {

//    var data= sharedPreferences.getData("MYPREF",false )

    val isLockEnabled = remember {
        mutableStateOf(false);
    }

    var isLocked by remember {
        mutableStateOf(true)
    }

    Column(modifier = modifier) {
        if (isLockEnabled.value && isLocked) LockScreen(){
            value-> isLocked=value
        } else Home(isLockEnabled)
    }
}

@Composable
fun LockScreen(onLockChanged: (Boolean) -> Unit){
    var pinValue by remember {
        mutableStateOf("")
    }
    var pin by remember {
        mutableStateOf("1234")
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)){
        Column {
            OutlinedTextField(value = pinValue , onValueChange ={
                if(it!=""){
                    pinValue=it

                    if(pin==pinValue){
                        onLockChanged(false)
                    }
                }
            } )
        }
    }
}

@Composable
fun Home(isLockEnabled:MutableState<Boolean>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            ) {
            Text(text = "Lock enabled")
             Switch(checked = isLockEnabled.value, onCheckedChange = {
                 isLockEnabled.value=it;
             })
        }
    }
}


