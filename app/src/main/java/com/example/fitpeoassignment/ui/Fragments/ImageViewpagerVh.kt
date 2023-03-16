package com.example.fitpeoassignment.ui.Fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitpeoassignment.R
import com.example.fitpeoassignment.databinding.FragmentImageViewpagerVhBinding
import com.example.fitpeoassignment.ui.ImageData
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ImageViewpagerVh(imageData: ImageData, transitionPosition: Int) : Fragment() {

    val transitionPosition = transitionPosition
    val imageData = imageData

    var isExpanded = false

    private var _binding : FragmentImageViewpagerVhBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("MyActivity","vp_vh frag created")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentImageViewpagerVhBinding.inflate(inflater,container,false)
        binding.urlIv.transitionName = "ContentTransition$transitionPosition"
        binding.thumbnailIv.transitionName = "ThumbnailTransition$transitionPosition"
        binding.titleTv.transitionName = "TitleTransition$transitionPosition"
        binding.albumIdTv.text = "Image Id: ${imageData.id}"
        //Picasso.get().load(Uri.parse(imageData.thumbnailUrl)).into(binding.thumbnailIv)
        Picasso.get().load(Uri.parse(imageData.url)).into(binding.urlIv,object :Callback{
            override fun onSuccess() {
            }

            override fun onError(e: Exception?) {
                TODO("Not yet implemented")
            }

        })
        binding.titleTv.text = imageData.title
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleTv.maxLines = 1

        binding.moreTv.setOnClickListener {
            if (isExpanded){
                binding.moreTv.text = "More"
                isExpanded = !isExpanded
                binding.titleTv.maxLines = 1
            }
            else{
                binding.moreTv.text = "Less"
                isExpanded = !isExpanded
                binding.titleTv.maxLines = 8
            }
        }
    }
}