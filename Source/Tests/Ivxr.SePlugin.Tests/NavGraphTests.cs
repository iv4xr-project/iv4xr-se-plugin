
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.Navigation;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using VRageMath;
using Xunit;

namespace Ivxr.SeGameLib.Tests
{
    public class NavGraphTests
    {
        [Fact]
        public void CreatesGraph()
        {
            // We will not call methods that use LowLevelObserver
            var graphEditor = new NavGraphEditor(new LowLevelObserver(new GameSession()));

            var grid = new MyCubeGrid();
            //grid.BuildGeneratedBlock()
            var graph = graphEditor.CreateGraph(null, Vector3D.Zero, Vector3I.Up);
        }
    }
}
