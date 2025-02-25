package com.example.smartgarden

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartgarden.ui.theme.SmartGardenTheme

class EditGarden : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGardenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    editGardenPage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun editGardenPage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var new_garden_name = remember { mutableStateOf("") }
    var new_garden_discription = remember { mutableStateOf("") }
    var new_arduino_discription = remember { mutableStateOf("") }
    var new_arduino = remember { mutableStateOf("") }
    var arduino_data = remember { mutableStateOf<ArrayList<Arduino>?>(null) }
    var old_arduino = remember { mutableStateOf("") }

    LaunchedEffect(false) {
        ConnectToAPI().getGardenFromAPI { result ->
            old_arduino.value = result[0]
            new_arduino.value = result[0]
            new_arduino_discription.value = result[1]
            new_garden_name.value = result[2]
            new_garden_discription.value = result[3]

            Log.d("MyTag", "$result")
        }
        ConnectToAPI().getArduinoFromAPI { result ->
            arduino_data.value = result
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopMenu(
            context,
            "Изменение грядки",
            "backarrow",
            Icons.Default.ArrowBack,
            { backArrowButton(context) }
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.02f))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Text("Новые данные", fontSize = 22.sp)
        }

        Spacer(modifier=Modifier.fillMaxHeight(0.06f))

        Text(modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp), text = "Теплица", fontSize = 16.sp)
        InputString(
            new_garden_name.value,
            { new_garden_name.value = it},
            "Новое название",
            "Синяя теплица",
            false
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.02f))

        InputString(
            new_garden_discription.value,
            { new_garden_discription.value = it},
            "Новое Описание",
            "Синяя теплица",
            false
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.06f))

        Text(modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp), text = "Ардуино", fontSize = 16.sp)


        Log.d("MyTag", "${new_arduino.value}")
        DropMenu(
            {new_arduino.value = it },
            "Ардуино",
            "Ваши ардуино",
            arduino_data.value,
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.02f))

        InputString(
            new_arduino_discription.value,
            { new_arduino_discription.value = it},
            "Новое описание aрдуино",
            "Синяя теплица",
            false
        )



        Spacer(modifier = Modifier.fillMaxHeight(0.12f))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Button(modifier = Modifier.padding(horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.SignInButton)),
                onClick = {
                    val arduino_id = if (new_arduino.value != old_arduino.value)  new_arduino.value.toInt() else old_arduino.value.toInt()
                    saveChange(context,
                        new_garden_name.value,
                        new_garden_discription.value,
                        arduino_id,
                        new_arduino_discription.value)
                }) {
                Text("Сохранить", fontSize = 26.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview7() {
    SmartGardenTheme {
        editGardenPage()
    }
}