package com.myaux.app.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myaux.app.ui.theme.*
import kotlinx.coroutines.delay

data class AlumnoCalificacion(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val numControl: Long,
    var parcial1: String = "",
    var parcial2: String = "",
    var parcial3: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalificacionesProfesorScreen(
    materias: List<String>,
    alumnos: List<AlumnoCalificacion>,
    isLoading: Boolean,
    onSelectGrupo: (String) -> Unit,
    onSelectMateria: (String) -> Unit,
    onSaveCalificaciones: (List<AlumnoCalificacion>, String) -> Unit,
    onBack: () -> Unit
) {
    val semestres = listOf("Seleccione...", "1°", "3°", "5°")
    val especialidades = listOf("Seleccione...", "ADMRH", "PRO", "ELE", "MEC", "MAU", "SMEC", "LOG")
    val turnos = listOf("Seleccione...", "AM", "BM", "CM", "AV", "BV", "CV")

    var selectedSemestre by remember { mutableStateOf(semestres[0]) }
    var selectedEspecialidad by remember { mutableStateOf(especialidades[0]) }
    var selectedTurno by remember { mutableStateOf(turnos[0]) }
    var selectedMateria by remember { mutableStateOf<String?>(null) }

    var expandedSem by remember { mutableStateOf(false) }
    var expandedEsp by remember { mutableStateOf(false) }
    var expandedTurno by remember { mutableStateOf(false) }
    var expandedMateria by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val grupoSeleccionado = if (
        selectedSemestre != "Seleccione..." &&
        selectedEspecialidad != "Seleccione..." &&
        selectedTurno != "Seleccione..."
    ) "${selectedSemestre.replace("°", "")}${selectedTurno}-${selectedEspecialidad}" else null

    LaunchedEffect(grupoSeleccionado, selectedMateria) {
        if (grupoSeleccionado != null && selectedMateria != null) {
            onSelectGrupo(grupoSeleccionado)
        }
    }

    val parcial1States = remember(alumnos) {
        alumnos.map { mutableStateOf(it.parcial1) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📝 Gestionar Calificaciones", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                actions = {
                    if (grupoSeleccionado != null && selectedMateria != null && alumnos.isNotEmpty()) {
                        IconButton(onClick = {
                            val updated = alumnos.mapIndexed { i, a ->
                                a.copy(parcial1 = parcial1States[i].value)
                            }
                            onSaveCalificaciones(updated, selectedMateria!!)
                            Toast.makeText(context, "Calificaciones guardadas", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.Save, contentDescription = "Guardar", tint = AccentGreen)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkTopBar,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = DarkSurface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Selection card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Selecciona semestre, especialidad y turno:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Semestre
                        ExposedDropdownMenuBox(
                            expanded = expandedSem,
                            onExpandedChange = { expandedSem = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedSemestre,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Semestre", fontSize = 10.sp) },
                                modifier = Modifier.menuAnchor(),
                                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedSem) },
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PurplePrimary,
                                    unfocusedBorderColor = TextGray.copy(alpha = 0.3f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                )
                            )
                            ExposedDropdownMenu(expandedSem, { expandedSem = false }) {
                                semestres.forEach { s ->
                                    DropdownMenuItem(text = { Text(s) }, onClick = {
                                        selectedSemestre = s; expandedSem = false
                                    })
                                }
                            }
                        }

                        // Especialidad
                        ExposedDropdownMenuBox(
                            expanded = expandedEsp,
                            onExpandedChange = { expandedEsp = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedEspecialidad,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Especialidad", fontSize = 10.sp) },
                                modifier = Modifier.menuAnchor(),
                                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedEsp) },
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PurplePrimary,
                                    unfocusedBorderColor = TextGray.copy(alpha = 0.3f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                )
                            )
                            ExposedDropdownMenu(expandedEsp, { expandedEsp = false }) {
                                especialidades.forEach { e ->
                                    DropdownMenuItem(text = { Text(e) }, onClick = {
                                        selectedEspecialidad = e; expandedEsp = false
                                    })
                                }
                            }
                        }

                        // Turno
                        ExposedDropdownMenuBox(
                            expanded = expandedTurno,
                            onExpandedChange = { expandedTurno = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedTurno,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Turno", fontSize = 10.sp) },
                                modifier = Modifier.menuAnchor(),
                                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTurno) },
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PurplePrimary,
                                    unfocusedBorderColor = TextGray.copy(alpha = 0.3f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                )
                            )
                            ExposedDropdownMenu(expandedTurno, { expandedTurno = false }) {
                                turnos.forEach { t ->
                                    DropdownMenuItem(text = { Text(t) }, onClick = {
                                        selectedTurno = t; expandedTurno = false
                                    })
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Materia dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedMateria,
                        onExpandedChange = { expandedMateria = it }
                    ) {
                        OutlinedTextField(
                            value = selectedMateria ?: "Seleccione materia...",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Materia") },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedMateria) },
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PurplePrimary,
                                unfocusedBorderColor = TextGray.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                            )
                        )
                        ExposedDropdownMenu(expandedMateria, { expandedMateria = false }) {
                            materias.forEach { m ->
                                DropdownMenuItem(text = { Text(m) }, onClick = {
                                    selectedMateria = m
                                    expandedMateria = false
                                    onSelectMateria(m)
                                })
                            }
                        }
                    }

                    if (grupoSeleccionado != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Grupo: $grupoSeleccionado",
                            fontSize = 13.sp,
                            color = PurplePrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PurplePrimary)
                }
            } else if (grupoSeleccionado == null || selectedMateria == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Selecciona grupo y materia", color = TextGray, fontSize = 14.sp)
                }
            } else if (alumnos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay alumnos en este grupo", color = TextGray, fontSize = 14.sp)
                }
            } else {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Alumno", Modifier.weight(2f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                    Text("P1", Modifier.weight(0.8f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentGreen, textAlign = TextAlign.Center)
                    Text("P2", Modifier.weight(0.8f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray.copy(alpha = 0.4f), textAlign = TextAlign.Center)
                    Text("P3", Modifier.weight(0.8f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray.copy(alpha = 0.4f), textAlign = TextAlign.Center)
                }

                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    itemsIndexed(alumnos) { index, alumno ->
                        val visible = remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            delay(index * 40L)
                            visible.value = true
                        }

                        AnimatedVisibility(
                            visible = visible.value,
                            enter = fadeIn(tween(300)) + slideInVertically(initialOffsetY = { it / 3 })
                        ) {
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkCard)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${alumno.apellido} ${alumno.nombre}",
                                        Modifier.weight(2f),
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )

                                    OutlinedTextField(
                                        value = parcial1States[index].value,
                                        onValueChange = { parcial1States[index].value = it },
                                        modifier = Modifier
                                            .weight(0.8f)
                                            .height(48.dp),
                                        textStyle = LocalTextStyle.current.copy(fontSize = 12.sp, textAlign = TextAlign.Center),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = AccentGreen,
                                            unfocusedBorderColor = TextGray.copy(alpha = 0.3f),
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                        ),
                                        singleLine = true
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Box(
                                        modifier = Modifier
                                            .weight(0.8f)
                                            .height(48.dp)
                                            .clickable {
                                                Toast.makeText(
                                                    context,
                                                    "Las calificaciones del 2do parcial estarán disponibles próximamente",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Lock,
                                            contentDescription = "Bloqueado",
                                            tint = TextGray.copy(alpha = 0.4f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(0.8f)
                                            .height(48.dp)
                                            .clickable {
                                                Toast.makeText(
                                                    context,
                                                    "Las calificaciones del 3er parcial estarán disponibles próximamente",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Lock,
                                            contentDescription = "Bloqueado",
                                            tint = TextGray.copy(alpha = 0.4f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
