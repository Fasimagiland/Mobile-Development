package com.arvl.fasimagiland.data.retrofit

import com.arvl.fasimagiland.data.response.AnalysisResponse
import com.arvl.fasimagiland.data.response.AnalyzeRequest
import com.arvl.fasimagiland.data.response.DetailResponse
import com.arvl.fasimagiland.data.response.StoryResponseItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("stories")
    fun getStories(): Call<List<StoryResponseItem>>

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") storyId: String
    ): Call<DetailResponse>

    @POST("stories/{id}")
    fun analyzeText(
        @Path("id") storyId: String,
        @Body request: AnalyzeRequest
    ): Call<AnalysisResponse>
}
