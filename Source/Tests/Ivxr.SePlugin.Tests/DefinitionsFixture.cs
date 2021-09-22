using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using ParallelTasks;
using Sandbox;
using Sandbox.Definitions;
using VRage;
using VRage.FileSystem;
using VRage.Game;
using VRage.ObjectBuilders;


namespace Ivxr.SeGameLib.Tests
{
    public class DefinitionsFixture : IDisposable
    {
        private MyObjectBuilder_Definitions m_definitions;

        public MyObjectBuilder_Definitions Definitions
        {
            get => m_definitions;
            set => m_definitions = value;
        }

        public Dictionary<string, MyCubeBlockDefinition> BlockDefinitions { get; set; }

        public DefinitionsFixture()
        {
            const string contentPath = "..\\..\\..\\..\\..\\..\\se\\Sources\\SpaceEngineers\\Content";
            MyFileSystem.Init(contentPath, Path.Combine(Directory.GetCurrentDirectory(), "data"));

            MyXmlSerializerManager.RegisterSerializableBaseType(typeof(MyObjectBuilder_Base));

            Assembly sectorTypeAssembly = typeof(MyObjectBuilder_Sector).Assembly;
            RegisterAssembly(sectorTypeAssembly);

            Assembly sandboxGameAssembly = typeof(MySandboxGame).Assembly;
            RegisterAssembly(sandboxGameAssembly);

            //Assembly objectBuildersAssembly = typeof(MyObjectBuilder_MotorAdvancedRotor).Assembly;
            //RegisterAssembly(objectBuildersAssembly);

            MyObjectBuilderSerializer.LoadSerializers();

            //Parallel.Scheduler = new FakeTaskScheduler(); // TODO: is this necessary?
            //List<Tuple<MyObjectBuilder_Definitions, string>> baseDefinitions = MyDefinitionManager.Static.GetSessionPreloadDefinitions();
            //MyDefinitionManager.Static.PreloadDefinitions();

            foreach (var file in Directory.GetFiles(Path.Combine(contentPath, "Data\\CubeBlocks")))
            {
                LoadCubeBlockDefinitionFile(file);
            }                        

            MySandboxGame.IsDedicated = true;
        }

        private void LoadCubeBlockDefinitionFile(string armor)
        {
            string cubeBlockDefinition = Path.Combine(Environment.CurrentDirectory, armor);
            MyObjectBuilderSerializer.DeserializeXML<MyObjectBuilder_Definitions>(cubeBlockDefinition, out m_definitions);

            if (BlockDefinitions == null)
            {
                BlockDefinitions = new Dictionary<string, MyCubeBlockDefinition>();
            }

            foreach (var builder in m_definitions.CubeBlocks)
            {
                var blockDefinition = new MyCubeBlockDefinition();
                builder.Components = Array.Empty<MyObjectBuilder_CubeBlockDefinition.CubeBlockComponent>();
                builder.PhysicalMaterial = "Default";
                blockDefinition.Init(builder, MyModContext.BaseGame);

                string key = blockDefinition.Id.TypeId + "+" + blockDefinition.Id.SubtypeName;
                if (!BlockDefinitions.ContainsKey(key))
                    BlockDefinitions.Add(key, blockDefinition);
            }
        }

        public void Dispose()
        {
        }

        private static void RegisterAssembly(Assembly assembly)
        {
            MyObjectBuilderSerializer.RegisterFromAssembly(assembly);
            MyObjectBuilderType.RegisterFromAssembly(assembly, true);
            MyXmlSerializerManager.RegisterFromAssembly(assembly);
            MyDefinitionManagerBase.RegisterTypesFromAssembly(assembly);
        }
    }     
}
