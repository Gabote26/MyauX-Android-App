package com.example.myauxapp.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myauxapp.data.repository.HorarioRepository

sealed class HorarioState {
    object Idle : HorarioState()
    data class Found(@androidx.annotation.DrawableRes val imagenRes: Int) : HorarioState()
    object NotFound : HorarioState()
}

class HorarioViewModel : ViewModel() {

    private val _semestre     = mutableStateOf("")
    private val _grupo        = mutableStateOf("")
    private val _especialidad = mutableStateOf("")
    private val _state        = mutableStateOf<HorarioState>(HorarioState.Idle)

    val semestre:     State<String>       = _semestre
    val grupo:        State<String>       = _grupo
    val especialidad: State<String>       = _especialidad
    val state:        State<HorarioState> = _state

    fun setSemestre(value: String) {
        _semestre.value = value
        buscar()
    }

    fun setGrupo(value: String) {
        _grupo.value = value
        buscar()
    }

    fun setEspecialidad(value: String) {
        _especialidad.value = value
        buscar()
    }

    private fun buscar() {
        val s = _semestre.value
        val g = _grupo.value
        val e = _especialidad.value

        // Solo busca cuando los 3 campos están seleccionados
        if (s.isBlank() || g.isBlank() || e.isBlank()) {
            _state.value = HorarioState.Idle
            return
        }

        val resultado = HorarioRepository.buscar(s, g, e)
        _state.value = if (resultado != null)
            HorarioState.Found(resultado)
        else
            HorarioState.NotFound
    }
}