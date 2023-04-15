package com.example.saymyname

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.saymyname.adapter.WordAdapter
import com.example.saymyname.databinding.FragmentMainBinding
import com.example.saymyname.databinding.FragmentResultsBinding
import com.example.saymyname.model.Word
import kotlinx.coroutines.launch

class ResultsFragment : Fragment() {
    private lateinit var viewModel: ResultsViewModel
    private lateinit var binding: FragmentResultsBinding
    private var learnedWords: List<Word> = emptyList()
    private var laterLearnedWords: List<Word> = emptyList()
    private lateinit var learnedWordAdapter: WordAdapter
    private lateinit var laterLearnAdapter: WordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ResultsViewModel::class.java]
        viewModel.viewModelScope.launch {
            laterLearnedWords = viewModel.getLaterLearnWords()
            learnedWords = viewModel.getLearnedWords()
            learnedWordAdapter = WordAdapter(learnedWords)
            laterLearnAdapter = WordAdapter(laterLearnedWords)
            binding.recyclerLearnLater.layoutManager = GridLayoutManager(context, 2)
            binding.recyclerLearned.layoutManager = GridLayoutManager(context, 2)
            binding.recyclerLearnLater.adapter = laterLearnAdapter
            binding.recyclerLearned.adapter = learnedWordAdapter
            val learnWordsSize = learnedWords.size.toString()
            val laterLearnWordsSize = laterLearnedWords.size.toString()
            binding.tvLearned.text = buildString {
                append("Learned(")
                append(learnWordsSize)
                append(")")
            }
            binding.tvLearnLater.text = buildString {
                append("Learn Later(")
                append(laterLearnWordsSize)
                append(")")
            }
        }
    }
}