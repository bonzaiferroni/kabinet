package kabinet.model

interface LabeledEnum<T: Enum<T>> {
    val label: String
}