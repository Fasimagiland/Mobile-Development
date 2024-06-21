package com.arvl.fasimagiland.data.response

import com.google.gson.annotations.SerializedName

data class AnalyzeRequest(
    @SerializedName("text")
    val text: String
)