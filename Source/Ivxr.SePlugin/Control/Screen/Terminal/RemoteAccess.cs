using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game.Gui;
using Sandbox.Graphics.GUI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class RemoteAccess : AbstractScreen<MyGuiScreenTerminal, RemoteAccessData>, IRemoteAccess
    {
        public override RemoteAccessData Data()
        {
            var grids = (Screen.GetInstanceFieldOrThrow<MyGuiControlParent>("m_propertiesTableParent")
                            .Controls.GetControlByName("ShipsData") as MyGuiControlTable).RowsAsList()
                    .Select(RowToGridData)
                    .ToList();
            return new RemoteAccessData()
            {
                Grids = grids,
            };
        }

        private static RemoteGridData RowToGridData(MyGuiControlTable.Row row)
        {
            return new RemoteGridData()
            {
                Name = row.GetCell(0).Text.ToString(),
                IsSelectable = row.UserData.GetInstanceFieldOrThrow<bool>("IsSelectable"),
            };
        }
    }
}
