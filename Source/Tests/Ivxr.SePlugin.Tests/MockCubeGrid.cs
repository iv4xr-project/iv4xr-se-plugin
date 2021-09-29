using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Havok;
using Sandbox.Definitions;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.GameSystems;
using Sandbox.ModAPI;
using VRage.Game;
using VRage.Game.Components;
using VRage.Game.ModAPI;
using VRage.Game.ModAPI.Interfaces;
using VRage.Utils;
using VRageMath;
using VRageRender.Messages;

namespace Ivxr.SeGameLib.Tests
{
public class MyTestPhysicsComponentBase : MyPhysicsComponentBase
    {
        #region Not Implemented

        public override float Mass { get { throw new NotImplementedException(); } }

        public override Vector3 GetVelocityAtPoint(Vector3D worldPos)
        {
            throw new NotImplementedException();
        }

        public override void GetVelocityAtPointLocal(ref Vector3D worldPos, out Vector3 linearVelocity)
        {
            throw new NotImplementedException();
        }

        public override float LinearDamping
        {
            get;
            set;
        }

        public override float AngularDamping { get; set; }

        public override float Speed { get { throw new NotImplementedException(); } }

        public override float Friction { get; set; }

        public override HkRigidBody RigidBody { get; protected set; }

        public override HkRigidBody RigidBody2 { get; protected set; }

        public override HkdBreakableBody BreakableBody { get; set; }

        public override bool IsMoving { get { throw new NotImplementedException(); } }

        public override Vector3 Gravity { get; set; }

        public MyPhysicsComponentDefinitionBase Definition { get; private set; }

        public override bool IsActive { get { throw new NotImplementedException(); } }

        public override event Action<MyPhysicsComponentBase, bool> OnBodyActiveStateChanged;

        protected override void CloseRigidBody()
        {
            throw new NotImplementedException();
        }

        public override void AddForce(MyPhysicsForceType type, Vector3? force, Vector3D? position, Vector3? torque, float? maxSpeed = null, bool applyImmediately = true, bool activeOnly = false)
        {
            throw new NotImplementedException();
        }

        public override void ApplyImpulse(Vector3 dir, Vector3D pos)
        {
            throw new NotImplementedException();
        }

        public override void ClearSpeed()
        {
            throw new NotImplementedException();
        }

        public override void Clear()
        {
            throw new NotImplementedException();
        }

        public override void CreateCharacterCollision(Vector3 center, float characterWidth, float characterHeight,
            float crouchHeight, float ladderHeight, float headSize, float headHeight,
            MatrixD worldTransform, float mass, ushort collisionLayer, bool isOnlyVertical, float maxSlope, float maxLimit, float maxSpeedRelativeToShip, bool networkProxy, float? maxForce)
        {
            throw new NotImplementedException();
        }

        public override void DebugDraw()
        {
            throw new NotImplementedException();
        }

        public override void Activate()
        {
            throw new NotImplementedException();
        }

        public override void Deactivate()
        {
            throw new NotImplementedException();
        }

        public override void ForceActivate()
        {
            throw new NotImplementedException();
        }

        public override Vector3D WorldToCluster(Vector3D worldPos)
        {
            throw new NotImplementedException();
        }

        public override Vector3D ClusterToWorld(Vector3 clusterPos)
        {
            throw new NotImplementedException();
        }

        public override void GetWorldMatrix(out MatrixD matrix)
        {
            throw new NotImplementedException();
        }

        public override bool HasRigidBody { get { throw new NotImplementedException(); } }

        public override Vector3 CenterOfMassLocal { get { throw new NotImplementedException(); } }

        public override Vector3D CenterOfMassWorld { get { throw new NotImplementedException(); } }

        public override void UpdateFromSystem()
        {
            throw new NotImplementedException();
        }

        public override void OnWorldPositionChanged(object source)
        {
            throw new NotImplementedException();
        }

        #endregion
    }

    public class MyTestCubeGrid : IMyCubeGrid
    {
        private Vector3I m_min;
        private Vector3I m_max;
        private Dictionary<Vector3I, IMySlimBlock> m_blocks = new Dictionary<Vector3I, IMySlimBlock>();

        public IMySlimBlock GetCubeBlock(VRageMath.Vector3I pos)
        {
            IMySlimBlock block;
            m_blocks.TryGetValue(pos, out block);
            return block;
        }

        internal void AddBlock(Vector3I position, MyBlockOrientation orientation, MyCubeBlockDefinition blockDefinition)
        {
            Vector3I min = position;
            Vector3I max;
            MySlimBlock.ComputeMax(blockDefinition, orientation, ref min, out max);

            MyTestSlimBlock slimBlock = new MyTestSlimBlock(position, min, max, orientation, blockDefinition);


            Vector3I temp = min;
            for (var it = new Vector3I_RangeIterator(ref min, ref max); it.IsValid(); it.GetNext(out temp))
            {
                m_blocks.Add(temp, slimBlock);
            }

            if (min.X < m_min.X || min.Y < m_min.Y || min.Z < m_min.Z)
            {
                m_min = min;
            }

            if (min.X > m_max.X || min.Y > m_max.Y || min.Z > m_max.Z)
            {
                m_max = min;
            }

            OnBlockAdded.InvokeIfNotNull(slimBlock);
        }

        internal void RemoveBlock(Vector3I position)
        {
            IMySlimBlock slimBlock;
            m_blocks.TryGetValue(position, out slimBlock);
            m_blocks.Remove(position);

            OnBlockRemoved.InvokeIfNotNull(slimBlock);
        }

        internal void CalculateMinMax(MyGridGasSystem gasSystem)
        {
            Vector3I min = m_min;
            Vector3I max = m_max;

            RecalcBounds();

            if (min != m_min || max != m_max)
            {
                gasSystem.OnCubeGridShrinked();
            }
        }

        private void RecalcBounds()
        {
            m_min = Vector3I.MaxValue;
            m_max = Vector3I.MinValue;

            foreach (var c in m_blocks)
            {
                m_min = Vector3I.Min(m_min, c.Key);
                m_max = Vector3I.Max(m_max, c.Key);
            }

            if (m_blocks.Count == 0)
            {
                m_min = -Vector3I.One;
                m_max = Vector3I.One;
            }
        }

        public IMySlimBlock AddBlock(VRage.Game.MyObjectBuilder_CubeBlock objectBuilder, bool testMerge)
        {
            throw new NotImplementedException();
        }

        public VRageMath.Vector3I Max
        {
            get { return m_max; }
        }

        public VRageMath.Vector3I Min
        {
            get { return m_min; }
        }

