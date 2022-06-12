namespace Iv4xr.SpaceEngineers.WorldModel.Screen
{
    public class AmountedDefinitionId
    {
        public DefinitionId Id;
        public int Amount;
        
        public override string ToString()
        {
            return $"{Amount}x {Id}";
        }
    }
}
