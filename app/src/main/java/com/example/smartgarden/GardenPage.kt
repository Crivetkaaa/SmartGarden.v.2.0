package com.example.smartgarden

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartgarden.ui.theme.SmartGardenTheme

class GardenPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGardenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GardenPagePainter(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GardenPagePainter(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var garden_data = remember { mutableStateOf<GardenArduino?>(null) }

    ConnectToAPI().getGardenFromAPI {  }

    Column(modifier.fillMaxSize()) {
        TopMenu(
            context,
            "Меню теплицы",
            "backArrow",
            Icons.Default.ArrowBack,
            { backArrowButton(context) },
            true
        )
        Text("vfbfdbfdb")
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){
            Text("Меню теплицы")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    SmartGardenTheme {
        GardenPagePainter()
    }
}