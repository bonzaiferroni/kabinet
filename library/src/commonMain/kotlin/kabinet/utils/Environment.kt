package kabinet.utils

interface Environment {
    fun read(key: String): String

    companion object {
        fun fromText(text: String): Environment = MapEnvironment(text)
    }
}

internal class MapEnvironment(content: String): Environment {
    private val map = mutableMapOf<String, String>()

    init {
        val lines = content.split('\n')
        for (line in lines) {
            val values = line.trim().split('=')
            if (values.size != 2) continue
            map[values[0]] = values[1]
        }
    }

    override fun read(key: String) = map.getValue(key)
}