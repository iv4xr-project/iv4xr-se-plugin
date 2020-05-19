using System;
using System.Collections.Concurrent;

namespace EU.Iv4xr.PluginLib
{
    public class RequestQueue : IDisposable
    {
        public RequestQueue()
        {
            Requests = new ConcurrentQueue<Request>();
            Replys = new BlockingCollection<Request>();
        }
        
        public ConcurrentQueue<Request> Requests { get; }
        public BlockingCollection<Request> Replys { get; }

        #region IDisposable Support
        private bool disposedValue = false; // To detect redundant calls

        protected virtual void Dispose(bool disposing)
        {
            if (!disposedValue)
            {
                if (disposing)
                {
                    Replys.Dispose();
                }

                disposedValue = true;
            }
        }

        public void Dispose()
        {
            // Do not change this code. Put cleanup code in Dispose(bool disposing) above.
            Dispose(true);
        }
        #endregion
    }
}