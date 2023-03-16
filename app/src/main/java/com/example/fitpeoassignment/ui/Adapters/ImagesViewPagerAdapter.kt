package com.example.fitpeoassignment.ui.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fitpeoassignment.ui.Fragments.ImageViewpagerVh
import com.example.fitpeoassignment.ui.ImageData

class ImagesViewPagerAdapter (list:ArrayList<ImageData>, fm: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fm,lifecycle) {

    private val object_list = list

    var fragmentsMap : MutableMap<Int, Fragment> = mutableMapOf()

    override fun getItemCount(): Int {
        return object_list.size
    }

    override fun createFragment(position: Int): Fragment {
        val frag = ImageViewpagerVh(object_list[position], position)
        fragmentsMap.set(position, frag)
        return frag
    }

}

