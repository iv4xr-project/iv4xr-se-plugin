package spaceEngineers.util.generator

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import spaceEngineers.controller.*
import spaceEngineers.model.BlockDefinition
import spaceEngineers.transport.SocketReaderWriter
import java.io.File

val blockHierarchyFile = File("./src/jvmMain/resources/block-hierarchy.json")

val parentMappings = SocketReaderWriter.SPACE_ENG_GSON.fromJson<Map<String, String>>(
    blockHierarchyFile.readText(),
    Map::class.java
)

val blockDefinitionHierarchyFile = File("./src/jvmMain/resources/block-definition-hierarchy.json")

val parentBlockDefinitionMappings = SocketReaderWriter.SPACE_ENG_GSON.fromJson<Map<String, String>>(
    blockDefinitionHierarchyFile.readText(),
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
    val defs = JvmSpaceEngineersBuilder.default().localhost().definitions.blockDefinitions()
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

    val csClassesAndMappings = File("../Source/Ivxr.SpaceEngineers/WorldModel/GeneratedBlocks.cs")


    csClassesAndMappings.writeText(
        """
$generatedText
using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel
{
    """.trim()
    )

    blockMappings.map { blockMapping ->
        val parents = getBlockParentsById(blockMapping.key, parentMappings)
        blockMapping.key to BlockMappingGenerator(
            fields = blockMapping.value,
            cls = blockMapping.key,
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
                csClassesAndMappings.appendText("${it.padTabs(1)}\n")
            }

        }
    }



    generateMappings(
        parentMappings = parentMappings,
        idsWithSerializers = blockMappings.keys,
        variableName = "generatedSerializerMappings"
    ).let {
        serializerMappings.appendText("$it\n")
    }

    generateCsMappings(
        parentMappings = parentMappings,
        idsWithSerializers = blockMappings.keys,
        className = "BlockMapper"
    ).let {
        csClassesAndMappings.appendText("$it\n")
    }

    csClassesAndMappings.appendText(
        """
}
    """.trimIndent()
    )

}

fun generateBlockDefinitionFiles() {
    val dataClasses = File("./src/commonMain/kotlin/spaceEngineers/model/BlockDefinitionDataClasses.kt")
    dataClasses.writeText(filePrefix)
    dataClasses.appendText(blockDataClassesImports)
    val interfaces = File("./src/commonMain/kotlin/spaceEngineers/model/BlockDefinitionInterfaces.kt")
    interfaces.writeText(filePrefix)
    val serializerMappings = File("./src/commonMain/kotlin/spaceEngineers/model/BlockDefinitionSerializerMappings.kt")
    serializerMappings.writeText(filePrefix)

    val csClassesAndMappings = File("../Source/Ivxr.SpaceEngineers/WorldModel/GeneratedBlockDefinitions.cs")
    csClassesAndMappings.writeText(
        """
$generatedText
using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel
{
""".trimStart()
    )

    val csFieldMappings = File("../Source/Ivxr.SePlugin/Control/BlockDefinitionCustomFieldsMapper.cs")
    csFieldMappings.writeText(
        """
$generatedText
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Definitions;

namespace Iv4xr.SePlugin.Control
{
    public static class BlockDefinitionCustomFieldsMapper
    {
        public static void AddCustomFields(MyCubeBlockDefinition myBlockDefinition, BlockDefinition blockDefinition)
        {
""".trimStart()
    )

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
                csClassesAndMappings.appendText("$it\n\n")
            }

            generator.generateCsFieldMappings().let {
                csFieldMappings.appendText("$it\n")
            }
        }
    }



    generateMappings(
        parentMappings = parentBlockDefinitionMappings,
        idsWithSerializers = blockDefinitionMappings.keys,
        variableName = "generatedBlockDefinitionSerializerMappings"
    ).let {
        serializerMappings.appendText("$it\n")
    }

    generateCsMappings(
        parentMappings = parentBlockDefinitionMappings,
        idsWithSerializers = blockDefinitionMappings.keys,
        className = "BlockDefinitionMapper"
    ).let {
        csClassesAndMappings.appendText("$it\n")
    }

    csClassesAndMappings.appendText(
        """
}
    """.trimIndent()
    )

    csFieldMappings.appendText(
        """
        }
    }
}
    """
    )

}

val jsonWriter = Json {
    prettyPrint = true
    prettyPrintIndent = "  "
}

fun generateBlockDefinitionHierarchyJson(spaceEngineers: SpaceEngineers) {
    val hierarchy = spaceEngineers.definitions.blockDefinitionHierarchy()
    blockDefinitionHierarchyFile.writeText(
        jsonWriter.encodeToString(hierarchy)
    )
}


fun generateBlockHierarchyJson(spaceEngineers: SpaceEngineers) {
    val hierarchy = spaceEngineers.definitions.blockHierarchy()
        .filter { it.key !in filteredParents && it.value !in filteredParents }.map {
        it.key.removeBuilderPrefix() to it.value.removeBuilderPrefix()
    }.toMap()
    blockHierarchyFile.writeText(
        jsonWriter.encodeToString(hierarchy)
    )
}

fun generateSourceJsonFromGame() {
    val spaceEngineers = JvmSpaceEngineersBuilder.default().localhost()
    generateBlockDefinitionHierarchyJson(spaceEngineers)
    generateBlockHierarchyJson(spaceEngineers)
}

fun main() {
    // This run requires SE game running and updates definition json files for the generator.
    //generateSourceJsonFromGame()
    generateBlockFiles()
    generateBlockDefinitionFiles()
}
