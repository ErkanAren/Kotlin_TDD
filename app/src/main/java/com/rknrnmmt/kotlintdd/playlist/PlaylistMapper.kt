package com.rknrnmmt.kotlintdd.playlist

import com.rknrnmmt.kotlintdd.R
import com.rknrnmmt.kotlintdd.playlist.models.Playlist
import com.rknrnmmt.kotlintdd.playlist.models.PlaylistRaw
import javax.inject.Inject

class PlaylistMapper @Inject constructor():Function1<List<PlaylistRaw>, List<Playlist>> {
    override fun invoke(playlistsRaw: List<PlaylistRaw>): List<Playlist> {
        return playlistsRaw.map { Playlist(it.id,it.name,it.category,if (it.category.equals("rock")) R.mipmap.rock else R.mipmap.playlist) }
    }
}