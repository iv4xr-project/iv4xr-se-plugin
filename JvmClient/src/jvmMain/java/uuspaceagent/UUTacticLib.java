package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.Action;
import nl.uu.cs.aplib.mainConcepts.Tactic;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.model.CharacterObservation;
import spaceEngineers.model.Vec2F;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static nl.uu.cs.aplib.AplibEDSL.action;
import static spaceEngineers.controller.Character.DISTANCE_CENTER_CAMERA;
import static uuspaceagent.UUSeAgentState.OBSERVATION_RADIUS;

@SuppressWarnings({"unchecked", "rawtypes"})
public class UUTacticLib {

    /**
     * The default speed with which the agent turns.
     */
    public static float TURNING_SPEED = 10f ;

    /**
     * The default speed with which the agent turns.
     */
    public static float ROLL_SPEED = 1f ;

    /**
     * The default walking speed. Set as 0.3f, as defined by SE (at this speed the agent
     * will walk, not running).
     */
    public static float WALK_SPEED = 0.3f ;

    /***
     * The default running speed. This is set at 0.4. SE defines that at this speed the
     * agent will run.
     */
    public static float RUN_SPEED = 0.4f ;

    /***
     * The flying speed. This is set the same as walking for now, but it can reach a whole
     * 110 m/s, which is somewhere around 4.4 to 5.5.
     */
    public static float FLY_SPEED = 0.3f ;

    /**
     * If the angle between the agent's current direction and the direction-to-go is less
     * than this threshold, we slow the agent's turning speed, to mitigate that it might
     * overshoot the intended direction to go.
     * Expressed in terms of cos(angle). Below is cos(10-degree):
     */
    public static float THRESHOLD_ANGLE_TO_SLOW_TURNING = (float) Math.cos(Math.toRadians(10f));
    /**
     * When the agent's distance to a square's center is less or equal to this threshold, the
     * square is considered as visited. This distance is an about the square size, so the agent
     * might actually in the neighboring square. This threshold is introduced to avoid the
     * agent from getting stuck because it keeps overshooting the center of a target square.
     * <p>
     * The threshold is not expressed literally as distance, but for efficiency it is expressed
     * as the square of the distance (so that we don't have to keep calculating square-roots).
     */
    public static float THRESHOLD_SQUARED_DISTANCE_TO_SQUARE = NavGrid.CUBE_SIZE * NavGrid.CUBE_SIZE; //1.3f * Grid2DNav.SQUARE_SIZE * 1.3f * Grid2DNav.SQUARE_SIZE
    public static float THRESHOLD_SQUARED_DISTANCE_TO_POINT= 1.7f ; // magic number ... :|

    /**
     * The unit move-vector that will cause the agent to move in the same direction as its
     * forward-orientation. That is, this vector will move the agent in the direction that
     * it faces to. Note that as a unit-vector it does not take speed into account; you need
     * to multiply it with the speed that you want.
     */
    static Vec3 FORWARDV3 = new Vec3(0,0,1) ;

    /**
     * Just the vector (0,0,0).
     */
    static Vec3 ZEROV3 = new Vec3(0,0,0) ;

    /**
     * Just the vector (0,0).
     */
    static Vec2F ZEROV2 = new Vec2F(0,0) ;

    /**
     * A more intelligent and performant primitive "move" (than the primitive "moveAndRotate"). This
     * method sends a burst of successive move commands to SE with little computation in between.
     * This results in fast(er) move. The number
     * of commands sent is specified by the parameter "duration", though the burst is stopped once
     * the agent is very close to the destination (some threshold). The agent will run, if the
     * distance to the destination is >= 1, else it will walk.
     * <p>
     * Note: (1) the method will not turn the direction the agent is facing,
     * and (2) that during the burst we will be blind to changes from SE.
     * <p>
     * The method returns an observation if it did at least one move. If at the beginning the agent
     * is already (very close to) at the destination the method will not do any move and will
     * return null.
     */
    public static CharacterObservation moveToward(UUSeAgentState agentState, Vec3 destination, int duration) {
        Vec3 playerPos = agentState.wom.position;
        if (agentState.jetpackRunning()) playerPos = agentState.centerPos();
        Vec3 destinationRelativeLocation = Vec3.sub(destination, playerPos) ;
        float sqDistance = destinationRelativeLocation.lengthSq() ;
        if (sqDistance <= 0.01) {
            // already very close to the destination
            return null ;
        }
        System.out.println(">>> agent @ " + agentState.wom.position + ", dest: " + destination
           + ", rel-direction: " + destinationRelativeLocation);
        System.out.println("    forward-vector: " + agentState.orientationForward());

        // Decide if we should move slow or fast:
        boolean running = true ;
        if (sqDistance <= 1) running = false ;

        // adjust the forward vector to make it angled towards the destination
        Vec3 forwardRun  = Rotation.rotate3d(agentState.orientationForward(), agentState.orientationUp(), destinationRelativeLocation);
        Vec3 forwardWalk = Vec3.mul(forwardRun.normalized(), FLY_SPEED * 0.75f);
        forwardRun = Vec3.mul(forwardRun.normalized(), FLY_SPEED);

        // adjust the forward vector to make it angles towards the destination
        if(!agentState.jetpackRunning()) {
            // 2D movement on surface:
            forwardRun  = Vec3.mul(FORWARDV3, RUN_SPEED);
            forwardWalk = Vec3.mul(FORWARDV3, WALK_SPEED);
            Matrix3D rotation = Rotation.getYRotation(agentState.orientationForward(),destinationRelativeLocation) ;
            forwardRun  = rotation.apply(forwardRun) ;
            forwardWalk = rotation.apply(forwardWalk) ;
            System.out.println(">>> no-fly forwardRun: " + forwardRun);
        }
        else {
            System.out.println(">>> forwardFly: " + forwardRun);
        }

        // now move... sustain it for the given duration:
        CharacterObservation obs = null ;
        float threshold = THRESHOLD_SQUARED_DISTANCE_TO_POINT - 0.15f ;
        for(int k=0; k<duration; k++) {
            obs = agentState.env().getController().getCharacter().moveAndRotate(
                    SEBlockFunctions.toSEVec3(running ? seFixPolarityMoveVector(forwardRun) : seFixPolarityMoveVector(forwardWalk))
                    , ZEROV2,
                    0, 1) ; // "roll" and "tick" ... using default values;
            sqDistance = Vec3.sub(SEBlockFunctions.fromSEVec3(obs.getPosition()), destination).lengthSq() ;
            if(sqDistance <= threshold) {
                break ;
            }
            if (running && sqDistance <= 1f) running = false ;
        }
        return obs ;
    }

