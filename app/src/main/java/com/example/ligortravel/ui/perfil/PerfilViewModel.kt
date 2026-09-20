package com.example.ligortravel.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ligortravel.data.AuthRepository
import com.example.ligortravel.data.RegistroUsuario
import com.example.ligortravel.data.UserRepository
import com.example.ligortravel.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PerfilUiState(
    val nombre: String = "",
    val email: String = "",
    val telefono: String = "",
    val editando: Boolean = false,
    val cargando: Boolean = false,
    val nombreOriginal: String = "",
    val telefonoOriginal: String = "",
    val error: String? = null,
    val guardadoExitoso: Boolean = false
)

class PerfilViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        cargarUsuarioActual()
    }

    private fun cargarUsuarioActual() {
        val usuario = SessionManager.usuarioActual ?: return
        _uiState.value = _uiState.value.copy(
            nombre = usuario.nombre,
            email = usuario.email,
            telefono = usuario.telefono
        )
    }

    fun onNombreChange(valor: String) {
        _uiState.value = _uiState.value.copy(nombre = valor, error = null)
    }

    fun onTelefonoChange(valor: String) {
        _uiState.value = _uiState.value.copy(telefono = valor, error = null)
    }

    fun activarEdicion() {
        _uiState.value = _uiState.value.copy(editando = true, nombreOriginal = _uiState.value.nombre, telefonoOriginal = _uiState.value.telefono, guardadoExitoso = false)
    }
    fun cancelarEdicion() {
        _uiState.value = _uiState.value.copy(editando = false, nombre = _uiState.value.nombreOriginal, telefono = _uiState.value.telefonoOriginal)
    }

    fun guardarCambios() {
        val estado = _uiState.value
        val usuarioActual = SessionManager.usuarioActual ?: return
        if (usuarioActual.id == null) return

        if (estado.nombre.isBlank() || estado.telefono.isBlank()) {
            _uiState.value = estado.copy(error = "Completa todos los campos")
            return
        }

        _uiState.value = estado.copy(cargando = true, error = null)

        viewModelScope.launch {
            val usuarioActualizado = RegistroUsuario(
                id = usuarioActual.id,
                nombre = estado.nombre.trim(),
                email = usuarioActual.email,
                telefono = estado.telefono.trim()
            )

            repository.guardarPerfil(usuarioActualizado)
                .onSuccess { usuario ->
                    SessionManager.iniciarSesion(usuario)
                    _uiState.value = _uiState.value.copy(
                        cargando = false,
                        editando = false,
                        guardadoExitoso = true
                    )
                }
                .onFailure { excepcion ->
                    _uiState.value = _uiState.value.copy(
                        cargando = false,
                        error = excepcion.message ?: "No se pudieron guardar los cambios"
                    )
                }
        }
    }

    fun cerrarSesion() {
        authRepository.cerrarSesion()
        SessionManager.cerrarSesion()
    }
}
