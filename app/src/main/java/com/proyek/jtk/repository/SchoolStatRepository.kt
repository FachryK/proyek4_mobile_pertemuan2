package com.proyek.jtk.repository

import com.proyek.jtk.api.ApiService
import com.proyek.jtk.model.ApiResponse

class SchoolStatRepository(private val apiService: ApiService = ApiService.create()) {

    suspend fun getSchoolStats(): ApiResponse? {
        return try {
            val response = apiService.getSchoolStats()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
