package cl.cgonzalez.iplacex.viajero.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import cl.cgonzalez.iplacex.viajero.R
import cl.cgonzalez.iplacex.viajero.database.DBHelper
import cl.cgonzalez.iplacex.viajero.database.Lugar
import cl.cgonzalez.iplacex.viajero.database.LugaresDao
import cl.cgonzalez.iplacex.viajero.ws.CambioAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val contexto = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val (lugares, setLugares) = remember { mutableStateOf(listOf<Lugar>()) }
    val cambioDolarAlojamientoList = remember { mutableStateListOf<Double>() }
    val cambioDolarTrasladoList = remember { mutableStateListOf<Double>() }

    for (i in lugares.indices) {
        cambioDolarAlojamientoList.add(0.0)
        cambioDolarTrasladoList.add(0.0)
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val dbHelper = DBHelper(contexto)
            var dao = LugaresDao(dbHelper)

            setLugares(dao.finAll())
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("form_screen") },
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Add, contentDescription = "Agregar")
                        Text(text = " Agregar Lugar ")
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(lugares) { index, lugar ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(3f)
                                    .padding(10.dp)
                            ) {
                                SubcomposeAsyncImage(
                                    model = lugar.imageUrl,
                                    contentDescription = "Img Url",
                                ) {
                                    when (painter.state) {
                                        is AsyncImagePainter.State.Loading -> CircularProgressIndicator()
                                        is AsyncImagePainter.State.Empty -> Image(
                                            painterResource(R.drawable.image_not_found),
                                            contentDescription = "Not Found"
                                        )

                                        is AsyncImagePainter.State.Error -> Image(
                                            painterResource(R.drawable.error_icon),
                                            contentDescription = "Error"
                                        )

                                        else -> SubcomposeAsyncImageContent()
                                    }
                                }

                            }

                            Column(
                                modifier = Modifier.weight(2f)
                            ) {
                                LaunchedEffect(lugar.alojamiento) {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        try {
                                            val tiposCambio =
                                                CambioAPI.service.getCambioDolar().resultado
                                            cambioDolarAlojamientoList[index] =
                                                lugar.alojamiento.toDoubleOrNull()
                                                    ?.let { it / tiposCambio.firstOrNull()?.precio!! }
                                                    ?: 0.0
                                        } catch (e: Exception) {
                                            Log.e("CambioAPI", e.message ?: "")
                                        }
                                    }
                                }
                                LaunchedEffect(lugar.traslado) {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        try {
                                            val tiposCambio =
                                                CambioAPI.service.getCambioDolar().resultado
                                            cambioDolarTrasladoList[index] =
                                                lugar.traslado.toDoubleOrNull()
                                                    ?.let { it / tiposCambio.firstOrNull()?.precio!! }
                                                    ?: 0.0
                                        } catch (e: Exception) {
                                            Log.e("CambioAPI", e.message ?: "")
                                        }
                                    }
                                }
                                Text(
                                    text = lugar.lugar,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.padding(5.dp))

                                Text(
                                    text = "Costo x Noche:\n" +
                                            "CLP: " + lugar.alojamiento + " USD: ${
                                        "%.2f".format(
                                            cambioDolarAlojamientoList[index]
                                        )
                                    }",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                                Spacer(modifier = Modifier.padding(5.dp))


                                Text(
                                    text = "Traslado:\n" +
                                            "CLP: " + lugar.traslado + " USD: ${
                                        "%.2f".format(
                                            cambioDolarTrasladoList[index]
                                        )
                                    }",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                                Spacer(modifier = Modifier.padding(5.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    IconButton(onClick = {
                                        navController.navigate("detail_screen/${lugar.id}")
                                    }) {
                                        Icon(Icons.Default.LocationOn,
                                            contentDescription = "Ver Detalles",
                                            tint = Color.Gray
                                        )
                                    }

                                    IconButton(onClick = {
                                    }) {
                                        Icon(Icons.Default.AccountBox,
                                            contentDescription = "Lista Fotos",
                                            tint = Color.Gray
                                        )
                                    }

                                    IconButton(onClick = {
                                        coroutineScope.launch(Dispatchers.IO) {
                                            val dbHelper = DBHelper(contexto)
                                            val dao = LugaresDao(dbHelper)
                                            dao.eliminarLugar(lugar.id)

                                            setLugares(dao.finAll())
                                        }
                                    }) {
                                        Icon(Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

