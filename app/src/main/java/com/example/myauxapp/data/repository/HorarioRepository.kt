package com.example.myauxapp.data.repository

import com.example.myauxapp.R
import com.example.myauxapp.ui.models.HorarioItem
import com.example.myauxapp.ui.models.HorarioKey

object HorarioRepository {

    val semestres = listOf("1", "2", "3", "4", "5", "6")
    val grupos = listOf("AM", "BM", "CM")
    val especialidades = listOf("PRO", "SMEC", "MEC")

    // Mapeo de cada combinacion con el drawable
    private val horarios = listOf(
        HorarioItem(HorarioKey("4", "AM", "PRO"), R.drawable.hor_4_am_pro),
        HorarioItem(HorarioKey("4", "AM", "MEC"), R.drawable.hor_4_am_mec),
        HorarioItem(HorarioKey("6", "AM", "SMEC"), R.drawable.hor_6_am_smec)
    )

    fun buscar(semestre: String, grupo: String, especialidad: String): Int? {
        return horarios.find {
            it.key.semestre == semestre && it.key.grupo == grupo && it.key.especialidad == especialidad
        }?.imagenRes
    }

}