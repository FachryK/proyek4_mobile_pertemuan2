package com.proyek.jtk.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.proyek.jtk.model.SchoolStatEntity
import com.proyek.jtk.repository.SchoolStatRepository
import com.proyek.jtk.ui.common.UiState

class HomeViewModel(private val repository: SchoolStatRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<SchoolStatEntity>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<SchoolStatEntity>>> = _uiState.asStateFlow()

    init {
        fetchSchoolStats()
    }

    fun fetchSchoolStats() {
        viewModelScope.launch {
            flow {
                val response = repository.getSchoolStats()
                emit(response?.data ?: emptyList())
            }
                .catch { e ->
                    _uiState.value = UiState.Error(e.localizedMessage ?: "Terjadi kesalahan")
                }
                .collect { data ->
                    _uiState.value = if (data.isNotEmpty()) UiState.Success(data)
                    else UiState.Error("Data kosong")
                }
        }
    }

    fun getSchoolStatById(schoolId: String): SchoolStatEntity? {
        val id = schoolId.toIntOrNull() ?: return null
        return (uiState.value as? UiState.Success)?.data?.find { it.id == id }
    }
}

class HomeViewModelFactory(private val repository: SchoolStatRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
