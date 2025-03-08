package com.proyek.jtk.model

import com.google.gson.annotations.SerializedName

data class SchoolStatEntity (
    val id: Int,
    @SerializedName("kode_provinsi")
    val kodeProvinsi: Int,
    @SerializedName("nama_provinsi")
    val namaProvinsi: String,
    @SerializedName("kode_kabupaten_kota")
    val kodeKabupatenKota: Int,
    @SerializedName("nama_kabupaten_kota")
    val namaKabupatenKota: String,
    @SerializedName("rata_rata_lama_sekolah")
    val rataRataLamaSekolah: Float,
    val satuan: String,
    val tahun: Int
)