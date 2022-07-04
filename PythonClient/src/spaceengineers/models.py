from dataclasses import dataclass


@dataclass
class DefinitionId:
    Id: str
    Type: str


@dataclass
class Vec3F:
    X: float
    Y: float
    Z: float


@dataclass
class Vec3I:
    X: int
    Y: int
    Z: int


@dataclass
class CharacterObservation:
    BootsState: object
    Camera: object
    CurrentWeapon: object
    DampenersOn: bool
    DefinitionId: DefinitionId
    DisplayName: str
    Extent: Vec3F
    Gravity: Vec3F
    HeadLocalXAngle: float
    HeadLocalYAngle: float
    Health: float
    HelmetEnabled: bool
    Hydrogen: float
    Id: str
    InScene: bool
    Inventory: object
    JetpackControlThrust: Vec3F
    JetpackFinalThrust: Vec3F
    JetpackRunning: bool
    Movement: object
    MovementFlags: object
    Name: str
    OrientationForward: Vec3F
    OrientationUp: Vec3F
    Oxygen: float
    Position: Vec3F
    RelativeDampeningEntity: object
    SuitEnergy: float
    TargetBlock: object
    TargetUseObject: object
    Velocity: Vec3F


@dataclass
class DebugInfo:
    Executable: str
    IsDedicated: bool
    IsServer: bool
    MachineName: str
    MultiplayerActive: bool
    SessionReady: bool
    UserName: str
    Version: int


@dataclass
class Vec2F:
    X: float
    Y: float


@dataclass
class ToolbarLocation:
    Page: int
    Slot: int


@dataclass
class Toolbar:
    Items: list
    PageCount: int
    SlotCount: int


@dataclass
class NavGraph:
    Edges: list
    Nodes: list


@dataclass
class Observation:
    Character: CharacterObservation
    Grids: list


@dataclass
class GamePlayData:
    Hud: object
    OreMarkers: list


@dataclass
class JoinGameData:
    Games: list
    JoinWorldButton: object
    SelectedTab: int
    SelectedTabName: str


@dataclass
class LoadGameData:
    CurrentDirectory: object
    Files: list
    RootDirectory: object


@dataclass
class MedicalsData:
    Factions: list
    IsMotdOpen: bool
    IsMultiplayerReady: bool
    MedicalRooms: list
    Paused: bool
    RespawnButton: object
    ShowFactions: bool
    ShowMotD: bool


@dataclass
class MessageBoxData:
    ButtonType: int
    Caption: str
    Text: str


@dataclass
class SaveAsData:
    Name: str


@dataclass
class ServerConnectData:
    AddServerToFavorites: bool
    Address: str


@dataclass
class TerminalControlPanelData:
    GridBlocks: list
    NewGroupName: str
    Owner: str
    Search: str
    ShareBlock: list
    ShareBlockSelectedIndex: int
    ShowBLockInToolbarConfig: bool
    ShowBlockInTerminal: bool
    ShowOnHUD: bool
    ToggleBlock: bool
    TransferTo: list


@dataclass
class TerminalInventoryData:
    LeftInventories: list
    RightInventories: list


@dataclass
class TerminalProductionData:
    Blueprints: list
    Inventory: list
    ProductionCooperativeMode: bool
    ProductionQueue: list
    ProductionRepeatMode: bool


@dataclass
class TerminalScreenData:
    SelectedTab: str


@dataclass
class SessionInfo:
    CurrentPath: str
    Description: str
    GameDefinition: DefinitionId
    IsAdminMenuEnabled: bool
    IsCopyPastingEnabled: bool
    IsPausable: bool
    IsRunningExperimental: bool
    IsServer: bool
    IsUnloading: bool
    Name: str
    Ready: bool
    Settings: object
    StreamingInProgress: bool
