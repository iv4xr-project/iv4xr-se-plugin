package spaceEngineers.navigation

import spaceEngineers.controller.Screens
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.extensions.typedFocusedScreen
import spaceEngineers.controller.extensions.waitForScreen
import spaceEngineers.graph.*
import spaceEngineers.model.ScreenName
import spaceEngineers.model.ScreenName.Companion.CubeBuilder
import spaceEngineers.model.ScreenName.Companion.GamePlay
import spaceEngineers.model.ScreenName.Companion.JoinGame
import spaceEngineers.model.ScreenName.Companion.LoadGame
import spaceEngineers.model.ScreenName.Companion.MainMenu
import spaceEngineers.model.ScreenName.Companion.MessageBox
import spaceEngineers.model.ScreenName.Companion.NewGame
import spaceEngineers.model.ScreenName.Companion.SaveAs
import spaceEngineers.model.ScreenName.Companion.ServerConnect
import spaceEngineers.model.ScreenName.Companion.Terminal
import spaceEngineers.model.ScreenName.Companion.ToolbarConfig
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

typealias ScreenTransitionAction = suspend ScreenTransition.ScreenTransitionContext.() -> Unit

infix fun ScreenName.edgeTo(to: ScreenName): String {
    return "${this.name}-${to.name}"
}

data class ScreenTransition(
    val from: ScreenName,
    val to: ScreenName,
    val timeout: Duration = 15.seconds,
    val transition: ScreenTransitionAction,
) {
    val edgeId: String
        get() = from edgeTo to

    data class ScreenTransitionContext(
        val from: ScreenName,
        val to: ScreenName,
        val spaceEngineers: SpaceEngineers,
    ) : Screens by spaceEngineers.screens {


    }

    private fun toContext(spaceEngineers: SpaceEngineers): ScreenTransitionContext {
        return ScreenTransitionContext(
            from = from, to = to, spaceEngineers = spaceEngineers
        )
    }

    suspend fun use(spaceEngineers: SpaceEngineers) {
        transition(toContext(spaceEngineers))
    }
}

val DEFAULT_TRANSITIONS = listOf(
    ScreenTransition(Terminal, GamePlay) {
        terminal.close()
    },
    ScreenTransition(GamePlay, Terminal) {
        //OR spaceEngineers.character.showTerminal()
        spaceEngineers.character.showInventory()
    },
    ScreenTransition(GamePlay, CubeBuilder) {
        gamePlay.showToolbarConfig()
    },
    ScreenTransition(CubeBuilder, GamePlay) {
        toolbarConfig.close()
    },
    ScreenTransition(MainMenu, NewGame) {
        mainMenu.newGame()
    },
    ScreenTransition(NewGame, MainMenu) {
        newGame.close()
    },
    ScreenTransition(MainMenu, JoinGame) {
        mainMenu.joinGame()
    },
    ScreenTransition(MainMenu, SaveAs) {
        mainMenu.saveAs()
    },
    ScreenTransition(SaveAs, MainMenu) {
        saveAs.pressCancel()
    },
    ScreenTransition(MessageBox, MainMenu) {
        //TODO: depending on MessageBox data
        val data = messageBox.data()
    },
    ScreenTransition(JoinGame, MainMenu) {
        joinGame.close()
    },
    ScreenTransition(JoinGame, ServerConnect) {
        joinGame.directConnect()
    },
    ScreenTransition(ServerConnect, JoinGame) {
        serverConnect.close()
    },
    ScreenTransition(MainMenu, LoadGame) {
        mainMenu.loadGame()
    },
    ScreenTransition(LoadGame, MainMenu) {
        loadGame.close()
    },
    ScreenTransition(GamePlay, ToolbarConfig) {
        gamePlay.showToolbarConfig()
    },
)


fun ScreenName.toNode(): spaceEngineers.graph.Node<ScreenName, Unit> {
    return DataNode(this, Unit)
}

fun ScreenTransition.toDirectedEdge(): DirectedEdge<String, ScreenName, ScreenTransition> {
    return DirectedEdge(from, to, edgeId, this)
}


fun List<ScreenTransition>.toGraph(): DirectedGraph<ScreenName, Unit, String, ScreenTransition> {
    return DirectedGraph(
        nodes = flatMap { listOf(it.from, it.to) }.map { it.toNode() },
        edges = map { it.toDirectedEdge() }
    )
}


class ScreenNavigation(
    val spaceEngineers: SpaceEngineers,
    transitions: List<ScreenTransition> = DEFAULT_TRANSITIONS,
    private val search: GraphSearch<ScreenName, String> = BasicGraphSearch(transitions.toGraph().toExtra()),
) {

    private val transitionsByEdgeId = transitions.associateBy { it.edgeId }

    suspend fun navigateTo(screenName: ScreenName) {
        val currentScreen = spaceEngineers.screens.typedFocusedScreen()
        if (currentScreen == screenName) {
            return
        }
        val path = search.findPath(currentScreen, screenName)
        val transitions = path.map { edgeId ->
            transitionsByEdgeId.getValue(edgeId)
        }
        transitions.forEach {
            useTransition(it)
        }
    }

    private suspend fun useTransition(transition: ScreenTransition) = with(spaceEngineers) {
        if (transition.to == screens.typedFocusedScreen()) {
            return
        }
        transition.use(spaceEngineers)

        screens.waitForScreen(screenName = transition.to, timeout = transition.timeout)
    }
}
