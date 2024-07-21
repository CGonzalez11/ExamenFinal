package cl.cgonzalez.iplacex.viajero.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(contexto: Context) : SQLiteOpenHelper(contexto, DB_LUGARES, null, DB_VERSION) {

    companion object{
        const val DB_LUGARES = "lugares.db"
        const val DB_VERSION = 1
        const val SQL_CREACION_TABLAS = """
            CREATE TABLE ${DBLugares.TablaLugares.TABLA_LISTA_LUGARES}(
            ${DBLugares.TablaLugares.COLUMNA_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${DBLugares.TablaLugares.COLUMNA_LUGAR} TEXT,
            ${DBLugares.TablaLugares.COLUMNA_IMG_URL} TEXT,
            ${DBLugares.TablaLugares.COLUMNA_LAT_LONG} TEXT,
            ${DBLugares.TablaLugares.COLUMNA_ORDEN} TEXT,
            ${DBLugares.TablaLugares.COLUMNA_ALOJAMIENTO} FLOAT,
            ${DBLugares.TablaLugares.COLUMNA_TRASLADO} FLOAT,
            ${DBLugares.TablaLugares.COLUMNA_COMENTARIOS} TEXT
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREACION_TABLAS)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}