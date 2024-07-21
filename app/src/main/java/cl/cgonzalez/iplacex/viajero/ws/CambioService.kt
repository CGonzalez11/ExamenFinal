package cl.cgonzalez.iplacex.viajero.ws

import retrofit2.http.GET

interface CambioService {

    //https://www.mindicador.cl/api/dolar
    @GET("api/dolar")
    suspend fun getCambioDolar(): ListaCambio

}