package kabinet.web

import kotlinx.serialization.Serializable
import kotlin.text.contains

@Serializable
data class Url(
    val href: String,
    val core: String,
    val scheme: String,
    val hostAddress: String,
    val hostSegmentCount: Int,
    val path: String,
    val fullPath: String,
    val pathSegments: Int,
    val params: Map<String, String>,
    val paramPath: String,
    val fragment: String?,
    val isRobotAllowed: Boolean?,
) {
    val authority get() = "$scheme://$hostAddress"
    val length get() = toString().length

    override fun toString() = href
    override fun equals(other: Any?) = other is Url && this.toString() == other.toString()
    override fun hashCode(): Int {
        var result = href.hashCode()
        result = 31 * result + href.hashCode()
        return result
    }

    fun isMaybeSibling(other: Url): Boolean {
        val split1 = path.split('/')
        val root1 = split1.getOrNull(1) ?: return false
        val split2 = other.path.split('/')
        val root2 = split2.getOrNull(1) ?: return false
        // first segment same length or both have 1 segment
        return root1.length == root2.length || pathSegments == 1 && other.pathSegments == 1
    }
}

internal fun <T : Url> tryParseUrl(block: () -> T): T? {
    return try {
        block()
    } catch (e: IllegalArgumentException) {
        null
    }
}

private fun String.deconstruct(delimiter: String): Pair<String, String?> =
    this.split(delimiter, limit = 2).let { Pair(it[0], it.getOrNull(1)) }

internal fun String.appendIfRelativeHref(context: Url) = if (this.contains("://")) {
    this
} else {
    "${context.authority}${if (this.startsWith("/")) "" else "/"}${this}"
}

fun String.toUrl() = Url.fromHref(this, null, null)

fun String.toUrlWithContext(context: Url) = this.appendIfRelativeHref(context).toUrl()

fun String.toUrlOrNull(): Url? = tryParseUrl { this.toUrl() }

fun String.toUrlWithContextOrNull(context: Url): Url? =
    tryParseUrl { this.appendIfRelativeHref(context).toUrl() }

fun Url.Companion.fromHrefOrNull(href: String) = tryParseUrl { fromHref(href) }

fun Url.Companion.fromHref(
    rawHref: String,
    junkParams: Set<String>? = null,
    disallowedPaths: Set<String>? = null,
): Url {
    if (!rawHref.startsWith("http"))
        throw IllegalArgumentException("Url must begin with http: $rawHref")
    val (beforeFragmentMarker, afterFragmentMarker) = rawHref.deconstruct("#")
    val fragment = afterFragmentMarker
    val (beforeScheme, afterScheme) = beforeFragmentMarker.deconstruct("://")
    requireNotNull(afterScheme) { "Invalid Url: $rawHref" }
    val scheme = beforeScheme

    val (beforePathMarker, afterPathMarker) = afterScheme.deconstruct("/")
    if (beforePathMarker.contains('@')) throw IllegalArgumentException("URL contains user info: $rawHref")
    val hostAddress = beforePathMarker.lowercase()
    if (hostAddress.length > 100) throw IllegalArgumentException("Domain too long: $hostAddress")
    if (hostAddress.contains(":")) throw IllegalArgumentException("No ports allowed here sir")
    val core = hostAddress.removePrefix("www.")
    val hostSegmentCount = core.split('.').size
    if (hostSegmentCount < 2) throw IllegalArgumentException("Invalid domain: $hostAddress")
    val rawPath = afterPathMarker?.let { "/$it" } ?: "/"

    val (beforeParamMarker, afterParamMarker) = rawPath.deconstruct("?")
    val path = beforeParamMarker
    val pathSegments = path.split('/').filter { it.isNotEmpty() }.size
    var requiredParamPath = ""

    val params = afterParamMarker?.split("&")?.mapNotNull { param ->
        val (key, value) = param.deconstruct("=")
        if (value == null) {
            if (requiredParamPath.isEmpty() && !afterParamMarker.contains('&')) {
                requiredParamPath += "?$key" // some sites use ? like a fragment marker (c-span)
            }
            return@mapNotNull null
        }
        if (key.startsWith("utm_") || junkParams != null && key in junkParams) return@mapNotNull null

        requiredParamPath += if (requiredParamPath.isEmpty()) "?" else "&"
        requiredParamPath += param
        Pair(key, value)
    }?.toMap() ?: emptyMap()
    val paramPath = requiredParamPath
    val fullPath = "$path$paramPath${fragment?.let { "#$it" } ?: ""}"
    val authority = "$scheme://$hostAddress"
    val href = "$authority$fullPath"
    val isRobotAllowed = disallowedPaths?.all { !path.startsWith(it) }
    return Url(
        href = href,
        core = core,
        scheme = scheme,
        hostAddress = hostAddress,
        hostSegmentCount = hostSegmentCount,
        path = path,
        fullPath = fullPath,
        pathSegments = pathSegments,
        params = params,
        paramPath = paramPath,
        fragment = fragment,
        isRobotAllowed = isRobotAllowed,
    )
}