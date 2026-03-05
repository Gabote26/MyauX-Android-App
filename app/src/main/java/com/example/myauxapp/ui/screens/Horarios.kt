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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myauxapp.R
import com.example.myauxapp.data.repository.HorarioRepository

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Horarios() {
    val viewModel: HorarioViewModel = viewModel()
    val state by viewModel.state
    val semestre by viewModel.semestre
    val grupo by viewModel.grupo
    val especialidad by viewModel.especialidad

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
        // Header con gradiente
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

            // ── Dropdown Semestre ──────────────────────────────────
            HorarioDropdown(
                label = "Semestre",
                selected = semestre,
                opciones = HorarioRepository.semestres,
                onSelect = { viewModel.setSemestre(it) }
            )

            // ── Dropdown Grupo ─────────────────────────────────────
            HorarioDropdown(
                label = "Grupo",
                selected = grupo,
                opciones = HorarioRepository.grupos,
                onSelect = { viewModel.setGrupo(it) }
            )

            // ── Dropdown Especialidad ──────────────────────────────
            HorarioDropdown(
                label = "Especialidad",
                selected = especialidad,
                opciones = HorarioRepository.especialidades,
                onSelect = { viewModel.setEspecialidad(it) }
            )

            // ── Resultado ──────────────────────────────────────────
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
                    when (val s = state) {

                        is HorarioState.Idle -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.calendar),
                                    contentDescription = null,
                                    modifier = Modifier.size(120.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Selecciona semestre, grupo y especialidad\npara ver el horario",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF9E9E9E),
                                    fontSize = 16.sp
                                )
                            }
                        }

                        is HorarioState.Found -> {
                            Image(
                                painter = painterResource(id = s.imagenRes),
                                contentDescription = "Horario",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.4f),
                                contentScale = ContentScale.Fit
                            )
                        }

                        is HorarioState.NotFound -> {
                            Text(
                                text = "No se encontró horario para esta combinación.",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Componente reutilizable de dropdown ───────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HorarioDropdown(
    label: String,
    selected: String,
    opciones: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected.ifBlank { "Selecciona $label" },
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6A1B9A),
                unfocusedBorderColor = Color(0xFF9E9E9E),
                focusedLabelColor = Color(0xFF6A1B9A),
                focusedTextColor = Color(0xFF4A148C),
                unfocusedTextColor = Color(0xFF4A148C)
            ),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onSelect(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}