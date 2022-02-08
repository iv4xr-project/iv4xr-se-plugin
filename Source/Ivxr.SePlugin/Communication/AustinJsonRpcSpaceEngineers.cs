using System.Collections.Generic;
using AustinHarris.JsonRpc;
using Iv4xr.PluginLib.Control;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.Navigation;
using Iv4xr.SpaceEngineers.WorldModel;

namespace Iv4xr.SePlugin.Communication
{
    public class AustinJsonRpcSpaceEngineers
    {
        private ISpaceEngineers m_se;

        public AustinJsonRpcSpaceEngineers(ISpaceEngineers se)
        {
            m_se = se;
        }

        public void Bind()
        {
            ServiceBinder.BindService(Handler.DefaultSessionId(), this);
        }

        [JsonRpcMethod("Character.MoveAndRotate")]
        CharacterObservation MoveAndRotate(PlainVec3D movement, PlainVec2F rotation3, float roll = 0, int ticks = 1)
        {
            return m_se.Character.MoveAndRotate(movement, rotation3, roll, ticks);
        }

        [JsonRpcMethod("Admin.Character.Teleport")]
        CharacterObservation Teleport(PlainVec3D position, PlainVec3D? orientationForward = null, PlainVec3D? orientationUp = null)
        {
            return m_se.Admin.Character.Teleport(position, orientationForward, orientationUp);
        }

        [JsonRpcMethod("Admin.Character.Use")]
        void Use(string blockId, int functionIndex, int action)
        {
            m_se.Admin.Character.Use(blockId, functionIndex, action);
        }

        [JsonRpcMethod("Admin.Character.Create")]
        CharacterObservation Create(string name, PlainVec3D position, PlainVec3D orientationForward, PlainVec3D orientationUp)
        {
            return m_se.Admin.Character.Create(name, position, orientationForward, orientationUp);
        }
        
        [JsonRpcMethod("Admin.Character.Switch")]
        void Switch(string id)
        {
            m_se.Admin.Character.Switch(id);
        }
        
        [JsonRpcMethod("Admin.Character.Remove")]
        void RemoveCharacter(string id)
        {
            m_se.Admin.Character.Remove(id);
        }

        [JsonRpcMethod("Admin.Observer.ObserveCharacters")]
        List<CharacterObservation> AdminObserveCharacters()
        {
            return m_se.Admin.Observer.ObserveCharacters();
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
        
        [JsonRpcMethod("Character.SwitchHelmet")]
        CharacterObservation SwitchHelmet()
        {
            return m_se.Character.SwitchHelmet();
        }
        
        [JsonRpcMethod("Character.Use")]
        void Use()
        {
            m_se.Character.Use();
        }
        
        [JsonRpcMethod("Screens.Medicals.MedicalRooms")]
        List<MedicalRoom> MedicalRooms()
        {
            return m_se.Screens.Medicals.MedicalRooms();
        }
        
        [JsonRpcMethod("Screens.Medicals.Respawn")]
        void Respawn(int roomIndex)
        {
            m_se.Screens.Medicals.Respawn(roomIndex);
        }
        
        [JsonRpcMethod("Screens.Medicals.Factions")]
        List<Faction> Factions()
        {
            return m_se.Screens.Medicals.Factions();
        }
        
        [JsonRpcMethod("Screens.Medicals.ChooseFaction")]
        void ChooseFaction(int factionIndex)
        {
            m_se.Screens.Medicals.ChooseFaction(factionIndex);
        }

        [JsonRpcMethod("Session.LoadScenario")]
        void LoadScenario(string scenarioPath)
        {
            m_se.Session.LoadScenario(scenarioPath);
        }
        
        [JsonRpcMethod("Session.Connect")]
        void Connect(string address)
        {
            m_se.Session.Connect(address);
        }
        
        [JsonRpcMethod("Session.Disconnect")]
        void Disconnect()
        {
            m_se.Session.Disconnect();
        }
        
        [JsonRpcMethod("Session.ExitGame")]
        void ExitGame()
        {
            m_se.Session.ExitGame();
        }
        
        [JsonRpcMethod("Screens.FocusedScreen")]
        string FocusedScreen()
        {
            return m_se.Screens.FocusedScreen();
        }
        
        [JsonRpcMethod("Screens.WaitUntilTheGameLoaded")]
        void WaitUntilTheGameLoaded()
        {
            m_se.Screens.WaitUntilTheGameLoaded();
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
        
        [JsonRpcMethod("Observer.ObserveCharacters")]
        List<CharacterObservation> ObserveCharacters()
        {
            return m_se.Observer.ObserveCharacters();
        }

        
        [JsonRpcMethod("Observer.SwitchCamera")]
        void SwitchCamera()
        {
            m_se.Observer.SwitchCamera();
        }

        [JsonRpcMethod("Observer.NavigationGraph")]
        NavGraph GetNavigationGraph()
        {
            return m_se.Observer.NavigationGraph();
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
        
        [JsonRpcMethod("Definitions.BlockHierarchy")]
        Dictionary<string, string> BlockHierarchy()
        {
            return m_se.Definitions.BlockHierarchy();
        }
        
        [JsonRpcMethod("Definitions.BlockDefinitionHierarchy")]
        Dictionary<string, string> BlockDefinitionHierarchy()
        {
            return m_se.Definitions.BlockDefinitionHierarchy();
        }

        [JsonRpcMethod("Blocks.Place")]
        void Place()
        {
            m_se.Blocks.Place();
        }

        [JsonRpcMethod("Admin.Blocks.PlaceAt")]
        string PlaceAt(DefinitionId blockDefinitionId, PlainVec3D position, PlainVec3D orientationForward, PlainVec3D orientationUp)
        {
            return m_se.Admin.Blocks.PlaceAt(blockDefinitionId, position, orientationForward, orientationUp);
        }

        [JsonRpcMethod("Admin.Blocks.PlaceInGrid")]
        string PlaceInGrid(DefinitionId blockDefinitionId, string gridId, PlainVec3I minPosition, PlainVec3I orientationForward,
            PlainVec3I orientationUp)
        {
            return m_se.Admin.Blocks.PlaceInGrid(blockDefinitionId, gridId, minPosition, orientationForward, orientationUp);
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

        [JsonRpcMethod("Admin.SetFrameLimitEnabled")]
        void SetFrameLimitEnabled(bool enabled)
        {
            m_se.Admin.SetFrameLimitEnabled(enabled);
        }

        [JsonRpcMethod("Character.BeginUsingTool")]
        void BeginUsingTool()
        {
            m_se.Character.BeginUsingTool();
        }

        [JsonRpcMethod("Character.EndUsingTool")]
        void EndUsingTool()
        {
            m_se.Character.EndUsingTool();
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

        [JsonRpcMethod("Items.Toolbar")]
        Toolbar Toolbar()
        {
            return m_se.Items.Toolbar();
        }
    }
}
