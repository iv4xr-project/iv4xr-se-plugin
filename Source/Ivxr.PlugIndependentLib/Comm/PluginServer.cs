using System;
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

        public PluginServer()
        {
            m_log = new ConsoleLog();
        }

        public PluginServer(ILog mLog)
        {
            m_log = mLog;
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

                bool disconnected;
                ProcessMessage(stream, message, out disconnected);
                if (disconnected)
                    break;
            }
        }

        private void ProcessMessage(NetworkStream clientStream, string message, out bool disconnected)
        {
            disconnected = false;

            if (message.StartsWith("{\"cmd\":\"DISCONNECT\""))
            {
                Reply(clientStream, "true");
                clientStream.Close(timeout: 100);  // ms
                disconnected = true;
            }
            else
            {
                // FIXME(PP): just a testing reply
                Reply(clientStream, $"Got {message.Length} bytes, thanks.");
            }
        }

        private void Reply(NetworkStream clientStream, string reply)
        {
            // TODO(PP): prevent allocation of a new buffer each time
            var replyBuffer = Encoding.ASCII.GetBytes(reply + '\n');
            clientStream.Write(replyBuffer, 0, replyBuffer.Length);
        }
    }
}