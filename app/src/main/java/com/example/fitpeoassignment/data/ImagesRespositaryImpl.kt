package com.example.fitpeoassignment.data

import android.util.Log
import com.example.fitpeoassignment.ui.ImageData
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImagesRespositaryImpl(
    val ImagesNetworkService: NetworkService
): ImagesRepositary {

    val imagesData: MutableSharedFlow<List<ImageData>> = MutableSharedFlow()

    val gsonConverter = Gson()

    override fun getFlow(): MutableSharedFlow<List<ImageData>> = imagesData

    override suspend fun getImages(){
        try {
            val result = ImagesNetworkService.getImagesData()
            result.enqueue(
                object : Callback<String> {
                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {
                        Log.v("MyActivity","${response}")
                        if (response.isSuccessful){
                            val data = gsonConverter.fromJson(response.body(), Array<ImageNetwork>::class.java).toList()
                            GlobalScope.launch {
                                imagesData.emit(data.map { it.asExternalModel() })
                            }
                        }
                        else{
                            Log.v("MyActivity","failed...${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.v("MyActivity","failed...${t.localizedMessage}")
                    }

                }
            )
        }catch (e :Exception){
            Log.v("MyActivity","error...${e.localizedMessage}")
        }
    }

}