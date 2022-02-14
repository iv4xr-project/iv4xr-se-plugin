using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Sandbox.Game;
using Sandbox.Game.Gui;
using Sandbox.Graphics.GUI;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin
{
    public static class MyGuiScreenExtensions
    {
        public static MyGuiControlButton Button(this MyGuiScreenBase screen, string fieldName)
        {
            return screen.GetInstanceFieldOrThrow<MyGuiControlButton>(fieldName);
        }

        public static void Click(this MyGuiScreenBase screen, string fieldName)
        {
            screen.Button(fieldName).PressButton();
        }

        public static MyGuiControlTable Table(this MyGuiScreenBase screen, string fieldName)
        {
            return screen.GetInstanceFieldOrThrow<MyGuiControlTable>(fieldName);
        }

        public static List<MyGuiControlTable.Row> RowsAsList(this MyGuiControlTable table)
        {
            var list = new List<MyGuiControlTable.Row>();
            for (var i = 0; i < table.RowsCount; i++)
            {
                var row = table.GetRow(i);
                list.Add(row);
            }

            return list;
        }

        public static string DisplayName(this MyGuiScreenBase screen)
        {
            return screen.GetType().Name
                    .Replace("MyGuiScreen", "")
                    .Replace("My", "");
        }

        public static T EnsureFocusedScreen<T>() where T : MyGuiScreenBase
        {
            MyGuiScreenBase baseScreen = MyScreenManager.GetScreenWithFocus();
            if (!(baseScreen is T screen))
                throw new InvalidOperationException(
                    $"Screen of type {typeof(T)} does not have focus, {baseScreen} has.");
            return screen;
        }

        public static IEnumerable<TResult> GridItemUserDataOfType<TResult>(this MyGuiControlGrid source)
        {
            return source.Items.OfType<MyGuiGridItem>().Select(i => i.UserData).OfType<TResult>();
        }

        public static MyGuiControlTabPage ProductionTab(this MyGuiScreenTerminal terminal)
        {
            return terminal.GetTabs().Pages[(int)MyTerminalPageEnum.Production];
        }

        public static TType TabControlByName<TType>(this MyGuiControlTabPage tab, string name)
                where TType : MyGuiControlBase
        {
            return (TType)tab.Controls.GetControlByName(name);
        }

        public static TType ScrollableChild<TType>(this MyGuiControlScrollablePanel scrollablePanel)
                where TType : MyGuiControlBase
        {
            return (TType)scrollablePanel.Controls[0];
        }

        public static List<MyGuiControlCombobox.Item> ItemsAsList(this MyGuiControlCombobox combo)
        {
            var list = new List<MyGuiControlCombobox.Item>();
            for (var i = 0; i < combo.GetItemsCount(); i++)
            {
                list.Add(combo.GetItemByIndex(i));
            }

            return list;
        }
    }
}
