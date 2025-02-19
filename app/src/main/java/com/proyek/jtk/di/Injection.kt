package com.proyek.jtk.di

import com.proyek.jtk.data.RewardRepository


object Injection {
    fun provideRepository(): RewardRepository {
        return RewardRepository.getInstance()
    }
}