package spaceEngineers.util.generator.python

import spaceEngineers.controller.SpaceEngineers
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.KType

fun main() {
    val types = DataStructuresGenerator().find().toSet()
    val clientGenerator = ClientGenerator(SpaceEngineers::class, types)

    val outFile = File("../PythonClient/src/spaceengineers/api.py")
    outFile.writeText(
        """from .models import *

        """.trimIndent()
    )
    outFile.appendText(clientGenerator.generateInterfaces())
}

val filteredMethods = setOf("toString", "equals", "hashCode")

val kotlinTypeToPython = mapOf(
    "String" to "str",
    "Int" to "int",
    "Float" to "float",
    "Double" to "float",
    "Map" to "dict",
    "Boolean" to "bool",
    "List" to "list",
    "Short" to "int",
    "Collection" to "list",
    "Byte" to "int",
    "Long" to "int",
    "Set" to "set",
)

val kotlinMethodToPython = mapOf(
    "continue" to "Continue"
)

fun KType.toPythonType(validTypes: Set<KType>): String {
    val kclass = toKClass()!!
    if (kclass.isValue) {
        return "object"
    }
    if (kclass == List::class) {
        return "List[${this.arguments.first().type!!.toPythonType(validTypes)}]"
    }
    if (kclass.simpleName !in kotlinTypeToPython && kclass.simpleName !in validTypes.mapNotNull { it.toKClass()?.simpleName }) {
        return "object"
    }
    return kclass.simpleName?.let { kotlinTypeToPython[it] ?: it } ?: error("No simple name for $this")
}

fun KType.toPythonTypeOrKeep(validTypes: Set<KType>): String {
    val kclass = toKClass()!!
    if (kclass.isValue) {
        return "object"
    }
    if (kclass == List::class) {
        return "List[${this.arguments.first().type!!.toPythonType(validTypes)}]"
    }
    return kclass.simpleName?.let { kotlinTypeToPython[it] ?: it } ?: error("No simple name for $this")
}

fun String.firstUppercase(): String {
    return this.substring(0, 1).uppercase() + this.substring(1, length)
}

fun String.toPythonMethod(): String {
    return kotlinMethodToPython[this] ?: firstUppercase()
}

const val TAB = "    "

class ClientGenerator(val iface: KClass<*>, val types: Set<KType>) {

    fun generateInterfaces(): String {
        return generateInterface(iface).joinToString("\n")
    }

    fun generateInterface(kclass: KClass<*>): List<String> {
        var result = ""
        result += """
class ${kclass.simpleName}(object):
"""
        val properties = kclass.members.filterIsInstance<KProperty1<*, *>>().map {
            """${it.name.firstUppercase()}: ${(it.returnType).toPythonTypeOrKeep(validTypes = types)}"""
        }

        val functions = kclass.members.filterIsInstance<KFunction<*>>().filter { it.name !in filteredMethods }.map {
            """def ${it.name.toPythonMethod()}(self${printParameters(it)})${returnType(it)}:"""
        }
        result += properties.joinToString(separator = "\n", postfix = "\n") {
            "$TAB$it"
        }
        result += functions.joinToString(separator = "\n\n", postfix = "\n") {
            "$TAB$it\n$TAB${TAB}pass"
        }
        if (properties.isEmpty() && functions.isEmpty()) {
            result += "${TAB}pass\n"
        }

        val referenced: List<String> = kclass.members.filterIsInstance<KProperty1<*, *>>().flatMap {
            generateInterface(it.returnType.classifier as KClass<*>)
        }

        return referenced + listOf(result)
    }

    private fun returnType(it: KFunction<*>): String {
        if (it.returnType.classifier == Unit::class) {
            return ""
        }
        return " -> ${(it.returnType).toPythonType(types)}"
    }

    fun printParameters(func: KFunction<*>): String {
        if (func.parameters.isEmpty()) {
            return ""
        }
        val parameters = func.parameters.subList(1, func.parameters.size)
        if (parameters.isEmpty()) {
            return ""
        }
        return parameters.joinToString(prefix = ", ", separator = ", ") {
            "${it.name}: ${it.type.toPythonType(types)}"
        }
    }
}
