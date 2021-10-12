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

    public class GravityGeneratorDefinition : BlockDefinition
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
            { "LCDPanelsBlockDefinition", "LCDPanelsBlockDefinition" },
            { "AirtightSlideDoorDefinition", "AirtightDoorGenericDefinition" },
        };
    }
}
