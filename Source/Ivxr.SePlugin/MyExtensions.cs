using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Iv4xr.SePlugin.Control;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Definitions;
using Sandbox.Game;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Multiplayer;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Game.World;
using VRage.Game;
using VRage.Game.Entity;
using VRage.Game.ModAPI.Ingame;
using VRageMath;
using File = Iv4xr.SpaceEngineers.WorldModel.File;

namespace Iv4xr.SePlugin
{
    public static class MyExtensions
    {
        public static PlainVec3D ToPlain(this Vector3D vector)
        {
            return new PlainVec3D(vector.X, vector.Y, vector.Z);
        }

        public static PlainVec3D ToPlain(this Vector3 vector)
        {
            return new PlainVec3D(vector.X, vector.Y, vector.Z);
        }

        public static Vector3 ToVector3(this PlainVec3D vector)
        {
            return new Vector3(vector.X, vector.Y, vector.Z);
        }

        public static Vector2 ToVector2(this PlainVec2F vector)
        {
            return new Vector2(vector.X, vector.Y);
        }

        public static Vector3D ToVector3D(this PlainVec3D vector)
        {
            return new Vector3D(vector.X, vector.Y, vector.Z);
        }

        public static PlainVec3F ToPlainF(this Vector3 vector)
        {
            return new PlainVec3F(vector.X, vector.Y, vector.Z);
        }

        public static PlainVec3I ToPlain(this Vector3I vector)
        {
            return new PlainVec3I(vector.X, vector.Y, vector.Z);
        }

        public static Vector3I ToVector3I(this PlainVec3I vec) => new Vector3I(vec.X, vec.Y, vec.Z);

        public static bool IsAdminOrCreative(this MySession session)
        {
            // copied from MyCubePlacer.Shoot
            return session.CreativeMode || (session.CreativeToolsEnabled(Sync.MyId) && session.HasCreativeRights);
        }

        /// <summary>
        /// If the page number is negative, it's ignored.
        /// </summary>
        public static void SwitchToPageOrNot(this MyToolbar toolbar, int page)
        {
            if (page < 0)
                return;

            toolbar.SwitchToPage(page);
        }

        public static Dictionary<string, string> FromTypeToString(this Dictionary<Type, Type> dictionary)
        {
            return dictionary.ToDictionary(item => item.Key.Name, item => item.Value.Name);
        }

        public static DefinitionId ToDefinitionId(this MyDefinitionBase myDefinitionBase)
        {
            return ToDefinitionId(myDefinitionBase.Id);
        }

        public static DefinitionId ToDefinitionId(this MyDefinitionId myDefinitionId)
        {
            return new DefinitionId()
            {
                Id = myDefinitionId.TypeId.ToString(),
                Type = myDefinitionId.SubtypeId.String,
            };
        }

        public static MyDefinitionId ToMyDefinitionId(this DefinitionId definitionId)
        {
            return MyDefinitionId.Parse(definitionId.ToString());
        }

        public static MyCubeBlockDefinition ToMyCubeBlockDefinition(this DefinitionId definitionId)
        {
            var id = definitionId.ToString();
            return MyDefinitionManager.Static
                    .GetDefinitionsOfType<MyCubeBlockDefinition>()
                    .First(
                        cbd => cbd.Id.TypeId.ToString() == definitionId.Id &&
                               cbd.Id.SubtypeId.String == definitionId.Type
                    );
        }

        /// <summary>
        /// Get field used to identify block (ideal would be FatBlock.EntityId, but FatBlock is sometimes null.
        /// </summary>
        public static long BlockId(this MySlimBlock block)
        {
            return block.UniqueId;
        }

        public static long CharacterId(this MyCharacter character)
        {
            if (character == null)
            {
                throw new NullReferenceException("character");
            }
            if (character.GetIdentity() == null)
            {
                throw new NullReferenceException("character.identity");
            }

            return character.GetIdentity().IdentityId;
        }
        
