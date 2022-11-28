﻿using System;
using System.Collections.Generic;
using Iv4xr.SpaceEngineers.WorldModel;
using static Iv4xr.SpaceEngineers.Role;
using static Iv4xr.SpaceEngineers.Purpose;

namespace Iv4xr.SpaceEngineers
{
    [Purpose(Hack)]
    [Role(Admin)]
    public interface ISpaceEngineersAdmin
    {
        void SetFrameLimitEnabled(bool enabled);
        ICharacterAdmin Character { get; }
        IBlocksAdmin Blocks { get; }
        IObserverAdmin Observer { get; }
        void UpdateDefaultInteractDistance(float distance);
        DebugInfo DebugInfo();
        ITestAdmin Tests { get; }
        void ShowNotification(string text);
        string Ping();
    }

    public interface IObserverAdmin
    {
        [Role(Observer)]
        List<CharacterObservation> ObserveCharacters();
    }

    public interface IBlocksAdmin
    {
        void Remove(string blockId);

        void SetIntegrity(string blockId, float integrity);
        void SetCustomName(string blockId, string customName);
        void CreateOrUpdateGroup(string name, string gridId, List<string> blockIds);
        void MapButtonToBlock(string buttonBlockId, int buttonIndex, string action, string targetId);
        void MapButtonToGroup(string buttonBlockId, int buttonIndex, string action, string groupName);

        string PlaceAt(DefinitionId blockDefinitionId, PlainVec3D position, PlainVec3D orientationForward,
            PlainVec3D orientationUp, PlainVec3F? color);

        string PlaceInGrid(DefinitionId blockDefinitionId, string gridId, PlainVec3I minPosition,
            PlainVec3I orientationForward, PlainVec3I orientationUp, PlainVec3F? color);
        
        IWarheadAdmin Warhead { get; }
    }

    public interface IWarheadAdmin
    {
        void Explode(string blockId);

        void Detonate(string blockId);

        bool StartCountdown(string blockId);

        bool StopCountdown(string blockId);
        
        void SetArmed(string blockId, bool armed);
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
