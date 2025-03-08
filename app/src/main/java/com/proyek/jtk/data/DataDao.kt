package com.proyek.jtk.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataDao {

    @Insert
    suspend fun insert(data: DataEntity)

    @Update
    suspend fun update(data: DataEntity)

    @Query("SELECT * FROM data_table ORDER BY id DESC")
    fun getAll(): LiveData<List<DataEntity>>

    @Query("SELECT * FROM data_table WHERE id = :dataId")
    suspend fun getById(dataId: Int): DataEntity?

    @Delete
    suspend fun delete(data: DataEntity)

    @Insert
    suspend fun insert(data: ProfileEntity)

    @Update
    suspend fun update(data: ProfileEntity)

    @Query("SELECT * FROM profile_table LIMIT 1")
    fun getProfile(): LiveData<ProfileEntity?>

    @Query("SELECT * FROM profile_table LIMIT 1")
    suspend fun getProfileSync(): ProfileEntity?
}
