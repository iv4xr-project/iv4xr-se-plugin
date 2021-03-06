﻿using System.Collections.Generic;
using AustinHarris.JsonRpc;
using Iv4xr.PluginLib.Control;
using Iv4xr.PluginLib.WorldModel;

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
        CharacterObservation MoveAndRotate(PlainVec3D movement, PlainVec2F rotation3, float roll = 0)
        {
            return m_se.Character.MoveAndRotate(movement, rotation3, roll);
        }

        [JsonRpcMethod("Admin.Character.Teleport")]
        CharacterObservation Teleport(PlainVec3D position, PlainVec3D? orientationForward = null, PlainVec3D? orientationUp = null)
        {
            return m_se.Admin.Character.Teleport(position, orientationForward, orientationUp);
        }

        [JsonRpcMethod("Character.TurnOnJetpack")]
        CharacterObservation TurnOnJetpack()
        {
            return m_se.Character.TurnOnJetpack();
        }

        [JsonRpcMethod("Character.TurnOffJetpack")]
        CharacterObservation TurnOffJetpack()
        {
            return m_se.Character.TurnOffJetpack();
        }

        [JsonRpcMethod("Session.LoadScenario")]
        void LoadScenario(string scenarioPath)
        {
            m_se.Session.LoadScenario(scenarioPath);
        }

        [JsonRpcMethod("Observer.Observe")]
        CharacterObservation Observe()
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

        [JsonRpcMethod("Blocks.Place")]
        void Place()
        {
            m_se.Blocks.Place();
        }

        [JsonRpcMethod("Admin.Blocks.PlaceAt")]
        void PlaceAt(string blockType, PlainVec3D position, PlainVec3D orientationForward, PlainVec3D orientationUp)
        {
            m_se.Admin.Blocks.PlaceAt(blockType, position, orientationForward, orientationUp);
        }
        
        [JsonRpcMethod("Admin.Blocks.Remove")]
        void Remove(string blockId)
        {
            m_se.Admin.Blocks.Remove(blockId);
        }
        
        [JsonRpcMethod("Admin.Blocks.SetIntegrity")]
        void SetIntegrity(string blockId, float integrity)
        {
            m_se.Admin.Blocks.SetIntegrity(blockId, integrity);
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
