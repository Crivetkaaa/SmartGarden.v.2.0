package com.example.smartgarden

import android.content.Context
import android.os.Bundle
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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartgarden.ui.theme.SmartGardenTheme
import java.nio.file.WatchEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGardenTheme {

                    Greeting(LocalContext.current)
                }
            }
        }
}

@Composable
fun Greeting(context: Context) {
    var login = rememberSaveable { mutableStateOf("1") }
    var password = rememberSaveable { mutableStateOf("1") }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TopMenu(context,
                "Авторизация",
                "smile",
                Icons.Default.Face,
                function = {})

            Spacer(modifier = Modifier.fillMaxHeight(0.3f))

            InputString(
                myvalue = login.value,
                myonvalue = { login.value = it },
                label = "Email",
                placeholder = "youremail@mail.ru",
                visual = false
            )

            Spacer(Modifier.fillMaxHeight(0.02f))

            InputString(
                myvalue = password.value,
                myonvalue = { password.value = it },
                label = "Password",
                placeholder = "Your password",
                visual = true
            )
            Spacer(Modifier.fillMaxHeight(0.12f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { authorizationButton(context, login.value, password.value) },
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.SignInButton))) {
                    Text("Sign in", fontSize = 26.sp)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { SignUpButton(context) },
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.SignUpButton))) {
                    Text("Sign up", fontSize = 20.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartGardenTheme {
        Greeting(LocalContext.current)
    }
}