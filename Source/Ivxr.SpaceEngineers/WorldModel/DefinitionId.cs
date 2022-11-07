namespace Iv4xr.SpaceEngineers.WorldModel
{
    public class DefinitionId
    {
        public string Id;
        public string Type;

        public static DefinitionId Parse(string definition)
        {
            var parts = definition.Split('/');
            return Create(parts[0], parts[1]);
        }

        public static DefinitionId Create(string id, string type)
        {
            if (id.StartsWith(ID_PREFIX))
            {
                return new DefinitionId() { Id = id, Type = type };
            }

            return new DefinitionId() { Id = $"{ID_PREFIX}{id}", Type = type };
        }

        public static readonly string ID_PREFIX = "MyObjectBuilder_";


        public override string ToString()
        {
            return $"{Id}/{Type}";
        }
    }
}
