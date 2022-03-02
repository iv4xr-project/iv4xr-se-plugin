using System.Collections.Generic;
using Iv4xr.SpaceEngineers.WorldModel;

namespace Iv4xr.SpaceEngineers
{
    public interface ISpaceEngineersAdmin
    {
        void SetFrameLimitEnabled(bool enabled);
        ICharacterAdmin Character { get; }
        IBlocksAdmin Blocks { get; }
        IObserverAdmin Observer { get;  }
        void UpdateDefaultInteractDistance(float distance);
    }

    public interface IObserverAdmin
    {
        List<CharacterObservation> ObserveCharacters();
    }

    public interface IBlocksAdmin
    {
        void Remove(string blockId);

        void SetIntegrity(string blockId, float integrity);

        string PlaceAt(DefinitionId blockDefinitionId, PlainVec3D position, PlainVec3D orientationForward,
            PlainVec3D orientationUp);

        string PlaceInGrid(DefinitionId blockDefinitionId, string gridId, PlainVec3I minPosition, PlainVec3I orientationForward,
            PlainVec3I orientationUp);
    }

    public interface ICharacterAdmin
    {
        CharacterObservation Teleport(PlainVec3D position, PlainVec3D? orientationForward = null, PlainVec3D? orientationUp = null);
        void Use(string blockId, int functionIndex, int action);

        CharacterObservation Create(string name, PlainVec3D position, PlainVec3D orientationForward, PlainVec3D orientationUp);
        void Switch(string id);
        void Remove(string id);
        void ShowTerminal(string blockId);
    }
}
