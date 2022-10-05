using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game;
using Sandbox.Game.Gui;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;
using VRage;
using VRage.Game.ModAPI;
using VRage.Utils;

namespace Iv4xr.SePlugin
{
    public static class MyGuiScreenExtensions
    {
        public static MyGuiControlButton Button(this object screen, string fieldName)
        {
            return screen.GetInstanceFieldOrThrow<MyGuiControlButton>(fieldName);
        }

        public static MyGuiControlGrid Grid(this object screen, string fieldName)
        {
            return screen.GetInstanceFieldOrThrow<MyGuiControlGrid>(fieldName);
        }

        public static void ClickItem(this MyGuiControlGrid grid)
        {
            grid.CallMethod<object>("TryTriggerSingleClickEvent");
        }
        
        public static void DoubleClickItem(this MyGuiControlGrid grid)
        {
            grid.CallMethod<object>("DoubleClickItem");
        }

        public static MyGuiControlCheckbox CheckBox(this object screen, string fieldName)
        {
            return screen.GetInstanceFieldOrThrow<MyGuiControlCheckbox>(fieldName);
        }

        public static MyGuiControlSearchBox SearchBox(this object screen, string fieldName)
        {
            return screen.GetInstanceFieldOrThrow<MyGuiControlSearchBox>(fieldName);
        }
        
        public static MyGuiControlTextbox TextBox(this object screen, string fieldName)
        {
            return screen.GetInstanceFieldOrThrow<MyGuiControlTextbox>(fieldName);
        }

        public static MyGuiControlRadioButton RadioButton(this object screen, string fieldName)
        {
            return screen.GetInstanceFieldOrThrow<MyGuiControlRadioButton>(fieldName);
        }

        public static MyGuiControlButton ButtonByText(this MyGuiControls group, MyStringId stringId)
        {
            return group.GetInstanceFieldOrThrow<List<MyGuiControlBase>>("m_visibleControls").OfType<MyGuiControlButton>()
                    .First(x => x.Text == MyTexts.Get(stringId).ToString());
        }
        
        public static MyGuiControlButton ButtonByText(this MyGuiControlElementGroup group, MyStringId stringId)
        {
            return group.GetInstanceFieldOrThrow<List<MyGuiControlBase>>("m_controlElements").OfType<MyGuiControlButton>()
                    .First(x => x.Text == MyTexts.Get(stringId).ToString());
        }

        public static void ClickButton(this object screen, string fieldName)
        {
            var button = screen.Button(fieldName);
            button.ThrowIfCantUse(fieldName);
            button.PressButton();
        }

        public static void ClickRadio(this object screen, string fieldName)
        {
            var radio = screen.RadioButton(fieldName);
            radio.ThrowIfCantUse(fieldName);
            radio.Selected = true;
        }

        public static void ClickCheckBox(this object screen, string fieldName)
        {
            var radio = screen.CheckBox(fieldName);
            radio.ThrowIfCantUse(fieldName);
            radio.IsChecked = !radio.IsChecked;
        }

        public static void EnterSearchText(this object screen, string fieldName, string text)
        {
            var searchBox = screen.SearchBox(fieldName);
            searchBox.ThrowIfCantUse(fieldName);
            searchBox.SearchText = text;
        }
        
        public static void EnterText(this object screen, string fieldName, string text)
        {
            var searchBox = screen.TextBox(fieldName);
            searchBox.ThrowIfCantUse(fieldName);
            searchBox.SetText(new StringBuilder(text));
        }

        public static MyGuiControlTable Table(this MyGuiScreenBase screen, string fieldName)
        {
            return screen.GetInstanceFieldOrThrow<MyGuiControlTable>(fieldName);
        }
        
        public static MyGuiControlTable TableOrNull(this MyGuiScreenBase screen, string fieldName)
        {
            return screen.GetInstanceField<MyGuiControlTable>(fieldName);
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

        public static T LoadScreenData<T>(this MyGuiScreenBase screen, T data) where T : BaseScreenData
        {
            if (screen == null)
            {
                return null;
            }
            screen.ThrowIfNull("screen", "Cannot load screen data of null");
            data.Name = screen.DisplayName();
            data.IsLoaded = screen.IsLoaded;
            data.IsOpened = screen.IsOpened;
            data.SkipTransition = screen.SkipTransition;
            data.Cancelled = screen.Cancelled;
            data.Visible= screen.Visible;
            data.CloseButtonEnabled = screen.CloseButtonEnabled;
            return data;
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

        private static void ThrowIfCantUse(this MyGuiControlBase controlBase, string fieldName)
        {
            if (!controlBase.Enabled)
            {
                throw new InvalidOperationException($"Control {fieldName} of type {controlBase.GetType()} is disabled!");
            }

            if (!controlBase.Visible)
            {
                throw new InvalidOperationException($"Control {fieldName} of type {controlBase.GetType()} is not visible!");
            }
        }

        public static GuiControlBase ToControlBase(this MyGuiControlBase controlBase)
        {
            return new GuiControlBase()
            {
                Enabled = controlBase.Enabled,
                Visible = controlBase.Visible,
                Name = controlBase.Name,
            };
        }
    }
}
