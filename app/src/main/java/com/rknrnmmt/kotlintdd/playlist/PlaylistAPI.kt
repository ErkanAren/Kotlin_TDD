package com.rknrnmmt.kotlintdd.playlist

interface PlaylistAPI {
    suspend fun fetchPlaylists():List<Playlist>

}