        public VRage.Game.Components.MyPhysicsComponentBase Physics
        {
            get
            {
                return new MyTestPhysicsComponentBase();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        #region Not Implemented

        public void ApplyDestructionDeformation(IMySlimBlock block)
        {
            throw new NotImplementedException();
        }

        public List<long> BigOwners
        {
            get { throw new NotImplementedException(); }
        }

        public List<long> SmallOwners
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsRespawnGrid
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool IsStatic
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void ChangeGridOwnership(long playerId, VRage.Game.MyOwnershipShareModeEnum shareMode)
        {
            throw new NotImplementedException();
        }

        public void ClearSymmetries()
        {
            throw new NotImplementedException();
        }

        public void ColorBlocks(VRageMath.Vector3I min, VRageMath.Vector3I max, VRageMath.Vector3 newHSV)
        {
            throw new NotImplementedException();
        }

        public void SkinBlocks(VRageMath.Vector3I min, VRageMath.Vector3I max, VRageMath.Vector3? newHSV, string newSkin)
        {
            throw new NotImplementedException();
        }

        public void OnConvertToDynamic()
        {
            throw new NotImplementedException();
        }

        public void FixTargetCube(out VRageMath.Vector3I cube, VRageMath.Vector3 fractionalGridPosition)
        {
            throw new NotImplementedException();
        }

        public VRageMath.Vector3 GetClosestCorner(VRageMath.Vector3I gridPos, VRageMath.Vector3 position)
        {
            throw new NotImplementedException();
        }

        public string CustomName
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.Vector3D? GetLineIntersectionExactAll(ref VRageMath.LineD line, out double distance, out IMySlimBlock intersectedBlock)
        {
            throw new NotImplementedException();
        }

        public bool GetLineIntersectionExactGrid(ref VRageMath.LineD line, ref VRageMath.Vector3I position, ref double distanceSquared)
        {
            throw new NotImplementedException();
        }

        public bool IsTouchingAnyNeighbor(VRageMath.Vector3I min, VRageMath.Vector3I max)
        {
            throw new NotImplementedException();
        }

        public bool CanMergeCubes(IMyCubeGrid gridToMerge, VRageMath.Vector3I gridOffset)
        {
            throw new NotImplementedException();
        }

        public VRageMath.MatrixI CalculateMergeTransform(IMyCubeGrid gridToMerge, VRageMath.Vector3I gridOffset)
        {
            throw new NotImplementedException();
        }

        public IMyCubeGrid MergeGrid_MergeBlock(IMyCubeGrid gridToMerge, VRageMath.Vector3I gridOffset)
        {
            throw new NotImplementedException();
        }

        public VRageMath.Vector3I? RayCastBlocks(VRageMath.Vector3D worldStart, VRageMath.Vector3D worldEnd)
        {
            throw new NotImplementedException();
        }

        public void RayCastCells(VRageMath.Vector3D worldStart, VRageMath.Vector3D worldEnd, List<VRageMath.Vector3I> outHitPositions, VRageMath.Vector3I? gridSizeInflate = null, bool havokWorld = false)
        {
            throw new NotImplementedException();
        }

        public void RazeBlock(VRageMath.Vector3I position)
        {
            throw new NotImplementedException();
        }

        public void RazeBlocks(ref VRageMath.Vector3I pos, ref VRageMath.Vector3UByte size)
        {
            throw new NotImplementedException();
        }

        public void RazeBlocks(List<VRageMath.Vector3I> locations)
        {
            throw new NotImplementedException();
        }

        public void RemoveBlock(IMySlimBlock block, bool updatePhysics = false)
        {
            throw new NotImplementedException();
        }

        public void RemoveDestroyedBlock(IMySlimBlock block)
        {
            throw new NotImplementedException();
        }

        public void UpdateBlockNeighbours(IMySlimBlock block)
        {
            throw new NotImplementedException();
        }

        public VRageMath.Vector3I WorldToGridInteger(VRageMath.Vector3 coords)
        {
            throw new NotImplementedException();
        }

        public void GetBlocks(List<IMySlimBlock> blocks, Func<IMySlimBlock, bool> collect = null)
        {
            blocks.AddRange(m_blocks.Values.Where(x => (collect == null) || collect(x)));
        }

        public List<IMySlimBlock> GetBlocksInsideSphere(ref VRageMath.BoundingSphereD sphere)
        {
            throw new NotImplementedException();
        }

        public event Action<IMySlimBlock> OnBlockAdded;

        public event Action<IMySlimBlock> OnBlockRemoved;

        public event Action<IMyCubeGrid> OnBlockOwnershipChanged;

        public event Action<IMyCubeGrid> OnGridChanged;

        public event Action<IMyCubeGrid, IMyCubeGrid> OnGridSplit;

        public event Action<IMyCubeGrid, bool> OnIsStaticChanged;

        public event Action<IMySlimBlock> OnBlockIntegrityChanged;

        public void UpdateOwnership(long ownerId, bool isFunctional)
        {
            throw new NotImplementedException();
        }

        public bool WillRemoveBlockSplitGrid(IMySlimBlock testBlock)
        {
            throw new NotImplementedException();
        }

        public bool CanAddCube(VRageMath.Vector3I pos)
        {
            throw new NotImplementedException();
        }

        public bool CanAddCubes(VRageMath.Vector3I min, VRageMath.Vector3I max)
        {
            throw new NotImplementedException();
        }

        public IMyCubeGrid SplitByPlane(VRageMath.PlaneD plane)
        {
            throw new NotImplementedException();
        }

        public IMyCubeGrid Split(List<IMySlimBlock> blocks, bool sync = true)
        {
            throw new NotImplementedException();
        }

        public VRageMath.Vector3I? XSymmetryPlane
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.Vector3I? YSymmetryPlane
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.Vector3I? ZSymmetryPlane
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool XSymmetryOdd
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool YSymmetryOdd
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool ZSymmetryOdd
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyEntityComponentContainer Components
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.Game.Components.MyPositionComponentBase PositionComp
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyRenderComponentBase Render
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyEntityComponentBase GameLogic
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyHierarchyComponentBase Hierarchy
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MySyncComponentBase SyncObject
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.Game.Components.MyModStorageComponentBase Storage
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ModAPI.EntityFlags Flags
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public long EntityId
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public string Name
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public string GetFriendlyName()
        {
            throw new NotImplementedException();
        }

        public void Close()
        {
            throw new NotImplementedException();
        }

        public bool MarkedForClose
        {
            get { throw new NotImplementedException(); }
        }

        public void Delete()
        {
            throw new NotImplementedException();
        }

        public bool Closed
        {
            get { throw new NotImplementedException(); }
        }

        public bool DebugAsyncLoading
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.ObjectBuilders.MyObjectBuilder_EntityBase GetObjectBuilder(bool copy = false)
        {
            throw new NotImplementedException();
        }

        public bool Save
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ObjectBuilders.MyPersistentEntityFlags2 PersistentFlags
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public event Action<VRage.ModAPI.IMyEntity> OnClose;

        public event Action<VRage.ModAPI.IMyEntity> OnClosing;

        public event Action<VRage.ModAPI.IMyEntity> OnMarkForClose;

        public void BeforeSave()
        {
            throw new NotImplementedException();
        }

        public IMyModel Model
        {
            get { throw new NotImplementedException(); }
        }

        public bool Synchronized
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ModAPI.MyEntityUpdateEnum NeedsUpdate
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ModAPI.IMyEntity GetTopMostParent(Type type = null)
        {
            throw new NotImplementedException();
        }

        public VRage.ModAPI.IMyEntity Parent
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Matrix LocalMatrix
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void SetLocalMatrix(VRageMath.Matrix localMatrix, object source = null)
        {
            throw new NotImplementedException();
        }

        public void GetChildren(List<VRage.ModAPI.IMyEntity> children, Func<VRage.ModAPI.IMyEntity, bool> collect = null)
        {
            throw new NotImplementedException();
        }

        public VRage.Game.Entity.MyEntitySubpart GetSubpart(string name)
        {
            throw new NotImplementedException();
        }

        public bool TryGetSubpart(string name, out VRage.Game.Entity.MyEntitySubpart subpart)
        {
            throw new NotImplementedException();
        }

        public bool NearFlag
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool CastShadows
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool FastCastShadowResolve
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool NeedsResolveCastShadow
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.Vector3 GetDiffuseColor()
        {
            throw new NotImplementedException();
        }

        public float MaxGlassDistSq
        {
            get { throw new NotImplementedException(); }
        }

        public bool NeedsDraw
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool NeedsDrawFromParent
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool Transparent
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool ShadowBoxLod
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool SkipIfTooSmall
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool Visible
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool IsVisible()
        {
            throw new NotImplementedException();
        }

        public bool NeedsWorldMatrix
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void DebugDraw()
        {
            throw new NotImplementedException();
        }

        public void DebugDrawInvalidTriangles()
        {
            throw new NotImplementedException();
        }

        public void EnableColorMaskForSubparts(bool enable)
        {
            throw new NotImplementedException();
        }

        public void SetColorMaskForSubparts(VRageMath.Vector3 colorMaskHsv)
        {
            throw new NotImplementedException();
        }

        public void SetEmissiveParts(string emissiveName, VRageMath.Color emissivePartColor, float emissivity)
        {
            throw new NotImplementedException();
        }

        public void SetEmissivePartsForSubparts(string emissiveName, VRageMath.Color emissivePartColor, float emissivity)
        {
            throw new NotImplementedException();
        }

        public float GetDistanceBetweenCameraAndBoundingSphere()
        {
            throw new NotImplementedException();
        }

        public float GetDistanceBetweenCameraAndPosition()
        {
            throw new NotImplementedException();
        }

        public float GetLargestDistanceBetweenCameraAndBoundingSphere()
        {
            throw new NotImplementedException();
        }

        public float GetSmallestDistanceBetweenCameraAndBoundingSphere()
        {
            throw new NotImplementedException();
        }

        public bool InScene
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void OnRemovedFromScene(object source)
        {
            throw new NotImplementedException();
        }

        public void OnAddedToScene(object source)
        {
            throw new NotImplementedException();
        }

        public bool InvalidateOnMove
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.MatrixD GetViewMatrix()
        {
            throw new NotImplementedException();
        }

        public VRageMath.MatrixD GetWorldMatrixNormalizedInv()
        {
            throw new NotImplementedException();
        }

        public void SetWorldMatrix(VRageMath.MatrixD worldMatrix, object source = null)
        {
            throw new NotImplementedException();
        }

        public VRageMath.MatrixD WorldMatrix
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.MatrixD WorldMatrixInvScaled
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.MatrixD WorldMatrixNormalizedInv
        {
            get { throw new NotImplementedException(); }
        }

        public void SetPosition(VRageMath.Vector3D pos)
        {
            throw new NotImplementedException();
        }

        public void Teleport(VRageMath.MatrixD pos, object source = null, bool ignoreAssert = false)
        {
            throw new NotImplementedException();
        }

        public bool GetIntersectionWithLine(ref VRageMath.LineD line, out VRage.Game.Models.MyIntersectionResultLineTriangleEx? tri, VRage.Game.Components.IntersectionFlags flags)
        {
            throw new NotImplementedException();
        }

        public VRageMath.Vector3D? GetIntersectionWithLineAndBoundingSphere(ref VRageMath.LineD line, float boundingSphereRadiusMultiplier)
        {
            throw new NotImplementedException();
        }

        public bool GetIntersectionWithSphere(ref VRageMath.BoundingSphereD sphere)
        {
            throw new NotImplementedException();
        }

        public bool GetIntersectionWithAABB(ref VRageMath.BoundingBoxD aabb)
        {
            throw new NotImplementedException();
        }

        public void GetTrianglesIntersectingSphere(ref VRageMath.BoundingSphere sphere, VRageMath.Vector3? referenceNormalVector, float? maxAngle, List<VRage.Utils.MyTriangle_Vertex_Normals> retTriangles, int maxNeighbourTriangles)
        {
            throw new NotImplementedException();
        }

        public bool DoOverlapSphereTest(float sphereRadius, VRageMath.Vector3D spherePos)
        {
            throw new NotImplementedException();
        }

        public bool IsVolumetric
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingBox LocalAABB
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.BoundingBox LocalAABBHr
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingSphere LocalVolume
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.Vector3 LocalVolumeOffset
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public IMyInventory GetInventory()
        {
            throw new NotImplementedException();
        }

        public IMyInventory GetInventory(int index)
        {
            throw new NotImplementedException();
        }

        public event Action<VRage.ModAPI.IMyEntity> OnPhysicsChanged;
        public event Action<IMyCubeGrid> GridPresenceTierChanged;
        public event Action<IMyCubeGrid> PlayerPresenceTierChanged;

        public VRageMath.Vector3D LocationForHudMarker
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsCCDForProjectiles
        {
            get { throw new NotImplementedException(); }
        }

        public void AddToGamePruningStructure()
        {
            throw new NotImplementedException();
        }

        public void RemoveFromGamePruningStructure()
        {
            throw new NotImplementedException();
        }

        public void UpdateGamePruningStructure()
        {
            throw new NotImplementedException();
        }

        public string DisplayName
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }


        public bool HasInventory
        {
            get { throw new NotImplementedException(); }
        }

        public int InventoryCount
        {
            get { throw new NotImplementedException(); }
        }

        VRage.Game.ModAPI.Ingame.IMyInventory VRage.Game.ModAPI.Ingame.IMyEntity.GetInventory()
        {
            throw new NotImplementedException();
        }

        VRage.Game.ModAPI.Ingame.IMyInventory VRage.Game.ModAPI.Ingame.IMyEntity.GetInventory(int index)
        {
            throw new NotImplementedException();
        }

        public VRageMath.BoundingBoxD WorldAABB
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingBoxD WorldAABBHr
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingSphereD WorldVolume
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingSphereD WorldVolumeHr
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Vector3D GetPosition()
        {
            throw new NotImplementedException();
        }


        public float GridSize
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.Game.MyCubeSize GridSizeEnum
        {
            get { throw new NotImplementedException(); }
        }

        public MyUpdateTiersGridPresence GridPresenceTier => throw new NotImplementedException();

        public MyUpdateTiersPlayerPresence PlayerPresenceTier => throw new NotImplementedException();

        public bool CubeExists(VRageMath.Vector3I pos)
        {
            throw new NotImplementedException();
        }

        VRage.Game.ModAPI.Ingame.IMySlimBlock VRage.Game.ModAPI.Ingame.IMyCubeGrid.GetCubeBlock(VRageMath.Vector3I pos)
        {
            throw new NotImplementedException();
        }

        public VRageMath.Vector3D GridIntegerToWorld(VRageMath.Vector3I gridCoords)
        {
            throw new NotImplementedException();
        }

        public VRageMath.Vector3I WorldToGridInteger(VRageMath.Vector3D coords)
        {
            throw new NotImplementedException();
        }

        #endregion


        public bool IsInSameLogicalGroupAs(IMyCubeGrid other)
        {
            throw new NotImplementedException();
        }

        public bool IsSameConstructAs(IMyCubeGrid other)
        {
            throw new NotImplementedException();
        }


        public bool IsSameConstructAs(VRage.Game.ModAPI.Ingame.IMyCubeGrid other)
        {
            throw new NotImplementedException();
        }


        public bool IsRoomAtPositionAirtight(Vector3I vector3I)
        {
            throw new NotImplementedException();
        }

        public void SetTextureChangesForSubparts(Dictionary<string, MyTextureChange> value)
        {
            throw new NotImplementedException();
        }

        public void SkinBlocks(VRageMath.Vector3I min, VRageMath.Vector3I max, VRageMath.Vector3 newHSV, string newSkin)
        {
            throw new NotImplementedException();
        }
    }

