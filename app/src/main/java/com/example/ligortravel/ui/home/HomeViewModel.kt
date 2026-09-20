package com.example.ligortravel.ui.home

import androidx.lifecycle.ViewModel
import com.example.ligortravel.data.Experiencia
import com.example.ligortravel.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val nombre: String = "",
    val ubicacion: String = "Lima, Perú",
    val destacadas: List<Experiencia> = emptyList(),
    val locales: List<Experiencia> = emptyList()
)

// Datos mock: todavía no existe una colección "experiencias" en Firestore,
// así que por ahora esta pantalla no usa ningún repositorio.
class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        cargarUsuarioActual()
        cargarExperienciasMock()
    }

    private fun cargarUsuarioActual() {
        val usuario = SessionManager.usuarioActual ?: return
        _uiState.value = _uiState.value.copy(nombre = usuario.nombre)
    }

    private fun cargarExperienciasMock() {
        _uiState.value = _uiState.value.copy(
            destacadas = listOf(
                Experiencia(
                    id = "1",
                    titulo = "Trekking a la Laguna 69",
                    ubicacion = "Huaraz, Áncash",
                    duracion = "1 día",
                    rating = 4.9,
                    precioDesde = 150
                ),
                Experiencia(
                    id = "2",
                    titulo = "Amanecer en Vinicunca",
                    ubicacion = "Cusipata, Cusco",
                    duracion = "12 h",
                    rating = 4.7,
                    precioDesde = 190,
                    categoria = "Trekking"
                )
            ),
            locales = listOf(
                Experiencia(
                    id = "3",
                    titulo = "Feria artesanal",
                    ubicacion = "Lima",
                    duracion = "2 h",
                    rating = 4.5,
                    precioDesde = 30
                ),
                Experiencia(
                    id = "4",
                    titulo = "Ruta rural",
                    ubicacion = "Lima",
                    duracion = "3 h",
                    rating = 4.6,
                    precioDesde = 40
                ),
                Experiencia(
                    id = "5",
                    titulo = "Tour gastronómico",
                    ubicacion = "Barranco, Lima",
                    duracion = "4 h",
                    rating = 4.8,
                    precioDesde = 80
                ),
                Experiencia(
                    id = "6",
                    titulo = "Paseo en bicicleta",
                    ubicacion = "Miraflores, Lima",
                    duracion = "2 h",
                    rating = 4.4,
                    precioDesde = 25
                ),
                Experiencia(
                    id = "7",
                    titulo = "Caminata al atardecer",
                    ubicacion = "Costa Verde, Lima",
                    duracion = "1 h",
                    rating = 4.7,
                    precioDesde = 20
                ),
                Experiencia(
                    id = "8",
                    titulo = "Visita a huaca",
                    ubicacion = "San Isidro, Lima",
                    duracion = "1 h",
                    rating = 4.3,
                    precioDesde = 15
                )
            )
        )
    }
}
