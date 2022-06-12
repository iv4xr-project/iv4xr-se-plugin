using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel.Screen
{
    public class BlueprintDefinition
    {
        public string DisplayName;
        public List<AmountedDefinitionId> Prerequisites;
        public List<AmountedDefinitionId> Results;
        
        public override string ToString()
        {
            return $"{{{string.Join(" ", Prerequisites)}}}->{{{string.Join(" ", Results)}}}";
        }

    }
}
