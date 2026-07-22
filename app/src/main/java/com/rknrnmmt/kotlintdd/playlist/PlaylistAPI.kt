package com.rknrnmmt.kotlintdd.playlist

import com.rknrnmmt.kotlintdd.playlist.models.PlaylistRaw
import retrofit2.http.GET

interface PlaylistAPI {

    @GET("playlists")
    suspend fun fetchPlaylists():List<PlaylistRaw>

}