        public static AmountedDefinitionId ToAmountedDefinition(this MyBlueprintDefinitionBase.Item item)
        {
            return new AmountedDefinitionId()
            {
                Id = item.Id.ToDefinitionId(),
                Amount = item.Amount.ToIntSafe()
            };
        }
        
        public static BlueprintDefinition ToBlueprintDefinition(this MyBlueprintDefinitionBase bp)
        {
            return new BlueprintDefinition()
            {
                DisplayName = bp.DisplayNameText,
                Prerequisites = bp.Prerequisites.Select(i => i.ToAmountedDefinition()).ToList(),
                Results = bp.Results.Select(i => i.ToAmountedDefinition()).ToList(),
            };
        }
        
        public static ProductionQueueItem ToProductionQueueItem(this MyProductionBlock.QueueItem bp)
        {
            return new ProductionQueueItem()
            {
                Amount = bp.Amount.ToIntSafe(),
                Blueprint = bp.Blueprint.ToBlueprintDefinition()
            };
        }
        
        public static AmountedDefinitionId ToAmountedDefinition(this MyPhysicalInventoryItem i)
        {
            return new AmountedDefinitionId()
            {
                Amount = i.Amount.ToIntSafe(),
                Id = i.GetDefinitionId().ToDefinitionId(),
            };
        }
        
        public static Inventory ToInventory(this MyInventory myInventory)
        {
            return new Inventory()
            {
                CurrentMass = (float)myInventory.CurrentMass,
                CurrentVolume = (float)myInventory.CurrentVolume,
                MaxMass = (float)myInventory.MaxMass,
                MaxVolume = (float)myInventory.MaxVolume,
                CargoPercentage = myInventory.CargoPercentage,
                Items = myInventory.GetItems().Select(ToInventoryItem).ToList(),
                Id = myInventory.InventoryId.m_hash,
            };
        }
        
        public static InventoryItem ToInventoryItem(this MyPhysicalInventoryItem myItem)
        {
            return new InventoryItem()
            {
                Amount = (int)myItem.Amount,
                Id = myItem.Content.GetId().ToDefinitionId(),
                ItemId =  myItem.ItemId,
            };
        }

        public static File ToFile(this FileInfo fileInfo)
        {
            return new File()
            {
                Name = fileInfo.Name,
                FullName = fileInfo.FullName,
                IsDirectory = (fileInfo.Attributes & FileAttributes.Directory) != 0
            };
        }
        
        public static File ToFile(this DirectoryInfo directoryInfo)
        {
            return new File()
            {
                Name = directoryInfo.Name,
                FullName = directoryInfo.FullName,
                IsDirectory = true,
            };
        }

        public static FloatingObject ToFloatingObject(this MyFloatingObject floatingObject)
        {
            return new FloatingObject()
            {
                Amount = (float)floatingObject.Amount.Value,
                ItemDefinition = floatingObject.ItemDefinition.ToPhysicalItemDefinition(),
                DisplayName = floatingObject.DisplayName,
                EntityId = floatingObject.EntityId,
            };
        }
      
        
        public static PhysicalItemDefinition ToPhysicalItemDefinition(this MyPhysicalItemDefinition myDefinitionBase)
        {
            var result = new PhysicalItemDefinition();
            BlockDefinitionEntityBuilder.AddBaseFields(myDefinitionBase, result);
            result.Health = myDefinitionBase.Health;
            result.Mass = myDefinitionBase.Mass;
            result.Volume = myDefinitionBase.Volume;
            result.Size = myDefinitionBase.Size.ToPlain();
            return result;
        }
        
        public static List<Role> Roles(this DebugInfo debugInfo)
        {
            var roles = new List<Role>();
            if (debugInfo.IsDedicated || (debugInfo.IsServer && debugInfo.SessionReady && debugInfo.MultiplayerActive))
            {
                roles.Add(Role.Admin);
            }

            if (!debugInfo.IsDedicated)
            {
                roles.Add(Role.Game);
            }

            roles.Add(Role.Observer);

            return roles;
        }
        
        public static bool CanDoRole(this DebugInfo debugInfo, Role role)
        {
            return debugInfo.Roles().Contains(role);
        }

    }
}