    public class MyTestSlimBlock : IMySlimBlock
    {
        private Vector3I m_position;
        private Vector3I m_min;
        private Vector3I m_max;
        private MyBlockOrientation m_orientation;
        private MyDefinitionBase m_blockDefinition;
        private IMyCubeBlock m_fatBlock;

        public MyTestSlimBlock(Vector3I position, Vector3I min, Vector3I max, MyBlockOrientation orientation, MyDefinitionBase blockDefinition)
        {
            m_position = position;
            m_min = min;
            m_max = max;
            m_orientation = orientation;

            m_blockDefinition = blockDefinition;

            string typeName = blockDefinition.Id.TypeId.ToString();
            if (typeName != "MyObjectBuilder_CubeBlock")
            {
                if (typeName == "MyObjectBuilder_AirVent")
                {
                    throw new NotImplementedException();
                    //m_fatBlock = new MyTestAirVent(m_position);
                }
                else if (typeName == "MyObjectBuilder_Door")
                {
                    m_fatBlock = new MyTestDoor(m_position);
                }
                else
                {
                    m_fatBlock = new MyTestCubeBlock(m_position);
                }
            }
        }

        public VRageMath.MyBlockOrientation Orientation
        {
            get { return m_orientation; }
        }

        public IMyCubeBlock FatBlock
        {
            get { return m_fatBlock; }
        }

