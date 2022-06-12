using System.IO;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game.Screens;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;

namespace Iv4xr.SePlugin.Control.Screen
{
    public class LoadGameScreen : AbstractScreen<MyGuiScreenLoadSandbox, LoadGameData>, ILoadGame
    {
        private MyGuiControlSaveBrowser Browser =>
                Screen.GetInstanceFieldOrThrow<MyGuiControlSaveBrowser>("m_saveBrowser");

        public override LoadGameData Data()
        {
            var rows = Browser.RowsAsList().Select(
                row =>
                {
                    var cellText = row.GetCell(0).Text.ToString();
                    if (row.UserData is FileInfo info)
                    {
                        var file = info.ToFile();
                        file.Name = cellText;
                        return file;
                    }
                    else if (row.UserData is DirectoryInfo directoryInfo)
                    {
                        return directoryInfo.ToFile();
                    }
                    else if (row.UserData == null && cellText == "..")
                    {
                        var currDir = Browser.GetInstanceFieldOrThrow<DirectoryInfo>("m_currentDir");
                        var file = currDir.Parent.ToFile();
                        file.Name = "..";
                        return file;
                    }

                    throw new InvalidDataException("Unable to convert data from ControlDirectoryBrowser");
                });
            return new LoadGameData()
            {
                CurrentDirectory = Browser.GetInstanceFieldOrThrow<DirectoryInfo>("m_currentDir").ToFile(),
                RootDirectory = Browser.GetInstanceFieldOrThrow<DirectoryInfo>("m_topMostDir").ToFile(),
                Files = rows.ToList(),
            };
        }

        public void Filter(string text)
        {
            Screen.EnterSearchText("m_searchBox", text);
        }

        public void DoubleClickWorld(int index)
        {
            Browser.SelectedRowIndex = index;
            MyGuiControlTable browserAsTable = Browser;
            browserAsTable.CallMethod<object>("OnItemDoubleClicked",
                new object[]
                {
                    Browser,
                    new MyGuiControlTable.EventArgs() { RowIndex = index }
                }
            );
        }

        public void Load()
        {
            Screen.ClickButton("m_loadButton");
        }

        public void Edit()
        {
            Screen.ClickButton("m_editButton");
        }

        public void Delete()
        {
            Screen.ClickButton("m_deleteButton");
        }

        public void Save()
        {
            Screen.ClickButton("m_saveButton");
        }

        public void Publish()
        {
            Screen.ClickButton("m_publishButton");
        }
    }
}
