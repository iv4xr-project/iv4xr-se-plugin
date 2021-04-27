using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;

namespace SeServerMock.Mocks
{
    public class MockObserver : IObserver
    {
        public ILog Log { get; set; }

        public Observation GetObservation()
        {
            var entity = new Entity()
            {
                    Id = "Ente",
                    Position = new PlainVec3D(3, 2, 1)
            };

            var entities = new List<Entity>
            {
                    entity
            };

            var block = new SlimBlock()
            {
                    Id = "blk",
                    Position = new PlainVec3D(5, 5, 5),
                    MaxIntegrity = 10f,
                    BuildIntegrity = 1.0f,
                    Integrity = 5.0f,
                    BlockType = "MockBlock"
            };

            var blocks = new List<SlimBlock>
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
                    AgentID = "Mock",
                    Position = new PlainVec3D(4, 2, 0),
                    Entities = entities,
                    Grids = grids
            };
        }

        public Observation GetObservation(ObservationArgs observationArgs)
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