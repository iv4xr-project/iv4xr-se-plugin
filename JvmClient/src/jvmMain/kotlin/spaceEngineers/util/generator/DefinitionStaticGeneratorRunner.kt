package spaceEngineers.util.generator

import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder

fun main() {
    val spaceEngineers = SpaceEngineersJavaProxyBuilder().localhost()
    println(generate(spaceEngineers.definitions.allDefinitions().map { it.definitionId }))
    // println(generateTypes(spaceEngineers.definitions.allDefinitions().map { it.definitionId }))
}
