package com.example.smartgarden

import android.content.Context
import android.renderscript.ScriptGroup.Input
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun TopMenu(context: Context, text: String, description:String, icon:ImageVector, function: (() -> Unit)){
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(colorResource(R.color.TopMenuColor)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "backbutton",
            modifier = Modifier.size(60.dp, 60.dp).padding(horizontal = 12.dp)
                .clickable {
                    function.invoke()
                }
        )
        Text(
            text=text,
            fontSize = 32.sp
        )
    }
}

@Composable
fun InputString(myvalue: String, myonvalue: (String) -> Unit,
                label: String, placeholder: String, visual:Boolean){
    val passwordTransformation = if (visual) PasswordVisualTransformation() else VisualTransformation.None
    TextField(
        value = myvalue,
        onValueChange = myonvalue,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        visualTransformation = passwordTransformation,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
    )

}
