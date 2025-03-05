package com.example.smartgarden

import java.sql.Date
import java.sql.Time

val usrManager = UserManager()

data class User(
    val id: Int
)

data class ArduinoData(
    val date: Date,
    val time: Time,
    val temperature: Int,
    val humidity: Int,
    val earth_humidity: Int

)

data class GardenArduino(
    val garden: Garden? = null,
    val arduino: Arduino? = null
)

data class Garden(
    val garden_id: Int,
    var garden_name: String,
    var garden_description: String
)

data class Arduino(
    val arduino_id: Int,
    val arduino_name: String,
    val arduino_description: String,
    val arduino_mac_address: String
)
class UserManager(){
    var usr: User? = null
    var garden_clickable: Int? = null
    var arduino_to_change: Int? = null
    var gardenarduino: GardenArduino? = null

    fun addUser(id: Int) {usr = User(id)}
    
    fun getGarden(callback: (ArrayList<Garden>) -> Unit){
        ConnectToAPI().getGardensFromAPI { result ->
            callback(result)
        }
    }

    fun getArduino(free_arduino:(Boolean) = false, callback: (ArrayList<Arduino>) -> Unit){
        ConnectToAPI().getArduinoFromAPI(free_arduino){ result ->
            callback(result)
        }
    }
}