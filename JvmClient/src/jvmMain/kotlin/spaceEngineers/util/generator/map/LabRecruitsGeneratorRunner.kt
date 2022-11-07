package spaceEngineers.util.generator.map

import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder
import spaceEngineers.controller.extend
import spaceEngineers.controller.extensions.removeAllBlocks
import spaceEngineers.util.generator.map.labrecruits.LabRecruitsMap
import spaceEngineers.util.generator.map.labrecruits.LabRecruitsMapBuilder
import java.io.File


fun main() {
    val text = File("./src/jvmTest/resources/labrecruits/maps/large_1928672215.csv").readText()
    val labRecruitsMap = LabRecruitsMap.fromString(text).toSimplified()
    val batch = SpaceEngineersJavaProxyBuilder().localhost()
    val se = batch.extend()
    se.removeAllBlocks()

    val mapPlacer = MapPlacer(labRecruitsMap, spaceEngineers = se, batchCallable = null)

    LabRecruitsMapBuilder(
        labRecruitsMap,
        se,
        mapPlacer,
    ).generate()
}
