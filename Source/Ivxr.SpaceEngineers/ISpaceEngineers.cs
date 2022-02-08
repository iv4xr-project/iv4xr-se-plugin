using System;
using System.Collections.Generic;
using Iv4xr.SpaceEngineers.Navigation;
using Iv4xr.SpaceEngineers.WorldModel;

namespace Iv4xr.SpaceEngineers
{
    public interface ISpaceEngineers
    {
        ICharacterController Character { get; }
        ISessionController Session { get; }
        IItems Items { get; }
        IObserver Observer { get; }
        IDefinitions Definitions { get; }
        IBlocks Blocks { get; }
        ISpaceEngineersAdmin Admin { get; }
    }

    public interface ISessionController
    {
        void LoadScenario(string scenarioPath);
        void Connect(string address);
        void Disconnect();
    }

    public interface IObserver
    {
        CharacterObservation Observe();
        Observation ObserveBlocks();
        Observation ObserveNewBlocks();
        List<CharacterObservation> ObserveCharacters();
        NavGraph NavigationGraph();
        void SwitchCamera();
        void TakeScreenshot(string absolutePath);
    }

    public interface IItems
    {
        void Equip(ToolbarLocation toolbarLocation);

        void SetToolbarItem(string name, ToolbarLocation toolbarLocation);

        Toolbar Toolbar();
    }
    
    public interface IDefinitions
    {
        List<BlockDefinition> BlockDefinitions();
        List<DefinitionBase> AllDefinitions();
        Dictionary<string, string> BlockHierarchy();
        Dictionary<string, string> BlockDefinitionHierarchy();
    }
    
    public interface ICharacterController
    {
        CharacterObservation MoveAndRotate(PlainVec3D movement, PlainVec2F rotation3, float roll = 0, int ticks = 1);
        CharacterObservation TurnOnJetpack();
        CharacterObservation TurnOffJetpack();
        CharacterObservation SwitchHelmet();
        void BeginUsingTool();
        void EndUsingTool();
        void Use();
    }

    public interface IBlocks
    {
        void Place();
    }
}
