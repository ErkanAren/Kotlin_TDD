package com.rknrnmmt.kotlintdd.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.rknrnmmt.kotlintdd.R


class PlaylistFragment : Fragment() {

    lateinit var viewModel: PlaylistViewModel
    lateinit var viewModelFactory: PlaylistViewModelFactory
    private val service = PlaylistService(object:PlaylistAPI{
        override suspend fun fetchPlaylists() {
            TODO("Not yet implemented")
        }

    })
    private val repository = PlaylistRepository(service)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.playlist_item_list, container, false)

        setupViewModel()

        viewModel.playlists.observe(this as LifecycleOwner) { result ->
            if (result.getOrNull() != null)
                setupList(view, result.getOrNull()!!)
            else {
                //TODO
            }
        }

        return view
    }

    private fun setupList(
        view: View?,
        playlists: List<Playlist>
    ) {
        with(view as RecyclerView) {
            layoutManager = LinearLayoutManager(context)

            adapter = MyPlaylistRecyclerViewAdapter(playlists)
        }
    }

    private fun setupViewModel() {
        viewModelFactory = PlaylistViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlaylistViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlaylistFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}