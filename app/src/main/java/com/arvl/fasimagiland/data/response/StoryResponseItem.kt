package com.arvl.fasimagiland.data.response

import com.google.gson.annotations.SerializedName

data class StoryResponseItem(

	@field:SerializedName("difficulty")
	val difficulty: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
