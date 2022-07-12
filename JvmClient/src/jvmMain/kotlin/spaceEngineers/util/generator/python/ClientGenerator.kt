package spaceEngineers.util.generator.python

import spaceEngineers.controller.SpaceEngineers
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties


fun main() {
    val clientGenerator = ClientGenerator(SpaceEngineers::class)

    File("../PythonClient/src/spaceengineers/api.py").writeText(clientGenerator.generateInterfaces())
    val dataClassGenerator = DataClassGenerator()
    val modelsFile = File("../PythonClient/src/spaceengineers/models.py")
    modelsFile.writeText(
        """from dataclasses import dataclass

    """.trimIndent()
    )
    modelsFile.appendText(dataClassGenerator.generate())
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
)

val kotlinMethodToPython = mapOf(
    "continue" to "Continue"
)

fun KClass<*>.toPythonType(): String {
    if (isValue) {
        return "object"
    }
    if (simpleName !in kotlinTypeToPython && simpleName !in validTypes.mapNotNull { (it.classifier as KClass<*>).simpleName }) {
        return "object"
    }
    return simpleName?.let { kotlinTypeToPython[it] ?: it } ?: error("No simple name for $this")
}

fun KClass<*>.forcePythonType(): String {
    if (isValue) {
        return "object"
    }
    return simpleName?.let { kotlinTypeToPython[it] ?: it } ?: error("No simple name for $this")
}

fun String.firstUppercase(): String {
    return this.substring(0, 1).uppercase() + this.substring(1, length)

}

fun String.toPythonMethod(): String {
    return kotlinMethodToPython[this] ?: firstUppercase()
}

const val TAB = "    "

class ClientGenerator(val iface: KClass<*>) {

    fun generateInterfaces(): String {
        return generateInterface(iface).joinToString("\n")
    }

    fun generateInterface(kclass: KClass<*>): List<String> {
        var result = ""
        result += """
class ${kclass.simpleName}(object):
"""
        val properties = kclass.members.filterIsInstance<KProperty1<*, *>>().map {
            """${it.name.firstUppercase()}: ${(it.returnType.classifier as KClass<*>).forcePythonType()}"""
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
        return " -> ${(it.returnType.classifier as KClass<*>).forcePythonType()}"
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
            "${it.name}: ${(it.type.classifier as KClass<*>).toPythonType()}"
        }
    }
}

val validTypes = mutableSetOf<KType>()

class DataClassGenerator(

) {

    val finishedTypes = mutableSetOf<KType>(
        Int::class.createType(),
        Float::class.createType(),
        String::class.createType(),
        Boolean::class.createType(),
        Unit::class.createType(),
        String::class.createType(nullable = true)
    )


    fun generate(): String {
        return generate(SpaceEngineers::class).joinToString("\n\n")
    }

    fun generate(kclass: KClass<*>): List<String> {
        val functionParameters: Set<KType> =
            kclass.members.filterIsInstance<KFunction<*>>().filter { it.name !in filteredMethods }.flatMap {
                functionKTypes(it)
            }.toSet()
        val returnTypes: List<String> = kclass.members.filterIsInstance<KProperty1<*, *>>().flatMap {
            generate(it.returnType.classifier as KClass<*>)
        }
        return returnTypes + functionParameters.map { validTypes.add(it); finishedTypes.add(it); it }
            .map { generateDataClass(it) }
    }

    fun generateDataClass(ktype: KType): String {
        val klass = ktype.classifier as KClass<*>
        var result = """@dataclass
class ${klass.simpleName}:
"""
        result += klass.memberProperties.filter { it.name != "dimensions" }.joinToString("\n") {
            """$TAB${it.name.firstUppercase()}: ${(it.returnType.classifier as KClass<*>).toPythonType()}"""
        }
        return result
    }

    private fun functionKTypes(kFunction: KFunction<*>): Set<KType> {
        return (kFunction.parameters.map { it.type } + kFunction.returnType).filter {
            !it.isMarkedNullable && it !in finishedTypes
        }.mapNotNull {
            val klass = (it.classifier as KClass<*>); if (klass.isData) {
            it
        } else {
            null
        }
        }.toSet()
    }
}
