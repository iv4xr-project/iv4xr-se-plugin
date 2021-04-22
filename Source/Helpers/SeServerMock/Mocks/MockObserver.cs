using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;

namespace SeServerMock.Mocks
{
    public class MockObserver : IObserver
    {
        public ILog Log { get; set; }

        public SeObservation GetObservation()
        {
            var entity = new SeEntity()
            {
                    Id = "Ente",
                    Position = new PlainVec3D(3, 2, 1)
            };

            var entities = new List<SeEntity>
            {
                    entity
            };

            var block = new SeBlock()
            {
                    Id = "blk",
                    Position = new PlainVec3D(5, 5, 5),
                    MaxIntegrity = 10f,
                    BuildIntegrity = 1.0f,
                    Integrity = 5.0f,
                    BlockType = "MockBlock"
            };

            var blocks = new List<SeBlock>
            {
                    block
            };

            var grids = new List<SeGrid>
            {
                    new SeGrid()
                    {
                            Position = block.Position,
                            Blocks = blocks
                    }
            };

            return new SeObservation()
            {
                    AgentID = "Mock",
                    Position = new PlainVec3D(4, 2, 0),
                    Entities = entities,
                    Grids = grids
            };
        }

        public SeObservation GetObservation(ObservationArgs observationArgs)
        {
            Log.WriteLine($"Observation mode: {observationArgs.ObservationMode}");

            return GetObservation();
        }

        public void TakeScreenshot(string absolutePath)
        {
                throw new System.NotImplementedException();
        }
    }
}