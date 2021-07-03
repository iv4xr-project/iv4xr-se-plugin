using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;
using Iv4xr.PluginLib.WorldModel;

namespace SeServerMock.Mocks
{
    public class MockObserver : IObserver
    {
        public ILog Log { get; set; }

        public CharacterObservation GetCharacterObservation()
        {
            return new CharacterObservation()
            {
                Id = "Mock",
                Position = new PlainVec3D(4, 2, 0),
            };
        }

        public Observation GetObservation()
        {
            var block = new Block()
            {
                Id = "blk",
                Position = new PlainVec3D(5, 5, 5),
                MaxIntegrity = 10f,
                BuildIntegrity = 1.0f,
                Integrity = 5.0f,
                BlockType = "MockBlock"
            };

            var blocks = new List<Block>
            {
                block
            };

            var grids = new List<CubeGrid>
            {
                new CubeGrid()
                {
                    Position = block.Position,
                    Blocks = blocks
                }
            };
            return new Observation()
            {
                Character = GetCharacterObservation(),
                Grids = grids
            };
        }

        public CharacterObservation Observe()
        {
            return GetCharacterObservation();
        }

        public Observation ObserveBlocks()
        {
            return GetObservation();
        }

        public Observation ObserveNewBlocks()
        {
            return GetObservation();
        }

        public void TakeScreenshot(string absolutePath)
        {
            throw new System.NotImplementedException();
        }
    }
}
