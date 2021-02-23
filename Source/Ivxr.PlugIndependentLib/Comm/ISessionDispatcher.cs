using System;
using System.Collections.Generic;
using System.Text;

namespace Iv4xr.PluginLib
{
    public interface ISessionDispatcher
    {
        void ProcessRequest(RequestItem request);
    }
}