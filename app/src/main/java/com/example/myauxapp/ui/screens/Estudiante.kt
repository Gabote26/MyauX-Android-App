package com.example.myauxapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Grade(
    val subject: String,
    val score: Double
)

data class Attendance(
    val subject: String,
    val attendancePct: Int,
    val delayPct: Int,
    val absencePct: Int
)

@Composable
fun Estudiante() {
    val grades = listOf(
        Grade("Programación", 9.5),
        Grade("Matemáticas", 8.7),
        Grade("Física", 9.0),
        Grade("Inglés", 8.5),
        Grade("Química", 9.2)
    )

    val attendances = listOf(
        Attendance("Programación", 95, 3, 2),
        Attendance("Matemáticas", 88, 8, 4),
        Attendance("Física", 75, 15, 10),
        Attendance("Inglés", 92, 5, 3),
        Attendance("Química", 80, 12, 8)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF4A148C),
            shadowElevation = 4.dp
        ) {
            Text(
                text = "Mi Perfil Académico",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Grades Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Calificaciones",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A148C),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Grades Table
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray)
                    ) {
                        // Header Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE1BEE7))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Materia",
                                modifier = Modifier.weight(2f),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Calificación",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            )
                        }

                        // Data Rows
                        grades.forEach { grade ->
                            HorizontalDivider(color = Color.LightGray)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = grade.subject,
                                    modifier = Modifier.weight(2f),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = String.format("%.1f", grade.score),
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4A148C),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Attendance Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Asistencias",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A148C),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Attendance Table
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray)
                    ) {
                        // Header Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE1BEE7))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Materia",
                                modifier = Modifier.weight(2f),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Asistencias",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Retardos",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Faltas",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }

                        // Data Rows
                        attendances.forEach { attendance ->
                            HorizontalDivider(color = Color.LightGray)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = attendance.subject,
                                    modifier = Modifier.weight(2f),
                                    fontSize = 12.sp
                                )
                                
                                // Attendance percentage with color coding
                                val attendanceColor = when {
                                    attendance.attendancePct >= 90 -> Color(0xFF4CAF50) // Green
                                    attendance.attendancePct == 80 -> Color(0xFFFFC107) // Yellow
                                    attendance.attendancePct < 80 -> Color(0xFFF44336) // Red
                                    else -> Color(0xFFFFC107) // Yellow for 80-89%
                                }
                                
                                Text(
                                    text = "${attendance.attendancePct}%",
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = attendanceColor,
                                    fontSize = 12.sp
                                )
                                
                                Text(
                                    text = "${attendance.delayPct}%",
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFC107), // Yellow
                                    fontSize = 12.sp
                                )
                                
                                Text(
                                    text = "${attendance.absencePct}%",
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF44336), // Red
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Legend
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Leyenda de Asistencias:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color(0xFF4CAF50))
                            )
                            Text(text = "≥90%", fontSize = 11.sp)
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color(0xFFFFC107))
                            )
                            Text(text = "80-89%", fontSize = 11.sp)
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color(0xFFF44336))
                            )
                            Text(text = "<80%", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}