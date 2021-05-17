package spaceEngineers.model

data class ToolbarItem
    (
    val type: String,
    val subType: String,
    val name: String
) {

    override fun toString(): String {
        return name
    }
}