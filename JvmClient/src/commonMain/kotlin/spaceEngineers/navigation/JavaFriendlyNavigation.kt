package spaceEngineers.navigation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import spaceEngineers.model.BlockId
import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.Vec3F
import java.util.concurrent.CompletableFuture
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class JavaFriendlyNavigation @JvmOverloads constructor(
    val navigation: Navigation,
    val scope: CoroutineScope = GlobalScope
) {

    fun moveInLine(
        targetLocation: Vec3F,
        movementType: CharacterMovementType = CharacterMovementType.RUN,
        timeout: Duration = 20.seconds,
    ): CompletableFuture<Unit> {
        return scope.future { navigation.moveInLine(targetLocation, movementType, timeout) }
    }

    fun navigateToBlock(
        id: BlockId,
        movementType: CharacterMovementType = CharacterMovementType.RUN,
        timeout: Duration = 120.seconds,
    ): CompletableFuture<Unit> {
        return scope.future { navigation.navigateToBlock(id, movementType, timeout) }
    }
}
