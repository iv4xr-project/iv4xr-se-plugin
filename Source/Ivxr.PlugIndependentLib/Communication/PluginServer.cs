using System;
using System.Collections.Concurrent;
using System.Data;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;


namespace Iv4xr.PluginLib
{
    public class PluginServer
    {
        private ILog m_log;
        private readonly ISessionDispatcher m_sessionDispatcher;
        private readonly RequestQueue m_requestQueue;

        private const int ReadTimeoutMs = 20_000;

        public PluginServer(ILog log, ISessionDispatcher sessionDispatcher, RequestQueue requestQueue)
        {
            m_log = log;
            m_sessionDispatcher = sessionDispatcher;
            m_requestQueue = requestQueue;
        }

        public void SetLog(ILog log)
        {
            m_log = log;
        }

        private bool m_shouldStop;

        public void Start(bool waitForFinish = false)
        {
            var thread = new Thread(() =>
            {
                var listener = Listen();

                while (!m_shouldStop)
                {
                    Serve(listener);
                }

                m_log.WriteLine("Ivxr server loop ended");
            })
            {
                    IsBackground = true,
                    Name = "Ivrx plugin server thread"
            };
            thread.Start();

            if (waitForFinish)
                thread.Join();
        }

        public void Stop()
        {
            m_shouldStop = true;
        }

        private TcpListener Listen()
        {
            var listener = new TcpListener(IPAddress.Any, 9678);
            listener.Start();
            m_log.WriteLine($"Listening at {listener.LocalEndpoint}");

            return listener;
        }

        private void Serve(TcpListener listener)
        {
            using (var client = listener.AcceptTcpClient())
            {
                m_log.WriteLine("Connected.");

                using (var stream = client.GetStream())
                {
                    try
                    {
                        stream.ReadTimeout = ReadTimeoutMs;
                        ServeConnectedClient(stream, client.ReceiveBufferSize);
                    }
                    catch (IOException e)
                    {
                        m_log.WriteLine($"Plugin server connection error: {e.Message}");
                    }
                }
            }
        }

        private void ServeConnectedClient(NetworkStream stream, int bufferSize)
        {
            var encoding = Encoding.UTF8;
            
            using (var reader = new StreamReader(stream, encoding, false, bufferSize))
            using (var writer = new StreamWriter(stream, encoding, 4 * bufferSize))  // Replies are bigger.
            {
                writer.AutoFlush = true;
                
                string message;
                while ((message = reader.ReadLine()) != null)
                {
                    m_log.WriteLine($"Read message: {message}");

                    ProcessMessage(stream, writer, message, out bool disconnect);
                    if (disconnect)
                        break;
                }
            }
        }

        private void ProcessMessage(NetworkStream clientStream, StreamWriter writer, string message,
            out bool disconnect)
        {
            disconnect = false;

            if (!message.StartsWith("{\"Cmd\":"))
            {
                // throw new InvalidDataException("Unexpected message header: " + message);
                m_log.WriteLine("Unexpected message header: " + message);
                // We disconnect here, because the outer loop can't handle a request without a reply from the queue.
                FlagDisconnect(writer, out disconnect, reply: false);
                return;
            }

            string command = message.Substring(startIndex: 7, length: 12);

            // ReSharper disable once StringLiteralTypo
            if (command.StartsWith("\"AGENTCOM")) // AGENTCOMMAND 
            {
                m_requestQueue.Requests.Enqueue(new RequestItem(writer, message));

                // TODO(PP): This tends to block when the message is not added to the queue. Rearchitecture, or at least add a timeout.
                WaitForReplyAndSendIt();
            }
            else if (command.StartsWith("\"SESSION\""))
            {
                m_sessionDispatcher.ProcessRequest(new RequestItem(writer, message));
            }
            else if (command.StartsWith("\"DISCONNECT\""))
            {
                FlagDisconnect(writer, out disconnect, reply: true);
            }
            else
            {
                // throw new NotImplementedException("Command unknown or not implemented: " + command);
                m_log.WriteLine("Command unknown or not implemented: " + command);
                // We disconnect here, because the outer loop can't handle a request without a reply from the queue.
                FlagDisconnect(writer, out disconnect, reply: false);
            }
        }

        private void FlagDisconnect(StreamWriter writer, out bool disconnect, bool reply = true)
        {
            Reply(writer, reply.ToString());
            disconnect = true;
        }

        private void WaitForReplyAndSendIt()
        {
            // TODO(PP): consider adding a timeout (blocks when no reply ready)
            var reply = m_requestQueue.Replies.Take();
            Reply(reply.ClientStreamWriter, reply.Message);
        }

        public static void Reply(StreamWriter writer, string reply)
        {
            writer.WriteLine(reply);
        }

        public static void ReplyOK(StreamWriter clientStreamWriter)
        {
            Reply(clientStreamWriter, "true");
        }

        public static void ReplyFalse(StreamWriter clientStreamWriter)
        {
            Reply(clientStreamWriter, "false");
        }
    }
}