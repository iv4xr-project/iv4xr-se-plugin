﻿using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib.WorldModel;
using Iv4xr.SePlugin.Control;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Cube;
using VRage.Game.ModAPI;
using VRageMath;

namespace Iv4xr.SePlugin.Navigation
{
    public class NavGraph
    {
        private readonly LowLevelObserver m_lowLevelObserver;
        
        public NavGraph(LowLevelObserver lowLevelObserver)
        {
            m_lowLevelObserver = lowLevelObserver;
        }
        
        private void CreateGraph()
        {
            var sphere = m_lowLevelObserver.GetBoundingSphere(
                m_lowLevelObserver.Radius * 2d);  // Get some look-ahead

            var grid = m_lowLevelObserver.CollectSurroundingRawBlocks(sphere)
                    .OrderByDescending(g => g.GetBlocks().Count).First();  // Get the biggest grid.
            
            // TODO: guess which face of the blocks in the grid is "up" (which surface to walk on)
            // perhaps compare the angle of all vectors with the up vector of the character?
            
            CreateGraph(grid);
        }

        private void CreateGraph(MyCubeGrid grid)
        {
            var map = new Dictionary<Vector3I, IMySlimBlock>(capacity: 256);

            foreach (var block in grid.GetBlocks())
            {
                map[block.Position] = block;
            }
        }
    }
}