    /**
     * A more intelligent and performant primitive "move" (than the primitive "moveAndRotate"). This
     * method sends a burst of successive move commands to SE with little computation in between.
     * This results in fast(er) move. The number
     * of commands sent is specified by the parameter "duration", though the burst is stopped once
     * the agent is very close to the destination (some threshold).
     * <p>
     * Note: (1) the method will not turn the direction the agent is facing,
     * and (2) that during the burst we will be blind to changes from SE.
     * <p>
     * The method returns an observation if it did at least one move. If at the beginning the agent
     * is already (very close to) at the destination the method will not do any move and return null.
     */
    public static CharacterObservation moveToward(UUSeAgentState3DVoxelGrid agentState, Vec3 destination, int duration) {
        Vec3 playerPos3D = agentState.centerPos();
        Vec3 destinationRelativeLocation = Vec3.sub(destination, playerPos3D) ;
        float sqDistance = destinationRelativeLocation.lengthSq() ;
        if (sqDistance <= 0.01) {
            // already very close to the destination
            return null ;
        }
        System.out.println(">>> agent @ " + playerPos3D + ", dest: " + destination
                + ", rel-direction: " + destinationRelativeLocation);
        System.out.println("    forward-vector: " + agentState.orientationForward());

        // adjust the forward vector to make it angled towards the destination
        Vec3 forwardFly = Rotation.rotate3d(agentState.orientationForward(), agentState.orientationUp(), destinationRelativeLocation);
        forwardFly = Vec3.mul(forwardFly.normalized(), FLY_SPEED);

        System.out.println(">>> forwardFly: " + forwardFly);

        // now move... sustain it for the given duration:
        CharacterObservation obs = null ;
        float threshold = THRESHOLD_SQUARED_DISTANCE_TO_POINT - 0.15f ;
        for(int k=0; k<duration; k++) {
            obs = agentState.env().getController().getCharacter().moveAndRotate(
                    SEBlockFunctions.toSEVec3(seFixPolarityMoveVector(forwardFly))
                    , ZEROV2,
                    0, 1) ; // "roll" and "tick" ... using default values;
            sqDistance = Vec3.sub(SEBlockFunctions.fromSEVec3(obs.getPosition()), destination).lengthSq() ;
            if(sqDistance <= threshold) {
                break ;
            }
        }
        return obs ;
    }
    /**
     * A more intelligent and performant primitive "move" (than the primitive "moveAndRotate"). This
     * method sends a burst of successive move commands to SE with little computation in between.
     * This results in fast(er) move. The number
     * of commands sent is specified by the parameter "duration", though the burst is stopped once
     * the agent is very close to the destination (some threshold).
     * <p>
     * Note: (1) the method will not turn the direction the agent is facing,
     * and (2) that during the burst we will be blind to changes from SE.
     * <p>
     * The method returns an observation if it did at least one move. If at the beginning the agent
     * is already (very close to) at the destination the method will not do any move and return null.
     */
    public static CharacterObservation moveToward(UUSeAgentState3DOctree agentState, Vec3 destination, int duration) {
        Vec3 playerPos3D = agentState.centerPos();
        Vec3 destinationRelativeLocation = Vec3.sub(destination, playerPos3D) ;
        float sqDistance = destinationRelativeLocation.lengthSq() ;
        if (sqDistance <= 0.01) {
            // already very close to the destination
            return null ;
        }
        System.out.println(">>> agent @ " + playerPos3D + ", dest: " + destination
                + ", rel-direction: " + destinationRelativeLocation);
        System.out.println("    forward-vector: " + agentState.orientationForward());

        // Decide if we should fly slow or fast:
        boolean running = true ;
        if (sqDistance <= 1) running = false ;

        // adjust the forward vector to make it angled towards the destination
        Vec3 forwardFly     = Rotation.rotate3d(agentState.orientationForward(), agentState.orientationUp(), destinationRelativeLocation);
        Vec3 forwardFlySlow = Vec3.mul(forwardFly.normalized(), FLY_SPEED * 0.75f);
        forwardFly = Vec3.mul(forwardFly.normalized(), FLY_SPEED);

        System.out.println(">>> forwardFly: " + forwardFly);

        // now move... sustain it for the given duration:
        CharacterObservation obs = null ;
        float threshold = THRESHOLD_SQUARED_DISTANCE_TO_POINT - 0.15f ;
        for(int k=0; k<duration; k++) {
            obs = agentState.env().getController().getCharacter().moveAndRotate(
                    SEBlockFunctions.toSEVec3(running ? seFixPolarityMoveVector(forwardFly) : seFixPolarityMoveVector(forwardFlySlow))
                    , ZEROV2,
                    0, 1) ; // "roll" and "tick" ... using default values;
            sqDistance = Vec3.sub(SEBlockFunctions.fromSEVec3(obs.getPosition()), destination).lengthSq() ;
            if(sqDistance <= threshold) {
                break ;
            }
            if (running && sqDistance <= 1f) running = false ;
        }
        return obs ;
    }

    /**
     * A primitive method that sends a burst of successive turn-angle commands to SE. This
     * method will only change the agent's forward facing vector. So, it rotates the agent's
     * forward vector around the agent's y-axis. It does not change the agent's up-facing
     * vector either. (so, it is like moving the agent's head horizontally, but not vertically).
     * <p>
     * The number of the turn-commands bursted is specified by the parameter duration. The agent
     * will turn on its place so that it would face the given destination. The parameter
     * "cosAlphaThreshold" specifies how far the agent should turn. It would turn until the angle
     * alpha between its forward direction and the straight line between itself and destination
     * is alpha. The cosAlphaThreshold expresses this alpha in terms of cos(alpha).
     * <p>
     * The method will burst the turning, if the remaining angle towards alpha is still large,
     * after which it will not burst (so it will just send one turn-command and return).
     * <p>
     * If at the beginning the angle to destination is less than alpha this method returns null,
     * and else it returns an observation at the end of the turns.
     */
    public static CharacterObservation yTurnTowardACT(UUSeAgentState agentState, Vec3 destination, float cosAlphaThreshold, Integer duration) {
        // direction vector to the next node:
        Vec3 dirToGo = Vec3.sub(destination, agentState.wom.position) ;
        Vec3 agentHdir = agentState.orientationForward() ;

        // for calculating 2D rotation we ignore the up-vector:
        Vec3 upVec = agentState.orientationUp();
        // project the two vectors to the plane perpendicular to the up-vector
        dirToGo   = Vec3.sub(dirToGo,   Vec3.mul(upVec, Vec3.dot(dirToGo,   upVec) / Vec3.dot(upVec, upVec)));
        agentHdir = Vec3.sub(agentHdir, Vec3.mul(upVec, Vec3.dot(agentHdir, upVec) / Vec3.dot(upVec, upVec)));

        if(dirToGo.lengthSq() < 1) {
            // the destination is too close within the agent's up-cylinder;
            // don't bother to rotate then
            return null ;
        }

        dirToGo = dirToGo.normalized() ;
        agentHdir = agentHdir.normalized() ;
        // angle between the dir-to-go and the agent's own direction (expressed as cos(angle)):
        var cos_alpha = Vec3.dot(agentHdir, dirToGo) ;
        //if(1 - cos_alpha < 0.01) {
        if(cos_alpha > cosAlphaThreshold) {
            // the angle is already quite aligned to the direction of where we have to go, no turning.
            return null ;
        }

        float turningSpeed = TURNING_SPEED ;
        boolean fastturning = true ;

        Vec3 normalVector = Vec3.cross(agentHdir,dirToGo);

        if(cos_alpha >= cosAlphaThreshold * THRESHOLD_ANGLE_TO_SLOW_TURNING) {
            // the angle between the agent's own direction and target direction is less than 10-degree
            // we reduce the turning-speed:
            turningSpeed = TURNING_SPEED * 0.25f ;
            fastturning = false ;
        }
        if (Vec3.dot(normalVector, upVec) > 0) {
            // The agent should then turn clock-wise; for SE this means giving
            // a negative turning speed. Else positive turning speed.
            turningSpeed = -turningSpeed ;
        }
        if(!fastturning || duration == null) {
            duration = 1 ;
        }
        Vec2F turningVector = new Vec2F(0, turningSpeed) ;

        // now send the turning commands:
        CharacterObservation obs = null ;
        for (int k=0; k<duration; k++) {
            obs = agentState.env().getController().getCharacter().moveAndRotate(
                    SEBlockFunctions.toSEVec3(ZEROV3),
                    turningVector,
                    0, 1) ; // "roll" and "tick" ... using default values;
            dirToGo = Vec3.sub(destination,SEBlockFunctions.fromSEVec3(obs.getPosition())) ;
            agentHdir = SEBlockFunctions.fromSEVec3(obs.getOrientationForward()) ;
            // for calculating 2D rotation we ignore the up-vector:
            upVec = SEBlockFunctions.fromSEVec3(obs.getOrientationUp());
            // project the two vectors to the plane perpendicular to the up-vector
            dirToGo   = Vec3.sub(dirToGo,   Vec3.mul(upVec, Vec3.dot(dirToGo,   upVec) / Vec3.dot(upVec, upVec)));
            agentHdir = Vec3.sub(agentHdir, Vec3.mul(upVec, Vec3.dot(agentHdir, upVec) / Vec3.dot(upVec, upVec)));

            if(dirToGo.lengthSq() < 1) {
                // the destination is too close within the agent's y-cylinder;
                // don't bother to rotate then
                break ;
            }

            dirToGo = dirToGo.normalized() ;
            agentHdir = agentHdir.normalized() ;
            // angle between the dir-to-go and the agent's own direction (expressed as cos(angle)):
            cos_alpha = Vec3.dot(agentHdir,dirToGo) ;
            //if(1 - cos_alpha < 0.01) {
            if(cos_alpha > cosAlphaThreshold) {
                // the angle is already quite aligned to the direction of where we have to go, no turning.
               break ;
            }
        }

        return obs ;
    }

