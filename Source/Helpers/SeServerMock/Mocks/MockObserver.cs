using System;
using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.Navigation;
using Iv4xr.SpaceEngineers.WorldModel;

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
                DefinitionId = new DefinitionId()
                {
                    Id = "MockId",
                    Type = "MockBlock",    
                },
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

        public List<CharacterObservation> ObserveCharacters()
        {
            throw new NotImplementedException();
        }

        public NavGraph NavigationGraph()
        {
            throw new NotImplementedException();
        }

        public void SwitchCamera()
        {
            throw new NotImplementedException();
        }

        public void TakeScreenshot(string absolutePath)
        {
            throw new NotImplementedException();
        }
    }
}
