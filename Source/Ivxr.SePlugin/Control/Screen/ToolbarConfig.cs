using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Gui;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;
using VRage.Game;

namespace Iv4xr.SePlugin.Control.Screen
{
    public class ToolbarConfig : AbstractScreen<MyGuiScreenToolbarConfigBase, ToolbarConfigData>, IToolbarConfig
    {
        public ToolbarConfig() : base(ScreenCloseType.Normal)
        {
        }

        public override ToolbarConfigData Data()
        {
            return new ToolbarConfigData()
            {
                SearchText = Screen.SearchBox("m_searchBox").SearchText,
                GridItems = GridItems().Select(x => FromToolbarItem(x)?.ToDefinitionId()).ToList(),
                Categories = Screen.GetInstanceFieldOrThrow<MyGuiControlListbox>("m_categoriesListbox").Items.Select(x => x.Text.ToString()).ToList(),
                SelectedCategories = Screen.GetInstanceFieldOrThrow<MyGuiControlListbox>("m_categoriesListbox").SelectedItems.Select(x => x.Text.ToString()).ToList(),
            };
        }

        public MyDefinitionId? FromToolbarItem(MyToolbarItem toolbarItem)
        {
            if (toolbarItem == null)
            {
                return null;
            }
            if (toolbarItem is MyToolbarItemDefinition mtid)
            {
                return mtid.Definition.Id;
            }
            if (toolbarItem is MyToolbarItemTerminalBlock titb)
            {
                return titb.GetInstanceFieldOrThrow<MyTerminalBlock>("m_block").DefinitionId;
            }

            throw new InvalidOperationException($"Don't know what to do with class {toolbarItem.GetType()}");
        }

        public IEnumerable<MyToolbarItem> GridItems()
        {
            return Screen.GetInstanceFieldOrThrow<MyGuiControlGrid>("m_gridBlocks").Items
                    .Where(x => x != null)
                    .Select(x =>
                            MyToolbarItemFactory.CreateToolbarItem(
                                ((MyGuiScreenToolbarConfigBase.GridItemUserData)x.UserData).ItemData()));

        }

        public void Search(string text)
        {
            Screen.EnterSearchText("m_searchBox", text);
        }

        public void SelectCategory(int index)
        {
            var listbox = Screen.GetInstanceFieldOrThrow<MyGuiControlListbox>("m_categoriesListbox");
            listbox.FocusedItem = listbox.Items[index];
            listbox.CallMethod<object>("SelectFocusedItem", new object[] { true });
        }

        public void DropGridItemToToolbar(int gridLocation, int toolbarLocation)
        {
            MyGuiScreenToolbarConfigBase.DropGridItemToToolbar(GridItems().ToList()[gridLocation], toolbarLocation);
        }
    }
}