    /**
     * Just like yTurnTowardACT, but it will rotate regardless of closeness to the target
     */
    public static CharacterObservation yTurnTowardACT2(UUSeAgentState agentState, Vec3 destination, float cosAlphaThreshold, Integer duration) {
        // direction vector to the next node:
        Vec3 dirToGo = Vec3.sub(destination, agentState.wom.position) ;
        Vec3 agentHdir = agentState.orientationForward() ;

        // for calculating 2D rotation we ignore the up-vector:
        Vec3 upVec = agentState.orientationUp();
        // project the two vectors to the plane perpendicular to the up-vector
        dirToGo   = Vec3.sub(dirToGo,   Vec3.mul(upVec, Vec3.dot(dirToGo,   upVec) / Vec3.dot(upVec, upVec)));
        agentHdir = Vec3.sub(agentHdir, Vec3.mul(upVec, Vec3.dot(agentHdir, upVec) / Vec3.dot(upVec, upVec)));

        dirToGo = dirToGo.normalized() ;
        agentHdir = agentHdir.normalized() ;
        // angle between the dir-to-go and the agent's own direction (expressed as cos(angle)):
        var cos_alpha = Vec3.dot(agentHdir, dirToGo) ;
        if(cos_alpha > cosAlphaThreshold) {
            // the angle is already quite aligned to the direction of where we have to go, no turning.
            return null ;
        }

        float turningSpeed = TURNING_SPEED ;
        boolean fastturning = true ;

        Vec3 normalVector = Vec3.cross(agentHdir,dirToGo);

        if(cos_alpha >= cosAlphaThreshold * THRESHOLD_ANGLE_TO_SLOW_TURNING) {
            // the angle between the agent's own direction and target direction is less than 10-degree
            // we reduce the turning-speed:
            turningSpeed = TURNING_SPEED * 0.25f ;
            fastturning = false ;
        }
        if (Vec3.dot(normalVector, upVec) > 0) {
            // The agent should then turn clock-wise; for SE this means giving
            // a negative turning speed. Else positive turning speed.
            turningSpeed = -turningSpeed ;
        }
        if(!fastturning || duration == null) {
            duration = 1 ;
        }
        Vec2F turningVector = new Vec2F(0, turningSpeed) ;

        // now send the turning commands:
        CharacterObservation obs = null ;
        for (int k=0; k<duration; k++) {
            obs = agentState.env().getController().getCharacter().moveAndRotate(
                    SEBlockFunctions.toSEVec3(ZEROV3),
                    turningVector,
                    0, 1) ; // "roll" and "tick" ... using default values;
            dirToGo = Vec3.sub(destination,SEBlockFunctions.fromSEVec3(obs.getPosition())) ;
            agentHdir = SEBlockFunctions.fromSEVec3(obs.getOrientationForward()) ;
            // for calculating 2D rotation we ignore the up-vector:
            upVec = SEBlockFunctions.fromSEVec3(obs.getOrientationUp());
            // project the two vectors to the plane perpendicular to the up-vector
            dirToGo   = Vec3.sub(dirToGo,   Vec3.mul(upVec, Vec3.dot(dirToGo,   upVec) / Vec3.dot(upVec, upVec)));
            agentHdir = Vec3.sub(agentHdir, Vec3.mul(upVec, Vec3.dot(agentHdir, upVec) / Vec3.dot(upVec, upVec)));

            dirToGo = dirToGo.normalized() ;
            agentHdir = agentHdir.normalized() ;
            // angle between the dir-to-go and the agent's own direction (expressed as cos(angle)):
            cos_alpha = Vec3.dot(agentHdir,dirToGo) ;
            //if(1 - cos_alpha < 0.01) {
            if(cos_alpha > cosAlphaThreshold) {
                // the angle is already quite aligned to the direction of where we have to go, no turning.
                break ;
            }
        }

        return obs ;
    }

    /**
     * A primitive method that sends a burst of successive turn-angle commands to SE. This
     * method rotates the agent's up-facing vector around the agent's z-axis.
     * (so, it is like moving the agent's head vertically, but not horizontally).
     * <p>
     * The number of the turn-commands bursted is specified by the parameter duration. The agent
     * will turn on its place so that it would face the given destination. The parameter
     * "cosAlphaThreshold" specifies how far the agent should turn. It would turn until the angle
     * alpha between its forward direction and the straight line between itself and destination
     * is alpha. The cosAlphaThreshold expresses this alpha in terms of cos(alpha).
     * <p>
     * The method will burst the turning, if the remaining angle towards alpha is still large,
     * after which it will not burst (so it will just send one turn-command and return).
     * <p>
     * If at the beginning the angle to destination is less than alpha this method returns null,
     * and else it returns an observation at the end of the turns.
     */
    public static CharacterObservation zTurnTowardACT(UUSeAgentState agentState, Vec3 destination, float cosAlphaThreshold, Integer duration) {
        // direction vector to the next node:
        //Vec3 dirToGo = Vec3.sub(destination,
        //        SEBlockFunctions.fromSEVec3(agentState.env().getController().getObserver().observe().getCamera().getPosition())) ;
        Vec3 pos = agentState.wom.position;
        pos.y += DISTANCE_CENTER_CAMERA;
        Vec3 dirToGo = Vec3.sub(destination, pos) ;
        Vec3 agentVdir = SEBlockFunctions.fromSEVec3(
                agentState.env().getController().getObserver().observe().getCamera().getOrientationForward()) ;

        // for calculating 2D rotation we ignore the z-value:
        dirToGo.z = 0 ;
        agentVdir.z = 0 ;

        if(dirToGo.lengthSq() < 1) {
            // the destination is too close within the agent's z-cylinder;
            // don't bother to rotate then
            return null;
        }

        dirToGo = dirToGo.normalized() ;
        agentVdir = agentVdir.normalized() ;
        // angle between the dir-to-go and the agent's own direction (expressed as cos(angle)):
        var cos_alpha = Vec3.dot(agentVdir, dirToGo) ;
        //if(1 - cos_alpha < 0.01) {
        if(cos_alpha > cosAlphaThreshold) {
            // the angle is already quite aligned to the direction of where we have to go, no turning.
            return null ;
        }

        float turningSpeed = TURNING_SPEED ;
        boolean fastturning = true ;

        Vec3 normalVector = Vec3.cross(agentVdir, dirToGo) ;

        if(cos_alpha >= THRESHOLD_ANGLE_TO_SLOW_TURNING) {
            // the angle between the agent's own direction and target direction is less than 10-degree
            // we reduce the turning-speed:
            turningSpeed = TURNING_SPEED * 0.25f ;
            fastturning = false ;
        }
        // check if we have to turn clockwise or counter-clockwise
        if (normalVector.z < 0) {
            // The agent should then turn counter-clockwise; for SE this means giving
            // a positive turning speed. Else negative turning speed.
            turningSpeed = -turningSpeed ;
        }
        if(!fastturning || duration == null) {
            duration = 1 ;
        }
        Vec2F turningVector = new Vec2F(turningSpeed,0) ;

        // now send the turning commands:
        CharacterObservation obs = null ;
        for (int k=0; k<duration; k++) {
            obs = agentState.env().getController().getCharacter().moveAndRotate(
                    SEBlockFunctions.toSEVec3(ZEROV3),
                    turningVector,
                    0, 1) ; // "roll" and "tick" ... using default values;
            //dirToGo = Vec3.sub(destination,SEBlockFunctions.fromSEVec3(obs.getCamera().getPosition())) ;
            pos = SEBlockFunctions.fromSEVec3(obs.getPosition()) ;
            pos.y += DISTANCE_CENTER_CAMERA ;
            dirToGo = Vec3.sub(destination, pos) ;
            agentVdir = SEBlockFunctions.fromSEVec3(obs.getCamera().getOrientationForward()) ;
            // for calculating 2D rotation we ignore the Z-value:
            dirToGo.z = 0 ;
            agentVdir.z = 0 ;

            if(dirToGo.lengthSq() < 1) {
                // the destination is too close within the agent's z-cylinder;
                // don't bother to rotate then
                break ;
            }

            dirToGo = dirToGo.normalized() ;
            agentVdir = agentVdir.normalized() ;
            // angle between the dir-to-go and the agent's own direction (expressed as cos(angle)):
            cos_alpha = Vec3.dot(agentVdir, dirToGo) ;
            //if(1 - cos_alpha < 0.01) {
            if(cos_alpha > cosAlphaThreshold) {
                // the angle is already quite aligned to the direction of where we have to go, no turning.
                break ;
            }
        }

        return obs ;
    }

