using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using AustinHarris.JsonRpc;
using Iv4xr.PluginLib;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Iv4xr.SePlugin.Communication
{
    using CommandDict = Dictionary<string, IStringCommand>;

    public class JsonRpcDispatcher
    {
        public ILog Log { get; set; }

        private readonly RequestQueue m_requestQueue;

        public JsonRpcDispatcher(RequestQueue requestQueue)
        {
            m_requestQueue = requestQueue;
        }

        public void ProcessRequests()
        {
            while (m_requestQueue.Requests.TryDequeue(out RequestItem request))
            {
                string jsonReply;
                try
                {
                    jsonReply = ProcessSingleRequest(request);
                    request.ClientStreamWriter.WriteLine(jsonReply);
                    request.ClientStreamWriter.Flush();
                }
                catch (Exception ex)
                {
                    Log.Exception(ex, "Error processing a request");
                    Log.WriteLine($"Full request: \"{request.Message}\"");
                }
            }
        }

        private string ProcessSingleRequest(RequestItem request)
        {
            return ProcessInternal(Handler.DefaultSessionId(), request.Message, null);
        }

        /*
         * This is simplified version of JsonRpcHandler.ProcessInternal (but it is private so I had to copy the code).
         * I couldn't use public methods, because they are asynchronous and executed in different thread.
         * I raised issue:
         * https://github.com/Astn/JSON-RPC.NET/issues/126
         */
        private static string ProcessInternal(string sessionId, string jsonRpc, object jsonRpcContext)
        {
            Handler sessionHandler = Handler.GetSessionHandler(sessionId);
            JsonRequest[] jsonRequestArray = new JsonRequest[1]
            {
                JsonConvert.DeserializeObject<JsonRequest>(jsonRpc)
            };

            bool flag = jsonRequestArray.Length == 1;
            StringBuilder stringBuilder = (StringBuilder) null;
            for (int index = 0; index < jsonRequestArray.Length; ++index)
            {
                JsonRequest Rpc = jsonRequestArray[index];
                JsonResponse jsonResponse1 = new JsonResponse();
                if ((false)) {}
                else
                {
                    jsonResponse1.Id = Rpc.Id;
                    JsonResponse jsonResponse2 = sessionHandler.Handle(Rpc, jsonRpcContext);
                    if (jsonResponse2 != null)
                    {
                        jsonResponse1.JsonRpc = jsonResponse2.JsonRpc;
                        jsonResponse1.Error = jsonResponse2.Error;
                        jsonResponse1.Result = jsonResponse2.Result;
                    }
                    else
                        continue;
                }

                if (jsonResponse1.Result == null && jsonResponse1.Error == null)
                    jsonResponse1.Result = (object) new JValue((object) null);
                if (flag && (jsonResponse1.Id != null || jsonResponse1.Error != null))
                {
                    StringWriter stringWriter = new StringWriter();
                    JsonTextWriter jsonTextWriter = new JsonTextWriter((TextWriter) stringWriter);
                    jsonTextWriter.WriteStartObject();
                    if (!string.IsNullOrEmpty(jsonResponse1.JsonRpc))
                    {
                        jsonTextWriter.WritePropertyName("jsonrpc");
                        jsonTextWriter.WriteValue(jsonResponse1.JsonRpc);
                    }

                    if (jsonResponse1.Error != null)
                    {
                        jsonTextWriter.WritePropertyName("error");
                        jsonTextWriter.WriteRawValue(JsonConvert.SerializeObject((object) jsonResponse1.Error));
                    }
                    else
                    {
                        jsonTextWriter.WritePropertyName("result");
                        jsonTextWriter.WriteRawValue(JsonConvert.SerializeObject(jsonResponse1.Result));
                    }

                    jsonTextWriter.WritePropertyName("id");
                    jsonTextWriter.WriteValue(jsonResponse1.Id);
                    jsonTextWriter.WriteEndObject();
                    return stringWriter.ToString();
                }

                if (jsonResponse1.Id == null && jsonResponse1.Error == null)
                {
                    stringBuilder = new StringBuilder(0);
                }
                else
                {
                    if (index == 0)
                        stringBuilder = new StringBuilder("[");
                    stringBuilder.Append(JsonConvert.SerializeObject((object) jsonResponse1));
                    if (index < jsonRequestArray.Length - 1)
                        stringBuilder.Append(',');
                    else if (index == jsonRequestArray.Length - 1)
                        stringBuilder.Append(']');
                }
            }

            return stringBuilder.ToString();
        }
    }
}