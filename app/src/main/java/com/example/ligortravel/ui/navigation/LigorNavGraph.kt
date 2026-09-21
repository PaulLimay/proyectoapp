package com.example.ligortravel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ligortravel.ui.auth.AuthScreen
import com.example.ligortravel.ui.home.HomeScreen
import com.example.ligortravel.ui.perfil.PerfilScreen
import com.example.ligortravel.ui.splash.SplashScreen

object Rutas {
    const val SPLASH = "splash"
    const val AUTH = "auth"
    const val HOME = "home"
    const val PERFIL = "perfil"
}

@Composable
fun LigorNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Rutas.SPLASH) {

        composable(Rutas.SPLASH) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Rutas.AUTH) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.AUTH) {
            AuthScreen(
                onAutenticado = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.AUTH) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.HOME) {
            HomeScreen(
                onIrAPerfil = {
                    navController.navigate(Rutas.PERFIL)
                }
            )
        }

        composable(Rutas.PERFIL) {
            PerfilScreen(
                onCerrarSesion = {
                    navController.navigate(Rutas.AUTH) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
