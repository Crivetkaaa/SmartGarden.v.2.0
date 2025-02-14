package com.example.smartgarden

import android.util.Log
import khttp.get
import khttp.post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
import org.json.JSONObject


class ConnectToAPI {
    val ip = "http://188.32.24.142:12200/"

    suspend fun getFromAPI(url: String, callback: (String) ->Unit){
        try {
            val response = withContext(Dispatchers.IO) {
                get("${ip}${url}") // Выполняем HTTP-запрос
            }
            if (response.statusCode == 200) {
                // Печать ответа в виде строки
                callback(response.text)
            } else {
                callback("${response.statusCode} - error number")
            }
        } catch (e:Exception){
            callback("No internet connection")
        }
    }

    suspend fun postToAPI(url: String, params: Map<String, Any>, callback: (String) -> Unit){
        try{
            val response = withContext(Dispatchers.IO){
                Log.d("MyTag", "$ip$url")
                post("$ip$url",
                    headers = mapOf("Content-Type" to "application/json"),
                    json = JSONObject(params))
            }
            if (response.statusCode in 200..299){

                callback(response.text)
            } else {
                callback("Error code - ${response.statusCode}")
            }
        } catch (e: Exception){
            callback("No internet connection")
        }
    }

    fun authorization(login: String, password: String, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {

            val url = "api/getuser/?email=${login}&password=${password}"
            getFromAPI(url) { result ->
                try {
                    val resbody = JSONObject(result).getJSONObject("post")
                    val id = resbody.getInt("id")
                    usrManager.addUser(id)
                    callback(true, "")

                } catch (e:Exception) {
                    callback(false, result)
                }
            }
        }
    }

    fun registration(login: String, password: String, callback: (Boolean) -> Unit){
        val url = "api/getuser/"
        val params = mapOf(
            "email" to login,
            "password" to password
        )
        CoroutineScope(Dispatchers.Main).launch {
            postToAPI(url, params) { result ->
                try {
                    Log.d("MyTag", "$result")
                    val status = JSONObject(result).getBoolean("post")
                    callback(status)
                } catch (e: Exception){
                    callback(false)
                }
            }
        }
    }


    fun getGardensFromAPI(callback: (ArrayList<Garden>) -> Unit){
        CoroutineScope(Dispatchers.Main).launch {
            val url = "api/garden/?user_id=${usrManager.usr!!.id}"
            getFromAPI(url){result ->
                try {
                    var usergardens: ArrayList<Garden> = ArrayList()
                    val resbody = JSONObject(result)
                    for (key in resbody.keys()) {
                        val garden_name = resbody.getJSONObject(key).getString("garden_name")
                        val garden_description = resbody.getJSONObject(key).getString("garden_description")


                        usergardens.add(Garden(garden_id = key.toInt(), garden_name = garden_name,
                            garden_description = garden_description))
                    callback(usergardens)
                    }
                } catch (e:Exception){

                }
            }
        }
    }

    fun getArduinoFromAPI(all_arduino: Boolean = false, callback: (ArrayList<Arduino>) -> Unit){
        CoroutineScope(Dispatchers.Main).launch {
            val url = "api/arduino/?user_id=${usrManager.usr!!.id}&free_arduino=$all_arduino"
            getFromAPI(url) { result ->
                try {
                    var userarduino: ArrayList<Arduino> = ArrayList()
                    val resbody = JSONObject(result)
                    for (key in resbody.keys()){
                        val arduino_id = key.toInt()
                        val arduino_name = resbody.getJSONObject(key).getString("arduino_name")
                        val mac_address = resbody.getJSONObject(key).getString("mac_address")
                        val arduino_description = resbody.getJSONObject(key).getString("arduino_description")
                        userarduino.add(Arduino(arduino_id, arduino_name, arduino_description, mac_address))

                    }
                    callback(userarduino)
                    Log.d("MyTag", "$result")
                } catch (e:Exception){

                }
            }

        }
    }
}