using System.Collections.Generic;
using AustinHarris.JsonRpc;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;

namespace Iv4xr.SePlugin.Communication
{
    public class AustinJsonRpcSpaceEngineers : JsonRpcService
    {
        private ISpaceEngineers m_se;

        public AustinJsonRpcSpaceEngineers(ISpaceEngineers se)
        {
            m_se = se;
        }

        [JsonRpcMethod("Character.MoveAndRotate")]
        Observation MoveAndRotate(Vector3 movement, Vector2 rotation3, float roll = 0)
        {
            return m_se.Character.MoveAndRotate(movement, rotation3, roll);
        }

        [JsonRpcMethod("Character.Teleport")]
        Observation Teleport(Vector3 position, Vector3? orientationForward = null, Vector3? orientationUp = null)
        {
            return m_se.Character.Teleport(position, orientationForward, orientationUp);
        }

        [JsonRpcMethod("Character.TurnOnJetpack")]
        Observation TurnOnJetpack()
        {
            return m_se.Character.TurnOnJetpack();
        }

        [JsonRpcMethod("Character.TurnOffJetpack")]
        Observation TurnOffJetpack()
        {
            return m_se.Character.TurnOffJetpack();
        }

        [JsonRpcMethod("Session.LoadScenario")]
        void LoadScenario(string scenarioPath)
        {
            m_se.Session.LoadScenario(scenarioPath);
        }

        [JsonRpcMethod("Observer.Observe")]
        Observation Observe()
        {
            return m_se.Observer.Observe();
        }

        [JsonRpcMethod("Observer.ObserveBlocks")]
        Observation ObserveBlocks()
        {
            return m_se.Observer.ObserveBlocks();
        }

        [JsonRpcMethod("Observer.ObserveNewBlocks")]
        Observation ObserveNewBlocks()
        {
            return m_se.Observer.ObserveNewBlocks();
        }

        [JsonRpcMethod("Observer.TakeScreenshot")]
        void TakeScreenshot(string absolutePath)
        {
            m_se.Observer.TakeScreenshot(absolutePath);
        }

        [JsonRpcMethod("Definitions.BlockDefinitions")]
        List<BlockDefinition> BlockDefinitions()
        {
            return m_se.Definitions.BlockDefinitions();
        }

        [JsonRpcMethod("Definitions.AllDefinitions")]
        List<DefinitionBase> AllDefinitions()
        {
            return m_se.Definitions.AllDefinitions();
        }

        [JsonRpcMethod("Items.Place")]
        void Place()
        {
            m_se.Items.Place();
        }

        [JsonRpcMethod("Items.PlaceAt")]
        void PlaceAt(string blockType, Vector3 position, Vector3 orientationForward, Vector3 orientationUp)
        {
            m_se.Items.PlaceAt(blockType, position, orientationForward, orientationUp);
        }

        [JsonRpcMethod("Items.BeginUsingTool")]
        void BeginUsingTool()
        {
            m_se.Items.BeginUsingTool();
        }

        [JsonRpcMethod("Items.EndUsingTool")]
        void EndUsingTool()
        {
            m_se.Items.EndUsingTool();
        }

        [JsonRpcMethod("Items.Equip")]
        void Equip(ToolbarLocation toolbarLocation)
        {
            m_se.Items.Equip(toolbarLocation);
        }

        [JsonRpcMethod("Items.SetToolbarItem")]
        void SetToolbarItem(string name, ToolbarLocation toolbarLocation)
        {
            m_se.Items.SetToolbarItem(name, toolbarLocation);
        }

        [JsonRpcMethod("Items.GetToolbar")]
        Toolbar GetToolbar()
        {
            return m_se.Items.GetToolbar();
        }
    }
}
