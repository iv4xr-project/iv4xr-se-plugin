package spaceEngineers.util.generator.map

interface MapLayer {
    val width: Int
    val height: Int
    operator fun get(x: Int, y: Int): BlockPlacementInformation?
}