        public VRageMath.Vector3I Position
        {
            get { return m_position; }
        }

        public VRage.Game.MyDefinitionBase BlockDefinition
        {
            get { return m_blockDefinition; }
        }

        public float BuildLevelRatio
        {
            get { return 1; }
        }

        public VRageMath.Vector3I Min
        {
            get { return m_min; }
        }

        public VRageMath.Vector3I Max
        {
            get { return m_max; }
        }

        #region Not Implemented

        public void AddNeighbours()
        {
            throw new NotImplementedException();
        }

        public void ApplyAccumulatedDamage(bool addDirtyParts = true)
        {
            throw new NotImplementedException();
        }

        public string CalculateCurrentModel(out VRageMath.Matrix orientation)
        {
            throw new NotImplementedException();
        }

        public void ComputeScaledCenter(out VRageMath.Vector3D scaledCenter)
        {
            throw new NotImplementedException();
        }

        public void ComputeScaledHalfExtents(out VRageMath.Vector3 scaledHalfExtents)
        {
            throw new NotImplementedException();
        }

        public void ComputeWorldCenter(out VRageMath.Vector3D worldCenter)
        {
            throw new NotImplementedException();
        }

        public void FixBones(float oldDamage, float maxAllowedBoneMovement)
        {
            throw new NotImplementedException();
        }

        public void FullyDismount(IMyInventory outputInventory)
        {
            throw new NotImplementedException();
        }

        public VRage.Game.MyObjectBuilder_CubeBlock GetCopyObjectBuilder()
        {
            throw new NotImplementedException();
        }

        public VRage.Game.MyObjectBuilder_CubeBlock GetObjectBuilder(bool copy = false)
        {
            throw new NotImplementedException();
        }

        public void InitOrientation(ref VRageMath.Vector3I forward, ref VRageMath.Vector3I up)
        {
            throw new NotImplementedException();
        }

        public void InitOrientation(VRageMath.Base6Directions.Direction Forward, VRageMath.Base6Directions.Direction Up)
        {
            throw new NotImplementedException();
        }

        public void InitOrientation(VRageMath.MyBlockOrientation orientation)
        {
            throw new NotImplementedException();
        }

        public void MoveItemsFromConstructionStockpile(IMyInventory toInventory, VRage.Game.MyItemFlags flags = MyItemFlags.None)
        {
            throw new NotImplementedException();
        }

        public void RemoveNeighbours()
        {
            throw new NotImplementedException();
        }

        public void SetToConstructionSite()
        {
            throw new NotImplementedException();
        }

        public void SpawnConstructionStockpile()
        {
            throw new NotImplementedException();
        }

        public void SpawnFirstItemInConstructionStockpile()
        {
            throw new NotImplementedException();
        }

        public void UpdateVisual()
        {
            throw new NotImplementedException();
        }

        public IMyCubeGrid CubeGrid
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Vector3 GetColorMask()
        {
            throw new NotImplementedException();
        }

        public void DecreaseMountLevel(float grinderAmount, IMyInventory outputInventory, bool useDefaultDeconstructEfficiency = false)
        {
            throw new NotImplementedException();
        }

        public void IncreaseMountLevel(float welderMountAmount, long welderOwnerPlayerId, IMyInventory outputInventory = null, float maxAllowedBoneMovement = 0, bool isHelping = false, VRage.Game.MyOwnershipShareModeEnum share = MyOwnershipShareModeEnum.Faction)
        {
            throw new NotImplementedException();
        }

        public int GetConstructionStockpileItemAmount(VRage.Game.MyDefinitionId id)
        {
            throw new NotImplementedException();
        }

        public void MoveItemsToConstructionStockpile(IMyInventory fromInventory)
        {
            throw new NotImplementedException();
        }

        public void ClearConstructionStockpile(IMyInventory outputInventory)
        {
            throw new NotImplementedException();
        }

        public void PlayConstructionSound(MyIntegrityChangeEnum integrityChangeType, bool deconstruction = false)
        {
            throw new NotImplementedException();
        }

        public bool CanContinueBuild(IMyInventory sourceInventory)
        {
            throw new NotImplementedException();
        }

        public List<IMySlimBlock> Neighbours
        {
            get { throw new NotImplementedException(); }
        }

        public void GetWorldBoundingBox(out VRageMath.BoundingBoxD aabb, bool useAABBFromBlockCubes = false)
        {
            throw new NotImplementedException();
        }

        public float Dithering
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        VRage.ObjectBuilders.SerializableDefinitionId VRage.Game.ModAPI.Ingame.IMySlimBlock.BlockDefinition
        {
            get { throw new NotImplementedException(); }
        }

        public float AccumulatedDamage
        {
            get { throw new NotImplementedException(); }
        }

        public float BuildIntegrity
        {
            get { throw new NotImplementedException(); }
        }

        public float CurrentDamage
        {
            get { throw new NotImplementedException(); }
        }

        public float DamageRatio
        {
            get { throw new NotImplementedException(); }
        }

        VRage.Game.ModAPI.Ingame.IMyCubeBlock VRage.Game.ModAPI.Ingame.IMySlimBlock.FatBlock
        {
            get { throw new NotImplementedException(); }
        }

        public void GetMissingComponents(Dictionary<string, int> addToDictionary)
        {
            throw new NotImplementedException();
        }

        public bool HasDeformation
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsDestroyed
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsFullIntegrity
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsFullyDismounted
        {
            get { throw new NotImplementedException(); }
        }

        public float MaxDeformation
        {
            get { throw new NotImplementedException(); }
        }

        public float MaxIntegrity
        {
            get { throw new NotImplementedException(); }
        }

        public float Mass
        {
            get { throw new NotImplementedException(); }
        }

        public long OwnerId
        {
            get { throw new NotImplementedException(); }
        }

        public bool ShowParts
        {
            get { throw new NotImplementedException(); }
        }

        public bool StockpileAllocated
        {
            get { throw new NotImplementedException(); }
        }

        public bool StockpileEmpty
        {
            get { throw new NotImplementedException(); }
        }

        VRage.Game.ModAPI.Ingame.IMyCubeGrid VRage.Game.ModAPI.Ingame.IMySlimBlock.CubeGrid
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Vector3 ColorMaskHSV
        {
            get { throw new NotImplementedException(); }
        }

        public void OnDestroy()
        {
            throw new NotImplementedException();
        }

        public bool DoDamage(float damage, VRage.Utils.MyStringHash damageSource, bool sync, MyHitInfo? hitInfo = null, long attackerId = 0, long realHitEntityId = 0)
        {
            throw new NotImplementedException();
        }

        public float Integrity
        {
            get { throw new NotImplementedException(); }
        }

        public bool UseDamageSystem
        {
            get { throw new NotImplementedException(); }
        }

        public void SetTextureChangesForSubparts(Dictionary<string, MyTextureChange> value)
        {
            throw new NotImplementedException();
        }

        public void GetNeighbours(ICollection<IMySlimBlock> collection)
        {
            throw new NotImplementedException();
        }

        public MyStringHash SkinSubtypeId 
        {
            get { throw new NotImplementedException(); }
        }

        public long BuiltBy => throw new NotImplementedException();

        #endregion

