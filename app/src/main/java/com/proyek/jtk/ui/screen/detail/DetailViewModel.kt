package com.proyek.jtk.ui.screen.detail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proyek.jtk.api.ApiService
import com.proyek.jtk.model.ApiResponseId
import com.proyek.jtk.model.SchoolStatEntity
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailViewModel(private val apiService: ApiService) : ViewModel() {

    val schoolStat = mutableStateOf<SchoolStatEntity?>(null)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun fetchSchoolStatsById(id: Int) {
        isLoading.value = true
        errorMessage.value = null
        viewModelScope.launch {
            try {
                val response: Response<ApiResponseId> = apiService.getSchoolStatsById(id)
                Log.d("DetailViewModel", "Raw API Response: ${response.raw()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("DetailViewModel", "Parsed API Response: $responseBody")

                    val data = responseBody?.data
                    if (data != null) {
                        schoolStat.value = data
                    } else {
                        errorMessage.value = "Data not found"
                    }
                } else {
                    errorMessage.value = "Failed to load data: ${response.message()}"
                    Log.e("DetailViewModel", "API Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage.value = "An error occurred: ${e.message}"
                Log.e("DetailViewModel", "Exception: ${e.stackTraceToString()}")
            } finally {
                isLoading.value = false
            }
        }
    }
}

class DetailViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
