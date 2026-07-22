package com.rknrnmmt.kotlintdd.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.rknrnmmt.kotlintdd.R

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistDetailsFragment : Fragment() {

    lateinit var viewModel: PlaylistDetailsViewModel

    @Inject
    lateinit var viewModelFactory: PlaylistDetailsViewModelFactory

    val args: PlaylistDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist_detail, container, false)
        val title = view.findViewById<TextView>(R.id.playlist_title)
        val description = view.findViewById<TextView>(R.id.playlist_description)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PlaylistDetailsViewModel::class.java)

        viewModel.getPlaylistDetailsById(args.playlistId)
            .observe(viewLifecycleOwner) { result ->
                val playlist = result.getOrNull()
                title.setText(playlist?.name)
                description.setText(playlist?.description)
            }

        // Inflate the layout for this fragment
        return view
    }
}