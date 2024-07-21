package cl.cgonzalez.iplacex.viajero.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.cgonzalez.iplacex.viajero.database.DBHelper
import cl.cgonzalez.iplacex.viajero.database.Lugar
import cl.cgonzalez.iplacex.viajero.database.LugaresDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(navController: NavController) {
    val contexto = LocalContext.current
    var lugar       by remember { mutableStateOf("") }
    var imageUrl    by remember { mutableStateOf("") }
    var latLong     by remember { mutableStateOf("") }
    var orden       by remember { mutableStateOf("") }
    var alojamiento by remember { mutableStateOf("") }
    var traslado    by remember { mutableStateOf("") }
    var comentarios by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Text(
            text = "Lugar",
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            value = lugar,
            onValueChange = { lugar = it },
            label = { Text("Ej. Valle Grande") },
            modifier = Modifier.fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.padding(2.dp))
// ---------->
        Text(
            text = "Img. Ref.",
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("www.img.cl/img.jpg") },
            modifier = Modifier.fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.padding(2.dp))
// ---------->
        Text(
            text = "Lat - Long",
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            value = latLong,
            onValueChange = { latLong = it },
            modifier = Modifier.fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.padding(2.dp))
// ---------->
        Text(
            text = "Orden",
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            value = orden,
            onValueChange = { orden = it },
            modifier = Modifier.fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.padding(2.dp))
// ---------->
        Text(
            text = "Valor Alojamiento",
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            value = alojamiento,
            onValueChange = { alojamiento = it },
            modifier = Modifier.fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.padding(2.dp))
// ---------->
        Text(
            text = "Valor Traslado",
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            value = traslado,
            onValueChange = { traslado = it },
            modifier = Modifier.fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.padding(2.dp))
// ---------->
        Text(
            text = "Comentarios",
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            value = comentarios,
            onValueChange = { comentarios = it },
                    modifier = Modifier.fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.padding(2.dp))

// ---------->
        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    val dbHelper = DBHelper(contexto)
                    val dao = LugaresDao(dbHelper)
                    val nuevoLugar =
                        Lugar(
                            0,
                            lugar,
                            imageUrl,
                            latLong,
                            orden,
                            alojamiento,
                            traslado,
                            comentarios)
                    dao.insertar(nuevoLugar)

                    withContext(Dispatchers.Main) {

                        navController.navigate("home_screen")
                    }
                }
            }
        ) {
            Text("Guardar")
        }
    }
}
