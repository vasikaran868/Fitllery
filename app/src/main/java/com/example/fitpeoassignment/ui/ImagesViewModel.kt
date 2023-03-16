package com.example.fitpeoassignment.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitpeoassignment.data.ImagesRepositary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    val repositary: ImagesRepositary
): ViewModel() {

    private val _imagesList: MutableLiveData<List<ImageData>> = MutableLiveData(emptyList())
    val imagesList:LiveData<List<ImageData>> get() =  _imagesList

    var currentPosition: Int? = null

    private val _albumList: MutableLiveData<List<AlbumVhData>> = MutableLiveData(emptyList())
    val albumList:LiveData<List<AlbumVhData>> get() =  _albumList

    var currentAlbum = 1

    var allImagesData = listOf<ImageData>()

    var searchFilter: String? = null


    init {
        observeFlow()
    }

    fun observeFlow(){
        viewModelScope.launch {
            try {
                repositary.getFlow().collect{list->
                    Log.v("MyActivity","received in viewmodel ${list}")
                    allImagesData = list
                    val albumList = mutableListOf<AlbumVhData>()
                    list.distinctBy { it.albumId }.forEach {
                        albumList.add(AlbumVhData(albumId = it.albumId, albumUrl = it.thumbnailUrl))
                    }
                    _imagesList.value = list.filter { it.albumId == currentAlbum }
                    _albumList.value = albumList.toList()
                }
            }catch (e:Exception){
                Log.v("MyActivity","ex: ${e.localizedMessage}")
            }
        }
        getImages()
    }

    fun changeCurrentAlbum(albumId: Int){
        currentAlbum = albumId
        searchFilter = null
        _imagesList.value = allImagesData.filter { it.albumId == currentAlbum }
    }

    fun searchFilterChanged(){
        if (searchFilter != null){
            _imagesList.value = allImagesData.filter { it.albumId == currentAlbum && it.title.startsWith(searchFilter!!) }
        }
        else{
            _imagesList.value = allImagesData.filter { it.albumId == currentAlbum }
        }
    }

    fun getImages(){
        viewModelScope.launch {
            repositary.getImages()
        }
    }

}