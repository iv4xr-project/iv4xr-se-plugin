package spaceEngineers.util.generator

import spaceEngineers.controller.*
import spaceEngineers.model.BlockDefinition
import spaceEngineers.transport.SocketReaderWriter
import java.io.File

val parentMappings = SocketReaderWriter.SPACE_ENG_GSON.fromJson<Map<String, String>>(
    File("./src/jvmMain/resources/block-hierarchy.json").readText(),
    Map::class.java
)

val parentBlockDefinitionMappings = SocketReaderWriter.SPACE_ENG_GSON.fromJson<Map<String, String>>(
    File("./src/jvmMain/resources/block-definition-hierarchy.json").readText(),
    Map::class.java
).map {
    it.key.removeDefinitionPrefix() to it.value.removeDefinitionPrefix()
}.toMap()

val idToTypesFile = File("./src/jvmMain/resources/block-ids-types.json")

val idToTypes = SocketReaderWriter.SPACE_ENG_GSON.fromJson<Map<String, List<String>>>(
    idToTypesFile.readText(),
    Map::class.java
)

fun getBlockIdsToTypes(defs: List<BlockDefinition>): Map<String, List<String>> {
    return defs.groupBy { it.definitionId.id }.mapNotNull { entry ->
        val types = entry.value.map { it.definitionId.type }.filter { it.isNotBlank() }
        if (types.isNotEmpty()) {
            entry.key.removeBuilderPrefix() to types
        } else {
            null
        }
    }.toMap()
}


fun generateBlockIdsToTypes() {
    val defs = JsonRpcSpaceEngineersBuilder.localhost().definitions.blockDefinitions()
    val idToTypes = getBlockIdsToTypes(defs)
    idToTypesFile.writeText(SocketReaderWriter.SPACE_ENG_GSON.toJson(idToTypes))
}


fun generateBlockFiles() {
    val dataClasses = File("./src/commonMain/kotlin/spaceEngineers/model/BlockDataClasses.kt")
    dataClasses.writeText(filePrefix)
    dataClasses.appendText(blockDataClassesImports)
    val interfaces = File("./src/commonMain/kotlin/spaceEngineers/model/BlockInterfaces.kt")
    interfaces.writeText(filePrefix)
    val serializerMappings = File("./src/commonMain/kotlin/spaceEngineers/model/BlockSerializerMappings.kt")
    serializerMappings.writeText(filePrefix)

    val csClassesAndMappings = File("../Source/Ivxr.PlugIndependentLib/WorldModel/GeneratedBlocks.cs")


    csClassesAndMappings.writeText(
        """
using System.Collections.Generic;

namespace Iv4xr.PluginLib.WorldModel
{
    """.trim()
    )

    blockMappings.map { it ->
        val parents = getBlockParentsById(it.key, parentMappings)
        it.key to BlockMappingGenerator(
            fields = it.value,
            cls = it.key,
            parents = parents,
            overriddenFields = getOverriddenFields(parents, blockMappings),
            commonFields = commonBlockFields,
        ).also { generator ->
            generator.generateDataClass().let {
                dataClasses.appendText("$it\n\n")
            }
            generator.generateInterface().let {
                interfaces.appendText("$it\n\n")
            }

            generator.generateCsClass().let {
                csClassesAndMappings.appendText("$it\n")
            }

        }
    }



    generateMappings(parentMappings = parentMappings, idsWithSerializers = blockMappings.keys, variableName = "generatedSerializerMappings").let {
        serializerMappings.appendText("$it\n")
    }

    generateCsMappings(parentMappings = parentMappings, idsWithSerializers = blockMappings.keys, className = "BlockMapper").let {
        csClassesAndMappings.appendText("$it\n")
    }

    csClassesAndMappings.appendText("""
}
    """.trimIndent())

}

fun generateBlockDefinitionFiles() {
    val dataClasses = File("./src/commonMain/kotlin/spaceEngineers/model/BlockDefinitionDataClasses.kt")
    dataClasses.writeText(filePrefix)
    dataClasses.appendText(blockDataClassesImports)
    val interfaces = File("./src/commonMain/kotlin/spaceEngineers/model/BlockDefinitionInterfaces.kt")
    interfaces.writeText(filePrefix)
    val serializerMappings = File("./src/commonMain/kotlin/spaceEngineers/model/BlockDefinitionSerializerMappings.kt")
    serializerMappings.writeText(filePrefix)

    val csClassesAndMappings = File("../Source/Ivxr.PlugIndependentLib/WorldModel/GeneratedBlockDefinitions.cs")
    csClassesAndMappings.writeText(
        """
using System.Collections.Generic;

namespace Iv4xr.PluginLib.WorldModel
{
""".trimStart()
    )

    val csFieldMappings = File("../Source/Ivxr.SePlugin/Control/BlockDefinitionCustomFieldsMapper.cs")
    csFieldMappings.writeText("""
using Iv4xr.PluginLib.WorldModel;
using Sandbox.Definitions;

namespace Iv4xr.SePlugin.Control
{
    public static class BlockDefinitionCustomFieldsMapper
    {
        public static void AddCustomFields(MyCubeBlockDefinition myBlockDefinition, BlockDefinition blockDefinition)
        {
""".trimStart())

    blockDefinitionMappings.map { it ->
        val parents = getBlockParentsById(it.key, parentBlockDefinitionMappings)
        it.key to BlockDefinitionMappingGenerator(
            fields = it.value,
            cls = it.key,
            parents = parents,
            overriddenFields = getOverriddenFields(parents, blockDefinitionMappings),
            commonFields = commonBlockDefinitionFields,
        ).also { generator ->
            generator.generateDataClass().let {
                dataClasses.appendText("$it\n\n")
            }
            generator.generateInterface().let {
                interfaces.appendText("$it\n\n")
            }

            generator.generateCsClass().let {
                csClassesAndMappings.appendText("$it\n")
            }

            generator.generateCsFieldMappings().let {
                csFieldMappings.appendText("$it\n")
            }
        }
    }



    generateMappings(parentMappings = parentBlockDefinitionMappings, idsWithSerializers = blockDefinitionMappings.keys, variableName = "generatedBlockDefinitionSerializerMappings").let {
        serializerMappings.appendText("$it\n")
    }

    generateCsMappings(parentMappings = parentBlockDefinitionMappings, idsWithSerializers = blockDefinitionMappings.keys, className = "BlockDefinitionMapper").let {
        csClassesAndMappings.appendText("$it\n")
    }

    csClassesAndMappings.appendText("""
}
    """.trimIndent())

    csFieldMappings.appendText("""
        }
    }
}
    """)

}

fun main() {
    generateBlockDefinitionFiles()
}
