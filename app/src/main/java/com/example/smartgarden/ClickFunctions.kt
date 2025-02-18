package com.example.smartgarden

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import kotlin.math.log

fun backArrowButton(context: Context){
        (context as Activity).finish()
}

fun SignUpButton(context: Context){
    context.startActivity(Intent(context, RegPage::class.java))
}

fun arduinoButton(context: Context){
    val intent = Intent(context, ArduinoPage::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Закроет все предыдущие экземпляры ArduinoPage
    context.startActivity(intent)
}

fun homeButton(context: Context){
    val intent = Intent(context, HomePage::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Закроет все предыдущие экземпляры ArduinoPage
    context.startActivity(intent)
}

fun addArduinoButton(context: Context){
//    TODO
    Log.d("MyTag", "add Arduino was clickable")
}

fun authorizationButton(context: Context, login: String, password: String){
    if (login.isEmpty() || password.isEmpty()){
        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
    } else {
        ConnectToAPI().authorization(login, password) {status, errostatus ->
            if (status){
                context.startActivity(Intent(context, HomePage::class.java))
            }
        }
    }
}

fun registrationButton(context: Context, login: String, password: String, password2: String){
    if (login.isEmpty() || password.isEmpty() || password2.isEmpty()) {
        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
    } else if (!password.equals(password2)) {
        Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
    } else {
        ConnectToAPI().registration(login, password){status ->
            if(status){
                Toast.makeText(context, "Вы были зарегистрированны", Toast.LENGTH_SHORT).show()
                (context as Activity).finish()
            } else {
                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun addGardenButton(context: Context){
    context.startActivity(Intent(context, AddGardenPage::class.java))
}

fun addArduinoInGarden(context: Context, arduino_id: Int = 0, garden_name: String, garden_discription: String){
    (context as Activity).finish()
    val params = mapOf(
        "arduino_id" to arduino_id,
        "garden_name" to garden_name,
        "garden_description" to garden_discription,
        "user_id" to usrManager.usr!!.id
    )
    ConnectToAPI().addArduinGardenAPI(params){ status, result ->
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
        if (status){
            context.startActivity(Intent(context, HomePage::class.java))
        }
    }

}

fun openGardenPage(context: Context, garden_id: Int){
    usrManager.garden_clickable = garden_id
    context.startActivity(Intent(context, GardenPage::class.java))
}