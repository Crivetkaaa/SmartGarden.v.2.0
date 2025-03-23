package com.example.smartgarden

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.rememberTopAppBarState

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
    context.startActivity(Intent(context, addArduinoPage::class.java))
}

fun authorizationButton(context: Context, login: String, password: String){
    if (login.isEmpty() || password.isEmpty()){
        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
    } else {
        ConnectToAPI().authorization(login, password) {status, errostatus ->
            if (status){
                context.startActivity(Intent(context, HomePage::class.java))
                (context as Activity).finish()
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

fun addArduinoInGarden(context: Context, arduino_id: Int = 0, garden_name: String, garden_discription: String, vegetable: String = ""){
    if (garden_name.isEmpty() || garden_discription.isEmpty()){
        Toast.makeText(context, "Название и описание теплицы не могут быть пустыми", Toast.LENGTH_SHORT).show()
    } else if (vegetable != "" && arduino_id == 0){
        Toast.makeText(context, "Тип овощей не может быть указан с пустым полем ардуино", Toast.LENGTH_SHORT).show()
    } else {
        (context as Activity).finish()
        val params = mapOf(
            "arduino_id" to arduino_id,
            "garden_name" to garden_name,
            "garden_description" to garden_discription,
            "user_id" to usrManager.usr!!.id,
            "vegetable" to vegetable
        )

        Log.d("MyTag", params.toString())
        ConnectToAPI().addArduinGardenAPI(params) { status, result ->
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
            if (status) {
                context.startActivity(Intent(context, HomePage::class.java))
            }
        }
    }
}

fun openGardenPage(context: Context, garden_id: Int){
    usrManager.garden_clickable = garden_id
    context.startActivity(Intent(context, GardenPage::class.java))
}

fun deleteButton(context: Context){
    val params = mapOf(
        "garden" to true,
        "garden_id" to usrManager.garden_clickable!!.toInt()
    )
    Log.d("MyTag", "$params")
    ConnectToAPI().postDeleteGarden(params) { result ->
        val message = if (result) "Теплица удалена" else "Проблемы с подключенгием"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        if (result) {
            (context as Activity).finish()
            context.startActivity(Intent(context, HomePage::class.java))
        }
    }
}

//fun saveChange(context: Context, garden_name: String, garden_discription: String,
//               arduino_id: Int, arduino_discription: String){
//    if (garden_name.isEmpty() || garden_discription.isEmpty()){
//        Toast.makeText(context, "Название и описание не могут быть пустыми", Toast.LENGTH_SHORT).show()
//    } else {
//        (context as Activity).finish()
//        val params = mapOf(
//            "arduino_in_garden" to usrManager.aringr!!.toInt(),
//            "garden_id" to usrManager.garden_clickable!!.toInt(),
//            "garden_name" to garden_name,
//            "garden_description" to garden_discription,
//            "new_arduino_id" to arduino_id,
//            "arduino_description" to arduino_discription
//        )
//
//        ConnectToAPI().postUpdateGarden(params) { result ->
//            Log.d("MyTag","wefwe")
//        }
//        Log.d("MyTag", "${JSONObject(params)}")
//    }
//}

fun getGardenInfo(context: Context, callback: (GardenArduino?) -> Unit){
    ConnectToAPI().getGardenFromAPI { status, result ->
        if (status) {
            callback(result!!)
        } else {
            Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
        }
    }
}

fun editButtonGarden(context: Context){
    val intent = Intent(context, EditPage::class.java).apply {
        putExtra("garden", true)
    }
    context.startActivity(intent)
}

fun editButtonArduino(context: Context){
    val intent = Intent(context, EditPage::class.java).apply {
        putExtra("garden", false)
    }
    context.startActivity(intent)
}

fun saveEdit(context: Context, isGarden: Boolean, newName: String, newDescription: String) {
    val params: Map<String, Any>
    if (isGarden) {
        params = mapOf(
            "garden" to true,
            "garden_id" to usrManager.gardenarduino!!.garden!!.garden_id,
            "new_garden_name" to newName,
            "new_garden_description" to newDescription
        )
    } else {
        params = mapOf(
            "garden" to false,
            "garden_id" to usrManager.gardenarduino!!.garden!!.garden_id,
            "old_arduino_id" to usrManager.gardenarduino!!.arduino!!.arduino_id,
            "new_arduino_id" to newName.toInt(),
            "new_arduino_description" to newDescription
        )
    }

    ConnectToAPI().postUpdateGarden(params) { status ->
        if (status) {
            Toast.makeText(context, "Данные обновлены", Toast.LENGTH_SHORT).show()

            (context as Activity).finish()
        } else {
            Toast.makeText(context, "Попробуйте позже", Toast.LENGTH_SHORT).show()

        }
        Log.d("MyTag", params.toString())
    }
}

fun deleteArduino(context: Context, arduino_id: Int){
    val params = mapOf(
        "garden" to false,
        "arduino_id" to arduino_id
    )
    ConnectToAPI().postDeleteGarden(params){ status ->
        if (status) {
            Toast.makeText(context, "Ардуино была удалена", Toast.LENGTH_SHORT).show()
            (context as Activity).recreate()
        }
        else Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()

    }
}

fun getData(context: Context, arduino_id: Int, callback: (ArrayList<ArduinoData>?) -> Unit){
    ConnectToAPI().getArduinoData( arduino_id){ result ->
        callback(result)
    }
}

fun addArduino(context: Context, wifiSSID: String, wifiPassword: String,
               arduino_name: String, arduino_description: String){
    Log.d("MyTag", usrManager.usr!!.id.toString())
    if (!wifiSSID.equals("")) {
        val params = mapOf(
            "wifiSSID" to wifiSSID,
            "wifiPassword" to wifiPassword,
            "ardiono_name" to arduino_name,
            "arduino_description" to arduino_description,
            "user_id" to usrManager.usr!!.id
        )

        Log.d("MyTag", params.toString())
        ConnectToESP().postArduino(params) { status ->
            if (status) {
                Toast.makeText(context, "Ардуино добавлено", Toast.LENGTH_SHORT).show()
                Log.d("MyTag", "good")
                context.startActivity(Intent(context, ArduinoPage::class.java))
            } else {
                Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                Log.d("MyTag", "bad")
            }
        }
    } else {
        Toast.makeText(context, "WiFi не может быть пустым",  Toast.LENGTH_SHORT).show()
    }
}

fun updateReleStatus(arduino_id: Int, rele_id: Int, rele_status: Boolean, callback: (ArrayList<Boolean>) -> Unit){
    val params = mapOf(
        "arduino_id" to arduino_id,
        "rele_id" to rele_id,
        "rele_status" to rele_status
    )

    ConnectToAPI().postReleStatus(params){ result->
        callback(result)
    }



}