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
    public class NavGraphEditor
    {
        private class GridLocation
        {
            public GridLocation(Block block)
            {
                Block = block;
            }

            public Block Block;
            public bool Visited = false;
            public FatNode Node;
        }
        
        private readonly LowLevelObserver m_lowLevelObserver;

        private FatNavGraph m_graph = new FatNavGraph();
        
        public NavGraphEditor(LowLevelObserver lowLevelObserver)
        {
            m_lowLevelObserver = lowLevelObserver;
        }

        public FatNavGraph GetGraph()
        {
            CreateGraph();
            
            return m_graph;
        }
        
        private void CreateGraph()
        {
            var sphere = m_lowLevelObserver.GetBoundingSphere(
                m_lowLevelObserver.Radius * 2d);  // Get some look-ahead

            var grid = m_lowLevelObserver.CollectSurroundingBlocks(sphere, ObservationMode.BLOCKS)
                    .OrderByDescending(g => g.Blocks.Count).First();  // Get the biggest grid.
            
            // TODO: guess which face of the blocks in the grid is "up" (which surface to walk on)
            // perhaps compare the angle of all vectors with the up vector of the character?
            
            // TODO: offset the start position to be below the character's feet
            // TODO: calculate which direction is actually up
            m_graph = CreateGraph(grid, m_lowLevelObserver.GetPlayerPosition(), Vector3I.Up);
        }

        internal FatNavGraph CreateGraph(CubeGrid grid, Vector3D start, Vector3I up)
        {
            var map = new Dictionary<Vector3I, GridLocation>(capacity: 256);

            Block startBlock = null;
            var minDistance = double.MaxValue;

            foreach (var block in grid.Blocks)
            {
                map[block.GridPosition.ToVector3I()] = new GridLocation(block);

                var distanceSquared = (block.Position.ToVector3D() - start).LengthSquared();
                if (distanceSquared < minDistance)
                {
                    minDistance = distanceSquared;
                    startBlock = block;
                }
            }

            if (startBlock is null)
                return null;

            var navGraph = new FatNavGraph();

            var steps = new StepVectors(up);
            var cubeQueue = new Queue<Block>();
            
            cubeQueue.Enqueue(startBlock);

            while (cubeQueue.Count > 0)
            {
                var currentCube = cubeQueue.Dequeue();
                var currentPosition = currentCube.GridPosition.ToVector3I();
                
                map[currentPosition].Visited = true;
                
                // check for obstacles (2 blocks above the site)
                if (map.ContainsKey(currentPosition + up) || map.ContainsKey(currentPosition + 2*up))
                    continue;

                var fatNode = new FatNode(currentCube.Position);  // TODO(P): Add some position offset.
                map[currentPosition].Node = fatNode;
                navGraph.Nodes.Add(fatNode);

                foreach (var step in steps.EnumerateSides())
                {
                    var peakedGridPosition = currentPosition + step;
                    if (!map.ContainsKey(peakedGridPosition))
                        continue;
                    
                    var peakedLocation = map[peakedGridPosition];
                    if (peakedLocation.Visited)
                    {
                        if (peakedLocation.Node != null)
                        {
                            // Add both directions.
                            fatNode.Neighbours.Add(peakedLocation.Node);
                            peakedLocation.Node.Neighbours.Add(fatNode);
                        }
                    }
                    else
                    {
                        // TODO: check for obstacles even before enqueueing the block?
                        if (!cubeQueue.Contains(peakedLocation.Block))  // Note: this can be optimized
                            cubeQueue.Enqueue(peakedLocation.Block);
                    }
                }
            }

            return navGraph;
        }
    }
}
