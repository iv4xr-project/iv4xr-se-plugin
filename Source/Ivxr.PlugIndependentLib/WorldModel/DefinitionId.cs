namespace Iv4xr.PluginLib.WorldModel
{
    public class DefinitionId
    {
        public string Id;
        public string Type;

        public override string ToString()
        {
            return $"{Id}/{Type}";
        }
    }
}
