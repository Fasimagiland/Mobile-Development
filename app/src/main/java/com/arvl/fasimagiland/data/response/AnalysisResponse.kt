package com.arvl.fasimagiland.data.response

import com.google.gson.annotations.SerializedName

data class AnalysisResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("cerita")
	val cerita: String? = null,

	@field:SerializedName("hasil gambar")
	val hasilGambar: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
