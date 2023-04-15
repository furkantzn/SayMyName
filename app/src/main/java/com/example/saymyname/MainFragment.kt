package com.example.saymyname

import android.content.res.Resources
import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.motion.utils.ViewState
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.saymyname.databinding.FragmentMainBinding
import kotlinx.coroutines.launch
import java.util.ResourceBundle

class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private var isChallengeStart = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        this.context?.let {
            viewModel.loadJson(it)
            viewModel.viewModelScope.launch { viewModel.createDB(it.applicationContext) }
        }

        binding.btnStart.setOnClickListener {
            if(!isChallengeStart){
                startChallenge()
            }else{
                stopChallenge()
            }
        }

        binding.btnLearned.setOnClickListener {
            val word = binding.tvWord.text.toString()
            if(word.isNotEmpty()){
                viewModel.saveLearnedWord(word)
            }
        }

        binding.btnLearnLater.setOnClickListener {
            val word = binding.tvWord.text.toString()
            if(word.isNotEmpty()){
                viewModel.saveLearnLaterWord(word)
            }
        }

        binding.btnShowResults.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToResultsFragment()
            view.findNavController().navigate(action)
        }
    }

    private fun startChallenge(){
        isChallengeStart = true
        handler = Handler(Looper.getMainLooper())

        runnable = object : Runnable {
            override fun run(){
                viewModel.viewModelScope.launch {
                        binding.tvWord.text = viewModel.getWord()
                }
                runAnimation()
                handler.postDelayed(this, 4000)
            }
        }

        handler.post(runnable)
        binding.btnStart.text = getString(R.string.stop)
        binding.btnLearnLater.visibility = View.VISIBLE
        binding.btnLearned.visibility = View.VISIBLE
        binding.btnShowResults.visibility = View.INVISIBLE
    }

    private fun stopChallenge(){
        isChallengeStart=false
        handler.removeCallbacks(runnable)
        binding.btnStart.text = getString(R.string.start)
        binding.btnLearnLater.visibility = View.INVISIBLE
        binding.btnLearned.visibility = View.INVISIBLE
        binding.btnShowResults.visibility = View.VISIBLE
        binding.tvWord.text = context?.getString(R.string.wordsText)
    }

    private fun runAnimation(){
        val anim = AnimationUtils.loadAnimation(this.context, R.anim.anim_textview)
        anim.duration = 2000
        binding.tvWord.clearAnimation()
        binding.tvWord.startAnimation(anim)
    }
}