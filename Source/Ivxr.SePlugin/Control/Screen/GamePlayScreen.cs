using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox;
using Sandbox.Definitions;
using Sandbox.Game;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Gui;
using Sandbox.Game.World;
using Sandbox.Graphics.GUI;
using VRage.Game;
using VRage.Game.Entity;
using VRage.ModAPI;
using VRage.Utils;
using VRage.Voxels;
using VRageMath;

namespace Iv4xr.SePlugin.Control.Screen
{
    public class GamePlayScreen : AbstractScreen<MyGuiScreenGamePlay, GamePlayData>, IGamePlay
    {
        class PositionedOreMarker
        {
            public Vector3D Position;
            public MyEntityOreDeposit OreDeposit;
            public string Name;
            public double Distance;
        }


        //based on MyGuiScreenHudSpace.DrawOreMarkers
        private IEnumerable<PositionedOreMarker> CreateMarkers()
        {
            MyHudOreMarkers oreMarkers = MyHud.OreMarkers;
            Vector3D controlledEntityPosition = Vector3D.Zero;
            if (MySession.Static != null && MySession.Static.ControlledEntity != null)
            {
                controlledEntityPosition = ((MyEntity)MySession.Static.ControlledEntity).WorldMatrix.Translation;
            }

            PositionedOreMarker[] nearestOreDeposits =
                    new PositionedOreMarker[MyDefinitionManager.Static.VoxelMaterialCount];
            float[] nearestDistancesSquared = new float[nearestOreDeposits.Length];

            for (int i = 0; i < nearestOreDeposits.Length; i++)
            {
                nearestOreDeposits[i] = null;
                nearestDistancesSquared[i] = float.MaxValue;
            }


            foreach (MyEntityOreDeposit oreMarker in oreMarkers)
            {
                for (int i = 0; i < oreMarker.Materials.Count; i++)
                {
                    MyEntityOreDeposit.Data depositData = oreMarker.Materials[i];

                    var oreMaterial = depositData.Material;
                    Vector3D oreWorldPosition;
                    var voxelMap = oreMarker.VoxelMap;
                    MyVoxelCoordSystems.LocalPositionToWorldPosition(
                        (voxelMap.PositionComp.GetPosition() - (Vector3D)voxelMap.StorageMin),
                        ref depositData.AverageLocalPosition, out oreWorldPosition);

                    //ProfilerShort.BeginNextBlock("Distance");
                    Vector3D diff = (controlledEntityPosition - oreWorldPosition);
                    float distanceSquared = (float)diff.LengthSquared();

                    float nearestDistanceSquared = nearestDistancesSquared[oreMaterial.Index];
                    if (distanceSquared < nearestDistanceSquared)
                    {
                        MyVoxelMaterialDefinition voxelMaterial =
                                MyDefinitionManager.Static.GetVoxelMaterialDefinition(oreMaterial.Index);
                        nearestOreDeposits[oreMaterial.Index] = new PositionedOreMarker()
                        {
                            Position = oreWorldPosition,
                            OreDeposit = oreMarker,
                            Name = oreMarkers.GetOreName(voxelMaterial),
                            Distance = diff.Length(),
                        };
                        nearestDistancesSquared[oreMaterial.Index] = distanceSquared;
                    }
                }
            }

            return nearestOreDeposits.Where(nod =>
                    nod?.OreDeposit?.VoxelMap != null && !nod.OreDeposit.VoxelMap.Closed);
        }

        public override GamePlayData Data()
        {
            CheckScreen();
            return new GamePlayData()
            {
                OreMarkers = CreateMarkers().Select(positionedOreMarker =>
                        new OreMarker()
                        {
                            Text = positionedOreMarker.Name,
                            Position = positionedOreMarker.Position.ToPlain(),
                            Distance = positionedOreMarker.Distance,
                            Materials = positionedOreMarker.OreDeposit.Materials
                                    .Select(data => data.Material.ToDefinitionId()).ToList(),
                        }
                ).ToList(),
                Hud = new Hud()
                {
                    Stats = MyHud.Stats.GetInstanceField<Dictionary<MyStringHash, IMyHudStat>>("m_stats").Values
                            .ToDictionary(
                                x => x.GetType().Name.Replace("MyStat", ""),
                                x => x.CurrentValue
                            )
                }
            };
        }

        public void ShowMainMenu()
        {
            CheckScreen();
            MyGuiSandbox.AddScreen(MyGuiSandbox.CreateScreen(MyPerGameSettings.GUI.MainMenu,
                MySandboxGame.IsPaused == false));
        }
    }
}