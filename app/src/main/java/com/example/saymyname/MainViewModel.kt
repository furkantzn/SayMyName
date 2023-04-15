package com.example.saymyname

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.saymyname.model.AppDatabase
import com.example.saymyname.model.Word
import com.example.saymyname.model.WordDao
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

class MainViewModel : ViewModel() {
    private lateinit var words :List<String>
    private lateinit var database: AppDatabase
    private lateinit var wordDao : WordDao

    suspend fun getWord() :String{
        val randomNumber = (0..words.size).random()
        if(getLearnedWords().contains(words[randomNumber])){
            getWord()
        }
        return words[randomNumber]
    }

    private suspend fun getLearnedWords():List<String>{
        var learnedWords = mutableListOf<String>()
        for (word in wordDao.getLearnedWords()){
            val isSuccess = word.name?.let { learnedWords.add(it) }
            if(isSuccess == false){
                word.name.let { learnedWords.add(it) }
            }
        }
        return learnedWords
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

    fun saveLearnedWord(wordName : String) = viewModelScope.launch{
        val wordObj = Word(UUID.randomUUID().toString(),wordName,true)
        wordDao.addWord(wordObj)
    }

    fun saveLearnLaterWord(wordName : String) = viewModelScope.launch{
        val wordObj = Word(UUID.randomUUID().toString(),wordName,false)
        wordDao.addWord(wordObj)
    }

    fun createDB(context: Context){
        DatabaseManager.createDB(context)
        wordDao = DatabaseManager.getWordDao()
    }
}