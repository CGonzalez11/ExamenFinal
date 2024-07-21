package cl.cgonzalez.iplacex.viajero.database

data class Lugar(
    val id: Int,
    val lugar: String,
    val imageUrl: String,
    val latLong: String,
    val orden: String,
    val alojamiento: String,
    val traslado: String,
    val comentarios: String
) {

}

