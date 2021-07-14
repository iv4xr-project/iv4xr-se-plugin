namespace Iv4xr.PluginLib.Json
{
    public interface IJsoner
    {
        string ToJson<T>(T obj);

        T ToObject<T>(string json);
    }
}
