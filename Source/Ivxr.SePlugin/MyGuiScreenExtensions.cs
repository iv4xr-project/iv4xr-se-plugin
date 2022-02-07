using System;
using System.Collections.Generic;
using Sandbox.Graphics.GUI;

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

        public static T EnsureFocusedScreen<T>() where T: MyGuiScreenBase
        {
            MyGuiScreenBase baseScreen = MyScreenManager.GetScreenWithFocus();
            if (!(baseScreen is T screen))
                throw new InvalidOperationException($"Screen of type {typeof(T)} does not have focus, {baseScreen} has.");
            return screen;
        }
    }
}