        public void AddDecals(ref MyHitInfo hitInfo, MyStringHash source, Vector3 forwardDirection, object customdata, IMyDecalHandler decalHandler,
            MyStringHash physicalMaterial, MyStringHash voxelMaterial, bool isTrail)
        {
            throw new NotImplementedException();
        }
    }

    public class MyTestCubeBlock : IMyCubeBlock
    {
        private Vector3I m_position;

        public VRageMath.Vector3I Position
        {
            get { return m_position; }
        }

        public MyTestCubeBlock(Vector3I position)
        {
            m_position = position;
        }

        #region Not Implemented

        public event Action<IMyCubeBlock> IsWorkingChanged;

        public void CalcLocalMatrix(out VRageMath.Matrix localMatrix, out string currModel)
        {
            throw new NotImplementedException();
        }

        public string CalculateCurrentModel(out VRageMath.Matrix orientation)
        {
            throw new NotImplementedException();
        }

        public bool CheckConnectionAllowed
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public IMyCubeGrid CubeGrid
        {
            get { throw new NotImplementedException(); }
        }

        public bool DebugDraw()
        {
            throw new NotImplementedException();
        }

        public MyObjectBuilder_CubeBlock GetObjectBuilderCubeBlock(bool copy = false)
        {
            throw new NotImplementedException();
        }

        public MyRelationsBetweenPlayerAndBlock GetPlayerRelationToOwner()
        {
            throw new NotImplementedException();
        }

        public MyRelationsBetweenPlayerAndBlock GetUserRelationToOwner(long playerId)
        {
            throw new NotImplementedException();
        }

        public void Init()
        {
            throw new NotImplementedException();
        }

        public void Init(MyObjectBuilder_CubeBlock builder, IMyCubeGrid cubeGrid)
        {
            throw new NotImplementedException();
        }

        public void OnBuildSuccess(long builtBy)
        {
            throw new NotImplementedException();
        }

        public void OnBuildSuccess(long builtBy, bool instantBuild)
        {
            throw new NotImplementedException();
        }

        public void OnDestroy()
        {
            throw new NotImplementedException();
        }

        public void OnModelChange()
        {
            throw new NotImplementedException();
        }

        public void OnRegisteredToGridSystems()
        {
            throw new NotImplementedException();
        }

        public void OnRemovedByCubeBuilder()
        {
            throw new NotImplementedException();
        }

        public void OnUnregisteredFromGridSystems()
        {
            throw new NotImplementedException();
        }

        public string RaycastDetectors(VRageMath.Vector3D worldFrom, VRageMath.Vector3D worldTo)
        {
            throw new NotImplementedException();
        }

        public void ReloadDetectors(bool refreshNetworks = true)
        {
            throw new NotImplementedException();
        }

        public VRage.Game.Components.MyResourceSinkComponentBase ResourceSink
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void UpdateIsWorking()
        {
            throw new NotImplementedException();
        }

        public void UpdateVisual()
        {
            throw new NotImplementedException();
        }

        public void SetDamageEffect(bool start)
        {
            throw new NotImplementedException();
        }

        public bool SetEffect(string effectName, bool stopPrevious = false)
        {
            throw new NotImplementedException();
        }

        public bool SetEffect(string effectName, float parameter, bool stopPrevious = false, bool ignoreParameter = false, bool removeSameNameEffects = false)
        {
            throw new NotImplementedException();
        }

        public int RemoveEffect(string effectName, int exception = -1)
        {
            throw new NotImplementedException();
        }

        public Dictionary<string, float> UpgradeValues
        {
            get { throw new NotImplementedException(); }
        }

        public void AddUpgradeValue(string upgrade, float defaultValue)
        {
            throw new NotImplementedException();
        }

        public IMySlimBlock SlimBlock
        {
            get { throw new NotImplementedException(); }
        }

        public event Action OnUpgradeValuesChanged;

        public VRage.ObjectBuilders.SerializableDefinitionId BlockDefinition
        {
            get { throw new NotImplementedException(); }
        }

        VRage.Game.ModAPI.Ingame.IMyCubeGrid VRage.Game.ModAPI.Ingame.IMyCubeBlock.CubeGrid
        {
            get { throw new NotImplementedException(); }
        }

        public string DefinitionDisplayNameText
        {
            get { throw new NotImplementedException(); }
        }

        public float DisassembleRatio
        {
            get { throw new NotImplementedException(); }
        }

        public string DisplayNameText
        {
            get { throw new NotImplementedException(); }
        }

        public string GetOwnerFactionTag()
        {
            throw new NotImplementedException();
        }

        public bool IsBeingHacked
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsFunctional
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsWorking
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Vector3I Max
        {
            get { throw new NotImplementedException(); }
        }

        public float Mass
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Vector3I Min
        {
            get { throw new NotImplementedException(); }
        }

        public int NumberInGrid
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.MyBlockOrientation Orientation
        {
            get { throw new NotImplementedException(); }
        }

        public long OwnerId
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.Game.Components.MyEntityComponentContainer Components
        {
            get { throw new NotImplementedException(); }
        }

        public long EntityId
        {
            get { throw new NotImplementedException(); }
        }

        public string Name
        {
            get { throw new NotImplementedException(); }
        }

        public string DisplayName
        {
            get { throw new NotImplementedException(); }
        }

        public bool HasInventory
        {
            get { throw new NotImplementedException(); }
        }

        public int InventoryCount
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.Game.ModAPI.Ingame.IMyInventory GetInventory()
        {
            throw new NotImplementedException();
        }

        public VRage.Game.ModAPI.Ingame.IMyInventory GetInventory(int index)
        {
            throw new NotImplementedException();
        }

        public VRageMath.BoundingBoxD WorldAABB
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingBoxD WorldAABBHr
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.MatrixD WorldMatrix
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingSphereD WorldVolume
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingSphereD WorldVolumeHr
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Vector3D GetPosition()
        {
            throw new NotImplementedException();
        }


        public VRage.Game.Components.MyPhysicsComponentBase Physics
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyPositionComponentBase PositionComp
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyRenderComponentBase Render
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyEntityComponentBase GameLogic
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyHierarchyComponentBase Hierarchy
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MySyncComponentBase SyncObject
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.Game.Components.MyModStorageComponentBase Storage
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ModAPI.EntityFlags Flags
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        long VRage.ModAPI.IMyEntity.EntityId
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        string VRage.ModAPI.IMyEntity.Name
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public string GetFriendlyName()
        {
            throw new NotImplementedException();
        }

        public void Close()
        {
            throw new NotImplementedException();
        }

        public bool MarkedForClose
        {
            get { throw new NotImplementedException(); }
        }

        public void Delete()
        {
            throw new NotImplementedException();
        }

        public bool Closed
        {
            get { throw new NotImplementedException(); }
        }

        public bool DebugAsyncLoading
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.ObjectBuilders.MyObjectBuilder_EntityBase GetObjectBuilder(bool copy = false)
        {
            throw new NotImplementedException();
        }

        public bool Save
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ObjectBuilders.MyPersistentEntityFlags2 PersistentFlags
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public event Action<VRage.ModAPI.IMyEntity> OnClose;

        public event Action<VRage.ModAPI.IMyEntity> OnClosing;

        public event Action<VRage.ModAPI.IMyEntity> OnMarkForClose;

        public void BeforeSave()
        {
            throw new NotImplementedException();
        }

        public IMyModel Model
        {
            get { throw new NotImplementedException(); }
        }

        public bool Synchronized
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ModAPI.MyEntityUpdateEnum NeedsUpdate
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ModAPI.IMyEntity GetTopMostParent(Type type = null)
        {
            throw new NotImplementedException();
        }

        public VRage.ModAPI.IMyEntity Parent
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Matrix LocalMatrix
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void SetLocalMatrix(VRageMath.Matrix localMatrix, object source = null)
        {
            throw new NotImplementedException();
        }

