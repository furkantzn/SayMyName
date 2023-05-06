package com.example.saymyname.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.saymyname.R
import com.example.saymyname.viewmodel.ResultsViewModel
import com.example.saymyname.adapter.WordAdapter
import com.example.saymyname.databinding.FragmentResultsBinding
import com.example.saymyname.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ResultsFragment : Fragment() {
    private lateinit var viewModel: ResultsViewModel
    private lateinit var binding: FragmentResultsBinding
    private var learnedWords: MutableList<Word> = mutableListOf()
    private var laterLearnedWords: MutableList<Word> = mutableListOf()
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
        setViewModel()
        setWordLists()
        setViewElements()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[ResultsViewModel::class.java]
    }

    private fun setWordLists() {
        runBlocking {
            launch(Dispatchers.IO) {
                laterLearnedWords = viewModel.getLaterLearnWords()
                learnedWords = viewModel.getLearnedWords()
            }
        }
    }

    private fun setViewElements() {
        learnedWordAdapter = WordAdapter(learnedWords, LearnedWordsClickListener(context, this))
        laterLearnAdapter = WordAdapter(
            laterLearnedWords,
            LaterLearnWordsClickListener(context, this)
        )

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

    private fun showDeleteWordDialog(
        title: String,
        word: Word,
        context: Context?,
        position: Int,
        adapterType: AdapterType
    ) {
        if (context != null) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.custom_dialog)
            val body = dialog.findViewById(R.id.tvBodyText) as TextView
            body.text = title
            val btnYes = dialog.findViewById(R.id.btnYes) as Button
            val btnNo = dialog.findViewById(R.id.btnNo) as Button
            btnNo.setOnClickListener {
                dialog.dismiss()
            }
            btnYes.setOnClickListener {
                viewModel.viewModelScope.launch {
                    if (word.learnStatus == false) {
                        viewModel.updateWord(Word(word.id, word.name, true))
                    } else {
                        viewModel.deleteWord(word)
                    }
                }
                if (adapterType == AdapterType.LearnedAdapter) {
                    learnedWordAdapter.removeWordFromList(position)
                    binding.tvLearned.text = buildString {
                        append("Learned(")
                        append(learnedWordAdapter.itemCount.toString())
                        append(")")
                    }
                } else if (adapterType == AdapterType.LaterLearnAdapter) {
                    laterLearnAdapter.removeWordFromList(position)
                    binding.tvLearnLater.text = buildString {
                        append("Learn Later(")
                        append(laterLearnAdapter.itemCount.toString())
                        append(")")
                    }
                }
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    class LearnedWordsClickListener(
        private var context: Context?,
        private var fragment: ResultsFragment
    ) : CustomViewHolderListener {
        override fun onWordItemClicked(word: Word, position: Int) {
            fragment.showDeleteWordDialog(
                "Do you want to delete ${word.name} from Learned list?",
                word,
                context,
                position,
                AdapterType.LearnedAdapter
            )
        }
    }

    class LaterLearnWordsClickListener(
        private var context: Context?,
        private var fragment: ResultsFragment
    ) : CustomViewHolderListener {
        override fun onWordItemClicked(word: Word, position: Int) {
            fragment.showDeleteWordDialog(
                "Did you learn ${word.name} ?",
                word,
                context,
                position,
                AdapterType.LaterLearnAdapter
            )
        }
    }

    enum class AdapterType {
        LearnedAdapter,
        LaterLearnAdapter
    }
}