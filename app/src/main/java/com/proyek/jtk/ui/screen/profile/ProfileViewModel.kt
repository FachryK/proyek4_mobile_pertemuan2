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
    val dataList: LiveData<ProfileEntity?> = dao.getProfile()

    fun upsertProfile(nama: String, nim: String, email: String, photo: String) {
        viewModelScope.launch {
            val currentData = dao.getProfileSync()
            if (currentData != null) {
                val updatedProfile = ProfileEntity(
                    id = currentData.id,
                    nama = nama,
                    nim = nim,
                    email = email,
                    photo = photo
                )
                dao.update(updatedProfile)
            } else {
                dao.insert(ProfileEntity(nama = nama, nim = nim, email = email, photo = photo))
            }
        }
    }
}
