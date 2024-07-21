package cl.cgonzalez.iplacex.viajero.database

import android.content.ContentValues
import cl.cgonzalez.iplacex.viajero.database.DBLugares.TablaLugares

class LugaresDao(val db: DBHelper) {

    fun finAll():List<Lugar>{
        val cursor = db.readableDatabase.query(
            TablaLugares.TABLA_LISTA_LUGARES,
            null,
            "",
            null,
            null,
            null,
            null
        )
        val lista = mutableListOf<Lugar>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_ID) )

            val lugar = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_LUGAR) )

            val imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_IMG_URL) )

            val latLong = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_LAT_LONG) )

            val orden= cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_ORDEN) )

            val alojamiento = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_ALOJAMIENTO) )

            val traslado = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_TRASLADO) )

            val comentarios = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_COMENTARIOS) )


            val sitios
            = Lugar(id, lugar, imageUrl, latLong ,orden, alojamiento, traslado, comentarios)
            lista.add(sitios)
        }
        cursor.close()
        return lista
    }

    fun findById(id: String): Lugar? {
        val db = db.readableDatabase
        val cursor = db.query(
            TablaLugares.TABLA_LISTA_LUGARES,
            null,
            "${TablaLugares.COLUMNA_ID} = ?",
            arrayOf(id),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_ID))
            val lugar = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_LUGAR))
            val imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_IMG_URL))
            val latLong = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_LAT_LONG))
            val orden = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_ORDEN))
            val alojamiento = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_ALOJAMIENTO))
            val traslado = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_TRASLADO))
            val comentarios = cursor.getString(cursor.getColumnIndexOrThrow(TablaLugares.COLUMNA_COMENTARIOS))


            val lugarId = Lugar(id, lugar, imageUrl, latLong, orden, alojamiento, traslado, comentarios)

            return lugarId
        } else {
            return null
        }
        cursor.close()
    }



    fun insertar(sitio: Lugar): Long {
        val valores = ContentValues().apply {
            put(TablaLugares.COLUMNA_LUGAR, sitio.lugar)
            put(TablaLugares.COLUMNA_IMG_URL, sitio.imageUrl)
            put(TablaLugares.COLUMNA_LAT_LONG, sitio.latLong)
            put(TablaLugares.COLUMNA_ORDEN, sitio.orden)
            put(TablaLugares.COLUMNA_ALOJAMIENTO, sitio.alojamiento)
            put(TablaLugares.COLUMNA_TRASLADO, sitio.traslado)
            put(TablaLugares.COLUMNA_COMENTARIOS, sitio.comentarios)
        }
        return db.writableDatabase.insert(TablaLugares.TABLA_LISTA_LUGARES, null, valores)
    }

    fun eliminarLugar(id: Int): Int {
        val resultado = db.writableDatabase.delete(
            TablaLugares.TABLA_LISTA_LUGARES,
            "${TablaLugares.COLUMNA_ID} = ?",
            arrayOf(id.toString())
        )
        return resultado
    }

}
