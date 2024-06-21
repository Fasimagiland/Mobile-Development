package com.arvl.fasimagiland.ui.screen.liststory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arvl.fasimagiland.data.response.DetailResponse
import com.arvl.fasimagiland.data.response.StoryResponseItem
import com.arvl.fasimagiland.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {

    private val _stories = MutableLiveData<List<StoryResponseItem>>()
    val stories: LiveData<List<StoryResponseItem>> get() = _stories

    private val _detailStory = MutableLiveData<String>()
    val detailStory: LiveData<String> get() = _detailStory

    private val apiService = ApiConfig.getApiService()

    fun fetchStories() {
        viewModelScope.launch {
            val call = apiService.getStories()
            call.enqueue(object : Callback<List<StoryResponseItem>> {
                override fun onResponse(
                    call: Call<List<StoryResponseItem>>,
                    response: Response<List<StoryResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        _stories.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<StoryResponseItem>>, t: Throwable) {
                    // Handle failure
                }
            })
        }
    }

    fun fetchDetailStory(storyId: String) {
        viewModelScope.launch {
            val call = apiService.getDetailStory(storyId)
            call.enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if (response.isSuccessful) {
                        _detailStory.postValue(response.body()?.story ?: "")
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    // Handle failure
                }
            })
        }
    }
}


