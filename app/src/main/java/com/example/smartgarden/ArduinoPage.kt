package com.example.smartgarden

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.smartgarden.ui.theme.SmartGardenTheme

class ArduinoPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGardenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ArduinoPageGenerate(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ArduinoPageGenerate(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val arduins = remember { mutableStateOf<List<Arduino>?>(null)}
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                usrManager.getArduino() { result ->

                    arduins.value = result
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(modifier.fillMaxSize()) {
        TopMenu(
            context,
            "Ардуино меню",
            "Go to gardens menu",
            Icons.Filled.Home,
            { homeButton(context) }
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.03f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Ардуино", fontSize = 24.sp)
        }
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            items(arduins.value.orEmpty()) { arduino ->
                Log.d("MyTag", "$arduino")
                Card(
                    modifier = Modifier.padding(3.dp),
                    onClick = {}
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(R.color.ArduinoCard))
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f), // Занимает доступное пространство
                                    text = arduino.arduino_name,
                                    fontSize = 35.sp
                                )
                                IconButton(onClick = {
                                    deleteArduino(context, arduino.arduino_id)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Удалить",
                                        tint = Color.Red
                                    )
                                }
                            }

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 6.dp, vertical = 8.dp),
                                text = arduino.arduino_description,
                                fontSize = 22.sp
                            )
                        }
                    }
                }
            }
        }


        Button(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(6.dp),
            colors = ButtonDefaults
                .buttonColors(containerColor = colorResource(R.color.AddGardenButton)),
            shape = RoundedCornerShape(6.dp),
            onClick = { addArduinoButton(context) }) {

            Text(text = "Добавить ардуино", fontSize = 22.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    SmartGardenTheme {
        ArduinoPageGenerate()
    }
}