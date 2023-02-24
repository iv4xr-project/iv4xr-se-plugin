using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Graphics.GUI;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class FactionsTab : AbstractTerminalTab<TerminalFactionsData>, IFactionsTab
    {
        public FactionsTab() : base(MyTerminalPageEnum.Factions, "m_controllerFactions")
        {
        }

        public override TerminalFactionsData Data()
        {
            return new TerminalFactionsData()
            {
                Factions = UntypedController.GetInstanceFieldOrThrow<MyGuiControlTable>("m_tableFactions").Rows.Select(
                    row =>
                            new Faction()
                            {
                                Tag = row.GetCell(0).Text?.ToString() ?? "",
                                Name = row.GetCell(1).Text?.ToString() ?? "",
                            }
                ).ToList(),
                SelectedFactionIndex =
                        UntypedController.GetInstanceFieldOrThrow<MyGuiControlTable>("m_tableFactions").SelectedRowIndex
            };
        }

        public void Create()
        {
            UntypedController.ClickButton("m_buttonCreate");
        }

        public void Join()
        {
            UntypedController.ClickButton("m_buttonJoin");
        }

        public void CancelJoin()
        {
            UntypedController.ClickButton("m_buttonCancelJoin");
        }

        public void Leave()
        {
            UntypedController.ClickButton("m_buttonLeave");
        }

        public void ProposePeace()
        {
            UntypedController.ClickButton("m_buttonSendPeace");
        }

        public void CancelRequest()
        {
            UntypedController.ClickButton("m_buttonCancelPeace");
        }

        public void SelectFaction(int index)
        {
            UntypedController.GetInstanceFieldOrThrow<MyGuiControlTable>("m_tableFactions").SelectedRowIndex = index;
        }


        public void AcceptPeace()
        {
            UntypedController.ClickButton("m_buttonAcceptPeace");
        }

        public void DeclareWar()
        {
            UntypedController.ClickButton("m_buttonMakeEnemy");
        }
    }
}
