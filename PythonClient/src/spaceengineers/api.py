# pylint: disable=C0115,C0116,R0902,C0103,R0903,R0913,W0401,W0622,C0114,R0205

from .models import *


class BlocksAdmin(object):
    def PlaceAt(
        self,
        blockDefinitionId: DefinitionId,
        position: Vec3F,
        orientationForward: Vec3F,
        orientationUp: Vec3F,
        color: Vec3F,
    ) -> str:
        pass

    def PlaceInGrid(
        self,
        blockDefinitionId: DefinitionId,
        gridId: str,
        minPosition: Vec3I,
        orientationForward: Vec3I,
        orientationUp: Vec3I,
        color: Vec3F,
    ) -> str:
        pass

    def Remove(self, blockId: str):
        pass

    def SetIntegrity(self, blockId: str, integrity: float):
        pass


class CharacterAdmin(object):
    def Create(
        self,
        name: str,
        position: Vec3F,
        orientationForward: Vec3F,
        orientationUp: Vec3F,
    ) -> CharacterObservation:
        pass

    def Die(self):
        pass

    def LocalCharacterId(self) -> str:
        pass

    def MainCharacterId(self) -> str:
        pass

    def Remove(self, id: str):
        pass

    def ShowTerminal(self, blockId: str):
        pass

    def Switch(self, id: str):
        pass

    def Teleport(
        self, position: Vec3F, orientationForward: Vec3F, orientationUp: Vec3F
    ) -> CharacterObservation:
        pass

    def Use(self, blockId: str, functionIndex: int, action: int):
        pass


class ObserverAdmin(object):
    def ObserveCharacters(self) -> List[CharacterObservation]:
        pass


class TestAdmin(object):
    def AdminOnly(self):
        pass

    def GameOnly(self):
        pass

    def ObserverOnly(self):
        pass


class SpaceEngineersAdmin(object):
    Blocks: BlocksAdmin
    Character: CharacterAdmin
    Observer: ObserverAdmin
    Tests: TestAdmin

    def DebugInfo(self) -> DebugInfo:
        pass

    def SetFrameLimitEnabled(self, enabled: bool):
        pass

    def ShowNotification(self, text: str):
        pass

    def UpdateDefaultInteractDistance(self, distance: float):
        pass


class Blocks(object):
    def Place(self):
        pass


class Character(object):
    def BeginUsingTool(self):
        pass

    def EndUsingTool(self):
        pass

    def Jump(self, movement: Vec3F):
        pass

    def MoveAndRotate(
        self, movement: Vec3F, rotation3: Vec2F, roll: float, ticks: int
    ) -> CharacterObservation:
        pass

    def ShowInventory(self):
        pass

    def ShowTerminal(self):
        pass

    def SwitchHelmet(self) -> CharacterObservation:
        pass

    def SwitchParkedStatus(self) -> bool:
        pass

    def SwitchWalk(self) -> bool:
        pass

    def TurnOffDampeners(self) -> CharacterObservation:
        pass

    def TurnOffJetpack(self) -> CharacterObservation:
        pass

    def TurnOnDampeners(self) -> CharacterObservation:
        pass

    def TurnOnJetpack(self) -> CharacterObservation:
        pass

    def TurnOnRelativeDampeners(self) -> CharacterObservation:
        pass

    def Use(self):
        pass


class Definitions(object):
    def AllDefinitions(self) -> List[DefinitionBase]:
        pass

    def BlockDefinitionHierarchy(self) -> dict:
        pass

    def BlockDefinitions(self) -> List[BlockDefinition]:
        pass

    def BlockHierarchy(self) -> dict:
        pass


class Input(object):
    def StartPlaying(self, snapshots: List[FrameSnapshot]):
        pass

    def StartRecording(self):
        pass

    def StopPlaying(self):
        pass

    def StopRecording(self) -> List[FrameSnapshot]:
        pass


class Items(object):
    def Activate(self, toolbarLocation: ToolbarLocation):
        pass

    def Equip(self, toolbarLocation: ToolbarLocation):
        pass

    def SetToolbarItem(
        self, definitionId: DefinitionId, toolbarLocation: ToolbarLocation
    ):
        pass

    def Toolbar(self) -> Toolbar:
        pass


class Observer(object):
    def NavigationGraph(self, gridId: str) -> NavGraph:
        pass

    def Observe(self) -> CharacterObservation:
        pass

    def ObserveBlocks(self) -> Observation:
        pass

    def ObserveCharacters(self) -> List[CharacterObservation]:
        pass

    def ObserveControlledEntity(self) -> ExtendedEntity:
        pass

    def ObserveFloatingObjects(self) -> List[FloatingObject]:
        pass

    def ObserveNewBlocks(self) -> Observation:
        pass

    def SwitchCamera(self):
        pass

    def TakeScreenshot(self, absolutePath: str):
        pass


class GamePlay(object):
    def Data(self) -> GamePlayData:
        pass

    def ShowMainMenu(self):
        pass


class JoinGame(object):
    def Data(self) -> JoinGameData:
        pass

    def DirectConnect(self):
        pass

    def JoinWorld(self):
        pass

    def Refresh(self):
        pass

    def SelectGame(self, index: int):
        pass

    def SelectTab(self, index: int):
        pass

    def ServerDetails(self):
        pass


