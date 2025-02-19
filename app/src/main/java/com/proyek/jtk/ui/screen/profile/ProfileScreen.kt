package com.proyek.jtk.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val profileData by viewModel.dataList.observeAsState(emptyList())

    var isEditing by remember { mutableStateOf(false) }
    var studentName by remember { mutableStateOf(profileData.firstOrNull()?.nama ?: "") }
    var studentId by remember { mutableStateOf(profileData.firstOrNull()?.id?.toString() ?: "") }
    var studentEmail by remember { mutableStateOf(profileData.firstOrNull()?.email ?: "") }
    var profileUploaded by remember { mutableStateOf(false) }

    LaunchedEffect(profileData.firstOrNull()) {
        profileData.firstOrNull()?.let { profile ->
            studentId = profile.id.toString()
            studentName = profile.nama
            studentEmail = profile.email
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
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = if (profileUploaded) "Uploaded Profile Picture" else "Default Profile Picture",
                    tint = if (profileUploaded) MaterialTheme.colorScheme.onPrimary else Color.Gray,
                    modifier = Modifier.size(80.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isEditing) {
                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Student Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = studentId,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            studentId = newValue
                        }
                    },
                    label = { Text("Student ID") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = studentEmail,
                    onValueChange = { studentEmail = it },
                    label = { Text("Student Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { profileUploaded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Upload Photo")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val studentIdInt = studentId.toIntOrNull() ?: 0
                        viewModel.upsertProfile(studentIdInt, studentName, studentEmail)
                        isEditing = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }

            } else {
                Text(text = studentName, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ID: $studentId", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = studentEmail, style = MaterialTheme.typography.bodyLarge)
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

