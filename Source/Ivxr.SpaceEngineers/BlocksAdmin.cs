using System.Collections.Generic;
using Iv4xr.SpaceEngineers.WorldModel;

namespace Iv4xr.SpaceEngineers
{
    public interface IBlocksAdmin
    {
        void Remove(string blockId);

        void SetIntegrity(string blockId, float integrity);
        void SetCustomName(string blockId, string customName);
        void CreateOrUpdateGroup(string name, string gridId, List<string> blockIds);
        void MapButtonToBlock(string buttonBlockId, int buttonIndex, string action, string targetId);
        void MapButtonToGroup(string buttonBlockId, int buttonIndex, string action, string groupName);

        CubeGrid PlaceAt(DefinitionId blockDefinitionId, PlainVec3D position, PlainVec3D orientationForward,
            PlainVec3D orientationUp, PlainVec3F? color);

        string PlaceInGrid(DefinitionId blockDefinitionId, string gridId, PlainVec3I minPosition,
            PlainVec3I orientationForward, PlainVec3I orientationUp, PlainVec3F? color);

        IWarheadAdmin Warhead { get; }
        IFunctionalBlockAdmin FunctionalBlock { get; }
        ITerminalBlockAdmin TerminalBlock { get; }
        IMedicalRoomAdmin MedicalRoom { get; }
        ISensorBlockAdmin SensorBlock { get; }
        IGravityGeneratorAdmin GravityGenerator { get; }
        IGravityGeneratorSphereAdmin GravityGeneratorSphere { get; }
        ITimerBlockAdmin TimerBlock { get; }
        IPistonBaseAdmin PistonBase { get; }
        IThrustAdmin Thrust { get; }
    }

    public interface IGravityGeneratorBaseAdmin
    {
        void SetGravityAcceleration(string blockId, float gravityAcceleration);
    }

    public interface IGravityGeneratorAdmin : IGravityGeneratorBaseAdmin
    {
        void SetFieldSize(string blockId, PlainVec3D fieldSize);
    }

    public interface IGravityGeneratorSphereAdmin : IGravityGeneratorBaseAdmin
    {
        void SetRadius(string blockId, float radius);
    }

    public interface ITimerBlockAdmin
    {
        void SetTriggerDelay(string blockId, float triggerDelay);
    }

    public interface IWarheadAdmin
    {
        void Explode(string blockId);

        void Detonate(string blockId);

        bool StartCountdown(string blockId);

        bool StopCountdown(string blockId);

        void SetArmed(string blockId, bool armed);
    }

    public interface IFunctionalBlockAdmin
    {
        void SetEnabled(string blockId, bool enabled);
    }

    public interface ITerminalBlockAdmin
    {
        void SetCustomName(string blockId, string customName);
        void SetCustomData(string blockId, string customData);
        void SetShowInInventory(string blockId, bool showInInventory);
        void SetShowInTerminal(string blockId, bool showInTerminal);
        void SetShowOnHUD(string blockId, bool showOnHUD);
    }

    public interface IMedicalRoomAdmin
    {
        void SetRespawnAllowed(string blockId, bool respawnAllowed);
        void SetHealingAllowed(string blockId, bool healingAllowed);
        void SetRefuelAllowed(string blockId, bool refuelAllowed);
        void SetSpawnWithoutOxygenEnabled(string blockId, bool spawnWithoutOxygenEnabled);
    }

    public interface ISensorBlockAdmin
    {
        void SetFieldMin(string blockId, PlainVec3D fieldMin);
        void SetFieldMax(string blockId, PlainVec3D fieldMax);
    }

    public interface IPistonBaseAdmin
    {
        void SetVelocity(string blockId, float velocity);
        void RecreateTop(string blockId);
    }

    public interface IThrustAdmin
    {
        void SetThrustOverride(string blockId, float thrustOverride);
    }

    public interface ICharacterAdmin
    {
        CharacterObservation Teleport(PlainVec3D position, PlainVec3D? orientationForward = null,
            PlainVec3D? orientationUp = null);

        void Use(string blockId, int functionIndex, int action);

        CharacterObservation Create(string name, PlainVec3D position, PlainVec3D orientationForward,
            PlainVec3D orientationUp);

        void Switch(string id);
        void Remove(string id);
        void ShowTerminal(string blockId);
        void Die();
        string MainCharacterId();
        string LocalCharacterId();
        void UpdateEnergy(float energy);
        void UpdateOxygen(float oxygen);
        void UpdateHydrogen(float hydrogen);
    }
}
