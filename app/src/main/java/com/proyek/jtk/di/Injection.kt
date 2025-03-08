package com.proyek.jtk.di

import com.proyek.jtk.api.ApiClient
import com.proyek.jtk.api.ApiService
import com.proyek.jtk.repository.SchoolStatRepository

object AppModule {

    val apiService: ApiService by lazy {
        ApiClient.apiService
    }

    val schoolStatRepository: SchoolStatRepository by lazy {
        SchoolStatRepository(apiService)
    }
}
