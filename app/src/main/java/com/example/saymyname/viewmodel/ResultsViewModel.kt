package com.example.saymyname.viewmodel

import androidx.lifecycle.ViewModel
import com.example.saymyname.model.DatabaseManager
import com.example.saymyname.model.Word
import com.example.saymyname.model.WordDao

class ResultsViewModel:ViewModel() {
    private var wordDao : WordDao = DatabaseManager.getWordDao()

    suspend fun getLearnedWords():MutableList<Word>{
        return wordDao.getLearnedWords()
    }

    suspend fun getLaterLearnWords():MutableList<Word>{
        return wordDao.getLaterLearnWords()
    }

    suspend fun updateWord(word: Word){
        wordDao.updateWord(word)
    }
    suspend fun deleteWord(word: Word){
        wordDao.deleteWord(word)
    }
}