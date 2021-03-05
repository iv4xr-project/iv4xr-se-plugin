using System.Linq;
using System.Net.Sockets;
using System.Text.RegularExpressions;

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
        public string Message { get; } // Maybe we'll add something more structured later.
        
        public string GetCmd()
        {
            // Skip outer layer "{\"Cmd\":\"AGENTCOMMAND\",\"Arg\":{\"Cmd\":\""
            // (Note: we are considering to replace this protocol by JSON RPC, which would solve this issue)
            var regex = new Regex("\"Cmd\":\"(?<cmd>[A-Z_]+)\"");
            return regex
                    .Matches(Message)
                    .Cast<Match>()
                    .Select(match => match.Groups["cmd"].Value).First(cmd => cmd != "AGENTCOMMAND");
        }
    }
}