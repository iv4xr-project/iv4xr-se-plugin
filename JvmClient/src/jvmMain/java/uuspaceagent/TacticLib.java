package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.Tactic;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.model.Vec2;


import java.util.List;

import static nl.uu.cs.aplib.AplibEDSL.* ;

public class TacticLib {

    public static float EPSILON_TO_NODE_IN_2D_PATH_NAVIGATION = 0.4f ;
    public static float SQEPSILON_TO_NODE_IN_2D_PATH_NAVIGATION = 0.16f ;

    /**
     * Expressed in terms of cos(angle). Below is cos(10-degree):
     */
    public static float EPSILON_DIRECTION_ANGLE = (float) Math.cos(Math.toRadians(10f));
    public static float TURNING_SPEED = 20f ;
    /**
     * Walking speed. Set as 10f in the forward direction (relative to the agent's orientation).
     */
    public static spaceEngineers.model.Vec3 WALK_SPEED = new spaceEngineers.model.Vec3(0,0,-10f) ;

    public static Tactic navigateTo(Vec3 destination) {
        return action("navigateTo").do2((USeAgentState state)
                        -> (Pair<List<Pair<Integer,Integer>>, Boolean> queryResult) -> {
                    var path = queryResult.fst ;
                    var arrivedAtDestination = queryResult.snd ;
                    if (arrivedAtDestination) {
                        state.currentPathToFollow.clear();
                        return state ;
                    }
                    // else we are not at the destination yet...

                    // set currentPathToFollow:
                    state.currentPathToFollow = path ;

                    // follow the path, direct the agent to the next node in the path (actually, the first
                    // node in the path, since we remove a node if it is passed):
                    var nextNode = state.currentPathToFollow.get(0) ;
                    var nextNodePos = state.grid2D.getSquareCenterLocation(nextNode) ;
                    var agentPos = state.wom.position ;
                    if(state.grid2D.squareDistanceToSquare(agentPos,nextNode) <= SQEPSILON_TO_NODE_IN_2D_PATH_NAVIGATION) {
                        // agent is already close enough to this next-node destination. Mark the node
                        // as reached (so, we remove it from the plan):
                        state.currentPathToFollow.remove(0) ;
                        return state ;
                    }
                    // direction vector to the next node:
                    Vec3 dirToGo = Vec3.sub(nextNodePos,agentPos).normalized() ;
                    Vec3 agentHdir = state.orientationForward() .normalized();
                    // for calculating 2D rotation we ignore the y-value:
                    dirToGo.y = 0 ;
                    agentHdir.y = 0 ;
                    // angle between the dir-to-go and the agent's own direction (expressed as cos(angle)):
                    var cos_alpha = Vec3.dot(agentHdir,dirToGo) ;
                    float turningSpeed = TURNING_SPEED ;
                    if(cos_alpha >= EPSILON_DIRECTION_ANGLE) {
                        // the angle between the agent's own direction and target direction is less than 10-degree
                        // we reduce the turning-speed:
                        turningSpeed = TURNING_SPEED*0.25f ;
                    }
                    // check if we have to turn clockwise or counter-clockwise
                    Vec3 normalVector = Vec3.cross(agentHdir,dirToGo) ;
                    if (normalVector.y < 0) {
                        // the dir-to-go is to the "left"/counter-clockwise direction
                        turningSpeed = - turningSpeed ;
                    }
                    Vec2 turningVector = new Vec2(0, turningSpeed) ;
                    // Now send a command to move the agent:
                    state.env().getController().getCharacter().moveAndRotate(WALK_SPEED, turningVector, 0) ; // the last is "roll"
                    // moving will take some time. We will now return the state at this moment.
                    // The state will be sampled again after some delta time, in the next agent-update.
                    return state ;
                } )
                .on((USeAgentState state)  -> {
                    if (state.wom==null) return null ;
                    var agentPos = state.wom.position ;
                    var destinationSq = state.grid2D.gridProjectedLocation(destination) ;
                    if (state.grid2D.squareDistanceToSquare(agentPos,destinationSq) <= SQEPSILON_TO_NODE_IN_2D_PATH_NAVIGATION) {
                        // the agent is already at the destination. Just return the path, and indicate that
                        // we have arrived at the destination:
                        return new Pair<>(state.currentPathToFollow,true) ;
                    }
                    int currentPathLength = state.currentPathToFollow.size() ;
                    if (currentPathLength == 0
                            || state.grid2D.squareDistanceToSquare(destination, state.currentPathToFollow.get(currentPathLength - 1)) > SQEPSILON_TO_NODE_IN_2D_PATH_NAVIGATION)
                    {  // there is no path planned, or there is an ongoing path, but it goes to a different target
                        var sqAgent= state.grid2D.gridProjectedLocation(agentPos) ;
                        var destSq = state.grid2D.gridProjectedLocation(destination) ;
                        List<Pair<Integer,Integer>> path = state.pathfinder2D.findPath(state.grid2D, sqAgent, destSq)  ;
                        if (path == null) {
                            // the pathfinder cannot find a path. The tactic is then not enabled:
                            return null ;
                        }
                        path = smoothenPath(path) ;
                        return new Pair<>(path,false) ;
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
    public static List<Pair<Integer,Integer>> smoothenPath(List<Pair<Integer,Integer>> path) {
        if(path.size() <= 2) return path ;
        int k = 0 ;
        while(k < path.size() - 2) {
            var v1 = path.get(k) ;
            var v2 = path.get(k+1) ;
            var v3 = path.get(k+2) ;
            float gradient_v1v2 = gradient2D(v2.fst - v1.fst, v2.snd - v1.snd) ;
            float gradient_v2v3 = gradient2D(v3.fst - v2.fst, v3.snd - v2.snd) ;
            //if( (v2.fst - v1.fst == v3.fst - v2.fst)  && (v2.snd - v1.snd == v3.snd - v2.snd)) {
            if(gradient_v1v2 == gradient_v2v3) {
                // dy and dx between (v1,v2) and between (v2,v3) are the same
                // then they lie along the same line. We then drop v2:
                path.remove(k+1) ;
            }
            else {
                k++ ;
            }
        }
        return path ;
    }


    // for calculating the grasdient dz/dx ... rounded to 0.001
    static float gradient2D(int dx, int dz) {
        if(dx == 0) {
            if (dz>0) return Float.POSITIVE_INFINITY ;
            if (dz<0) return Float.NEGATIVE_INFINITY ;
            return 0 ;
        }
        float gradient = 1000f * ((float) dz) / ((float) dx) ;
        gradient = ((float) Math.round(gradient) )/1000f ;
        return gradient ;
    }

}
