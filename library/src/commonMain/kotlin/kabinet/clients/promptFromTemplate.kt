package kabinet.clients

fun promptFromTemplate(
    template: String,
    vararg items: Pair<String, String>
): String {
    var text = template
    items.forEach { (key, value) ->
        val regex = Regex("<\\|${Regex.escape(key)}\\|>")
        text = regex.replace(text, value)
    }
    return text
}