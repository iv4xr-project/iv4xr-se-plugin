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
    val text = DataStructuresGenerator().generate()

    val modelsFile = File("../PythonClient/src/spaceengineers/models.py")
    modelsFile.writeText(
        """from dataclasses import dataclass
from typing import List

    """.trimIndent()
    )
    modelsFile.appendText(text)

}

fun generateDataClass(ktype: KType, validTypes: Set<KType>): String {
    val klass = ktype.classifier as KClass<*>
    if (klass.qualifiedName?.startsWith("spaceEngineers") != true) {
        return ""
    }
    var result = """@dataclass
class ${klass.simpleName}:
"""
    result += klass.memberProperties.filter { it.returnType.toKClass() != null }.filter { it.name != "dimensions" }
        .joinToString("\n") {
            //println(it)
            """$TAB${it.name.firstUppercase()}: ${it.returnType.toPythonType(validTypes)}"""
        }
    return "\n\n" + result + ""
}

fun KType.toKClass(): KClass<*>? {
    if (classifier is KClass<*>) {
        return classifier as KClass<*>
    }
    return null
}


fun KType.containsMemberOfType(type: KType): Boolean {
    if (this.toString().contains("kotlin") && type.toString().contains("kotlin")) {
        return false
    }
    println("$this contains $type TEST")
    return (toKClass()?.run {
        memberProperties.map { it.returnType }.any { property ->
            (property == type).apply {
                if (this) {
                    println("1")
                }
            } ||
                    property.arguments.mapNotNull { argumentProjection -> argumentProjection.type }
                        .contains(type).apply {
                            if (this) {
                                println("2")
                            }
                        }
        }
    } == true).apply {
        if (this) {
            println(this@containsMemberOfType)
            println("""${this@containsMemberOfType} contains $type""")
        }
    }
}

class DataStructuresGenerator(
    private val finishedTypes: MutableSet<KType> = mutableSetOf<KType>(
        Int::class.createType(),
        Float::class.createType(),
        String::class.createType(),
        Boolean::class.createType(),
        Unit::class.createType(),
        String::class.createType(nullable = true),
        Long::class.createType(),
        Byte::class.createType(),
        Short::class.createType(),
        Double::class.createType(),
    )
) {

    fun generate(): String {
        val types = find().toSet().sortedWith { x, y ->
            if (x.containsMemberOfType(y)) {
                x.arguments.count { it.type != null } + 100
            } else if (y.containsMemberOfType(x)) {
                -y.arguments.count { it.type != null } - 100
            } else {
                y.arguments.count { it.type != null } - x.arguments.count { it.type != null }
            }
        }.filter { it.toString().length > 4 }


        return types.joinToString("\n") { generateDataClass(it, types.toSet()) }
    }

    fun dependencyGraph(): Map<KType, Set<KType>> {
        return find().toSet()
            .filter {
                it !in finishedTypes && it.toKClass()?.qualifiedName?.startsWith("spaceEngineers.model") == true
            }
            .map {
                it to findMembers(it.toKClass()).flatMap { listOf(it) + it.arguments.mapNotNull { it.type } }
                    .filter {
                        it.toString().contains("spaceEngineers")
                    }.filter { it !in finishedTypes }.toSet()
            }.toMap()
    }

    fun find(): List<KType> {
        return exploreInterfaces(SpaceEngineers::class)
            .includeGenerics().filter { it !in finishedTypes }
    }

    fun List<KType>.includeGenerics(): List<KType> {
        return flatMap {
            listOf(it) + it.arguments.mapNotNull { it.type }
        }
    }

    val exploredInterfaces = mutableListOf<KClass<*>>()

    val exploredModels = mutableListOf<KClass<*>>()

    fun List<KType>.SEModelsOnly(): List<KType> = filter {
        it.toKClass()?.qualifiedName?.startsWith("spaceEngineers") == true
    }

    fun List<KType>.includeDataMembers(): List<KType> {
        return flatMap { listOf(it) + properties(it) }
    }

    fun properties(type: KType): List<KType> {
        return type.toKClass()?.let {
            it.memberProperties.filter { it.name !in setOf("dimensions", "statsWrapper") }.map {
                it.returnType
            }
        } ?: emptyList<KType>()
    }

    fun exploreInterfaces(kclass: KClass<*>): List<KType> {
        if (kclass in exploredInterfaces) {
            return emptyList()
        }
        exploredInterfaces.add(kclass)


        val subInterfaces = kclass.members.filterIsInstance<KProperty1<*, *>>().map { it.returnType }


        val functionalParameterTypes = findFunctionTypes(kclass)

        val nestedTypes = subInterfaces.mapNotNull { it.toKClass() }.flatMap {
            exploreInterfaces(it)
        }


        return (functionalParameterTypes + nestedTypes).includeGenerics().includeDataMembers()
    }

    private fun findMembers(kclass: KClass<*>?): List<KType> {
        if (kclass == null) {
            return emptyList()
        }
        return kclass.memberProperties.filter { it.name !in setOf("dimensions", "statsWrapper") }.map {
            it.returnType
        }
    }

    fun findFunctionTypes(kclass: KClass<*>): List<KType> {
        return kclass.members.filterIsInstance<KFunction<*>>().filter { it.name !in filteredMethods }.flatMap {
            functionKTypes(it)
        }
    }

    fun <T> List<T>.skipFirst(): List<T> {
        if (isEmpty()) {
            return emptyList()
        }
        return subList(1, size)
    }

    private fun functionKTypes(kFunction: KFunction<*>): List<KType> {
        return (kFunction.parameters.skipFirst().map { it.type } + listOf(kFunction.returnType)).filter {
            !it.isMarkedNullable
        }
    }
}