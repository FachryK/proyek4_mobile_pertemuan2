package com.proyek.jtk.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("message") val message: String,
    @SerializedName("error") val error: Int,
    @SerializedName("data") val data: List<SchoolStatEntity>
)

data class ApiResponseId(
    @SerializedName("message") val message: String,
    @SerializedName("error") val error: Int,
    @SerializedName("data") val data: SchoolStatEntity
)
