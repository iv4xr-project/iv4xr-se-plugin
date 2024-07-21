package uuspaceagent;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Timer {
    public static long totalTime=0, pathfindingTime=0, exploreTime=0, getNeighbourTime=0, moveTime=0,
            addToGridTime=0, removeFromGridTime=0, updateDoorsTime=0,
            expandTime=0, updateStateTime=0, initializeGridTime=0;
    private static List<Long> pathfindingTimes = new ArrayList<Long>();
    private static List<Long> explorationTimes = new ArrayList<Long>();
    private static List<Long> expandTimes = new ArrayList<Long>();
    private static int pathfinding=0, explore=0, getNeighbour=0, move=0,
            addToGrid=0, removeFromGrid=0, updateDoors=0,
            expand=0, updateState=0;
    public static Instant testStart, pathfindingStart, exploreStart, getNeighbourStart, moveStart,
            addToGridStart, removeFromGridStart, updateDoorsStart,
            expandStart, updateStateStart, initializeGridStart;

    public static void endTest() {
        Instant end = Instant.now();
        totalTime += Duration.between(testStart, end).toMillis();
    }
    public static void endPathfinding() {
        Instant end = Instant.now();
        long time = Duration.between(pathfindingStart, end).toNanos();
        pathfindingTimes.add(time);
        pathfindingTime += time;
        pathfinding++;
    }
    public static void endExplore() {
        Instant end = Instant.now();
        long time = Duration.between(exploreStart, end).toNanos();
        explorationTimes.add(time);
        exploreTime += time;
        explore++;
    }
    public static void endGetNeighbour() {
        Instant end = Instant.now();
        getNeighbourTime += Duration.between(getNeighbourStart, end).toNanos();
        getNeighbour++;
    }
    public static void endMove() {
        Instant end = Instant.now();
        moveTime += Duration.between(moveStart, end).toMillis();
        move++;
    }
    public static void endAddToGrid() {
        Instant end = Instant.now();
        addToGridTime += Duration.between(addToGridStart, end).toNanos();
        addToGrid++;
    }
    public static void endRemoveFromGrid() {
        Instant end = Instant.now();
        removeFromGridTime += Duration.between(removeFromGridStart, end).toNanos();
        removeFromGrid++;
    }
    public static void endUpdateDoors() {
        Instant end = Instant.now();
        updateDoorsTime += Duration.between(updateDoorsStart, end).toNanos();
        updateDoors++;
    }
    public static void endExpand() {
        Instant end = Instant.now();
        long time = Duration.between(expandStart, end).toNanos();
        if (time <= 0) return;
        expandTimes.add(time);
        expandTime += time;
        expand++;
    }
    public static void endUpdateState() {
        Instant end = Instant.now();
        updateStateTime += Duration.between(updateStateStart, end).toMillis();
        updateState++;
    }
    public static void endInitializeGrid() {
        Instant end = Instant.now();
        initializeGridTime += Duration.between(initializeGridStart, end).toNanos();
    }

    public static void print() {
        System.out.println("-------------------------------------");
        System.out.println("Initialize grid time:  "+initializeGridTime +" ns");
        System.out.println("Add to grid time:      "+addToGridTime      +" ns / "+addToGrid);
        System.out.println("Remove from grid time: "+removeFromGridTime +" ns / "+removeFromGrid);
        System.out.println("Update doors time:     "+updateDoorsTime    +" ns / "+updateDoors);

        System.out.println("Total Expand time:     "+expandTime         +" ns / "+expand);
        expandTimes.forEach(time -> System.out.println("-> "+time));

        System.out.println("Pathfinding time:      "+pathfindingTime    +" ns / "+pathfinding);
        pathfindingTimes.forEach(time -> System.out.println("-> "+time));

        System.out.println("Explore time:          "+exploreTime        +" ns / "+explore);
        explorationTimes.forEach(time -> System.out.println("-> "+time));

        System.out.println("Get neighbour time:    "+getNeighbourTime   +" ns / "+getNeighbour);
        System.out.println("Move time:             "+moveTime           +" ms / "+move);
        System.out.println("Update state time:     "+updateStateTime    +" ms / "+updateState);
        System.out.println("Total time:            "+totalTime          +" ms");
        System.out.println("-------------------------------------");
    }
}