        public void GetChildren(List<VRage.ModAPI.IMyEntity> children, Func<VRage.ModAPI.IMyEntity, bool> collect = null)
        {
            throw new NotImplementedException();
        }

        public VRage.Game.Entity.MyEntitySubpart GetSubpart(string name)
        {
            throw new NotImplementedException();
        }

        public bool TryGetSubpart(string name, out VRage.Game.Entity.MyEntitySubpart subpart)
        {
            throw new NotImplementedException();
        }

        public bool NearFlag
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool CastShadows
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool FastCastShadowResolve
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool NeedsResolveCastShadow
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.Vector3 GetDiffuseColor()
        {
            throw new NotImplementedException();
        }

        public float MaxGlassDistSq
        {
            get { throw new NotImplementedException(); }
        }

        public bool NeedsDraw
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool NeedsDrawFromParent
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool Transparent
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool ShadowBoxLod
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool SkipIfTooSmall
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool Visible
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool IsVisible()
        {
            throw new NotImplementedException();
        }

        public bool NeedsWorldMatrix
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        void VRage.ModAPI.IMyEntity.DebugDraw()
        {
            throw new NotImplementedException();
        }

        public void DebugDrawInvalidTriangles()
        {
            throw new NotImplementedException();
        }

        public void EnableColorMaskForSubparts(bool enable)
        {
            throw new NotImplementedException();
        }

        public void SetColorMaskForSubparts(VRageMath.Vector3 colorMaskHsv)
        {
            throw new NotImplementedException();
        }

        public void SetEmissiveParts(string emissiveName, VRageMath.Color emissivePartColor, float emissivity)
        {
            throw new NotImplementedException();
        }

        public void SetEmissivePartsForSubparts(string emissiveName, VRageMath.Color emissivePartColor, float emissivity)
        {
            throw new NotImplementedException();
        }

        public float GetDistanceBetweenCameraAndBoundingSphere()
        {
            throw new NotImplementedException();
        }

        public float GetDistanceBetweenCameraAndPosition()
        {
            throw new NotImplementedException();
        }

        public float GetLargestDistanceBetweenCameraAndBoundingSphere()
        {
            throw new NotImplementedException();
        }

        public float GetSmallestDistanceBetweenCameraAndBoundingSphere()
        {
            throw new NotImplementedException();
        }

        public bool InScene
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void OnRemovedFromScene(object source)
        {
            throw new NotImplementedException();
        }

        public void OnAddedToScene(object source)
        {
            throw new NotImplementedException();
        }

        public bool InvalidateOnMove
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.MatrixD GetViewMatrix()
        {
            throw new NotImplementedException();
        }

        public VRageMath.MatrixD GetWorldMatrixNormalizedInv()
        {
            throw new NotImplementedException();
        }

        public void SetWorldMatrix(VRageMath.MatrixD worldMatrix, object source = null)
        {
            throw new NotImplementedException();
        }

        VRageMath.MatrixD VRage.ModAPI.IMyEntity.WorldMatrix
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.MatrixD WorldMatrixInvScaled
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.MatrixD WorldMatrixNormalizedInv
        {
            get { throw new NotImplementedException(); }
        }

        public void SetPosition(VRageMath.Vector3D pos)
        {
            throw new NotImplementedException();
        }

        public void Teleport(VRageMath.MatrixD pos, object source = null, bool ignoreAssert = false)
        {
            throw new NotImplementedException();
        }

        public bool GetIntersectionWithLine(ref VRageMath.LineD line, out VRage.Game.Models.MyIntersectionResultLineTriangleEx? tri, VRage.Game.Components.IntersectionFlags flags)
        {
            throw new NotImplementedException();
        }

        public VRageMath.Vector3D? GetIntersectionWithLineAndBoundingSphere(ref VRageMath.LineD line, float boundingSphereRadiusMultiplier)
        {
            throw new NotImplementedException();
        }

        public bool GetIntersectionWithSphere(ref VRageMath.BoundingSphereD sphere)
        {
            throw new NotImplementedException();
        }

        public bool GetIntersectionWithAABB(ref VRageMath.BoundingBoxD aabb)
        {
            throw new NotImplementedException();
        }

        public void GetTrianglesIntersectingSphere(ref VRageMath.BoundingSphere sphere, VRageMath.Vector3? referenceNormalVector, float? maxAngle, List<VRage.Utils.MyTriangle_Vertex_Normals> retTriangles, int maxNeighbourTriangles)
        {
            throw new NotImplementedException();
        }

        public bool DoOverlapSphereTest(float sphereRadius, VRageMath.Vector3D spherePos)
        {
            throw new NotImplementedException();
        }

        public bool IsVolumetric
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingBox LocalAABB
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.BoundingBox LocalAABBHr
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingSphere LocalVolume
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.Vector3 LocalVolumeOffset
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        IMyInventory VRage.ModAPI.IMyEntity.GetInventory()
        {
            throw new NotImplementedException();
        }

        IMyInventory VRage.ModAPI.IMyEntity.GetInventory(int index)
        {
            throw new NotImplementedException();
        }

        public event Action<VRage.ModAPI.IMyEntity> OnPhysicsChanged;

        public VRageMath.Vector3D LocationForHudMarker
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsCCDForProjectiles
        {
            get { throw new NotImplementedException(); }
        }

        public void AddToGamePruningStructure()
        {
            throw new NotImplementedException();
        }

        public void RemoveFromGamePruningStructure()
        {
            throw new NotImplementedException();
        }

        public void UpdateGamePruningStructure()
        {
            throw new NotImplementedException();
        }

        string VRage.ModAPI.IMyEntity.DisplayName
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void SetTextureChangesForSubparts(Dictionary<string, MyTextureChange> value)
        {
            throw new NotImplementedException();
        }

        #endregion
    }

    public class MyTestDoor : IMyCubeBlock, IMyDoor
    {
        private Vector3I m_position;

        public VRageMath.Vector3I Position
        {
            get { return m_position; }
        }

        public MyTestDoor(Vector3I position)
        {
            m_position = position;
        }

        public Sandbox.ModAPI.Ingame.DoorStatus Status
        {
            get { return Sandbox.ModAPI.Ingame.DoorStatus.Closed; }
        }

        #region Not Implemented

        public event Action<IMyCubeBlock> IsWorkingChanged;

        public void CalcLocalMatrix(out VRageMath.Matrix localMatrix, out string currModel)
        {
            throw new NotImplementedException();
        }

        public string CalculateCurrentModel(out VRageMath.Matrix orientation)
        {
            throw new NotImplementedException();
        }

        public bool CheckConnectionAllowed
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public IMyCubeGrid CubeGrid
        {
            get { throw new NotImplementedException(); }
        }

        public bool DebugDraw()
        {
            throw new NotImplementedException();
        }

        public MyObjectBuilder_CubeBlock GetObjectBuilderCubeBlock(bool copy = false)
        {
            throw new NotImplementedException();
        }

        public MyRelationsBetweenPlayerAndBlock GetPlayerRelationToOwner()
        {
            throw new NotImplementedException();
        }

        public MyRelationsBetweenPlayerAndBlock GetUserRelationToOwner(long playerId)
        {
            throw new NotImplementedException();
        }

        public void Init()
        {
            throw new NotImplementedException();
        }

        public void Init(MyObjectBuilder_CubeBlock builder, IMyCubeGrid cubeGrid)
        {
            throw new NotImplementedException();
        }

        public void OnBuildSuccess(long builtBy)
        {
            throw new NotImplementedException();
        }

        public void OnBuildSuccess(long builtBy, bool instantBuild)
        {
            throw new NotImplementedException();
        }

        public void OnDestroy()
        {
            throw new NotImplementedException();
        }

        public void OnModelChange()
        {
            throw new NotImplementedException();
        }

