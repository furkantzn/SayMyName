package com.example.saymyname.model

import androidx.room.*

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    suspend fun getAll(): MutableList<Word>

    @Query("SELECT * FROM word WHERE learn_status = true")
    suspend fun getLearnedWords() : MutableList<Word>

    @Query("SELECT * FROM word WHERE learn_status = false")
    suspend fun getLaterLearnWords() : MutableList<Word>

    @Update
    suspend fun updateWord(word: Word)

    @Insert
    suspend fun addWord(word: Word)

    @Delete
    suspend fun deleteWord(word: Word)
}