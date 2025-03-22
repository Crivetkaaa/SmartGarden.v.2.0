package com.example.smartgarden
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize

class AddGardenPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                    Greeting3()
        }
    }
}

@Composable
fun Greeting3(modifier: Modifier = Modifier) {
    val content = LocalContext.current

    val garden_name = remember { mutableStateOf("") }
    val garden_discription = remember { mutableStateOf("") }
    val arduino_id = remember { mutableStateOf("") }
    val vegetable = remember { mutableStateOf("") }

    val free_arduin =  remember { mutableStateOf<ArrayList<Arduino>?>(null) }
    val vegetables = remember { mutableStateOf<ArrayList<Vegetables>?>(null) }

    LaunchedEffect(false) {
        usrManager.getArduino(true) { result ->
            free_arduin.value = result
        }
        ConnectToAPI().getVegetables { result->
            vegetables.value = result
        }
    }
    Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier.padding(innerPadding)) {
            TopMenu(
                content,
                "Новая теплица",
                "backarrow",
                Icons.Default.ArrowBack,
                { backArrowButton(content) },
            )

            Spacer(Modifier.fillMaxHeight(0.12f))

            InputString(
                garden_name.value,
                { garden_name.value = it },
                "Название теплицы",
                "Синяя теплица",
                false,
            )

            Spacer(Modifier.fillMaxWidth(0.06f).padding(6.dp))

            InputString(
                garden_discription.value,
                { garden_discription.value = it },
                "Описание теплицы",
                "Синяя теплица",
                false
            )

            Spacer(Modifier.fillMaxWidth(0.06f).padding(6.dp))

            DropMenu({arduino_id.value = it}, "Ардуино", "Ваша ардуино", free_arduin.value)

            Spacer(Modifier.fillMaxWidth(0.06f).padding(6.dp))

            DropVegetablesMenu({vegetable.value = it}, "Vegetabel", "Какой vegetabel", vegetables.value)

            Row(modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalArrangement = Arrangement.Center) {
                Button(modifier = Modifier.padding(12.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.SignInButton)),
                    onClick = {
                        if (arduino_id.value.isEmpty()) {
                            addArduinoInGarden(content,
                                garden_name=garden_name.value,
                                garden_discription=garden_discription.value,
                                vegetable = vegetable.value)
                        } else {
                            addArduinoInGarden(
                                content, arduino_id.value.toInt(),
                                garden_name.value, garden_discription.value, vegetable.value
                            )
                        }

                    }) {
                    Text("Добавить", fontSize = 22.sp)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
        Greeting3()
    }
