package com.example.ligortravel.ui.home

import androidx.lifecycle.ViewModel
import com.example.ligortravel.R
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
                    precioDesde = 150,
                    categoria = "Trekking",
                    imagenRes = R.drawable.laguna_69
                ),
                Experiencia(
                    id = "2",
                    titulo = "Amanecer en Vinicunca",
                    ubicacion = "Cusipata, Cusco",
                    duracion = "12 h",
                    rating = 4.7,
                    precioDesde = 190,
                    categoria = "Trekking",
                    imagenRes = R.drawable.vinicunca
                )
            ),
            locales = listOf(
                Experiencia(
                    id = "3",
                    titulo = "Machu Picchu",
                    ubicacion = "Cusco",
                    duracion = "1 día",
                    rating = 5.0,
                    precioDesde = 250,
                    categoria = "Historia",
                    imagenRes = R.drawable.machu_picchu
                ),
                Experiencia(
                    id = "4",
                    titulo = "Valle Sagrado",
                    ubicacion = "Cusco",
                    duracion = "1 día",
                    rating = 4.8,
                    precioDesde = 180,
                    categoria = "Cultura",
                    imagenRes = R.drawable.valle_sagrado
                ),
                Experiencia(
                    id = "5",
                    titulo = "Cusco esencial",
                    ubicacion = "Cusco",
                    duracion = "2 días",
                    rating = 4.7,
                    precioDesde = 220,
                    categoria = "Cultura",
                    imagenRes = R.drawable.cusco_esencial
                ),
                Experiencia(
                    id = "6",
                    titulo = "Cañón del Colca",
                    ubicacion = "Arequipa",
                    duracion = "2 días",
                    rating = 4.8,
                    precioDesde = 200,
                    categoria = "Naturaleza",
                    imagenRes = R.drawable.canon_colca
                ),
                Experiencia(
                    id = "7",
                    titulo = "Islas Ballestas",
                    ubicacion = "Paracas, Ica",
                    duracion = "3 h",
                    rating = 4.6,
                    precioDesde = 60,
                    categoria = "Naturaleza",
                    imagenRes = R.drawable.islas_ballestas
                ),
                Experiencia(
                    id = "8",
                    titulo = "Ruta del Pisco",
                    ubicacion = "Ica",
                    duracion = "4 h",
                    rating = 4.5,
                    precioDesde = 70,
                    categoria = "Gastronomía",
                    imagenRes = R.drawable.ruta_pisco
                ),
                Experiencia(
                    id = "9",
                    titulo = "Aventura en Lunahuaná",
                    ubicacion = "Cañete, Lima",
                    duracion = "1 día",
                    rating = 4.6,
                    precioDesde = 90,
                    categoria = "Aventura",
                    imagenRes = R.drawable.lunahuana
                ),
                Experiencia(
                    id = "10",
                    titulo = "Santuario de Pachacamac",
                    ubicacion = "Lurín, Lima",
                    duracion = "2 h",
                    rating = 4.4,
                    precioDesde = 25,
                    categoria = "Historia",
                    imagenRes = R.drawable.pachacamac
                ),
                Experiencia(
                    id = "11",
                    titulo = "Laguna Churup",
                    ubicacion = "Huaraz, Áncash",
                    duracion = "6 h",
                    rating = 4.7,
                    precioDesde = 80,
                    categoria = "Trekking",
                    imagenRes = R.drawable.laguna_churup
                ),
                Experiencia(
                    id = "12",
                    titulo = "Playas del Norte Chico",
                    ubicacion = "Huaral, Lima",
                    duracion = "1 día",
                    rating = 4.3,
                    precioDesde = 50,
                    imagenRes = R.drawable.norte_chico
                ),
                Experiencia(
                    id = "13",
                    titulo = "Pesca artesanal",
                    ubicacion = "Callao, Lima",
                    duracion = "3 h",
                    rating = 4.2,
                    precioDesde = 35,
                    categoria = "Vivencial",
                    imagenRes = R.drawable.pesca_artesanal
                ),
                Experiencia(
                    id = "14",
                    titulo = "Lima de noche",
                    ubicacion = "Miraflores, Lima",
                    duracion = "2 h",
                    rating = 4.5,
                    precioDesde = 40,
                    imagenRes = R.drawable.lima_nocturna
                ),
                Experiencia(
                    id = "15",
                    titulo = "Feria artesanal",
                    ubicacion = "Barranco, Lima",
                    duracion = "2 h",
                    rating = 4.5,
                    precioDesde = 30,
                    categoria = "Artesanía",
                    imagenRes = R.drawable.feria_artesanal
                )
            )
        )
    }
}
