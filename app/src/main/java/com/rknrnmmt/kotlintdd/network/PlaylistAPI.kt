package com.rknrnmmt.kotlintdd.network

import com.rknrnmmt.kotlintdd.models.PlaylistRaw
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaylistAPI {

    @GET("playlists")
    suspend fun fetchPlaylists():List<PlaylistRaw>

    @GET("playlists/{id}")
    suspend fun fetchPlaylistDetailsById(@Path("id") id:String): PlaylistRaw
}