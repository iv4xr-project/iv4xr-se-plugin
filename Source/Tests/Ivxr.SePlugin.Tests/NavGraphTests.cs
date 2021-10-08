
using System.Collections.Generic;
using System.Linq;
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

        private static NavGraph CreateGraph(IEnumerable<PlainVec3I> gridGenerator)
        {
            // We will not call methods that use LowLevelObserver
            var graphEditor = new NavGraphEditor(new LowLevelObserver(new GameSession()));

            var grid = CreateTestGrid(gridGenerator);
            
            return graphEditor.CreateGraph(grid, Vector3D.Zero, Vector3I.Up);
        }
        
        [Fact]
        public void CreatesTrivialGraph()
        {
            var graph = CreateGraph(TrivialGridGenerator());

            Assert.Equal(3, graph.Nodes.Count);
        }

        [Theory]
        [InlineData(2, 2)]
        [InlineData(1, 5)]
        [InlineData(3, 4)]
        [InlineData(10, 17)]
        public void CreatesRectangularGraph(int width, int length)
        {
            var graph = CreateGraph(GenGridPositions());
            
            Assert.Equal(width * length, graph.Nodes.Count);

            if (width >= 3 && length >= 3)
            {
                var maxEdges = graph.Nodes.Select(n => n.Neighbours.Count).Max();
                Assert.Equal(4, maxEdges);
            }

            if (width >= 2 && length >= 2)
            {
                var minEdges = graph.Nodes.Select(n => n.Neighbours.Count).Min();
                Assert.Equal(2, minEdges);
            }

            IEnumerable<PlainVec3I> GenGridPositions()
            {
                for (var x = 1; x <= width; x++)
                    for (var z = 1; z <= length; z++)
                        yield return new PlainVec3I(x, 0, z);
            }
        }
    }
}
