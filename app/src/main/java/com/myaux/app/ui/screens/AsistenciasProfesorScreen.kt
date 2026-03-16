package com.myaux.app.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myaux.app.ui.theme.*
import kotlinx.coroutines.delay

data class AlumnoAsistencia(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val numControl: Long,
    var estado: String = "", // "A", "F", "J"
    var locked: Boolean = false // If more than 15 mins since saved
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsistenciasProfesorScreen(
    materias: List<String>,
    alumnos: List<AlumnoAsistencia>,
    isLoading: Boolean,
    onSelectGrupo: (String, java.time.LocalDate) -> Unit,
    onSaveAsistencias: (List<AlumnoAsistencia>, String, java.time.LocalDate) -> Unit,
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

    var classDate by remember { mutableStateOf(java.time.LocalDate.now()) }

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selected = java.time.LocalDate.of(year, month + 1, dayOfMonth)
            if (selected.dayOfWeek == java.time.DayOfWeek.SATURDAY || selected.dayOfWeek == java.time.DayOfWeek.SUNDAY) {
                Toast.makeText(context, "No puedes seleccionar fines de semana", Toast.LENGTH_SHORT).show()
            } else {
                classDate = selected
            }
        },
        classDate.year,
        classDate.monthValue - 1,
        classDate.dayOfMonth
    )

    // When group selection or date changes, notify parent
    LaunchedEffect(grupoSeleccionado, classDate, selectedMateria) {
        if (grupoSeleccionado != null && selectedMateria != null) {
            onSelectGrupo(grupoSeleccionado, classDate)
        }
    }

    // Local mutable copy of student states
    val alumnoStates = remember(alumnos) {
        alumnos.map { mutableStateOf(it.estado) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📋 Gestionar Asistencias", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                actions = {
                    if (grupoSeleccionado != null && selectedMateria != null && alumnos.isNotEmpty()) {
                        IconButton(onClick = {
                            val updated = alumnos.mapIndexed { i, a ->
                                a.copy(estado = alumnoStates[i].value)
                            }
                            onSaveAsistencias(updated, selectedMateria!!, classDate)
                            Toast.makeText(context, "Asistencias guardadas", Toast.LENGTH_SHORT).show()
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
            // Selection card - same style as Horarios
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
                                })
                            }
                        }
                    }

                    if (grupoSeleccionado != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Grupo: $grupoSeleccionado",
                                fontSize = 13.sp,
                                color = PurplePrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = { datePickerDialog.show() },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkTopBar),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Fecha: ${classDate.dayOfMonth}/${classDate.monthValue}/${classDate.year}", color = Color.White, fontSize = 12.sp)
                            }
                        }
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
                    Text("A", Modifier.weight(0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentGreen, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Text("F", Modifier.weight(0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentRed, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Text("J", Modifier.weight(0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentYellow, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
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
                                        .padding(horizontal = 12.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${alumno.apellido} ${alumno.nombre}",
                                        Modifier.weight(2f),
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )

                                    Checkbox(
                                        checked = alumnoStates[index].value == "A",
                                        onCheckedChange = { if (!alumno.locked) alumnoStates[index].value = if (it) "A" else "" },
                                        enabled = !alumno.locked,
                                        modifier = Modifier.weight(0.6f),
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = AccentGreen,
                                            uncheckedColor = TextGray.copy(alpha = 0.4f),
                                            disabledCheckedColor = AccentGreen.copy(alpha = 0.5f)
                                        )
                                    )

                                    Checkbox(
                                        checked = alumnoStates[index].value == "F",
                                        onCheckedChange = { if (!alumno.locked) alumnoStates[index].value = if (it) "F" else "" },
                                        enabled = !alumno.locked,
                                        modifier = Modifier.weight(0.6f),
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = AccentRed,
                                            uncheckedColor = TextGray.copy(alpha = 0.4f),
                                            disabledCheckedColor = AccentRed.copy(alpha = 0.5f)
                                        )
                                    )

                                    Checkbox(
                                        checked = alumnoStates[index].value == "J",
                                        onCheckedChange = { if (!alumno.locked) alumnoStates[index].value = if (it) "J" else "" },
                                        enabled = !alumno.locked,
                                        modifier = Modifier.weight(0.6f),
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = AccentYellow,
                                            uncheckedColor = TextGray.copy(alpha = 0.4f),
                                            disabledCheckedColor = AccentYellow.copy(alpha = 0.5f)
                                        )
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
