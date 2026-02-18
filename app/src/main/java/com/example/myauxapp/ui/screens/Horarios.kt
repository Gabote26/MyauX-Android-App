package com.example.myauxapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myauxapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Horarios() {
    var selectedSemester by remember { mutableStateOf("Seleccionar Semestre") }
    var selectedGroup by remember { mutableStateOf("Seleccionar Grupo") }
    var selectedSpecialty by remember { mutableStateOf("Seleccionar Especialidad") }
    
    var expandedSemester by remember { mutableStateOf(false) }
    var expandedGroup by remember { mutableStateOf(false) }
    var expandedSpecialty by remember { mutableStateOf(false) }
    
    val semesters = listOf("1er Semestre", "2do Semestre", "3er Semestre", "4to Semestre", "5to Semestre", "6to Semestre")
    val groups = listOf("Grupo A", "Grupo B", "Grupo C", "Grupo D")
    val specialties = listOf("Programación", "Electrónica", "Mecatrónica", "Administración")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F5F5),
                        Color(0xFFEDE7F6)
                    )
                )
            )
    ) {
        // Header with gradient
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF6A1B9A),
                                Color(0xFF4A148C)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = "Horarios",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Semester Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedSemester,
                onExpandedChange = { expandedSemester = !expandedSemester }
            ) {
                OutlinedTextField(
                    value = selectedSemester,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Semestre") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6A1B9A),
                        focusedLabelColor = Color(0xFF6A1B9A),
                        unfocusedBorderColor = Color(0xFF9E9E9E)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedSemester,
                    onDismissRequest = { expandedSemester = false }
                ) {
                    semesters.forEach { semester ->
                        DropdownMenuItem(
                            text = { Text(semester) },
                            onClick = {
                                selectedSemester = semester
                                expandedSemester = false
                            }
                        )
                    }
                }
            }
            
            // Group Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedGroup,
                onExpandedChange = { expandedGroup = !expandedGroup }
            ) {
                OutlinedTextField(
                    value = selectedGroup,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Grupo") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6A1B9A),
                        focusedLabelColor = Color(0xFF6A1B9A),
                        unfocusedBorderColor = Color(0xFF9E9E9E)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedGroup,
                    onDismissRequest = { expandedGroup = false }
                ) {
                    groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group) },
                            onClick = {
                                selectedGroup = group
                                expandedGroup = false
                            }
                        )
                    }
                }
            }
            
            // Specialty Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedSpecialty,
                onExpandedChange = { expandedSpecialty = !expandedSpecialty }
            ) {
                OutlinedTextField(
                    value = selectedSpecialty,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Especialidad") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6A1B9A),
                        focusedLabelColor = Color(0xFF6A1B9A),
                        unfocusedBorderColor = Color(0xFF9E9E9E)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedSpecialty,
                    onDismissRequest = { expandedSpecialty = false }
                ) {
                    specialties.forEach { specialty ->
                        DropdownMenuItem(
                            text = { Text(specialty) },
                            onClick = {
                                selectedSpecialty = specialty
                                expandedSpecialty = false
                            }
                        )
                    }
                }
            }
            
            // Mi Horario Button
            Button(
                onClick = { /* TODO: Navigate to user's personal schedule */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A1B9A)
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = "Mi Horario",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            
            // Schedule Image Display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedSemester != "Seleccionar Semestre" && 
                        selectedGroup != "Seleccionar Grupo" && 
                        selectedSpecialty != "Seleccionar Especialidad") {
                        // TODO: Replace with actual schedule image based on selection
                        // For now, show placeholder
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.calendar),
                                contentDescription = "Horario",
                                modifier = Modifier.size(120.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Horario: $selectedSemester - $selectedGroup",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF6A1B9A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = selectedSpecialty,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF757575),
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Aquí se mostrará la imagen del horario",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF9E9E9E),
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Text(
                            text = "Selecciona semestre, grupo y especialidad\npara ver el horario",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF9E9E9E),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            }
        }
    }
}