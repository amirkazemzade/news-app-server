package com.example.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("items")
data class DataListModel(val items: List<DataModel>) : DataModel