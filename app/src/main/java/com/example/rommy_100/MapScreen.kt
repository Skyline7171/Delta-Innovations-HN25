package com.example.rommy_100

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun OSMMapView(
    modifier: Modifier,
    // Coordenadas de Managua
    latitude: Double = 12.1364,
    longitude: Double = -86.2514,
    zoom: Double = 12.0
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                // Configuración de la fuente de los mosaicos
                setTileSource(TileSourceFactory.MAPNIK)

                // Habilita controles de zoom y multitoque
                setBuiltInZoomControls(true)
                setMultiTouchControls(true)

                // Establece la vista inicial
                val mapController = controller
                mapController.setZoom(zoom)
                mapController.setCenter(GeoPoint(latitude, longitude))
            }
        },
        // Aquí es posible poner código para actualizar la vista si alguna propiedad del Composable cambia
        update = { mapView ->
            // Por ejemplo, si las coordenadas cambian, actualiza el centro
            mapView.controller.setCenter(GeoPoint(latitude, longitude))
        }
    )
}