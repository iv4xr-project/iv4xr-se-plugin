using System.Net.Sockets;

namespace Iv4xr.PluginLib
{
	/// <summary>
	/// Request item of the request queue, carring its context.
	/// </summary>
    public class RequestItem
    {
        public RequestItem(NetworkStream clientStream, string message)
        {
            ClientStream = clientStream;
            Message = message;
        }
        
        public NetworkStream ClientStream { get; }
        public string Message { get; }  // Maybe we'll add something more structured later.
    }
}