class LoadGame(object):
    def Data(self) -> LoadGameData:
        pass

    def Delete(self):
        pass

    def DoubleClickWorld(self, index: int):
        pass

    def Edit(self):
        pass

    def Filter(self, text: str):
        pass

    def Load(self):
        pass

    def Publish(self):
        pass

    def Save(self):
        pass


class MainMenu(object):
    def Character(self):
        pass

    def Continue(self):
        pass

    def ExitToMainMenu(self):
        pass

    def ExitToWindows(self):
        pass

    def JoinGame(self):
        pass

    def LoadGame(self):
        pass

    def NewGame(self):
        pass

    def Options(self):
        pass

    def Save(self):
        pass

    def SaveAs(self):
        pass


class Medicals(object):
    def Data(self) -> MedicalsData:
        pass

    def Join(self):
        pass

    def Refresh(self):
        pass

    def Respawn(self):
        pass

    def SelectFaction(self, factionIndex: int):
        pass

    def SelectRespawn(self, roomIndex: int):
        pass

    def ShowMessageOfTheDay(self):
        pass


class MessageBox(object):
    def Data(self) -> MessageBoxData:
        pass

    def PressNo(self):
        pass

    def PressYes(self):
        pass


class SaveAs(object):
    def Data(self) -> SaveAsData:
        pass

    def PressCancel(self):
        pass

    def PressOk(self):
        pass

    def SetName(self, name: str):
        pass


class ServerConnect(object):
    def Connect(self):
        pass

    def Data(self) -> ServerConnectData:
        pass

    def EnterAddress(self, address: str):
        pass

    def ToggleAddServerToFavorites(self):
        pass


class CommsTab(object):
    pass


class ControlPanelTab(object):
    def Data(self) -> TerminalControlPanelData:
        pass

    def EnterBlockGroup(self, text: str):
        pass

    def FilterBlocks(self, text: str):
        pass


class FactionsTab(object):
    pass


class GpsTab(object):
    pass


class InfoTab(object):
    pass


class InventorySide(object):
    def ClickSelectedItem(self):
        pass

    def DoubleClickSelectedItem(self):
        pass

    def Filter(self, text: str):
        pass

    def FilterAll(self):
        pass

    def FilterEnergy(self):
        pass

    def FilterShip(self):
        pass

    def FilterStorage(self):
        pass

    def FilterSystem(self):
        pass

    def SelectItem(self, index: int):
        pass

    def SwapToCharacterOrItem(self):
        pass

    def SwapToGrid(self):
        pass

    def ToggleHideEmpty(self):
        pass


class InventorySide(object):
    def ClickSelectedItem(self):
        pass

    def DoubleClickSelectedItem(self):
        pass

    def Filter(self, text: str):
        pass

    def FilterAll(self):
        pass

    def FilterEnergy(self):
        pass

    def FilterShip(self):
        pass

    def FilterStorage(self):
        pass

    def FilterSystem(self):
        pass

    def SelectItem(self, index: int):
        pass

    def SwapToCharacterOrItem(self):
        pass

    def SwapToGrid(self):
        pass

    def ToggleHideEmpty(self):
        pass


class InventoryTab(object):
    Left: InventorySide
    Right: InventorySide

    def Data(self) -> TerminalInventoryData:
        pass

    def Deposit(self):
        pass

    def DropSelected(self):
        pass

    def FromBuildPlannerToProductionQueue(self):
        pass

    def SelectedToProductionQueue(self):
        pass

    def TransferInventoryItem(
        self, sourceInventoryId: int, destinationInventoryId: int, itemId: int
    ):
        pass

    def Withdraw(self):
        pass


class ProductionTab(object):
    def AddToProductionQueue(self, index: int):
        pass

    def Data(self) -> TerminalProductionData:
        pass

    def EnterBlueprintSearchBox(self, text: str):
        pass

    def RemoveFromProductionQueue(self, index: int):
        pass

    def SelectAssembler(self, index: int):
        pass

    def SelectBlueprint(self, index: int):
        pass

    def ToggleProductionCooperativeMode(self):
        pass

    def ToggleProductionRepeatMode(self):
        pass


class Terminal(object):
    Comms: CommsTab
    ControlPanel: ControlPanelTab
    Factions: FactionsTab
    Gps: GpsTab
    Info: InfoTab
    Inventory: InventoryTab
    Production: ProductionTab

    def Close(self):
        pass

    def Data(self) -> TerminalScreenData:
        pass

    def SelectTab(self, index: int):
        pass


class Screens(object):
    GamePlay: GamePlay
    JoinGame: JoinGame
    LoadGame: LoadGame
    MainMenu: MainMenu
    Medicals: Medicals
    MessageBox: MessageBox
    SaveAs: SaveAs
    ServerConnect: ServerConnect
    Terminal: Terminal

    def FocusedScreen(self) -> str:
        pass

    def WaitUntilTheGameLoaded(self):
        pass


class Session(object):
    def Connect(self, address: str):
        pass

    def Disconnect(self):
        pass

    def ExitGame(self):
        pass

    def ExitToMainMenu(self):
        pass

    def Info(self) -> SessionInfo:
        pass

    def LoadScenario(self, scenarioPath: str):
        pass


class SpaceEngineers(object):
    Admin: SpaceEngineersAdmin
    Blocks: Blocks
    Character: Character
    Definitions: Definitions
    Input: Input
    Items: Items
    Observer: Observer
    Screens: Screens
    Session: Session
