package kabinet.model

import kotlinx.serialization.Serializable

@Serializable
data class SpeechGenRequest(
    val text: String,
    val theme: String? = null,
    val voice: SpeechVoice? = null,
    val filename: String? = null,
)

enum class SpeechVoice(
    val apiName: String
) {
    Alloy("af_alloy"),
    Aoede("af_aoede"),
    Bella("af_bella"),
    Heart("af_heart"),
    Jessica("af_jessica"),
    Kore("af_kore"),
    Nicole("af_nicole"),
    Nova("af_nova"),
    River("af_river"),
    Sarah("af_sarah"),
    Sky("af_sky"),
    Adam("am_adam"),
    Echo("am_echo"),
    Eric("am_eric"),
    Fenrir("am_fenrir"),
    Liam("am_liam"),
    Michael("am_michael"),
    Onyx("am_onyx"),
    Puck("am_puck"),
    Santa("am_santa"),
    Alice("bf_alice"),
    Emma("bf_emma"),
    Isabella("bf_isabella"),
    Lily("bf_lily"),
    Daniel("bm_daniel"),
    Fable("bm_fable"),
    George("bm_george"),
    Lewis("bm_lewis");

    companion object {
        fun getByIntrinsicIndex(intrinsicIndex: Int) = SpeechVoice.entries[intrinsicIndex % SpeechVoice.entries.size]
    }
}

enum class GeminiVoice(
    val apiName: String
) {
    Bright("Zephyr"),
    Upbeat("Puck"),
    Informative("Charon"),
    Firm("Kore"),
    Excitable("Fenrir"),
    Youthful("Leda"),
    Breezy("Aoede"),
    EasyGoing("Callirrhoe"),
    Smooth("Algieba"),
    Breathy("Enceladus"),
    Clear("Iapetus"),
    Gravelly("Algenib"),
    Informative2("Rasalgethi"),
    Upbeat2("Laomedeia"),
    Soft("Achernar"),
    Firm2("Alnilam"),
    Even("Schedar"),
    Mature("Gacrux"),
    Forward("Pulcherrima"),
    Friendly("Achird"),
    Casual("Zubenelgenubi"),
    Gentle("Vindemiatrix"),
    Lively("Sadachbia"),
    Knowledgeable("Sadaltager"),
    Warm("Sulafat");

    companion object {
        fun getByIntrinsicIndex(intrinsicIndex: Int) = entries[intrinsicIndex % entries.size]
    }
}