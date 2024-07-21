package cl.cgonzalez.iplacex.viajero.ws

import com.squareup.moshi.Json

data class ListaCambio(
    @Json(name = "serie")
    val resultado:List<Cambio>
)

data class Cambio(
    @Json(name = "fecha")
    val ultimaFecha: String = "",
    @Json(name = "valor")
    val precio: Double = 0.0
)
