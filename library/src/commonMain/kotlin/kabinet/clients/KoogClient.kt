package kabinet.clients

import ai.koog.embeddings.base.Embedder
import ai.koog.embeddings.base.Vector
import ai.koog.embeddings.local.LLMEmbedder
import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.llm.LLModel

class KoogClient {

    private val embedders: MutableMap<LLModel, Embedder> = mutableMapOf()

    fun initEmbedder(model: LLModel, embedder: Embedder) {
        embedders[model] = embedder
    }

    suspend fun embed(text: String, model: LLModel): FloatArray {
        val embedder = embedders[model] ?: error("embedder not found: $model")
        return embedder.embed(text).toFloatArray()
    }
}

fun Vector.toFloatArray() = FloatArray(values.size) { i -> values[i].toFloat() }

fun KoogClient.addOpenAiEmbedder(token: String, model: LLModel = OpenAIModels.Embeddings.TextEmbedding3Small) {
    val client = OpenAILLMClient(token)
    val embedder = LLMEmbedder(client, model)
    initEmbedder(model, embedder)
}


