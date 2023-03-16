package com.example.fitpeoassignment.ui.Fragments

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpeoassignment.MainActivity
import com.example.fitpeoassignment.R
import com.example.fitpeoassignment.databinding.FragmentListingBinding
import com.example.fitpeoassignment.ui.Adapters.AlbumListingRecyclerViewAdapter
import com.example.fitpeoassignment.ui.Adapters.ImageListingRecyclerViewAdapter
import com.example.fitpeoassignment.ui.ImagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListingFrag : Fragment() {

    private val viewModel by activityViewModels<ImagesViewModel>()

    private var _binding : FragmentListingBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
        val albumAdapter = AlbumListingRecyclerViewAdapter(
            filterAlbumAction ={
                Log.v("MyActivity","$it")
                viewModel.changeCurrentAlbum(it)
                binding.albumidTv.text = "Album ${viewModel.currentAlbum}"
                binding.searchEt.setText("")
                if (binding.searchEt.isFocused){
                    val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(binding.searchEt.windowToken, 0)
                    binding.searchEt.clearFocus()
                }
            }
        )
        val adapter = ImageListingRecyclerViewAdapter(
            toImageAction = { imageData, imageView, thumbnailIv,titleTv, position ->
                viewModel.currentPosition = position
                val extras = FragmentNavigatorExtras(
                    imageView to "ContentTransition$position",
                    thumbnailIv to "ThumbnailTransition$position",
                    titleTv to "TitleTransition$position"
                )
                val action = ListingFragDirections.actionListingFragToImageFrag("Transition$position", imageData = imageData)
                findNavController().navigate(action, extras)
            }
        )

        binding.apply {
            albumsRecview.adapter = albumAdapter
            albumsRecview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            listingRecview.adapter = adapter
            listingRecview.layoutManager = GridLayoutManager(requireContext(),2)
            albumidTv.text = "Album ${viewModel.currentAlbum}"
        }

        viewModel.imagesList.observe(viewLifecycleOwner){
            adapter.submitList(it.toList())
            if (it.size > 0){
                binding.imagesDuplicateShimmerLay.visibility = View.GONE
            }
        }
        viewModel.albumList.observe(viewLifecycleOwner){
            albumAdapter.submitList(it.toList())
            if (it.size > 0){
                binding.albumDuplicateShimmerLay.visibility = View.GONE
            }
        }
        binding.searchEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.searchHintLay.visibility = View.INVISIBLE
            }
            else{
                binding.searchHintLay.visibility = View.VISIBLE
            }
        }
        binding.searchEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (viewModel.searchFilter != null && binding.searchEt.text.isNullOrBlank() && binding.searchEt.isFocused){
//                    Log.v("MyActivity","hiiiii")
//                    val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
//                    inputMethodManager?.hideSoftInputFromWindow(binding.searchEt.windowToken, 0)
//                    binding.searchEt.clearFocus()
//                }
                if (binding.searchEt.text.isNullOrBlank()){
                    viewModel.searchFilter = null
                    viewModel.searchFilterChanged()
                }
                else{
                    viewModel.searchFilter = s.toString()
                    viewModel.searchFilterChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        setSharedElementTransitionCallback()
    }

    private fun scrollToPosition() {
        viewModel.currentPosition?.let {
            val layoutManager = binding.listingRecview.layoutManager!!
            val view = layoutManager.findViewByPosition(it)
            if (view == null || layoutManager.isViewPartiallyVisible(view, false, true)){
                binding.listingRecview.scrollToPosition(it)
            }
        }
    }

    fun setSharedElementTransitionCallback(){
        this.setExitSharedElementCallback(object : androidx.core.app.SharedElementCallback(){
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                super.onMapSharedElements(names, sharedElements)
                //scrollToPosition()
                val selectedViewHolder= binding.listingRecview.findViewHolderForAdapterPosition(viewModel.currentPosition!!)
                sharedElements?.set(names!![0],
                    selectedViewHolder!!.itemView.findViewById(R.id.content_iv)
                )
                sharedElements?.set(names!![1],
                    selectedViewHolder!!.itemView.findViewById(R.id.thumbnail_iv)
                )
                sharedElements?.set(names!![2],
                    selectedViewHolder!!.itemView.findViewById(R.id.title_tv)
                )
            }
        })
    }
}