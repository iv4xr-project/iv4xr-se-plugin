using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Xml;

namespace SeServerMock
{
    public class Server
    {
        public void Start()
        {
            var listener = new TcpListener(IPAddress.Any, 9678);
            listener.Start();
            Console.WriteLine($"Listening at {listener.LocalEndpoint.ToString()}");

            using (var client = listener.AcceptTcpClient())
            {
                Console.WriteLine("Connected.");

                var stream = client.GetStream();
                var buffer = new byte[4096];

                while (true)
                {
                    int readCount;
                    int readSoFar = 0;
                    while ((readCount = stream.Read(buffer, readSoFar, buffer.Length - readSoFar)) != 0)
                    {
                        readSoFar += readCount;
                        if (readSoFar >= buffer.Length*3/4)
                            throw new InternalBufferOverflowException("Buffer too small (TODO: grow it).");

                        string message = Encoding.ASCII.GetString(buffer, 0, readSoFar);
                        int indexOfNewLine = message.IndexOf('\n');
                        if ((indexOfNewLine != -1) && (indexOfNewLine != message.Length - 1))
                            throw new NotImplementedException("Unexpected new line in the middle of message.");
                        
                        Console.WriteLine($"Read message: {message}");

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
}