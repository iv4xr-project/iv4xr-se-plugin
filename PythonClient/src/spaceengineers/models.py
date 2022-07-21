from dataclasses import dataclass
from typing import List


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
class BootsState:
    Value: object


@dataclass
class BasePose:
    OrientationForward: Vec3F
    OrientationUp: Vec3F
    Position: Vec3F


@dataclass
class ExtendedEntity:
    DefinitionId: DefinitionId
    DisplayName: str
    Id: str
    InScene: bool
    Name: str
    Velocity: Vec3F
    OrientationForward: Vec3F
    OrientationUp: Vec3F
    Position: Vec3F


@dataclass
class InventoryItem:
    Amount: int
    Id: DefinitionId
    ItemId: int


@dataclass
class Inventory:
    CargoPercentage: float
    CurrentMass: float
    CurrentVolume: float
    Id: int
    Items: List[InventoryItem]
    MaxMass: float
    MaxVolume: float


@dataclass
class CharacterMovement:
    Direction: object
    IsCrouching: bool
    IsFalling: bool
    IsJumping: bool
    IsStanding: bool
    Mode: object
    Rotation: object
    Speed: object
    Value: object


@dataclass
class CharacterMovementFlags:
    Value: object
    WantsWalk: bool


@dataclass
class BaseEntity:
    DefinitionId: DefinitionId
    DisplayName: str
    Id: str
    InScene: bool
    Name: str
    OrientationForward: Vec3F
    OrientationUp: Vec3F
    Position: Vec3F
    Velocity: Vec3F


@dataclass
class UseObject:
    SupportedActions: int
    ContinuousUsage: bool
    Name: str
    PrimaryAction: int
    SecondaryAction: int


@dataclass
class Block:
    BuildIntegrity: float
    BuiltBy: str
    DefinitionId: DefinitionId
    Functional: bool
    GridPosition: Vec3I
    Integrity: float
    MaxIntegrity: float
    MaxPosition: Vec3F
    MinPosition: Vec3F
    OwnerId: str
    Size: Vec3F
    UseObjects: List[UseObject]
    Working: bool
    Id: str
    OrientationForward: Vec3F
    OrientationUp: Vec3F
    Position: Vec3F


@dataclass
class UseObject:
    SupportedActions: int
    ContinuousUsage: bool
    Name: str
    PrimaryAction: int
    SecondaryAction: int


@dataclass
class CharacterObservation:
    BootsState: object
    Camera: BasePose
    CurrentWeapon: ExtendedEntity
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
    Inventory: Inventory
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
    RelativeDampeningEntity: BaseEntity
    SuitEnergy: float
    TargetBlock: Block
    TargetUseObject: UseObject
    Velocity: Vec3F


@dataclass
class Vec2F:
    X: float
    Y: float


@dataclass
class DefinitionBase:
    AvailableInSurvival: bool
    DefinitionId: DefinitionId
    Enabled: bool
    Public: bool


@dataclass
class BuildProgressModel:
    BuildRatioUpperBound: float


@dataclass
class DataPhysicalItemDefinition:
    AvailableInSurvival: bool
    DefinitionId: DefinitionId
    Enabled: bool
    Health: int
    Mass: float
    Public: bool
    Size: Vec3F
    Volume: float


@dataclass
class Component:
    Count: int
    DeconstructItem: DataPhysicalItemDefinition
    Definition: DataPhysicalItemDefinition


@dataclass
class CubeSize:
    Value: float
    Name: str
    Ordinal: int


@dataclass
class MountPoint:
    Default: bool
    Enabled: bool
    End: Vec3F
    ExclusionMask: int
    Normal: Vec3F
    PressurizedWhenOpen: bool
    PropertiesMask: int
    Start: Vec3F


@dataclass
class BlockDefinition:
    BuildProgressModels: List[BuildProgressModel]
    Components: List[Component]
    CubeSize: CubeSize
    MountPoints: List[MountPoint]
    Size: Vec3F
    Type: str
    AvailableInSurvival: bool
    DefinitionId: DefinitionId
    Enabled: bool
    Mass: float
    Public: bool


@dataclass
class KeyboardSnapshot:
    PressedKeys: List[int]
    Text: List[object]


@dataclass
class MouseSnapshot:
    CursorPositionX: int
    CursorPositionY: int
    LeftButton: bool
    MiddleButton: bool
    RightButton: bool
    ScrollWheelValue: int
    X: int
    XButton1: bool
    XButton2: bool
    Y: int


@dataclass
class InputSnapshot:
    Keyboard: KeyboardSnapshot
    Mouse: MouseSnapshot


@dataclass
class FrameSnapshot:
    Input: InputSnapshot


@dataclass
class ToolbarLocation:
    Page: int
    Slot: int


@dataclass
class ToolbarItem:
    Id: DefinitionId
    Name: str


