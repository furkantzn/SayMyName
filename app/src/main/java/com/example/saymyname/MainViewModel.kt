package com.example.saymyname

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException

class MainViewModel : ViewModel() {
    private lateinit var words :List<String>


    fun getWord() :String{
        val randomNumber = (0..words.size).random()
        return words[randomNumber]
    }

    fun loadJson(context: Context) {
        val gson = GsonBuilder().create()
        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("englishcommonwords.json")
                .bufferedReader()
                .use { it.readText() }

            words = gson.fromJson<ArrayList<String>>(jsonString,
                object : TypeToken<ArrayList<String>>() {}.type)
        } catch (exception: java.lang.Exception) {
            Log.e("MainViewModel", exception.message.toString())
        }
    }
}