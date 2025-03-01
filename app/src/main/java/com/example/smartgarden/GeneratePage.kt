package com.example.smartgarden

import android.content.Context
import android.renderscript.ScriptGroup.Input
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp




@Composable
fun TopMenu(context: Context, text: String, description:String, icon:ImageVector, function: (() -> Unit),
            deleteButton: Boolean = false){


    Row(
        modifier = Modifier.background(colorResource(R.color.TopMenuColor)).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(60.dp, 60.dp).padding(horizontal = 12.dp)
                .clickable {
                    function.invoke()
                }
        )
        Text(
            text=text,
            fontSize = 32.sp
        )
        if (deleteButton) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "deleteButton",
                    modifier = Modifier.size(60.dp, 60.dp).padding(horizontal = 12.dp)
                        .clickable { deleteButton(context) }
                )
            }
        }
    }
}

@Composable
fun InputString(myvalue: String, myonvalue: (String) -> Unit,
                label: String, placeholder: String, visual:Boolean){

    val passwordTransformation = if (visual) PasswordVisualTransformation() else VisualTransformation.None


    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = myvalue,
            onValueChange = myonvalue,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            visualTransformation = passwordTransformation,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        )
    }
}

@Composable
fun DropMenu(myonvalue: (String) -> Unit, label: String, placeholder: String, arduino_data: ArrayList<Arduino>?){
    val expanded = remember { mutableStateOf(false) }
    var selectedOption = remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = selectedOption.value,
            onValueChange = myonvalue,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            readOnly = true,

            trailingIcon = {
                    Box {
                        IconButton(onClick = { expanded.value = true }) {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Показать меню"
                            )
                        }

                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false }
                        ) {
                            arduino_data?.forEach { arduino ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedOption.value = arduino.arduino_name
                                        myonvalue(arduino.arduino_id.toString())
                                        expanded.value = false
                                    },
                                    text = { Text("${arduino.arduino_name}") }
                                )
                            }
                        }
                    }
            }
        )
    }
}


@Composable
fun GardenMenuInfo(labelName: String, name: String, description: String, function: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
    ) {
        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
            Text(labelName, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 12.dp))
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Название: $name", fontSize = 18.sp, modifier = Modifier.padding(12.dp))
                Text(text = "Описание: $description", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 12.dp))
            }

            IconButton(
                onClick = { function() },
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "editButton")
            }
        }

        HorizontalDivider(Modifier.padding(vertical = 6.dp), thickness = 2.dp)
    }
}