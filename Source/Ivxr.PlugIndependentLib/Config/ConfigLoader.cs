using System;
using System.IO;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Json;

namespace Iv4xr.SePlugin.Config
{
    public class ConfigLoader
    {
        public ILog Log { get; set; }
        public IJsoner Jsoner { get; }
        public string ConfigPath { get; }
        
        public ConfigLoader(ILog log, IJsoner jsoner, string configPath)
        {
            Log = log;
            Jsoner = jsoner;
            ConfigPath = configPath;
        }

        public void SaveDefault()
        {
            Save(new PluginConfig());
        }

        public void Save(PluginConfig config)
        {
            var jsonConfig = Jsoner.ToJson(config);
            
            File.WriteAllText(ConfigPath, jsonConfig);
        }

        public PluginConfig LoadOrSaveDefault()
        {
            try
            {
                if (!File.Exists(ConfigPath))
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
            var text = File.ReadAllText(ConfigPath);

            // This will use defaults for missing values, as they are pre-filled by the PluginConfig constructor.
            return Jsoner.ToObject<PluginConfig>(text);
        }
    }
}
