using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.Session;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;

namespace Iv4xr.SePlugin.Communication
{
    public abstract class AbstractServiceOnGameLoop
    {
        private readonly FuncActionDispatcher m_funcActionDispatcher;

        protected AbstractServiceOnGameLoop(FuncActionDispatcher funcActionDispatcher)
        {
            m_funcActionDispatcher = funcActionDispatcher;
        }

        protected async Task<dynamic> EnqueueAsync(Func<dynamic> func)
        {
            return await m_funcActionDispatcher.EnqueueAsync(func);
        }

        protected async Task<object> EnqueueAsync(Action func)
        {
            return await m_funcActionDispatcher.EnqueueAsync(func);
        }

        protected dynamic Enqueue(Func<dynamic> func)
        {
            return m_funcActionDispatcher.Enqueue(func);
        }

        protected object Enqueue(Action func)
        {
            return m_funcActionDispatcher.Enqueue(func);
        }
    }

    public class DefinitionsOnGameLoop : AbstractServiceOnGameLoop, IDefinitions
    {
        private readonly IDefinitions m_definitions;

        public DefinitionsOnGameLoop(IDefinitions definitions, FuncActionDispatcher funcActionDispatcher) : base(funcActionDispatcher)
        {
            m_definitions = definitions;
        }

        public List<BlockDefinition> BlockDefinitions()
        {
            return Enqueue(() => m_definitions.BlockDefinitions());
        }

        public List<DefinitionBase> AllDefinitions()
        {
            return Enqueue(() => m_definitions.AllDefinitions());
        }
    }

    public class ObserverOnGameLoop : AbstractServiceOnGameLoop, IObserver
    {
        private readonly IObserver m_observer;

        public ObserverOnGameLoop(IObserver observer, FuncActionDispatcher funcActionDispatcher) : base(funcActionDispatcher)
        {
            m_observer = observer;
        }

        public Observation Observe()
        {
            return Enqueue(() => m_observer.Observe());
        }

        public Observation ObserveBlocks()
        {
            return Enqueue(() => m_observer.ObserveBlocks());
        }

        public Observation ObserveNewBlocks()
        {
            return Enqueue(() => m_observer.ObserveNewBlocks());
        }

        public void TakeScreenshot(string absolutePath)
        {
            Enqueue(() => m_observer.TakeScreenshot(absolutePath));
        }
    }

    public class ItemsOnGameLoop : AbstractServiceOnGameLoop, IItems
    {
        private readonly IItems m_items;

        public ItemsOnGameLoop(IItems items, FuncActionDispatcher funcActionDispatcher) : base(funcActionDispatcher)
        {
            m_items = items;
        }

        public void Place()
        {
            Enqueue(() => m_items.Place());
        }

        public void BeginUseTool()
        {
            Enqueue(() => m_items.BeginUseTool());
        }

        public void EndUseTool()
        {
            Enqueue(() => m_items.EndUseTool());
        }

        public void Equip(ToolbarLocation toolbarLocation)
        {
            Enqueue(() => m_items.Equip(toolbarLocation));
        }

        public void SetToolbarItem(string name, ToolbarLocation toolbarLocation)
        {
            Enqueue(() => m_items.SetToolbarItem(name, toolbarLocation));
        }

        public Toolbar GetToolbar()
        {
            return Enqueue(() => m_items.GetToolbar());
        }
    }

    public class CharacterOnGameLoop : AbstractServiceOnGameLoop, ICharacterController
    {
        private readonly ICharacterController m_character;
        private readonly IObserver m_observer;

        public CharacterOnGameLoop(ICharacterController character, IObserver observer, FuncActionDispatcher funcActionDispatcher) :
                base(funcActionDispatcher)
        {
            m_character = character;
            m_observer = observer;
        }

        public Observation MoveAndRotate(Vector3 movement, Vector2 rotation3, float roll = 0)
        {
            return Enqueue(() =>
            {
                m_character.MoveAndRotate(movement, rotation3, roll);
                return m_observer.Observe();
            });
        }
    }

    public class SynchronizedSpaceEngineers : ISpaceEngineers
    {
        public ICharacterController Character { get; }
        public ISessionController Session { get; }
        public IItems Items { get; }
        public IObserver Observer { get; }
        public IDefinitions Definitions { get; }


        public SynchronizedSpaceEngineers(ISpaceEngineers se, FuncActionDispatcher funcActionDispatcher)
        {
            Character = new CharacterOnGameLoop(se.Character, se.Observer, funcActionDispatcher);
            Session = se.Session;
            Items = new ItemsOnGameLoop(se.Items, funcActionDispatcher);
            Observer = new ObserverOnGameLoop(se.Observer, funcActionDispatcher);
            Definitions = new DefinitionsOnGameLoop(se.Definitions, funcActionDispatcher);
        }
    }
}
