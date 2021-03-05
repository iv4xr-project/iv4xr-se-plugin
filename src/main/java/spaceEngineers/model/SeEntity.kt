package spaceEngineers.model


interface SeEntity {
    // TODO(PP): Add entity type
    val id: String
    val position: Vec3 // TODO(PP): Add sub-entities to support hierarchy.
}