package com.rknrnmmt.kotlintdd.details

import com.rknrnmmt.kotlintdd.R
import com.rknrnmmt.kotlintdd.models.Playlist
import com.rknrnmmt.kotlintdd.models.PlaylistRaw
import javax.inject.Inject

class PlaylistDetailsMapper @Inject constructor():Function1<PlaylistRaw, Playlist> {
    override fun invoke(playlistsRaw: PlaylistRaw): Playlist {
        return Playlist(playlistsRaw.id,playlistsRaw.name,playlistsRaw.category,playlistsRaw.description,if (playlistsRaw.category.equals("rock")) R.mipmap.rock else R.mipmap.playlist)
    }
}