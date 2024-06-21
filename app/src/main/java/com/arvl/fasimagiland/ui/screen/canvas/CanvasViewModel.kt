package com.arvl.fasimagiland.ui.screen.canvas

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arvl.fasimagiland.data.response.AnalysisResponse
import com.arvl.fasimagiland.data.response.AnalyzeRequest
import com.arvl.fasimagiland.data.retrofit.ApiConfig
import com.arvl.fasimagiland.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CanvasViewModel : ViewModel() {

    private val _storyText = MutableLiveData<String>()
    val storyText: LiveData<String> get() = _storyText

    private val _classificationResults = MutableLiveData<List<Classifications>?>()
    val classificationResults: LiveData<List<Classifications>?> get() = _classificationResults

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _lastBitmap = MutableLiveData<Bitmap>()
    val lastBitmap: LiveData<Bitmap> get() = _lastBitmap

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _isFailed = MutableLiveData<Boolean>()
    val isFailed: LiveData<Boolean> get() = _isFailed

    fun setStoryText(text: String) {
        _storyText.value = text
    }

    private val _isAnalysisSuccessful = MutableLiveData<Boolean>()
    val isAnalysisSuccessful: LiveData<Boolean> get() = _isAnalysisSuccessful

    fun analyzeText(storyId: String, text: String) {
        val apiService = ApiConfig.getApiService()
        val analyzeCall = apiService.analyzeText(storyId, AnalyzeRequest(text))

        analyzeCall.enqueue(object : Callback<AnalysisResponse> {
            override fun onResponse(call: Call<AnalysisResponse>, response: Response<AnalysisResponse>) {
                if (response.isSuccessful) {
                    val analysisResponse = response.body()
                    val nextStory = analysisResponse?.data?.cerita ?: ""
                    _storyText.postValue(nextStory)
                    _isAnalysisSuccessful.postValue(true)
                } else {
                    _errorMessage.postValue("Silahkan gambar ulang!")
                    _isAnalysisSuccessful.postValue(false)
                }
            }

            override fun onFailure(call: Call<AnalysisResponse>, t: Throwable) {
                _errorMessage.postValue("Network error: ${t.message}")
                _isAnalysisSuccessful.postValue(false)
            }
        })
    }



    fun classifyCanvas(bitmap: Bitmap, classifierHelper: ImageClassifierHelper) {
        _lastBitmap.postValue(bitmap)
        classifierHelper.classifyCanvas(bitmap)
    }

}

