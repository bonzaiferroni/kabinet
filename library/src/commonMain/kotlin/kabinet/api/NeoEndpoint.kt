package kabinet.api

import io.ktor.http.HttpMethod
import kotlinx.datetime.Instant

open class NeoEndpoint<Received>(
    val method: HttpMethod,
    val parent: NeoEndpoint<*>?,
    val pathNode: String
) {
    val path: List<String> = createPath()

    private fun createPath(): MutableList<String> = (parent?.createPath() ?: mutableListOf()).also { it.add(pathNode) }

    fun <T> addParam(key: String, toValue: (String) -> T, toString: (T) -> String, ) =
        EndpointParam<T>(key, toValue, toString)

    fun addLongParam(key: String) = EndpointParam(
        key = key,
        toValue = { it.toLong() },
        toString = { it.toString() }
    )

    fun addIntParam(key: String) = EndpointParam(
        key = key,
        toValue = { it.toInt() },
        toString = { it.toString() }
    )

    fun addInstantParam(key: String) = EndpointParam(
        key = key,
        toValue = { Instant.fromEpochSeconds(it.toLong())},
        toString = { it.epochSeconds.toString() }
    )

    fun addStringParam(key: String) = EndpointParam(
        key = key,
        toValue = { it },
        toString = { it }
    )

    fun addBooleanParam(key: String) = EndpointParam(
        key = key,
        toValue = { it.toBoolean() },
        toString = { it.toString() }
    )

    fun addIntList(key: String) = addListParam(key, { it.toInt()}, { it.toString() })

    fun <T> addListParam(key: String, toValue: (String) -> T, toString: (T) -> String) = EndpointParam<Collection<T>>(
        key = key,
        toValue = { it.takeIf { it.isNotEmpty() }?. split(",")?.map { toValue(it)} ?: emptyList() },
        toString = { it.joinToString(",") { toString(it)} }
    )
}