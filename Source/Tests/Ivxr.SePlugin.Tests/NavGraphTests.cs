
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.Navigation;
using Sandbox.Definitions;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using VRageMath;
using Xunit;

namespace Ivxr.SeGameLib.Tests
{
    public class NavGraphTests : IClassFixture<DefinitionsFixture>
    {
        private readonly DefinitionsFixture m_fixture;

        public NavGraphTests(DefinitionsFixture definitionsFixture)
        {
            m_fixture = definitionsFixture;
        }

        [Fact]
        public void DefinitionsAreNotEmpty()
        {
            var count = m_fixture.Definitions.CubeBlocks.Length;
            Assert.NotEqual(0, count);
        }
        
        [Fact]
        public void CreatesGraph()
        {
            // We will not call methods that use LowLevelObserver
            var graphEditor = new NavGraphEditor(new LowLevelObserver(new GameSession()));

            //MyDefinitionManager.RegisterTypesFromAssembly();
            var blockPlacer = new BlockPlacer();
            blockPlacer.PlaceBlock("LargeBlockArmorBlock", Vector3.Zero, Vector3.Forward, Vector3.Up);
            // var grid = new MyCubeGrid();
            
            //grid.BuildGeneratedBlock()
            //var graph = graphEditor.CreateGraph(null, Vector3D.Zero, Vector3I.Up);
        }
    }
}
