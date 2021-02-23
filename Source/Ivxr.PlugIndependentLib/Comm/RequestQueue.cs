using System;
using System.Collections.Concurrent;

namespace Iv4xr.PluginLib
{
    public class RequestQueue : IDisposable
    {
        public RequestQueue()
        {
            Requests = new ConcurrentQueue<RequestItem>();
            Replies = new BlockingCollection<RequestItem>();
        }

        public ConcurrentQueue<RequestItem> Requests { get; }
        public BlockingCollection<RequestItem> Replies { get; }

        #region IDisposable Support

        private bool disposedValue = false; // To detect redundant calls

        protected virtual void Dispose(bool disposing)
        {
            if (!disposedValue)
            {
                if (disposing)
                {
                    Replies.Dispose();
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