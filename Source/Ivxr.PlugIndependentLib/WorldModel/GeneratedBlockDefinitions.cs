using System.Collections.Generic;

namespace Iv4xr.PluginLib.WorldModel
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
	public class GravityGeneratorDefinition : GravityGeneratorBaseDefinition 
	{
	    public float RequiredPowerInput;
	}
	public class GravityGeneratorBaseDefinition : BlockDefinition 
	{
	    public float MinGravityAcceleration;
	    public float MaxGravityAcceleration;
	}
	public static class BlockDefinitionMapper
	{
	    public static readonly Dictionary<string, string> Mapping = new Dictionary<string, string>
	    {
		    {"AirtightHangarDoorDefinition", "AirtightDoorGenericDefinition"},
		    {"AirtightSlideDoorDefinition", "AirtightDoorGenericDefinition"},
		    {"BatteryBlockDefinition", "PowerProducerDefinition"},
		    {"ReactorDefinition", "PowerProducerDefinition"},
		    {"FueledPowerProducerDefinition", "PowerProducerDefinition"},
		    {"HydrogenEngineDefinition", "PowerProducerDefinition"},
		    {"GasFueledPowerProducerDefinition", "PowerProducerDefinition"},
		    {"WindTurbineDefinition", "PowerProducerDefinition"},
		    {"SolarPanelDefinition", "PowerProducerDefinition"},
		    {"GravityGeneratorSphereDefinition", "GravityGeneratorBaseDefinition"},
        };
    }
}