        public void OnRegisteredToGridSystems()
        {
            throw new NotImplementedException();
        }

        public void OnRemovedByCubeBuilder()
        {
            throw new NotImplementedException();
        }

        public void OnUnregisteredFromGridSystems()
        {
            throw new NotImplementedException();
        }

        public string RaycastDetectors(VRageMath.Vector3D worldFrom, VRageMath.Vector3D worldTo)
        {
            throw new NotImplementedException();
        }

        public void ReloadDetectors(bool refreshNetworks = true)
        {
            throw new NotImplementedException();
        }

        public VRage.Game.Components.MyResourceSinkComponentBase ResourceSink
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void UpdateIsWorking()
        {
            throw new NotImplementedException();
        }

        public void UpdateVisual()
        {
            throw new NotImplementedException();
        }

        public void SetDamageEffect(bool start)
        {
            throw new NotImplementedException();
        }

        public bool SetEffect(string effectName, bool stopPrevious = false)
        {
            throw new NotImplementedException();
        }

        public bool SetEffect(string effectName, float parameter, bool stopPrevious = false, bool ignoreParameter = false, bool removeSameNameEffects = false)
        {
            throw new NotImplementedException();
        }

        public int RemoveEffect(string effectName, int exception = -1)
        {
            throw new NotImplementedException();
        }

        public Dictionary<string, float> UpgradeValues
        {
            get { throw new NotImplementedException(); }
        }

        public void AddUpgradeValue(string upgrade, float defaultValue)
        {
            throw new NotImplementedException();
        }

        public IMySlimBlock SlimBlock
        {
            get { throw new NotImplementedException(); }
        }

        public event Action OnUpgradeValuesChanged;

        public VRage.ObjectBuilders.SerializableDefinitionId BlockDefinition
        {
            get { throw new NotImplementedException(); }
        }

        VRage.Game.ModAPI.Ingame.IMyCubeGrid VRage.Game.ModAPI.Ingame.IMyCubeBlock.CubeGrid
        {
            get { throw new NotImplementedException(); }
        }

        public string DefinitionDisplayNameText
        {
            get { throw new NotImplementedException(); }
        }

        public float DisassembleRatio
        {
            get { throw new NotImplementedException(); }
        }

        public string DisplayNameText
        {
            get { throw new NotImplementedException(); }
        }

        public string GetOwnerFactionTag()
        {
            throw new NotImplementedException();
        }

        public bool IsBeingHacked
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsFunctional
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsWorking
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Vector3I Max
        {
            get { throw new NotImplementedException(); }
        }

        public float Mass
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Vector3I Min
        {
            get { throw new NotImplementedException(); }
        }

        public int NumberInGrid
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.MyBlockOrientation Orientation
        {
            get { throw new NotImplementedException(); }
        }

        public long OwnerId
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.Game.Components.MyEntityComponentContainer Components
        {
            get { throw new NotImplementedException(); }
        }

        public long EntityId
        {
            get { throw new NotImplementedException(); }
        }

        public string Name
        {
            get { throw new NotImplementedException(); }
        }

        public string DisplayName
        {
            get { throw new NotImplementedException(); }
        }

        public bool HasInventory
        {
            get { throw new NotImplementedException(); }
        }

        public int InventoryCount
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.Game.ModAPI.Ingame.IMyInventory GetInventory()
        {
            throw new NotImplementedException();
        }

        public VRage.Game.ModAPI.Ingame.IMyInventory GetInventory(int index)
        {
            throw new NotImplementedException();
        }

        public VRageMath.BoundingBoxD WorldAABB
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingBoxD WorldAABBHr
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.MatrixD WorldMatrix
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingSphereD WorldVolume
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingSphereD WorldVolumeHr
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Vector3D GetPosition()
        {
            throw new NotImplementedException();
        }


        public VRage.Game.Components.MyPhysicsComponentBase Physics
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyPositionComponentBase PositionComp
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyRenderComponentBase Render
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyEntityComponentBase GameLogic
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MyHierarchyComponentBase Hierarchy
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.Game.Components.MySyncComponentBase SyncObject
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.Game.Components.MyModStorageComponentBase Storage
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ModAPI.EntityFlags Flags
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        long VRage.ModAPI.IMyEntity.EntityId
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        string VRage.ModAPI.IMyEntity.Name
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public string GetFriendlyName()
        {
            throw new NotImplementedException();
        }

        public void Close()
        {
            throw new NotImplementedException();
        }

        public bool MarkedForClose
        {
            get { throw new NotImplementedException(); }
        }

        public void Delete()
        {
            throw new NotImplementedException();
        }

        public bool Closed
        {
            get { throw new NotImplementedException(); }
        }

        public bool DebugAsyncLoading
        {
            get { throw new NotImplementedException(); }
        }

        public VRage.ObjectBuilders.MyObjectBuilder_EntityBase GetObjectBuilder(bool copy = false)
        {
            throw new NotImplementedException();
        }

        public bool Save
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ObjectBuilders.MyPersistentEntityFlags2 PersistentFlags
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public event Action<VRage.ModAPI.IMyEntity> OnClose;

        public event Action<VRage.ModAPI.IMyEntity> OnClosing;

        public event Action<VRage.ModAPI.IMyEntity> OnMarkForClose;

        public void BeforeSave()
        {
            throw new NotImplementedException();
        }

        public IMyModel Model
        {
            get { throw new NotImplementedException(); }
        }

        public bool Synchronized
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ModAPI.MyEntityUpdateEnum NeedsUpdate
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRage.ModAPI.IMyEntity GetTopMostParent(Type type = null)
        {
            throw new NotImplementedException();
        }

        public VRage.ModAPI.IMyEntity Parent
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.Matrix LocalMatrix
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void SetLocalMatrix(VRageMath.Matrix localMatrix, object source = null)
        {
            throw new NotImplementedException();
        }

        public void GetChildren(List<VRage.ModAPI.IMyEntity> children, Func<VRage.ModAPI.IMyEntity, bool> collect = null)
        {
            throw new NotImplementedException();
        }

        public VRage.Game.Entity.MyEntitySubpart GetSubpart(string name)
        {
            throw new NotImplementedException();
        }

        public bool TryGetSubpart(string name, out VRage.Game.Entity.MyEntitySubpart subpart)
        {
            throw new NotImplementedException();
        }

        public bool NearFlag
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool CastShadows
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool FastCastShadowResolve
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool NeedsResolveCastShadow
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.Vector3 GetDiffuseColor()
        {
            throw new NotImplementedException();
        }

        public float MaxGlassDistSq
        {
            get { throw new NotImplementedException(); }
        }

        public bool NeedsDraw
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool NeedsDrawFromParent
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool Transparent
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool ShadowBoxLod
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool SkipIfTooSmall
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool Visible
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool IsVisible()
        {
            throw new NotImplementedException();
        }

        public bool NeedsWorldMatrix
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        void VRage.ModAPI.IMyEntity.DebugDraw()
        {
            throw new NotImplementedException();
        }

        public void DebugDrawInvalidTriangles()
        {
            throw new NotImplementedException();
        }

        public void EnableColorMaskForSubparts(bool enable)
        {
            throw new NotImplementedException();
        }

        public void SetColorMaskForSubparts(VRageMath.Vector3 colorMaskHsv)
        {
            throw new NotImplementedException();
        }

        public void SetEmissiveParts(string emissiveName, VRageMath.Color emissivePartColor, float emissivity)
        {
            throw new NotImplementedException();
        }

        public void SetEmissivePartsForSubparts(string emissiveName, VRageMath.Color emissivePartColor, float emissivity)
        {
            throw new NotImplementedException();
        }

        public float GetDistanceBetweenCameraAndBoundingSphere()
        {
            throw new NotImplementedException();
        }

