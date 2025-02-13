package com.example.smartgarden

import android.util.Log
import androidx.core.location.GnssStatusCompat.Callback

val usrManager = UserManager()

data class User(
    val id: Int
)

data class Garden(
    val garden_id: Int,
    val garden_name: String
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
}