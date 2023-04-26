package com.example.saymyname

import com.example.saymyname.model.Word

interface CustomViewHolderListener {
    fun onWordItemClicked(word : Word,position:Int)
}