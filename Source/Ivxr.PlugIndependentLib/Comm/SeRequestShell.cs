using System;
using System.Collections.Generic;
using System.Text;

namespace Iv4xr.PluginLib.Comm
{
    /// <summary>
    /// Type used for json deserialization. Called SeRequest on the Java side.
    /// </summary>
    public class SeRequestShell<ArgumentType>
    {
        public string Cmd; // Consider using enum.
        public ArgumentType Arg;
    }
}