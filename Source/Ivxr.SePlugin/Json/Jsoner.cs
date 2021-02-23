using System;
using System.Collections.Generic;
using System.Text;
using LitJson;

namespace Iv4xr.SePlugin.Json
{
    /// <summary>
    /// Just a very thin wrapper around a JSON provider, because it's quite likely we'll need to swich to a different one.
    /// 
    /// Currently uses (an outdated) LitJson already present in the SE codebase.
    /// Other candidates are:
    /// * Newtonsoft Json or
    /// * the new System.Text.Json, which should be faster, or
    /// * Utf8Json, maybe even faster, see (but probably not maintained)
    /// https://michaelscodingspot.com/the-battle-of-c-to-json-serializers-in-net-core-3/
    /// </summary>
    public class Jsoner
    {
        private readonly JsonWriter m_writer = new JsonWriter();

        public string ToJson(object obj)
        {
            m_writer.Reset();

            JsonMapper.ToJson(obj, m_writer);

            return m_writer.ToString();
        }

        public T ToObject<T>(string json)
        {
            return JsonMapper.ToObject<T>(json);
        }
    }
}