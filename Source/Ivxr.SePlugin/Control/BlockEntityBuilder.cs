using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Cube;
using VRage.Game;
using VRage.Game.Entity.UseObject;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control
{
    public class BlockEntityBuilder
    {

        public Block CreateAndFill(MySlimBlock sourceBlock)
        {
            var definitionId = EntityBuilder.GetDefinitionId(sourceBlock.BlockDefinition);
            var block = CreateBlock(definitionId.Id);
            AddStandardFields(sourceBlock, block);
            AddCustomFields(sourceBlock, block);
            return block;
        }

        private Block CreateBlock(string id)
        {
            var type = BlockMapper.Mapping.TryGetValue(id.Replace("MyObjectBuilder_", ""), out var cls)
                    ? GetBlockType(cls)
                    : typeof(Block);
            var instance = (Block)Activator.CreateInstance(type);
            return instance;
        }

        public static Type GetBlockType(string id)
        {
            return typeof(Block).Assembly.GetTypes()
                           .FirstOrDefault(type => type.Name == id) ??
                   throw new NullReferenceException("Type cannot be null, id: " + id);
        }

        private void AddStandardFields(MySlimBlock sourceBlock, Block block)
        {
            var grid = sourceBlock.CubeGrid;

            block.Id = sourceBlock.UniqueId.ToString();
            block.DefinitionId = EntityBuilder.GetDefinitionId(sourceBlock.BlockDefinition);
            block.Position = grid.GridIntegerToWorld(sourceBlock.Position).ToPlain();
            block.MaxIntegrity = sourceBlock.MaxIntegrity;
            block.BuildIntegrity = sourceBlock.BuildIntegrity;
            block.Integrity = sourceBlock.Integrity;
            block.MinPosition = grid.GridIntegerToWorld(sourceBlock.Min).ToPlain();
            block.MaxPosition = grid.GridIntegerToWorld(sourceBlock.Max).ToPlain();

            // Note: it does not have to be the same as block.Min - block.Max (because of rotations)
            block.Size = sourceBlock.BlockDefinition.Size.ToPlain();

            block.OrientationForward =
                    grid.WorldMatrix.GetDirectionVector(sourceBlock.Orientation.Forward).ToPlain();
            block.OrientationUp = grid.WorldMatrix.GetDirectionVector(sourceBlock.Orientation.Up).ToPlain();
            block.Functional = sourceBlock.FatBlock?.IsFunctional ?? false;
            block.Working = sourceBlock.FatBlock?.IsWorking ?? false;
            block.UseObjects = GetUseObjects(sourceBlock);
        }

        private void AddCustomFields(MySlimBlock sourceBlock, Block block)
        {
            if (sourceBlock.FatBlock is MyFunctionalBlock myFunctionalBlock && block is FunctionalBlock functionalBlock)
            {
                functionalBlock.Enabled = myFunctionalBlock.Enabled;
            }

            if (sourceBlock.FatBlock is MyTerminalBlock myTerminalBlock && block is TerminalBlock terminalBlock)
            {
                terminalBlock.ShowInInventory = myTerminalBlock.ShowInInventory;
                terminalBlock.ShowInTerminal = myTerminalBlock.ShowInTerminal;
                terminalBlock.ShowOnHUD = myTerminalBlock.ShowOnHUD;
            }

            if (sourceBlock.FatBlock is MyDoorBase myDoorBase && block is DoorBase doorBase)
            {
                doorBase.Open = myDoorBase.Open;
                doorBase.AnyoneCanUse = myDoorBase.AnyoneCanUse;
            }

            if (sourceBlock.FatBlock is MyFueledPowerProducer myReactor &&
                block is FueledPowerProducer fueledPowerProducer)
            {
                fueledPowerProducer.Capacity = myReactor.Capacity;
                fueledPowerProducer.MaxOutput = myReactor.MaxOutput;
                fueledPowerProducer.CurrentOutput = myReactor.CurrentOutput;
            }
        }

        private List<UseObject> GetUseObjects(MySlimBlock block)
        {
            if (block?.FatBlock?.UseObjectsComponent == null)
            {
                return new List<UseObject>();
            }

            var objects = new List<IMyUseObject>();
            block.FatBlock.UseObjectsComponent.GetInteractiveObjects(objects);
            return objects.Select(EntityBuilder.CreateUseObject).ToList();
        }
    }
}
