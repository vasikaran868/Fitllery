package com.example.fitpeoassignment.ui.Fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.doOnPreDraw
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.fitpeoassignment.databinding.FragmentImageBinding
import com.example.fitpeoassignment.ui.Adapters.ImagesViewPagerAdapter
import com.example.fitpeoassignment.ui.ImageData
import com.example.fitpeoassignment.ui.ImagesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ImageFrag : Fragment() {

    private var _binding : FragmentImageBinding? = null
    val binding get() = _binding!!

    private val viewModel by activityViewModels<ImagesViewModel>()

    val args: ImageFragArgs by navArgs()

    lateinit var viewPagerAdapter :ImagesViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        val list = java.util.ArrayList<ImageData>(viewModel.imagesList.value)
        viewPagerAdapter = ImagesViewPagerAdapter(list,requireActivity().supportFragmentManager,lifecycle)
        binding.imagesViewpager.adapter = viewPagerAdapter
        binding.imagesViewpager.setCurrentItem(viewModel.currentPosition!!, false)
        binding.imagesViewpager.registerOnPageChangeCallback(object : OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.currentPosition = position
            }

        })
        //binding.imagesViewpager.currentItem = viewModel.imagesList.value!!.toList().indexOf(args.imageData)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        requireView().doOnPreDraw {
            startPostponedEnterTransition()
        }
        setSharedElementTransitionCallback()
    }

    fun setSharedElementTransitionCallback(){
        this.setEnterSharedElementCallback(object :  androidx.core.app.SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                super.onMapSharedElements(names, sharedElements)

                val selectedFragment = viewPagerAdapter.fragmentsMap[binding.imagesViewpager.currentItem]

                sharedElements?.set(names!![0],
                    selectedFragment!!.requireView().findViewById(com.example.fitpeoassignment.R.id.url_iv)
                )
                sharedElements?.set(names!![1],
                    selectedFragment!!.requireView().findViewById(com.example.fitpeoassignment.R.id.thumbnail_iv)
                )
                sharedElements?.set(names!![2],
                    selectedFragment!!.requireView().findViewById(com.example.fitpeoassignment.R.id.title_tv)
                )
            }
        })
    }
}