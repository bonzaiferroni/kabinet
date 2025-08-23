package kabinet.console

class LogTableBuffer<T>(
    private val table: LogTable<T>,
    val provideKey: (T) -> String
) {
    private val items = mutableMapOf<String, T>()

    fun logItem(console: LogHandle, item: T) {
        val key = provideKey(item)
        if (items.contains(key)) {
            console.logTable(table, items.values)
            items.clear()
        }
        items[key] = item
    }
}