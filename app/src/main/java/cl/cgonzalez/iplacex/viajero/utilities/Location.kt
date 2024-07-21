package cl.cgonzalez.iplacex.viajero.utilities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import cl.cgonzalez.iplacex.viajero.viewmodel.FormRecepcionViewModel

fun permisoLocalizacion(context: Context): Boolean {
    val permisoFineLocation = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val permisoCoarseLocation = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return permisoFineLocation && permisoCoarseLocation
}

fun obtenerUbicacionActual(context: Context, formRecepcionVm: FormRecepcionViewModel) {
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    if (permisoLocalizacion(context)) {
        if (ActivityCompat.checkSelfPermission
                (context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission
                (context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                formRecepcionVm.ubicacion.value = location
            }
    }
}

fun solicitarPermisoUbicacion(activity: ComponentActivity) {
    val permisos = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val requestCode = 123

    requestPermissions(activity, permisos, requestCode)
}