@dataclass
class Toolbar:
    Items: List[ToolbarItem]
    PageCount: int
    SlotCount: int


@dataclass
class Edge:
    I: int
    J: int


@dataclass
class Node:
    Id: int
    Position: Vec3F


@dataclass
class NavGraph:
    Edges: List[Edge]
    Nodes: List[Node]


@dataclass
class Block:
    BuildIntegrity: float
    BuiltBy: str
    DefinitionId: DefinitionId
    Functional: bool
    GridPosition: Vec3I
    Integrity: float
    MaxIntegrity: float
    MaxPosition: Vec3F
    MinPosition: Vec3F
    OwnerId: str
    Size: Vec3F
    UseObjects: List[UseObject]
    Working: bool
    Id: str
    OrientationForward: Vec3F
    OrientationUp: Vec3F
    Position: Vec3F


@dataclass
class CubeGrid:
    Blocks: List[Block]
    DefinitionId: DefinitionId
    DisplayName: str
    Id: str
    InScene: bool
    Mass: float
    Name: str
    OrientationForward: Vec3F
    OrientationUp: Vec3F
    Parked: bool
    Position: Vec3F
    Velocity: Vec3F


@dataclass
class Observation:
    Character: CharacterObservation
    Grids: List[CubeGrid]


@dataclass
class ExtendedEntity:
    DefinitionId: DefinitionId
    DisplayName: str
    Id: str
    InScene: bool
    Name: str
    Velocity: Vec3F
    OrientationForward: Vec3F
    OrientationUp: Vec3F
    Position: Vec3F


@dataclass
class PhysicalItemDefinition:
    Health: int
    Mass: float
    Size: Vec3F
    Volume: float
    AvailableInSurvival: bool
    DefinitionId: DefinitionId
    Enabled: bool
    Public: bool


@dataclass
class FloatingObject:
    Amount: float
    DisplayName: str
    EntityId: int
    ItemDefinition: PhysicalItemDefinition


@dataclass
class Hud:
    Stats: dict
    StatsWrapper: object


@dataclass
class OreMarker:
    Distance: float
    Materials: List[DefinitionId]
    Position: Vec3F
    Text: str


@dataclass
class GamePlayData:
    Hud: Hud
    OreMarkers: List[OreMarker]


@dataclass
class ListedGameInformation:
    Server: str
    World: str


@dataclass
class GuiControlBase:
    Enabled: bool
    Name: str
    Visible: bool


@dataclass
class JoinGameData:
    Games: List[ListedGameInformation]
    JoinWorldButton: GuiControlBase
    SelectedTab: int
    SelectedTabName: str


@dataclass
class File:
    FullName: str
    IsDirectory: bool
    Name: str


@dataclass
class LoadGameData:
    CurrentDirectory: File
    Files: List[File]
    RootDirectory: File


@dataclass
class Faction:
    Name: str
    Tag: str


@dataclass
class MedicalRoom:
    AvailableIn: str
    Name: str


@dataclass
class GuiControlBase:
    Enabled: bool
    Name: str
    Visible: bool


@dataclass
class MedicalsData:
    Factions: List[Faction]
    IsMotdOpen: bool
    IsMultiplayerReady: bool
    MedicalRooms: List[MedicalRoom]
    Paused: bool
    RespawnButton: GuiControlBase
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
class TerminalScreenData:
    SelectedTab: str


@dataclass
class BlockOrGroupItem:
    Text: str
    Visible: bool


@dataclass
class TerminalControlPanelData:
    GridBlocks: List[BlockOrGroupItem]
    NewGroupName: str
    Owner: str
    Search: str
    ShareBlock: List[str]
    ShareBlockSelectedIndex: int
    ShowBLockInToolbarConfig: bool
    ShowBlockInTerminal: bool
    ShowOnHUD: bool
    ToggleBlock: bool
    TransferTo: List[str]


@dataclass
class TerminalInventoryData:
    LeftInventories: List[Inventory]
    RightInventories: List[Inventory]


@dataclass
class AmountedDefinitionId:
    Amount: int
    Id: DefinitionId


@dataclass
class BlueprintDefinition:
    DisplayName: str
    Prerequisites: List[AmountedDefinitionId]
    Results: List[AmountedDefinitionId]


@dataclass
class ProductionQueueItem:
    Amount: int
    Blueprint: BlueprintDefinition


@dataclass
class TerminalProductionData:
    Blueprints: List[BlueprintDefinition]
    Inventory: List[AmountedDefinitionId]
    ProductionCooperativeMode: bool
    ProductionQueue: List[ProductionQueueItem]
    ProductionRepeatMode: bool


@dataclass
class SessionSettings:
    GameMode: object
    InfiniteAmmo: bool


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
    Settings: SessionSettings
    StreamingInProgress: bool


@dataclass
class GameMode:
    Value: object
