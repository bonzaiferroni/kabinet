package kabinet.gemini

import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kabinet.console.LogLevel
import kampfire.model.GeminiVoice

suspend fun GeminiClient.generateSpeech(
    text: String,
    theme: String?,
    voice: String?,
): String? {
    val response = tryRequest {
        val request = GeminiRequest(
            contents = listOf(GeminiContent(
                parts = listOf(
                    GeminiPart(
                        text = "${theme ?: "Say"}:\n$text"
                    ))
            )),
            generationConfig = GenerationConfig(
                responseModalities = listOf("AUDIO"),
                speechConfig = SpeechConfig(
                    voiceConfig = VoiceConfig(
                        prebuiltVoiceConfig = PrebuiltVoiceConfig(
                            voiceName = voice ?: GeminiVoice.Upbeat.apiName
                        )
                    )
                )
            )
        )
        GeminiApiRequest("generateContent", request, "gemini-2.5-pro-preview-tts")
    }

    if (response?.status == HttpStatusCode.OK) {
        val geminiResponse = response.body<GeminiResponse>()
        val data = geminiResponse.candidates.firstOrNull()?.content?.parts
            ?.firstOrNull() { it.inlineData != null}?.inlineData?.data
        if (data == null) {
            logMessage(LogLevel.Error, "Missing data in speech generation:\ntext: ${text.take(50)}\n${geminiResponse}")
        }
        return data
    } else {
        return null
    }
}

// gemini-2.5-flash-preview-tts
// gemini-2.5-pro-preview-tts
// gemini-2.5-flash-preview-native-audio-dialog
// gemini-2.5-flash-native-audio-preview-09-2025
// gemini-2.5-flash-exp-native-audio-thinking-dialog