package kabinet.console

class LogTable<T>(
    vararg val columns: LogColumn<T>,
    val color: LogColor = LogColor.Gray
)

data class LogColumn<T>(
    val label: String,
    val width: Int? = null,
    val color: LogColor? = null,
    val justify: LogJustify = LogJustify.Left,
    val provideValue: (T) -> Any?
) {
    fun getValue(item: T) = provideValue(item)?.toString() ?: nullChar
}

fun <T> LogHandle.logTable(table: LogTable<T>, items: Collection<T>) {
    val widths = table.columns.map { column ->
        column.width ?: maxOf(items.maxOf { column.getValue(it).length }, column.label.length)
    }

    table.columns.forEachIndexed { index, column ->
        cell(value = column.label, justify = column.justify, color = table.color, width = widths[index])
    }
    send(background = LogColor.Black)

    for (item in items) {
        table.columns.forEachIndexed { index, column ->
            cell(
                value = column.getValue(item),
                width = widths[index],
                color = column.color,
                justify = column.justify,
            )
        }
        send(background = table.color)
    }
}

private const val nullChar = "✖"