package com.proyek.jtk.api

import com.proyek.jtk.model.ApiResponse
import com.proyek.jtk.model.ApiResponseId
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("od_17046_rata_rata_lama_sekolah_berdasarkan_kabupatenkota")
    suspend fun getSchoolStats(): Response<ApiResponse>

    companion object {
        private const val BASE_URL = "https://data.jabarprov.go.id/api-backend/bigdata/bps/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

    @GET("od_17046_rata_rata_lama_sekolah_berdasarkan_kabupatenkota/{id}")
    suspend fun getSchoolStatsById(@Path("id") id: Int): Response<ApiResponseId>
}
