package com.example.ligortravel.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// Paleta oscura/naranja usada por las pantallas que ya siguen el estilo elegido (Bocetos/1c.png
// y los mockups de dise-os-ligor), mientras el tema global del proyecto (Theme.kt/Color.kt)
// todavía no se actualiza a este estilo.
val LigorDarkColorScheme = darkColorScheme(
    primary = Color(0xFFF5A94B),
    onPrimary = Color(0xFF1A1208),
    background = Color(0xFF141414),
    onBackground = Color(0xFFF2F2F2),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFF2F2F2),
    onSurfaceVariant = Color(0xFFAFAFAF),
    error = Color(0xFFEF5350)
)
