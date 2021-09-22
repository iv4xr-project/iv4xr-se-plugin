using System;
using System.Collections.Generic;
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
        public readonly List<FatNode> Nodes = new List<FatNode>();
    }
    
    public class NavGraphEditor
    {
        private class GridLocation
        {
            public GridLocation(IMySlimBlock block)
            {
                Block = block;
            }

            public IMySlimBlock Block;
            public bool Visited = false;
            public FatNode Node;
        }
        
        private readonly LowLevelObserver m_lowLevelObserver;

        private NavGraph m_graph = new NavGraph();
        
        public NavGraphEditor(LowLevelObserver lowLevelObserver)
        {
            m_lowLevelObserver = lowLevelObserver;
        }

        public NavGraph GetGraph()
        {
            CreateGraph();
            
            return m_graph;
        }
        
        private void CreateGraph()
        {
            var sphere = m_lowLevelObserver.GetBoundingSphere(
                m_lowLevelObserver.Radius * 2d);  // Get some look-ahead

            var grid = m_lowLevelObserver.CollectSurroundingRawBlocks(sphere)
                    .OrderByDescending(g => g.GetBlocks().Count).First();  // Get the biggest grid.
            
            // TODO: guess which face of the blocks in the grid is "up" (which surface to walk on)
            // perhaps compare the angle of all vectors with the up vector of the character?
            
            // TODO: offset the start position to be below the character's feet
            // TODO: calculate which direction is actually up
            m_graph = CreateGraph(grid, m_lowLevelObserver.GetPlayerPosition(), Vector3I.Up);
        }

        internal NavGraph CreateGraph(IMyCubeGrid grid, Vector3D start, Vector3I up)
        {
            var map = new Dictionary<Vector3I, GridLocation>(capacity: 256);

            IMySlimBlock startBlock = null;
            var minDistance = double.MaxValue;

            var blocks = new List<IMySlimBlock>();
            grid.GetBlocks(blocks);
            foreach (var block in blocks)
            {
                map[block.Position] = new GridLocation(block);

                var distanceSquared = (grid.GridIntegerToWorld(block.Position) - start).LengthSquared();
                if (distanceSquared < minDistance)
                {
                    minDistance = distanceSquared;
                    startBlock = block;
                }
            }

            if (startBlock is null)
                return null;

            var navGraph = new NavGraph();

            var steps = new StepVectors(up);
            var cubeQueue = new Queue<IMySlimBlock>();
            
            cubeQueue.Enqueue(startBlock);

            while (cubeQueue.Count > 0)
            {
                var currentCube = cubeQueue.Dequeue();
                var currentPosition = currentCube.Position;
                
                map[currentPosition].Visited = true;
                
                // check for obstacles (2 blocks above the site)
                if (map.ContainsKey(currentPosition + up) || map.ContainsKey(currentPosition + 2*up))
                    continue;

                var fatNode = new FatNode();
                map[currentPosition].Node = fatNode;
                navGraph.Nodes.Add(fatNode);

                foreach (var step in steps.EnumerateSides())  // TODO enumerate side neighbors
                {
                    if (!map.ContainsKey(currentPosition + step))
                        continue;
                    
                    var peakedLocation = map[currentPosition + step];
                    if (peakedLocation.Visited)
                    {
                        if (peakedLocation.Node != null)
                            fatNode.Neighbours.Add(peakedLocation.Node);
                    }
                    else
                    {
                        // TODO: check for obstacles even before enqueueing the block?
                        cubeQueue.Enqueue(peakedLocation.Block);
                    }
                }
            }

            return navGraph;
        }
    }
}
