package com.example.fitpeoassignment.ui.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpeoassignment.databinding.AlbumViewholderBinding
import com.example.fitpeoassignment.ui.AlbumVhData
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class AlbumListingRecyclerViewAdapter( val filterAlbumAction:(Int)->Unit):
    ListAdapter<AlbumVhData, AlbumListingRecyclerViewAdapter.AlbumViewHolder>(Diffcallback){

    class AlbumViewHolder(private var binding: AlbumViewholderBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(albumData:AlbumVhData){
            binding.apply {
                albumIdTv.text = if (albumData.albumId < 10) "0${albumData.albumId}" else albumData.albumId.toString()
                Picasso.get().load(Uri.parse(albumData.albumUrl)).into(binding.thumbnailIv,object: Callback{
                    override fun onSuccess() {
                        binding.shimmerLay.stopShimmer()
                        binding.shimmerLay.hideShimmer()
                    }

                    override fun onError(e: Exception?) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumListingRecyclerViewAdapter.AlbumViewHolder {
        return AlbumListingRecyclerViewAdapter.AlbumViewHolder(
            AlbumViewholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlbumListingRecyclerViewAdapter.AlbumViewHolder, position: Int) {
        var albumData = getItem(position)
        holder.bind(albumData)
        holder.itemView.setOnClickListener {
            filterAlbumAction(albumData.albumId)
        }

    }

    companion object{
        private val Diffcallback = object: DiffUtil.ItemCallback<AlbumVhData>(){
            override fun areItemsTheSame(oldItem: AlbumVhData, newItem: AlbumVhData): Boolean {
                return oldItem.albumId == newItem.albumId
            }

            override fun areContentsTheSame(oldItem: AlbumVhData, newItem: AlbumVhData): Boolean {
                return oldItem == newItem
            }
        }
    }


}