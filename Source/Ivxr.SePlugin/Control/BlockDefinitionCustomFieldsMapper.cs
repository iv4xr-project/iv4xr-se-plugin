// Generated automatically by BlockMappingGeneratorRunner.kt, do not change.

using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Definitions;

namespace Iv4xr.SePlugin.Control
{
    public static class BlockDefinitionCustomFieldsMapper
    {
        public static void AddCustomFields(MyCubeBlockDefinition myBlockDefinition, BlockDefinition blockDefinition)
        {
			if (myBlockDefinition is MyAirtightDoorGenericDefinition myAirtightDoorGenericDefinition && 
			    blockDefinition is AirtightDoorGenericDefinition airtightDoorGenericDefinition )
			{
			    airtightDoorGenericDefinition.PowerConsumptionIdle = myAirtightDoorGenericDefinition.PowerConsumptionIdle;
			    airtightDoorGenericDefinition.PowerConsumptionMoving = myAirtightDoorGenericDefinition.PowerConsumptionMoving;
			    airtightDoorGenericDefinition.OpeningSpeed = myAirtightDoorGenericDefinition.OpeningSpeed;    
			}
			if (myBlockDefinition is MyLCDPanelsBlockDefinition myLCDPanelsBlockDefinition && 
			    blockDefinition is LCDPanelsBlockDefinition lCDPanelsBlockDefinition )
			{
			    lCDPanelsBlockDefinition.RequiredPowerInput = myLCDPanelsBlockDefinition.RequiredPowerInput;    
			}
			if (myBlockDefinition is MyPowerProducerDefinition myPowerProducerDefinition && 
			    blockDefinition is PowerProducerDefinition powerProducerDefinition )
			{
			    powerProducerDefinition.MaxPowerOutput = myPowerProducerDefinition.MaxPowerOutput;    
			}
			if (myBlockDefinition is MyAdvancedDoorDefinition myAdvancedDoorDefinition && 
			    blockDefinition is AdvancedDoorDefinition advancedDoorDefinition )
			{
			    advancedDoorDefinition.PowerConsumptionIdle = myAdvancedDoorDefinition.PowerConsumptionIdle;
			    advancedDoorDefinition.PowerConsumptionMoving = myAdvancedDoorDefinition.PowerConsumptionMoving;    
			}
			if (myBlockDefinition is MyAirVentDefinition myAirVentDefinition && 
			    blockDefinition is AirVentDefinition airVentDefinition )
			{
			    airVentDefinition.StandbyPowerConsumption = myAirVentDefinition.StandbyPowerConsumption;
			    airVentDefinition.OperationalPowerConsumption = myAirVentDefinition.OperationalPowerConsumption;
			    airVentDefinition.VentilationCapacityPerSecond = myAirVentDefinition.VentilationCapacityPerSecond;    
			}
			if (myBlockDefinition is MyProductionBlockDefinition myProductionBlockDefinition && 
			    blockDefinition is ProductionBlockDefinition productionBlockDefinition )
			{
			    productionBlockDefinition.InventoryMaxVolume = myProductionBlockDefinition.InventoryMaxVolume;
			    productionBlockDefinition.StandbyPowerConsumption = myProductionBlockDefinition.StandbyPowerConsumption;
			    productionBlockDefinition.OperationalPowerConsumption = myProductionBlockDefinition.OperationalPowerConsumption;    
			}
			if (myBlockDefinition is MyAssemblerDefinition myAssemblerDefinition && 
			    blockDefinition is AssemblerDefinition assemblerDefinition )
			{
			    assemblerDefinition.AssemblySpeed = myAssemblerDefinition.AssemblySpeed;    
			}
			if (myBlockDefinition is MyBatteryBlockDefinition myBatteryBlockDefinition && 
			    blockDefinition is BatteryBlockDefinition batteryBlockDefinition )
			{
			    batteryBlockDefinition.MaxStoredPower = myBatteryBlockDefinition.MaxStoredPower;
			    batteryBlockDefinition.InitialStoredPowerRatio = myBatteryBlockDefinition.InitialStoredPowerRatio;
			    batteryBlockDefinition.RequiredPowerInput = myBatteryBlockDefinition.RequiredPowerInput;
			    batteryBlockDefinition.AdaptibleInput = myBatteryBlockDefinition.AdaptibleInput;    
			}
			if (myBlockDefinition is MyBeaconDefinition myBeaconDefinition && 
			    blockDefinition is BeaconDefinition beaconDefinition )
			{
			    beaconDefinition.MaxBroadcastRadius = myBeaconDefinition.MaxBroadcastRadius;
			    beaconDefinition.MaxBroadcastPowerDrainkW = myBeaconDefinition.MaxBroadcastPowerDrainkW;    
			}
			if (myBlockDefinition is MyButtonPanelDefinition myButtonPanelDefinition && 
			    blockDefinition is ButtonPanelDefinition buttonPanelDefinition )
			{
			    buttonPanelDefinition.ButtonCount = myButtonPanelDefinition.ButtonCount;    
			}
			if (myBlockDefinition is MyCameraBlockDefinition myCameraBlockDefinition && 
			    blockDefinition is CameraBlockDefinition cameraBlockDefinition )
			{
			    cameraBlockDefinition.RequiredPowerInput = myCameraBlockDefinition.RequiredPowerInput;
			    cameraBlockDefinition.RequiredChargingInput = myCameraBlockDefinition.RequiredChargingInput;
			    cameraBlockDefinition.MinFov = myCameraBlockDefinition.MinFov;
			    cameraBlockDefinition.MaxFov = myCameraBlockDefinition.MaxFov;
			    cameraBlockDefinition.RaycastConeLimit = myCameraBlockDefinition.RaycastConeLimit;
			    cameraBlockDefinition.RaycastDistanceLimit = myCameraBlockDefinition.RaycastDistanceLimit;
			    cameraBlockDefinition.RaycastTimeMultiplier = myCameraBlockDefinition.RaycastTimeMultiplier;    
			}
			if (myBlockDefinition is MyShipControllerDefinition myShipControllerDefinition && 
			    blockDefinition is ShipControllerDefinition shipControllerDefinition )
			{
			    shipControllerDefinition.EnableFirstPerson = myShipControllerDefinition.EnableFirstPerson;
			    shipControllerDefinition.EnableShipControl = myShipControllerDefinition.EnableShipControl;
			    shipControllerDefinition.EnableBuilderCockpit = myShipControllerDefinition.EnableBuilderCockpit;
			    shipControllerDefinition.IsDefault3rdView = myShipControllerDefinition.IsDefault3rdView;    
			}
			if (myBlockDefinition is MyCockpitDefinition myCockpitDefinition && 
			    blockDefinition is CockpitDefinition cockpitDefinition )
			{
			    cockpitDefinition.OxygenCapacity = myCockpitDefinition.OxygenCapacity;
			    cockpitDefinition.IsPressurized = myCockpitDefinition.IsPressurized;
			    cockpitDefinition.HasInventory = myCockpitDefinition.HasInventory;    
			}
			if (myBlockDefinition is MyConveyorSorterDefinition myConveyorSorterDefinition && 
			    blockDefinition is ConveyorSorterDefinition conveyorSorterDefinition )
			{
			    conveyorSorterDefinition.PowerInput = myConveyorSorterDefinition.PowerInput;    
			}
			if (myBlockDefinition is MyCryoChamberDefinition myCryoChamberDefinition && 
			    blockDefinition is CryoChamberDefinition cryoChamberDefinition )
			{
			    cryoChamberDefinition.IdlePowerConsumption = myCryoChamberDefinition.IdlePowerConsumption;    
			}
			if (myBlockDefinition is MyDecoyDefinition myDecoyDefinition && 
			    blockDefinition is DecoyDefinition decoyDefinition )
			{
			    decoyDefinition.LightningRodRadiusLarge = myDecoyDefinition.LightningRodRadiusLarge;
			    decoyDefinition.LightningRodRadiusSmall = myDecoyDefinition.LightningRodRadiusSmall;    
			}
			if (myBlockDefinition is MyDoorDefinition myDoorDefinition && 
			    blockDefinition is DoorDefinition doorDefinition )
			{
			    doorDefinition.MaxOpen = myDoorDefinition.MaxOpen;
			    doorDefinition.OpeningSpeed = myDoorDefinition.OpeningSpeed;    
			}
			if (myBlockDefinition is MyExhaustBlockDefinition myExhaustBlockDefinition && 
			    blockDefinition is ExhaustBlockDefinition exhaustBlockDefinition )
			{
			    exhaustBlockDefinition.RequiredPowerInput = myExhaustBlockDefinition.RequiredPowerInput;    
			}
			if (myBlockDefinition is MyFueledPowerProducerDefinition myFueledPowerProducerDefinition && 
			    blockDefinition is FueledPowerProducerDefinition fueledPowerProducerDefinition )
			{
			    fueledPowerProducerDefinition.FuelProductionToCapacityMultiplier = myFueledPowerProducerDefinition.FuelProductionToCapacityMultiplier;    
			}
			if (myBlockDefinition is MyGasFueledPowerProducerDefinition myGasFueledPowerProducerDefinition && 
			    blockDefinition is GasFueledPowerProducerDefinition gasFueledPowerProducerDefinition )
			{
			    gasFueledPowerProducerDefinition.FuelCapacity = myGasFueledPowerProducerDefinition.FuelCapacity;    
			}
			if (myBlockDefinition is MyGasTankDefinition myGasTankDefinition && 
			    blockDefinition is GasTankDefinition gasTankDefinition )
			{
			    gasTankDefinition.Capacity = myGasTankDefinition.Capacity;    
			}
			if (myBlockDefinition is MyGravityGeneratorBaseDefinition myGravityGeneratorBaseDefinition && 
			    blockDefinition is GravityGeneratorBaseDefinition gravityGeneratorBaseDefinition )
			{
			    gravityGeneratorBaseDefinition.MinGravityAcceleration = myGravityGeneratorBaseDefinition.MinGravityAcceleration;
			    gravityGeneratorBaseDefinition.MaxGravityAcceleration = myGravityGeneratorBaseDefinition.MaxGravityAcceleration;    
			}
			if (myBlockDefinition is MyGravityGeneratorDefinition myGravityGeneratorDefinition && 
			    blockDefinition is GravityGeneratorDefinition gravityGeneratorDefinition )
			{
			    gravityGeneratorDefinition.RequiredPowerInput = myGravityGeneratorDefinition.RequiredPowerInput;    
			}
			if (myBlockDefinition is MyGravityGeneratorSphereDefinition myGravityGeneratorSphereDefinition && 
			    blockDefinition is GravityGeneratorSphereDefinition gravityGeneratorSphereDefinition )
			{
			    gravityGeneratorSphereDefinition.MinRadius = myGravityGeneratorSphereDefinition.MinRadius;
			    gravityGeneratorSphereDefinition.MaxRadius = myGravityGeneratorSphereDefinition.MaxRadius;
			    gravityGeneratorSphereDefinition.BasePowerInput = myGravityGeneratorSphereDefinition.BasePowerInput;
			    gravityGeneratorSphereDefinition.ConsumptionPower = myGravityGeneratorSphereDefinition.ConsumptionPower;    
			}
			if (myBlockDefinition is MyGyroDefinition myGyroDefinition && 
			    blockDefinition is GyroDefinition gyroDefinition )
			{
			    gyroDefinition.ForceMagnitude = myGyroDefinition.ForceMagnitude;
			    gyroDefinition.RequiredPowerInput = myGyroDefinition.RequiredPowerInput;    
			}
			if (myBlockDefinition is MySoundBlockDefinition mySoundBlockDefinition && 
			    blockDefinition is SoundBlockDefinition soundBlockDefinition )
			{
			    soundBlockDefinition.MinRange = mySoundBlockDefinition.MinRange;
			    soundBlockDefinition.MaxRange = mySoundBlockDefinition.MaxRange;
			    soundBlockDefinition.MaxLoopPeriod = mySoundBlockDefinition.MaxLoopPeriod;
			    soundBlockDefinition.EmitterNumber = mySoundBlockDefinition.EmitterNumber;
			    soundBlockDefinition.LoopUpdateThreshold = mySoundBlockDefinition.LoopUpdateThreshold;    
			}
			if (myBlockDefinition is MyJumpDriveDefinition myJumpDriveDefinition && 
			    blockDefinition is JumpDriveDefinition jumpDriveDefinition )
			{
			    jumpDriveDefinition.RequiredPowerInput = myJumpDriveDefinition.RequiredPowerInput;
			    jumpDriveDefinition.PowerNeededForJump = myJumpDriveDefinition.PowerNeededForJump;
			    jumpDriveDefinition.MaxJumpDistance = myJumpDriveDefinition.MaxJumpDistance;
			    jumpDriveDefinition.MaxJumpMass = myJumpDriveDefinition.MaxJumpMass;
			    jumpDriveDefinition.JumpDelay = myJumpDriveDefinition.JumpDelay;    
			}
			if (myBlockDefinition is MyLandingGearDefinition myLandingGearDefinition && 
			    blockDefinition is LandingGearDefinition landingGearDefinition )
			{
			    landingGearDefinition.MaxLockSeparatingVelocity = myLandingGearDefinition.MaxLockSeparatingVelocity;    
			}
			if (myBlockDefinition is MyWeaponBlockDefinition myWeaponBlockDefinition && 
			    blockDefinition is WeaponBlockDefinition weaponBlockDefinition )
			{
			    weaponBlockDefinition.InventoryMaxVolume = myWeaponBlockDefinition.InventoryMaxVolume;
			    weaponBlockDefinition.InventoryFillFactorMin = myWeaponBlockDefinition.InventoryFillFactorMin;    
			}
			if (myBlockDefinition is MyLightingBlockDefinition myLightingBlockDefinition && 
			    blockDefinition is LightingBlockDefinition lightingBlockDefinition )
			{
			    lightingBlockDefinition.RequiredPowerInput = myLightingBlockDefinition.RequiredPowerInput;
			    lightingBlockDefinition.ReflectorConeDegrees = myLightingBlockDefinition.ReflectorConeDegrees;    
			}
			if (myBlockDefinition is MyMechanicalConnectionBlockBaseDefinition myMechanicalConnectionBlockBaseDefinition && 
			    blockDefinition is MechanicalConnectionBlockBaseDefinition mechanicalConnectionBlockBaseDefinition )
			{
			    mechanicalConnectionBlockBaseDefinition.SafetyDetach = myMechanicalConnectionBlockBaseDefinition.SafetyDetach;
			    mechanicalConnectionBlockBaseDefinition.SafetyDetachMin = myMechanicalConnectionBlockBaseDefinition.SafetyDetachMin;
			    mechanicalConnectionBlockBaseDefinition.SafetyDetachMax = myMechanicalConnectionBlockBaseDefinition.SafetyDetachMax;    
			}
			if (myBlockDefinition is MyMedicalRoomDefinition myMedicalRoomDefinition && 
			    blockDefinition is MedicalRoomDefinition medicalRoomDefinition )
			{
			    medicalRoomDefinition.RespawnAllowed = myMedicalRoomDefinition.RespawnAllowed;
			    medicalRoomDefinition.HealingAllowed = myMedicalRoomDefinition.HealingAllowed;
			    medicalRoomDefinition.RefuelAllowed = myMedicalRoomDefinition.RefuelAllowed;
			    medicalRoomDefinition.SuitChangeAllowed = myMedicalRoomDefinition.SuitChangeAllowed;
			    medicalRoomDefinition.CustomWardrobesEnabled = myMedicalRoomDefinition.CustomWardrobesEnabled;
			    medicalRoomDefinition.ForceSuitChangeOnRespawn = myMedicalRoomDefinition.ForceSuitChangeOnRespawn;
			    medicalRoomDefinition.SpawnWithoutOxygenEnabled = myMedicalRoomDefinition.SpawnWithoutOxygenEnabled;
			    medicalRoomDefinition.WardrobeCharacterOffsetLength = myMedicalRoomDefinition.WardrobeCharacterOffsetLength;    
			}
			if (myBlockDefinition is MyMergeBlockDefinition myMergeBlockDefinition && 
			    blockDefinition is MergeBlockDefinition mergeBlockDefinition )
			{
			    mergeBlockDefinition.Strength = myMergeBlockDefinition.Strength;    
			}
			if (myBlockDefinition is MyOreDetectorDefinition myOreDetectorDefinition && 
			    blockDefinition is OreDetectorDefinition oreDetectorDefinition )
			{
			    oreDetectorDefinition.MaximumRange = myOreDetectorDefinition.MaximumRange;    
			}
			if (myBlockDefinition is MyOxygenFarmDefinition myOxygenFarmDefinition && 
			    blockDefinition is OxygenFarmDefinition oxygenFarmDefinition )
			{
			    oxygenFarmDefinition.IsTwoSided = myOxygenFarmDefinition.IsTwoSided;
			    oxygenFarmDefinition.PanelOffset = myOxygenFarmDefinition.PanelOffset;
			    oxygenFarmDefinition.MaxGasOutput = myOxygenFarmDefinition.MaxGasOutput;
			    oxygenFarmDefinition.OperationalPowerConsumption = myOxygenFarmDefinition.OperationalPowerConsumption;    
			}
			if (myBlockDefinition is MyOxygenGeneratorDefinition myOxygenGeneratorDefinition && 
			    blockDefinition is OxygenGeneratorDefinition oxygenGeneratorDefinition )
			{
			    oxygenGeneratorDefinition.IceConsumptionPerSecond = myOxygenGeneratorDefinition.IceConsumptionPerSecond;
			    oxygenGeneratorDefinition.IsOxygenOnly = myOxygenGeneratorDefinition.IsOxygenOnly;
			    oxygenGeneratorDefinition.InventoryFillFactorMin = myOxygenGeneratorDefinition.InventoryFillFactorMin;
			    oxygenGeneratorDefinition.InventoryFillFactorMax = myOxygenGeneratorDefinition.InventoryFillFactorMax;
			    oxygenGeneratorDefinition.FuelPullAmountFromConveyorInMinutes = myOxygenGeneratorDefinition.FuelPullAmountFromConveyorInMinutes;    
			}
			if (myBlockDefinition is MyParachuteDefinition myParachuteDefinition && 
			    blockDefinition is ParachuteDefinition parachuteDefinition )
			{
			    parachuteDefinition.PowerConsumptionIdle = myParachuteDefinition.PowerConsumptionIdle;
			    parachuteDefinition.PowerConsumptionMoving = myParachuteDefinition.PowerConsumptionMoving;
			    parachuteDefinition.DragCoefficient = myParachuteDefinition.DragCoefficient;
			    parachuteDefinition.ReefAtmosphereLevel = myParachuteDefinition.ReefAtmosphereLevel;
			    parachuteDefinition.MinimumAtmosphereLevel = myParachuteDefinition.MinimumAtmosphereLevel;
			    parachuteDefinition.RadiusMultiplier = myParachuteDefinition.RadiusMultiplier;    
			}
			if (myBlockDefinition is MyPistonBaseDefinition myPistonBaseDefinition && 
			    blockDefinition is PistonBaseDefinition pistonBaseDefinition )
			{
			    pistonBaseDefinition.Minimum = myPistonBaseDefinition.Minimum;
			    pistonBaseDefinition.Maximum = myPistonBaseDefinition.Maximum;
			    pistonBaseDefinition.MaxVelocity = myPistonBaseDefinition.MaxVelocity;
			    pistonBaseDefinition.RequiredPowerInput = myPistonBaseDefinition.RequiredPowerInput;
			    pistonBaseDefinition.MaxImpulse = myPistonBaseDefinition.MaxImpulse;
			    pistonBaseDefinition.DefaultMaxImpulseAxis = myPistonBaseDefinition.DefaultMaxImpulseAxis;
			    pistonBaseDefinition.DefaultMaxImpulseNonAxis = myPistonBaseDefinition.DefaultMaxImpulseNonAxis;
			    pistonBaseDefinition.UnsafeImpulseThreshold = myPistonBaseDefinition.UnsafeImpulseThreshold;    
			}
			if (myBlockDefinition is MyProjectorDefinition myProjectorDefinition && 
			    blockDefinition is ProjectorDefinition projectorDefinition )
			{
			    projectorDefinition.RequiredPowerInput = myProjectorDefinition.RequiredPowerInput;
			    projectorDefinition.AllowScaling = myProjectorDefinition.AllowScaling;
			    projectorDefinition.AllowWelding = myProjectorDefinition.AllowWelding;
			    projectorDefinition.IgnoreSize = myProjectorDefinition.IgnoreSize;
			    projectorDefinition.RotationAngleStepDeg = myProjectorDefinition.RotationAngleStepDeg;    
			}
			if (myBlockDefinition is MyRadioAntennaDefinition myRadioAntennaDefinition && 
			    blockDefinition is RadioAntennaDefinition radioAntennaDefinition )
			{
			    radioAntennaDefinition.MaxBroadcastRadius = myRadioAntennaDefinition.MaxBroadcastRadius;
			    radioAntennaDefinition.LightningRodRadiusLarge = myRadioAntennaDefinition.LightningRodRadiusLarge;
			    radioAntennaDefinition.LightningRodRadiusSmall = myRadioAntennaDefinition.LightningRodRadiusSmall;    
			}

        }
    }
}
    
