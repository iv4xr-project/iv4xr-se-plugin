using System;
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
        string Echo(string text);
        void SetCreativeTools(bool enabled);
    }

    public interface IObserverAdmin
    {
        [Role(Observer)]
        List<CharacterObservation> ObserveCharacters();
        CubeGrid GridById(string gridId);
        Block BlockById(string blockId);
    }
}
