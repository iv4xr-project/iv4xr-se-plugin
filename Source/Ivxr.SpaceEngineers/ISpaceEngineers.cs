using System.Collections.Generic;
using Iv4xr.SpaceEngineers.Navigation;
using Iv4xr.SpaceEngineers.WorldModel;
using static Iv4xr.SpaceEngineers.Role;

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
        IScreens Screens { get; }
    }
    
    public interface ISessionController
    {
        [Role(Admin)]
        void LoadScenario(string scenarioPath);
        
        [Role(Game)]
        void Connect(string address);
        
        [Role(Game)]
        void Disconnect();
        
        void ExitGame();
        
        [Role(Game)]
        void ExitToMainMenu();
    }
    
    public interface IObserver
    {
        CharacterObservation Observe();
        Observation ObserveBlocks();
        Observation ObserveNewBlocks();
        List<CharacterObservation> ObserveCharacters();
        List<FloatingObject> ObserveFloatingObjects();
        NavGraph NavigationGraph();
        void SwitchCamera();
        void TakeScreenshot(string absolutePath);
    }

    [Role(Game)]
    public interface IItems
    {
        void Equip(ToolbarLocation toolbarLocation);

        void SetToolbarItem(DefinitionId definitionId, ToolbarLocation toolbarLocation);

        Toolbar Toolbar();
    }
    
    public interface IDefinitions
    {
        List<BlockDefinition> BlockDefinitions();
        List<DefinitionBase> AllDefinitions();
        Dictionary<string, string> BlockHierarchy();
        Dictionary<string, string> BlockDefinitionHierarchy();
    }
    
    [Role(Game)]
    public interface ICharacterController
    {
        CharacterObservation MoveAndRotate(PlainVec3D movement, PlainVec2F rotation3, float roll = 0, int ticks = 1);
        CharacterObservation TurnOnJetpack();
        CharacterObservation TurnOffJetpack();
        CharacterObservation TurnOnDampeners();
        CharacterObservation TurnOffDampeners();
        CharacterObservation SwitchHelmet();
        void BeginUsingTool();
        void EndUsingTool();
        void Use();
        void ShowTerminal();
        void ShowInventory();
    }

    [Role(Game)]
    public interface IBlocks
    {
        void Place();
    }
}
