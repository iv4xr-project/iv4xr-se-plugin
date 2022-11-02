package spaceEngineers.model.typing

object DefinitionIds {

    object ScenarioDefinition : SafeDefinitionIdId() {
        val EarthEasyStart = create(this, DefinitionIdTypes.EarthEasyStart)
        val EasyMarsStart = create(this, DefinitionIdTypes.EasyMarsStart)
        val EasyAlienStart = create(this, DefinitionIdTypes.EasyAlienStart)
        val EasyMoonStart = create(this, DefinitionIdTypes.EasyMoonStart)
        val StarSystem = create(this, DefinitionIdTypes.StarSystem)
        val Planet = create(this, DefinitionIdTypes.Planet)
        val EasyStart1 = create(this, DefinitionIdTypes.EasyStart1)
        val EasyStart2 = create(this, DefinitionIdTypes.EasyStart2)
        val LoneSurvivor = create(this, DefinitionIdTypes.LoneSurvivor)
        val DebugStart = create(this, DefinitionIdTypes.DebugStart)
        val CrashedRedShip = create(this, DefinitionIdTypes.CrashedRedShip)
        val TwoPlatforms = create(this, DefinitionIdTypes.TwoPlatforms)
        val Asteroids = create(this, DefinitionIdTypes.Asteroids)
        val EmptyWorld = create(this, DefinitionIdTypes.EmptyWorld)
        val UraniumHeist = create(this, DefinitionIdTypes.UraniumHeist)
    }

    object AmmoMagazine : SafeDefinitionIdId() {
        val SemiAutoPistolMagazine = create(this, DefinitionIdTypes.SemiAutoPistolMagazine)
        val FullAutoPistolMagazine = create(this, DefinitionIdTypes.FullAutoPistolMagazine)
        val ElitePistolMagazine = create(this, DefinitionIdTypes.ElitePistolMagazine)
        val AutomaticRifleGun_Mag_20rd = create(this, DefinitionIdTypes.AutomaticRifleGun_Mag_20rd)
        val RapidFireAutomaticRifleGun_Mag_50rd = create(this, DefinitionIdTypes.RapidFireAutomaticRifleGun_Mag_50rd)
        val PreciseAutomaticRifleGun_Mag_5rd = create(this, DefinitionIdTypes.PreciseAutomaticRifleGun_Mag_5rd)
        val UltimateAutomaticRifleGun_Mag_30rd = create(this, DefinitionIdTypes.UltimateAutomaticRifleGun_Mag_30rd)
        val NATO_5p56x45mm = create(this, DefinitionIdTypes.NATO_5p56x45mm)
        val AutocannonClip = create(this, DefinitionIdTypes.AutocannonClip)
        val NATO_25x184mm = create(this, DefinitionIdTypes.NATO_25x184mm)
        val Missile200mm = create(this, DefinitionIdTypes.Missile200mm)
        val LargeCalibreAmmo = create(this, DefinitionIdTypes.LargeCalibreAmmo)
        val MediumCalibreAmmo = create(this, DefinitionIdTypes.MediumCalibreAmmo)
        val LargeRailgunAmmo = create(this, DefinitionIdTypes.LargeRailgunAmmo)
        val SmallRailgunAmmo = create(this, DefinitionIdTypes.SmallRailgunAmmo)
    }

    object AnimationDefinition : SafeDefinitionIdId() {
        val Idle = create(this, DefinitionIdTypes.Idle)
        val StandLeftTurn = create(this, DefinitionIdTypes.StandLeftTurn)
        val StandRightTurn = create(this, DefinitionIdTypes.StandRightTurn)
        val Walk = create(this, DefinitionIdTypes.Walk)
        val WalkBack = create(this, DefinitionIdTypes.WalkBack)
        val WalkRightFront = create(this, DefinitionIdTypes.WalkRightFront)
        val WalkRightBack = create(this, DefinitionIdTypes.WalkRightBack)
        val WalkLeftFront = create(this, DefinitionIdTypes.WalkLeftFront)
        val WalkLeftBack = create(this, DefinitionIdTypes.WalkLeftBack)
        val StrafeLeft = create(this, DefinitionIdTypes.StrafeLeft)
        val StrafeRight = create(this, DefinitionIdTypes.StrafeRight)
        val Run = create(this, DefinitionIdTypes.Run)
        val RunBack = create(this, DefinitionIdTypes.RunBack)
        val RunLeft = create(this, DefinitionIdTypes.RunLeft)
        val RunRight = create(this, DefinitionIdTypes.RunRight)
        val RunRightFront = create(this, DefinitionIdTypes.RunRightFront)
        val RunRightBack = create(this, DefinitionIdTypes.RunRightBack)
        val RunLeftFront = create(this, DefinitionIdTypes.RunLeftFront)
        val RunLeftBack = create(this, DefinitionIdTypes.RunLeftBack)
        val Sprint = create(this, DefinitionIdTypes.Sprint)
        val CrouchWalk = create(this, DefinitionIdTypes.CrouchWalk)
        val CrouchWalkBack = create(this, DefinitionIdTypes.CrouchWalkBack)
        val CrouchIdle = create(this, DefinitionIdTypes.CrouchIdle)
        val CrouchStrafeLeft = create(this, DefinitionIdTypes.CrouchStrafeLeft)
        val CrouchStrafeRight = create(this, DefinitionIdTypes.CrouchStrafeRight)
        val CrouchWalkRightFront = create(this, DefinitionIdTypes.CrouchWalkRightFront)
        val CrouchWalkRightBack = create(this, DefinitionIdTypes.CrouchWalkRightBack)
        val CrouchWalkLeftFront = create(this, DefinitionIdTypes.CrouchWalkLeftFront)
        val CrouchWalkLeftBack = create(this, DefinitionIdTypes.CrouchWalkLeftBack)
        val CrouchLeftTurn = create(this, DefinitionIdTypes.CrouchLeftTurn)
        val CrouchRightTurn = create(this, DefinitionIdTypes.CrouchRightTurn)
        val Jump = create(this, DefinitionIdTypes.Jump)
        val FreeFall = create(this, DefinitionIdTypes.FreeFall)
        val Jetpack = create(this, DefinitionIdTypes.Jetpack)
        val Died = create(this, DefinitionIdTypes.Died)
        val DiedFps = create(this, DefinitionIdTypes.DiedFps)
        val Wave = create(this, DefinitionIdTypes.Wave)
        val HelmetOpen = create(this, DefinitionIdTypes.HelmetOpen)
        val HelmetClose = create(this, DefinitionIdTypes.HelmetClose)
        val HoldingTool = create(this, DefinitionIdTypes.HoldingTool)
        val HoldingWeapon = create(this, DefinitionIdTypes.HoldingWeapon)
        val HoldingPistol = create(this, DefinitionIdTypes.HoldingPistol)
        val Victory = create(this, DefinitionIdTypes.Victory)
        val Thumb_Up = create(this, DefinitionIdTypes.Thumb_Up)
        val FacePalm = create(this, DefinitionIdTypes.FacePalm)
        val UseCubePlacerFingers = create(this, DefinitionIdTypes.UseCubePlacerFingers)
        val cockpit23 = create(this, DefinitionIdTypes.cockpit23)
        val cockpit1_large = create(this, DefinitionIdTypes.cockpit1_large)
        val cockpit1_small = create(this, DefinitionIdTypes.cockpit1_small)
        val passengerseat_large = create(this, DefinitionIdTypes.passengerseat_large)
        val Passengerseat_large_Adjusted = create(this, DefinitionIdTypes.Passengerseat_large_Adjusted)
        val PassengerBench = create(this, DefinitionIdTypes.PassengerBench)
        val FighterCockpitPosture = create(this, DefinitionIdTypes.FighterCockpitPosture)
        val Bed_Laying_Pose = create(this, DefinitionIdTypes.Bed_Laying_Pose)
        val Bed_Laying_Pose_Female = create(this, DefinitionIdTypes.Bed_Laying_Pose_Female)
        val Table_Sitting_Pose = create(this, DefinitionIdTypes.Table_Sitting_Pose)
        val Table_Sitting_Pose_Female = create(this, DefinitionIdTypes.Table_Sitting_Pose_Female)
        val passengerseat_small = create(this, DefinitionIdTypes.passengerseat_small)
        val Passengerseat_Small_Adjusted = create(this, DefinitionIdTypes.Passengerseat_Small_Adjusted)
        val RoverCockpitPose = create(this, DefinitionIdTypes.RoverCockpitPose)
        val BuggyCockpitPose = create(this, DefinitionIdTypes.BuggyCockpitPose)
        val HelmCockpitPose = create(this, DefinitionIdTypes.HelmCockpitPose)
        val HelmCockpitPoseSmall = create(this, DefinitionIdTypes.HelmCockpitPoseSmall)
        val LadderUp = create(this, DefinitionIdTypes.LadderUp)
        val LadderDown = create(this, DefinitionIdTypes.LadderDown)
        val IdleSpider1 = create(this, DefinitionIdTypes.IdleSpider1)
        val DeburrowSpider = create(this, DefinitionIdTypes.DeburrowSpider)
        val BurrowSpider = create(this, DefinitionIdTypes.BurrowSpider)
        val AttackStinger = create(this, DefinitionIdTypes.AttackStinger)
        val AttackFrontLegs = create(this, DefinitionIdTypes.AttackFrontLegs)
        val AttackBite = create(this, DefinitionIdTypes.AttackBite)
        val WalkSpider = create(this, DefinitionIdTypes.WalkSpider)
        val RunSpider = create(this, DefinitionIdTypes.RunSpider)
        val DeathSpider = create(this, DefinitionIdTypes.DeathSpider)
        val test_emote = create(this, DefinitionIdTypes.test_emote)
        val Idle_Female = create(this, DefinitionIdTypes.Idle_Female)
        val Angry_Female = create(this, DefinitionIdTypes.Angry_Female)
        val Cold_Female = create(this, DefinitionIdTypes.Cold_Female)
        val CheckWrist_Female = create(this, DefinitionIdTypes.CheckWrist_Female)
        val AssistEnd_Female = create(this, DefinitionIdTypes.AssistEnd_Female)
        val AssistCome_Female = create(this, DefinitionIdTypes.AssistCome_Female)
        val Dance_Female = create(this, DefinitionIdTypes.Dance_Female)
        val PointAggressive_Female = create(this, DefinitionIdTypes.PointAggressive_Female)
        val PointBack_Female = create(this, DefinitionIdTypes.PointBack_Female)
        val PointDown_Female = create(this, DefinitionIdTypes.PointDown_Female)
        val PointForward_Female = create(this, DefinitionIdTypes.PointForward_Female)
        val PointLeft_Female = create(this, DefinitionIdTypes.PointLeft_Female)
        val PointRight_Female = create(this, DefinitionIdTypes.PointRight_Female)
        val Charge_Female = create(this, DefinitionIdTypes.Charge_Female)
        val Yelling_Female = create(this, DefinitionIdTypes.Yelling_Female)
        val GotHit_Female = create(this, DefinitionIdTypes.GotHit_Female)
        val Whatever_Female = create(this, DefinitionIdTypes.Whatever_Female)
        val FingerGuns_Female = create(this, DefinitionIdTypes.FingerGuns_Female)
        val DanceDisco1_Female = create(this, DefinitionIdTypes.DanceDisco1_Female)
        val DanceDisco2_Female = create(this, DefinitionIdTypes.DanceDisco2_Female)
        val LookingAround_Female = create(this, DefinitionIdTypes.LookingAround_Female)
        val Stretching_Female = create(this, DefinitionIdTypes.Stretching_Female)
        val ComeHereBaby_Female = create(this, DefinitionIdTypes.ComeHereBaby_Female)
        val FYou_Female = create(this, DefinitionIdTypes.FYou_Female)
        val Drunk_Female = create(this, DefinitionIdTypes.Drunk_Female)
        val RPS_Paper_Female = create(this, DefinitionIdTypes.RPS_Paper_Female)
        val RPS_Rock_Female = create(this, DefinitionIdTypes.RPS_Rock_Female)
        val RPS_Scissor_Female = create(this, DefinitionIdTypes.RPS_Scissor_Female)
        val Salute_Female = create(this, DefinitionIdTypes.Salute_Female)
        val Angry = create(this, DefinitionIdTypes.Angry)
        val AssistEnd = create(this, DefinitionIdTypes.AssistEnd)
        val AssistCome = create(this, DefinitionIdTypes.AssistCome)
        val Dance = create(this, DefinitionIdTypes.Dance)
        val Charge = create(this, DefinitionIdTypes.Charge)
        val ComeHereBaby = create(this, DefinitionIdTypes.ComeHereBaby)
        val DanceDisco1 = create(this, DefinitionIdTypes.DanceDisco1)
        val DanceDisco2 = create(this, DefinitionIdTypes.DanceDisco2)
        val LookingAround = create(this, DefinitionIdTypes.LookingAround)
        val Stretching = create(this, DefinitionIdTypes.Stretching)
        val Whatever = create(this, DefinitionIdTypes.Whatever)
        val FingerGuns = create(this, DefinitionIdTypes.FingerGuns)
        val Yelling = create(this, DefinitionIdTypes.Yelling)
        val GotHit = create(this, DefinitionIdTypes.GotHit)
        val PointAggressive = create(this, DefinitionIdTypes.PointAggressive)
        val PointBack = create(this, DefinitionIdTypes.PointBack)
        val PointDown = create(this, DefinitionIdTypes.PointDown)
        val PointForward = create(this, DefinitionIdTypes.PointForward)
        val PointLeft = create(this, DefinitionIdTypes.PointLeft)
        val PointRight = create(this, DefinitionIdTypes.PointRight)
        val Cold = create(this, DefinitionIdTypes.Cold)
        val CheckWrist = create(this, DefinitionIdTypes.CheckWrist)
        val FYou = create(this, DefinitionIdTypes.FYou)
        val Drunk = create(this, DefinitionIdTypes.Drunk)
        val RPS_Paper = create(this, DefinitionIdTypes.RPS_Paper)
        val RPS_Rock = create(this, DefinitionIdTypes.RPS_Rock)
        val RPS_Scissor = create(this, DefinitionIdTypes.RPS_Scissor)
        val Salute = create(this, DefinitionIdTypes.Salute)
    }

    object AnimalBot : SafeDefinitionIdId() {
        val SpaceSpider = create(this, DefinitionIdTypes.SpaceSpider)
        val SpaceSpiderBlack = create(this, DefinitionIdTypes.SpaceSpiderBlack)
        val SpaceSpiderBrown = create(this, DefinitionIdTypes.SpaceSpiderBrown)
        val SpaceSpiderGreen = create(this, DefinitionIdTypes.SpaceSpiderGreen)
        val Wolf = create(this, DefinitionIdTypes.Wolf)
    }

    object Character : SafeDefinitionIdId() {
        val Default_Astronaut = create(this, DefinitionIdTypes.Default_Astronaut)
        val Default_Astronaut_Female = create(this, DefinitionIdTypes.Default_Astronaut_Female)
        val Space_spider_black = create(this, DefinitionIdTypes.Space_spider_black)
        val Space_spider_brown = create(this, DefinitionIdTypes.Space_spider_brown)
        val Space_spider_green = create(this, DefinitionIdTypes.Space_spider_green)
        val Space_spider = create(this, DefinitionIdTypes.Space_spider)
        val Space_Wolf = create(this, DefinitionIdTypes.Space_Wolf)
    }

    object Component : SafeDefinitionIdId() {
        val Construction = create(this, DefinitionIdTypes.Construction)
        val MetalGrid = create(this, DefinitionIdTypes.MetalGrid)
        val InteriorPlate = create(this, DefinitionIdTypes.InteriorPlate)
        val SteelPlate = create(this, DefinitionIdTypes.SteelPlate)
        val Girder = create(this, DefinitionIdTypes.Girder)
        val SmallTube = create(this, DefinitionIdTypes.SmallTube)
        val LargeTube = create(this, DefinitionIdTypes.LargeTube)
        val Motor = create(this, DefinitionIdTypes.Motor)
        val Display = create(this, DefinitionIdTypes.Display)
        val BulletproofGlass = create(this, DefinitionIdTypes.BulletproofGlass)
        val Superconductor = create(this, DefinitionIdTypes.Superconductor)
        val Computer = create(this, DefinitionIdTypes.Computer)
        val Reactor = create(this, DefinitionIdTypes.Reactor)
        val Thrust = create(this, DefinitionIdTypes.Thrust)
        val GravityGenerator = create(this, DefinitionIdTypes.GravityGenerator)
        val Medical = create(this, DefinitionIdTypes.Medical)
        val RadioCommunication = create(this, DefinitionIdTypes.RadioCommunication)
        val Detector = create(this, DefinitionIdTypes.Detector)
        val Explosives = create(this, DefinitionIdTypes.Explosives)
        val SolarCell = create(this, DefinitionIdTypes.SolarCell)
        val PowerCell = create(this, DefinitionIdTypes.PowerCell)
        val Canvas = create(this, DefinitionIdTypes.Canvas)
        val EngineerPlushie = create(this, DefinitionIdTypes.EngineerPlushie)
        val ZoneChip = create(this, DefinitionIdTypes.ZoneChip)
    }

    object CurveDefinition : SafeDefinitionIdId() {
        val FadeOutLin = create(this, DefinitionIdTypes.FadeOutLin)
        val FadeInLin = create(this, DefinitionIdTypes.FadeInLin)
        val FadeIn = create(this, DefinitionIdTypes.FadeIn)
        val FadeOut = create(this, DefinitionIdTypes.FadeOut)
    }

    object DebrisDefinition : SafeDefinitionIdId() {
        val Debris01 = create(this, DefinitionIdTypes.Debris01)
        val Debris04 = create(this, DefinitionIdTypes.Debris04)
        val Debris05 = create(this, DefinitionIdTypes.Debris05)
        val Debris06 = create(this, DefinitionIdTypes.Debris06)
        val Debris07 = create(this, DefinitionIdTypes.Debris07)
        val Debris08 = create(this, DefinitionIdTypes.Debris08)
        val Debris09 = create(this, DefinitionIdTypes.Debris09)
        val Debris10 = create(this, DefinitionIdTypes.Debris10)
        val Debris12 = create(this, DefinitionIdTypes.Debris12)
        val Debris13 = create(this, DefinitionIdTypes.Debris13)
        val Debris17 = create(this, DefinitionIdTypes.Debris17)
        val Debris21 = create(this, DefinitionIdTypes.Debris21)
        val Debris25 = create(this, DefinitionIdTypes.Debris25)
        val Debris28 = create(this, DefinitionIdTypes.Debris28)
        val Debris31 = create(this, DefinitionIdTypes.Debris31)
        val VoxelDebris08 = create(this, DefinitionIdTypes.VoxelDebris08)
        val VoxelDebris07 = create(this, DefinitionIdTypes.VoxelDebris07)
        val VoxelDebris06 = create(this, DefinitionIdTypes.VoxelDebris06)
        val VoxelDebris05 = create(this, DefinitionIdTypes.VoxelDebris05)
        val VoxelDebris04 = create(this, DefinitionIdTypes.VoxelDebris04)
        val VoxelDebris03 = create(this, DefinitionIdTypes.VoxelDebris03)
        val VoxelDebris02 = create(this, DefinitionIdTypes.VoxelDebris02)
        val VoxelDebris01 = create(this, DefinitionIdTypes.VoxelDebris01)
    }

    object EdgesDefinition : SafeDefinitionIdId() {
        val Light = create(this, DefinitionIdTypes.Light)
        val Heavy = create(this, DefinitionIdTypes.Heavy)
    }

    object FactionDefinition : SafeDefinitionIdId() {
        val Spiders = create(this, DefinitionIdTypes.Spiders)
        val SpacePirates = create(this, DefinitionIdTypes.SpacePirates)
        val FirstColonist = create(this, DefinitionIdTypes.FirstColonist)
    }

    object FlareDefinition : SafeDefinitionIdId() {
        val Sun = create(this, DefinitionIdTypes.Sun)
        val BeaconSmall = create(this, DefinitionIdTypes.BeaconSmall)
        val BeaconLarge = create(this, DefinitionIdTypes.BeaconLarge)
        val SpotlightLarge = create(this, DefinitionIdTypes.SpotlightLarge)
        val SpotlightSmall = create(this, DefinitionIdTypes.SpotlightSmall)
        val InteriorLight = create(this, DefinitionIdTypes.InteriorLight)
        val InteriorLightSmall = create(this, DefinitionIdTypes.InteriorLightSmall)
        val NoFlare = create(this, DefinitionIdTypes.NoFlare)
        val SmallGridSmallThruster = create(this, DefinitionIdTypes.SmallGridSmallThruster)
        val SmallGridLargeThruster = create(this, DefinitionIdTypes.SmallGridLargeThruster)
        val LargeGridSmallThruster = create(this, DefinitionIdTypes.LargeGridSmallThruster)
        val LargeGridLargeThruster = create(this, DefinitionIdTypes.LargeGridLargeThruster)
        val Jetpack = create(this, DefinitionIdTypes.Jetpack)
        val Welder = create(this, DefinitionIdTypes.Welder)
        val ShipWelder = create(this, DefinitionIdTypes.ShipWelder)
        val ShipWelderLarge = create(this, DefinitionIdTypes.ShipWelderLarge)
        val Grinder = create(this, DefinitionIdTypes.Grinder)
        val ShipGrinder = create(this, DefinitionIdTypes.ShipGrinder)
        val Headlamp = create(this, DefinitionIdTypes.Headlamp)
    }

    object GasProperties : SafeDefinitionIdId() {
        val Oxygen = create(this, DefinitionIdTypes.Oxygen)
        val Hydrogen = create(this, DefinitionIdTypes.Hydrogen)
    }

    object GlobalEventBase : SafeDefinitionIdId() {
        val SpawnCargoShip = create(this, DefinitionIdTypes.SpawnCargoShip)
        val April2014 = create(this, DefinitionIdTypes.April2014)
        val MeteorWave = create(this, DefinitionIdTypes.MeteorWave)
        val MeteorWaveCataclysm = create(this, DefinitionIdTypes.MeteorWaveCataclysm)
        val MeteorWaveCataclysmUnreal = create(this, DefinitionIdTypes.MeteorWaveCataclysmUnreal)
    }

