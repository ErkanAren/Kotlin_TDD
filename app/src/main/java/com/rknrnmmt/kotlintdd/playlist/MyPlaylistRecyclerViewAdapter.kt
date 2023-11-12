package com.rknrnmmt.kotlintdd.playlist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.rknrnmmt.kotlintdd.R


class MyPlaylistRecyclerViewAdapter(private val playlists:List<Playlist>) : RecyclerView.Adapter<MyPlaylistRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent,false)

        return ViewHolder(view)
        /*return ViewHolder(
            PlaylistItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )*/

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = playlists[position]
        holder.playlistName.text = item.name
        holder.playlistCategory.text = item.category
        holder.playlistImage.setImageResource(R.mipmap.playlist)
    }

    override fun getItemCount(): Int = playlists.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playlistName: TextView = view.findViewById(R.id.playlist_name)
        val playlistCategory: TextView = view.findViewById(R.id.playlist_category)
        val playlistImage: ImageView = view.findViewById(R.id.playlist_image)

        override fun toString(): String {
            return super.toString() //+ " '" + contentView.text + "'"
        }
    }

}