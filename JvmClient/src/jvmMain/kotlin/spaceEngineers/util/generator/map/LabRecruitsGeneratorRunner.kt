package spaceEngineers.util.generator.map

import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder
import spaceEngineers.util.generator.map.labrecruits.LabRecruitsMap
import java.io.File


fun main() {
    val lines = File("./src/jvmMain/resources/LabRecruits_level_small.csv").readLines()

    val labRecruitsMap = LabRecruitsMap.fromLines(lines)
    val spaceEngineers = SpaceEngineersJavaProxyBuilder().localhost()
    val labRecruitsMapPlacer = MapPlacer(labRecruitsMap, spaceEngineers = spaceEngineers)
    labRecruitsMapPlacer.generate()
}


