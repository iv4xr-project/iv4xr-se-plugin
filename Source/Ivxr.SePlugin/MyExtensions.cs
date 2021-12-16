﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Definitions;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Multiplayer;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Game.World;
using VRage.Game;
using VRageMath;

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
        public static object GetInstanceField(this object instance, string fieldName)
        {
            BindingFlags bindFlags = BindingFlags.Instance | BindingFlags.Public | BindingFlags.NonPublic
                                     | BindingFlags.Static;
            var t = instance.GetType();
            FieldInfo field = t.GetField(fieldName, bindFlags);
            return field.GetValue(instance);
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
            return character.GetIdentity().IdentityId;
        } 
    }
}
