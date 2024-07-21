package cl.cgonzalez.iplacex.viajero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.cgonzalez.iplacex.viajero.database.DBHelper
import cl.cgonzalez.iplacex.viajero.screen.CameraScreen
import cl.cgonzalez.iplacex.viajero.screen.DetailScreen
import cl.cgonzalez.iplacex.viajero.screen.FormScreen
import cl.cgonzalez.iplacex.viajero.screen.HomeScreen
import cl.cgonzalez.iplacex.viajero.viewmodel.FormRecepcionViewModel


class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHelper = DBHelper(this)
        setContent {
            Navigation(dbHelper)
        }
    }


    @Composable
    fun Navigation(dbHelper: DBHelper) {
        val navController = rememberNavController()
        val formRecepcionVm: FormRecepcionViewModel by viewModels()

        NavHost(navController = navController, startDestination = "home_screen") {
            composable("home_screen") {
                HomeScreen(navController)
            }
            composable("form_screen") {
                FormScreen(navController)
            }
            composable("detail_screen/{lugarId}") { backStackEntry ->
                val lugarId = backStackEntry.arguments?.getString("lugarId")
                if (lugarId != null) {
                    DetailScreen(navController, lugarId, formRecepcionVm)
                }
            }
            composable("camera_screen") { backStackEntry ->
                val lugarId = backStackEntry.arguments?.getString("lugarId")
                if (lugarId != null) {
                    CameraScreen(navController)
                }
            }
        }
    }
}



