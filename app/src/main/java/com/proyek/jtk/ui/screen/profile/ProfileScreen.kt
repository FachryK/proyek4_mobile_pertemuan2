package com.proyek.jtk.ui.screen.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val profileData by viewModel.dataList.observeAsState(initial = null)

    var isEditing by remember { mutableStateOf(false) }
    var studentName by remember { mutableStateOf("") }
    var studentNIM by remember { mutableStateOf("") }
    var studentEmail by remember { mutableStateOf("") }
    var studentPhoto by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(profileData) {
        profileData?.let { profile ->
            studentNIM = profile.nim.ifBlank { "" }
            studentName = profile.nama.ifBlank { "" }
            studentEmail = profile.email.ifBlank { "" }
            studentPhoto = profile.photo.takeIf { it.isNotBlank() }?.let { Uri.parse(it) }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            studentPhoto = it
            profileData?.let { profile ->
                viewModel.upsertProfile(
                    nama = profile.nama,
                    nim = profile.nim,
                    email = profile.email,
                    photo = it.toString()
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .background(Color.LightGray, CircleShape)
                    .clickable(enabled = isEditing) {
                    if (isEditing) {
                        imagePickerLauncher.launch("image/*")
                    }
                },
                contentAlignment = Alignment.Center
            ) {
                if (studentPhoto != null) {
                    androidx.compose.foundation.Image(
                        painter = rememberAsyncImagePainter(model = studentPhoto),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default Profile Picture",
                        tint = Color.Gray,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isEditing) {
                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Nama Mahasiswa") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = studentNIM,
                    onValueChange = { studentNIM = it },
                    label = { Text("NIM Mahasiswa") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = studentEmail,
                    onValueChange = { studentEmail = it },
                    label = { Text("Email Mahasiswa") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.upsertProfile(
                            studentName,
                            studentNIM,
                            studentEmail,
                            studentPhoto?.toString() ?: ""
                        )
                        isEditing = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }

            } else {
                Text(text = studentName.ifBlank { "Belum Mengisi" }, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = studentNIM.ifBlank { "Belum Mengisi" }, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = studentEmail.ifBlank { "Belum Mengisi" }, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Profile")
                }
            }
        }
    }
}
