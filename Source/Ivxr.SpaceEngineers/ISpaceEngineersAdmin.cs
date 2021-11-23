using Iv4xr.SpaceEngineers.WorldModel;

namespace Iv4xr.SpaceEngineers
{
    public interface ISpaceEngineersAdmin
    {
        void SetFrameLimitEnabled(bool enabled);
        ICharacterAdmin Character { get; }
        IBlocksAdmin Blocks { get; }
    }

    public interface IBlocksAdmin
    {
        void Remove(string blockId);

        void SetIntegrity(string blockId, float integrity);

        string PlaceAt(DefinitionId blockDefinitionId, PlainVec3D position, PlainVec3D orientationForward,
            PlainVec3D orientationUp);
    }

    public interface ICharacterAdmin
    {
        CharacterObservation Teleport(PlainVec3D position, PlainVec3D? orientationForward = null, PlainVec3D? orientationUp = null);
        void Use(string blockId, int functionIndex, int action);
    }
}