    /**
     * Fix the polarity of move-vector to be given to the SE's method moveAndRotate(). It
     * requires us to reverse the polarity of the values for x and z-axis, but not the y-axis.
     */
    public static Vec3 seFixPolarityMoveVector(Vec3 v) {
        return new Vec3(- v.x,  v.y, - v.z) ;
    }

    /**
     * A primitive method that will send successive grind-commends to SE. The number of commands
     * is specified by the parameter "duration". The method does not check if in between
     * the targeted block is actually already destroyed.
     */
    public static void grind(UUSeAgentState state, int duration) {
        for(int k=0; k<duration; k++) {
            state.env().beginUsingTool();
        }
    }

    /**
     * When invoked repeatedly, this action turns the agent until it horizontally faces towards the
     * given destination. The turning is around the y-axis (so, on the XZ plane; the y coordinates on all
     * points in the agent would stay the same). When the agent faces towards the destination
     * (with some epsilon), the action is no longer enabled.
     * <p>
     * The action returns the resulting angle (expressed in cos-alpha) between the agent's
     * forward-orientation and the direction-vector towards the given destination.
     */
    public static Action yTurnTowardACT(Vec3 destination) {

        float cosAlphaThreshold  = 0.995f ;
        float cosAlphaThreshold_ = 0.995f ;

        return action("turning towards " + destination)
                .on((UUSeAgentState state) ->{
                    Vec3 dirToGo = Vec3.sub(destination,state.wom.position) ;
                    Vec3 forwardOrientation = state.orientationForward() ;

                    // for calculating 2D rotation we ignore the up-vector:
                    Vec3 upVec = state.orientationUp();
                    // project the dirToGo to the plane perpendicular to the up-vector
                    dirToGo = Vec3.sub(dirToGo, Vec3.mul(upVec, Vec3.dot(dirToGo, upVec) / Vec3.dot(upVec, upVec)));

                    dirToGo = dirToGo.normalized() ;
                    forwardOrientation = forwardOrientation.normalized() ;
                    var cos_alpha = Vec3.dot(forwardOrientation, dirToGo) ;
//                    if(cos_alpha >= cosAlphaThreshold) { // the angle is quite aligned, the action is disabled
//                        return null ;
//                    }
                    return cos_alpha ;
                })
                .do2((UUSeAgentState state) -> (Float cos_alpha) -> {
                    if(cos_alpha >= cosAlphaThreshold) { // the angle is quite aligned, the action is disabled
                        return cos_alpha ;
                    }
                    // yTurnTowardACT2 will always rotate regardless of closeness to the target
                    CharacterObservation obs = yTurnTowardACT2(state, destination, cosAlphaThreshold_, 10) ;
                    if(obs == null) {
                        return cos_alpha ;
                    }
                    Vec3 dirToGo = Vec3.sub(destination,state.wom.position) ;
                    Vec3 forwardOrientation = SEBlockFunctions.fromSEVec3(obs.getOrientationForward()) ;

                    // for calculating 2D rotation we ignore the up-vector:
                    Vec3 upVec = state.orientationUp();
                    // project the dirToGo to the plane perpendicular to the up-vector
                    dirToGo = Vec3.sub(dirToGo, Vec3.mul(upVec, Vec3.dot(dirToGo, upVec) / Vec3.dot(upVec, upVec)));

                    dirToGo = dirToGo.normalized() ;
                    forwardOrientation = forwardOrientation.normalized() ;
                    cos_alpha = Vec3.dot(forwardOrientation, dirToGo) ;
                    return cos_alpha ;
                }) ;
    }

    /**
     * When invoked repeatedly, this action turns the camera until it vertically faces towards the
     * given destination. The turning is around the z-axis (so, on the XY plane; the z coordinates on all
     * points in the agent would stay the same). When the agent faces towards the destination
     * (with some epsilon), the action is no longer enabled.
     * <p>
     * The action returns the resulting angle (expressed in cos-alpha) between the agent's
     * up-orientation and the direction-vector towards the given destination.
     */
    public static Action zTurnTowardACT(Vec3 destination) {

        float cosAlphaThreshold  = 0.995f ;
        float cosAlphaThreshold_ = 0.995f ;

        return action("turning towards " + destination)
                .on((UUSeAgentState state) -> {
                    Vec3 pos = state.wom.position;
                    Vec3 upOrientation = SEBlockFunctions.fromSEVec3(
                            state.env().getController().getObserver().observe().getCamera().getOrientationUp()) ;
                    pos = Vec3.add(pos, Vec3.mul(upOrientation, DISTANCE_CENTER_CAMERA)); // TODO: check if this works for first and third person
                    Vec3 dirToGo = Vec3.sub(destination, pos) ;
                    Vec3 forwardOrientation = SEBlockFunctions.fromSEVec3(
                            state.env().getController().getObserver().observe().getCamera().getOrientationForward()) ;
                    dirToGo.z = 0 ;
                    forwardOrientation.z = 0 ;
                    dirToGo = dirToGo.normalized() ;
                    forwardOrientation = forwardOrientation.normalized() ;
                    var cos_alpha = Vec3.dot(forwardOrientation, dirToGo) ;
//                    if(cos_alpha >= cosAlphaThreshold) { // the angle is quite aligned, the action is disabled
//                        return null ;
//                    }
                    return cos_alpha ;
                })
                .do2((UUSeAgentState state) -> (Float cos_alpha) -> {
                    if(cos_alpha >= cosAlphaThreshold) { // the angle is quite aligned, the action is disabled
                        return null ;
                    }
                    CharacterObservation obs = zTurnTowardACT(state, destination, cosAlphaThreshold_, 10) ;
                    if(obs == null) {
                        return cos_alpha ;
                    }
                    Vec3 pos = SEBlockFunctions.fromSEVec3(obs.getPosition()) ;
                    pos.y += DISTANCE_CENTER_CAMERA ;
                    Vec3 dirToGo = Vec3.sub(destination, pos) ;
                    Vec3 forwardOrientation = SEBlockFunctions.fromSEVec3(obs.getCamera().getOrientationForward()) ;
                    dirToGo.z = 0 ;
                    forwardOrientation.z = 0 ;
                    dirToGo = dirToGo.normalized() ;
                    forwardOrientation = forwardOrientation.normalized() ;
                    cos_alpha = Vec3.dot(forwardOrientation, dirToGo) ;
                    return cos_alpha ;
                }) ;
    }

