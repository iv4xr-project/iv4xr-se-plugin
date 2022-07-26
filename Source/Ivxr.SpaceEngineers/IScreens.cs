using System.Collections.Generic;
using Iv4xr.SpaceEngineers.WorldModel;
using Iv4xr.SpaceEngineers.WorldModel.Screen;

namespace Iv4xr.SpaceEngineers
{
    [Role(Role.Game)]
    public interface IScreens
    {
        string FocusedScreen();
        void WaitUntilTheGameLoaded();
        IMedicals Medicals { get; }
        ITerminal Terminal { get; }
        IMainMenu MainMenu { get; }
        IMessageBox MessageBox { get; }
        IJoinGame JoinGame { get; }
        IServerConnect ServerConnect { get; }
        ILoadGame LoadGame { get; }
        INewGame NewGame { get; }
        IGamePlay GamePlay { get; }
        ISaveAs SaveAs { get; }
        IToolbarConfig ToolbarConfig { get; }
    }

    public interface IGamePlay
    {
        GamePlayData Data();
        void ShowMainMenu();
        void ShowToolbarConfig();
    }

    public interface IServerConnect
    {
        ServerConnectData Data();
        void Connect();
        void EnterAddress(string address);
        void ToggleAddServerToFavorites();
    }
    
    public interface IJoinGame
    {
        void DirectConnect();
        void JoinWorld();
        void Refresh();
        void ServerDetails();
        void SelectTab(int index);
        void SelectGame(int index);
        JoinGameData Data();
    }

    public interface IMainMenu
    {
        void Continue();
        void NewGame();
        void LoadGame();
        void JoinGame();
        void Options();
        void Character();
        void ExitToWindows();
        void ExitToMainMenu();
        void SaveAs();
        void Save();
        void Players();
    }

    public interface IMessageBox
    {
        void PressYes();
        void PressNo();
        MessageBoxData Data();
    }

    public interface IMedicals
    {
        MedicalsData Data();
        void SelectRespawn(int roomIndex);
        void Respawn();
        void SelectFaction(int factionIndex);
        void Join();
        void Refresh();
        void ShowMessageOfTheDay();
    }

    public interface ITerminal
    {
        TerminalScreenData Data();
        void SelectTab(int index);
        IInventoryTab Inventory { get; }
        IControlPanelTab ControlPanel { get;  }
        IProductionTab Production { get; }
        IInfoTab Info { get;  }
        IFactionsTab Factions { get;  }
        ICommsTab Comms { get;  }
        IGpsTab Gps { get;  }
        void Close();
    }

    public interface IGpsTab
    {
        
    }

    public interface ICommsTab
    {
        
    }

    public interface IFactionsTab
    {
        
    }

    public interface IControlPanelTab
    {
        TerminalControlPanelData Data();
        void FilterBlocks(string text);
        void EnterBlockGroup(string text);
        void GroupSave();
        void GroupDelete();
    }

    public interface IInfoTab
    {
        
    }
    
    public interface IProductionTab {
        TerminalProductionData Data();
        void ToggleProductionRepeatMode();
        void ToggleProductionCooperativeMode();
        void AddToProductionQueue(int index);
        void RemoveFromProductionQueue(int index);
        void SelectBlueprint(int index);
        void EnterBlueprintSearchBox(string text);
        void SelectAssembler(int index);
    }

    public interface IInventoryTab
    {
        TerminalInventoryData Data();
        void TransferInventoryItemToRight(int sourceInventoryId, int destinationInventoryId, int itemId);
        void TransferInventoryItemToLeft(int sourceInventoryId, int destinationInventoryId, int itemId);
        
        void DropSelected();
        void Withdraw();
        void Deposit();
        void FromBuildPlannerToProductionQueue();
        void SelectedToProductionQueue();
        IInventorySide Left { get; }
        IInventorySide Right { get; }
    }

    public interface IInventorySide
    {
        void Filter(string text);
        void SwapToGrid();
        void SwapToCharacterOrItem();
        void SelectItem(int index);
        void ClickSelectedItem();
        void DoubleClickSelectedItem();

        void FilterAll();
        void FilterEnergy();
        void FilterShip();
        void FilterSystem();
        void FilterStorage();

        void ToggleHideEmpty();
    }

    public interface ILoadGame
    {
        LoadGameData Data();
        void Filter(string text);
        void DoubleClickWorld(int index);
        void Load();
        void Edit();
        void Delete();
        void Save();
        void Publish();
    }

    public interface INewGame
    {
    }

    public interface ISaveAs
    {
        SaveAsData Data();
        void PressOk();
        void PressCancel();
        void SetName(string name);
    }

    public interface IToolbarConfig
    {
        ToolbarConfigData Data();
        void Close();
        void Search(string text);
        void DropGridItemToToolbar(int gridLocation, int toolbarLocation);
        void SelectCategory(int index);
    }
}
