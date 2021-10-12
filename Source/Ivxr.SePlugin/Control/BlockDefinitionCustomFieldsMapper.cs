using Iv4xr.PluginLib.WorldModel;
using Sandbox.Definitions;

namespace Iv4xr.SePlugin.Control
{
    public class BlockDefinitionCustomFieldsMapper
    {
        public static void AddCustomFields(MyCubeBlockDefinition myBlockDefinition, BlockDefinition blockDefinition)
        {
            if (myBlockDefinition is MyLCDPanelsBlockDefinition myLcdPanelsBlockDefinition &&
                blockDefinition is LCDPanelsBlockDefinition lcdPanelsBlockDefinition)
            {
                lcdPanelsBlockDefinition.RequiredPowerInput = myLcdPanelsBlockDefinition.RequiredPowerInput;
            }
            
            if (myBlockDefinition is MyAirtightDoorGenericDefinition myAirtightDoorGenericDefinition  &&
                blockDefinition is AirtightDoorGenericDefinition airtightDoorGenericDefinition)
            {
                airtightDoorGenericDefinition.PowerConsumptionIdle = myAirtightDoorGenericDefinition.PowerConsumptionIdle;
                airtightDoorGenericDefinition.PowerConsumptionMoving = myAirtightDoorGenericDefinition.PowerConsumptionMoving;
                airtightDoorGenericDefinition.OpeningSpeed = myAirtightDoorGenericDefinition.OpeningSpeed;
            }
        }

    }
}
