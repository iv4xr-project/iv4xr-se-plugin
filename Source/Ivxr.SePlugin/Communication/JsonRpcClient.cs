using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using Iv4xr.SePlugin.WorldModel;
using StreamJsonRpc;
using VRageMath;

namespace Iv4xr.SePlugin.Communication
{
    public interface ICharacterControllerAsync
    {
        Task<Observation> MoveAndRotate(Vector3 movement, Vector2 rotation3, float roll = 0);
    }

    public interface IDefinitionsAsync
    {
        Task<List<BlockDefinition>> BlockDefinitions();
        Task<List<DefinitionBase>> AllDefinitions();
    }

    public interface IItemsAsync
    {
        Task Place();

        Task BeginUseTool();

        Task EndUseTool();

        Task Equip(ToolbarLocation toolbarLocation);

        Task SetToolbarItem(string name, ToolbarLocation toolbarLocation);

        Task<Toolbar> GetToolbar();
    }

    public interface IObserverAsync
    {
        Task<Observation> Observe();
        Task<Observation> ObserveBlocks();
        Task<Observation> ObserveNewBlocks();
        Task TakeScreenshot(string absolutePath);
    }

    public interface ISessionControllerAsync
    {
        Task LoadScenario(string scenarioPath);
    }

    public class JsonRpcClientSpaceEngineers
    {
        public ICharacterControllerAsync Character;
        public IDefinitionsAsync Definitions;
        public IItemsAsync Items;
        public IObserverAsync Observer;
        public ISessionControllerAsync Session;
        private readonly JsonRpc m_jsonRpc;


        public static JsonRpcClientSpaceEngineers Connect(string hostname, int port)
        {
            var tcpClient = new TcpClient(hostname, port);
            var stream = tcpClient.GetStream();
            return new JsonRpcClientSpaceEngineers(stream);
        }

        public JsonRpcClientSpaceEngineers(Stream stream)
        {
            var formatter = new JsonMessageFormatter(Encoding.UTF8);
            var messageHandler = new NewLineDelimitedMessageHandler(stream, stream, formatter);
            m_jsonRpc = new JsonRpc(messageHandler);
            Character = Attach<ICharacterControllerAsync>("Character.");
            Definitions = Attach<IDefinitionsAsync>("Definitions.");
            Items = Attach<IItemsAsync>("Items.");
            Observer = Attach<IObserverAsync>("Observer.");
            Session = Attach<ISessionControllerAsync>("Session.");
            m_jsonRpc.StartListening();
        }

        private T Attach<T>(string prefix) where T : class
        {
            return m_jsonRpc.Attach<T>(new JsonRpcProxyOptions()
            {
                MethodNameTransform = CommonMethodNameTransforms.Prepend(prefix)
            });
        }
    }
}
