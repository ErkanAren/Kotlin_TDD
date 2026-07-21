package com.rknrnmmt.kotlintdd.playlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.rknrnmmt.kotlintdd.R
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    lateinit var viewModel: PlaylistViewModel

    @Inject
    lateinit var viewModelFactory: PlaylistViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.playlist_item_list, container, false)
        val loader = view.findViewById<ProgressBar>(R.id.loader)
        val list = view?.findViewById<RecyclerView>(R.id.playlists_list)

        setupViewModel()

        viewModel.loader.observe(viewLifecycleOwner) { loading ->
           when (loading) {
               true -> loader.visibility = View.VISIBLE
               else -> { loader.visibility = View.GONE }
           }
        }
        viewModel.playlists.observe(viewLifecycleOwner) { result ->
            if (result.getOrNull() != null) {
                Log.i("mytag", "result is ok")
                setupList(list, result.getOrNull()!!)
            } else {
                Log.i("mytag", "something is wrong with the result")
            }
        }

        return view
    }

    private fun setupList(
        list: RecyclerView?,
        playlists: List<Playlist>
    ) {
        Log.i("mytag","playlist size: "+playlists.size)

        list?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = MyPlaylistRecyclerViewAdapter(playlists)
        }
    }

    private fun setupViewModel() {
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