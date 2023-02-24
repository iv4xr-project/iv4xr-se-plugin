using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Common.ObjectBuilders.Definitions;
using Sandbox.Definitions;
using Sandbox.Game;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Multiplayer;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Game.Weapons;
using Sandbox.Game.World;
using VRage;
using VRage.Audio;
using VRage.Game;
using VRage.Game.Entity;
using VRage.Game.ModAPI.Ingame;
using VRageMath;
using File = Iv4xr.SpaceEngineers.WorldModel.Screen.File;

namespace Iv4xr.SePlugin
{
    public static class MyExtensions
    {
        public static PlainVec3D ToPlain(this Vector3D vector)
        {
            return new PlainVec3D(vector.X, vector.Y, vector.Z);
        }

        public static Color ToColor(this Vector3 vector)
        {
            return new Color(vector.X, vector.Y, vector.Z);
        }

        public static Vector3 RgbToHsv(this Vector3 vector)
        {
            return MyColorPickerConstants.HSVToHSVOffset(vector.ToColor().ColorToHSV());
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

        public static Vector3 ToVector3(this PlainVec3F vector)
        {
            return new Vector3(vector.X, vector.Y, vector.Z);
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
            var item =  new InventoryItem()
            {
                Amount = (int)myItem.Amount,
                Id = myItem.Content.GetId().ToDefinitionId(),
                ItemId = myItem.ItemId,
                Scale = myItem.Scale,
            };
            if (myItem.Content is MyObjectBuilder_GasContainerObject gasContainerObject)
            {
                item.GasLevel = gasContainerObject.GasLevel;
            } 
            return item;
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
            var result = new FloatingObject()
            {
                Amount = (float)floatingObject.Amount.Value,
                ItemDefinition = floatingObject.ItemDefinition.ToPhysicalItemDefinition(),
            };
            floatingObject.ToEntity(result);
            return result;
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

        public static ToolbarItem ToToolbarItem(this MyToolbarItem toolbarItem)
        {
            switch (toolbarItem)
            {
                case null:
                    return null;
                case MyToolbarItemDefinition myToolbarItemDefinition:
                    return new ToolbarItemDefinition()
                    {
                        Id = myToolbarItemDefinition.Definition.Id.ToDefinitionId(),
                        Name = toolbarItem.DisplayName.ToString(),
                        Enabled = toolbarItem.Enabled,
                    };
                case MyToolbarItemTerminalBlock myToolbarItemTerminalBlock:
                    return new ToolbarItemTerminalBlock()
                    {
                        Name = toolbarItem.DisplayName.ToString(),
                        Enabled = toolbarItem.Enabled,
                        ActionId = myToolbarItemTerminalBlock.ActionId,
                        BlockEntityId = myToolbarItemTerminalBlock.BlockEntityId,
                    };
                case MyToolbarItemActions myToolbarItemActions:
                    return new ToolbarItemTerminalBlock()
                    {
                        Name = toolbarItem.DisplayName.ToString(),
                        Enabled = toolbarItem.Enabled,
                        ActionId = myToolbarItemActions.ActionId,
                    };
                default:
                    return new ToolbarItem()
                    {
                        Name = toolbarItem.DisplayName.ToString(),
                        Enabled = toolbarItem.Enabled,
                    };
            }
        }

        public static List<Role> Roles(this DebugInfo debugInfo)
        {
            var roles = new List<Role>();
            if (debugInfo.IsDedicated ||
                debugInfo.IsServer && debugInfo.SessionReady ||
                !debugInfo.SessionReady && !debugInfo.IsDedicated && !debugInfo.MultiplayerActive
               )
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

        public static Entity ToEntityOrNull(this MyEntity entity, Entity newEntity = null)
        {
            return entity == null ? null : ToEntity(entity, newEntity);
        }

        public static Entity ToEntity(this MyEntity entity, Entity newEntity = null)
        {
            entity.ThrowIfNull("entity");
            var orientation = entity.PositionComp.GetOrientation();
            var result = newEntity ?? new Entity();
            result.Id = entity.EntityId.ToString();
            if (entity is MyCharacter character)
            {
                result.Id = character.CharacterId().ToString();
            }

            result.Position = entity.PositionComp.GetPosition().ToPlain();
            result.OrientationForward = orientation.Forward.ToPlain();
            result.OrientationUp = orientation.Up.ToPlain();
            result.DisplayName = entity.DisplayName ?? entity.GetType().Name;
            result.Name = entity.Name;
            result.Velocity = entity.Physics?.LinearVelocity.ToPlain() ?? PlainVec3DConst.Zero;
            result.InScene = entity.InScene;
            result.DefinitionId = entity.DefinitionId?.ToDefinitionId() ?? new DefinitionId()
            {
                Id = entity.ToString(),
                Type = entity.GetType().ToString(),
            };
            if (entity is MyCockpit cockpit)
            {
                result.DefinitionId = cockpit.BlockDefinition.Id.ToDefinitionId();
            }
            return result;
        }

        public static Entity ToPolymorphicEntity(this MyEntity entity)
        {
            if (entity == null)
            {
                return null;
            }

            if (entity is MyCharacter character)
            {
                return new CharacterObservationBuilder(new BlockEntityBuilder()).CreateCharacterObservation(character);
            }

            if (entity is MyCubeBlock block)
            {
                return new BlockEntityBuilder().CreateAndFill(block.SlimBlock);
            }

            if (entity is MyCubeGrid grid)
            {
                return EntityBuilder.CreateSeGrid(grid, new List<Block>());
            }

            if (entity is MyHandToolBase tool)
            {
                return tool.ToHandTool();
            }

            if (entity is MyEngineerToolBase engineerTool)
            {
                return engineerTool.ToHandTool();
            }

            if (entity is MyHandDrill drill)
            {
                return drill.ToHandTool();
            }


            return entity.ToEntity();
        }

        public static HandTool ToHandTool(this MyEngineerToolBase handToolBase)
        {
            var handTool = new HandTool()
            {
                IsShooting = handToolBase.IsShooting,
            };
            handToolBase.ToEntity(handTool);
            return handTool;
        }

        public static HandTool ToHandTool(this IMyHandheldGunObject<MyToolBase> handToolBase)
        {
            var handTool = new HandTool()
            {
                IsShooting = handToolBase.IsShooting,
            };
            ((MyEntity)handToolBase).ToEntity(handTool);
            return handTool;
        }

        public static HandTool ToHandTool(this MyHandToolBase handToolBase)
        {
            var handTool = new HandTool()
            {
                IsShooting = handToolBase.IsShooting,
            };
            handToolBase.ToEntity(handTool);
            return handTool;
        }

        public static SessionSettings ToSessionSettings(this MyObjectBuilder_SessionSettings settings)
        {
            return new SessionSettings()
            {
                GameMode = (GameModeEnum)settings.GameMode,
                InfiniteAmmo = settings.InfiniteAmmo,
            };
        }
        
        public static CameraController ToCameraController(this MySession session)
        {
            if (session.CameraController == null)
            {
                return null;
            }

            return new CameraController()
            {
                IsInFirstPersonView = session.CameraController.IsInFirstPersonView,
                ForceFirstPersonCamera = session.CameraController.ForceFirstPersonCamera,
                Entity = session.CameraController?.Entity?.ToPolymorphicEntity(),
                CameraControllerEnum = (CameraControllerEnum)session.GetCameraControllerEnum(),
            };
        }

        public static SessionInfo ToSessionInfo(this MySession session)
        {
            if (session == null)
            {
                return null;
            }
            return new SessionInfo()
            {
                Name = session.Name,
                CurrentPath = session.CurrentPath,
                IsAdminMenuEnabled = session.IsAdminMenuEnabled,
                IsRunningExperimental = session.IsRunningExperimental,
                Ready = session.Ready,
                IsUnloading = session.IsUnloading,
                StreamingInProgress = session.StreamingInProgress,
                IsCopyPastingEnabled = session.IsCopyPastingEnabled,
                IsServer = session.IsServer,
                IsPausable = session.IsPausable(),
                GameDefinition = session.GameDefinition.ToDefinitionId(),
                Settings = session.Settings.ToSessionSettings(),
                Camera = session.ToCameraController(),
            };
        }

        private static IEnumerable<IMySourceVoice> GetSourceVoice(IEnumerable<object> collection)
        {
            return collection.SelectMany(
                sourceVoicePool =>
                {
                    var allVoices = (IDictionary)sourceVoicePool.GetInstanceFieldOrThrow<object>("m_allVoices");
                    return allVoices.Keys.Cast<IMySourceVoice>();
                });
        }

        public static SoundBanks ToSoundBanks(this MyCueBank cueBank)
        {
            var hudPools =
                    cueBank.GetInstanceFieldOrThrow<IDictionary>("m_voiceHudPools").Values.Cast<object>();
            var soundPools =
                    cueBank.GetInstanceFieldOrThrow<IDictionary>("m_voiceSoundPools").Values.Cast<object>();
            var musicPools =
                    cueBank.GetInstanceFieldOrThrow<IDictionary>("m_voiceMusicPools").Values.Cast<object>();
            var sounds = GetSourceVoice(soundPools);
            var huds = GetSourceVoice(hudPools);
            var music = GetSourceVoice(musicPools);
            return new SoundBanks()
            {
                Sound = sounds.ToSounds(),
                Hud = huds.ToSounds(),
                Music = music.ToSounds(),
            };
        }

        public static List<Sound> ToSounds(this IEnumerable<IMySourceVoice> source)
        {
            return source.Select(x => x.ToSound()).ToList();
        }

        public static Sound ToSound(this IMySourceVoice source)
        {
            return new Sound()
            {
                CueEnum = source.CueEnum.ToString(),
                IsPlaying = source.IsPlaying,
                IsPaused = source.IsPaused,
            };
        }

        public static ParticleEffect ToParticleEffect(this MyParticleEffect myParticleEffect)
        {
            return new ParticleEffect()
            {
                Name = myParticleEffect.GetName(),
                Position = myParticleEffect.WorldMatrix.Translation.ToPlain(),
            };
        }

        public static Toolbar ToToolbar(this MyToolbar toolbar)
        {
            return new Toolbar()
            {
                SlotCount = toolbar.SlotCount,
                PageCount = toolbar.PageCount,
                Items = new ToolbarItem[toolbar.ItemCount]
                        .Select((index, item) => toolbar[item]?.ToToolbarItem())
                        .ToList()
            };
        }

        public static MyCubeGrid.MyBlockLocation ToMyBlockLocation(
            this BlockLocation blockLocation,
            long? entityId = null,
            long? playerId = null)
        {
            var min = blockLocation.MinPosition.ToVector3I();
            var orientation = Quaternion.CreateFromForwardUp(blockLocation.OrientationForward.ToVector3I(),
                blockLocation.OrientationUp.ToVector3I());
            return new MyCubeGrid.MyBlockLocation(
                blockLocation.DefinitionId.ToMyDefinitionId(), min, min, min, orientation,
                entityId ?? MyEntityIdentifier.AllocateId(),
                playerId ?? MySession.Static.LocalPlayerId
            );
        }
    }
}
