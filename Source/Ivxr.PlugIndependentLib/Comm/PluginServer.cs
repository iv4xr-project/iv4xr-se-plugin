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

            while (!m_shouldStop)
            {
                int readCount;
                int readSoFar = 0;
                while ((readCount = stream.Read(buffer, readSoFar, buffer.Length - readSoFar)) != 0)
                {
                    readSoFar += readCount;
                    if (readSoFar >= buffer.Length * 3 / 4)
                        throw new InternalBufferOverflowException("Buffer too small (TODO: grow it).");

                    string message = Encoding.ASCII.GetString(buffer, 0, readSoFar);
                    int indexOfNewLine = message.IndexOf('\n');
                    if ((indexOfNewLine != -1) && (indexOfNewLine != message.Length - 1))
                        throw new NotImplementedException("Unexpected new line in the middle of message.");

                    m_log.WriteLine($"Read message: {message}");

                    // FIXME: just a testing reply
                    var replyBuffer = Encoding.ASCII.GetBytes($"Got {readCount} bytes, thanks.\n");
                    stream.Write(replyBuffer, 0, replyBuffer.Length);

                    // we are only doing this because we don't support reading multiple parts...
                    readSoFar = 0;
                }
            }
        }
    }
}