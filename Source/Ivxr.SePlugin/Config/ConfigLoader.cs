using System;
using System.IO;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Json;
using VRage.FileSystem;

namespace Iv4xr.SePlugin.Config
{
    public class ConfigLoader
    {
        public ILog Log { get; set; }
        public IJsoner Jsoner { get; }
        
        private const string CONFIG_FILE = "ivxr-plugin.config";

        public ConfigLoader(ILog log, IJsoner jsoner)
        {
            Log = log;
            Jsoner = jsoner;
        }

        public void SaveDefault()
        {
            var jsonConfig = Jsoner.ToJson(new PluginConfig());
            
            File.WriteAllText(GetConfigPath(), jsonConfig);
        }

        public PluginConfig LoadOrSaveDefault()
        {
            try
            {
                if (!File.Exists(GetConfigPath()))
                {
                    SaveDefault();
                }
                
                var config = Load();
                
                (new ConfigValidator(Log)).EnforceValidConfig(config);
                
                Log?.WriteLine($"Using configuration:\n{Jsoner.ToJson(config)}");

                return config;
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

            // This will use defaults for missing values, as they are pre-filled by the PluginConfig constructor.
            return Jsoner.ToObject<PluginConfig>(text);
        }

        private static string GetConfigPath()
        {
            return Path.Combine(MyFileSystem.UserDataPath, CONFIG_FILE);
        }
    }
}
