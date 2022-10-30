package bdd.setup

import spaceEngineers.controller.ExtendedSpaceEngineers
import spaceEngineers.model.ScreenName
import spaceEngineers.model.canUse
import spaceEngineers.util.whileWithTimeout

suspend fun ExtendedSpaceEngineers.connectDirectly(address: String) {
    extensions.screen.navigation.navigateTo(ScreenName.ServerConnect)
    screens.serverConnect.enterAddress("${address}:27016")
    screens.serverConnect.connect()
}

suspend fun ExtendedSpaceEngineers.createLobbyGame(scenarioId: String, filterSaved: Boolean = true) {
    extensions.screen.navigation.navigateTo(ScreenName.LoadGame)
    val data = screens.loadGame.data()
    val filtered = data.files.filter { it.name == scenarioId && (!it.fullName.contains("-saved") || !filterSaved) }
    check(filtered.size < 2) { "Multiple non '-saved' files with same name: $filtered" }
    val index = data.files.indexOfFirst { it.name == scenarioId && (!it.fullName.contains("-saved") || !filterSaved) }
    check(index > -1) {
        "Scenario $scenarioId not found in the list, found: ${data.files.map { it.name }.sorted()}"
    }
    screens.loadGame.doubleClickWorld(index)
}

suspend fun ExtendedSpaceEngineers.connectToFirstFriendlyGame() {
    extensions.screen.navigation.navigateTo(ScreenName.JoinGame)
    with(screens.joinGame) {
        selectTab(5)
        whileWithTimeout {
            data().selectedTab != 5 || data().games.isEmpty()
        }
        selectGame(0)
        whileWithTimeout { !data().joinWorldButton.canUse }
        joinWorld()
    }
}