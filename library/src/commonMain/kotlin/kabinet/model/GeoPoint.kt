package kabinet.model

import kotlinx.serialization.Serializable
import kotlin.math.*

@Serializable
data class GeoPoint(
    val lon: Double,
    val lat: Double,
) {
    val x get() = lon
    val y get() = lat

    fun toList(): List<Double> = listOf(lon, lat)
    fun toArray(): Array<Double> = arrayOf(lon, lat)

    fun distanceTo(other: GeoPoint): Double {
        val earthRadiusMeters = 6_371_000.0
        val degToRad = PI / 180.0

        val lat1 = lat * degToRad
        val lat2 = other.lat * degToRad
        val dLat = (other.lat - lat) * degToRad
        val dLon = (other.lon - lon) * degToRad

        val a =
            sin(dLat / 2).pow(2) +
                    cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadiusMeters * c
    }

    fun toQuery() = "lng=$lon&lat=$lat"

    companion object {
        val Denver = GeoPoint(-104.95, 39.75)

        fun fromQuery(parameters: Map<String, List<String>>) = parameters.let {
            val lng = parameters["lng"]?.firstOrNull()?.toDoubleOrNull() ?: return@let null
            val lat = parameters["lat"]?.firstOrNull()?.toDoubleOrNull() ?: return@let null
            GeoPoint(lng, lat)
        }
    }
}