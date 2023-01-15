package com.example.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Categories")
data class CategoriesModel(
    val categories: List<Int>
) : DataModel()