    object PhysicalGunObject : SafeDefinitionIdId() {
        val GoodAIRewardPunishmentTool = create(this, DefinitionIdTypes.GoodAIRewardPunishmentTool)
        val SemiAutoPistolItem = create(this, DefinitionIdTypes.SemiAutoPistolItem)
        val FullAutoPistolItem = create(this, DefinitionIdTypes.FullAutoPistolItem)
        val ElitePistolItem = create(this, DefinitionIdTypes.ElitePistolItem)
        val AutomaticRifleItem = create(this, DefinitionIdTypes.AutomaticRifleItem)
        val PreciseAutomaticRifleItem = create(this, DefinitionIdTypes.PreciseAutomaticRifleItem)
        val RapidFireAutomaticRifleItem = create(this, DefinitionIdTypes.RapidFireAutomaticRifleItem)
        val UltimateAutomaticRifleItem = create(this, DefinitionIdTypes.UltimateAutomaticRifleItem)
        val BasicHandHeldLauncherItem = create(this, DefinitionIdTypes.BasicHandHeldLauncherItem)
        val AdvancedHandHeldLauncherItem = create(this, DefinitionIdTypes.AdvancedHandHeldLauncherItem)
        val WelderItem = create(this, DefinitionIdTypes.WelderItem)
        val Welder2Item = create(this, DefinitionIdTypes.Welder2Item)
        val Welder3Item = create(this, DefinitionIdTypes.Welder3Item)
        val Welder4Item = create(this, DefinitionIdTypes.Welder4Item)
        val AngleGrinderItem = create(this, DefinitionIdTypes.AngleGrinderItem)
        val AngleGrinder2Item = create(this, DefinitionIdTypes.AngleGrinder2Item)
        val AngleGrinder3Item = create(this, DefinitionIdTypes.AngleGrinder3Item)
        val AngleGrinder4Item = create(this, DefinitionIdTypes.AngleGrinder4Item)
        val HandDrillItem = create(this, DefinitionIdTypes.HandDrillItem)
        val HandDrill2Item = create(this, DefinitionIdTypes.HandDrill2Item)
        val HandDrill3Item = create(this, DefinitionIdTypes.HandDrill3Item)
        val HandDrill4Item = create(this, DefinitionIdTypes.HandDrill4Item)
        val CubePlacerItem = create(this, DefinitionIdTypes.CubePlacerItem)
    }

    object Ore : SafeDefinitionIdId() {
        val Stone = create(this, DefinitionIdTypes.Stone)
        val Iron = create(this, DefinitionIdTypes.Iron)
        val Nickel = create(this, DefinitionIdTypes.Nickel)
        val Cobalt = create(this, DefinitionIdTypes.Cobalt)
        val Magnesium = create(this, DefinitionIdTypes.Magnesium)
        val Silicon = create(this, DefinitionIdTypes.Silicon)
        val Silver = create(this, DefinitionIdTypes.Silver)
        val Gold = create(this, DefinitionIdTypes.Gold)
        val Platinum = create(this, DefinitionIdTypes.Platinum)
        val Uranium = create(this, DefinitionIdTypes.Uranium)
        val Scrap = create(this, DefinitionIdTypes.Scrap)
        val Ice = create(this, DefinitionIdTypes.Ice)
        val Organic = create(this, DefinitionIdTypes.Organic)
    }

    object Ingot : SafeDefinitionIdId() {
        val Stone = create(this, DefinitionIdTypes.Stone)
        val Iron = create(this, DefinitionIdTypes.Iron)
        val Nickel = create(this, DefinitionIdTypes.Nickel)
        val Cobalt = create(this, DefinitionIdTypes.Cobalt)
        val Magnesium = create(this, DefinitionIdTypes.Magnesium)
        val Silicon = create(this, DefinitionIdTypes.Silicon)
        val Silver = create(this, DefinitionIdTypes.Silver)
        val Gold = create(this, DefinitionIdTypes.Gold)
        val Platinum = create(this, DefinitionIdTypes.Platinum)
        val Uranium = create(this, DefinitionIdTypes.Uranium)
        val Scrap = create(this, DefinitionIdTypes.Scrap)
    }

    object OxygenContainerObject : SafeDefinitionIdId() {
        val OxygenBottle = create(this, DefinitionIdTypes.OxygenBottle)
    }

    object GasContainerObject : SafeDefinitionIdId() {
        val HydrogenBottle = create(this, DefinitionIdTypes.HydrogenBottle)
    }

    object TreeObject : SafeDefinitionIdId() {
        val DesertTree = create(this, DefinitionIdTypes.DesertTree)
        val DesertTreeDead = create(this, DefinitionIdTypes.DesertTreeDead)
        val LeafTree = create(this, DefinitionIdTypes.LeafTree)
        val PineTree = create(this, DefinitionIdTypes.PineTree)
        val PineTreeSnow = create(this, DefinitionIdTypes.PineTreeSnow)
        val LeafTreeMedium = create(this, DefinitionIdTypes.LeafTreeMedium)
        val DesertTreeMedium = create(this, DefinitionIdTypes.DesertTreeMedium)
        val DesertTreeDeadMedium = create(this, DefinitionIdTypes.DesertTreeDeadMedium)
        val PineTreeMedium = create(this, DefinitionIdTypes.PineTreeMedium)
        val PineTreeSnowMedium = create(this, DefinitionIdTypes.PineTreeSnowMedium)
        val DeadBushMedium = create(this, DefinitionIdTypes.DeadBushMedium)
        val DesertBushMedium = create(this, DefinitionIdTypes.DesertBushMedium)
        val LeafBushMedium_var1 = create(this, DefinitionIdTypes.LeafBushMedium_var1)
        val LeafBushMedium_var2 = create(this, DefinitionIdTypes.LeafBushMedium_var2)
        val PineBushMedium = create(this, DefinitionIdTypes.PineBushMedium)
        val SnowPineBushMedium = create(this, DefinitionIdTypes.SnowPineBushMedium)
    }

    object ConsumableItem : SafeDefinitionIdId() {
        val ClangCola = create(this, DefinitionIdTypes.ClangCola)
        val CosmicCoffee = create(this, DefinitionIdTypes.CosmicCoffee)
        val Medkit = create(this, DefinitionIdTypes.Medkit)
        val Powerkit = create(this, DefinitionIdTypes.Powerkit)
    }

    object Datapad : SafeDefinitionIdId() {
        val Datapad = create(this, DefinitionIdTypes.Datapad)
    }

    object Package : SafeDefinitionIdId() {
        val Package = create(this, DefinitionIdTypes.Package)
    }

    object PhysicalObject : SafeDefinitionIdId() {
        val SpaceCredit = create(this, DefinitionIdTypes.SpaceCredit)
    }

    object PhysicalMaterialDefinition : SafeDefinitionIdId() {
        val Stone = create(this, DefinitionIdTypes.Stone)
        val Wood = create(this, DefinitionIdTypes.Wood)
        val Wheel = create(this, DefinitionIdTypes.Wheel)
        val Rock = create(this, DefinitionIdTypes.Rock)
        val Metal = create(this, DefinitionIdTypes.Metal)
        val Glass = create(this, DefinitionIdTypes.Glass)
        val GlassOpaque = create(this, DefinitionIdTypes.GlassOpaque)
        val Ammo = create(this, DefinitionIdTypes.Ammo)
        val Character = create(this, DefinitionIdTypes.Character)
        val CharacterFemale = create(this, DefinitionIdTypes.CharacterFemale)
        val Spider = create(this, DefinitionIdTypes.Spider)
        val Wolf = create(this, DefinitionIdTypes.Wolf)
        val RifleBullet = create(this, DefinitionIdTypes.RifleBullet)
        val Grinder = create(this, DefinitionIdTypes.Grinder)
        val HandDrill = create(this, DefinitionIdTypes.HandDrill)
        val ShipDrill = create(this, DefinitionIdTypes.ShipDrill)
        val GunBullet = create(this, DefinitionIdTypes.GunBullet)
        val Missile = create(this, DefinitionIdTypes.Missile)
        val LargeShell = create(this, DefinitionIdTypes.LargeShell)
        val MediumShell = create(this, DefinitionIdTypes.MediumShell)
        val ExpBullet = create(this, DefinitionIdTypes.ExpBullet)
        val Tree = create(this, DefinitionIdTypes.Tree)
        val GunAutocannonBullet = create(this, DefinitionIdTypes.GunAutocannonBullet)
    }

    object ResourceDistributionGroup : SafeDefinitionIdId() {
        val Defense = create(this, DefinitionIdTypes.Defense)
        val DefenseAdative = create(this, DefinitionIdTypes.DefenseAdative)
        val Conveyors = create(this, DefinitionIdTypes.Conveyors)
        val Factory = create(this, DefinitionIdTypes.Factory)
        val Doors = create(this, DefinitionIdTypes.Doors)
        val Utility = create(this, DefinitionIdTypes.Utility)
        val Charging = create(this, DefinitionIdTypes.Charging)
        val Gyro = create(this, DefinitionIdTypes.Gyro)
        val Generators = create(this, DefinitionIdTypes.Generators)
        val Thrust = create(this, DefinitionIdTypes.Thrust)
        val BatteryBlock = create(this, DefinitionIdTypes.BatteryBlock)
        val SolarPanels = create(this, DefinitionIdTypes.SolarPanels)
        val Battery = create(this, DefinitionIdTypes.Battery)
        val Reactors = create(this, DefinitionIdTypes.Reactors)
    }

    object SoundCategoryDefinition : SafeDefinitionIdId() {
        val DefaultCategory = create(this, DefinitionIdTypes.DefaultCategory)
        val MusicCategory = create(this, DefinitionIdTypes.MusicCategory)
    }

    object EntityStat : SafeDefinitionIdId() {
        val Health = create(this, DefinitionIdTypes.Health)
        val SpiderHealth = create(this, DefinitionIdTypes.SpiderHealth)
        val SpaceCharacterHealth = create(this, DefinitionIdTypes.SpaceCharacterHealth)
        val WolfHealth = create(this, DefinitionIdTypes.WolfHealth)
    }

    object VoxelHandDefinition : SafeDefinitionIdId() {
        val Box = create(this, DefinitionIdTypes.Box)
        val Capsule = create(this, DefinitionIdTypes.Capsule)
        val Ramp = create(this, DefinitionIdTypes.Ramp)
        val Sphere = create(this, DefinitionIdTypes.Sphere)
        val AutoLevel = create(this, DefinitionIdTypes.AutoLevel)
    }

    object TransparentMaterialDefinition : SafeDefinitionIdId() {
        val DefaultOffscreenTarget = create(this, DefinitionIdTypes.DefaultOffscreenTarget)
        val SecondOffscreenTarget = create(this, DefinitionIdTypes.SecondOffscreenTarget)
        val Square = create(this, DefinitionIdTypes.Square)
        val SquareIgnoreDepth = create(this, DefinitionIdTypes.SquareIgnoreDepth)
        val ContainerBorder = create(this, DefinitionIdTypes.ContainerBorder)
        val ContainerBorderSelected = create(this, DefinitionIdTypes.ContainerBorderSelected)
        val DecalGlare = create(this, DefinitionIdTypes.DecalGlare)
        val Explosion = create(this, DefinitionIdTypes.Explosion)
        val Explosion_line = create(this, DefinitionIdTypes.Explosion_line)
        val Explosion_pieces = create(this, DefinitionIdTypes.Explosion_pieces)
        val ExplosionSmokeDebrisLine = create(this, DefinitionIdTypes.ExplosionSmokeDebrisLine)
        val WarpBubble = create(this, DefinitionIdTypes.WarpBubble)
        val GlareHeadlight = create(this, DefinitionIdTypes.GlareHeadlight)
        val GlareLsInteriorLight = create(this, DefinitionIdTypes.GlareLsInteriorLight)
        val GlareLsThrustLarge = create(this, DefinitionIdTypes.GlareLsThrustLarge)
        val GlareLsThrustSmall = create(this, DefinitionIdTypes.GlareLsThrustSmall)
        val GlareLsLight = create(this, DefinitionIdTypes.GlareLsLight)
        val GlareSsLight = create(this, DefinitionIdTypes.GlareSsLight)
        val GlareSsThrustLarge = create(this, DefinitionIdTypes.GlareSsThrustLarge)
        val GlareWelder = create(this, DefinitionIdTypes.GlareWelder)
        val GunSmoke = create(this, DefinitionIdTypes.GunSmoke)
        val IlluminatingShell = create(this, DefinitionIdTypes.IlluminatingShell)
        val LightGlare = create(this, DefinitionIdTypes.LightGlare)
        val LightGlareDistant = create(this, DefinitionIdTypes.LightGlareDistant)
        val LightGlare_b = create(this, DefinitionIdTypes.LightGlare_b)
        val LightGlare_WithDepth = create(this, DefinitionIdTypes.LightGlare_WithDepth)
        val MissileGlare = create(this, DefinitionIdTypes.MissileGlare)
        val MuzzleFlashMachineGunFront = create(this, DefinitionIdTypes.MuzzleFlashMachineGunFront)
        val MuzzleFlashMachineGunSide = create(this, DefinitionIdTypes.MuzzleFlashMachineGunSide)
        val particle_flash_a = create(this, DefinitionIdTypes.particle_flash_a)
        val particle_flash_b = create(this, DefinitionIdTypes.particle_flash_b)
        val particle_flash_c = create(this, DefinitionIdTypes.particle_flash_c)
        val particle_glare = create(this, DefinitionIdTypes.particle_glare)
        val particle_laser = create(this, DefinitionIdTypes.particle_laser)
        val particle_nuclear = create(this, DefinitionIdTypes.particle_nuclear)
        val particle_stone = create(this, DefinitionIdTypes.particle_stone)
        val particle_trash_a = create(this, DefinitionIdTypes.particle_trash_a)
        val particle_trash_b = create(this, DefinitionIdTypes.particle_trash_b)
        val ProjectileTrailLine = create(this, DefinitionIdTypes.ProjectileTrailLine)
        val DisconnectedPlayerIcon = create(this, DefinitionIdTypes.DisconnectedPlayerIcon)
        val PlayerIndicator_NeutralFriendly = create(this, DefinitionIdTypes.PlayerIndicator_NeutralFriendly)
        val PlayerIndicator_Enemy = create(this, DefinitionIdTypes.PlayerIndicator_Enemy)
        val GizmoDrawLine = create(this, DefinitionIdTypes.GizmoDrawLine)
        val GizmoDrawLineRed = create(this, DefinitionIdTypes.GizmoDrawLineRed)
        val GizmoDrawLineWhite = create(this, DefinitionIdTypes.GizmoDrawLineWhite)
        val ReflectorCone = create(this, DefinitionIdTypes.ReflectorCone)
        val ReflectorConeCharacter = create(this, DefinitionIdTypes.ReflectorConeCharacter)
        val ReflectorConeNarrow = create(this, DefinitionIdTypes.ReflectorConeNarrow)
        val ReflectorGlareAlphaBlended = create(this, DefinitionIdTypes.ReflectorGlareAlphaBlended)
        val ShotgunParticle = create(this, DefinitionIdTypes.ShotgunParticle)
        val Smoke = create(this, DefinitionIdTypes.Smoke)
        val Smoke_b = create(this, DefinitionIdTypes.Smoke_b)
        val Smoke_c = create(this, DefinitionIdTypes.Smoke_c)
        val smoke_field = create(this, DefinitionIdTypes.smoke_field)
        val smoke_field2 = create(this, DefinitionIdTypes.smoke_field2)
        val Smoke_Ignore_Depth = create(this, DefinitionIdTypes.Smoke_Ignore_Depth)
        val Smoke_lit = create(this, DefinitionIdTypes.Smoke_lit)
        val Smoke_square = create(this, DefinitionIdTypes.Smoke_square)
        val Smoke_square_unlit = create(this, DefinitionIdTypes.Smoke_square_unlit)
        val Sparks_a = create(this, DefinitionIdTypes.Sparks_a)
        val Sparks_b = create(this, DefinitionIdTypes.Sparks_b)
        val Stardust = create(this, DefinitionIdTypes.Stardust)
        val Firefly = create(this, DefinitionIdTypes.Firefly)
        val Dandelion01 = create(this, DefinitionIdTypes.Dandelion01)
        val SunDisk = create(this, DefinitionIdTypes.SunDisk)
        val Test = create(this, DefinitionIdTypes.Test)
        val Arrow = create(this, DefinitionIdTypes.Arrow)
        val ArrowLeft = create(this, DefinitionIdTypes.ArrowLeft)
        val ArrowRight = create(this, DefinitionIdTypes.ArrowRight)
        val SquareFullColor = create(this, DefinitionIdTypes.SquareFullColor)
        val ArrowBlue = create(this, DefinitionIdTypes.ArrowBlue)
        val ArrowRed = create(this, DefinitionIdTypes.ArrowRed)
        val ArrowGreen = create(this, DefinitionIdTypes.ArrowGreen)
        val ArrowLeftBlue = create(this, DefinitionIdTypes.ArrowLeftBlue)
        val ArrowLeftGreen = create(this, DefinitionIdTypes.ArrowLeftGreen)
        val ArrowLeftRed = create(this, DefinitionIdTypes.ArrowLeftRed)
        val ArrowRightBlue = create(this, DefinitionIdTypes.ArrowRightBlue)
        val ArrowRightGreen = create(this, DefinitionIdTypes.ArrowRightGreen)
        val ArrowRightRed = create(this, DefinitionIdTypes.ArrowRightRed)
        val RedDot = create(this, DefinitionIdTypes.RedDot)
        val RedDotIgnoreDepth = create(this, DefinitionIdTypes.RedDotIgnoreDepth)
        val FrostedGlass = create(this, DefinitionIdTypes.FrostedGlass)
        val FrostedGlassLarger = create(this, DefinitionIdTypes.FrostedGlassLarger)
        val AsteroidParticle = create(this, DefinitionIdTypes.AsteroidParticle)
        val WeaponLaser = create(this, DefinitionIdTypes.WeaponLaser)
        val WeaponLaserIgnoreDepth = create(this, DefinitionIdTypes.WeaponLaserIgnoreDepth)
        val Blood = create(this, DefinitionIdTypes.Blood)
        val Blood5 = create(this, DefinitionIdTypes.Blood5)
        val Blood2 = create(this, DefinitionIdTypes.Blood2)
        val Blood3 = create(this, DefinitionIdTypes.Blood3)
        val Blood4 = create(this, DefinitionIdTypes.Blood4)
        val Spark1 = create(this, DefinitionIdTypes.Spark1)
        val Spark2 = create(this, DefinitionIdTypes.Spark2)
        val Smoke_cutout = create(this, DefinitionIdTypes.Smoke_cutout)
        val WhiteDot = create(this, DefinitionIdTypes.WhiteDot)
        val WhiteSpark = create(this, DefinitionIdTypes.WhiteSpark)
        val Fire = create(this, DefinitionIdTypes.Fire)
        val Blood_Sprite_A = create(this, DefinitionIdTypes.Blood_Sprite_A)
        val Volumetric_Blood_01 = create(this, DefinitionIdTypes.Volumetric_Blood_01)
        val Volumetric_Blood_02 = create(this, DefinitionIdTypes.Volumetric_Blood_02)
        val Volumetric_Blood_03 = create(this, DefinitionIdTypes.Volumetric_Blood_03)
        val Volumetric_Blood_04 = create(this, DefinitionIdTypes.Volumetric_Blood_04)
        val Volumetric_Blood_05 = create(this, DefinitionIdTypes.Volumetric_Blood_05)
        val Volumetric_Blood_06 = create(this, DefinitionIdTypes.Volumetric_Blood_06)
        val Volumetric_Blood_07 = create(this, DefinitionIdTypes.Volumetric_Blood_07)
        val Volumetric_Blood_08 = create(this, DefinitionIdTypes.Volumetric_Blood_08)
        val Volumetric_Blood_09 = create(this, DefinitionIdTypes.Volumetric_Blood_09)
        val Volumetric_Blood_10 = create(this, DefinitionIdTypes.Volumetric_Blood_10)
        val Volumetric_Blood_11 = create(this, DefinitionIdTypes.Volumetric_Blood_11)
        val Volumetric_Blood_12 = create(this, DefinitionIdTypes.Volumetric_Blood_12)
        val Volumetric_Blood_13 = create(this, DefinitionIdTypes.Volumetric_Blood_13)
        val Volumetric_Blood_14 = create(this, DefinitionIdTypes.Volumetric_Blood_14)
        val Explosion_Spritesheet_01 = create(this, DefinitionIdTypes.Explosion_Spritesheet_01)
        val Spark5 = create(this, DefinitionIdTypes.Spark5)
        val Spark6 = create(this, DefinitionIdTypes.Spark6)
        val Smoke_SpriteSheet_A_90f = create(this, DefinitionIdTypes.Smoke_SpriteSheet_A_90f)
        val Smoke_SpriteSheet_A_90f_Shrink = create(this, DefinitionIdTypes.Smoke_SpriteSheet_A_90f_Shrink)
        val Lightning_Spherical = create(this, DefinitionIdTypes.Lightning_Spherical)
        val Flame_Jet_A_Sprite = create(this, DefinitionIdTypes.Flame_Jet_A_Sprite)
        val Current_A_Sprite = create(this, DefinitionIdTypes.Current_A_Sprite)
        val Large_Explosion_Sprite_A = create(this, DefinitionIdTypes.Large_Explosion_Sprite_A)
        val Debris_Sprite_A = create(this, DefinitionIdTypes.Debris_Sprite_A)
        val Lens_Flare_A = create(this, DefinitionIdTypes.Lens_Flare_A)
        val Muzzle_Flash_A_Sprite = create(this, DefinitionIdTypes.Muzzle_Flash_A_Sprite)
        val Muzzle_Flash_B_Sprite = create(this, DefinitionIdTypes.Muzzle_Flash_B_Sprite)
        val Shell_Casings_A = create(this, DefinitionIdTypes.Shell_Casings_A)
        val Smoke_Trail = create(this, DefinitionIdTypes.Smoke_Trail)
        val Trail_Sprite_A = create(this, DefinitionIdTypes.Trail_Sprite_A)
        val particle_glare_alpha = create(this, DefinitionIdTypes.particle_glare_alpha)
        val WhiteBlock = create(this, DefinitionIdTypes.WhiteBlock)
        val Atlas_D_01 = create(this, DefinitionIdTypes.Atlas_D_01)
        val SafeZoneShield_Material = create(this, DefinitionIdTypes.SafeZoneShield_Material)
        val TransparentScreenArea = create(this, DefinitionIdTypes.TransparentScreenArea)
        val TransparentScreenArea90 = create(this, DefinitionIdTypes.TransparentScreenArea90)
        val TransparentScreenArea180 = create(this, DefinitionIdTypes.TransparentScreenArea180)
        val TransparentScreenArea270 = create(this, DefinitionIdTypes.TransparentScreenArea270)
        val TransparentScreenArea_Outside = create(this, DefinitionIdTypes.TransparentScreenArea_Outside)
        val Atlas_E_01 = create(this, DefinitionIdTypes.Atlas_E_01)
        val Atlas_F_01 = create(this, DefinitionIdTypes.Atlas_F_01)
        val NoGlareLight = create(this, DefinitionIdTypes.NoGlareLight)
        val EngineThrustMiddle = create(this, DefinitionIdTypes.EngineThrustMiddle)
        val EngineThrustSideway = create(this, DefinitionIdTypes.EngineThrustSideway)
        val SciFiEngineThrustMiddle = create(this, DefinitionIdTypes.SciFiEngineThrustMiddle)
        val ModularEngineThrustMiddle = create(this, DefinitionIdTypes.ModularEngineThrustMiddle)
        val JetpackThrustMiddle = create(this, DefinitionIdTypes.JetpackThrustMiddle)
        val JetpackThrustSideway = create(this, DefinitionIdTypes.JetpackThrustSideway)
        val GlareJetpack = create(this, DefinitionIdTypes.GlareJetpack)
        val GlareSsThrustSmall = create(this, DefinitionIdTypes.GlareSsThrustSmall)
        val SunBigGlow = create(this, DefinitionIdTypes.SunBigGlow)
        val SunBigRays = create(this, DefinitionIdTypes.SunBigRays)
        val SunGlare = create(this, DefinitionIdTypes.SunGlare)
        val SunFlareCircle = create(this, DefinitionIdTypes.SunFlareCircle)
        val SunFlareBlurred = create(this, DefinitionIdTypes.SunFlareBlurred)
        val SunFlareSquare = create(this, DefinitionIdTypes.SunFlareSquare)
        val SunFlareBlue = create(this, DefinitionIdTypes.SunFlareBlue)
        val SunFlareWhiteAnamorphic = create(this, DefinitionIdTypes.SunFlareWhiteAnamorphic)
        val AnamorphicFlare = create(this, DefinitionIdTypes.AnamorphicFlare)
        val HexagonRays = create(this, DefinitionIdTypes.HexagonRays)
        val SunFlareAnamorphic = create(this, DefinitionIdTypes.SunFlareAnamorphic)
        val GlassInside = create(this, DefinitionIdTypes.GlassInside)
        val SafeZoneShieldGlass = create(this, DefinitionIdTypes.SafeZoneShieldGlass)
        val GenericGlass = create(this, DefinitionIdTypes.GenericGlass)
        val RocketLauncher_Precision_Glass = create(this, DefinitionIdTypes.RocketLauncher_Precision_Glass)
        val RocketLauncher_Regular_Glass = create(this, DefinitionIdTypes.RocketLauncher_Regular_Glass)
        val WindowInside = create(this, DefinitionIdTypes.WindowInside)
        val GlassOutside = create(this, DefinitionIdTypes.GlassOutside)
        val WindowRectangularOutside = create(this, DefinitionIdTypes.WindowRectangularOutside)
        val WindowRectangularInside = create(this, DefinitionIdTypes.WindowRectangularInside)
        val WindowTriangularOutside = create(this, DefinitionIdTypes.WindowTriangularOutside)
        val WindowTriangularInside = create(this, DefinitionIdTypes.WindowTriangularInside)
        val Glass = create(this, DefinitionIdTypes.Glass)
        val CockpitGlassInside = create(this, DefinitionIdTypes.CockpitGlassInside)
        val CockpitGlassOutside = create(this, DefinitionIdTypes.CockpitGlassOutside)
        val CockpitFighterGlassInside = create(this, DefinitionIdTypes.CockpitFighterGlassInside)
        val CockpitFighterGlassOutside = create(this, DefinitionIdTypes.CockpitFighterGlassOutside)
        val CockpitIndustrialGlassOutside = create(this, DefinitionIdTypes.CockpitIndustrialGlassOutside)
        val CockpitIndustrialGlassInside = create(this, DefinitionIdTypes.CockpitIndustrialGlassInside)
        val CockpitGlass = create(this, DefinitionIdTypes.CockpitGlass)
    }

