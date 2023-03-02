package com.example.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class ResponseModel @OptIn(ExperimentalSerializationApi::class) constructor(
    val state: State,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val message: String? = null,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val result: DataModel? = null,
)