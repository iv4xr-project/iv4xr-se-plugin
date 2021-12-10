namespace Iv4xr.SpaceEngineers.WorldModel
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
