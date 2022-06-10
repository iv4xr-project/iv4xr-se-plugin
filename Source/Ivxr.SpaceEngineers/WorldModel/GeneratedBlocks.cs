// Generated automatically by BlockMappingGeneratorRunner.kt, do not change.

using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel
{	
	public class TerminalBlock : Block 
	{
	    public bool ShowInInventory;
	    public bool ShowInTerminal;
	    public bool ShowOnHUD;
	}
	
	public class FunctionalBlock : TerminalBlock 
	{
	    public bool Enabled;
	}
	
	public class DoorBase : FunctionalBlock 
	{
	    public bool Open;
	    public bool AnyoneCanUse;
	}
	
	public class FueledPowerProducer : FunctionalBlock 
	{
	    public float MaxOutput;
	    public float CurrentOutput;
	    public float Capacity;
	}
	public static class BlockMapper
	{
	    public static readonly Dictionary<string, string> Mapping = new Dictionary<string, string>
	    {
		    { "DebugSphere1", "FunctionalBlock" },
		    { "DebugSphere2", "FunctionalBlock" },
		    { "DebugSphere3", "FunctionalBlock" },
		    { "MyProgrammableBlock", "FunctionalBlock" },
		    { "Projector", "FunctionalBlock" },
		    { "ProjectorBase", "FunctionalBlock" },
		    { "SensorBlock", "FunctionalBlock" },
		    { "TargetDummyBlock", "FunctionalBlock" },
		    { "SoundBlock", "FunctionalBlock" },
		    { "ButtonPanel", "FunctionalBlock" },
		    { "TimerBlock", "FunctionalBlock" },
		    { "TurretControlBlock", "FunctionalBlock" },
		    { "RadioAntenna", "FunctionalBlock" },
		    { "Beacon", "FunctionalBlock" },
		    { "RemoteControl", "TerminalBlock" },
		    { "ShipController", "TerminalBlock" },
		    { "LaserAntenna", "FunctionalBlock" },
		    { "Cockpit", "TerminalBlock" },
		    { "Gyro", "FunctionalBlock" },
		    { "CryoChamber", "TerminalBlock" },
		    { "CargoContainer", "TerminalBlock" },
		    { "VendingMachine", "FunctionalBlock" },
		    { "StoreBlock", "FunctionalBlock" },
		    { "Jukebox", "FunctionalBlock" },
		    { "LCDPanelsBlock", "FunctionalBlock" },
		    { "TextPanel", "FunctionalBlock" },
		    { "ReflectorLight", "FunctionalBlock" },
		    { "LightingBlock", "FunctionalBlock" },
		    { "Door", "DoorBase" },
		    { "AirtightHangarDoor", "DoorBase" },
		    { "AirtightDoorGeneric", "DoorBase" },
		    { "AirtightSlideDoor", "DoorBase" },
		    { "SafeZoneBlock", "FunctionalBlock" },
		    { "ContractBlock", "FunctionalBlock" },
		    { "BatteryBlock", "FunctionalBlock" },
		    { "Reactor", "FueledPowerProducer" },
		    { "HydrogenEngine", "FueledPowerProducer" },
		    { "WindTurbine", "FunctionalBlock" },
		    { "SolarPanel", "FunctionalBlock" },
		    { "GravityGenerator", "FunctionalBlock" },
		    { "GravityGeneratorBase", "FunctionalBlock" },
		    { "GravityGeneratorSphere", "FunctionalBlock" },
		    { "VirtualMass", "FunctionalBlock" },
		    { "SpaceBall", "FunctionalBlock" },
		    { "LandingGear", "FunctionalBlock" },
		    { "OxygenTank", "FunctionalBlock" },
		    { "GasTank", "FunctionalBlock" },
		    { "Assembler", "FunctionalBlock" },
		    { "ProductionBlock", "FunctionalBlock" },
		    { "Refinery", "FunctionalBlock" },
		    { "ConveyorSorter", "FunctionalBlock" },
		    { "Thrust", "FunctionalBlock" },
		    { "InteriorLight", "FunctionalBlock" },
		    { "AirVent", "FunctionalBlock" },
		    { "Collector", "FunctionalBlock" },
		    { "ShipConnector", "FunctionalBlock" },
		    { "PistonBase", "FunctionalBlock" },
		    { "MechanicalConnectionBlock", "FunctionalBlock" },
		    { "ExtendedPistonBase", "FunctionalBlock" },
		    { "MotorStator", "FunctionalBlock" },
		    { "MotorBase", "FunctionalBlock" },
		    { "MotorAdvancedStator", "FunctionalBlock" },
		    { "MedicalRoom", "FunctionalBlock" },
		    { "OxygenGenerator", "FunctionalBlock" },
		    { "SurvivalKit", "FunctionalBlock" },
		    { "OxygenFarm", "FunctionalBlock" },
		    { "UpgradeModule", "FunctionalBlock" },
		    { "ExhaustBlock", "FunctionalBlock" },
		    { "MotorSuspension", "FunctionalBlock" },
		    { "Drill", "FunctionalBlock" },
		    { "ShipGrinder", "FunctionalBlock" },
		    { "ShipToolBase", "FunctionalBlock" },
		    { "ShipWelder", "FunctionalBlock" },
		    { "OreDetector", "FunctionalBlock" },
		    { "JumpDrive", "FunctionalBlock" },
		    { "CameraBlock", "FunctionalBlock" },
		    { "MergeBlock", "FunctionalBlock" },
		    { "Parachute", "DoorBase" },
		    { "SmallMissileLauncher", "FunctionalBlock" },
		    { "UserControllableGun", "FunctionalBlock" },
		    { "SmallGatlingGun", "FunctionalBlock" },
		    { "Searchlight", "FunctionalBlock" },
		    { "HeatVentBlock", "FunctionalBlock" },
		    { "Warhead", "TerminalBlock" },
		    { "Decoy", "FunctionalBlock" },
		    { "LargeGatlingTurret", "FunctionalBlock" },
		    { "ConveyorTurretBase", "FunctionalBlock" },
		    { "TurretBase", "FunctionalBlock" },
		    { "LargeMissileTurret", "FunctionalBlock" },
		    { "InteriorTurret", "FunctionalBlock" },
		    { "SmallMissileLauncherReload", "FunctionalBlock" },
        };
    }
}