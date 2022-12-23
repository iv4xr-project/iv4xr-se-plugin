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

        List<string> BatchPlaceInGrid(string gridId, PlainVec3F? color, List<BlockLocation> blockPlacementConfigs);
        void SetPhysicalItemInInventory(string blockId, DefinitionId definitionId, float amount);
        void RequestConversionToShip(string gridId);
        void RequestConversionToStation(string gridId);
        void SetDestructibleBlocks(string gridId, bool destructibleBlocks);

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
        IButtonPanelAdmin ButtonPanel { get; }
        IDoorBaseAdmin Door { get; }
        ITextPanelAdmin TextPanel { get; }

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
        void Start(string blockId);
        void Stop(string blockId);
        void TriggerNow(string blockId);
    }

    public interface IWarheadAdmin
    {
        void Explode(string blockId);

        void Detonate(string blockId);

        bool StartCountdown(string blockId);

        bool StopCountdown(string blockId);

        void SetArmed(string blockId, bool armed);
        void SetCountdownMs(string blockId, int countdown);
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

    public interface IDoorBaseAdmin
    {
        void SetAnyoneCanUse(string blockId, bool anyoneCanUse);
        void SetOpen(string blockId, bool open);
    }
    
    public interface IButtonPanelAdmin
    {
        void SetAnyoneCanUse(string blockId, bool anyoneCanUse);
    }

    public interface IPistonBaseAdmin
    {
        void SetVelocity(string blockId, float velocity);
        void RecreateTop(string blockId);
    }

    public interface ITextPanelAdmin
    {
        void SetPublicTitle(string blockId, string publicTitle);
        void SetPrivateTitle(string blockId, string privateTitle);
        void SetPrivateDescription(string blockId, string privateDescription);
        void SetContentType(string blockId, int contentType);
        void SetTextPadding(string blockId, float padding);
        void SetText(string blockId, string text);
        void SetAlignment(string blockId, int alignment);
        void SetFontSize(string blockId, float fontSize);
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
