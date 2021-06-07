using System;
using System.IO;
using Havok;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Json;
using VRage.FileSystem;

namespace Iv4xr.SePlugin.Config
{
    public class ConfigLoader
    {
        public ILog Log { get; set; }
        
        private const string CONFIG_FILE = "ivxr-plugin.config";

        public ConfigLoader(ILog log)
        {
            Log = log;
        }

        public void SaveDefault()
        {
            var jsonConfig = (new Jsoner()).ToJson(new PluginConfig());
            
            File.WriteAllText(GetConfigPath(), jsonConfig);
        }

        public PluginConfig LoadOrSaveDefault()
        {
            if (!File.Exists(GetConfigPath()))
            {
                SaveDefault();
                return new PluginConfig();
            }

            try
            {
                return Load();
            }
            catch (Exception e)
            {
                Log?.Exception(e, "Failed to load config.");
                return new PluginConfig();
            }
        }

        private PluginConfig Load()
        {
            var text = File.ReadAllText(GetConfigPath());
            Log?.WriteLine($"Config file read:\n{text}");

            return (new Jsoner()).ToObject<PluginConfig>(text);
        }

        private static string GetConfigPath()
        {
            return Path.Combine(MyFileSystem.UserDataPath, CONFIG_FILE);
        }
    }
}
