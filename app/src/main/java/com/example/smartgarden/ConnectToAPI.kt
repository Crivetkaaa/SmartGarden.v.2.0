package com.example.smartgarden

import android.util.Log
import khttp.get
import khttp.post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
import org.json.JSONObject
import java.text.SimpleDateFormat


class ConnectToAPI {
    val ip = "http://192.168.0.24:8000/"

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
                } catch (e:Exception){

                }
            }

        }
    }

    fun addArduinGardenAPI(params: Map<String, Any>, callback: (Boolean, String) -> Unit){
        val url = "api/arduinoingarden/"
        CoroutineScope(Dispatchers.Main).launch {
            postToAPI(url, params){ result ->
                try {
                    callback(true, "Теплица была добавлена")
                } catch (e: Exception){
                    callback(false, result)
                }
            }
        }
    }

    fun getGardenFromAPI(callback: (Boolean, GardenArduino?) -> Unit){
        val url = "api/mygarden/?user_id=${usrManager.usr!!.id}&garden_id=${usrManager.garden_clickable}"
        CoroutineScope(Dispatchers.Main).launch {
            getFromAPI(url){ result ->
                try {
                    Log.d("MyTag", "$result")
                    val resbody = JSONObject(result)
                    val status = resbody.getBoolean("arduino_check")

                    var arduino: Arduino? = null
                    if (status) {
                        arduino = Arduino(
                            resbody.getInt("arduino_id"),
                            resbody.getString("arduino_name"),
                            resbody.getString("arduino_description"),
                            resbody.getString("mac_address"))
                        usrManager.arduino_to_change = resbody.getInt("arduino_id")
                    }
                    val garden = Garden(
                        resbody.getInt("garden_id"),
                        resbody.getString("garden_name"),
                        resbody.getString("garden_description")
                    )
                    Log.d("MyTag", "${GardenArduino(garden, arduino)}")
                    usrManager.gardenarduino = GardenArduino(garden, arduino)
                    callback(true, GardenArduino(garden, arduino))
                    } catch (e: Exception){
                        Log.d("MyTag", "vssdv")
                    callback(false, null)
                }
            }
        }
    }
    fun postUpdateGarden(params: Map<String, Any>, callback: (Boolean) -> Unit){
        CoroutineScope(Dispatchers.Main).launch {
            postToAPI("api/edit/", params){ result ->
                try {
                    val resbody = JSONObject(result)
                    val status = resbody.getBoolean("status")
                    Log.d("MyTag", "$result")
                    callback(status)
                } catch (e: Exception){
                    callback(false)
                }

            }
        }
    }

    fun postDeleteGarden(params: Map<String, Any>, callback: (Boolean) -> Unit){
        CoroutineScope(Dispatchers.Main).launch {
            postToAPI("api/delete/", params){ result ->
                Log.d("MyTag", result)
                try {
                    val resbody = JSONObject(result)
                    val status = resbody.getBoolean("status")
                    callback(status)
                } catch (e: Exception) {
                    callback(false)
                }
            }
        }
    }

    fun getArduinoData(arduino_id: Int, callback: (ArrayList<ArduinoData>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            getFromAPI("api/data/?arduino_id=${arduino_id}",){ result ->
                Log.d("MyTag", arduino_id.toString())

                Log.d("MyTag", result.toString())
                try {
                    val dateInputFormat = SimpleDateFormat("yyyy-MM-dd")
                    val dateOutputFormat = SimpleDateFormat("dd.MM.yyyy")

                    val resbody = JSONObject(result)
                    val array: ArrayList<ArduinoData> = ArrayList()

                    for (key in resbody.keys()) {
                        val datajson = resbody.getJSONObject(key)
                        val date = dateOutputFormat.format(dateInputFormat.parse(datajson.getString("date")))
                        val air_t = datajson.getDouble("air_t")
                        val air_h = datajson.getDouble("air_h")
                        val earth_h = datajson.getDouble("earth_h")
                        Log.d("MyTag", "${air_t}  $air_h")
                        array.add(ArduinoData(date, air_t, air_h, earth_h))
                    }
                    callback(array)
                } catch (e: Exception) {
                    Log.e("MyTag", "Ошибка при обработке данных: ${e.message}")
                }

            }
        }
    }
    fun getVegetables(callback: (ArrayList<Vegetables>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            getFromAPI("api/vegetable/") { result ->
                try {
                    Log.d("MyTag", result)
                    var array: ArrayList<Vegetables> = ArrayList()
                    val resbody = JSONObject(result)
                    for (key in resbody.keys()) {
                        array.add(Vegetables(resbody.getString(key)))
                    }
                    callback(array)
                } catch (e: Exception){

                }

            }
        }
    }
}