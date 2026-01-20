package kabinet.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoPoint(
    val lon: Double,
    val lat: Double,
) {
    val x get() = lon
    val y get() = lat

    fun toList(): List<Double> = listOf(lon, lat)
    fun toArray(): Array<Double> = arrayOf(lon, lat)
}