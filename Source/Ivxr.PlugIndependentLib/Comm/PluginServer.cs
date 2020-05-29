using System;
using System.Collections.Concurrent;
using System.Data;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Xml;

using EU.Iv4xr.PluginLib;
using EU.Iv4xr.PluginLib.Log;

namespace EU.Iv4xr.PluginLib
{
    public class PluginServer
    {
        private ILog m_log;
        private readonly RequestQueue m_requestQueue;

        public PluginServer(ILog log, RequestQueue requestQueue)
        {
            m_log = log;
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
                        ServeConnectedClient(stream);
                    }
                    catch (IOException e)
                    {
                        m_log.WriteLine($"Plugin server connection error: {e.Message}");
                    }
                }
            }
        }

        private void ServeConnectedClient(NetworkStream stream)
        {
            var buffer = new byte[4096];

            int readCount;
            while ((readCount = stream.Read(buffer, 0, buffer.Length)) != 0)
            {
                string message = Encoding.ASCII.GetString(buffer, 0, readCount);
                int indexOfNewLine = message.IndexOf('\n');
                // TODO(PP): Change to a warning (after validating it does not actually occur).
                if ((indexOfNewLine != -1) && (indexOfNewLine != message.Length - 1))
                    throw new NotImplementedException("Unexpected new line in the middle of message.");

                // TODO(PP): Implement this.
                if (indexOfNewLine == -1)
                    throw new NotImplementedException("Reading message in multiple parts not implemented.");

                m_log.WriteLine($"Read message: {message}");

                ProcessMessage(stream, message, out bool disconnected);
                if (disconnected)
                    break;

                WaitForReplyAndSendIt();
            }
        }

        private void ProcessMessage(NetworkStream clientStream, string message, out bool disconnected)
        {
            disconnected = false;

            if (!message.StartsWith("{\"Cmd\":"))
            {
                // TODO: throw new InvalidDataException("Unexpected message header: " + message);
                m_log.WriteLine("Unexpected message header: " + message);

                // TODO(PP): Remove this. For now, just reply anyway to test the communication.
                m_requestQueue.Requests.Enqueue(new Request(clientStream, message));
                return;
            }

            string command = message.Substring(startIndex: 7, length: 12);

            if (command.StartsWith("\"AGENTCOM"))  // AGENTCOMMAND 
            {
                m_requestQueue.Requests.Enqueue(new Request(clientStream, message));
            }
            else if (command.StartsWith("\"DISCONNECT\""))
            {
                Reply(clientStream, "true");
                clientStream.Close(timeout: 100);  // ms
                disconnected = true;
                return;  // It's important not to wait from any reply from the queue.
            }
            else
            {
                throw new NotImplementedException("Command unknown or not implemented: " + command);
            }
        }

        private void WaitForReplyAndSendIt()
        {
            // TODO(PP): consider adding a timetout
            var reply = m_requestQueue.Replys.Take();
            Reply(reply.ClientStream, reply.Message);
        }

        private void Reply(NetworkStream clientStream, string reply)
        {
            // TODO(PP): prevent allocation of a new buffer each time
            var replyBuffer = Encoding.ASCII.GetBytes(reply + '\n');
            clientStream.Write(replyBuffer, 0, replyBuffer.Length);
        }
    }
}