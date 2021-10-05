
using System.Collections.Generic;
using Iv4xr.PluginLib.WorldModel;
using Iv4xr.SePlugin;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.Navigation;
using VRageMath;
using Xunit;

namespace Ivxr.SeGameLib.Tests
{
    public class NavGraphTests
    {
        private CubeGrid CreateTestGrid()
        {
            var grid = new CubeGrid()
            {
                Blocks = new List<Block>()
            };

            for (var x = 0; x < 3; x++)
            {
                var gridPosition = new PlainVec3I(x, 0, 0);
                
                var block = new Block
                {
                    GridPosition = gridPosition,
                    Position = new Vector3D(gridPosition.ToVector3I()).ToPlain()
                };
                
                grid.Blocks.Add(block);
            }

            return grid;
        }
        
        [Fact]
        public void CreatesGraph()
        {
            // We will not call methods that use LowLevelObserver
            var graphEditor = new NavGraphEditor(new LowLevelObserver(new GameSession()));

            var grid = CreateTestGrid();
            var graph = graphEditor.CreateGraph(grid, Vector3D.Zero, Vector3I.Up);
            
            Assert.Equal(3, graph.Nodes.Count);
        }
    }
}
