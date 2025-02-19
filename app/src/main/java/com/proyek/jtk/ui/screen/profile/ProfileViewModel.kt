package com.proyek.jtk.ui.screen.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.proyek.jtk.data.AppDatabase
import com.proyek.jtk.data.ProfileEntity
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).dataDao()
    val dataList: LiveData<List<ProfileEntity>> = dao.getProfile()

    fun upsertProfile(id : Int, nama: String, email: String) {
        viewModelScope.launch {
            val currentData = dataList.value?.firstOrNull()
            if (currentData != null) {
                val updatedProfile = currentData.copy(id = id, nama = nama, email = email)
                dao.update(updatedProfile)
            } else {
                dao.insert(ProfileEntity(id = id, nama = nama, email = email))
            }
        }
    }
}
