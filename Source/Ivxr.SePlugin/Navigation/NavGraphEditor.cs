using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.SpaceEngineers.WorldModel;
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

        public NavGraphEditor(LowLevelObserver lowLevelObserver)
        {
            m_lowLevelObserver = lowLevelObserver;
        }

        /// <summary>
        /// Calculates navigation graph based on the largest grid in the surroundings of the character. 
        /// </summary>
        /// NOTE: The graph is calculated each time from scratch, it can probably be modified to allow for incremental
        /// updates, but it would be non-trivial. (Extending the platform should be easy, removing blocks or adding
        /// obstacles less easy.
        /// <returns></returns>
        public FatNavGraph GetGraph()
        {
            var sphere = m_lowLevelObserver.GetBoundingSphere(
                null,
                m_lowLevelObserver.Radius * 2d);  // Get some look-ahead

            var sourceGrid = m_lowLevelObserver.Grids().ToList()
                    .OrderByDescending(g => g.BlocksCount).First();  // Get the biggest grid.

            // Convert both "ends" of the orientation vector
            var zeroInGridCoordinates = sourceGrid.WorldToGridScaledLocal(Vector3D.Zero);
            var characterOrientationUpInGrid = sourceGrid.WorldToGridScaledLocal(
                m_lowLevelObserver.CurrentPlayerOrientationUp()) - zeroInGridCoordinates;
            
            // TODO: offset the start position to be below the character's feet
            return CreateGraph(m_lowLevelObserver.ConvertToSeGrid(sourceGrid, sphere),
                m_lowLevelObserver.CurrentPlayerPosition(),
                GuessWhichSideIsUp(characterOrientationUpInGrid));
        }

        internal FatNavGraph CreateGraph(CubeGrid grid, Vector3D start, Vector3I up)
        {
            var map = new Dictionary<Vector3I, GridLocation>(capacity: 256);

            var startBlock = InitMapAndFindStartBlock(grid, start, map);

            var navGraph = new FatNavGraph();

            var steps = new StepVectors(up);
            var cubeQueue = new Queue<Block>();
            var nodeBuilder = new FatNodeBuilder();
            
            cubeQueue.Enqueue(startBlock);

            while (cubeQueue.Count > 0)
            {
                var currentCube = cubeQueue.Dequeue();
                var currentPosition = currentCube.GridPosition.ToVector3I();
                
                map[currentPosition].Visited = true;
                
                // check for obstacles (2 blocks above the site)
                if (map.ContainsKey(currentPosition + up) || map.ContainsKey(currentPosition + 2*up))
                    continue;

                var fatNode = nodeBuilder.Create(currentCube.Position);  // TODO(P): Add some position offset.
                map[currentPosition].Node = fatNode;
                navGraph.Nodes.Add(fatNode);

                ExploreSides(map, cubeQueue, steps, currentPosition, fatNode);
            }

            return navGraph;
        }

        private static void ExploreSides(Dictionary<Vector3I, GridLocation> map,
            Queue<Block> cubeQueue, StepVectors steps, Vector3I currentPosition, FatNode fatNode)
        {
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
                    if (!cubeQueue.Contains(peakedLocation.Block)) // Note: this can be optimized
                        cubeQueue.Enqueue(peakedLocation.Block);
                }
            }
        }

        private static Block InitMapAndFindStartBlock(CubeGrid grid, Vector3D start, Dictionary<Vector3I, GridLocation> map)
        {
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
                throw new InvalidOperationException("Couldn't find a starting block for nav graph.");
            
            return startBlock;
        }

        /// <param name="characterOrientationUpInGrid">character orientation up converted to grid coordinates</param>
        internal static Vector3I GuessWhichSideIsUp(Vector3D characterOrientationUpInGrid)
        {
            var closestAngleVector = new StepVectors(Vector3I.Up).Enumerate()
                    .Select(v => new Tuple<double, Vector3I>(characterOrientationUpInGrid.Dot(v), v))
                    .OrderBy(x => x.Item1)
                    .Select(pair => pair.Item2).First();

            return ((characterOrientationUpInGrid + closestAngleVector).LengthSquared() >
                (characterOrientationUpInGrid - closestAngleVector).LengthSquared())
                    ? closestAngleVector
                    : -closestAngleVector;
        }
    }
}
