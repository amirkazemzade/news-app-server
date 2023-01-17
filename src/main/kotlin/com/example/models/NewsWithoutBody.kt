package com.example.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("NewsTitle")
data class NewsWithoutBody @OptIn(ExperimentalSerializationApi::class) constructor(
    val id: Int? = null,
    val title: String,
    @SerialName("category_id") val categoryId: Int,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    @SerialName("view_count") val viewCount: Int = 0,
) : DataModel