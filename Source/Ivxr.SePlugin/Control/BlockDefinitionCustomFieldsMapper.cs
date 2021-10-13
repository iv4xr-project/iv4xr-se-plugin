using Iv4xr.PluginLib.WorldModel;
using Sandbox.Definitions;

namespace Iv4xr.SePlugin.Control
{
    public static class BlockDefinitionCustomFieldsMapper
    {
        public static void AddCustomFields(MyCubeBlockDefinition myBlockDefinition, BlockDefinition blockDefinition)
        {
            if (myBlockDefinition is MyAirtightDoorGenericDefinition myAirtightDoorGenericDefinition  &&
                blockDefinition is AirtightDoorGenericDefinition airtightDoorGenericDefinition)
            {
                airtightDoorGenericDefinition.PowerConsumptionIdle = myAirtightDoorGenericDefinition.PowerConsumptionIdle;
                airtightDoorGenericDefinition.PowerConsumptionMoving = myAirtightDoorGenericDefinition.PowerConsumptionMoving;
                airtightDoorGenericDefinition.OpeningSpeed = myAirtightDoorGenericDefinition.OpeningSpeed;
            }
            
            if (myBlockDefinition is MyLCDPanelsBlockDefinition myLcdPanelsBlockDefinition &&
                blockDefinition is LCDPanelsBlockDefinition lcdPanelsBlockDefinition)
            {
                lcdPanelsBlockDefinition.RequiredPowerInput = myLcdPanelsBlockDefinition.RequiredPowerInput;
            }

            if (myBlockDefinition is MyPowerProducerDefinition myPowerProducerDefinition  &&
                blockDefinition is PowerProducerDefinition powerProducerDefinition)
            {
                powerProducerDefinition.MaxPowerOutput = myPowerProducerDefinition.MaxPowerOutput;
            }
            
            if (myBlockDefinition is MyGravityGeneratorDefinition myGravityGeneratorDefinition  &&
                blockDefinition is GravityGeneratorDefinition gravityGeneratorDefinition)
            {
                gravityGeneratorDefinition.RequiredPowerInput = myGravityGeneratorDefinition.RequiredPowerInput;
            }
            
            if (myBlockDefinition is MyGravityGeneratorBaseDefinition myGravityGeneratorBaseDefinition  &&
                blockDefinition is GravityGeneratorBaseDefinition gravityGeneratorBaseDefinition)
            {
                gravityGeneratorBaseDefinition.MinGravityAcceleration = myGravityGeneratorBaseDefinition.MinGravityAcceleration;
                gravityGeneratorBaseDefinition.MaxGravityAcceleration = myGravityGeneratorBaseDefinition.MaxGravityAcceleration;
            }

        }

    }
}
