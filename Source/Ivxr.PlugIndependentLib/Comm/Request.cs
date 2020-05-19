using System.Net.Sockets;

namespace EU.Iv4xr.PluginLib
{
    public class Request
    {
        public Request(NetworkStream clientStream, string message)
        {
            ClientStream = clientStream;
            Message = message;
        }
        
        public NetworkStream ClientStream { get; }
        public string Message { get; }  // Maybe we'll add something more structured later.
    }
}