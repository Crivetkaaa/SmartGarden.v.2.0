package com.example.smartgarden

import android.util.Log
import androidx.core.location.GnssStatusCompat.Callback

val usrManager = UserManager()

data class User(
    val id: Int
)

data class Garden(
    val garden_id: Int,
    val garden_name: String,
    val garden_description: String
)

data class Arduino(
    val arduino_id: Int,
    val arduino_name: String,
    val arduino_description: String,
    val arduino_mac_address: String
)
class UserManager(){
    var usr: User? = null


    fun addUser(id: Int) {usr = User(id)}
    fun getUser(): User?  = usr

    fun getGarden(callback: (ArrayList<Garden>) -> Unit){
        ConnectToAPI().getGardensFromAPI { result ->
            callback(result)
        }
    }

    fun getArduino(callback: (ArrayList<Arduino>) -> Unit){
        ConnectToAPI().getArduinoFromAPI{ result ->
            callback(result)
        }
    }
}