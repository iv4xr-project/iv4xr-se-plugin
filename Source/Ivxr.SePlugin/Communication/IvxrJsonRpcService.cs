using System.Collections.Generic;
using AustinHarris.JsonRpc;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;

namespace Iv4xr.SePlugin.Communication
{
    public class IvxrJsonRpcService : JsonRpcService
    {
        private readonly ISpaceEngineers m_se;

        public IvxrJsonRpcService(ISpaceEngineers se)
        {
            m_se = se;
        }

        [JsonRpcMethod("Items.Equip")]
        public void Equip(ToolbarLocation toolbarLocation)
        {
            m_se.Items.EquipToolbarItem(toolbarLocation.Slot, toolbarLocation.Page);
        }

        [JsonRpcMethod("Items.Place")]
        public void Place()
        {
            m_se.Items.PlaceBlock();
        }

        [JsonRpcMethod("Items.StartUsingTool")]
        public void StartUsingTool()
        {
            m_se.Items.BeginUseTool();
        }

        [JsonRpcMethod("Items.EndUsingTool")]
        public void EndUsingTool()
        {
            m_se.Items.EndUseTool();
        }

        [JsonRpcMethod("Items.SetToolbarItem")]
        public void SetToolbarItem(string name, ToolbarLocation location)
        {
            m_se.Items.SetToolbarItem(location.Slot, location.Page, name);
        }

        [JsonRpcMethod("Items.GetToolbar")]
        public Toolbar GetToolbar()
        {
            return m_se.Items.GetToolbar();
        }

        [JsonRpcMethod("Character.MoveAndRotate")]
        public Observation MoveAndRotate(Vector3D movement, Vector2 rotation3, float roll)
        {
            m_se.Character.Move(movement, rotation3, roll);
            return m_se.Observer.GetObservation(new ObservationArgs
                    {ObservationMode = ObservationMode.BASIC});
        }

        [JsonRpcMethod("Observer.Observe")]
        public Observation Observe()
        {
            return m_se.Observer.GetObservation(ObservationArgs.Default);
        }

        [JsonRpcMethod("Observer.ObserveBlocks")]
        public Observation ObserveBlocks()
        {
            return m_se.Observer.GetObservation(new ObservationArgs
                    {ObservationMode = ObservationMode.BLOCKS});
        }

        [JsonRpcMethod("Observer.ObserveNewBlocks")]
        public Observation ObserveNewBlocks()
        {
            var observationArgs = new ObservationArgs {ObservationMode = ObservationMode.NEW_BLOCKS};
            return m_se.Observer.GetObservation(observationArgs);
        }
        
        [JsonRpcMethod("Observer.TakeScreenshot")]
        public void TakeScreenshot(string absolutePath)
        {
            m_se.Observer.TakeScreenshot(absolutePath);
        }

        [JsonRpcMethod("Definitions.BlockDefinitions")]
        public List<BlockDefinition> BlockDefinitions()
        {
            return m_se.Definitions.BlockDefinitions();
        }
        
        [JsonRpcMethod("Definitions.AllDefinitions")]
        public List<DefinitionBase> AllDefinitions()
        {
            return m_se.Definitions.AllDefinitions();
        }

        [JsonRpcMethod("Session.LoadScenario")]
        public void LoadScenario(string scenarioPath)
        {
            m_se.Session.LoadScenario(scenarioPath);
        }
    }
}
