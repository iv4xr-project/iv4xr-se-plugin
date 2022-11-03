package spaceEngineers.util.generator.map

import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder
import spaceEngineers.controller.extend
import spaceEngineers.controller.extensions.removeAllBlocks
import spaceEngineers.util.generator.map.labrecruits.LabRecruitsMap
import spaceEngineers.util.generator.map.labrecruits.LabRecruitsMapBuilder
import java.io.File


fun main() {
    val se = SpaceEngineersJavaProxyBuilder().localhost().extend()
    se.removeAllBlocks()

    val lines = File("./src/jvmMain/resources/LabRecruits_level_small.csv").readLines()
    val labRecruitsMap = LabRecruitsMap.fromLines(lines)
    LabRecruitsMapBuilder(labRecruitsMap, se).generate()
}
