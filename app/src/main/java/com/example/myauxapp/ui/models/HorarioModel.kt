package com.example.myauxapp.ui.models

import androidx.annotation.DrawableRes

data class HorarioKey(
    val semestre: String,
    val grupo: String,
    val especialidad: String
)

data class HorarioItem(
    val key: HorarioKey,
    @DrawableRes val imagenRes: Int
)