package com.example.saymyname.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word (
    @PrimaryKey val id : String,
    @ColumnInfo(name = "name") val name:String?,
    @ColumnInfo(name = "learn_status") val learnStatus:Boolean?
        )