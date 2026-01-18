package kabinet.gemini

//import com.google.genai.Client
//
//
//class NeoGeminiClient(apiKey: String) {
//
//    val client: Client? = Client.builder().apiKey(apiKey).build()
//
//    fun textChat(prompt: String): String? {
//        val response = client!!.models.generateContent("gemini-2.5-flash", prompt, null)
//        return response.text()
//    }
//}