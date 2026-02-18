package com.example.myauxapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NewsItem(
    val title: String,
    val date: String,
    val content: String
)

@Composable
fun HomeScreen() {
    val newsList = listOf(
        NewsItem(
            title = "17 de febrero no hay clases",
            date = "15 de febrero, 2026",
            content = "Se informa a todos los estudiantes que el día 17 de febrero no habrá clases por actividades académicas."
        ),
        NewsItem(
            title = "20 de febrero entrega de calificaciones",
            date = "14 de febrero, 2026",
            content = "La entrega de calificaciones del periodo actual será el 20 de febrero. Favor de estar pendientes."
        ),
        NewsItem(
            title = "Inicio de inscripciones",
            date = "10 de febrero, 2026",
            content = "Las inscripciones para el próximo semestre inician el 25 de febrero. Consulta tu horario de inscripción."
        ),
        NewsItem(
            title = "Mantenimiento de instalaciones",
            date = "8 de febrero, 2026",
            content = "Se realizará mantenimiento en las instalaciones del edificio A el día 22 de febrero."
        )
    )

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
                    text = "Noticias y Anuncios",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
        }

        // News List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(newsList) { news ->
                NewsCard(news)
            }
        }
    }
}

@Composable
fun NewsCard(news: NewsItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A1B9A),
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = news.date,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9E9E9E),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = news.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF424242),
                lineHeight = 22.sp
            )
        }
    }
}
