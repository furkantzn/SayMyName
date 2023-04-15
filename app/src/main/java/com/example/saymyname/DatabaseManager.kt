package com.example.saymyname

import android.content.Context
import androidx.room.Room
import com.example.saymyname.model.AppDatabase
import com.example.saymyname.model.WordDao

object DatabaseManager {
    private lateinit var database: AppDatabase
    private lateinit var wordDao : WordDao
    fun createDB(context: Context){
        database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,"saymyname-database"
        ).build()

        wordDao = database.wordDao()
    }

    fun getWordDao():WordDao{
        return wordDao
    }
}