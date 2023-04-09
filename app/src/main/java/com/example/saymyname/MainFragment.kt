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
import com.example.saymyname.databinding.FragmentMainBinding
import kotlinx.coroutines.launch
import java.util.ResourceBundle

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

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
            if(binding.btnStart.text.equals("Start")){
                startChallenge()
            }else if (binding.btnStart.text.equals("Stop")){
                stopChallenge()
            }else{
                Log.e("MainFragment","Btn name could not find!")
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
    }

    private fun startChallenge(){
        handler = Handler(Looper.getMainLooper())

        runnable = object : Runnable {
            override fun run(){
                viewModel.viewModelScope.launch {
                        binding.tvWord.text = viewModel.getWord()
                }
                runAnimation()
                handler.postDelayed(this, 3000)
            }
        }

        handler.post(runnable)
        binding.btnStart.text = "Stop"
        binding.btnLearnLater.visibility = View.VISIBLE
        binding.btnLearned.visibility = View.VISIBLE
    }

    private fun stopChallenge(){
        handler.removeCallbacks(runnable)
        binding.btnStart.text = "Start"
        binding.btnLearnLater.visibility = View.INVISIBLE
        binding.btnLearned.visibility = View.INVISIBLE
        binding.tvWord.text = context?.getString(R.string.wordsText)
    }

    private fun runAnimation(){
        val anim = AnimationUtils.loadAnimation(this.context, R.anim.anim_textview)
        anim.duration = 3000
        binding.tvWord.clearAnimation()
        binding.tvWord.startAnimation(anim)
    }
}