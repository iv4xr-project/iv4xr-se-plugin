using System;
using System.Collections.Generic;
using Iv4xr.SpaceEngineers.Navigation;
using Iv4xr.SpaceEngineers.WorldModel;

namespace Iv4xr.SpaceEngineers
{
    public interface IScreens
    {
        string FocusedScreen();
        void WaitUntilTheGameLoaded();
        IMedicals Medicals { get; }
        ITerminal Terminal { get; }
    }

    public interface IMedicals
    {
        List<MedicalRoom> MedicalRooms();
        void Respawn(int roomIndex);
        List<Faction> Factions();
        void ChooseFaction(int factionIndex);
    }

    public interface ITerminal
    {
        TerminalScreenData Data();
        void SelectTab(int index);
        void ToggleProductionRepeatMode();
        void ToggleProductionCooperativeMode();
        void AddToProductionQueue(int index);
        void RemoveFromProductionQueue(int index);
        void SelectBlueprint(int index);
        void EnterBlueprintSearchBox(string text);
        void SelectAssembler(int index);
    }
}
