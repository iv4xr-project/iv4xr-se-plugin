﻿using System.Linq;
using System.Text;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game;
using Sandbox.Graphics.GUI;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class InfoTab : AbstractTerminalTab<TerminalInfoData>, IInfoTab
    {
        public InfoTab() : base(MyTerminalPageEnum.Info, "m_controllerInfo")
        {
        }

        public void ConvertToShip()
        {
            var button = (MyGuiControlButton)InfoPage.Controls.GetControlByName("ConvertBtn");
            button.ThrowIfCantUse("Convert to ship");
            button.PressButton();
        }

        public void ConvertToStation()
        {
            var button = (MyGuiControlButton)InfoPage.Controls.GetControlByName("ConvertToStationBtn");
            button.ThrowIfCantUse("Convert to station");
            button.PressButton();
        }

        public void RenameGrid()
        {
            var button = (MyGuiControlButton)InfoPage.Controls.GetControlByName("RenameShipButton");
            button.ThrowIfCantUse("Rename button");
            button.PressButton();
        }

        public void EnterGridName(string name)
        {
            var gridNameControls = (MyGuiControlTextbox)InfoPage.GetControlByName("RenameShipText");
            gridNameControls.ThrowIfCantUse("Grid name");
            gridNameControls.SetText(new StringBuilder(name));
        }

        private MyGuiControlCheckbox CheckBoxByName(string name)
        {
            return (MyGuiControlCheckbox)InfoPage.GetControlByName(name);
        }

        public override TerminalInfoData Data()
        {
            MyGuiControlList list = (MyGuiControlList)InfoPage.Controls.GetControlByName("InfoList");

            return new TerminalInfoData()
            {
                GridInfo = string.Join("\n", list.Controls.OfType<MyGuiControlLabel>().Select(x => x.Text)),
                GridName = ((MyGuiControlTextbox)InfoPage.GetControlByName("RenameShipText")).Text,
                ShowCenterOfMass = CheckBoxByName("CenterBtn").IsChecked,
                ShowGravityRange = CheckBoxByName("ShowGravityGizmo").IsChecked,
                ShowSensorsFieldRange = CheckBoxByName("ShowSenzorGizmo").IsChecked,
                ShowAntennaRange = CheckBoxByName("ShowAntenaGizmo").IsChecked,
                ShowGridPivot = CheckBoxByName("PivotBtn").IsChecked,
            };
        }

        private MyGuiControlTabPage InfoPage =>
                UntypedController.GetInstanceFieldOrThrow<MyGuiControlTabPage>("m_infoPage");
    }
}
