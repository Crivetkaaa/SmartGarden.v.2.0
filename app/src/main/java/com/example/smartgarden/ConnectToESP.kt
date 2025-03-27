package com.example.smartgarden

import android.util.Log
import androidx.core.location.GnssStatusCompat.Callback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import khttp.get
import khttp.post
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ConnectToESP {
    val ip = "http://192.168.4.1/"

    suspend fun getESP(url: String, callback: (String) -> Unit){
        try {
            val response = withContext(Dispatchers.IO) {
                get("${ip}/${url}/")
            }
            if (response.statusCode == 200){
                callback(response.text)
            }
        } catch (e: Exception){}
    }

    suspend fun postToESP(url: String, params: Map<String, Any>, callback: (String) -> Unit){
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
            callback(e.toString())
        }
    }

    fun postArduino(params: Map<String, Any>, callback: (Boolean) -> Unit){
        CoroutineScope(Dispatchers.Main).launch {
            postToESP("newarduino/", params){ result->
                try {
                    Log.d("MyTag", "ESP result: $result")
                    val resbody = JSONObject(result)
                    val status = resbody.getBoolean("status")
                    callback(status)
                } catch (e: Exception){
                    callback(false)
                }
            }
        }
    }

}