        public float GetDistanceBetweenCameraAndPosition()
        {
            throw new NotImplementedException();
        }

        public float GetLargestDistanceBetweenCameraAndBoundingSphere()
        {
            throw new NotImplementedException();
        }

        public float GetSmallestDistanceBetweenCameraAndBoundingSphere()
        {
            throw new NotImplementedException();
        }

        public bool InScene
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void OnRemovedFromScene(object source)
        {
            throw new NotImplementedException();
        }

        public void OnAddedToScene(object source)
        {
            throw new NotImplementedException();
        }

        public bool InvalidateOnMove
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.MatrixD GetViewMatrix()
        {
            throw new NotImplementedException();
        }

        public VRageMath.MatrixD GetWorldMatrixNormalizedInv()
        {
            throw new NotImplementedException();
        }

        public void SetWorldMatrix(VRageMath.MatrixD worldMatrix, object source = null)
        {
            throw new NotImplementedException();
        }

        VRageMath.MatrixD VRage.ModAPI.IMyEntity.WorldMatrix
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.MatrixD WorldMatrixInvScaled
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.MatrixD WorldMatrixNormalizedInv
        {
            get { throw new NotImplementedException(); }
        }

        public void SetPosition(VRageMath.Vector3D pos)
        {
            throw new NotImplementedException();
        }

        public void Teleport(VRageMath.MatrixD pos, object source = null, bool ignoreAssert = false)
        {
            throw new NotImplementedException();
        }

        public bool GetIntersectionWithLine(ref VRageMath.LineD line, out VRage.Game.Models.MyIntersectionResultLineTriangleEx? tri, VRage.Game.Components.IntersectionFlags flags)
        {
            throw new NotImplementedException();
        }

        public VRageMath.Vector3D? GetIntersectionWithLineAndBoundingSphere(ref VRageMath.LineD line, float boundingSphereRadiusMultiplier)
        {
            throw new NotImplementedException();
        }

        public bool GetIntersectionWithSphere(ref VRageMath.BoundingSphereD sphere)
        {
            throw new NotImplementedException();
        }

        public bool GetIntersectionWithAABB(ref VRageMath.BoundingBoxD aabb)
        {
            throw new NotImplementedException();
        }

        public void GetTrianglesIntersectingSphere(ref VRageMath.BoundingSphere sphere, VRageMath.Vector3? referenceNormalVector, float? maxAngle, List<VRage.Utils.MyTriangle_Vertex_Normals> retTriangles, int maxNeighbourTriangles)
        {
            throw new NotImplementedException();
        }

        public bool DoOverlapSphereTest(float sphereRadius, VRageMath.Vector3D spherePos)
        {
            throw new NotImplementedException();
        }

        public bool IsVolumetric
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingBox LocalAABB
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.BoundingBox LocalAABBHr
        {
            get { throw new NotImplementedException(); }
        }

        public VRageMath.BoundingSphere LocalVolume
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public VRageMath.Vector3 LocalVolumeOffset
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        IMyInventory VRage.ModAPI.IMyEntity.GetInventory()
        {
            throw new NotImplementedException();
        }

        IMyInventory VRage.ModAPI.IMyEntity.GetInventory(int index)
        {
            throw new NotImplementedException();
        }

        public event Action<VRage.ModAPI.IMyEntity> OnPhysicsChanged;

        public VRageMath.Vector3D LocationForHudMarker
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsCCDForProjectiles
        {
            get { throw new NotImplementedException(); }
        }

        public void AddToGamePruningStructure()
        {
            throw new NotImplementedException();
        }

        public void RemoveFromGamePruningStructure()
        {
            throw new NotImplementedException();
        }

        public void UpdateGamePruningStructure()
        {
            throw new NotImplementedException();
        }

        string VRage.ModAPI.IMyEntity.DisplayName
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        #endregion

        #region Not Implemented

        public event Action<bool> DoorStateChanged;

        public event Action<IMyDoor, bool> OnDoorStateChanged;

        public event Action<IMyTerminalBlock> EnabledChanged;

        public bool Enabled
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void RequestEnable(bool enable)
        {
            throw new NotImplementedException();
        }

        public string CustomName
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public string CustomNameWithFaction
        {
            get { throw new NotImplementedException(); }
        }

        public string DetailedInfo
        {
            get { throw new NotImplementedException(); }
        }

        public string CustomInfo
        {
            get { throw new NotImplementedException(); }
        }

        public string CustomData
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool HasLocalPlayerAccess()
        {
            throw new NotImplementedException();
        }

        public bool HasPlayerAccess(long playerId)
        {
            throw new NotImplementedException();
        }

        public void SetCustomName(string text)
        {
            throw new NotImplementedException();
        }

        public void SetCustomName(StringBuilder text)
        {
            throw new NotImplementedException();
        }

        public bool ShowOnHUD
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool ShowInTerminal
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public bool ShowInToolbarConfig
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void GetActions(List<Sandbox.ModAPI.Interfaces.ITerminalAction> resultList, Func<Sandbox.ModAPI.Interfaces.ITerminalAction, bool> collect = null)
        {
            throw new NotImplementedException();
        }

        public void SearchActionsOfName(string name, List<Sandbox.ModAPI.Interfaces.ITerminalAction> resultList, Func<Sandbox.ModAPI.Interfaces.ITerminalAction, bool> collect = null)
        {
            throw new NotImplementedException();
        }

        public Sandbox.ModAPI.Interfaces.ITerminalAction GetActionWithName(string name)
        {
            throw new NotImplementedException();
        }

        public Sandbox.ModAPI.Interfaces.ITerminalProperty GetProperty(string id)
        {
            throw new NotImplementedException();
        }

        public void GetProperties(List<Sandbox.ModAPI.Interfaces.ITerminalProperty> resultList, Func<Sandbox.ModAPI.Interfaces.ITerminalProperty, bool> collect = null)
        {
            throw new NotImplementedException();
        }

        public bool ShowInInventory
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public event Action<IMyTerminalBlock> CustomDataChanged;

        public event Action<IMyTerminalBlock> CustomNameChanged;

        public event Action<IMyTerminalBlock> OwnershipChanged;

        public event Action<IMyTerminalBlock> PropertiesChanged;

        public event Action<IMyTerminalBlock> ShowOnHUDChanged;

        public event Action<IMyTerminalBlock> VisibilityChanged;

        public event Action<IMyTerminalBlock, StringBuilder> AppendingCustomInfo;
        public event Action<IMyFunctionalBlock> UpdateTimerTriggered;

        public void RefreshCustomInfo()
        {
            throw new NotImplementedException();
        }

        public bool Open
        {
            get { throw new NotImplementedException(); }
        }

        public float OpenRatio
        {
            get { throw new NotImplementedException(); }
        }

        public void OpenDoor()
        {
            throw new NotImplementedException();
        }

        public void CloseDoor()
        {
            throw new NotImplementedException();
        }

        public void ToggleDoor()
        {
            throw new NotImplementedException();
        }

        public void SetTextureChangesForSubparts(Dictionary<string, MyTextureChange> value)
        {
            throw new NotImplementedException();
        }

        #endregion


        public bool IsSameConstructAs(Sandbox.ModAPI.Ingame.IMyTerminalBlock other)
        {
            throw new NotImplementedException();
        }


        public bool IsInSameLogicalGroupAs(IMyTerminalBlock other)
        {
            throw new NotImplementedException();
        }

        public bool IsSameConstructAs(IMyTerminalBlock other)
        {
            throw new NotImplementedException();
        }

        public uint GetFramesFromLastTrigger()
        {
            throw new NotImplementedException();
        }

        public bool IsFullyClosed
        {
            get { return true; }
        }

        public bool IsUpdateTimerCreated => throw new NotImplementedException();

        public bool IsUpdateTimerEnabled => throw new NotImplementedException();
    }
}
