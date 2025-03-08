package com.proyek.jtk.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.proyek.jtk.R
import com.proyek.jtk.api.ApiService
import com.proyek.jtk.model.SchoolStatEntity
import com.proyek.jtk.repository.SchoolStatRepository
import com.proyek.jtk.ui.common.UiState

@Composable
fun HomeScreen(navController: NavController) {
    val repository = remember { SchoolStatRepository(ApiService.create()) }

    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(repository))

    val uiState by homeViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.fetchSchoolStats()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }
            is UiState.Success -> {
                if (state.data.isEmpty()) {
                    Text(
                        text = "Tidak ada data sekolah yang tersedia.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    HomeContent(schoolStats = state.data, navController = navController)
                }
            }
            is UiState.Error -> {
                Text(
                    text = "Terjadi kesalahan: ${state.message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun HomeContent(schoolStats: List<SchoolStatEntity>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(schoolStats) { data ->
            SchoolStatCard(
                schoolStat = data,
                onClick = { navController.navigate("detail/${data.id}") }
            )
        }
    }
}

@Composable
fun SchoolStatCard(schoolStat: SchoolStatEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = getCityImage(schoolStat.namaKabupatenKota),
                contentDescription = "Gambar ${schoolStat.namaKabupatenKota}",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Lokasi",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = schoolStat.namaKabupatenKota,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Lama Sekolah",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Lama Sekolah: ${schoolStat.rataRataLamaSekolah} tahun",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Kalender",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tahun: ${schoolStat.tahun}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun getCityImage(cityName: String): Painter {
    val formattedCityName = cityName
        .lowercase()
        .replace(" ", "_")
        .substringAfter("kabupaten_")

    val resourceId = when (formattedCityName) {
        "bogor" -> R.drawable.bogor
        "sukabumi" -> R.drawable.sukabumi
        "cianjur" -> R.drawable.cianjur
        "bandung" -> R.drawable.bandung
        "garut" -> R.drawable.garut
        else -> R.drawable.belum
    }
    return painterResource(id = resourceId)
}
