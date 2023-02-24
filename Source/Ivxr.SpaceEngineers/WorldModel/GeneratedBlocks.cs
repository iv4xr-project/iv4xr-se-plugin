// Generated automatically by BlockMappingGeneratorRunner.kt, do not change.

using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel
{	
	public class TerminalBlock : Block 
	{
	    public bool ShowInInventory;
	    public bool ShowInTerminal;
	    public bool ShowOnHUD;
	    public string CustomName;
	    public string CustomData;
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
	
	public class Warhead : TerminalBlock 
	{
	    public bool IsCountingDown;
	    public bool IsArmed;
	}
	
	public class MedicalRoom : FunctionalBlock 
	{
	    public bool SuitChangeAllowed;
	    public bool CustomWardrobesEnabled;
	    public string SpawnName;
	    public bool RespawnAllowed;
	    public bool RefuelAllowed;
	    public bool HealingAllowed;
	    public bool SpawnWithoutOxygenEnabled;
	    public bool ForceSuitChangeOnRespawn;
	}
	
	public class TimerBlock : FunctionalBlock 
	{
	    public bool Silent;
	    public float TriggerDelay;
	    public Toolbar Toolbar;
	}
	
	public class SensorBlock : FunctionalBlock 
	{
	    public bool IsActive;
	    public PlainVec3D FieldMin;
	    public PlainVec3D FieldMax;
	    public float MaxRange;
	    public int Filters;
	    public Toolbar Toolbar;
	}
	
	public class GravityGenerator : GravityGeneratorBase 
	{
	    public PlainVec3D FieldSize;
	}
	
	public class GravityGeneratorSphere : GravityGeneratorBase 
	{
	    public float Radius;
	}
	
	public class GravityGeneratorBase : FunctionalBlock 
	{
	    public float GravityAcceleration;
	}
	
	public class MechanicalConnectionBlockBase : Block 
	{
	    public float SafetyDetach;
	}
	
	public class PistonBase : FunctionalBlock 
	{
	    public float CurrentPosition;
	    public int Status;
	    public float Velocity;
	    public float MinLimit;
	    public float MaxLimit;
	    public float MaxImpulseAxis;
	    public float MaxImpulseNonAxis;
	}
	
	public class Thrust : FunctionalBlock 
	{
	    public float ThrustOverride;
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
		    { "TargetDummyBlock", "FunctionalBlock" },
		    { "SoundBlock", "FunctionalBlock" },
		    { "ButtonPanel", "FunctionalBlock" },
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
		    { "VirtualMass", "FunctionalBlock" },
		    { "SpaceBall", "FunctionalBlock" },
		    { "LandingGear", "FunctionalBlock" },
		    { "OxygenTank", "FunctionalBlock" },
		    { "GasTank", "FunctionalBlock" },
		    { "Assembler", "FunctionalBlock" },
		    { "ProductionBlock", "FunctionalBlock" },
		    { "Refinery", "FunctionalBlock" },
		    { "ConveyorSorter", "FunctionalBlock" },
		    { "InteriorLight", "FunctionalBlock" },
		    { "AirVent", "FunctionalBlock" },
		    { "Collector", "FunctionalBlock" },
		    { "ShipConnector", "FunctionalBlock" },
		    { "MechanicalConnectionBlock", "FunctionalBlock" },
		    { "ExtendedPistonBase", "PistonBase" },
		    { "MotorStator", "FunctionalBlock" },
		    { "MotorBase", "FunctionalBlock" },
		    { "MotorAdvancedStator", "FunctionalBlock" },
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