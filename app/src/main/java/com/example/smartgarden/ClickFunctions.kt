package com.example.smartgarden

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import kotlin.math.log

fun backArrowButton(context: Context){
    Log.d("MyTag", "Back arrow was clickable")
        (context as Activity)!!.finish()
}


fun SignUpButton(context: Context){
    context.startActivity(Intent(context, RegPage::class.java))
}

fun homeButton(context: Context){
//    TODO Переадресация
    Log.d("MyTag", "Home menu was clickable")
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