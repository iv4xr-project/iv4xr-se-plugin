
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
        private static IEnumerable<PlainVec3I> TrivialGridGenerator()
        {
            for (var x = 0; x < 3; x++)
            {
                yield return new PlainVec3I(x, 0, 0);
            }
        }
        
        private static CubeGrid CreateTestGrid(IEnumerable<PlainVec3I> gridGenerator)
        {
            var grid = new CubeGrid()
            {
                Blocks = new List<Block>()
            };

            foreach (var gridPosition in gridGenerator)
            {
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
            var graph = CreateGraph(TrivialGridGenerator());

            Assert.Equal(3, graph.Nodes.Count);
        }

        private static NavGraph CreateGraph(IEnumerable<PlainVec3I> gridGenerator)
        {
            // We will not call methods that use LowLevelObserver
            var graphEditor = new NavGraphEditor(new LowLevelObserver(new GameSession()));

            var grid = CreateTestGrid(gridGenerator);
            
            return graphEditor.CreateGraph(grid, Vector3D.Zero, Vector3I.Up);
        }
    }
}