    /**
     * When invoked repeatedly, this action drives the agent to move in the straight-line towards the given
     * destination. The destination is assumed to be on the same XZ plane as the agent. The space between
     * the agent and the destination are assumed to be clear, and the XZ-plane along the travel is walkable.
     * <p>
     * The action is no longer enabled if the agent is already at the destination (or very very close to it).
     * <p>
     * This action will not turn the agent to face the destination (so, it will strafe, if its orientation
     * is not aligned first).
     * <p>
     * The action returns the resulting distance to the destination. More precisely, it returns
     * the square of the distance, to avoid calculating the more expensive square-root.
     */
    public static Action straightline2DMoveTowardsACT(Vec3 destination) {

        return action("straight line move to " + destination)
                .do1((UUSeAgentState state) -> {
                    var sqDistance = Vec3.sub(destination,state.wom.position).lengthSq() ;
                    CharacterObservation obs = moveToward(state,destination,7) ;
                    if(obs == null) {
                        return sqDistance ;
                    }
                    return Vec3.sub(destination,SEBlockFunctions.fromSEVec3(obs.getPosition())).lengthSq() ;

                })
                .on_((UUSeAgentState state) -> Vec3.sub(destination, state.wom.position).lengthSq() >= 0.01);
    }

    public static Tactic navigateToBlockTAC(Function<UUSeAgentState, Predicate<WorldEntity>> selector,
                                            SEBlockFunctions.BlockSides side,
                                            float delta) {

        return action("navigateTo").do2((UUSeAgentState state)
                        -> (Pair<List<DPos3>, Boolean> queryResult) -> {
                    var path = queryResult.fst ;
                    var arrivedAtDestination = queryResult.snd ;

                    Timer.moveStart = Instant.now();

                    if (state instanceof UUSeAgentState2D) {
                        // check first if we should turn on/off jetpack:
                        if (state.wom.position.y - state.getOrigin().y <= NavGrid.AGENT_HEIGHT
                                && path.get(0).y == 0 && state.jetpackRunning()
                        ) {
                            state.env().getController().getCharacter().turnOffJetpack();
                        } else {
                            if (path.get(0).y > 0 && !state.jetpackRunning()) {
                                state.env().getController().getCharacter().turnOnJetpack();
                            }
                        }
                    } else if (state instanceof UUSeAgentState3DVoxelGrid || state instanceof UUSeAgentState3DOctree) {
                        // turn on jetpack if not already on
                        if (!state.jetpackRunning())
                            state.env().getController().getCharacter().turnOnJetpack();
                    }

                    if (arrivedAtDestination) {
                        state.currentPathToFollow.clear();
                        Timer.endMove();
                        if (state instanceof UUSeAgentState3DVoxelGrid || state instanceof UUSeAgentState3DOctree) {
                            return new Pair<>(state.centerPos(), state.orientationForward());
                        }
                        else {
                            return new Pair<>(state.wom.position, state.orientationForward());
                        }
                    }
                    // else we are not at the destination yet...

                    // set currentPathToFollow:
                    state.currentPathToFollow = path ;

                    // follow the path, direct the agent to the next node in the path (actually, the first
                    // node in the path, since we remove a node if it is passed):
                    var nextNode = state.currentPathToFollow.get(0) ;
                    var nextNodePos = state.getBlockCenter(nextNode) ;
                    if(Vec3.sub(nextNodePos, state.centerPos()).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {
                        // agent is already in the same square as the next-node destination-square. Mark the node
                        // as reached (so, we remove it from the plan):
                        state.currentPathToFollow.remove(0) ;
                        Timer.endMove();
                        return new Pair<>(state.centerPos(), state.orientationForward()) ;
                    }
                    CharacterObservation obs = null ;
                    // disabling rotation for now
                    obs = yTurnTowardACT(state, nextNodePos, 0.9f, 10) ;
                    if (obs != null) {
                        // we did turning, we won't move.
                        Timer.endMove();
                        return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward())) ;
                    }
//                    obs = zTurnTowardACT(state, nextNodePos, 0.8f,5) ;
//                    if (obs != null) {
//                        // we did turning, we won't move.
//                        return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward())) ;
//                    }
                    if (state instanceof UUSeAgentState3DVoxelGrid) {
                        obs = moveToward((UUSeAgentState3DVoxelGrid) state, nextNodePos, 20);
                    }
                    else if (state instanceof UUSeAgentState3DOctree) {
                        obs = moveToward((UUSeAgentState3DOctree) state, nextNodePos, 20);
                    }
                    else {
                        obs = moveToward(state, nextNodePos, 20);
                    }
                    Timer.endMove();
                    return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward()));
                } )
                .on((UUSeAgentState state)  -> {
                    if (state.wom==null) return null ;

                    WorldEntity block = SEBlockFunctions.findClosestBlock(state.wom, selector.apply(state));
                    if (block == null) return null;

                    Vec3 destination;
                    if (block.getStringProperty("blockType").contains("ButtonPanel"))
                        destination = (Vec3) block.getProperty("centerPosition");
                    else
                        destination = SEBlockFunctions.getSideCenterPoint(block, side, delta + 1.5f);

                    var agentSq = state.getGridPos(state.centerPos()) ;
                    if (agentSq instanceof Octree)
                        if (((Octree) agentSq).label == Label.BLOCKED && ((Octree) agentSq).boundary.size > 0.625) {
                            ((Octree) agentSq).subdivide(((Octree) agentSq).label);
                            agentSq = state.getGridPos(state.centerPos());
                        }
                    var destinationSq = state.getGridPos(destination) ;
                    var destinationSqCenterPos = state.getBlockCenter(destinationSq) ;
                    if(Vec3.sub(destinationSqCenterPos, state.centerPos()).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {

                        // the agent is already at the destination. Just return the path, and indicate that
                        // we have arrived at the destination:
                        return new Pair<>(state.currentPathToFollow, true) ;
                    }
                    int currentPathLength = state.currentPathToFollow.size() ;
                    if (currentPathLength == 0
                            || ! destinationSq.equals(state.currentPathToFollow.get(currentPathLength - 1)))
                    {  // there is no path planned, or there is an ongoing path, but it goes to a different target
                        if (state instanceof UUSeAgentState3DOctree) {
                            List<Octree> path = state.pathfinder.findPath(state.getGrid(), agentSq, destinationSq);
                            if (path == null) {
                                // the pathfinder cannot find a path. The tactic is then not enabled:
                                System.out.println("### NO path to " + destination);
                                return null;
                            }
                            path = smoothenOctreePath(path);
                            System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState3DOctree) state, path));
                            return new Pair<>(path, false);
                        }
                        else {
                            List<DPos3> path = state.pathfinder.findPath(state.getGrid(), agentSq, destinationSq);
                            if (path == null) {
                                // the pathfinder cannot find a path. The tactic is then not enabled:
                                System.out.println("### NO path to " + destination);
                                return null;
                            }
                            path = smoothenPath(path);
                            if (state instanceof UUSeAgentState2D) // cursed way to do it
                                System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState2D) state, path));
                            else if (state instanceof UUSeAgentState3DVoxelGrid)
                                System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState3DVoxelGrid) state, path));
                            return new Pair<>(path, false);
                        }
                    }
                    else {
                        // the currently followed path leads to the specified destination, and  furthermore we are not
                        // at the destination yet:
                        return new Pair<>(state.currentPathToFollow,false) ;
                    }
                })
                .lift();
    }

    /**
     * A tactic; if executed repeatedly it guides the agent to get to some distance close to a
     * given destination, if it is reachable. 2D grid-based pathfinding is used to do this.
     * To be more precise, the tactic targets the square S on the grid where the destination is
     * located. Then it travels to some point with the distance at most d to the center of S.
     * D is the square root of THRESHOLD_SQUARED_DISTANCE_TO_SQUARE.
     * <p>
     * The tactic returns the resulting new position and forward-orientation of the agent.
     */
    public static Tactic navigateToTAC(Vec3 destination) {

        return action("navigateTo").do2((UUSeAgentState state)
                        -> (Pair<List<DPos3>, Boolean> queryResult) -> {
                    var path = queryResult.fst ;
                    var arrivedAtDestination = queryResult.snd ;

                    Timer.moveStart = Instant.now();

                    if (state instanceof UUSeAgentState2D) {
                        // check first if we should turn on/off jetpack:
                        if (state.wom.position.y - state.getOrigin().y <= NavGrid.AGENT_HEIGHT
                                && path.get(0).y == 0 && state.jetpackRunning()
                        ) {
                            state.env().getController().getCharacter().turnOffJetpack();
                        } else {
                            if (path.get(0).y > 0 && !state.jetpackRunning()) {
                                state.env().getController().getCharacter().turnOnJetpack();
                                //state.env().getController().getAdmin().getCharacter().use();
                            }
                        }
                    } else if (state instanceof UUSeAgentState3DVoxelGrid || state instanceof UUSeAgentState3DOctree) {
                        // turn on jetpack if not already on
                        if (!state.jetpackRunning())
                            state.env().getController().getCharacter().turnOnJetpack();
                    }

                    if (arrivedAtDestination) {
                        state.currentPathToFollow.clear();
                        Timer.endMove();
                        if (state instanceof UUSeAgentState3DVoxelGrid
                            || state instanceof UUSeAgentState3DOctree
                            || state.jetpackRunning()) {
                            return new Pair<>(state.centerPos(), state.orientationForward());
                        }
                        else {
                            return new Pair<>(state.wom.position, state.orientationForward());
                        }
                    }
                    // else we are not at the destination yet...

                    // set currentPathToFollow:
                    state.currentPathToFollow = path ;

                    // follow the path, direct the agent to the next node in the path (actually, the first
                    // node in the path, since we remove a node if it is passed):
                    var nextNode = state.currentPathToFollow.get(0) ;
                    var nextNodePos = state.getBlockCenter(nextNode) ;
                    if(Vec3.sub(nextNodePos, state.centerPos()).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {
                        // agent is already in the same square as the next-node destination-square. Mark the node
                        // as reached (so, we remove it from the plan):
                        state.currentPathToFollow.remove(0) ;
                        Timer.endMove();
                        return new Pair<>(state.centerPos(), state.orientationForward()) ;
                    }
                    CharacterObservation obs;
                    // disabling rotation for now
                    obs = yTurnTowardACT(state, nextNodePos, 0.8f, 10) ;
                    if (obs != null) {
                        // we did turning, we won't move.
                        Timer.endMove();
                        return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward())) ;
                    }
//                    obs = zTurnTowardACT(state, nextNodePos, 0.8f,5) ;
//                    if (obs != null) {
//                        // we did turning, we won't move.
//                        return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward())) ;
//                    }
                    if (state instanceof UUSeAgentState3DVoxelGrid) {
                        obs = moveToward((UUSeAgentState3DVoxelGrid) state, nextNodePos, 20);
                    }
                    else if (state instanceof UUSeAgentState3DOctree) {
                        obs = moveToward((UUSeAgentState3DOctree) state, nextNodePos, 20);
                    }
                    else {
                        obs = moveToward(state, nextNodePos, 20);
                    }
                    Timer.endMove();
                    return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward()))  ;
                } )
                .on((UUSeAgentState state)  -> {
                    if (state.wom==null) return null ;
                    var agentSq = state.getGridPos(state.centerPos()) ;
                    if (agentSq instanceof Octree)
                        if (((Octree) agentSq).label == Label.BLOCKED && ((Octree) agentSq).boundary.size > 0.625) {
                            ((Octree) agentSq).subdivide(((Octree) agentSq).label);
                            agentSq = state.getGridPos(state.centerPos());
                        }
                    var destinationSq = state.getGridPos(destination) ;
                    var destinationSqCenterPos = state.getBlockCenter(destinationSq) ;
                    if(Vec3.sub(destinationSqCenterPos, state.centerPos()).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {
                        // the agent is already at the destination. Just return the path, and indicate that
                        // we have arrived at the destination:
                        return new Pair<>(state.currentPathToFollow, true) ;
                    }
                    int currentPathLength = state.currentPathToFollow.size() ;
                    if (currentPathLength == 0
                            || ! destinationSq.equals(state.currentPathToFollow.get(currentPathLength - 1)))
                    {  // there is no path planned, or there is an ongoing path, but it goes to a different target
                        if (state instanceof UUSeAgentState3DOctree) {
                            List<Octree> path = state.pathfinder.findPath(state.getGrid(), agentSq, destinationSq);
                            if (path == null) {
                                // the pathfinder cannot find a path. The tactic is then not enabled:
                                System.out.println("### NO path to " + destination);
                                return null;
                            }
                            path = smoothenOctreePath(path);
                            System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState3DOctree) state, path));
                            return new Pair<>(path, false);
                        }
                        else {
                            List<DPos3> path = state.pathfinder.findPath(state.getGrid(), agentSq, destinationSq);
                            if (path == null) {
                                // the pathfinder cannot find a path. The tactic is then not enabled:
                                System.out.println("### NO path to " + destination);
                                return null;
                            }
                            path = smoothenPath(path);
                            if (state instanceof UUSeAgentState2D) // cursed way to do it
                                System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState2D) state, path));
                            else if (state instanceof UUSeAgentState3DVoxelGrid)
                                System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState3DVoxelGrid) state, path));
                            return new Pair<>(path, false);
                        }
                    }
                    else {
                        // the currently followed path leads to the specified destination, and  furthermore we are not
                        // at the destination yet:
                        return new Pair<>(state.currentPathToFollow,false) ;
                    }
                })
                .lift();
    }

    /**
     * Optimize a path such that every segment v1,v2,v3 that lie on the same line, then we remove
     * v2 from the path.
     */
    public static List<DPos3> smoothenPath(List<DPos3> path) {
        if(path.size() <= 2) return path ;
        int k = 0 ;
        while(k < path.size() - 2) {
            var v1 = path.get(k).toVec3() ;
            var v2 = path.get(k+1).toVec3() ;
            var v3 = path.get(k+2).toVec3() ;

            var gradient_v1v2 = Vec3.sub(v2,v1).normalized() ;
            var gradient_v2v3 = Vec3.sub(v3,v2).normalized() ;
            float cos_alpha = Vec3.dot(gradient_v1v2, gradient_v2v3) ;
            if (cos_alpha >= 0.99) {
                // the gradients v1-->v2 and v2-->v3 are the same or almost the same,
                // so v1--v2--v3 are on the same line, or almost at the same line.
                // We then remove v2:
                path.remove(k+1) ;
            }
            else {
                k++ ;
            }
        }
        return path ;
    }

    /**
     * Optimize a path such that every segment v1,v2,v3 that lie on the same line, then we remove
     * v2 from the path.
     */
    public static List<Octree> smoothenOctreePath(List<Octree> path) {
        if(path.size() <= 2) return path ;
        int k = 0 ;
        while(k < path.size() - 2) {
            var v1 = path.get(k).boundary.center();
            var v2 = path.get(k+1).boundary.center();
            var v3 = path.get(k+2).boundary.center();

            var gradient_v1v2 = Vec3.sub(v2,v1).normalized() ;
            var gradient_v2v3 = Vec3.sub(v3,v2).normalized() ;
            float cos_alpha = Vec3.dot(gradient_v1v2, gradient_v2v3) ;

            float threshold = 0.95f;

            if (cos_alpha >= threshold) {
                // the gradients v1-->v2 and v2-->v3 are the same or almost the same,
                // so v1--v2--v3 are on the same line, or almost at the same line.
                // We then remove v2:
                path.remove(k+1) ;
            }
            else {
                k++ ;
            }
        }
        return path ;
    }

    /**
     * A tactic; if executed repeatedly it guides the agent to get to some distance close to a
     * location next to an unknown location, if it is reachable. Pathfinding is used to do this.
     * To be more precise, the tactic targets the node N which neighbours an unknown node.
     * Then it travels to some point with the distance at most d to the center of S.
     * D is the square root of THRESHOLD_SQUARED_DISTANCE_TO_SQUARE.
     * <p>
     * It does this until there are no more reachable nodes next to an unknown node.
     * <p>
     * The tactic returns the resulting new position and forward-orientation of the agent.
     */
    public static Tactic exploreTAC() {
        return action("explore")
                .do2((UUSeAgentState state) -> (Pair<List<DPos3>, Boolean> queryResult) -> {
                    var path = queryResult.fst;
                    var arrivedAtDestination = queryResult.snd;

                    Timer.moveStart = Instant.now();

                    var agentSq = state.getGridPos(state.centerPos());
                    for (var next : state.getGrid().neighbours_explore(agentSq)) {
                         if (state.getGrid().isUnknown(next)) {
                             state.getGrid().updateUnknown(state.centerPos(), OBSERVATION_RADIUS);

                             state.currentPathToFollow.clear();
                             Timer.endMove();
                             //return new Pair<>(state.centerPos(), state.orientationForward());
                             return false;
                         }
                    }
                    // else we are not at the destination yet...

                    if (state instanceof UUSeAgentState2D) {
                        // check first if we should turn on/off jetpack:
                        if (state.wom.position.y - state.getOrigin().y <= NavGrid.AGENT_HEIGHT
                                && path.get(0).y == 0 && state.jetpackRunning()
                        ) {
                            state.env().getController().getCharacter().turnOffJetpack();
                        } else {
                            if (path.get(0).y > 0 && !state.jetpackRunning())
                                state.env().getController().getCharacter().turnOnJetpack();
                        }
                    } else if (state instanceof UUSeAgentState3DVoxelGrid || state instanceof UUSeAgentState3DOctree) {
                        // turn on jetpack if not already on
                        if (!state.jetpackRunning())
                            state.env().getController().getCharacter().turnOnJetpack();
                    }

                    // set currentPathToFollow:
                    state.currentPathToFollow = path ;

                    // follow the path, direct the agent to the next node in the path (actually, the first
                    // node in the path, since we remove a node if it is passed):
                    var nextNode = state.currentPathToFollow.get(0);
                    var nextNodePos = state.getBlockCenter(nextNode);
                    if(Vec3.sub(nextNodePos, state.centerPos()).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {
                        // agent is already in the same square as the next-node destination-square. Mark the node
                        // as reached (so, we remove it from the plan):
                        state.currentPathToFollow.remove(0) ;
                        Timer.endMove();
                        //return new Pair<>(state.centerPos(), state.orientationForward()) ;
                        return false;
                    }

                    CharacterObservation obs;
                    // disabling rotation for now
                    obs = yTurnTowardACT(state, nextNodePos, 0.8f, 10) ;
                    if (obs != null) {
                        // we did turning, we won't move.
                        Timer.endMove();
                        //return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward())) ;
                        return false;
                    }

                    if (state instanceof UUSeAgentState3DVoxelGrid) {
                        obs = moveToward((UUSeAgentState3DVoxelGrid) state, nextNodePos, 20);
                    } else if (state instanceof UUSeAgentState3DOctree) {
                        obs = moveToward((UUSeAgentState3DOctree) state, nextNodePos, 20);
                    } else {
                        obs = moveToward(state, nextNodePos, 20);
                    }

                    Timer.endMove();
                    //return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward()));
                    return false;
                })
                .on((UUSeAgentState state) -> {
                    if (state.wom==null) return null ;
                    var agentSq = state.getGridPos(state.centerPos());

                    int currentPathLength = state.currentPathToFollow.size() ;
                    if (currentPathLength == 0)
                    {  // there is no path planned, or there is an ongoing path, but it goes to a different target
                        if (state instanceof UUSeAgentState3DOctree) {
                            List<Octree> path = state.pathfinder.explore(state.getGrid(), agentSq);
                            if (path == null) {
                                // the pathfinder cannot find a path. The tactic is then not enabled:
                                System.out.println("### NO path to an unknown node");
                                return null;
                            }
                            path = smoothenOctreePath(path);
                            System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState3DOctree) state, path));
                            return new Pair<>(path, false);
                        }
                        else {
                            List<DPos3> path = state.pathfinder.explore(state.getGrid(), agentSq);
                            if (path == null) {
                                // the pathfinder cannot find a path. The tactic is then not enabled:
                                System.out.println("### NO path to an unknown node");
                                return null;
                            }
                            path = smoothenPath(path);
                            if (state instanceof UUSeAgentState2D) // cursed way to do it
                                System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState2D) state, path));
                            else if (state instanceof UUSeAgentState3DVoxelGrid)
                                System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState3DVoxelGrid) state, path));
                            return new Pair<>(path, false);
                        }
                    }
                    else {
                        return new Pair<>(state.currentPathToFollow,false) ;
                    }
                })
                .lift();
    }

    public static Tactic exploreTAC(Function<UUSeAgentState, Predicate<WorldEntity>> selector,
                                    SEBlockFunctions.BlockSides side,
                                    float delta) {
        return action("explore")
                .do2((UUSeAgentState state) -> (Pair<List<DPos3>, Boolean> queryResult) -> {
                    var path = queryResult.fst;
                    var arrivedAtDestination = queryResult.snd;

                    Timer.moveStart = Instant.now();

                    if (arrivedAtDestination) {
                        state.currentPathToFollow.clear();
                        Timer.endMove();
                        return true;
                    }

                    // set currentPathToFollow:
                    state.currentPathToFollow = path ;

                    var lastNode = state.currentPathToFollow.get(path.size() - 1);
                    var lastNodePos = state.getBlockCenter(lastNode);
                    if (Vec3.sub(lastNodePos, state.centerPos()).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {
                        state.getGrid().updateUnknown(state.centerPos(), OBSERVATION_RADIUS);

                        state.currentPathToFollow.clear();
                        Timer.endMove();
                        return false;
                    }
                    // else we are not at the destination yet...

                    if (state instanceof UUSeAgentState2D) {
                        // check first if we should turn on/off jetpack:
                        if (state.wom.position.y - state.getOrigin().y <= NavGrid.AGENT_HEIGHT
                                && path.get(0).y == 0 && state.jetpackRunning()
                        ) {
                            state.env().getController().getCharacter().turnOffJetpack();
                        } else {
                            if (path.get(0).y > 0 && !state.jetpackRunning()) {
                                state.env().getController().getCharacter().turnOnJetpack();
                                //state.env().getController().getAdmin().getCharacter().use();
                            }
                        }
                    } else if (state instanceof UUSeAgentState3DVoxelGrid || state instanceof UUSeAgentState3DOctree) {
                        // turn on jetpack if not already on
                        if (!state.jetpackRunning())
                            state.env().getController().getCharacter().turnOnJetpack();
                    }

                    // follow the path, direct the agent to the next node in the path (actually, the first
                    // node in the path, since we remove a node if it is passed):
                    var nextNode = state.currentPathToFollow.get(0);
                    var nextNodePos = state.getBlockCenter(nextNode);
                    if(Vec3.sub(nextNodePos, state.centerPos()).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {
                        // agent is already in the same square as the next-node destination-square. Mark the node
                        // as reached (so, we remove it from the plan):
                        state.currentPathToFollow.remove(0) ;
                        Timer.endMove();
                        return false;
                    }

                    CharacterObservation obs;
                    // disabling rotation for now
                    obs = yTurnTowardACT(state, nextNodePos, 0.8f, 10) ;
                    if (obs != null) {
                        // we did turning, we won't move.
                        Timer.endMove();
                        return false;
                    }

                    if (state instanceof UUSeAgentState3DVoxelGrid) {
                        moveToward((UUSeAgentState3DVoxelGrid) state, nextNodePos, 20);
                    } else if (state instanceof UUSeAgentState3DOctree) {
                        moveToward((UUSeAgentState3DOctree) state, nextNodePos, 20);
                    } else {
                        moveToward(state, nextNodePos, 20);
                    }
                    Timer.endMove();
                    return false;
                })
                .on((UUSeAgentState state) -> {
                    if (state.wom==null) return null ;
                    var agentSq = state.getGridPos(state.centerPos());
                    if (agentSq instanceof Octree)
                        if (((Octree) agentSq).label == Label.BLOCKED && ((Octree) agentSq).boundary.size > 0.625) {
                            ((Octree) agentSq).subdivide(((Octree) agentSq).label);
                            agentSq = state.getGridPos(state.centerPos());
                        }

                    int currentPathLength = state.currentPathToFollow.size() ;
                    if (currentPathLength == 0)
                    {  // there is no path planned, or there is an ongoing path, but it goes to a different target

                        // if the target block is a reachable location, then the tactic is not enabled
                        WorldEntity block = SEBlockFunctions.findClosestBlock(state.wom, selector.apply(state));
                        if (block != null) {
                            Vec3 targetLocation = SEBlockFunctions.getSideCenterPoint(block, side, delta + 1.5f);
                            var destinationSq = state.getGridPos(targetLocation);
                            if (!state.getGrid().isUnknown(destinationSq)) {

                                var path = state.pathfinder.findPath(state.getGrid(), agentSq, destinationSq);
                                if (path != null) {
                                    System.out.println("### found a path to the goal");
                                    return new Pair<>(null, true);
                                }
                            }
                        }
                        // otherwise, plan a new explore path
                        if (state instanceof UUSeAgentState3DOctree) {
                            List<Octree> path = state.pathfinder.explore(state.getGrid(), agentSq);
                            if (path == null) {
                                // the pathfinder cannot find a path. The tactic is then not enabled:
                                System.out.println("### NO path to an unknown node");
                                return new Pair<>(null, true);
                            }
                            path = smoothenOctreePath(path);
                            System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState3DOctree) state, path));
                            return new Pair<>(path, false);
                        }
                        else {
                            List<DPos3> path = state.pathfinder.explore(state.getGrid(), agentSq);
                            if (path == null) {
                                // the pathfinder cannot find a path. The tactic is then not enabled:
                                System.out.println("### NO path to an unknown node");
                                return new Pair<>(null, true);
                            }
                            path = smoothenPath(path);
                            if (state instanceof UUSeAgentState2D) // cursed way to do it
                                System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState2D) state, path));
                            else if (state instanceof UUSeAgentState3DVoxelGrid)
                                System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState3DVoxelGrid) state, path));
                            return new Pair<>(path, false);
                        }
                    }
                    else {
                        return new Pair<>(state.currentPathToFollow,false) ;
                    }
                })
                .lift();
    }

    public static Tactic exploreTAC(Vec3 destination) {
        return action("explore")
                .do2((UUSeAgentState state) -> (Pair<List<DPos3>, Boolean> queryResult) -> {
                    var path = queryResult.fst;
                    var arrivedAtDestination = queryResult.snd;

                    Timer.moveStart = Instant.now();

                    if (arrivedAtDestination) {
                        state.currentPathToFollow.clear();
                        Timer.endMove();
                        return true;
                    }

                    // set currentPathToFollow:
                    state.currentPathToFollow = path ;

                    var lastNode = state.currentPathToFollow.get(path.size() - 1);
                    var lastNodePos = state.getBlockCenter(lastNode);
                    if (Vec3.sub(lastNodePos, state.centerPos()).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {
                        state.getGrid().updateUnknown(state.centerPos(), OBSERVATION_RADIUS);

                        state.currentPathToFollow.clear();
                        Timer.endMove();
                        return new Pair<>(state.centerPos(), state.orientationForward());
                    }
                    // else we are not at the destination yet...

                    if (state instanceof UUSeAgentState2D) {
                        // check first if we should turn on/off jetpack:
                        if (state.wom.position.y - state.getOrigin().y <= NavGrid.AGENT_HEIGHT
                                && path.get(0).y == 0 && state.jetpackRunning()
                        ) {
                            state.env().getController().getCharacter().turnOffJetpack();
                        } else {
                            if (path.get(0).y > 0 && !state.jetpackRunning()) {
                                state.env().getController().getCharacter().turnOnJetpack();
                                //state.env().getController().getAdmin().getCharacter().use();
                            }
                        }
                    } else if (state instanceof UUSeAgentState3DVoxelGrid || state instanceof UUSeAgentState3DOctree) {
                        // turn on jetpack if not already on
                        if (!state.jetpackRunning())
                            state.env().getController().getCharacter().turnOnJetpack();
                    }

                    // follow the path, direct the agent to the next node in the path (actually, the first
                    // node in the path, since we remove a node if it is passed):
                    var nextNode = state.currentPathToFollow.get(0);
                    var nextNodePos = state.getBlockCenter(nextNode);
                    if(Vec3.sub(nextNodePos, state.centerPos()).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {
                        // agent is already in the same square as the next-node destination-square. Mark the node
                        // as reached (so, we remove it from the plan):
                        state.currentPathToFollow.remove(0) ;
                        Timer.endMove();
                        return new Pair<>(state.centerPos(), state.orientationForward());
                    }

                    CharacterObservation obs;
                    // disabling rotation for now
                    obs = yTurnTowardACT(state, nextNodePos, 0.8f, 10) ;
                    if (obs != null) {
                        // we did turning, we won't move.
                        Timer.endMove();
                        return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward())) ;
                    }

                    if (state instanceof UUSeAgentState3DVoxelGrid) {
                        obs = moveToward((UUSeAgentState3DVoxelGrid) state, nextNodePos, 20);
                    } else if (state instanceof UUSeAgentState3DOctree) {
                        obs = moveToward((UUSeAgentState3DOctree) state, nextNodePos, 20);
                    } else {
                        obs = moveToward(state, nextNodePos, 20);
                    }
                    Timer.endMove();
                    return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward()));
                })
                .on((UUSeAgentState state) -> {
                    if (state.wom==null) return null ;
                    var agentSq = state.getGridPos(state.centerPos());
                    if (agentSq instanceof Octree)
                        if (((Octree) agentSq).label == Label.BLOCKED && ((Octree) agentSq).boundary.size > 0.625) {
                            ((Octree) agentSq).subdivide(((Octree) agentSq).label);
                            agentSq = state.getGridPos(state.centerPos());
                        }

                    int currentPathLength = state.currentPathToFollow.size() ;
                    if (currentPathLength == 0)
                    {  // there is no path planned, or there is an ongoing path, but it goes to a different target
                        if (state instanceof UUSeAgentState3DOctree) {
                            List<Octree> path = state.pathfinder.exploreTo(state.getGrid(), agentSq, destination);
                            if (path == null) {
                                // the pathfinder cannot find a path. The tactic is then not enabled:
                                System.out.println("### NO path to an unknown node");
                                return new Pair<>(null, true);
                            }
                            path = smoothenOctreePath(path);
                            System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState3DOctree) state, path));
                            return new Pair<>(path, false);
                        }
                        else {
                            List<DPos3> path = state.pathfinder.exploreTo(state.getGrid(), agentSq, destination);

                            if (path == null) {
                                // the pathfinder cannot find a path. The tactic is then not enabled:
                                System.out.println("### NO path to an unknown node");
                                return new Pair<>(null, true);
                            }
                            path = smoothenPath(path);
                            if (state instanceof UUSeAgentState2D) // cursed way to do it
                                System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState2D) state, path));
                            else if (state instanceof UUSeAgentState3DVoxelGrid)
                                System.out.println("### PATH: " + PrintInfos.showPath((UUSeAgentState3DVoxelGrid) state, path));
                            return new Pair<>(path, false);
                        }
                    }
                    else {
                        return new Pair<>(state.currentPathToFollow,false) ;
                    }
                })
                .lift();
    }
}
