package com.example.fitpeoassignment.ui.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpeoassignment.databinding.ImageViewholderBinding
import com.example.fitpeoassignment.ui.ImageData
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ImageListingRecyclerViewAdapter(val toImageAction:(ImageData, ImageView, ImageView,TextView, Int)->Unit):ListAdapter<ImageData,ImageListingRecyclerViewAdapter.ImageViewHolder> (Diffcallback){

    class ImageViewHolder(private var binding: ImageViewholderBinding): RecyclerView.ViewHolder(binding.root){

        val thumbnailIv = binding.thumbnailIv
        val titleTv = binding.titleTv
        val contentIv = binding.contentIv

        fun bind(imageData: ImageData, context: Context, position: Int){
            binding.apply {
                binding.shimmerLay.startShimmer()
                binding.shimmerLay.visibility = View.VISIBLE
                Picasso.get().load(Uri.parse(imageData.thumbnailUrl)).into(binding.thumbnailIv,object :Callback{
                    override fun onSuccess() {
                        binding.shimmerLay.stopShimmer()
                        binding.shimmerLay.hideShimmer()
                    }

                    override fun onError(e: Exception?) {
                    }
                })
                Picasso.get().load(Uri.parse(imageData.url)).into(binding.contentIv)
                titleTv.text = imageData.title
                contentIv.transitionName = "ContentTransition$position"
                thumbnailIv.transitionName = "ThumbnailTransition$position"
                titleTv.transitionName = "TitleTransition$position"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ImageViewholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        var imageData = getItem(position)
        holder.bind(imageData, holder.itemView.context, position)
        holder.itemView.setOnClickListener {
            toImageAction(imageData, holder.contentIv, holder.thumbnailIv,holder.titleTv, position)
        }
    }

    companion object{
        private val Diffcallback = object: DiffUtil.ItemCallback<ImageData>(){
            override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
                return oldItem == newItem
            }
        }
    }


}