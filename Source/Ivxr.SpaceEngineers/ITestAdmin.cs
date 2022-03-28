using static Iv4xr.SpaceEngineers.Role;

namespace Iv4xr.SpaceEngineers
{
    public interface ITestAdmin
    {
        [Role(Admin)]
        void AdminOnly();

        [Role(Game)]
        void GameOnly();

        [Role(Observer)]
        void ObserverOnly();
    }
}
