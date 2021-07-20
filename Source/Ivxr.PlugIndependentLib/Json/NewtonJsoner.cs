using Newtonsoft.Json;

namespace Iv4xr.PluginLib.Json
{
    public class NewtonJsoner:IJsoner
    {
        public string ToJson<T>(T obj)
        {
            return JsonConvert.SerializeObject(obj);
        }

        public T ToObject<T>(string json)
        {
             return JsonConvert.DeserializeObject<T>(json);
        }
    }
}
