package com.rknrnmmt.kotlintdd.details

import com.rknrnmmt.kotlintdd.R
import com.rknrnmmt.kotlintdd.models.PlaylistRaw
import com.rknrnmmt.kotlintdd.playlist.PlaylistMapper
import com.rknrnmmt.kotlintdd.utils.BaseUnitTest
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PlaylistDetailsMapperShould : BaseUnitTest() {

    private val mapper = PlaylistDetailsMapper()
    private val playlistsRaw = PlaylistRaw("1","name","","my category")
    private val playlistsRawRock = PlaylistRaw("1","name","rock","description1")

    private val playlist = mapper.invoke(playlistsRaw)

    private val playlistRock = mapper.invoke(playlistsRawRock)

    @Test
    fun keepSameId(){
        assertEquals(playlistsRaw.id, playlist.id)
    }

    @Test
    fun keepSameName(){
        assertEquals(playlistsRaw.name, playlist.name)
    }

    @Test
    fun keepSameDescription(){
        assertEquals(playlistsRaw.description, playlist.description)
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