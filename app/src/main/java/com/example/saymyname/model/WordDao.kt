package com.example.saymyname.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    suspend fun getAll(): List<Word>

    @Query("SELECT * FROM word WHERE learn_status = true")
    suspend fun getLearnedWords() : List<Word>

    @Query("SELECT * FROM word WHERE learn_status = false")
    suspend fun getLaterLearnWords() : List<Word>

    @Update
    suspend fun updateWord(word: Word)

    @Insert
    suspend fun addWord(word: Word)
}