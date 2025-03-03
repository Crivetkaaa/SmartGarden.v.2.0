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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartgarden.ui.theme.SmartGardenTheme

class EditPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGardenTheme {
                val editGarden = intent.getBooleanExtra("garden", true)
                    Greeting4(
                        whoedit = editGarden
                    )

            }
        }
    }
}

@Composable
fun Greeting4(whoedit:Boolean, modifier: Modifier = Modifier) {
    val content = LocalContext.current

    val arduino_id =
        remember { mutableStateOf("") }

    if (!whoedit){
        arduino_id.value = usrManager.gardenarduino!!.arduino!!.arduino_id.toString()
    }

    var name by remember { mutableStateOf<String?>("") }
    var description by remember { mutableStateOf<String?>("") }

    val free_arduin = remember { mutableStateOf<ArrayList<Arduino>?>(null) }

    LaunchedEffect(false) {
        usrManager.getArduino(true) { result ->
            free_arduin.value = result
        }
    }

    var topmenu = ""

    if (whoedit) {
        name = usrManager.gardenarduino!!.garden!!.garden_name
        description = usrManager.gardenarduino!!.garden!!.garden_description
        topmenu = "теплицы"
    } else {
        name = usrManager.gardenarduino!!.arduino!!.arduino_name
        description = usrManager.gardenarduino!!.arduino!!.arduino_description
        topmenu = "ардуино"

    }

    var namevalue by remember { mutableStateOf(name) }
    var descriptionvalue by remember { mutableStateOf(description) }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        Column(modifier.padding(innerPadding)) {
            TopMenu(
                content,
                "Редактирование $topmenu",
                "backArrow",
                Icons.AutoMirrored.Filled.ArrowBack,
                { backArrowButton(content) }
            )
            Spacer(Modifier.fillMaxHeight(0.24f))
            if (whoedit) {
                InputString(
                    namevalue.toString(),
                    { namevalue = it },
                    "Название",
                    "Дача",
                    false
                )
            } else {
                DropMenu(
                    { arduino_id.value = it },
                    "Ардуино",
                    "Aрдуино",
                    free_arduin.value,
                    usrManager.gardenarduino!!.arduino!!.arduino_name
                )

            }

            Spacer(Modifier.fillMaxHeight(0.02f))
            
            InputString(
                descriptionvalue.toString(),
                { descriptionvalue = it },
                "Описание",
                "Описание",
                false
            )

            Spacer(Modifier.fillMaxHeight(0.02f))


            Row(
                Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(colors = ButtonDefaults.buttonColors(colorResource(R.color.SignInButton)),
                    onClick = {

                    Log.d("MyTag", arduino_id.value!!.toString())

                    saveEdit(
                        content,
                        whoedit,
                        if (whoedit) namevalue.toString() else arduino_id.value,
                        descriptionvalue.toString()
                    )
                }) {
                    Text("Сохранить", fontSize = 26.sp)
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview7() {
    SmartGardenTheme {
        Greeting4(whoedit = true)
    }
}