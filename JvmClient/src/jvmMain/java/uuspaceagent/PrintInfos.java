package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;

import java.util.List;
import java.util.stream.Collectors;

public class PrintInfos {

    public static String indent(String s, int k) {
        String indentation = "" ;
        for(; 0<k; k--) {
            indentation += " " ;
        }
        String ind = indentation ;
        StringBuffer z = new StringBuffer() ;
        int i=0 ;
        for (var line : s.lines().collect(Collectors.toList())) {
            if(i>0) z.append("\n") ;
            z.append(ind) ;
            z.append(line) ;
            i = 1 ;
        }
        return z.toString() ;
    }

    public static String showWorldEntity(WorldEntity we) {
        StringBuffer z = new StringBuffer() ;
        z.append(">> Entity " + we.id + " (type=" + we.type + ")") ;
        z.append("\n  pos   : " + we.position) ;
        z.append("\n  extent: " + we.extent) ;
        z.append("\n  tstamp: " + we.timestamp) ;
        for(var prop : we.properties.entrySet()) {
            z.append("\n  " + prop.getKey() + ": " + prop.getValue()) ;
        }
        if(we.elements.size() > 0) {
            z.append("\n  #sub-elements:" + we.elements.size()) ;
            int i = 0 ;
            for(var f : we.elements.values()) {
                String fstr = "["+ i + "] " + showWorldEntity(f) ;
                z.append("\n" + indent(fstr,6)) ;
                i++ ;
            }
        }
        return z.toString() ;
    }

    public static String showWOMElements(WorldModel wm) {
        StringBuffer z = new StringBuffer() ;
        z.append(">> World Model #elements:" + wm.elements.size()) ;
        int i = 0 ;
        for(var e : wm.elements.values()) {
           String estr = "["+ i + "] " + showWorldEntity(e) ;
           z.append("\n" + indent(estr,3)) ;
           i++ ;
        }
        return z.toString() ;
    }

    public static String showWOMAgent(WorldModel wm) {
        StringBuffer z = new StringBuffer() ;
        z.append(">> agent " + wm.agentId + " @" + wm.position) ;
        //System.out.println(">>> wm.agentid:" + wm.agentId) ;
        var info = wm.elements.get(wm.agentId) ;
        z.append(", hdir:" + info.properties.get("orientationForward")) ;
        z.append(", vdir:" + info.properties.get("orientationUp")) ;
        z.append(", health:" + info.properties.get("healthRatio")) ;
        z.append(", jet:" + info.properties.get("jetpackRunning")) ;
        return z.toString() ;
    }

    /**
     * For printing the obstacles that block a given unit-cube on the nav-grid maintained by
     * the given agent-state.
     */
    public static String showObstacle(UUSeAgentState2D state, DPos3 cube) {
        StringBuffer z = new StringBuffer() ;
        z.append(">> Obstacles on " + cube + ", center:" + state.navgrid.getSquareCenterLocation(cube)) ;
        boolean isBlocking = state.navgrid.knownObstacles.contains(cube) ;
        if (!isBlocking) {
            z.append(" none") ;
            return z.toString() ;
        }
//        int k = 0 ;
//        for(var o : obstacles) {
//            z.append("") ;
//            WorldEntity e = SEBlockFunctions.findWorldEntity(state.wom,o.obstacle) ;
//            String estr = "\n" + indent("> " + showWorldEntity(e),5) ;
//            z.append(estr) ;
//            k++ ;
//        }
        return z.toString() ;
    }

    public static String showPath(UUSeAgentState2D state, List<DPos3> path) {
        StringBuffer z = new StringBuffer() ;
        if (path == null) {
            z.append("the path is null") ;
            return z.toString() ;
        }
        if (path.isEmpty()) {
            z.append("path is empty") ;
            return z.toString() ;
        }
        z.append("path.size: " + path.size()) ;
        int k = 0 ;
        for(var cube : path) {
            z.append("\n" + "> Node " + k + ":" + cube + ", center:" + state.navgrid.getSquareCenterLocation(cube));
            boolean isBlocking = state.navgrid.knownObstacles.contains(cube) ;
            if (!isBlocking) {
                z.append(", no-obstacle") ;
            }
            else {
                z.append(", obstacles: ") ;
//                int m = 0 ;
//                for (var o : obstacles) {
//                    WorldEntity e = SEBlockFunctions.findWorldEntity(state.wom,o.obstacle) ;
//                    if (m>0) z.append(", ") ;
//                    z.append(o.obstacle + "("
//                            + e.properties.get("blockType")
//                            + ", blocking:" + o.isBlocking
//                            + ")") ;
//                    m++ ;
//                }
            }
            k++ ;
        }
        return z.toString() ;
    }

    public static String showPath(UUSeAgentState3DVoxelGrid state, List<DPos3> path) {
        StringBuffer z = new StringBuffer() ;
        if (path == null) {
            z.append("the path is null") ;
            return z.toString() ;
        }
        if (path.isEmpty()) {
            z.append("path is empty") ;
            return z.toString() ;
        }
        z.append("path.size: " + path.size()) ;
        int k = 0 ;
        for(var cube : path) {
            z.append("\n" + "> Node " + k + ":" + cube + ", center:" + state.grid.getCubeCenterLocation(cube));
            if (state.grid.get(cube).label != Label.BLOCKED) {
                z.append(", no-obstacles") ;
            }
            else {
                z.append(", has-obstacles") ;
            }
            k++ ;
        }
        return z.toString() ;
    }

    public static String showPath(UUSeAgentState3DOctree state, List<Octree> path) {
        StringBuffer z = new StringBuffer() ;
        if (path == null) {
            z.append("the path is null") ;
            return z.toString() ;
        }
        if (path.isEmpty()) {
            z.append("path is empty") ;
            return z.toString() ;
        }
        z.append("path.size: " + path.size()) ;
        int k = 0 ;
        for(var node : path) {
            z.append("\n" + "> Node " + String.format("%2d", k) + ": center: (" + String.format("% .4f", node.boundary.center().x)
                + "," + String.format("% .4f", node.boundary.center().y)
                + "," + String.format("% .4f", node.boundary.center().z) + ")");
            if (node.label != Label.BLOCKED) {
                z.append(", no-obstacles") ;
            }
            else {
                z.append(", has-obstacles") ;
            }
            k++ ;
        }
        return z.toString() ;
    }
}
