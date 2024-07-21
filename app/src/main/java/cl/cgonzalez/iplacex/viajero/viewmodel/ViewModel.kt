package cl.cgonzalez.iplacex.viajero.viewmodel

import android.location.Location
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FormRecepcionViewModel : ViewModel() {
    val nombreImagen = mutableStateOf("")
    val fotos = mutableStateListOf<Uri?>()
    val ubicacion = mutableStateOf<Location?>(null)

}