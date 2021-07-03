using System.Collections.Generic;
using Iv4xr.PluginLib.WorldModel;

namespace Iv4xr.PluginLib.Control
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
    }

    public interface IObserver
    {
        CharacterObservation Observe();
        Observation ObserveBlocks();
        Observation ObserveNewBlocks();
        void TakeScreenshot(string absolutePath);
    }

    public interface IItems
    {
        void BeginUsingTool();

        void EndUsingTool();

        void Equip(ToolbarLocation toolbarLocation);

        void SetToolbarItem(string name, ToolbarLocation toolbarLocation);

        Toolbar GetToolbar();
    }
    
    public interface IDefinitions
    {
        List<BlockDefinition> BlockDefinitions();
        List<DefinitionBase> AllDefinitions();
    }
    
    public interface ICharacterController
    {
        CharacterObservation MoveAndRotate(PlainVec3D movement, PlainVec2F rotation3, float roll = 0);
        CharacterObservation TurnOnJetpack();
        CharacterObservation TurnOffJetpack();
    }

    public interface IBlocks
    {
        void Place();
    }
}
