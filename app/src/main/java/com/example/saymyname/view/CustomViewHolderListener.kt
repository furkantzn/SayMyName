package com.example.saymyname.view

import com.example.saymyname.model.Word

interface CustomViewHolderListener {
    fun onWordItemClicked(word : Word,position:Int)
}