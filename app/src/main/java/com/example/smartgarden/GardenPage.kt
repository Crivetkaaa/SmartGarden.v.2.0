package com.example.smartgarden

import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
    //TODO отобржать что тут овощь
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }

    var garden_infos by remember { mutableStateOf<GardenArduino?>(null) }
    var gardenData by remember { mutableStateOf<List<ArduinoData>?>(null) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                getGardenInfo(context) { result ->
                    garden_infos = result
                }


            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Column(modifier.fillMaxSize().verticalScroll(state)) {
        TopMenu(
            context,
            "Меню теплицы",
            "backArrow",
            Icons.AutoMirrored.Filled.ArrowBack,
            { backArrowButton(context) },
            true
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text("Данные о теплице", fontSize = 26.sp)
        }

        garden_infos?.garden?.let { gardenInfo ->
            GardenMenuInfo("Теплица",
                gardenInfo.garden_name,
                gardenInfo.garden_description,
                { editButtonGarden(context) })
        }
        garden_infos?.arduino?.let { arduinoInfo ->


            GardenMenuInfo("Aрдуино",
                arduinoInfo.arduino_name,
                arduinoInfo.arduino_description,
                { editButtonArduino(context) })
            Row(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text("Графики", fontSize = 20.sp)
            }
            getData(context, arduinoInfo.arduino_id) { result ->
                gardenData = result
            }
            gardenData?.let { data ->
                val temperatures = data.map { it.temperature.toFloat() }
                val air_h = data.map { it.humidity.toFloat() }
                val earth_h = data.map { it.earth_humidity.toFloat() }
                val dates = data.map { it.date }


                DrawTemperatureChart(
                    temperatures, dates,
                    colorResource(R.color.AirTemperatureGraphic), "Температура воздуха"
                )


                Spacer(Modifier.height(45.dp))
                DrawTemperatureChart(
                    air_h, dates,
                    colorResource(R.color.AirHumidityGraphic), "Влажность воздуха"
                )

                Spacer(Modifier.height(45.dp))
                DrawTemperatureChart(
                    earth_h, dates,
                    colorResource(R.color.EarthHumidityGraphic), "Влажность земли"
                )

            }
        }
    }
}

@Composable
fun DrawTemperatureChart(temperatures: List<Float>, dates: List<String>, mycolor: Color,
                         graphicsName: String) {
    val maxTemperature = temperatures.maxOrNull() ?: 1f
    val barWidth = 20.dp
    val spacing = 13.dp

    val totalWidth = (temperatures.size * (barWidth.value + spacing.value)).dp + 15.dp

    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }

    Row(
        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        Text("$graphicsName", fontSize = 20.sp)
    }

    Row(Modifier.horizontalScroll(state = state).padding(12.dp)) {
        Canvas(
            modifier = Modifier
                .width(totalWidth)
                .height(340.dp)
        ) {
            val canvasHeight: Float = size.height

            for (i in temperatures.indices) {
                val temperatureHeight =
                    (temperatures[i] / maxTemperature) * (canvasHeight * 0.8f)

                drawRect(
                    color = mycolor,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x = (i * (barWidth.toPx() + spacing.toPx())),
                        y = canvasHeight - temperatureHeight.toFloat() - 120f
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        width = barWidth.toPx(),
                        height = temperatureHeight.toFloat()
                    )
                )

                val textX = (i * (barWidth.toPx() + spacing.toPx())) + (barWidth.toPx() / 2)
                val textY = canvasHeight - temperatureHeight.toFloat() - 127f

                val paint = android.graphics.Paint().apply {
                    textSize = 30f
                    color = android.graphics.Color.BLACK
                    textAlign = android.graphics.Paint.Align.CENTER
                }

                drawContext.canvas.nativeCanvas.drawText(
                    temperatures[i].toString(),
                    textX,
                    textY,
                    paint
                )

                drawContext.canvas.nativeCanvas.save()

                val rotatedTextX = textX - 20f
                val rotatedTextY = canvasHeight - 15f

                drawContext.canvas.nativeCanvas.rotate(45f, rotatedTextX, rotatedTextY)

                val textHeight = paint.descent() - paint.ascent()

                drawContext.canvas.nativeCanvas.drawText(
                    dates[i],
                    rotatedTextX - (textHeight / 2) + 45f,
                    rotatedTextY - textHeight - 30f,
                    paint
                )

                drawContext.canvas.nativeCanvas.restore()
            }
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