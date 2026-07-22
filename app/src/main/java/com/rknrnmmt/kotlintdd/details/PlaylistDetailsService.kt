package com.rknrnmmt.kotlintdd.details

import com.rknrnmmt.kotlintdd.models.PlaylistRaw
import com.rknrnmmt.kotlintdd.network.PlaylistAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.RuntimeException
import javax.inject.Inject

class PlaylistDetailsService @Inject constructor(
    private val playlistAPI: PlaylistAPI
){
    suspend fun fetchPlaylistDetailsById(id:String): Flow<Result<PlaylistRaw>> {
        return flow {
            emit(Result.success(playlistAPI.fetchPlaylistDetailsById(id)))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

}