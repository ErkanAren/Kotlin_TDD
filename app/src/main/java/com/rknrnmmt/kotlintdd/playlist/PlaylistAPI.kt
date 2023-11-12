package com.rknrnmmt.kotlintdd.playlist

import retrofit2.http.GET

interface PlaylistAPI {

    @GET("playlists")
    suspend fun fetchPlaylists():List<Playlist>

}