    object AudioEffectDefinition : SafeDefinitionIdId() {
        val CrossFadeLin = create(this, DefinitionIdTypes.CrossFadeLin)
        val FadeOutLin = create(this, DefinitionIdTypes.FadeOutLin)
        val FadeInLin = create(this, DefinitionIdTypes.FadeInLin)
        val CrossFade = create(this, DefinitionIdTypes.CrossFade)
        val FadeOut = create(this, DefinitionIdTypes.FadeOut)
        val FadeIn = create(this, DefinitionIdTypes.FadeIn)
        val LowPass = create(this, DefinitionIdTypes.LowPass)
        val LowPassHelmet = create(this, DefinitionIdTypes.LowPassHelmet)
        val LowPassCockpit = create(this, DefinitionIdTypes.LowPassCockpit)
        val LowPassCockpitNoOxy = create(this, DefinitionIdTypes.LowPassCockpitNoOxy)
        val LowPassNoHelmetNoOxy = create(this, DefinitionIdTypes.LowPassNoHelmetNoOxy)
        val realShipFilter = create(this, DefinitionIdTypes.realShipFilter)
    }

    object Trees : SafeDefinitionIdId() {
        val LeafTree = create(this, DefinitionIdTypes.LeafTree)
        val LeafTreeMedium = create(this, DefinitionIdTypes.LeafTreeMedium)
        val PineTree = create(this, DefinitionIdTypes.PineTree)
        val DesertTree = create(this, DefinitionIdTypes.DesertTree)
        val DesertTreeMedium = create(this, DefinitionIdTypes.DesertTreeMedium)
        val DesertTreeDead = create(this, DefinitionIdTypes.DesertTreeDead)
        val DesertTreeDeadMedium = create(this, DefinitionIdTypes.DesertTreeDeadMedium)
        val PineTreeSnow = create(this, DefinitionIdTypes.PineTreeSnow)
        val SnowForestEdge = create(this, DefinitionIdTypes.SnowForestEdge)
        val WoodGrassForestEdge = create(this, DefinitionIdTypes.WoodGrassForestEdge)
        val RockGrassForestEdge = create(this, DefinitionIdTypes.RockGrassForestEdge)
        val GrassForestEdge = create(this, DefinitionIdTypes.GrassForestEdge)
        val GrassOldForestEdge = create(this, DefinitionIdTypes.GrassOldForestEdge)
        val SandForestEdge = create(this, DefinitionIdTypes.SandForestEdge)
        val AlienSnowForestEdge = create(this, DefinitionIdTypes.AlienSnowForestEdge)
        val AlienOrangeGrassForestEdge = create(this, DefinitionIdTypes.AlienOrangeGrassForestEdge)
        val AlienYellowGrassForestEdge = create(this, DefinitionIdTypes.AlienYellowGrassForestEdge)
        val AlienGreenGrassForestEdge = create(this, DefinitionIdTypes.AlienGreenGrassForestEdge)
        val AlienRockGrassForestEdge = create(this, DefinitionIdTypes.AlienRockGrassForestEdge)
        val AlienSandForestEdge = create(this, DefinitionIdTypes.AlienSandForestEdge)
        val SnowForestMedium = create(this, DefinitionIdTypes.SnowForestMedium)
        val WoodGrassForestMedium = create(this, DefinitionIdTypes.WoodGrassForestMedium)
        val RockGrassForestMedium = create(this, DefinitionIdTypes.RockGrassForestMedium)
        val GrassForestMedium = create(this, DefinitionIdTypes.GrassForestMedium)
        val GrassOldForestMedium = create(this, DefinitionIdTypes.GrassOldForestMedium)
        val SandForestMedium = create(this, DefinitionIdTypes.SandForestMedium)
        val AlienSnowForestMedium = create(this, DefinitionIdTypes.AlienSnowForestMedium)
        val AlienOrangeGrassForestMedium = create(this, DefinitionIdTypes.AlienOrangeGrassForestMedium)
        val AlienYellowGrassForestMedium = create(this, DefinitionIdTypes.AlienYellowGrassForestMedium)
        val AlienGreenGrassForestMedium = create(this, DefinitionIdTypes.AlienGreenGrassForestMedium)
        val AlienRockGrassForestMedium = create(this, DefinitionIdTypes.AlienRockGrassForestMedium)
        val AlienSandForestMedium = create(this, DefinitionIdTypes.AlienSandForestMedium)
        val SnowForestLarge = create(this, DefinitionIdTypes.SnowForestLarge)
        val WoodGrassForestLarge = create(this, DefinitionIdTypes.WoodGrassForestLarge)
        val RockGrassForestLarge = create(this, DefinitionIdTypes.RockGrassForestLarge)
        val GrassForestLarge = create(this, DefinitionIdTypes.GrassForestLarge)
        val GrassOldForestLarge = create(this, DefinitionIdTypes.GrassOldForestLarge)
        val SandForestLarge = create(this, DefinitionIdTypes.SandForestLarge)
        val AlienSnowForestLarge = create(this, DefinitionIdTypes.AlienSnowForestLarge)
        val AlienOrangeGrassForestLarge = create(this, DefinitionIdTypes.AlienOrangeGrassForestLarge)
        val AlienYellowGrassForestLarge = create(this, DefinitionIdTypes.AlienYellowGrassForestLarge)
        val AlienGreenGrassForestLarge = create(this, DefinitionIdTypes.AlienGreenGrassForestLarge)
        val AlienRockGrassForestLarge = create(this, DefinitionIdTypes.AlienRockGrassForestLarge)
        val AlienSandForestLarge = create(this, DefinitionIdTypes.AlienSandForestLarge)
        val SnowTrees = create(this, DefinitionIdTypes.SnowTrees)
        val DesertTrees = create(this, DefinitionIdTypes.DesertTrees)
        val JungleTrees = create(this, DefinitionIdTypes.JungleTrees)
        val PertamSandFoliage = create(this, DefinitionIdTypes.PertamSandFoliage)
    }

    object DestroyableItems : SafeDefinitionIdId() {
        val DeadBushSmall = create(this, DefinitionIdTypes.DeadBushSmall)
        val PineBushSmall = create(this, DefinitionIdTypes.PineBushSmall)
        val SnowPineBushSmall = create(this, DefinitionIdTypes.SnowPineBushSmall)
        val SnowFoliage = create(this, DefinitionIdTypes.SnowFoliage)
        val WoodGrassFoliage = create(this, DefinitionIdTypes.WoodGrassFoliage)
        val RockGrassFoliage = create(this, DefinitionIdTypes.RockGrassFoliage)
        val GrassFoliage = create(this, DefinitionIdTypes.GrassFoliage)
        val GrassOldFoliage = create(this, DefinitionIdTypes.GrassOldFoliage)
        val SandFoliage = create(this, DefinitionIdTypes.SandFoliage)
        val AlienSnowFoliage = create(this, DefinitionIdTypes.AlienSnowFoliage)
        val AlienOrangeGrassFoliage = create(this, DefinitionIdTypes.AlienOrangeGrassFoliage)
        val AlienYellowGrassFoliage = create(this, DefinitionIdTypes.AlienYellowGrassFoliage)
        val AlienGreenGrassFoliage = create(this, DefinitionIdTypes.AlienGreenGrassFoliage)
        val AlienRockGrassFoliage = create(this, DefinitionIdTypes.AlienRockGrassFoliage)
        val AlienSandFoliage = create(this, DefinitionIdTypes.AlienSandFoliage)
        val SnowBushForestEdge = create(this, DefinitionIdTypes.SnowBushForestEdge)
        val WoodGrassBushForestEdge = create(this, DefinitionIdTypes.WoodGrassBushForestEdge)
        val RockGrassBushForestEdge = create(this, DefinitionIdTypes.RockGrassBushForestEdge)
        val GrassBushForestEdge = create(this, DefinitionIdTypes.GrassBushForestEdge)
        val GrassOldBushForestEdge = create(this, DefinitionIdTypes.GrassOldBushForestEdge)
        val SandBushForestEdge = create(this, DefinitionIdTypes.SandBushForestEdge)
        val AlienSnowBushForestEdge = create(this, DefinitionIdTypes.AlienSnowBushForestEdge)
        val AlienOrangeGrassBushForestEdge = create(this, DefinitionIdTypes.AlienOrangeGrassBushForestEdge)
        val AlienYellowGrassBushForestEdge = create(this, DefinitionIdTypes.AlienYellowGrassBushForestEdge)
        val AlienGreenGrassBushForestEdge = create(this, DefinitionIdTypes.AlienGreenGrassBushForestEdge)
        val AlienRockGrassBushForestEdge = create(this, DefinitionIdTypes.AlienRockGrassBushForestEdge)
        val AlienSandBushForestEdge = create(this, DefinitionIdTypes.AlienSandBushForestEdge)
        val SnowBushForestMedium = create(this, DefinitionIdTypes.SnowBushForestMedium)
        val WoodGrassBushForestMedium = create(this, DefinitionIdTypes.WoodGrassBushForestMedium)
        val RockGrassBushForestMedium = create(this, DefinitionIdTypes.RockGrassBushForestMedium)
        val GrassBushForestMedium = create(this, DefinitionIdTypes.GrassBushForestMedium)
        val GrassOldBushForestMedium = create(this, DefinitionIdTypes.GrassOldBushForestMedium)
        val SandBushForestMedium = create(this, DefinitionIdTypes.SandBushForestMedium)
        val AlienSnowBushForestMedium = create(this, DefinitionIdTypes.AlienSnowBushForestMedium)
        val AlienOrangeGrassBushForestMedium = create(this, DefinitionIdTypes.AlienOrangeGrassBushForestMedium)
        val AlienYellowGrassBushForestMedium = create(this, DefinitionIdTypes.AlienYellowGrassBushForestMedium)
        val AlienGreenGrassBushForestMedium = create(this, DefinitionIdTypes.AlienGreenGrassBushForestMedium)
        val AlienRockGrassBushForestMedium = create(this, DefinitionIdTypes.AlienRockGrassBushForestMedium)
        val AlienSandBushForestMedium = create(this, DefinitionIdTypes.AlienSandBushForestMedium)
        val SnowBushForestLarge = create(this, DefinitionIdTypes.SnowBushForestLarge)
        val WoodGrassBushForestLarge = create(this, DefinitionIdTypes.WoodGrassBushForestLarge)
        val RockGrassBushForestLarge = create(this, DefinitionIdTypes.RockGrassBushForestLarge)
        val GrassBushForestLarge = create(this, DefinitionIdTypes.GrassBushForestLarge)
        val GrassOldBushForestLarge = create(this, DefinitionIdTypes.GrassOldBushForestLarge)
        val SandBushForestLarge = create(this, DefinitionIdTypes.SandBushForestLarge)
        val AlienSnowBushForestLarge = create(this, DefinitionIdTypes.AlienSnowBushForestLarge)
        val AlienOrangeGrassBushForestLarge = create(this, DefinitionIdTypes.AlienOrangeGrassBushForestLarge)
        val AlienYellowGrassBushForestLarge = create(this, DefinitionIdTypes.AlienYellowGrassBushForestLarge)
        val AlienGreenGrassBushForestLarge = create(this, DefinitionIdTypes.AlienGreenGrassBushForestLarge)
        val AlienRockGrassBushForestLarge = create(this, DefinitionIdTypes.AlienRockGrassBushForestLarge)
        val AlienSandBushForestLarge = create(this, DefinitionIdTypes.AlienSandBushForestLarge)
        val BushesDesert = create(this, DefinitionIdTypes.BushesDesert)
        val SnowBushes = create(this, DefinitionIdTypes.SnowBushes)
        val BushesForest = create(this, DefinitionIdTypes.BushesForest)
        val PertamSandBushFoliage = create(this, DefinitionIdTypes.PertamSandBushFoliage)
        val PertamGrassOldBushFoliage = create(this, DefinitionIdTypes.PertamGrassOldBushFoliage)
    }

    object Tree : SafeDefinitionIdId() {
        val TreeMedium_4m = create(this, DefinitionIdTypes.TreeMedium_4m)
        val TreeMedium_6m = create(this, DefinitionIdTypes.TreeMedium_6m)
        val TreeMedium_8m = create(this, DefinitionIdTypes.TreeMedium_8m)
    }

