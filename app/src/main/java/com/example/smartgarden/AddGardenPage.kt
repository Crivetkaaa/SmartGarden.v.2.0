package com.example.smartgarden
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartgarden.ui.theme.SmartGardenTheme

class AddGardenPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGardenTheme {
                    Greeting3()
            }
        }
    }
}

@Composable
fun Greeting3(modifier: Modifier = Modifier) {
    val content = LocalContext.current

    Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TopMenu(
                content,
                "Новая теплица",
                "backarrow",
                Icons.Default.ArrowBack,
                { backArrowButton(content) }
            )
            Text(
                text = "Hello",
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    SmartGardenTheme {
        Greeting3()
    }
}