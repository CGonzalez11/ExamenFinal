package cl.cgonzalez.iplacex.viajero.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import cl.cgonzalez.iplacex.viajero.database.DBHelper
import cl.cgonzalez.iplacex.viajero.database.Lugar
import cl.cgonzalez.iplacex.viajero.database.LugaresDao
import cl.cgonzalez.iplacex.viajero.ws.CambioAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun DetailScreen(navController: NavController, lugarId: String, formRecepcionVm: ViewModel) {
    val contexto = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val (lugares, setLugar) = remember { mutableStateOf<Lugar?>(null) }
    var cambioDolarAlojamiento by remember { mutableStateOf(0.0) }
    var cambioDolarTraslado by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val dbHelper = DBHelper(contexto)
            val dao = LugaresDao(dbHelper)
            setLugar(dao.findById(lugarId))
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        lugares?.let {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {

                SubcomposeAsyncImage(
                    model = it.imageUrl,
                    contentDescription = "Img Url",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    text = it.lugar,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LaunchedEffect(it.alojamiento) {
                        coroutineScope.launch(Dispatchers.IO) {
                            try {
                                val tiposCambio = CambioAPI.service.getCambioDolar().resultado
                                cambioDolarAlojamiento = it.alojamiento.toDoubleOrNull()
                                    ?.let { it / tiposCambio.firstOrNull()?.precio!! } ?: 0.0
                            } catch (e: Exception) {
                                Log.e("CoctelAPI", e.message ?: "")
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Costo x Noche:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "CLP: ${it.alojamiento}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "USD: ${"%.2f".format(cambioDolarAlojamiento)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    LaunchedEffect(it.traslado) {
                        coroutineScope.launch(Dispatchers.IO) {
                            try {
                                val tiposCambio = CambioAPI.service.getCambioDolar().resultado
                                cambioDolarTraslado = it.traslado.toDoubleOrNull()
                                    ?.let { it / tiposCambio.firstOrNull()?.precio!! } ?: 0.0
                            } catch (e: Exception) {
                                Log.e("CoctelAPI", e.message ?: "")
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Traslado:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "CLP: ${it.traslado}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "USD: ${"%.2f".format(cambioDolarTraslado)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Text(
                    text = "Comentarios",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = it.comentarios,
                    style = MaterialTheme.typography.bodySmall
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Button(onClick = {
                            navController.navigate("camera_screen")
                        }) {
                            Text("Tomar Foto")
                        }
                    }

                    Column {
                        Button(onClick = {
                        }) {
                            Text("Ver Fotos")
                        }
                    }
                }
            }
        }
    }
}