    object CubeBlock : SafeDefinitionIdId() {
        val LargeRailStraight = create(this, DefinitionIdTypes.LargeRailStraight)
        val LargeBlockArmorBlock = create(this, DefinitionIdTypes.LargeBlockArmorBlock)
        val LargeBlockArmorSlope = create(this, DefinitionIdTypes.LargeBlockArmorSlope)
        val LargeBlockArmorCorner = create(this, DefinitionIdTypes.LargeBlockArmorCorner)
        val LargeBlockArmorCornerInv = create(this, DefinitionIdTypes.LargeBlockArmorCornerInv)
        val LargeRoundArmor_Slope = create(this, DefinitionIdTypes.LargeRoundArmor_Slope)
        val LargeRoundArmor_Corner = create(this, DefinitionIdTypes.LargeRoundArmor_Corner)
        val LargeRoundArmor_CornerInv = create(this, DefinitionIdTypes.LargeRoundArmor_CornerInv)
        val LargeHeavyBlockArmorBlock = create(this, DefinitionIdTypes.LargeHeavyBlockArmorBlock)
        val LargeHeavyBlockArmorSlope = create(this, DefinitionIdTypes.LargeHeavyBlockArmorSlope)
        val LargeHeavyBlockArmorCorner = create(this, DefinitionIdTypes.LargeHeavyBlockArmorCorner)
        val LargeHeavyBlockArmorCornerInv = create(this, DefinitionIdTypes.LargeHeavyBlockArmorCornerInv)
        val SmallBlockArmorBlock = create(this, DefinitionIdTypes.SmallBlockArmorBlock)
        val SmallBlockArmorSlope = create(this, DefinitionIdTypes.SmallBlockArmorSlope)
        val SmallBlockArmorCorner = create(this, DefinitionIdTypes.SmallBlockArmorCorner)
        val SmallBlockArmorCornerInv = create(this, DefinitionIdTypes.SmallBlockArmorCornerInv)
        val SmallHeavyBlockArmorBlock = create(this, DefinitionIdTypes.SmallHeavyBlockArmorBlock)
        val SmallHeavyBlockArmorSlope = create(this, DefinitionIdTypes.SmallHeavyBlockArmorSlope)
        val SmallHeavyBlockArmorCorner = create(this, DefinitionIdTypes.SmallHeavyBlockArmorCorner)
        val SmallHeavyBlockArmorCornerInv = create(this, DefinitionIdTypes.SmallHeavyBlockArmorCornerInv)
        val LargeHalfArmorBlock = create(this, DefinitionIdTypes.LargeHalfArmorBlock)
        val LargeHeavyHalfArmorBlock = create(this, DefinitionIdTypes.LargeHeavyHalfArmorBlock)
        val LargeHalfSlopeArmorBlock = create(this, DefinitionIdTypes.LargeHalfSlopeArmorBlock)
        val LargeHeavyHalfSlopeArmorBlock = create(this, DefinitionIdTypes.LargeHeavyHalfSlopeArmorBlock)
        val HalfArmorBlock = create(this, DefinitionIdTypes.HalfArmorBlock)
        val HeavyHalfArmorBlock = create(this, DefinitionIdTypes.HeavyHalfArmorBlock)
        val HalfSlopeArmorBlock = create(this, DefinitionIdTypes.HalfSlopeArmorBlock)
        val HeavyHalfSlopeArmorBlock = create(this, DefinitionIdTypes.HeavyHalfSlopeArmorBlock)
        val LargeBlockArmorRoundSlope = create(this, DefinitionIdTypes.LargeBlockArmorRoundSlope)
        val LargeBlockArmorRoundCorner = create(this, DefinitionIdTypes.LargeBlockArmorRoundCorner)
        val LargeBlockArmorRoundCornerInv = create(this, DefinitionIdTypes.LargeBlockArmorRoundCornerInv)
        val LargeHeavyBlockArmorRoundSlope = create(this, DefinitionIdTypes.LargeHeavyBlockArmorRoundSlope)
        val LargeHeavyBlockArmorRoundCorner = create(this, DefinitionIdTypes.LargeHeavyBlockArmorRoundCorner)
        val LargeHeavyBlockArmorRoundCornerInv = create(this, DefinitionIdTypes.LargeHeavyBlockArmorRoundCornerInv)
        val SmallBlockArmorRoundSlope = create(this, DefinitionIdTypes.SmallBlockArmorRoundSlope)
        val SmallBlockArmorRoundCorner = create(this, DefinitionIdTypes.SmallBlockArmorRoundCorner)
        val SmallBlockArmorRoundCornerInv = create(this, DefinitionIdTypes.SmallBlockArmorRoundCornerInv)
        val SmallHeavyBlockArmorRoundSlope = create(this, DefinitionIdTypes.SmallHeavyBlockArmorRoundSlope)
        val SmallHeavyBlockArmorRoundCorner = create(this, DefinitionIdTypes.SmallHeavyBlockArmorRoundCorner)
        val SmallHeavyBlockArmorRoundCornerInv = create(this, DefinitionIdTypes.SmallHeavyBlockArmorRoundCornerInv)
        val LargeBlockArmorSlope2Base = create(this, DefinitionIdTypes.LargeBlockArmorSlope2Base)
        val LargeBlockArmorSlope2Tip = create(this, DefinitionIdTypes.LargeBlockArmorSlope2Tip)
        val LargeBlockArmorCorner2Base = create(this, DefinitionIdTypes.LargeBlockArmorCorner2Base)
        val LargeBlockArmorCorner2Tip = create(this, DefinitionIdTypes.LargeBlockArmorCorner2Tip)
        val LargeBlockArmorInvCorner2Base = create(this, DefinitionIdTypes.LargeBlockArmorInvCorner2Base)
        val LargeBlockArmorInvCorner2Tip = create(this, DefinitionIdTypes.LargeBlockArmorInvCorner2Tip)
        val LargeHeavyBlockArmorSlope2Base = create(this, DefinitionIdTypes.LargeHeavyBlockArmorSlope2Base)
        val LargeHeavyBlockArmorSlope2Tip = create(this, DefinitionIdTypes.LargeHeavyBlockArmorSlope2Tip)
        val LargeHeavyBlockArmorCorner2Base = create(this, DefinitionIdTypes.LargeHeavyBlockArmorCorner2Base)
        val LargeHeavyBlockArmorCorner2Tip = create(this, DefinitionIdTypes.LargeHeavyBlockArmorCorner2Tip)
        val LargeHeavyBlockArmorInvCorner2Base = create(this, DefinitionIdTypes.LargeHeavyBlockArmorInvCorner2Base)
        val LargeHeavyBlockArmorInvCorner2Tip = create(this, DefinitionIdTypes.LargeHeavyBlockArmorInvCorner2Tip)
        val SmallBlockArmorSlope2Base = create(this, DefinitionIdTypes.SmallBlockArmorSlope2Base)
        val SmallBlockArmorSlope2Tip = create(this, DefinitionIdTypes.SmallBlockArmorSlope2Tip)
        val SmallBlockArmorCorner2Base = create(this, DefinitionIdTypes.SmallBlockArmorCorner2Base)
        val SmallBlockArmorCorner2Tip = create(this, DefinitionIdTypes.SmallBlockArmorCorner2Tip)
        val SmallBlockArmorInvCorner2Base = create(this, DefinitionIdTypes.SmallBlockArmorInvCorner2Base)
        val SmallBlockArmorInvCorner2Tip = create(this, DefinitionIdTypes.SmallBlockArmorInvCorner2Tip)
        val SmallHeavyBlockArmorSlope2Base = create(this, DefinitionIdTypes.SmallHeavyBlockArmorSlope2Base)
        val SmallHeavyBlockArmorSlope2Tip = create(this, DefinitionIdTypes.SmallHeavyBlockArmorSlope2Tip)
        val SmallHeavyBlockArmorCorner2Base = create(this, DefinitionIdTypes.SmallHeavyBlockArmorCorner2Base)
        val SmallHeavyBlockArmorCorner2Tip = create(this, DefinitionIdTypes.SmallHeavyBlockArmorCorner2Tip)
        val SmallHeavyBlockArmorInvCorner2Base = create(this, DefinitionIdTypes.SmallHeavyBlockArmorInvCorner2Base)
        val SmallHeavyBlockArmorInvCorner2Tip = create(this, DefinitionIdTypes.SmallHeavyBlockArmorInvCorner2Tip)
        val LargeBlockArmorCornerSquare = create(this, DefinitionIdTypes.LargeBlockArmorCornerSquare)
        val SmallBlockArmorCornerSquare = create(this, DefinitionIdTypes.SmallBlockArmorCornerSquare)
        val LargeBlockHeavyArmorCornerSquare = create(this, DefinitionIdTypes.LargeBlockHeavyArmorCornerSquare)
        val SmallBlockHeavyArmorCornerSquare = create(this, DefinitionIdTypes.SmallBlockHeavyArmorCornerSquare)
        val LargeBlockArmorCornerSquareInverted = create(this, DefinitionIdTypes.LargeBlockArmorCornerSquareInverted)
        val SmallBlockArmorCornerSquareInverted = create(this, DefinitionIdTypes.SmallBlockArmorCornerSquareInverted)
        val LargeBlockHeavyArmorCornerSquareInverted =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorCornerSquareInverted)
        val SmallBlockHeavyArmorCornerSquareInverted =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorCornerSquareInverted)
        val LargeBlockArmorHalfCorner = create(this, DefinitionIdTypes.LargeBlockArmorHalfCorner)
        val SmallBlockArmorHalfCorner = create(this, DefinitionIdTypes.SmallBlockArmorHalfCorner)
        val LargeBlockHeavyArmorHalfCorner = create(this, DefinitionIdTypes.LargeBlockHeavyArmorHalfCorner)
        val SmallBlockHeavyArmorHalfCorner = create(this, DefinitionIdTypes.SmallBlockHeavyArmorHalfCorner)
        val LargeBlockArmorHalfSlopeCorner = create(this, DefinitionIdTypes.LargeBlockArmorHalfSlopeCorner)
        val SmallBlockArmorHalfSlopeCorner = create(this, DefinitionIdTypes.SmallBlockArmorHalfSlopeCorner)
        val LargeBlockHeavyArmorHalfSlopeCorner = create(this, DefinitionIdTypes.LargeBlockHeavyArmorHalfSlopeCorner)
        val SmallBlockHeavyArmorHalfSlopeCorner = create(this, DefinitionIdTypes.SmallBlockHeavyArmorHalfSlopeCorner)
        val LargeBlockArmorHalfSlopeCornerInverted =
            create(this, DefinitionIdTypes.LargeBlockArmorHalfSlopeCornerInverted)
        val SmallBlockArmorHalfSlopeCornerInverted =
            create(this, DefinitionIdTypes.SmallBlockArmorHalfSlopeCornerInverted)
        val LargeBlockHeavyArmorHalfSlopeCornerInverted =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorHalfSlopeCornerInverted)
        val SmallBlockHeavyArmorHalfSlopeCornerInverted =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorHalfSlopeCornerInverted)
        val LargeBlockArmorHalfSlopedCorner = create(this, DefinitionIdTypes.LargeBlockArmorHalfSlopedCorner)
        val SmallBlockArmorHalfSlopedCorner = create(this, DefinitionIdTypes.SmallBlockArmorHalfSlopedCorner)
        val LargeBlockHeavyArmorHalfSlopedCorner = create(this, DefinitionIdTypes.LargeBlockHeavyArmorHalfSlopedCorner)
        val SmallBlockHeavyArmorHalfSlopedCorner = create(this, DefinitionIdTypes.SmallBlockHeavyArmorHalfSlopedCorner)
        val LargeBlockArmorHalfSlopedCornerBase = create(this, DefinitionIdTypes.LargeBlockArmorHalfSlopedCornerBase)
        val SmallBlockArmorHalfSlopedCornerBase = create(this, DefinitionIdTypes.SmallBlockArmorHalfSlopedCornerBase)
        val LargeBlockHeavyArmorHalfSlopedCornerBase =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorHalfSlopedCornerBase)
        val SmallBlockHeavyArmorHalfSlopedCornerBase =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorHalfSlopedCornerBase)
        val LargeBlockArmorHalfSlopeInverted = create(this, DefinitionIdTypes.LargeBlockArmorHalfSlopeInverted)
        val SmallBlockArmorHalfSlopeInverted = create(this, DefinitionIdTypes.SmallBlockArmorHalfSlopeInverted)
        val LargeBlockHeavyArmorHalfSlopeInverted =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorHalfSlopeInverted)
        val SmallBlockHeavyArmorHalfSlopeInverted =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorHalfSlopeInverted)
        val LargeBlockArmorSlopedCorner = create(this, DefinitionIdTypes.LargeBlockArmorSlopedCorner)
        val SmallBlockArmorSlopedCorner = create(this, DefinitionIdTypes.SmallBlockArmorSlopedCorner)
        val LargeBlockHeavyArmorSlopedCorner = create(this, DefinitionIdTypes.LargeBlockHeavyArmorSlopedCorner)
        val SmallBlockHeavyArmorSlopedCorner = create(this, DefinitionIdTypes.SmallBlockHeavyArmorSlopedCorner)
        val LargeBlockArmorSlopedCornerBase = create(this, DefinitionIdTypes.LargeBlockArmorSlopedCornerBase)
        val SmallBlockArmorSlopedCornerBase = create(this, DefinitionIdTypes.SmallBlockArmorSlopedCornerBase)
        val LargeBlockHeavyArmorSlopedCornerBase = create(this, DefinitionIdTypes.LargeBlockHeavyArmorSlopedCornerBase)
        val SmallBlockHeavyArmorSlopedCornerBase = create(this, DefinitionIdTypes.SmallBlockHeavyArmorSlopedCornerBase)
        val LargeBlockArmorSlopedCornerTip = create(this, DefinitionIdTypes.LargeBlockArmorSlopedCornerTip)
        val SmallBlockArmorSlopedCornerTip = create(this, DefinitionIdTypes.SmallBlockArmorSlopedCornerTip)
        val LargeBlockHeavyArmorSlopedCornerTip = create(this, DefinitionIdTypes.LargeBlockHeavyArmorSlopedCornerTip)
        val SmallBlockHeavyArmorSlopedCornerTip = create(this, DefinitionIdTypes.SmallBlockHeavyArmorSlopedCornerTip)
        val LargeBlockArmorRaisedSlopedCorner = create(this, DefinitionIdTypes.LargeBlockArmorRaisedSlopedCorner)
        val SmallBlockArmorRaisedSlopedCorner = create(this, DefinitionIdTypes.SmallBlockArmorRaisedSlopedCorner)
        val LargeBlockHeavyArmorRaisedSlopedCorner =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorRaisedSlopedCorner)
        val SmallBlockHeavyArmorRaisedSlopedCorner =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorRaisedSlopedCorner)
        val LargeBlockArmorSlopeTransition = create(this, DefinitionIdTypes.LargeBlockArmorSlopeTransition)
        val SmallBlockArmorSlopeTransition = create(this, DefinitionIdTypes.SmallBlockArmorSlopeTransition)
        val LargeBlockHeavyArmorSlopeTransition = create(this, DefinitionIdTypes.LargeBlockHeavyArmorSlopeTransition)
        val SmallBlockHeavyArmorSlopeTransition = create(this, DefinitionIdTypes.SmallBlockHeavyArmorSlopeTransition)
        val LargeBlockArmorSlopeTransitionBase = create(this, DefinitionIdTypes.LargeBlockArmorSlopeTransitionBase)
        val SmallBlockArmorSlopeTransitionBase = create(this, DefinitionIdTypes.SmallBlockArmorSlopeTransitionBase)
        val LargeBlockHeavyArmorSlopeTransitionBase =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorSlopeTransitionBase)
        val SmallBlockHeavyArmorSlopeTransitionBase =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorSlopeTransitionBase)
        val LargeBlockArmorSlopeTransitionBaseMirrored =
            create(this, DefinitionIdTypes.LargeBlockArmorSlopeTransitionBaseMirrored)
        val SmallBlockArmorSlopeTransitionBaseMirrored =
            create(this, DefinitionIdTypes.SmallBlockArmorSlopeTransitionBaseMirrored)
        val LargeBlockHeavyArmorSlopeTransitionBaseMirrored =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorSlopeTransitionBaseMirrored)
        val SmallBlockHeavyArmorSlopeTransitionBaseMirrored =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorSlopeTransitionBaseMirrored)
        val LargeBlockArmorSlopeTransitionMirrored =
            create(this, DefinitionIdTypes.LargeBlockArmorSlopeTransitionMirrored)
        val SmallBlockArmorSlopeTransitionMirrored =
            create(this, DefinitionIdTypes.SmallBlockArmorSlopeTransitionMirrored)
        val LargeBlockHeavyArmorSlopeTransitionMirrored =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorSlopeTransitionMirrored)
        val SmallBlockHeavyArmorSlopeTransitionMirrored =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorSlopeTransitionMirrored)
        val LargeBlockArmorSlopeTransitionTip = create(this, DefinitionIdTypes.LargeBlockArmorSlopeTransitionTip)
        val SmallBlockArmorSlopeTransitionTip = create(this, DefinitionIdTypes.SmallBlockArmorSlopeTransitionTip)
        val LargeBlockHeavyArmorSlopeTransitionTip =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorSlopeTransitionTip)
        val SmallBlockHeavyArmorSlopeTransitionTip =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorSlopeTransitionTip)
        val LargeBlockArmorSlopeTransitionTipMirrored =
            create(this, DefinitionIdTypes.LargeBlockArmorSlopeTransitionTipMirrored)
        val SmallBlockArmorSlopeTransitionTipMirrored =
            create(this, DefinitionIdTypes.SmallBlockArmorSlopeTransitionTipMirrored)
        val LargeBlockHeavyArmorSlopeTransitionTipMirrored =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorSlopeTransitionTipMirrored)
        val SmallBlockHeavyArmorSlopeTransitionTipMirrored =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorSlopeTransitionTipMirrored)
        val LargeBlockArmorSquareSlopedCornerBase =
            create(this, DefinitionIdTypes.LargeBlockArmorSquareSlopedCornerBase)
        val SmallBlockArmorSquareSlopedCornerBase =
            create(this, DefinitionIdTypes.SmallBlockArmorSquareSlopedCornerBase)
        val LargeBlockHeavyArmorSquareSlopedCornerBase =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorSquareSlopedCornerBase)
        val SmallBlockHeavyArmorSquareSlopedCornerBase =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorSquareSlopedCornerBase)
        val LargeBlockArmorSquareSlopedCornerTip = create(this, DefinitionIdTypes.LargeBlockArmorSquareSlopedCornerTip)
        val SmallBlockArmorSquareSlopedCornerTip = create(this, DefinitionIdTypes.SmallBlockArmorSquareSlopedCornerTip)
        val LargeBlockHeavyArmorSquareSlopedCornerTip =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorSquareSlopedCornerTip)
        val SmallBlockHeavyArmorSquareSlopedCornerTip =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorSquareSlopedCornerTip)
        val LargeBlockArmorSquareSlopedCornerTipInv =
            create(this, DefinitionIdTypes.LargeBlockArmorSquareSlopedCornerTipInv)
        val SmallBlockArmorSquareSlopedCornerTipInv =
            create(this, DefinitionIdTypes.SmallBlockArmorSquareSlopedCornerTipInv)
        val LargeBlockHeavyArmorSquareSlopedCornerTipInv =
            create(this, DefinitionIdTypes.LargeBlockHeavyArmorSquareSlopedCornerTipInv)
        val SmallBlockHeavyArmorSquareSlopedCornerTipInv =
            create(this, DefinitionIdTypes.SmallBlockHeavyArmorSquareSlopedCornerTipInv)
        val LargeArmorPanelLight = create(this, DefinitionIdTypes.LargeArmorPanelLight)
        val LargeArmorSlopedSidePanelLight = create(this, DefinitionIdTypes.LargeArmorSlopedSidePanelLight)
        val LargeArmorSlopedPanelLight = create(this, DefinitionIdTypes.LargeArmorSlopedPanelLight)
        val LargeArmorHalfPanelLight = create(this, DefinitionIdTypes.LargeArmorHalfPanelLight)
        val LargeArmorQuarterPanelLight = create(this, DefinitionIdTypes.LargeArmorQuarterPanelLight)
        val LargeArmor2x1SlopedPanelLight = create(this, DefinitionIdTypes.LargeArmor2x1SlopedPanelLight)
        val LargeArmor2x1SlopedPanelTipLight = create(this, DefinitionIdTypes.LargeArmor2x1SlopedPanelTipLight)
        val LargeArmor2x1SlopedSideBasePanelLight =
            create(this, DefinitionIdTypes.LargeArmor2x1SlopedSideBasePanelLight)
        val LargeArmor2x1SlopedSideTipPanelLight = create(this, DefinitionIdTypes.LargeArmor2x1SlopedSideTipPanelLight)
        val LargeArmor2x1SlopedSideBasePanelLightInv =
            create(this, DefinitionIdTypes.LargeArmor2x1SlopedSideBasePanelLightInv)
        val LargeArmor2x1SlopedSideTipPanelLightInv =
            create(this, DefinitionIdTypes.LargeArmor2x1SlopedSideTipPanelLightInv)
        val LargeArmorHalfSlopedPanelLight = create(this, DefinitionIdTypes.LargeArmorHalfSlopedPanelLight)
        val LargeArmor2x1HalfSlopedPanelLightRight =
            create(this, DefinitionIdTypes.LargeArmor2x1HalfSlopedPanelLightRight)
        val LargeArmor2x1HalfSlopedTipPanelLightRight =
            create(this, DefinitionIdTypes.LargeArmor2x1HalfSlopedTipPanelLightRight)
        val LargeArmor2x1HalfSlopedPanelLightLeft =
            create(this, DefinitionIdTypes.LargeArmor2x1HalfSlopedPanelLightLeft)
        val LargeArmor2x1HalfSlopedTipPanelLightLeft =
            create(this, DefinitionIdTypes.LargeArmor2x1HalfSlopedTipPanelLightLeft)
        val LargeArmorPanelHeavy = create(this, DefinitionIdTypes.LargeArmorPanelHeavy)
        val LargeArmorSlopedSidePanelHeavy = create(this, DefinitionIdTypes.LargeArmorSlopedSidePanelHeavy)
        val LargeArmorSlopedPanelHeavy = create(this, DefinitionIdTypes.LargeArmorSlopedPanelHeavy)
        val LargeArmorHalfPanelHeavy = create(this, DefinitionIdTypes.LargeArmorHalfPanelHeavy)
        val LargeArmorQuarterPanelHeavy = create(this, DefinitionIdTypes.LargeArmorQuarterPanelHeavy)
        val LargeArmor2x1SlopedPanelHeavy = create(this, DefinitionIdTypes.LargeArmor2x1SlopedPanelHeavy)
        val LargeArmor2x1SlopedPanelTipHeavy = create(this, DefinitionIdTypes.LargeArmor2x1SlopedPanelTipHeavy)
        val LargeArmor2x1SlopedSideBasePanelHeavy =
            create(this, DefinitionIdTypes.LargeArmor2x1SlopedSideBasePanelHeavy)
        val LargeArmor2x1SlopedSideTipPanelHeavy = create(this, DefinitionIdTypes.LargeArmor2x1SlopedSideTipPanelHeavy)
        val LargeArmor2x1SlopedSideBasePanelHeavyInv =
            create(this, DefinitionIdTypes.LargeArmor2x1SlopedSideBasePanelHeavyInv)
        val LargeArmor2x1SlopedSideTipPanelHeavyInv =
            create(this, DefinitionIdTypes.LargeArmor2x1SlopedSideTipPanelHeavyInv)
        val LargeArmorHalfSlopedPanelHeavy = create(this, DefinitionIdTypes.LargeArmorHalfSlopedPanelHeavy)
        val LargeArmor2x1HalfSlopedPanelHeavyRight =
            create(this, DefinitionIdTypes.LargeArmor2x1HalfSlopedPanelHeavyRight)
        val LargeArmor2x1HalfSlopedTipPanelHeavyRight =
            create(this, DefinitionIdTypes.LargeArmor2x1HalfSlopedTipPanelHeavyRight)
        val LargeArmor2x1HalfSlopedPanelHeavyLeft =
            create(this, DefinitionIdTypes.LargeArmor2x1HalfSlopedPanelHeavyLeft)
        val LargeArmor2x1HalfSlopedTipPanelHeavyLeft =
            create(this, DefinitionIdTypes.LargeArmor2x1HalfSlopedTipPanelHeavyLeft)
        val SmallArmorPanelLight = create(this, DefinitionIdTypes.SmallArmorPanelLight)
        val SmallArmorSlopedSidePanelLight = create(this, DefinitionIdTypes.SmallArmorSlopedSidePanelLight)
        val SmallArmorSlopedPanelLight = create(this, DefinitionIdTypes.SmallArmorSlopedPanelLight)
        val SmallArmorHalfPanelLight = create(this, DefinitionIdTypes.SmallArmorHalfPanelLight)
        val SmallArmorQuarterPanelLight = create(this, DefinitionIdTypes.SmallArmorQuarterPanelLight)
        val SmallArmor2x1SlopedPanelLight = create(this, DefinitionIdTypes.SmallArmor2x1SlopedPanelLight)
        val SmallArmor2x1SlopedPanelTipLight = create(this, DefinitionIdTypes.SmallArmor2x1SlopedPanelTipLight)
        val SmallArmor2x1SlopedSideBasePanelLight =
            create(this, DefinitionIdTypes.SmallArmor2x1SlopedSideBasePanelLight)
        val SmallArmor2x1SlopedSideTipPanelLight = create(this, DefinitionIdTypes.SmallArmor2x1SlopedSideTipPanelLight)
        val SmallArmor2x1SlopedSideBasePanelLightInv =
            create(this, DefinitionIdTypes.SmallArmor2x1SlopedSideBasePanelLightInv)
        val SmallArmor2x1SlopedSideTipPanelLightInv =
            create(this, DefinitionIdTypes.SmallArmor2x1SlopedSideTipPanelLightInv)
        val SmallArmorHalfSlopedPanelLight = create(this, DefinitionIdTypes.SmallArmorHalfSlopedPanelLight)
        val SmallArmor2x1HalfSlopedPanelLightRight =
            create(this, DefinitionIdTypes.SmallArmor2x1HalfSlopedPanelLightRight)
        val SmallArmor2x1HalfSlopedTipPanelLightRight =
            create(this, DefinitionIdTypes.SmallArmor2x1HalfSlopedTipPanelLightRight)
        val SmallArmor2x1HalfSlopedPanelLightLeft =
            create(this, DefinitionIdTypes.SmallArmor2x1HalfSlopedPanelLightLeft)
        val SmallArmor2x1HalfSlopedTipPanelLightLeft =
            create(this, DefinitionIdTypes.SmallArmor2x1HalfSlopedTipPanelLightLeft)
        val SmallArmorPanelHeavy = create(this, DefinitionIdTypes.SmallArmorPanelHeavy)
        val SmallArmorSlopedSidePanelHeavy = create(this, DefinitionIdTypes.SmallArmorSlopedSidePanelHeavy)
        val SmallArmorSlopedPanelHeavy = create(this, DefinitionIdTypes.SmallArmorSlopedPanelHeavy)
        val SmallArmorHalfPanelHeavy = create(this, DefinitionIdTypes.SmallArmorHalfPanelHeavy)
        val SmallArmorQuarterPanelHeavy = create(this, DefinitionIdTypes.SmallArmorQuarterPanelHeavy)
        val SmallArmor2x1SlopedPanelHeavy = create(this, DefinitionIdTypes.SmallArmor2x1SlopedPanelHeavy)
        val SmallArmor2x1SlopedPanelTipHeavy = create(this, DefinitionIdTypes.SmallArmor2x1SlopedPanelTipHeavy)
        val SmallArmor2x1SlopedSideBasePanelHeavy =
            create(this, DefinitionIdTypes.SmallArmor2x1SlopedSideBasePanelHeavy)
        val SmallArmor2x1SlopedSideTipPanelHeavy = create(this, DefinitionIdTypes.SmallArmor2x1SlopedSideTipPanelHeavy)
        val SmallArmor2x1SlopedSideBasePanelHeavyInv =
            create(this, DefinitionIdTypes.SmallArmor2x1SlopedSideBasePanelHeavyInv)
        val SmallArmor2x1SlopedSideTipPanelHeavyInv =
            create(this, DefinitionIdTypes.SmallArmor2x1SlopedSideTipPanelHeavyInv)
        val SmallArmorHalfSlopedPanelHeavy = create(this, DefinitionIdTypes.SmallArmorHalfSlopedPanelHeavy)
        val SmallArmor2x1HalfSlopedPanelHeavyRight =
            create(this, DefinitionIdTypes.SmallArmor2x1HalfSlopedPanelHeavyRight)
        val SmallArmor2x1HalfSlopedTipPanelHeavyRight =
            create(this, DefinitionIdTypes.SmallArmor2x1HalfSlopedTipPanelHeavyRight)
        val SmallArmor2x1HalfSlopedPanelHeavyLeft =
            create(this, DefinitionIdTypes.SmallArmor2x1HalfSlopedPanelHeavyLeft)
        val SmallArmor2x1HalfSlopedTipPanelHeavyLeft =
            create(this, DefinitionIdTypes.SmallArmor2x1HalfSlopedTipPanelHeavyLeft)
        val LargeBlockDeskChairless = create(this, DefinitionIdTypes.LargeBlockDeskChairless)
        val LargeBlockDeskChairlessCorner = create(this, DefinitionIdTypes.LargeBlockDeskChairlessCorner)
        val LargeBlockDeskChairlessCornerInv = create(this, DefinitionIdTypes.LargeBlockDeskChairlessCornerInv)
        val Shower = create(this, DefinitionIdTypes.Shower)
        val WindowWall = create(this, DefinitionIdTypes.WindowWall)
        val WindowWallLeft = create(this, DefinitionIdTypes.WindowWallLeft)
        val WindowWallRight = create(this, DefinitionIdTypes.WindowWallRight)
        val Catwalk = create(this, DefinitionIdTypes.Catwalk)
        val CatwalkCorner = create(this, DefinitionIdTypes.CatwalkCorner)
        val CatwalkStraight = create(this, DefinitionIdTypes.CatwalkStraight)
        val CatwalkWall = create(this, DefinitionIdTypes.CatwalkWall)
        val CatwalkRailingEnd = create(this, DefinitionIdTypes.CatwalkRailingEnd)
        val CatwalkRailingHalfRight = create(this, DefinitionIdTypes.CatwalkRailingHalfRight)
        val CatwalkRailingHalfLeft = create(this, DefinitionIdTypes.CatwalkRailingHalfLeft)
        val CatwalkHalf = create(this, DefinitionIdTypes.CatwalkHalf)
        val CatwalkHalfRailing = create(this, DefinitionIdTypes.CatwalkHalfRailing)
        val CatwalkHalfCenterRailing = create(this, DefinitionIdTypes.CatwalkHalfCenterRailing)
        val CatwalkHalfOuterRailing = create(this, DefinitionIdTypes.CatwalkHalfOuterRailing)
        val GratedStairs = create(this, DefinitionIdTypes.GratedStairs)
        val GratedHalfStairs = create(this, DefinitionIdTypes.GratedHalfStairs)
        val GratedHalfStairsMirrored = create(this, DefinitionIdTypes.GratedHalfStairsMirrored)
        val RailingStraight = create(this, DefinitionIdTypes.RailingStraight)
        val RailingDouble = create(this, DefinitionIdTypes.RailingDouble)
        val RailingCorner = create(this, DefinitionIdTypes.RailingCorner)
        val RailingDiagonal = create(this, DefinitionIdTypes.RailingDiagonal)
        val RailingHalfRight = create(this, DefinitionIdTypes.RailingHalfRight)
        val RailingHalfLeft = create(this, DefinitionIdTypes.RailingHalfLeft)
        val RailingCenter = create(this, DefinitionIdTypes.RailingCenter)
        val Railing2x1Right = create(this, DefinitionIdTypes.Railing2x1Right)
        val Railing2x1Left = create(this, DefinitionIdTypes.Railing2x1Left)
        val Freight1 = create(this, DefinitionIdTypes.Freight1)
        val Freight2 = create(this, DefinitionIdTypes.Freight2)
        val Freight3 = create(this, DefinitionIdTypes.Freight3)
        val ArmorCenter = create(this, DefinitionIdTypes.ArmorCenter)
        val ArmorCorner = create(this, DefinitionIdTypes.ArmorCorner)
        val ArmorInvCorner = create(this, DefinitionIdTypes.ArmorInvCorner)
        val ArmorSide = create(this, DefinitionIdTypes.ArmorSide)
        val SmallArmorCenter = create(this, DefinitionIdTypes.SmallArmorCenter)
        val SmallArmorCorner = create(this, DefinitionIdTypes.SmallArmorCorner)
        val SmallArmorInvCorner = create(this, DefinitionIdTypes.SmallArmorInvCorner)
        val SmallArmorSide = create(this, DefinitionIdTypes.SmallArmorSide)
        val Monolith = create(this, DefinitionIdTypes.Monolith)
        val Stereolith = create(this, DefinitionIdTypes.Stereolith)
        val DeadAstronaut = create(this, DefinitionIdTypes.DeadAstronaut)
        val LargeDeadAstronaut = create(this, DefinitionIdTypes.LargeDeadAstronaut)
        val EngineerPlushie = create(this, DefinitionIdTypes.EngineerPlushie)
        val DeadBody01 = create(this, DefinitionIdTypes.DeadBody01)
        val DeadBody02 = create(this, DefinitionIdTypes.DeadBody02)
        val DeadBody03 = create(this, DefinitionIdTypes.DeadBody03)
        val DeadBody04 = create(this, DefinitionIdTypes.DeadBody04)
        val DeadBody05 = create(this, DefinitionIdTypes.DeadBody05)
        val DeadBody06 = create(this, DefinitionIdTypes.DeadBody06)
        val LargeBlockCylindricalColumn = create(this, DefinitionIdTypes.LargeBlockCylindricalColumn)
        val SmallBlockCylindricalColumn = create(this, DefinitionIdTypes.SmallBlockCylindricalColumn)
        val LargeGridBeamBlock = create(this, DefinitionIdTypes.LargeGridBeamBlock)
        val LargeGridBeamBlockSlope = create(this, DefinitionIdTypes.LargeGridBeamBlockSlope)
        val LargeGridBeamBlockRound = create(this, DefinitionIdTypes.LargeGridBeamBlockRound)
        val LargeGridBeamBlockSlope2x1Base = create(this, DefinitionIdTypes.LargeGridBeamBlockSlope2x1Base)
        val LargeGridBeamBlockSlope2x1Tip = create(this, DefinitionIdTypes.LargeGridBeamBlockSlope2x1Tip)
        val LargeGridBeamBlockHalf = create(this, DefinitionIdTypes.LargeGridBeamBlockHalf)
        val LargeGridBeamBlockHalfSlope = create(this, DefinitionIdTypes.LargeGridBeamBlockHalfSlope)
        val LargeGridBeamBlockEnd = create(this, DefinitionIdTypes.LargeGridBeamBlockEnd)
        val LargeGridBeamBlockJunction = create(this, DefinitionIdTypes.LargeGridBeamBlockJunction)
        val LargeGridBeamBlockTJunction = create(this, DefinitionIdTypes.LargeGridBeamBlockTJunction)
        val SmallGridBeamBlock = create(this, DefinitionIdTypes.SmallGridBeamBlock)
        val SmallGridBeamBlockSlope = create(this, DefinitionIdTypes.SmallGridBeamBlockSlope)
        val SmallGridBeamBlockRound = create(this, DefinitionIdTypes.SmallGridBeamBlockRound)
        val SmallGridBeamBlockSlope2x1Base = create(this, DefinitionIdTypes.SmallGridBeamBlockSlope2x1Base)
        val SmallGridBeamBlockSlope2x1Tip = create(this, DefinitionIdTypes.SmallGridBeamBlockSlope2x1Tip)
        val SmallGridBeamBlockHalf = create(this, DefinitionIdTypes.SmallGridBeamBlockHalf)
        val SmallGridBeamBlockHalfSlope = create(this, DefinitionIdTypes.SmallGridBeamBlockHalfSlope)
        val SmallGridBeamBlockEnd = create(this, DefinitionIdTypes.SmallGridBeamBlockEnd)
        val SmallGridBeamBlockJunction = create(this, DefinitionIdTypes.SmallGridBeamBlockJunction)
        val SmallGridBeamBlockTJunction = create(this, DefinitionIdTypes.SmallGridBeamBlockTJunction)
        val Passage2 = create(this, DefinitionIdTypes.Passage2)
        val Passage2Wall = create(this, DefinitionIdTypes.Passage2Wall)
        val LargeStairs = create(this, DefinitionIdTypes.LargeStairs)
        val LargeRamp = create(this, DefinitionIdTypes.LargeRamp)
        val LargeSteelCatwalk = create(this, DefinitionIdTypes.LargeSteelCatwalk)
        val LargeSteelCatwalk2Sides = create(this, DefinitionIdTypes.LargeSteelCatwalk2Sides)
        val LargeSteelCatwalkCorner = create(this, DefinitionIdTypes.LargeSteelCatwalkCorner)
        val LargeSteelCatwalkPlate = create(this, DefinitionIdTypes.LargeSteelCatwalkPlate)
        val LargeCoverWall = create(this, DefinitionIdTypes.LargeCoverWall)
        val LargeCoverWallHalf = create(this, DefinitionIdTypes.LargeCoverWallHalf)
        val LargeCoverWallHalfMirrored = create(this, DefinitionIdTypes.LargeCoverWallHalfMirrored)
        val LargeBlockInteriorWall = create(this, DefinitionIdTypes.LargeBlockInteriorWall)
        val LargeInteriorPillar = create(this, DefinitionIdTypes.LargeInteriorPillar)
        val Viewport1 = create(this, DefinitionIdTypes.Viewport1)
        val Viewport2 = create(this, DefinitionIdTypes.Viewport2)
        val BarredWindow = create(this, DefinitionIdTypes.BarredWindow)
        val BarredWindowSlope = create(this, DefinitionIdTypes.BarredWindowSlope)
        val BarredWindowSide = create(this, DefinitionIdTypes.BarredWindowSide)
        val BarredWindowFace = create(this, DefinitionIdTypes.BarredWindowFace)
        val StorageShelf1 = create(this, DefinitionIdTypes.StorageShelf1)
        val StorageShelf2 = create(this, DefinitionIdTypes.StorageShelf2)
        val StorageShelf3 = create(this, DefinitionIdTypes.StorageShelf3)
        val LargeBlockSciFiWall = create(this, DefinitionIdTypes.LargeBlockSciFiWall)
        val LargeBlockBarCounter = create(this, DefinitionIdTypes.LargeBlockBarCounter)
        val LargeBlockBarCounterCorner = create(this, DefinitionIdTypes.LargeBlockBarCounterCorner)
        val LargeSymbolA = create(this, DefinitionIdTypes.LargeSymbolA)
        val LargeSymbolB = create(this, DefinitionIdTypes.LargeSymbolB)
        val LargeSymbolC = create(this, DefinitionIdTypes.LargeSymbolC)
        val LargeSymbolD = create(this, DefinitionIdTypes.LargeSymbolD)
        val LargeSymbolE = create(this, DefinitionIdTypes.LargeSymbolE)
        val LargeSymbolF = create(this, DefinitionIdTypes.LargeSymbolF)
        val LargeSymbolG = create(this, DefinitionIdTypes.LargeSymbolG)
        val LargeSymbolH = create(this, DefinitionIdTypes.LargeSymbolH)
        val LargeSymbolI = create(this, DefinitionIdTypes.LargeSymbolI)
        val LargeSymbolJ = create(this, DefinitionIdTypes.LargeSymbolJ)
        val LargeSymbolK = create(this, DefinitionIdTypes.LargeSymbolK)
        val LargeSymbolL = create(this, DefinitionIdTypes.LargeSymbolL)
        val LargeSymbolM = create(this, DefinitionIdTypes.LargeSymbolM)
        val LargeSymbolN = create(this, DefinitionIdTypes.LargeSymbolN)
        val LargeSymbolO = create(this, DefinitionIdTypes.LargeSymbolO)
        val LargeSymbolP = create(this, DefinitionIdTypes.LargeSymbolP)
        val LargeSymbolQ = create(this, DefinitionIdTypes.LargeSymbolQ)
        val LargeSymbolR = create(this, DefinitionIdTypes.LargeSymbolR)
        val LargeSymbolS = create(this, DefinitionIdTypes.LargeSymbolS)
        val LargeSymbolT = create(this, DefinitionIdTypes.LargeSymbolT)
        val LargeSymbolU = create(this, DefinitionIdTypes.LargeSymbolU)
        val LargeSymbolV = create(this, DefinitionIdTypes.LargeSymbolV)
        val LargeSymbolW = create(this, DefinitionIdTypes.LargeSymbolW)
        val LargeSymbolX = create(this, DefinitionIdTypes.LargeSymbolX)
        val LargeSymbolY = create(this, DefinitionIdTypes.LargeSymbolY)
        val LargeSymbolZ = create(this, DefinitionIdTypes.LargeSymbolZ)
        val SmallSymbolA = create(this, DefinitionIdTypes.SmallSymbolA)
        val SmallSymbolB = create(this, DefinitionIdTypes.SmallSymbolB)
        val SmallSymbolC = create(this, DefinitionIdTypes.SmallSymbolC)
        val SmallSymbolD = create(this, DefinitionIdTypes.SmallSymbolD)
        val SmallSymbolE = create(this, DefinitionIdTypes.SmallSymbolE)
        val SmallSymbolF = create(this, DefinitionIdTypes.SmallSymbolF)
        val SmallSymbolG = create(this, DefinitionIdTypes.SmallSymbolG)
        val SmallSymbolH = create(this, DefinitionIdTypes.SmallSymbolH)
        val SmallSymbolI = create(this, DefinitionIdTypes.SmallSymbolI)
        val SmallSymbolJ = create(this, DefinitionIdTypes.SmallSymbolJ)
        val SmallSymbolK = create(this, DefinitionIdTypes.SmallSymbolK)
        val SmallSymbolL = create(this, DefinitionIdTypes.SmallSymbolL)
        val SmallSymbolM = create(this, DefinitionIdTypes.SmallSymbolM)
        val SmallSymbolN = create(this, DefinitionIdTypes.SmallSymbolN)
        val SmallSymbolO = create(this, DefinitionIdTypes.SmallSymbolO)
        val SmallSymbolP = create(this, DefinitionIdTypes.SmallSymbolP)
        val SmallSymbolQ = create(this, DefinitionIdTypes.SmallSymbolQ)
        val SmallSymbolR = create(this, DefinitionIdTypes.SmallSymbolR)
        val SmallSymbolS = create(this, DefinitionIdTypes.SmallSymbolS)
        val SmallSymbolT = create(this, DefinitionIdTypes.SmallSymbolT)
        val SmallSymbolU = create(this, DefinitionIdTypes.SmallSymbolU)
        val SmallSymbolV = create(this, DefinitionIdTypes.SmallSymbolV)
        val SmallSymbolW = create(this, DefinitionIdTypes.SmallSymbolW)
        val SmallSymbolX = create(this, DefinitionIdTypes.SmallSymbolX)
        val SmallSymbolY = create(this, DefinitionIdTypes.SmallSymbolY)
        val SmallSymbolZ = create(this, DefinitionIdTypes.SmallSymbolZ)
        val LargeSymbol0 = create(this, DefinitionIdTypes.LargeSymbol0)
        val LargeSymbol1 = create(this, DefinitionIdTypes.LargeSymbol1)
        val LargeSymbol2 = create(this, DefinitionIdTypes.LargeSymbol2)
        val LargeSymbol3 = create(this, DefinitionIdTypes.LargeSymbol3)
        val LargeSymbol4 = create(this, DefinitionIdTypes.LargeSymbol4)
        val LargeSymbol5 = create(this, DefinitionIdTypes.LargeSymbol5)
        val LargeSymbol6 = create(this, DefinitionIdTypes.LargeSymbol6)
        val LargeSymbol7 = create(this, DefinitionIdTypes.LargeSymbol7)
        val LargeSymbol8 = create(this, DefinitionIdTypes.LargeSymbol8)
        val LargeSymbol9 = create(this, DefinitionIdTypes.LargeSymbol9)
        val SmallSymbol0 = create(this, DefinitionIdTypes.SmallSymbol0)
        val SmallSymbol1 = create(this, DefinitionIdTypes.SmallSymbol1)
        val SmallSymbol2 = create(this, DefinitionIdTypes.SmallSymbol2)
        val SmallSymbol3 = create(this, DefinitionIdTypes.SmallSymbol3)
        val SmallSymbol4 = create(this, DefinitionIdTypes.SmallSymbol4)
        val SmallSymbol5 = create(this, DefinitionIdTypes.SmallSymbol5)
        val SmallSymbol6 = create(this, DefinitionIdTypes.SmallSymbol6)
        val SmallSymbol7 = create(this, DefinitionIdTypes.SmallSymbol7)
        val SmallSymbol8 = create(this, DefinitionIdTypes.SmallSymbol8)
        val SmallSymbol9 = create(this, DefinitionIdTypes.SmallSymbol9)
        val LargeSymbolHyphen = create(this, DefinitionIdTypes.LargeSymbolHyphen)
        val LargeSymbolUnderscore = create(this, DefinitionIdTypes.LargeSymbolUnderscore)
        val LargeSymbolDot = create(this, DefinitionIdTypes.LargeSymbolDot)
        val LargeSymbolApostrophe = create(this, DefinitionIdTypes.LargeSymbolApostrophe)
        val LargeSymbolAnd = create(this, DefinitionIdTypes.LargeSymbolAnd)
        val LargeSymbolColon = create(this, DefinitionIdTypes.LargeSymbolColon)
        val LargeSymbolExclamationMark = create(this, DefinitionIdTypes.LargeSymbolExclamationMark)
        val LargeSymbolQuestionMark = create(this, DefinitionIdTypes.LargeSymbolQuestionMark)
        val SmallSymbolHyphen = create(this, DefinitionIdTypes.SmallSymbolHyphen)
        val SmallSymbolUnderscore = create(this, DefinitionIdTypes.SmallSymbolUnderscore)
        val SmallSymbolDot = create(this, DefinitionIdTypes.SmallSymbolDot)
        val SmallSymbolApostrophe = create(this, DefinitionIdTypes.SmallSymbolApostrophe)
        val SmallSymbolAnd = create(this, DefinitionIdTypes.SmallSymbolAnd)
        val SmallSymbolColon = create(this, DefinitionIdTypes.SmallSymbolColon)
        val SmallSymbolExclamationMark = create(this, DefinitionIdTypes.SmallSymbolExclamationMark)
        val SmallSymbolQuestionMark = create(this, DefinitionIdTypes.SmallSymbolQuestionMark)
        val FireCover = create(this, DefinitionIdTypes.FireCover)
        val FireCoverCorner = create(this, DefinitionIdTypes.FireCoverCorner)
        val HalfWindow = create(this, DefinitionIdTypes.HalfWindow)
        val HalfWindowInv = create(this, DefinitionIdTypes.HalfWindowInv)
        val HalfWindowCorner = create(this, DefinitionIdTypes.HalfWindowCorner)
        val HalfWindowCornerInv = create(this, DefinitionIdTypes.HalfWindowCornerInv)
        val HalfWindowDiagonal = create(this, DefinitionIdTypes.HalfWindowDiagonal)
        val HalfWindowRound = create(this, DefinitionIdTypes.HalfWindowRound)
        val Embrasure = create(this, DefinitionIdTypes.Embrasure)
        val PassageSciFi = create(this, DefinitionIdTypes.PassageSciFi)
        val PassageSciFiWall = create(this, DefinitionIdTypes.PassageSciFiWall)
        val PassageSciFiIntersection = create(this, DefinitionIdTypes.PassageSciFiIntersection)
        val PassageSciFiGate = create(this, DefinitionIdTypes.PassageSciFiGate)
        val PassageScifiCorner = create(this, DefinitionIdTypes.PassageScifiCorner)
        val PassageSciFiTjunction = create(this, DefinitionIdTypes.PassageSciFiTjunction)
        val PassageSciFiWindow = create(this, DefinitionIdTypes.PassageSciFiWindow)
        val BridgeWindow1x1Slope = create(this, DefinitionIdTypes.BridgeWindow1x1Slope)
        val BridgeWindow1x1Face = create(this, DefinitionIdTypes.BridgeWindow1x1Face)
        val LargeWindowSquare = create(this, DefinitionIdTypes.LargeWindowSquare)
        val LargeWindowEdge = create(this, DefinitionIdTypes.LargeWindowEdge)
        val Window1x2Slope = create(this, DefinitionIdTypes.Window1x2Slope)
        val Window1x2Inv = create(this, DefinitionIdTypes.Window1x2Inv)
        val Window1x2Face = create(this, DefinitionIdTypes.Window1x2Face)
        val Window1x2SideLeft = create(this, DefinitionIdTypes.Window1x2SideLeft)
        val Window1x2SideLeftInv = create(this, DefinitionIdTypes.Window1x2SideLeftInv)
        val Window1x2SideRight = create(this, DefinitionIdTypes.Window1x2SideRight)
        val Window1x2SideRightInv = create(this, DefinitionIdTypes.Window1x2SideRightInv)
        val Window1x1Slope = create(this, DefinitionIdTypes.Window1x1Slope)
        val Window1x1Face = create(this, DefinitionIdTypes.Window1x1Face)
        val Window1x1Side = create(this, DefinitionIdTypes.Window1x1Side)
        val Window1x1SideInv = create(this, DefinitionIdTypes.Window1x1SideInv)
        val Window1x1Inv = create(this, DefinitionIdTypes.Window1x1Inv)
        val Window1x2Flat = create(this, DefinitionIdTypes.Window1x2Flat)
        val Window1x2FlatInv = create(this, DefinitionIdTypes.Window1x2FlatInv)
        val Window1x1Flat = create(this, DefinitionIdTypes.Window1x1Flat)
        val Window1x1FlatInv = create(this, DefinitionIdTypes.Window1x1FlatInv)
        val Window3x3Flat = create(this, DefinitionIdTypes.Window3x3Flat)
        val Window3x3FlatInv = create(this, DefinitionIdTypes.Window3x3FlatInv)
        val Window2x3Flat = create(this, DefinitionIdTypes.Window2x3Flat)
        val Window2x3FlatInv = create(this, DefinitionIdTypes.Window2x3FlatInv)
        val SmallWindow1x2Slope = create(this, DefinitionIdTypes.SmallWindow1x2Slope)
        val SmallWindow1x2Inv = create(this, DefinitionIdTypes.SmallWindow1x2Inv)
        val SmallWindow1x2Face = create(this, DefinitionIdTypes.SmallWindow1x2Face)
        val SmallWindow1x2SideLeft = create(this, DefinitionIdTypes.SmallWindow1x2SideLeft)
        val SmallWindow1x2SideLeftInv = create(this, DefinitionIdTypes.SmallWindow1x2SideLeftInv)
        val SmallWindow1x2SideRight = create(this, DefinitionIdTypes.SmallWindow1x2SideRight)
        val SmallWindow1x2SideRightInv = create(this, DefinitionIdTypes.SmallWindow1x2SideRightInv)
        val SmallWindow1x1Slope = create(this, DefinitionIdTypes.SmallWindow1x1Slope)
        val SmallWindow1x1Face = create(this, DefinitionIdTypes.SmallWindow1x1Face)
        val SmallWindow1x1Side = create(this, DefinitionIdTypes.SmallWindow1x1Side)
        val SmallWindow1x1SideInv = create(this, DefinitionIdTypes.SmallWindow1x1SideInv)
        val SmallWindow1x1Inv = create(this, DefinitionIdTypes.SmallWindow1x1Inv)
        val SmallWindow1x2Flat = create(this, DefinitionIdTypes.SmallWindow1x2Flat)
        val SmallWindow1x2FlatInv = create(this, DefinitionIdTypes.SmallWindow1x2FlatInv)
        val SmallWindow1x1Flat = create(this, DefinitionIdTypes.SmallWindow1x1Flat)
        val SmallWindow1x1FlatInv = create(this, DefinitionIdTypes.SmallWindow1x1FlatInv)
        val SmallWindow3x3Flat = create(this, DefinitionIdTypes.SmallWindow3x3Flat)
        val SmallWindow3x3FlatInv = create(this, DefinitionIdTypes.SmallWindow3x3FlatInv)
        val SmallWindow2x3Flat = create(this, DefinitionIdTypes.SmallWindow2x3Flat)
        val SmallWindow2x3FlatInv = create(this, DefinitionIdTypes.SmallWindow2x3FlatInv)
        val WindowRound = create(this, DefinitionIdTypes.WindowRound)
        val WindowRoundInv = create(this, DefinitionIdTypes.WindowRoundInv)
        val WindowRoundCorner = create(this, DefinitionIdTypes.WindowRoundCorner)
        val WindowRoundCornerInv = create(this, DefinitionIdTypes.WindowRoundCornerInv)
        val WindowRoundFace = create(this, DefinitionIdTypes.WindowRoundFace)
        val WindowRoundFaceInv = create(this, DefinitionIdTypes.WindowRoundFaceInv)
        val WindowRoundInwardsCorner = create(this, DefinitionIdTypes.WindowRoundInwardsCorner)
        val WindowRoundInwardsCornerInv = create(this, DefinitionIdTypes.WindowRoundInwardsCornerInv)
        val SmallWindowRound = create(this, DefinitionIdTypes.SmallWindowRound)
        val SmallWindowRoundInv = create(this, DefinitionIdTypes.SmallWindowRoundInv)
        val SmallWindowRoundCorner = create(this, DefinitionIdTypes.SmallWindowRoundCorner)
        val SmallWindowRoundCornerInv = create(this, DefinitionIdTypes.SmallWindowRoundCornerInv)
        val SmallWindowRoundFace = create(this, DefinitionIdTypes.SmallWindowRoundFace)
        val SmallWindowRoundFaceInv = create(this, DefinitionIdTypes.SmallWindowRoundFaceInv)
        val SmallWindowRoundInwardsCorner = create(this, DefinitionIdTypes.SmallWindowRoundInwardsCorner)
        val SmallWindowRoundInwardsCornerInv = create(this, DefinitionIdTypes.SmallWindowRoundInwardsCornerInv)
    }

    object DebugSphere1 : SafeDefinitionIdId() {
        val DebugSphereLarge = create(this, DefinitionIdTypes.DebugSphereLarge)
    }

    object DebugSphere2 : SafeDefinitionIdId() {
        val DebugSphereLarge = create(this, DefinitionIdTypes.DebugSphereLarge)
    }

    object DebugSphere3 : SafeDefinitionIdId() {
        val DebugSphereLarge = create(this, DefinitionIdTypes.DebugSphereLarge)
    }

    object MyProgrammableBlock : SafeDefinitionIdId() {
        val SmallProgrammableBlock = create(this, DefinitionIdTypes.SmallProgrammableBlock)
        val LargeProgrammableBlock = create(this, DefinitionIdTypes.LargeProgrammableBlock)
    }

    object Projector : SafeDefinitionIdId() {
        val LargeProjector = create(this, DefinitionIdTypes.LargeProjector)
        val SmallProjector = create(this, DefinitionIdTypes.SmallProjector)
        val LargeBlockConsole = create(this, DefinitionIdTypes.LargeBlockConsole)
    }

    object SensorBlock : SafeDefinitionIdId() {
        val SmallBlockSensor = create(this, DefinitionIdTypes.SmallBlockSensor)
        val LargeBlockSensor = create(this, DefinitionIdTypes.LargeBlockSensor)
    }

    object TargetDummyBlock : SafeDefinitionIdId() {
        val TargetDummy = create(this, DefinitionIdTypes.TargetDummy)
    }

    object SoundBlock : SafeDefinitionIdId() {
        val SmallBlockSoundBlock = create(this, DefinitionIdTypes.SmallBlockSoundBlock)
        val LargeBlockSoundBlock = create(this, DefinitionIdTypes.LargeBlockSoundBlock)
    }

    object ButtonPanel : SafeDefinitionIdId() {
        val ButtonPanelLarge = create(this, DefinitionIdTypes.ButtonPanelLarge)
        val ButtonPanelSmall = create(this, DefinitionIdTypes.ButtonPanelSmall)
        val VerticalButtonPanelLarge = create(this, DefinitionIdTypes.VerticalButtonPanelLarge)
        val VerticalButtonPanelSmall = create(this, DefinitionIdTypes.VerticalButtonPanelSmall)
        val LargeSciFiButtonTerminal = create(this, DefinitionIdTypes.LargeSciFiButtonTerminal)
        val LargeSciFiButtonPanel = create(this, DefinitionIdTypes.LargeSciFiButtonPanel)
    }

    object TimerBlock : SafeDefinitionIdId() {
        val TimerBlockLarge = create(this, DefinitionIdTypes.TimerBlockLarge)
        val TimerBlockSmall = create(this, DefinitionIdTypes.TimerBlockSmall)
    }

    object TurretControlBlock : SafeDefinitionIdId() {
        val LargeTurretControlBlock = create(this, DefinitionIdTypes.LargeTurretControlBlock)
        val SmallTurretControlBlock = create(this, DefinitionIdTypes.SmallTurretControlBlock)
    }

    object RadioAntenna : SafeDefinitionIdId() {
        val LargeBlockRadioAntenna = create(this, DefinitionIdTypes.LargeBlockRadioAntenna)
        val SmallBlockRadioAntenna = create(this, DefinitionIdTypes.SmallBlockRadioAntenna)
        val LargeBlockRadioAntennaDish = create(this, DefinitionIdTypes.LargeBlockRadioAntennaDish)
    }

    object Beacon : SafeDefinitionIdId() {
        val LargeBlockBeacon = create(this, DefinitionIdTypes.LargeBlockBeacon)
        val SmallBlockBeacon = create(this, DefinitionIdTypes.SmallBlockBeacon)
    }

    object RemoteControl : SafeDefinitionIdId() {
        val LargeBlockRemoteControl = create(this, DefinitionIdTypes.LargeBlockRemoteControl)
        val SmallBlockRemoteControl = create(this, DefinitionIdTypes.SmallBlockRemoteControl)
    }

    object LaserAntenna : SafeDefinitionIdId() {
        val LargeBlockLaserAntenna = create(this, DefinitionIdTypes.LargeBlockLaserAntenna)
        val SmallBlockLaserAntenna = create(this, DefinitionIdTypes.SmallBlockLaserAntenna)
    }

    object TerminalBlock : SafeDefinitionIdId() {
        val ControlPanel = create(this, DefinitionIdTypes.ControlPanel)
        val SmallControlPanel = create(this, DefinitionIdTypes.SmallControlPanel)
        val LargeBlockSciFiTerminal = create(this, DefinitionIdTypes.LargeBlockSciFiTerminal)
    }

    object Cockpit : SafeDefinitionIdId() {
        val LargeBlockCockpit = create(this, DefinitionIdTypes.LargeBlockCockpit)
        val LargeBlockCockpitSeat = create(this, DefinitionIdTypes.LargeBlockCockpitSeat)
        val SmallBlockCockpit = create(this, DefinitionIdTypes.SmallBlockCockpit)
        val DBSmallBlockFighterCockpit = create(this, DefinitionIdTypes.DBSmallBlockFighterCockpit)
        val CockpitOpen = create(this, DefinitionIdTypes.CockpitOpen)
        val RoverCockpit = create(this, DefinitionIdTypes.RoverCockpit)
        val OpenCockpitSmall = create(this, DefinitionIdTypes.OpenCockpitSmall)
        val OpenCockpitLarge = create(this, DefinitionIdTypes.OpenCockpitLarge)
        val LargeBlockDesk = create(this, DefinitionIdTypes.LargeBlockDesk)
        val LargeBlockDeskCorner = create(this, DefinitionIdTypes.LargeBlockDeskCorner)
        val LargeBlockDeskCornerInv = create(this, DefinitionIdTypes.LargeBlockDeskCornerInv)
        val LargeBlockCouch = create(this, DefinitionIdTypes.LargeBlockCouch)
        val LargeBlockCouchCorner = create(this, DefinitionIdTypes.LargeBlockCouchCorner)
        val LargeBlockBathroomOpen = create(this, DefinitionIdTypes.LargeBlockBathroomOpen)
        val LargeBlockBathroom = create(this, DefinitionIdTypes.LargeBlockBathroom)
        val LargeBlockToilet = create(this, DefinitionIdTypes.LargeBlockToilet)
        val SmallBlockCockpitIndustrial = create(this, DefinitionIdTypes.SmallBlockCockpitIndustrial)
        val LargeBlockCockpitIndustrial = create(this, DefinitionIdTypes.LargeBlockCockpitIndustrial)
        val PassengerSeatLarge = create(this, DefinitionIdTypes.PassengerSeatLarge)
        val PassengerSeatSmall = create(this, DefinitionIdTypes.PassengerSeatSmall)
        val PassengerSeatSmallNew = create(this, DefinitionIdTypes.PassengerSeatSmallNew)
        val PassengerSeatSmallOffset = create(this, DefinitionIdTypes.PassengerSeatSmallOffset)
        val BuggyCockpit = create(this, DefinitionIdTypes.BuggyCockpit)
        val PassengerBench = create(this, DefinitionIdTypes.PassengerBench)
        val SmallBlockStandingCockpit = create(this, DefinitionIdTypes.SmallBlockStandingCockpit)
        val LargeBlockStandingCockpit = create(this, DefinitionIdTypes.LargeBlockStandingCockpit)
    }

    object Gyro : SafeDefinitionIdId() {
        val LargeBlockGyro = create(this, DefinitionIdTypes.LargeBlockGyro)
        val SmallBlockGyro = create(this, DefinitionIdTypes.SmallBlockGyro)
    }

    object Kitchen : SafeDefinitionIdId() {
        val LargeBlockKitchen = create(this, DefinitionIdTypes.LargeBlockKitchen)
    }

    object CryoChamber : SafeDefinitionIdId() {
        val LargeBlockBed = create(this, DefinitionIdTypes.LargeBlockBed)
        val LargeBlockCryoChamber = create(this, DefinitionIdTypes.LargeBlockCryoChamber)
        val SmallBlockCryoChamber = create(this, DefinitionIdTypes.SmallBlockCryoChamber)
    }

    object CargoContainer : SafeDefinitionIdId() {
        val LargeBlockLockerRoom = create(this, DefinitionIdTypes.LargeBlockLockerRoom)
        val LargeBlockLockerRoomCorner = create(this, DefinitionIdTypes.LargeBlockLockerRoomCorner)
        val LargeBlockLockers = create(this, DefinitionIdTypes.LargeBlockLockers)
        val LargeBlockLargeIndustrialContainer = create(this, DefinitionIdTypes.LargeBlockLargeIndustrialContainer)
        val SmallBlockSmallContainer = create(this, DefinitionIdTypes.SmallBlockSmallContainer)
        val SmallBlockMediumContainer = create(this, DefinitionIdTypes.SmallBlockMediumContainer)
        val SmallBlockLargeContainer = create(this, DefinitionIdTypes.SmallBlockLargeContainer)
        val LargeBlockSmallContainer = create(this, DefinitionIdTypes.LargeBlockSmallContainer)
        val LargeBlockLargeContainer = create(this, DefinitionIdTypes.LargeBlockLargeContainer)
        val LargeBlockWeaponRack = create(this, DefinitionIdTypes.LargeBlockWeaponRack)
        val SmallBlockWeaponRack = create(this, DefinitionIdTypes.SmallBlockWeaponRack)
    }

    object Planter : SafeDefinitionIdId() {
        val LargeBlockPlanters = create(this, DefinitionIdTypes.LargeBlockPlanters)
    }

    object VendingMachine : SafeDefinitionIdId() {
        val FoodDispenser = create(this, DefinitionIdTypes.FoodDispenser)
        val VendingMachine = create(this, DefinitionIdTypes.VendingMachine)
    }

    object Jukebox : SafeDefinitionIdId() {
        val Jukebox = create(this, DefinitionIdTypes.Jukebox)
    }

    object LCDPanelsBlock : SafeDefinitionIdId() {
        val LabEquipment = create(this, DefinitionIdTypes.LabEquipment)
        val MedicalStation = create(this, DefinitionIdTypes.MedicalStation)
    }

    object TextPanel : SafeDefinitionIdId() {
        val TransparentLCDLarge = create(this, DefinitionIdTypes.TransparentLCDLarge)
        val TransparentLCDSmall = create(this, DefinitionIdTypes.TransparentLCDSmall)
        val SmallTextPanel = create(this, DefinitionIdTypes.SmallTextPanel)
        val SmallLCDPanelWide = create(this, DefinitionIdTypes.SmallLCDPanelWide)
        val SmallLCDPanel = create(this, DefinitionIdTypes.SmallLCDPanel)
        val LargeBlockCorner_LCD_1 = create(this, DefinitionIdTypes.LargeBlockCorner_LCD_1)
        val LargeBlockCorner_LCD_2 = create(this, DefinitionIdTypes.LargeBlockCorner_LCD_2)
        val LargeBlockCorner_LCD_Flat_1 = create(this, DefinitionIdTypes.LargeBlockCorner_LCD_Flat_1)
        val LargeBlockCorner_LCD_Flat_2 = create(this, DefinitionIdTypes.LargeBlockCorner_LCD_Flat_2)
        val SmallBlockCorner_LCD_1 = create(this, DefinitionIdTypes.SmallBlockCorner_LCD_1)
        val SmallBlockCorner_LCD_2 = create(this, DefinitionIdTypes.SmallBlockCorner_LCD_2)
        val SmallBlockCorner_LCD_Flat_1 = create(this, DefinitionIdTypes.SmallBlockCorner_LCD_Flat_1)
        val SmallBlockCorner_LCD_Flat_2 = create(this, DefinitionIdTypes.SmallBlockCorner_LCD_Flat_2)
        val LargeTextPanel = create(this, DefinitionIdTypes.LargeTextPanel)
        val LargeLCDPanel = create(this, DefinitionIdTypes.LargeLCDPanel)
        val LargeLCDPanelWide = create(this, DefinitionIdTypes.LargeLCDPanelWide)
        val LargeLCDPanel5x5 = create(this, DefinitionIdTypes.LargeLCDPanel5x5)
        val LargeLCDPanel5x3 = create(this, DefinitionIdTypes.LargeLCDPanel5x3)
        val LargeLCDPanel3x3 = create(this, DefinitionIdTypes.LargeLCDPanel3x3)
    }

    object ReflectorLight : SafeDefinitionIdId() {
        val RotatingLightLarge = create(this, DefinitionIdTypes.RotatingLightLarge)
        val RotatingLightSmall = create(this, DefinitionIdTypes.RotatingLightSmall)
        val LargeBlockFrontLight = create(this, DefinitionIdTypes.LargeBlockFrontLight)
        val SmallBlockFrontLight = create(this, DefinitionIdTypes.SmallBlockFrontLight)
        val OffsetSpotlight = create(this, DefinitionIdTypes.OffsetSpotlight)
    }

    object Door : SafeDefinitionIdId() {
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
        val SmallDoor = create(this, DefinitionIdTypes.SmallDoor)
        val LargeBlockGate = create(this, DefinitionIdTypes.LargeBlockGate)
        val LargeBlockOffsetDoor = create(this, DefinitionIdTypes.LargeBlockOffsetDoor)
        val SmallSideDoor = create(this, DefinitionIdTypes.SmallSideDoor)
        val SlidingHatchDoor = create(this, DefinitionIdTypes.SlidingHatchDoor)
        val SlidingHatchDoorHalf = create(this, DefinitionIdTypes.SlidingHatchDoorHalf)
    }

    object AirtightHangarDoor : SafeDefinitionIdId() {
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
        val AirtightHangarDoorWarfare2A = create(this, DefinitionIdTypes.AirtightHangarDoorWarfare2A)
        val AirtightHangarDoorWarfare2B = create(this, DefinitionIdTypes.AirtightHangarDoorWarfare2B)
        val AirtightHangarDoorWarfare2C = create(this, DefinitionIdTypes.AirtightHangarDoorWarfare2C)
    }

    object AirtightSlideDoor : SafeDefinitionIdId() {
        val LargeBlockSlideDoor = create(this, DefinitionIdTypes.LargeBlockSlideDoor)
    }

    object StoreBlock : SafeDefinitionIdId() {
        val StoreBlock = create(this, DefinitionIdTypes.StoreBlock)
        val AtmBlock = create(this, DefinitionIdTypes.AtmBlock)
    }

    object SafeZoneBlock : SafeDefinitionIdId() {
        val SafeZoneBlock = create(this, DefinitionIdTypes.SafeZoneBlock)
    }

    object ContractBlock : SafeDefinitionIdId() {
        val ContractBlock = create(this, DefinitionIdTypes.ContractBlock)
    }

    object BatteryBlock : SafeDefinitionIdId() {
        val LargeBlockBatteryBlock = create(this, DefinitionIdTypes.LargeBlockBatteryBlock)
        val SmallBlockBatteryBlock = create(this, DefinitionIdTypes.SmallBlockBatteryBlock)
        val SmallBlockSmallBatteryBlock = create(this, DefinitionIdTypes.SmallBlockSmallBatteryBlock)
        val LargeBlockBatteryBlockWarfare2 = create(this, DefinitionIdTypes.LargeBlockBatteryBlockWarfare2)
        val SmallBlockBatteryBlockWarfare2 = create(this, DefinitionIdTypes.SmallBlockBatteryBlockWarfare2)
    }

    object Reactor : SafeDefinitionIdId() {
        val SmallBlockSmallGenerator = create(this, DefinitionIdTypes.SmallBlockSmallGenerator)
        val SmallBlockLargeGenerator = create(this, DefinitionIdTypes.SmallBlockLargeGenerator)
        val LargeBlockSmallGenerator = create(this, DefinitionIdTypes.LargeBlockSmallGenerator)
        val LargeBlockLargeGenerator = create(this, DefinitionIdTypes.LargeBlockLargeGenerator)
        val LargeBlockSmallGeneratorWarfare2 = create(this, DefinitionIdTypes.LargeBlockSmallGeneratorWarfare2)
        val LargeBlockLargeGeneratorWarfare2 = create(this, DefinitionIdTypes.LargeBlockLargeGeneratorWarfare2)
        val SmallBlockSmallGeneratorWarfare2 = create(this, DefinitionIdTypes.SmallBlockSmallGeneratorWarfare2)
        val SmallBlockLargeGeneratorWarfare2 = create(this, DefinitionIdTypes.SmallBlockLargeGeneratorWarfare2)
    }

    object HydrogenEngine : SafeDefinitionIdId() {
        val LargeHydrogenEngine = create(this, DefinitionIdTypes.LargeHydrogenEngine)
        val SmallHydrogenEngine = create(this, DefinitionIdTypes.SmallHydrogenEngine)
    }

    object WindTurbine : SafeDefinitionIdId() {
        val LargeBlockWindTurbine = create(this, DefinitionIdTypes.LargeBlockWindTurbine)
    }

    object SolarPanel : SafeDefinitionIdId() {
        val LargeBlockSolarPanel = create(this, DefinitionIdTypes.LargeBlockSolarPanel)
        val SmallBlockSolarPanel = create(this, DefinitionIdTypes.SmallBlockSolarPanel)
    }

    object GravityGenerator : SafeDefinitionIdId() {
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
    }

    object GravityGeneratorSphere : SafeDefinitionIdId() {
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
    }

    object VirtualMass : SafeDefinitionIdId() {
        val VirtualMassLarge = create(this, DefinitionIdTypes.VirtualMassLarge)
        val VirtualMassSmall = create(this, DefinitionIdTypes.VirtualMassSmall)
    }

    object SpaceBall : SafeDefinitionIdId() {
        val SpaceBallLarge = create(this, DefinitionIdTypes.SpaceBallLarge)
        val SpaceBallSmall = create(this, DefinitionIdTypes.SpaceBallSmall)
    }

    object LandingGear : SafeDefinitionIdId() {
        val LargeBlockMagneticPlate = create(this, DefinitionIdTypes.LargeBlockMagneticPlate)
        val SmallBlockMagneticPlate = create(this, DefinitionIdTypes.SmallBlockMagneticPlate)
        val LargeBlockLandingGear = create(this, DefinitionIdTypes.LargeBlockLandingGear)
        val SmallBlockLandingGear = create(this, DefinitionIdTypes.SmallBlockLandingGear)
        val LargeBlockSmallMagneticPlate = create(this, DefinitionIdTypes.LargeBlockSmallMagneticPlate)
        val SmallBlockSmallMagneticPlate = create(this, DefinitionIdTypes.SmallBlockSmallMagneticPlate)
    }

    object ConveyorConnector : SafeDefinitionIdId() {
        val LargeBlockConveyorPipeSeamless = create(this, DefinitionIdTypes.LargeBlockConveyorPipeSeamless)
        val LargeBlockConveyorPipeCorner = create(this, DefinitionIdTypes.LargeBlockConveyorPipeCorner)
        val LargeBlockConveyorPipeFlange = create(this, DefinitionIdTypes.LargeBlockConveyorPipeFlange)
        val LargeBlockConveyorPipeEnd = create(this, DefinitionIdTypes.LargeBlockConveyorPipeEnd)
        val ConveyorTube = create(this, DefinitionIdTypes.ConveyorTube)
        val ConveyorTubeDuct = create(this, DefinitionIdTypes.ConveyorTubeDuct)
        val ConveyorTubeSmall = create(this, DefinitionIdTypes.ConveyorTubeSmall)
        val ConveyorTubeDuctSmall = create(this, DefinitionIdTypes.ConveyorTubeDuctSmall)
        val ConveyorTubeMedium = create(this, DefinitionIdTypes.ConveyorTubeMedium)
        val ConveyorFrameMedium = create(this, DefinitionIdTypes.ConveyorFrameMedium)
        val ConveyorTubeCurved = create(this, DefinitionIdTypes.ConveyorTubeCurved)
        val ConveyorTubeSmallCurved = create(this, DefinitionIdTypes.ConveyorTubeSmallCurved)
        val ConveyorTubeCurvedMedium = create(this, DefinitionIdTypes.ConveyorTubeCurvedMedium)
    }

    object Conveyor : SafeDefinitionIdId() {
        val LargeBlockConveyorPipeJunction = create(this, DefinitionIdTypes.LargeBlockConveyorPipeJunction)
        val LargeBlockConveyorPipeIntersection = create(this, DefinitionIdTypes.LargeBlockConveyorPipeIntersection)
        val LargeBlockConveyorPipeT = create(this, DefinitionIdTypes.LargeBlockConveyorPipeT)
        val SmallBlockConveyor = create(this, DefinitionIdTypes.SmallBlockConveyor)
        val SmallBlockConveyorConverter = create(this, DefinitionIdTypes.SmallBlockConveyorConverter)
        val LargeBlockConveyor = create(this, DefinitionIdTypes.LargeBlockConveyor)
        val SmallShipConveyorHub = create(this, DefinitionIdTypes.SmallShipConveyorHub)
        val ConveyorTubeSmallT = create(this, DefinitionIdTypes.ConveyorTubeSmallT)
        val ConveyorTubeT = create(this, DefinitionIdTypes.ConveyorTubeT)
    }

    object OxygenTank : SafeDefinitionIdId() {
        val LargeHydrogenTankIndustrial = create(this, DefinitionIdTypes.LargeHydrogenTankIndustrial)
        val OxygenTankSmall = create(this, DefinitionIdTypes.OxygenTankSmall)
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
        val LargeHydrogenTank = create(this, DefinitionIdTypes.LargeHydrogenTank)
        val LargeHydrogenTankSmall = create(this, DefinitionIdTypes.LargeHydrogenTankSmall)
        val SmallHydrogenTank = create(this, DefinitionIdTypes.SmallHydrogenTank)
        val SmallHydrogenTankSmall = create(this, DefinitionIdTypes.SmallHydrogenTankSmall)
    }

    object Assembler : SafeDefinitionIdId() {
        val LargeAssemblerIndustrial = create(this, DefinitionIdTypes.LargeAssemblerIndustrial)
        val LargeAssembler = create(this, DefinitionIdTypes.LargeAssembler)
        val BasicAssembler = create(this, DefinitionIdTypes.BasicAssembler)
    }

    object Refinery : SafeDefinitionIdId() {
        val LargeRefineryIndustrial = create(this, DefinitionIdTypes.LargeRefineryIndustrial)
        val LargeRefinery = create(this, DefinitionIdTypes.LargeRefinery)
        val Blast_Furnace = create(this, DefinitionIdTypes.Blast_Furnace)
    }

    object ConveyorSorter : SafeDefinitionIdId() {
        val LargeBlockConveyorSorterIndustrial = create(this, DefinitionIdTypes.LargeBlockConveyorSorterIndustrial)
        val LargeBlockConveyorSorter = create(this, DefinitionIdTypes.LargeBlockConveyorSorter)
        val MediumBlockConveyorSorter = create(this, DefinitionIdTypes.MediumBlockConveyorSorter)
        val SmallBlockConveyorSorter = create(this, DefinitionIdTypes.SmallBlockConveyorSorter)
    }

    object Thrust : SafeDefinitionIdId() {
        val LargeBlockLargeHydrogenThrustIndustrial =
            create(this, DefinitionIdTypes.LargeBlockLargeHydrogenThrustIndustrial)
        val LargeBlockSmallHydrogenThrustIndustrial =
            create(this, DefinitionIdTypes.LargeBlockSmallHydrogenThrustIndustrial)
        val SmallBlockLargeHydrogenThrustIndustrial =
            create(this, DefinitionIdTypes.SmallBlockLargeHydrogenThrustIndustrial)
        val SmallBlockSmallHydrogenThrustIndustrial =
            create(this, DefinitionIdTypes.SmallBlockSmallHydrogenThrustIndustrial)
        val SmallBlockSmallThrustSciFi = create(this, DefinitionIdTypes.SmallBlockSmallThrustSciFi)
        val SmallBlockLargeThrustSciFi = create(this, DefinitionIdTypes.SmallBlockLargeThrustSciFi)
        val LargeBlockSmallThrustSciFi = create(this, DefinitionIdTypes.LargeBlockSmallThrustSciFi)
        val LargeBlockLargeThrustSciFi = create(this, DefinitionIdTypes.LargeBlockLargeThrustSciFi)
        val LargeBlockLargeAtmosphericThrustSciFi =
            create(this, DefinitionIdTypes.LargeBlockLargeAtmosphericThrustSciFi)
        val LargeBlockSmallAtmosphericThrustSciFi =
            create(this, DefinitionIdTypes.LargeBlockSmallAtmosphericThrustSciFi)
        val SmallBlockLargeAtmosphericThrustSciFi =
            create(this, DefinitionIdTypes.SmallBlockLargeAtmosphericThrustSciFi)
        val SmallBlockSmallAtmosphericThrustSciFi =
            create(this, DefinitionIdTypes.SmallBlockSmallAtmosphericThrustSciFi)
        val SmallBlockSmallThrust = create(this, DefinitionIdTypes.SmallBlockSmallThrust)
        val SmallBlockLargeThrust = create(this, DefinitionIdTypes.SmallBlockLargeThrust)
        val LargeBlockSmallThrust = create(this, DefinitionIdTypes.LargeBlockSmallThrust)
        val LargeBlockLargeThrust = create(this, DefinitionIdTypes.LargeBlockLargeThrust)
        val LargeBlockLargeHydrogenThrust = create(this, DefinitionIdTypes.LargeBlockLargeHydrogenThrust)
        val LargeBlockSmallHydrogenThrust = create(this, DefinitionIdTypes.LargeBlockSmallHydrogenThrust)
        val SmallBlockLargeHydrogenThrust = create(this, DefinitionIdTypes.SmallBlockLargeHydrogenThrust)
        val SmallBlockSmallHydrogenThrust = create(this, DefinitionIdTypes.SmallBlockSmallHydrogenThrust)
        val LargeBlockLargeAtmosphericThrust = create(this, DefinitionIdTypes.LargeBlockLargeAtmosphericThrust)
        val LargeBlockSmallAtmosphericThrust = create(this, DefinitionIdTypes.LargeBlockSmallAtmosphericThrust)
        val SmallBlockLargeAtmosphericThrust = create(this, DefinitionIdTypes.SmallBlockLargeAtmosphericThrust)
        val SmallBlockSmallAtmosphericThrust = create(this, DefinitionIdTypes.SmallBlockSmallAtmosphericThrust)
        val SmallBlockSmallModularThruster = create(this, DefinitionIdTypes.SmallBlockSmallModularThruster)
        val SmallBlockLargeModularThruster = create(this, DefinitionIdTypes.SmallBlockLargeModularThruster)
        val LargeBlockSmallModularThruster = create(this, DefinitionIdTypes.LargeBlockSmallModularThruster)
        val LargeBlockLargeModularThruster = create(this, DefinitionIdTypes.LargeBlockLargeModularThruster)
    }

    object Passage : SafeDefinitionIdId() {
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
    }

    object Ladder2 : SafeDefinitionIdId() {
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
        val LadderShaft = create(this, DefinitionIdTypes.LadderShaft)
        val LadderSmall = create(this, DefinitionIdTypes.LadderSmall)
    }

    object InteriorLight : SafeDefinitionIdId() {
        val SmallLight = create(this, DefinitionIdTypes.SmallLight)
        val SmallBlockSmallLight = create(this, DefinitionIdTypes.SmallBlockSmallLight)
        val LargeBlockLight_1corner = create(this, DefinitionIdTypes.LargeBlockLight_1corner)
        val LargeBlockLight_2corner = create(this, DefinitionIdTypes.LargeBlockLight_2corner)
        val SmallBlockLight_1corner = create(this, DefinitionIdTypes.SmallBlockLight_1corner)
        val SmallBlockLight_2corner = create(this, DefinitionIdTypes.SmallBlockLight_2corner)
        val OffsetLight = create(this, DefinitionIdTypes.OffsetLight)
        val PassageSciFiLight = create(this, DefinitionIdTypes.PassageSciFiLight)
        val LargeLightPanel = create(this, DefinitionIdTypes.LargeLightPanel)
        val SmallLightPanel = create(this, DefinitionIdTypes.SmallLightPanel)
    }

    object AirVent : SafeDefinitionIdId() {
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
        val AirVentFull = create(this, DefinitionIdTypes.AirVentFull)
        val SmallAirVent = create(this, DefinitionIdTypes.SmallAirVent)
        val SmallAirVentFull = create(this, DefinitionIdTypes.SmallAirVentFull)
    }

    object Collector : SafeDefinitionIdId() {
        val Collector = create(this, DefinitionIdTypes.Collector)
        val CollectorSmall = create(this, DefinitionIdTypes.CollectorSmall)
    }

    object ShipConnector : SafeDefinitionIdId() {
        val Connector = create(this, DefinitionIdTypes.Connector)
        val ConnectorSmall = create(this, DefinitionIdTypes.ConnectorSmall)
        val ConnectorMedium = create(this, DefinitionIdTypes.ConnectorMedium)
    }

    object PistonBase : SafeDefinitionIdId() {
        val LargePistonBase = create(this, DefinitionIdTypes.LargePistonBase)
        val SmallPistonBase = create(this, DefinitionIdTypes.SmallPistonBase)
    }

    object ExtendedPistonBase : SafeDefinitionIdId() {
        val LargePistonBase = create(this, DefinitionIdTypes.LargePistonBase)
        val SmallPistonBase = create(this, DefinitionIdTypes.SmallPistonBase)
    }

    object PistonTop : SafeDefinitionIdId() {
        val LargePistonTop = create(this, DefinitionIdTypes.LargePistonTop)
        val SmallPistonTop = create(this, DefinitionIdTypes.SmallPistonTop)
    }

    object MotorStator : SafeDefinitionIdId() {
        val LargeStator = create(this, DefinitionIdTypes.LargeStator)
        val SmallStator = create(this, DefinitionIdTypes.SmallStator)
    }

    object MotorRotor : SafeDefinitionIdId() {
        val LargeRotor = create(this, DefinitionIdTypes.LargeRotor)
        val SmallRotor = create(this, DefinitionIdTypes.SmallRotor)
    }

    object MotorAdvancedStator : SafeDefinitionIdId() {
        val LargeAdvancedStator = create(this, DefinitionIdTypes.LargeAdvancedStator)
        val SmallAdvancedStator = create(this, DefinitionIdTypes.SmallAdvancedStator)
        val SmallAdvancedStatorSmall = create(this, DefinitionIdTypes.SmallAdvancedStatorSmall)
        val LargeHinge = create(this, DefinitionIdTypes.LargeHinge)
        val MediumHinge = create(this, DefinitionIdTypes.MediumHinge)
        val SmallHinge = create(this, DefinitionIdTypes.SmallHinge)
    }

    object MotorAdvancedRotor : SafeDefinitionIdId() {
        val LargeAdvancedRotor = create(this, DefinitionIdTypes.LargeAdvancedRotor)
        val SmallAdvancedRotor = create(this, DefinitionIdTypes.SmallAdvancedRotor)
        val SmallAdvancedRotorSmall = create(this, DefinitionIdTypes.SmallAdvancedRotorSmall)
        val LargeHingeHead = create(this, DefinitionIdTypes.LargeHingeHead)
        val MediumHingeHead = create(this, DefinitionIdTypes.MediumHingeHead)
        val SmallHingeHead = create(this, DefinitionIdTypes.SmallHingeHead)
    }

    object MedicalRoom : SafeDefinitionIdId() {
        val LargeMedicalRoom = create(this, DefinitionIdTypes.LargeMedicalRoom)
    }

    object OxygenGenerator : SafeDefinitionIdId() {
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
        val OxygenGeneratorSmall = create(this, DefinitionIdTypes.OxygenGeneratorSmall)
    }

    object SurvivalKit : SafeDefinitionIdId() {
        val SurvivalKitLarge = create(this, DefinitionIdTypes.SurvivalKitLarge)
        val SurvivalKit = create(this, DefinitionIdTypes.SurvivalKit)
    }

    object OxygenFarm : SafeDefinitionIdId() {
        val LargeBlockOxygenFarm = create(this, DefinitionIdTypes.LargeBlockOxygenFarm)
    }

    object UpgradeModule : SafeDefinitionIdId() {
        val LargeProductivityModule = create(this, DefinitionIdTypes.LargeProductivityModule)
        val LargeEffectivenessModule = create(this, DefinitionIdTypes.LargeEffectivenessModule)
        val LargeEnergyModule = create(this, DefinitionIdTypes.LargeEnergyModule)
    }

    object ExhaustBlock : SafeDefinitionIdId() {
        val SmallExhaustPipe = create(this, DefinitionIdTypes.SmallExhaustPipe)
        val LargeExhaustPipe = create(this, DefinitionIdTypes.LargeExhaustPipe)
    }

    object MotorSuspension : SafeDefinitionIdId() {
        val OffroadSuspension3x3 = create(this, DefinitionIdTypes.OffroadSuspension3x3)
        val OffroadSuspension5x5 = create(this, DefinitionIdTypes.OffroadSuspension5x5)
        val OffroadSuspension1x1 = create(this, DefinitionIdTypes.OffroadSuspension1x1)
        val OffroadSuspension2x2 = create(this, DefinitionIdTypes.OffroadSuspension2x2)
        val OffroadSmallSuspension3x3 = create(this, DefinitionIdTypes.OffroadSmallSuspension3x3)
        val OffroadSmallSuspension5x5 = create(this, DefinitionIdTypes.OffroadSmallSuspension5x5)
        val OffroadSmallSuspension1x1 = create(this, DefinitionIdTypes.OffroadSmallSuspension1x1)
        val OffroadSmallSuspension2x2 = create(this, DefinitionIdTypes.OffroadSmallSuspension2x2)
        val OffroadSuspension3x3mirrored = create(this, DefinitionIdTypes.OffroadSuspension3x3mirrored)
        val OffroadSuspension5x5mirrored = create(this, DefinitionIdTypes.OffroadSuspension5x5mirrored)
        val OffroadSuspension1x1mirrored = create(this, DefinitionIdTypes.OffroadSuspension1x1mirrored)
        val OffroadSuspension2x2Mirrored = create(this, DefinitionIdTypes.OffroadSuspension2x2Mirrored)
        val OffroadSmallSuspension3x3mirrored = create(this, DefinitionIdTypes.OffroadSmallSuspension3x3mirrored)
        val OffroadSmallSuspension5x5mirrored = create(this, DefinitionIdTypes.OffroadSmallSuspension5x5mirrored)
        val OffroadSmallSuspension1x1mirrored = create(this, DefinitionIdTypes.OffroadSmallSuspension1x1mirrored)
        val OffroadSmallSuspension2x2Mirrored = create(this, DefinitionIdTypes.OffroadSmallSuspension2x2Mirrored)
        val Suspension3x3 = create(this, DefinitionIdTypes.Suspension3x3)
        val Suspension5x5 = create(this, DefinitionIdTypes.Suspension5x5)
        val Suspension1x1 = create(this, DefinitionIdTypes.Suspension1x1)
        val Suspension2x2 = create(this, DefinitionIdTypes.Suspension2x2)
        val SmallSuspension3x3 = create(this, DefinitionIdTypes.SmallSuspension3x3)
        val SmallSuspension5x5 = create(this, DefinitionIdTypes.SmallSuspension5x5)
        val SmallSuspension1x1 = create(this, DefinitionIdTypes.SmallSuspension1x1)
        val SmallSuspension2x2 = create(this, DefinitionIdTypes.SmallSuspension2x2)
        val Suspension3x3mirrored = create(this, DefinitionIdTypes.Suspension3x3mirrored)
        val Suspension5x5mirrored = create(this, DefinitionIdTypes.Suspension5x5mirrored)
        val Suspension1x1mirrored = create(this, DefinitionIdTypes.Suspension1x1mirrored)
        val Suspension2x2Mirrored = create(this, DefinitionIdTypes.Suspension2x2Mirrored)
        val SmallSuspension3x3mirrored = create(this, DefinitionIdTypes.SmallSuspension3x3mirrored)
        val SmallSuspension5x5mirrored = create(this, DefinitionIdTypes.SmallSuspension5x5mirrored)
        val SmallSuspension1x1mirrored = create(this, DefinitionIdTypes.SmallSuspension1x1mirrored)
        val SmallSuspension2x2Mirrored = create(this, DefinitionIdTypes.SmallSuspension2x2Mirrored)
    }

    object Wheel : SafeDefinitionIdId() {
        val OffroadSmallRealWheel1x1 = create(this, DefinitionIdTypes.OffroadSmallRealWheel1x1)
        val OffroadSmallRealWheel2x2 = create(this, DefinitionIdTypes.OffroadSmallRealWheel2x2)
        val OffroadSmallRealWheel = create(this, DefinitionIdTypes.OffroadSmallRealWheel)
        val OffroadSmallRealWheel5x5 = create(this, DefinitionIdTypes.OffroadSmallRealWheel5x5)
        val OffroadRealWheel1x1 = create(this, DefinitionIdTypes.OffroadRealWheel1x1)
        val OffroadRealWheel2x2 = create(this, DefinitionIdTypes.OffroadRealWheel2x2)
        val OffroadRealWheel = create(this, DefinitionIdTypes.OffroadRealWheel)
        val OffroadRealWheel5x5 = create(this, DefinitionIdTypes.OffroadRealWheel5x5)
        val OffroadSmallRealWheel1x1mirrored = create(this, DefinitionIdTypes.OffroadSmallRealWheel1x1mirrored)
        val OffroadSmallRealWheel2x2Mirrored = create(this, DefinitionIdTypes.OffroadSmallRealWheel2x2Mirrored)
        val OffroadSmallRealWheelmirrored = create(this, DefinitionIdTypes.OffroadSmallRealWheelmirrored)
        val OffroadSmallRealWheel5x5mirrored = create(this, DefinitionIdTypes.OffroadSmallRealWheel5x5mirrored)
        val OffroadRealWheel1x1mirrored = create(this, DefinitionIdTypes.OffroadRealWheel1x1mirrored)
        val OffroadRealWheel2x2Mirrored = create(this, DefinitionIdTypes.OffroadRealWheel2x2Mirrored)
        val OffroadRealWheelmirrored = create(this, DefinitionIdTypes.OffroadRealWheelmirrored)
        val OffroadRealWheel5x5mirrored = create(this, DefinitionIdTypes.OffroadRealWheel5x5mirrored)
        val OffroadWheel1x1 = create(this, DefinitionIdTypes.OffroadWheel1x1)
        val OffroadSmallWheel1x1 = create(this, DefinitionIdTypes.OffroadSmallWheel1x1)
        val OffroadWheel3x3 = create(this, DefinitionIdTypes.OffroadWheel3x3)
        val OffroadSmallWheel3x3 = create(this, DefinitionIdTypes.OffroadSmallWheel3x3)
        val OffroadWheel5x5 = create(this, DefinitionIdTypes.OffroadWheel5x5)
        val OffroadSmallWheel5x5 = create(this, DefinitionIdTypes.OffroadSmallWheel5x5)
        val OffroadWheel2x2 = create(this, DefinitionIdTypes.OffroadWheel2x2)
        val OffroadSmallWheel2x2 = create(this, DefinitionIdTypes.OffroadSmallWheel2x2)
        val SmallRealWheel1x1 = create(this, DefinitionIdTypes.SmallRealWheel1x1)
        val SmallRealWheel2x2 = create(this, DefinitionIdTypes.SmallRealWheel2x2)
        val SmallRealWheel = create(this, DefinitionIdTypes.SmallRealWheel)
        val SmallRealWheel5x5 = create(this, DefinitionIdTypes.SmallRealWheel5x5)
        val RealWheel1x1 = create(this, DefinitionIdTypes.RealWheel1x1)
        val RealWheel2x2 = create(this, DefinitionIdTypes.RealWheel2x2)
        val RealWheel = create(this, DefinitionIdTypes.RealWheel)
        val RealWheel5x5 = create(this, DefinitionIdTypes.RealWheel5x5)
        val SmallRealWheel1x1mirrored = create(this, DefinitionIdTypes.SmallRealWheel1x1mirrored)
        val SmallRealWheel2x2Mirrored = create(this, DefinitionIdTypes.SmallRealWheel2x2Mirrored)
        val SmallRealWheelmirrored = create(this, DefinitionIdTypes.SmallRealWheelmirrored)
        val SmallRealWheel5x5mirrored = create(this, DefinitionIdTypes.SmallRealWheel5x5mirrored)
        val RealWheel1x1mirrored = create(this, DefinitionIdTypes.RealWheel1x1mirrored)
        val RealWheel2x2Mirrored = create(this, DefinitionIdTypes.RealWheel2x2Mirrored)
        val RealWheelmirrored = create(this, DefinitionIdTypes.RealWheelmirrored)
        val RealWheel5x5mirrored = create(this, DefinitionIdTypes.RealWheel5x5mirrored)
        val Wheel1x1 = create(this, DefinitionIdTypes.Wheel1x1)
        val SmallWheel1x1 = create(this, DefinitionIdTypes.SmallWheel1x1)
        val Wheel3x3 = create(this, DefinitionIdTypes.Wheel3x3)
        val SmallWheel3x3 = create(this, DefinitionIdTypes.SmallWheel3x3)
        val Wheel5x5 = create(this, DefinitionIdTypes.Wheel5x5)
        val SmallWheel5x5 = create(this, DefinitionIdTypes.SmallWheel5x5)
        val Wheel2x2 = create(this, DefinitionIdTypes.Wheel2x2)
        val SmallWheel2x2 = create(this, DefinitionIdTypes.SmallWheel2x2)
    }

    object EmissiveBlock : SafeDefinitionIdId() {
        val LargeNeonTubesStraight1 = create(this, DefinitionIdTypes.LargeNeonTubesStraight1)
        val LargeNeonTubesStraight2 = create(this, DefinitionIdTypes.LargeNeonTubesStraight2)
        val LargeNeonTubesCorner = create(this, DefinitionIdTypes.LargeNeonTubesCorner)
        val LargeNeonTubesBendUp = create(this, DefinitionIdTypes.LargeNeonTubesBendUp)
        val LargeNeonTubesBendDown = create(this, DefinitionIdTypes.LargeNeonTubesBendDown)
        val LargeNeonTubesStraightEnd1 = create(this, DefinitionIdTypes.LargeNeonTubesStraightEnd1)
        val LargeNeonTubesStraightEnd2 = create(this, DefinitionIdTypes.LargeNeonTubesStraightEnd2)
        val LargeNeonTubesStraightDown = create(this, DefinitionIdTypes.LargeNeonTubesStraightDown)
        val LargeNeonTubesU = create(this, DefinitionIdTypes.LargeNeonTubesU)
        val LargeNeonTubesT = create(this, DefinitionIdTypes.LargeNeonTubesT)
        val LargeNeonTubesCircle = create(this, DefinitionIdTypes.LargeNeonTubesCircle)
        val SmallNeonTubesStraight1 = create(this, DefinitionIdTypes.SmallNeonTubesStraight1)
        val SmallNeonTubesStraight2 = create(this, DefinitionIdTypes.SmallNeonTubesStraight2)
        val SmallNeonTubesCorner = create(this, DefinitionIdTypes.SmallNeonTubesCorner)
        val SmallNeonTubesBendUp = create(this, DefinitionIdTypes.SmallNeonTubesBendUp)
        val SmallNeonTubesBendDown = create(this, DefinitionIdTypes.SmallNeonTubesBendDown)
        val SmallNeonTubesStraightDown = create(this, DefinitionIdTypes.SmallNeonTubesStraightDown)
        val SmallNeonTubesStraightEnd1 = create(this, DefinitionIdTypes.SmallNeonTubesStraightEnd1)
        val SmallNeonTubesU = create(this, DefinitionIdTypes.SmallNeonTubesU)
        val SmallNeonTubesT = create(this, DefinitionIdTypes.SmallNeonTubesT)
        val SmallNeonTubesCircle = create(this, DefinitionIdTypes.SmallNeonTubesCircle)
    }

    object Drill : SafeDefinitionIdId() {
        val SmallBlockDrill = create(this, DefinitionIdTypes.SmallBlockDrill)
        val LargeBlockDrill = create(this, DefinitionIdTypes.LargeBlockDrill)
    }

    object ShipGrinder : SafeDefinitionIdId() {
        val LargeShipGrinder = create(this, DefinitionIdTypes.LargeShipGrinder)
        val SmallShipGrinder = create(this, DefinitionIdTypes.SmallShipGrinder)
    }

    object ShipWelder : SafeDefinitionIdId() {
        val LargeShipWelder = create(this, DefinitionIdTypes.LargeShipWelder)
        val SmallShipWelder = create(this, DefinitionIdTypes.SmallShipWelder)
    }

    object OreDetector : SafeDefinitionIdId() {
        val LargeOreDetector = create(this, DefinitionIdTypes.LargeOreDetector)
        val SmallBlockOreDetector = create(this, DefinitionIdTypes.SmallBlockOreDetector)
    }

    object JumpDrive : SafeDefinitionIdId() {
        val LargeJumpDrive = create(this, DefinitionIdTypes.LargeJumpDrive)
    }

    object CameraBlock : SafeDefinitionIdId() {
        val SmallCameraBlock = create(this, DefinitionIdTypes.SmallCameraBlock)
        val LargeCameraBlock = create(this, DefinitionIdTypes.LargeCameraBlock)
    }

    object MergeBlock : SafeDefinitionIdId() {
        val LargeShipMergeBlock = create(this, DefinitionIdTypes.LargeShipMergeBlock)
        val SmallShipMergeBlock = create(this, DefinitionIdTypes.SmallShipMergeBlock)
        val SmallShipSmallMergeBlock = create(this, DefinitionIdTypes.SmallShipSmallMergeBlock)
    }

    object Parachute : SafeDefinitionIdId() {
        val LgParachute = create(this, DefinitionIdTypes.LgParachute)
        val SmParachute = create(this, DefinitionIdTypes.SmParachute)
    }

    object SmallMissileLauncher : SafeDefinitionIdId() {
        val SmallMissileLauncherWarfare2 = create(this, DefinitionIdTypes.SmallMissileLauncherWarfare2)
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
        val LargeMissileLauncher = create(this, DefinitionIdTypes.LargeMissileLauncher)
        val LargeBlockLargeCalibreGun = create(this, DefinitionIdTypes.LargeBlockLargeCalibreGun)
    }

    object SmallGatlingGun : SafeDefinitionIdId() {
        val SmallGatlingGunWarfare2 = create(this, DefinitionIdTypes.SmallGatlingGunWarfare2)
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
        val SmallBlockAutocannon = create(this, DefinitionIdTypes.SmallBlockAutocannon)
    }

    object Searchlight : SafeDefinitionIdId() {
        val SmallSearchlight = create(this, DefinitionIdTypes.SmallSearchlight)
        val LargeSearchlight = create(this, DefinitionIdTypes.LargeSearchlight)
    }

    object HeatVentBlock : SafeDefinitionIdId() {
        val LargeHeatVentBlock = create(this, DefinitionIdTypes.LargeHeatVentBlock)
        val SmallHeatVentBlock = create(this, DefinitionIdTypes.SmallHeatVentBlock)
    }

    object Warhead : SafeDefinitionIdId() {
        val LargeWarhead = create(this, DefinitionIdTypes.LargeWarhead)
        val SmallWarhead = create(this, DefinitionIdTypes.SmallWarhead)
    }

    object Decoy : SafeDefinitionIdId() {
        val LargeDecoy = create(this, DefinitionIdTypes.LargeDecoy)
        val SmallDecoy = create(this, DefinitionIdTypes.SmallDecoy)
    }

    object LargeGatlingTurret : SafeDefinitionIdId() {
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
        val SmallGatlingTurret = create(this, DefinitionIdTypes.SmallGatlingTurret)
        val AutoCannonTurret = create(this, DefinitionIdTypes.AutoCannonTurret)
    }

    object LargeMissileTurret : SafeDefinitionIdId() {
        val EMPTY = create(this, DefinitionIdTypes.EMPTY)
        val SmallMissileTurret = create(this, DefinitionIdTypes.SmallMissileTurret)
        val LargeCalibreTurret = create(this, DefinitionIdTypes.LargeCalibreTurret)
        val LargeBlockMediumCalibreTurret = create(this, DefinitionIdTypes.LargeBlockMediumCalibreTurret)
        val SmallBlockMediumCalibreTurret = create(this, DefinitionIdTypes.SmallBlockMediumCalibreTurret)
    }

    object InteriorTurret : SafeDefinitionIdId() {
        val LargeInteriorTurret = create(this, DefinitionIdTypes.LargeInteriorTurret)
    }

    object SmallMissileLauncherReload : SafeDefinitionIdId() {
        val SmallRocketLauncherReload = create(this, DefinitionIdTypes.SmallRocketLauncherReload)
        val SmallBlockMediumCalibreGun = create(this, DefinitionIdTypes.SmallBlockMediumCalibreGun)
        val LargeRailgun = create(this, DefinitionIdTypes.LargeRailgun)
        val SmallRailgun = create(this, DefinitionIdTypes.SmallRailgun)
    }

    object SpawnGroupDefinition : SafeDefinitionIdId() {
        val DoM_Blue_01 = create(this, DefinitionIdTypes.DoM_Blue_01)
        val DoM_Blue_02 = create(this, DefinitionIdTypes.DoM_Blue_02)
        val DoM_Blue_03 = create(this, DefinitionIdTypes.DoM_Blue_03)
        val DoM_Blue_04 = create(this, DefinitionIdTypes.DoM_Blue_04)
        val DoM_Red_01 = create(this, DefinitionIdTypes.DoM_Red_01)
        val DoM_Red_02 = create(this, DefinitionIdTypes.DoM_Red_02)
        val DoM_Red_03 = create(this, DefinitionIdTypes.DoM_Red_03)
        val DoM_Red_04 = create(this, DefinitionIdTypes.DoM_Red_04)
        val Cargo_Buddy_mk_1 = create(this, DefinitionIdTypes.Cargo_Buddy_mk_1)
        val Insinuator_mk_1 = create(this, DefinitionIdTypes.Insinuator_mk_1)
        val Imperator_mk_1 = create(this, DefinitionIdTypes.Imperator_mk_1)
        val Instigator_mk_1 = create(this, DefinitionIdTypes.Instigator_mk_1)
        val Drillbot_mk_1 = create(this, DefinitionIdTypes.Drillbot_mk_1)
        val Grindbot_mk_1 = create(this, DefinitionIdTypes.Grindbot_mk_1)
        val Enforcer_mk_2 = create(this, DefinitionIdTypes.Enforcer_mk_2)
        val Ablator = create(this, DefinitionIdTypes.Ablator)
        val Cursor = create(this, DefinitionIdTypes.Cursor)
        val Combatant_mk_1_13_Weapons_Ready = create(this, DefinitionIdTypes.Combatant_mk_1_13_Weapons_Ready)
        val Military1 = create(this, DefinitionIdTypes.Military1)
        val Military2 = create(this, DefinitionIdTypes.Military2)
        val Military3 = create(this, DefinitionIdTypes.Military3)
        val Mining1 = create(this, DefinitionIdTypes.Mining1)
        val Mining2 = create(this, DefinitionIdTypes.Mining2)
        val Mining3 = create(this, DefinitionIdTypes.Mining3)
        val The_Pilgrims_Curiosity = create(this, DefinitionIdTypes.The_Pilgrims_Curiosity)
        val R_U_S_T__Freighter = create(this, DefinitionIdTypes.R_U_S_T__Freighter)
        val Trade1 = create(this, DefinitionIdTypes.Trade1)
        val Trade2 = create(this, DefinitionIdTypes.Trade2)
        val Trade3 = create(this, DefinitionIdTypes.Trade3)
        val PirateCarrier = create(this, DefinitionIdTypes.PirateCarrier)
        val Encounter_Debris_A = create(this, DefinitionIdTypes.Encounter_Debris_A)
        val Encounter_Debris_B = create(this, DefinitionIdTypes.Encounter_Debris_B)
        val Encounter_Ambasador_A = create(this, DefinitionIdTypes.Encounter_Ambasador_A)
        val Encounter_Ambasador_B = create(this, DefinitionIdTypes.Encounter_Ambasador_B)
        val Encounter_Blue_frame = create(this, DefinitionIdTypes.Encounter_Blue_frame)
        val Encounter_Corvette_A = create(this, DefinitionIdTypes.Encounter_Corvette_A)
        val Encounter_Corvette_B = create(this, DefinitionIdTypes.Encounter_Corvette_B)
        val Encounter_Droneyard = create(this, DefinitionIdTypes.Encounter_Droneyard)
        val Encounter_Ghoul_Corvette_A = create(this, DefinitionIdTypes.Encounter_Ghoul_Corvette_A)
        val Encounter_Ghoul_Corvette_B = create(this, DefinitionIdTypes.Encounter_Ghoul_Corvette_B)
        val Encounter_Ghoul_Corvette_C = create(this, DefinitionIdTypes.Encounter_Ghoul_Corvette_C)
        val Encounter_Homing_beacon = create(this, DefinitionIdTypes.Encounter_Homing_beacon)
        val Encounter_Hydro_Tanker = create(this, DefinitionIdTypes.Encounter_Hydro_Tanker)
        val Encounter_Imp_A = create(this, DefinitionIdTypes.Encounter_Imp_A)
        val Encounter_Imp_B = create(this, DefinitionIdTypes.Encounter_Imp_B)
        val Encounter_MushStation_A = create(this, DefinitionIdTypes.Encounter_MushStation_A)
        val Encounter_MushStation_B = create(this, DefinitionIdTypes.Encounter_MushStation_B)
        val Encounter_Ponos_F1_A = create(this, DefinitionIdTypes.Encounter_Ponos_F1_A)
        val Encounter_Ponos_F1_B = create(this, DefinitionIdTypes.Encounter_Ponos_F1_B)
        val Encounter_RS_1217_Transporter_A = create(this, DefinitionIdTypes.Encounter_RS_1217_Transporter_A)
        val Encounter_Section_F = create(this, DefinitionIdTypes.Encounter_Section_F)
        val Encounter_Skyheart_A = create(this, DefinitionIdTypes.Encounter_Skyheart_A)
        val Encounter_Skyheart_B = create(this, DefinitionIdTypes.Encounter_Skyheart_B)
        val Encounter_Stingray_II_A = create(this, DefinitionIdTypes.Encounter_Stingray_II_A)
        val Encounter_Stingray_II_B = create(this, DefinitionIdTypes.Encounter_Stingray_II_B)
        val Encounter_Vulture_vessel = create(this, DefinitionIdTypes.Encounter_Vulture_vessel)
        val Encounter_HEC_Debris_A = create(this, DefinitionIdTypes.Encounter_HEC_Debris_A)
        val Encounter_HEC_Debris_B = create(this, DefinitionIdTypes.Encounter_HEC_Debris_B)
        val Encounter_Habitat_Pods = create(this, DefinitionIdTypes.Encounter_Habitat_Pods)
        val Encounter_Haunted_Section = create(this, DefinitionIdTypes.Encounter_Haunted_Section)
        val Encounter_Pirate_Raider = create(this, DefinitionIdTypes.Encounter_Pirate_Raider)
        val Encounter_Prometheus_A = create(this, DefinitionIdTypes.Encounter_Prometheus_A)
        val Encounter_Prometheus_B = create(this, DefinitionIdTypes.Encounter_Prometheus_B)
        val Encounter_Red_A = create(this, DefinitionIdTypes.Encounter_Red_A)
        val Encounter_Red_B = create(this, DefinitionIdTypes.Encounter_Red_B)
        val Encounter_RespawnShip = create(this, DefinitionIdTypes.Encounter_RespawnShip)
        val Encounter_Salvage_station = create(this, DefinitionIdTypes.Encounter_Salvage_station)
        val Encounter_Stealth_pirate_station = create(this, DefinitionIdTypes.Encounter_Stealth_pirate_station)
        val Encounter_Severed_Bow = create(this, DefinitionIdTypes.Encounter_Severed_Bow)
        val Encounter_Hermit_Station = create(this, DefinitionIdTypes.Encounter_Hermit_Station)
        val Encounter_Mining_Vessel = create(this, DefinitionIdTypes.Encounter_Mining_Vessel)
        val Encounter_Mining_Outpost = create(this, DefinitionIdTypes.Encounter_Mining_Outpost)
        val Encounter_RoidStation = create(this, DefinitionIdTypes.Encounter_RoidStation)
        val Encounter_Safehouse_station = create(this, DefinitionIdTypes.Encounter_Safehouse_station)
        val Encounter_Shuttle = create(this, DefinitionIdTypes.Encounter_Shuttle)
        val Encounter_Mercenary_Wreckage = create(this, DefinitionIdTypes.Encounter_Mercenary_Wreckage)
        val Encounter_Minefield = create(this, DefinitionIdTypes.Encounter_Minefield)
        val Blue_Drone_MK1 = create(this, DefinitionIdTypes.Blue_Drone_MK1)
        val Blue_Drone_MK2 = create(this, DefinitionIdTypes.Blue_Drone_MK2)
        val Red_Drone_MK1 = create(this, DefinitionIdTypes.Red_Drone_MK1)
        val Escord_Drone = create(this, DefinitionIdTypes.Escord_Drone)
        val Light_patrol_drone = create(this, DefinitionIdTypes.Light_patrol_drone)
        val Vulture_Drone = create(this, DefinitionIdTypes.Vulture_Drone)
        val ProtectoBot = create(this, DefinitionIdTypes.ProtectoBot)
        val Raider_Drone = create(this, DefinitionIdTypes.Raider_Drone)
        val Salvage_Drone = create(this, DefinitionIdTypes.Salvage_Drone)
        val Seeker_Mine = create(this, DefinitionIdTypes.Seeker_Mine)
        val Snub_Fighter = create(this, DefinitionIdTypes.Snub_Fighter)
        val Stash_satellite = create(this, DefinitionIdTypes.Stash_satellite)
        val V2_Gunboat = create(this, DefinitionIdTypes.V2_Gunboat)
        val Hostile_Miner = create(this, DefinitionIdTypes.Hostile_Miner)
        val Old_Mining_Drone = create(this, DefinitionIdTypes.Old_Mining_Drone)
        val Tusk = create(this, DefinitionIdTypes.Tusk)
        val DroneS_1GG_1 = create(this, DefinitionIdTypes.DroneS_1GG_1)
        val DroneS_1GG_2 = create(this, DefinitionIdTypes.DroneS_1GG_2)
        val DroneS_1GG_3 = create(this, DefinitionIdTypes.DroneS_1GG_3)
        val DroneS_Drill_Warhead = create(this, DefinitionIdTypes.DroneS_Drill_Warhead)
        val DroneS_2GG_1GT = create(this, DefinitionIdTypes.DroneS_2GG_1GT)
        val DroneS_1MT_2GG = create(this, DefinitionIdTypes.DroneS_1MT_2GG)
        val DroneS_2GG_1GT_1RL = create(this, DefinitionIdTypes.DroneS_2GG_1GT_1RL)
        val DroneS_2GG_2GT = create(this, DefinitionIdTypes.DroneS_2GG_2GT)
        val DroneL_1RL_1GG_2IT = create(this, DefinitionIdTypes.DroneL_1RL_1GG_2IT)
        val BossDroneL_1RL_2GT_1MT_2IT = create(this, DefinitionIdTypes.BossDroneL_1RL_2GT_1MT_2IT)
        val Eradicator_mk_1 = create(this, DefinitionIdTypes.Eradicator_mk_1)
        val Incisor_mk_1 = create(this, DefinitionIdTypes.Incisor_mk_1)
        val Informant_mk_1 = create(this, DefinitionIdTypes.Informant_mk_1)
        val Assailant_mk_1 = create(this, DefinitionIdTypes.Assailant_mk_1)
        val Dyad_mk_1 = create(this, DefinitionIdTypes.Dyad_mk_1)
        val Weapon_Platform_Gatling1 = create(this, DefinitionIdTypes.Weapon_Platform_Gatling1)
        val DriftingPursuant = create(this, DefinitionIdTypes.DriftingPursuant)
        val FleetingRival = create(this, DefinitionIdTypes.FleetingRival)
        val SpitefulAggressor = create(this, DefinitionIdTypes.SpitefulAggressor)
        val StingingAdversary = create(this, DefinitionIdTypes.StingingAdversary)
        val Spur = create(this, DefinitionIdTypes.Spur)
        val Molar = create(this, DefinitionIdTypes.Molar)
        val KingMolar = create(this, DefinitionIdTypes.KingMolar)
        val DodgeDrone = create(this, DefinitionIdTypes.DodgeDrone)
    }
}
