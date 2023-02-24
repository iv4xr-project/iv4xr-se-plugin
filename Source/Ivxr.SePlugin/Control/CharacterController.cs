using System;
using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Engine.Multiplayer;
using Sandbox.Game;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Entities.Character.Components;
using Sandbox.Game.EntityComponents;
using Sandbox.Game.Gui;
using Sandbox.Game.Multiplayer;
using Sandbox.Game.World;
using VRage.Game;
using VRage.Game.Entity.UseObject;
using VRage.Game.ModAPI;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public class CharacterController : ICharacterController, ICharacterAdmin
    {
        private readonly IGameSession m_session;
        private readonly IObserver m_observer;
        private readonly LowLevelObserver m_lowLevelObserver;

        private readonly ILog Log;

        public CharacterController(IGameSession session, IObserver observer, LowLevelObserver lowLevelObserver,
            ILog log)
        {
            m_session = session;
            m_observer = observer;
            m_lowLevelObserver = lowLevelObserver;
            Log = log;
        }

        public CharacterObservation TurnOnDampeners()
        {
            EnsureCharacterLives();
            Character.JetpackComp.EnableDampeners(true);
            return m_observer.Observe();
        }

        public CharacterObservation TurnOnRelativeDampeners()
        {
            EnsureCharacterLives();
            MyMultiplayer.RaiseStaticEvent(s => MyPlayerCollection.SetDampeningEntity, Character.Entity.EntityId);
            return m_observer.Observe();
        }

        public CharacterObservation TurnOffDampeners()
        {
            EnsureCharacterLives();
            Character.JetpackComp.EnableDampeners(false);
            return m_observer.Observe();
        }

        public string LocalCharacterId()
        {
            var character = MySession.Static.LocalCharacter;
            return character?.CharacterId().ToString();
        }

        private MyResourceSourceComponent ResourceSource => Character.SuitBattery.ResourceSource;

        public void UpdateEnergy(float energy)
        {
            var absoluteEnergy = MyEnergyConstants.BATTERY_MAX_CAPACITY * energy;
            ResourceSource.SetRemainingCapacityByType(MyResourceDistributorComponent.ElectricityId, absoluteEnergy);
        }

        public void UpdateHealth(float health)
        {
            Character.StatComp.Health.CallMethod<object>("SetValue", new object[] { health * 100, null });
        }


        public void UpdateOxygen(float oxygen)
        {
            var oxygenId = MyCharacterOxygenComponent.OxygenId;
            Character.OxygenComponent.UpdateStoredGasLevel(ref oxygenId, oxygen);
        }

        public void UpdateHydrogen(float hydrogen)
        {
            var hydrogenId = MyCharacterOxygenComponent.HydrogenId;
            Character.OxygenComponent.UpdateStoredGasLevel(ref hydrogenId, hydrogen);
        }


        public CharacterObservation Jump(PlainVec3D movement)
        {
            EnsureCharacterLives();
            Character.Jump(movement.ToVector3());
            return m_observer.Observe();
        }

        public CharacterObservation TurnOnJetpack()
        {
            EnsureCharacterLives();
            Character.JetpackComp.TurnOnJetpack(true);
            return m_observer.Observe();
        }

        public CharacterObservation TurnOffJetpack()
        {
            EnsureCharacterLives();
            Character.JetpackComp.TurnOnJetpack(false);
            return m_observer.Observe();
        }

        public void SetHelmet(bool enabled)
        {
            EnsureCharacterLives();
            if (Character.OxygenComponent.HelmetEnabled == enabled)
            {
                return;
            }

            IMyControllableEntity entity = (IMyControllableEntity) Character;            
            entity.SwitchHelmet();
        }

        public void SetLight(bool enabled)
        {
            EnsureCharacterLives();
            Character.EnableLights(enabled);
        }

        public void SetBroadcasting(bool enabled)
        {
            EnsureCharacterLives();
            Character.EnableBroadcasting(enabled);
        }

        public void BeginUsingTool()
        {
            EnsureCharacterLives();
            var entityController = GetEntityController();
            entityController.ControlledEntity.BeginShoot(MyShootActionEnum.PrimaryAction);
        }

        public void EndUsingTool()
        {
            EnsureCharacterLives();
            var entityController = GetEntityController();
            entityController.ControlledEntity.EndShoot(MyShootActionEnum.PrimaryAction);
        }

        public void Use()
        {
            EnsureCharacterLives();
            MySession.Static.ControlledEntity.Use();
        }

        public void ShowTerminal()
        {
            EnsureCharacterLives();
            Character.ShowTerminal();
        }

        public void ShowInventory()
        {
            EnsureCharacterLives();
            Character.ShowInventory();
        }

        public bool SwitchParkedStatus()
        {
            EnsureCharacterLives();
            var controlledEntity = MySession.Static.ControlledEntity;
            if (!(controlledEntity is MyShipController ctrl))
                throw new InvalidOperationException(
                    $"Current ControlledEntity is not of type MyShipController, but {controlledEntity.GetType()}"
                );
            ctrl.SwitchParkedStatus();
            return ctrl.CubeGrid.IsParked;
        }

        public bool SwitchWalk()
        {
            EnsureCharacterLives();
            Character.SwitchWalk();
            return Character.WantsWalk;
        }

        public string MainCharacterId()
        {
            return m_session.MainCharacterId();
        }

        public void Use(string blockId, int functionIndex, int action)
        {
            var block = m_lowLevelObserver.GetBlockById(blockId);
            var objects = new List<IMyUseObject>();
            block.FatBlock.UseObjectsComponent.GetInteractiveObjects(objects);
            var obj = objects[functionIndex];
            obj.Use((UseActionEnum)action, Character);
        }

        public CharacterObservation Create(string name, PlainVec3D position, PlainVec3D orientationForward,
            PlainVec3D orientationUp)
        {
            m_session.CreateCharacter(name, position.ToVector3D(), orientationForward.ToVector3D(),
                orientationUp.ToVector3D());
            return m_observer.Observe();
        }

        public void Switch(string id)
        {
            m_session.SetCharacter(long.Parse(id));
        }

        public void Remove(string id)
        {
            m_session.RemoveCharacter(long.Parse(id));
        }

        public void ShowTerminal(string blockId)
        {
            var block = m_lowLevelObserver.GetBlockById(blockId);
            block.FatBlock.ThrowIfNull("FatBlock", "Block has to be functional to show terminal");
            MyGuiScreenTerminal.Show(MyTerminalPageEnum.Inventory, Character, block.FatBlock);
        }

        public void Die()
        {
            EnsureCharacterLives();
            Character.Die();
        }

        public CharacterObservation Teleport(PlainVec3D position, PlainVec3D? orientationForward,
            PlainVec3D? orientationUp)
        {
            var vecPosition = new Vector3D(position.ToVector3());
            if (orientationForward == null && orientationUp == null)
            {
                GetEntityController().ControlledEntity.Entity.PositionComp.SetPosition(vecPosition);
                return m_observer.Observe();
            }

            if (orientationForward == null || orientationUp == null)
            {
                throw new InvalidOperationException("Either both or none of the orientations are supposed to be set");
            }

            var matrix = MatrixD.CreateWorld(
                vecPosition,
                orientationForward?.ToVector3() ?? Vector3.Zero,
                orientationUp?.ToVector3() ?? Vector3.Zero
            );
            GetEntityController().Player.Character.PositionComp.SetWorldMatrix(ref matrix);
            return m_observer.Observe();
        }

        public CharacterObservation MoveAndRotate(PlainVec3D movement, PlainVec2F rotation3, float roll, int ticks)
        {
            EnsureCharacterLives();
            var vector3d = movement.ToVector3D();
            if (!Character.JetpackRunning)
            {
                if ((vector3d == Vector3D.Down && Character.CurrentMovementState == MyCharacterMovementEnum.Standing)
                    || (vector3d == Vector3D.Up && Character.CurrentMovementState == MyCharacterMovementEnum.Crouching))
                {
                    Character.Crouch();
                }
            }

            IvxrPlugin.Context.ContinuousMovementController.ChangeMovement(
                new ContinuousMovementContext()
                        { MoveVector = movement, RotationVector = rotation3, Roll = roll, TicksLeft = ticks }
            );

            return m_observer.Observe();
        }


        private MyEntityController GetEntityController()
        {
            if (m_session.Character is null)
                throw new NullReferenceException("I'm out of character!"); // Should not happen.

            var entityController = m_session.Character.ControllerInfo.Controller;

            if (entityController is null) // Happens when the character enters a vehicle, for example.
                throw new NotSupportedException("Entity control not possible now.");

            return entityController;
        }

        private MyCharacter Character => m_session.Character;

        private void EnsureCharacterLives(string message = "Cannot do this while dead")
        {
            if (Character.IsDead)
            {
                throw new InvalidOperationException(message);
            }
        }
    }
}
