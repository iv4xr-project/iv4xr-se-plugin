package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;

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

}
