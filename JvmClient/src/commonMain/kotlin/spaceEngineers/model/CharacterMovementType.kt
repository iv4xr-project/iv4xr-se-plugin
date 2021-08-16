package spaceEngineers.model

enum class CharacterMovementType(val speed: Float) {
    WALK(0.3f), RUN(1f), SPRINT(1.7f);

    companion object {

        const val WALK_THRESHOLD = 0.4f
        const val SPRINT_THRESHOLD = 1.6f

        fun fromValue(value: Float): CharacterMovementType {
            //yes it's less and then less or equal, from SE code
            return if (value < WALK_THRESHOLD) {
                WALK
            } else if (value <= SPRINT_THRESHOLD) {
                RUN
            } else {
                SPRINT
            }
        }
    }
}
