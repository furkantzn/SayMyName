package com.example.saymyname

import androidx.lifecycle.ViewModel
import com.example.saymyname.model.AppDatabase
import com.example.saymyname.model.Word
import com.example.saymyname.model.WordDao

class ResultsViewModel:ViewModel() {
    private var wordDao : WordDao = DatabaseManager.getWordDao()

    public suspend fun getLearnedWords():List<Word>{
        return wordDao.getLearnedWords()
    }

    public suspend fun getLaterLearnWords():List<Word>{
        return wordDao.getLaterLearnWords()
    }
}