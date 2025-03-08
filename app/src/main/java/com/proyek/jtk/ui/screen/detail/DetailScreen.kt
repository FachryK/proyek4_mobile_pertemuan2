package com.proyek.jtk.ui.screen.detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.proyek.jtk.ui.screen.home.getCityImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    backStackEntry: NavBackStackEntry,
    viewModel: DetailViewModel = viewModel()
) {
    val schoolId = backStackEntry.arguments?.getString("schoolId")?.toIntOrNull()

    if (schoolId != null) {
        LaunchedEffect(schoolId) {
            viewModel.fetchSchoolStatsById(schoolId)
        }
    }

    val schoolStat = viewModel.schoolStat.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Data") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        content = { paddingValues ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if (errorMessage != null) {
                    Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_LONG).show()
                } else {
                    schoolStat?.let { stat ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp)
                        ) {
                            val cityImage = getCityImage(stat.namaKabupatenKota)

                            Image(
                                painter = cityImage,
                                contentDescription = "Gambar Sekolah",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .padding(bottom = 16.dp),
                                contentScale = ContentScale.Crop
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Nama Kota: ${stat.namaKabupatenKota}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Nama Provinsi: ${stat.namaProvinsi}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Rata-rata Lama Sekolah: ${stat.rataRataLamaSekolah}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Satuan: ${stat.satuan}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Tahun: ${stat.tahun}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

