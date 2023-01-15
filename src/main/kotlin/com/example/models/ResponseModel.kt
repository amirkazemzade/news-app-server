package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ResponseModel(
    val state: State,
    val message: String? = null,
    val result: DataModel? = null,
)