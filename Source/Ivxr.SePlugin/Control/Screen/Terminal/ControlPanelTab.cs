using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.GameSystems;
using Sandbox.Game.Gui;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class ControlPanelTab : AbstractTerminalTab<TerminalControlPanelData>, IControlPanelTab

    {
        private readonly BlockEntityBuilder Builder = new BlockEntityBuilder();

        public ControlPanelTab() : base(MyTerminalPageEnum.ControlPanel, "m_controllerControlPanel")
        {
        }

        public void GroupSave()
        {
            UntypedController.ClickButton("m_groupSave");
        }

        public void GroupDelete()
        {
            UntypedController.ClickButton("m_groupDelete");
        }

        private IBlockOrGroupItem FromItem(MyGuiControlListbox.Item item)
        {
            var userObject = item.UserData;
            switch (userObject)
            {
                case MyBlockGroup blockGroup:
                {
                    var blocks = blockGroup.GetInstanceFieldOrThrow<HashSet<MyTerminalBlock>>("Blocks");
                    return new BlockGroupItem(item.Visible, item.Text.ToString())
                    {
                        Name = blockGroup.Name.ToString(),
                        Blocks = blocks.Select(block => Builder.CreateAndFill(block.SlimBlock)).ToList()
                    };
                }
                case MyTerminalBlock block:
                    return new BlockItem(item.Visible, item.Text.ToString())
                    {
                        Block = Builder.CreateAndFill(block.SlimBlock)
                    };
                default:
                    throw new InvalidOperationException(
                        $"Cannot create item from list of type {userObject.GetType().Name}");
            }
        }

        TControlType ControlById<TControlType>(IEnumerable<ITerminalControl> basicControls, string id)
                where TControlType : ITerminalControl
        {
            Console.WriteLine(id);
            return (TControlType)basicControls.FirstOrDefault(control => control.Id == id)
                    .ThrowIfNull(
                        $"Couldn't find control {id}, found: {string.Join(",", basicControls.Select(bc => bc.Id))}");
        }

        bool GetSwitchById<TBlockType>(string id) where TBlockType : MyTerminalBlock
        {
            return ControlById<MyTerminalControlOnOffSwitch<TBlockType>>(
                        MyTerminalControlFactory.GetControls(typeof(TBlockType)), id)
                    .GetInstanceFieldOrThrow<MyGuiControlOnOffSwitch>("m_onOffSwitch").Value;
        }

        private MyGuiControlGenericFunctionalBlock BlockControl =>
                UntypedController.GetInstanceFieldOrThrow<MyGuiControlGenericFunctionalBlock>("blockControl");

        private MyGuiControlCombobox TransferToCombobox =>
                BlockControl.GetInstanceFieldOrThrow<MyGuiControlCombobox>("m_transferToCombobox");

        private MyGuiControlCombobox ShareModeCombobox =>
                BlockControl.GetInstanceFieldOrThrow<MyGuiControlCombobox>("m_shareModeCombobox");

        public void SelectShareMode(int index)
        {
            ShareModeCombobox.SelectItemByIndex(index);
        }

        public void TransferTo(int index)
        {
            TransferToCombobox.SelectItemByIndex(index);
        }

        public override TerminalControlPanelData Data()
        {
            var listBox = UntypedController.GetInstanceFieldOrThrow<MyGuiControlListbox>("m_blockListbox");


            return new TerminalControlPanelData()
            {
                Search = UntypedController.SearchBox("m_searchBox").SearchText,
                NewGroupName = UntypedController.TextBox("m_groupName").Text,
                GridBlocks = listBox.Items.Select(FromItem).ToList(),
                ToggleBlock = GetSwitchById<MyFunctionalBlock>("OnOff"),
                ShowBlockInTerminal = GetSwitchById<MyTerminalBlock>("ShowInTerminal"),
                ShowBLockInToolbarConfig = GetSwitchById<MyTerminalBlock>("ShowInToolbarConfig"),
                ShowOnHUD = GetSwitchById<MyTerminalBlock>("ShowOnHUD"),
                Owner = BlockControl.GetInstanceFieldOrThrow<MyGuiControlLabel>("m_ownerLabel").Text ?? "",
                TransferTo = TransferToCombobox.GetInstanceFieldOrThrow<List<MyGuiControlCombobox.Item>>("m_items")
                        .Select(i => i.Value.ToString()).ToList(),
                ShareBlock = ShareModeCombobox.GetInstanceFieldOrThrow<List<MyGuiControlCombobox.Item>>("m_items")
                        .Select(i => i.Value.ToString()).ToList(),
                ShareBlockSelectedIndex = ShareModeCombobox.GetSelectedIndex().ToNullIfMinusOne(),
            };
        }

        public void FilterBlocks(string text)
        {
            UntypedController.SearchBox("m_searchBox").SearchText = text;
        }

        public void EnterBlockGroup(string text)
        {
            UntypedController.TextBox("m_groupName").Text = text;
        }
    }
}
