package com.rknrnmmt.kotlintdd.playlist

import com.rknrnmmt.kotlintdd.R

data class Playlist (val id: String,
                     val name: String,
                     val category:String,
                     val image:Int = R.mipmap.playlist
)