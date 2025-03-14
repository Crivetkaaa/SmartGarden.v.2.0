package com.example.smartgarden

import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Intent
import android.net.wifi.ScanResult
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.smartgarden.ui.theme.SmartGardenTheme
import com.example.wifiscanner.Test
import com.example.wifiscanner.WifiScannerScreen

class addArduinoPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGardenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WifiScannerScreen(
                        LocalContext.current,
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiScannerScreen(context: Context, modifier: Modifier) {
    var wifiNetworks by remember { mutableStateOf<List<ScanResult>>(emptyList()) }
    var permissionGranted by remember { mutableStateOf(false) }
    var permissionRequested by remember { mutableStateOf(false) }

    var wifiName by remember { mutableStateOf("") }
    var wifiPassword by remember { mutableStateOf("") }

    var arduino_description by remember { mutableStateOf("") }
    var arduino_name by remember { mutableStateOf("") }

    // Запрос разрешения на доступ к местоположению
    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            wifiNetworks = getAvailableWifiNetworks(context) ?: emptyList()
        }
    }

    LaunchedEffect(Unit) {
        when {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                permissionGranted = true
                wifiNetworks = getAvailableWifiNetworks(context) ?: emptyList()
            }
            else -> {
                if (!permissionRequested) {
                    permissionRequested = true
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopMenu(
            context,
            "Выбор WiFi для Ardiono",
            "backarrow",
            Icons.Default.ArrowBack,
            { backArrowButton(context) },
        )

        Text(text = "Доступные Wi-Fi сети", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier.height(8.dp))

        if (permissionGranted) {
            DropWiFiMenu(
                {wifiName = it },
                "WiFi сеть для Arduino",
                "RT-WiFi-A4AD",
                wifiNetworks
            )
            Spacer(Modifier.fillMaxHeight(0.012f))
            InputString(
                wifiPassword,
                {wifiPassword = it},
                "Пароль",
                "Пароль",
                true
            )

            HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 2.dp)

            InputString(
                arduino_name,
                {arduino_name = it},
                "Название для Arduino",
                "Название",
            )
            Spacer(Modifier.height(8.dp))
            InputString(
                arduino_description,
                {arduino_description = it},
                "Описание для Arduino",
                "Описание",
            )
            Spacer(Modifier.height(12.dp))
            Button(colors = ButtonDefaults.buttonColors(colorResource(R.color.SignInButton)),
                onClick = {
                addArduino(context, wifiName, wifiPassword, arduino_name, arduino_description)
            }) {
                Text(text="Добавить", fontSize = 22.sp)
            }
        } else {
            Text(text = "Разрешение на доступ к местоположению не предоставлено", modifier = Modifier.padding(16.dp))
        }

    }
}

fun getAvailableWifiNetworks(context: Context): List<ScanResult>? {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    // Проверяем, включен ли Wi-Fi
    if (!wifiManager.isWifiEnabled) {
        return null // Wi-Fi не включен
    }

    // Проверяем разрешение перед запуском сканирования
    return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        try {
            wifiManager.startScan() // Запускаем сканирование
            // Добавляем задержку, чтобы дать время на завершение сканирования
            Thread.sleep(5) // Задержка в 2 секунды
            wifiManager.scanResults // Возвращаем список доступных сетей
        } catch (e: SecurityException) {
            // Обработка исключения, если разрешение было отклонено
            null
        }
    } else {
        null // Если разрешение не предоставлено, возвращаем null
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview8() {
    SmartGardenTheme {
        LocalContext.current
        WifiScannerScreen(LocalContext.current)
    }
}