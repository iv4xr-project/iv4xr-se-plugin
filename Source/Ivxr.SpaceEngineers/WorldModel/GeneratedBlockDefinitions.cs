// Generated automatically by BlockMappingGeneratorRunner.kt, do not change.

using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel
{
	public class AirtightDoorGenericDefinition : BlockDefinition 
	{
	    public float PowerConsumptionIdle;
	    public float PowerConsumptionMoving;
	    public float OpeningSpeed;
	}

	public class LCDPanelsBlockDefinition : BlockDefinition 
	{
	    public float RequiredPowerInput;
	}

	public class PowerProducerDefinition : BlockDefinition 
	{
	    public float MaxPowerOutput;
	}

	public class AdvancedDoorDefinition : BlockDefinition 
	{
	    public float PowerConsumptionIdle;
	    public float PowerConsumptionMoving;
	}

	public class AirVentDefinition : BlockDefinition 
	{
	    public float StandbyPowerConsumption;
	    public float OperationalPowerConsumption;
	    public float VentilationCapacityPerSecond;
	}

	public class ProductionBlockDefinition : BlockDefinition 
	{
	    public float InventoryMaxVolume;
	    public float StandbyPowerConsumption;
	    public float OperationalPowerConsumption;
	}

	public class AssemblerDefinition : ProductionBlockDefinition 
	{
	    public float AssemblySpeed;
	}

	public class BatteryBlockDefinition : PowerProducerDefinition 
	{
	    public float MaxStoredPower;
	    public float InitialStoredPowerRatio;
	    public float RequiredPowerInput;
	    public bool AdaptibleInput;
	}

	public class BeaconDefinition : BlockDefinition 
	{
	    public float MaxBroadcastRadius;
	    public float MaxBroadcastPowerDrainkW;
	}

	public class ButtonPanelDefinition : BlockDefinition 
	{
	    public int ButtonCount;
	}

	public class CameraBlockDefinition : BlockDefinition 
	{
	    public float RequiredPowerInput;
	    public float RequiredChargingInput;
	    public float MinFov;
	    public float MaxFov;
	    public float RaycastConeLimit;
	    public double RaycastDistanceLimit;
	    public float RaycastTimeMultiplier;
	}

	public class ShipControllerDefinition : BlockDefinition 
	{
	    public bool EnableFirstPerson;
	    public bool EnableShipControl;
	    public bool EnableBuilderCockpit;
	    public bool IsDefault3rdView;
	}

	public class CockpitDefinition : ShipControllerDefinition 
	{
	    public float OxygenCapacity;
	    public bool IsPressurized;
	    public bool HasInventory;
	}

	public class ConveyorSorterDefinition : BlockDefinition 
	{
	    public float PowerInput;
	}

	public class CryoChamberDefinition : CockpitDefinition 
	{
	    public float IdlePowerConsumption;
	}

	public class DecoyDefinition : BlockDefinition 
	{
	    public float LightningRodRadiusLarge;
	    public float LightningRodRadiusSmall;
	}

	public class DoorDefinition : BlockDefinition 
	{
	    public float MaxOpen;
	    public float OpeningSpeed;
	}

	public class ExhaustBlockDefinition : BlockDefinition 
	{
	    public float RequiredPowerInput;
	}

	public class FueledPowerProducerDefinition : PowerProducerDefinition 
	{
	    public float FuelProductionToCapacityMultiplier;
	}

	public class GasFueledPowerProducerDefinition : FueledPowerProducerDefinition 
	{
	    public float FuelCapacity;
	}

	public class GasTankDefinition : ProductionBlockDefinition 
	{
	    public float Capacity;
	}

	public class GravityGeneratorBaseDefinition : BlockDefinition 
	{
	    public float MinGravityAcceleration;
	    public float MaxGravityAcceleration;
	}

	public class GravityGeneratorDefinition : GravityGeneratorBaseDefinition 
	{
	    public float RequiredPowerInput;
	}

	public class GravityGeneratorSphereDefinition : GravityGeneratorBaseDefinition 
	{
	    public float MinRadius;
	    public float MaxRadius;
	    public float BasePowerInput;
	    public float ConsumptionPower;
	}

	public class GyroDefinition : BlockDefinition 
	{
	    public float ForceMagnitude;
	    public float RequiredPowerInput;
	}

	public class SoundBlockDefinition : BlockDefinition 
	{
	    public float MinRange;
	    public float MaxRange;
	    public float MaxLoopPeriod;
	    public int EmitterNumber;
	    public int LoopUpdateThreshold;
	}

	public class JumpDriveDefinition : BlockDefinition 
	{
	    public float RequiredPowerInput;
	    public float PowerNeededForJump;
	    public double MinJumpDistance;
	    public double MaxJumpDistance;
	    public double MaxJumpMass;
	}

	public class LandingGearDefinition : BlockDefinition 
	{
	    public float MaxLockSeparatingVelocity;
	}

	public class WeaponBlockDefinition : BlockDefinition 
	{
	    public float InventoryMaxVolume;
	    public float InventoryFillFactorMin;
	}

	public class LightingBlockDefinition : BlockDefinition 
	{
	    public float RequiredPowerInput;
	    public float ReflectorConeDegrees;
	}

	public class MechanicalConnectionBlockBaseDefinition : BlockDefinition 
	{
	    public float SafetyDetach;
	    public float SafetyDetachMin;
	    public float SafetyDetachMax;
	}

	public class MedicalRoomDefinition : BlockDefinition 
	{
	    public bool RespawnAllowed;
	    public bool HealingAllowed;
	    public bool RefuelAllowed;
	    public bool SuitChangeAllowed;
	    public bool CustomWardrobesEnabled;
	    public bool ForceSuitChangeOnRespawn;
	    public bool SpawnWithoutOxygenEnabled;
	    public float WardrobeCharacterOffsetLength;
	}

	public class MergeBlockDefinition : BlockDefinition 
	{
	    public float Strength;
	}

	public class OreDetectorDefinition : BlockDefinition 
	{
	    public float MaximumRange;
	}

	public class OxygenFarmDefinition : BlockDefinition 
	{
	    public bool IsTwoSided;
	    public float PanelOffset;
	    public float MaxGasOutput;
	    public float OperationalPowerConsumption;
	}

	public class OxygenGeneratorDefinition : ProductionBlockDefinition 
	{
	    public float IceConsumptionPerSecond;
	    public bool IsOxygenOnly;
	    public float InventoryFillFactorMin;
	    public float InventoryFillFactorMax;
	    public float FuelPullAmountFromConveyorInMinutes;
	}

	public class ParachuteDefinition : BlockDefinition 
	{
	    public float PowerConsumptionIdle;
	    public float PowerConsumptionMoving;
	    public float DragCoefficient;
	    public float ReefAtmosphereLevel;
	    public float MinimumAtmosphereLevel;
	    public float RadiusMultiplier;
	}

	public class PistonBaseDefinition : MechanicalConnectionBlockBaseDefinition 
	{
	    public float Minimum;
	    public float Maximum;
	    public float MaxVelocity;
	    public float RequiredPowerInput;
	    public float MaxImpulse;
	    public float DefaultMaxImpulseAxis;
	    public float DefaultMaxImpulseNonAxis;
	    public float UnsafeImpulseThreshold;
	}

	public class ProjectorDefinition : BlockDefinition 
	{
	    public float RequiredPowerInput;
	    public bool AllowScaling;
	    public bool AllowWelding;
	    public bool IgnoreSize;
	    public int RotationAngleStepDeg;
	}

	public class RadioAntennaDefinition : BlockDefinition 
	{
	    public float MaxBroadcastRadius;
	    public float LightningRodRadiusLarge;
	    public float LightningRodRadiusSmall;
	}

	public static class BlockDefinitionMapper
	{
	    public static readonly Dictionary<string, string> Mapping = new Dictionary<string, string>
	    {
		    { "RemoteControlDefinition", "ShipControllerDefinition" },
		    { "JukeboxDefinition", "SoundBlockDefinition" },
		    { "ReflectorBlockDefinition", "LightingBlockDefinition" },
		    { "AirtightHangarDoorDefinition", "AirtightDoorGenericDefinition" },
		    { "AirtightSlideDoorDefinition", "AirtightDoorGenericDefinition" },
		    { "ReactorDefinition", "FueledPowerProducerDefinition" },
		    { "HydrogenEngineDefinition", "GasFueledPowerProducerDefinition" },
		    { "WindTurbineDefinition", "PowerProducerDefinition" },
		    { "SolarPanelDefinition", "PowerProducerDefinition" },
		    { "ExtendedPistonBaseDefinition", "PistonBaseDefinition" },
		    { "MotorStatorDefinition", "MechanicalConnectionBlockBaseDefinition" },
		    { "MotorAdvancedStatorDefinition", "MechanicalConnectionBlockBaseDefinition" },
		    { "RefineryDefinition", "ProductionBlockDefinition" },
		    { "SurvivalKitDefinition", "AssemblerDefinition" },
		    { "MotorSuspensionDefinition", "MechanicalConnectionBlockBaseDefinition" },
		    { "LargeTurretBaseDefinition", "WeaponBlockDefinition" },
        };
    }
}