package com.rknrnmmt.kotlintdd.playlist

import com.rknrnmmt.kotlintdd.R
import com.rknrnmmt.kotlintdd.models.PlaylistRaw
import com.rknrnmmt.kotlintdd.utils.BaseUnitTest
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PlaylistMapperShould : BaseUnitTest() {

    private val mapper = PlaylistMapper()
    private val playlistsRaw = PlaylistRaw("1","name","","my category")
    private val playlistsRawRock = PlaylistRaw("1","name","rock","")

    private val playlist = mapper.invoke(listOf(playlistsRaw)).first()

    private val playlistRock = mapper.invoke(listOf(playlistsRawRock)).first()

    @Test
    fun keepSameId(){
        assertEquals(playlistsRaw.id, playlist.id)
    }

    @Test
    fun keepSameName(){
        assertEquals(playlistsRaw.name, playlist.name)
    }

    @Test
    fun keepSameCategory(){
        assertEquals(playlistsRaw.category, playlist.category)
    }

    @Test
    fun mapDefaultImageWhenNotRockCategory(){
        assertEquals(R.mipmap.playlist, playlist.image)
    }

    @Test
    fun mapRockImageWhenRockCategory(){
        assertEquals(R.mipmap.rock, playlistRock.image)
    }
}