using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Iv4xr.PluginLib.Control;
using Iv4xr.SpaceEngineers.Navigation;
using Iv4xr.SePlugin.Control;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;

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

        protected T Enqueue<T>(Func<T> func) where T: class 
        {
            return m_funcActionDispatcher.Enqueue(func);
        }

        protected void Enqueue(Action func)
        {
            m_funcActionDispatcher.Enqueue(func);
        }
    }

    public class DefinitionsOnGameLoop : AbstractServiceOnGameLoop, IDefinitions
    {
        private readonly IDefinitions m_definitions;

        public DefinitionsOnGameLoop(IDefinitions definitions, FuncActionDispatcher funcActionDispatcher) : base(
            funcActionDispatcher)
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

        public Dictionary<string, string> BlockHierarchy()
        {
            return Enqueue(() => m_definitions.BlockHierarchy());
        }

        public Dictionary<string, string> BlockDefinitionHierarchy()
        {
            return Enqueue(() => m_definitions.BlockDefinitionHierarchy());
        }
    }

    public class ObserverOnGameLoop : AbstractServiceOnGameLoop, IObserver
    {
        private readonly IObserver m_observer;

        public ObserverOnGameLoop(IObserver observer, FuncActionDispatcher funcActionDispatcher) : base(
            funcActionDispatcher)
        {
            m_observer = observer;
        }

        public CharacterObservation Observe()
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

        public List<CharacterObservation> ObserveCharacters()
        {
            return Enqueue(() => m_observer.ObserveCharacters());
        }

        public NavGraph NavigationGraph()
        {
            return Enqueue(() => m_observer.NavigationGraph());
        }

        public void SwitchCamera()
        {
            Enqueue(() => m_observer.SwitchCamera());
        }

        public void TakeScreenshot(string absolutePath)
        {
            Enqueue(() => m_observer.TakeScreenshot(absolutePath));
        }
    }

    public class BlocksOnGameLoop : AbstractServiceOnGameLoop, IBlocks
    {
        private readonly IBlocks m_blocks;

        public BlocksOnGameLoop(IBlocks blocks, FuncActionDispatcher funcActionDispatcher) : base(funcActionDispatcher)
        {
            m_blocks = blocks;
        }

        public void Place()
        {
            Enqueue(() => m_blocks.Place());
        }
    }

    public class BlocksAdminOnGameLoop : AbstractServiceOnGameLoop, IBlocksAdmin
    {
        private readonly IBlocksAdmin m_blocks;

        public BlocksAdminOnGameLoop(IBlocksAdmin blocks, FuncActionDispatcher funcActionDispatcher) : base(
            funcActionDispatcher)
        {
            m_blocks = blocks;
        }

        public void Remove(string blockId)
        {
            Enqueue(() => m_blocks.Remove(blockId));
        }

        public void SetIntegrity(string blockId, float integrity)
        {
            Enqueue(() => m_blocks.SetIntegrity(blockId, integrity));
        }

        public string PlaceAt(DefinitionId blockDefinitionId, PlainVec3D position, PlainVec3D orientationForward,
            PlainVec3D orientationUp)
        {
            return Enqueue(() => m_blocks.PlaceAt(blockDefinitionId, position, orientationForward, orientationUp));
        }

        public string PlaceInGrid(DefinitionId blockDefinitionId, string gridId, PlainVec3I minPosition, PlainVec3I orientationForward,
            PlainVec3I orientationUp)
        {
            return Enqueue(() => m_blocks.PlaceInGrid(blockDefinitionId, gridId, minPosition, orientationForward, orientationUp));
        }
    }

    public class ItemsOnGameLoop : AbstractServiceOnGameLoop, IItems
    {
        private readonly IItems m_items;

        public ItemsOnGameLoop(IItems items, FuncActionDispatcher funcActionDispatcher) : base(funcActionDispatcher)
        {
            m_items = items;
        }

        public void Equip(ToolbarLocation toolbarLocation)
        {
            Enqueue(() => m_items.Equip(toolbarLocation));
        }

        public void SetToolbarItem(string name, ToolbarLocation toolbarLocation)
        {
            Enqueue(() => m_items.SetToolbarItem(name, toolbarLocation));
        }

        public Toolbar Toolbar()
        {
            return Enqueue(() => m_items.Toolbar());
        }
    }

    public class CharacterAdminOnGameLoop : AbstractServiceOnGameLoop, ICharacterAdmin
    {
        private readonly ICharacterAdmin m_character;
        private readonly IObserver m_observer;

        public CharacterAdminOnGameLoop(ICharacterAdmin character, IObserver observer,
            FuncActionDispatcher funcActionDispatcher) :
                base(funcActionDispatcher)
        {
            m_character = character;
            m_observer = observer;
        }

        public CharacterObservation Teleport(PlainVec3D position, PlainVec3D? orientationForward,
            PlainVec3D? orientationUp)
        {
            return Enqueue(() =>
            {
                m_character.Teleport(position, orientationForward, orientationUp);
                return m_observer.Observe();
            });
        }

        public void Use(string blockId, int functionIndex, int action)
        {
            Enqueue(() => m_character.Use(blockId, functionIndex, action));
        }

        public CharacterObservation Create(string id, PlainVec3D position, PlainVec3D orientationForward, PlainVec3D orientationUp)
        {
            return Enqueue(() => m_character.Create(id, position, orientationForward, orientationUp));
        }

        public void Switch(string id)
        {
            Enqueue(() => m_character.Switch(id));
        }

        public void Remove(string id)
        {
            Enqueue(() => m_character.Remove(id));
        }
    }

    public class CharacterOnGameLoop : AbstractServiceOnGameLoop, ICharacterController
    {
        private readonly ICharacterController m_character;
        private readonly IObserver m_observer;

        public CharacterOnGameLoop(ICharacterController character, IObserver observer,
            FuncActionDispatcher funcActionDispatcher) :
                base(funcActionDispatcher)
        {
            m_character = character;
            m_observer = observer;
        }

        public CharacterObservation MoveAndRotate(PlainVec3D movement, PlainVec2F rotation3, float roll = 0, int ticks = 1)
        {
            return Enqueue(() =>
            {
                m_character.MoveAndRotate(movement, rotation3, roll, ticks);
                return m_observer.Observe();
            });
        }

        public CharacterObservation TurnOnJetpack()
        {
            return Enqueue(() =>
            {
                m_character.TurnOnJetpack();
                return m_observer.Observe();
            });
        }

        public CharacterObservation TurnOffJetpack()
        {
            return Enqueue(() =>
            {
                m_character.TurnOffJetpack();
                return m_observer.Observe();
            });
        }

        public CharacterObservation SwitchHelmet()
        {
            return Enqueue(() => m_character.SwitchHelmet());
        }

        public void BeginUsingTool()
        {
            Enqueue(() => m_character.BeginUsingTool());
        }

        public void EndUsingTool()
        {
            Enqueue(() => m_character.EndUsingTool());
        }

        public void Use()
        {
            Enqueue(() =>
            {
                m_character.Use();
            });
        }
    }

    public class SynchronizedSpaceEngineersAdmin : AbstractServiceOnGameLoop, ISpaceEngineersAdmin
    {
        public ISpaceEngineersAdmin Admin { get; }
        
        public void SetFrameLimitEnabled(bool enabled)
        {
            Enqueue(() => { Admin.SetFrameLimitEnabled(enabled); });
        }

        public ICharacterAdmin Character
        {
            get { return Admin.Character; }
        }

        public IBlocksAdmin Blocks
        {
            get { return Admin.Blocks; }
        }

        public SynchronizedSpaceEngineersAdmin(ISpaceEngineersAdmin admin, FuncActionDispatcher funcActionDispatcher) :
                base(funcActionDispatcher)
        {
            Admin = admin;
        }
    }

    public class SynchronizedSpaceEngineers : ISpaceEngineers
    {
        public ICharacterController Character { get; }
        public ISessionController Session { get; }
        public IItems Items { get; }
        public IObserver Observer { get; }
        public IDefinitions Definitions { get; }
        public IBlocks Blocks { get; }
        public ISpaceEngineersAdmin Admin { get; }


        public SynchronizedSpaceEngineers(ISpaceEngineers se, FuncActionDispatcher funcActionDispatcher)
        {
            Character = new CharacterOnGameLoop(se.Character, se.Observer, funcActionDispatcher);
            Session = se.Session;
            Items = new ItemsOnGameLoop(se.Items, funcActionDispatcher);
            Observer = new ObserverOnGameLoop(se.Observer, funcActionDispatcher);
            Definitions = new DefinitionsOnGameLoop(se.Definitions, funcActionDispatcher);
            Blocks = new BlocksOnGameLoop(se.Blocks, funcActionDispatcher);
            Admin = new SynchronizedSpaceEngineersAdmin(new SpaceEngineersAdmin(
                new CharacterAdminOnGameLoop(se.Admin.Character, se.Observer, funcActionDispatcher),
                new BlocksAdminOnGameLoop(se.Admin.Blocks, funcActionDispatcher)
            ), funcActionDispatcher);
        }
    }
}
