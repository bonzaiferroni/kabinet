package kabinet.utils

import kotlin.math.sqrt

fun FloatArray.normalize(): FloatArray {
    val magnitude = sqrt(sumOf { (it * it).toDouble() })
    require(magnitude > 0) { "Cannot normalize a zero vector!" }
    return map { (it / magnitude).toFloat() }.toFloatArray()
}

fun averageAndNormalize(origin: FloatArray, target: FloatArray): FloatArray {
    require(origin.size == target.size) { "Vectors must be the same size" }
    val averaged = FloatArray(origin.size) { i -> (origin[i] + target[i]) / 2 }
    return averaged.normalize()
}

fun weightedAverageAndNormalize(
    origin: FloatArray,
    target: FloatArray,
    originWeight: Int,
    targetWeight: Int
): FloatArray {
    require(origin.size == target.size) { "Vectors must be the same size" }
    require(originWeight >= 0 && targetWeight >= 0) { "Weights must be non-negative" }

    // Compute weighted average
    val weightedAverage = FloatArray(origin.size) { i ->
        (origin[i] * originWeight + target[i] * targetWeight) / (originWeight + targetWeight)
    }

    // Normalize the result
    val magnitude = sqrt(weightedAverage.sumOf { (it * it).toDouble() })
    require(magnitude > 0) { "Cannot normalize a zero vector!" }
    return weightedAverage.map { (it / magnitude).toFloat() }.toFloatArray()
}

fun cosineDistance(origin: FloatArray, target: FloatArray): Float {
    require(origin.size == target.size) { "Vectors must be the same size" }

    // Calculate dot product
    val dotProduct = origin.zip(target).sumOf { (a, b) -> (a * b).toDouble() }

    // Calculate magnitudes
    val magnitude1 = sqrt(origin.sumOf { (it * it).toDouble() })
    val magnitude2 = sqrt(target.sumOf { (it * it).toDouble() })

    // Check for zero magnitudes to avoid division by zero
    require(magnitude1 > 0 && magnitude2 > 0) { "Cannot calculate cosine distance for zero vectors!" }

    return 1 - (dotProduct / (magnitude1 * magnitude2)).toFloat()
}

fun cosineDistances(origin: FloatArray, targets: List<FloatArray>) = targets.map { cosineDistance(origin, it) }

fun dotProduct(origin: FloatArray, target: FloatArray): Float {
    require(origin.size == target.size) { "Vectors must be the same size" }
    return origin.zip(target).sumOf { (a, b) -> (a * b).toDouble() }.toFloat()
}

fun distance(origin: FloatArray, target: FloatArray) = 1 - dotProduct(origin, target)

fun averageAndNormalize(vectors: List<FloatArray>): FloatArray {
    require(vectors.isNotEmpty()) { "The list of vectors cannot be empty" }
    val size = vectors.first().size
    require(vectors.all { it.size == size }) { "All vectors must be the same size" }

    // Calculate the element-wise average
    val averaged = FloatArray(size) { i ->
        vectors.sumOf { it[i].toDouble() }.toFloat() / vectors.size
    }

    // Normalize the averaged vector
    val magnitude = kotlin.math.sqrt(averaged.sumOf { (it * it).toDouble() }).toFloat()
    require(magnitude > 0) { "Cannot normalize a zero vector!" }
    return averaged.map { it / magnitude }.toFloatArray()
}