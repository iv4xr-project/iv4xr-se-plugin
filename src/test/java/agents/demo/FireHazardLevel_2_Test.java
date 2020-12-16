/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/
package agents.demo;

import agents.LabRecruitsTestAgent;
import agents.TestSettings;
import agents.tactics.GoalLib;
import environments.LabRecruitsConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.spatial.Vec3;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import logger.PrintColor;
import nl.uu.cs.aplib.mainConcepts.Environment;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import game.Platform;
import game.LabRecruitsTestServer;
import world.BeliefState;

import static agents.TestSettings.*;
import static nl.uu.cs.aplib.AplibEDSL.SEQ;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

/**
 * In this demo we show a level with plenty of fire hazard. The level has a room with
 * a fire extinguisher which serves as the goal room. The testing task is to verify
 * that this goal room is reachable and such that the agent remains healthy enough
 * (which we will define as retaining at least 0.5 of its original health).
 *
 * The test is not very intelligent though. The agent is programmed to go through a series
 * of a pre-defined way-points that should keep it mostly safe. It still have to find
 * its own way to navigate between the way-points.
 *
 * In this Hazard-level the way-points will direct the agent to avoid the fire by first
 * going to the 2nd floor, and then navigate in the floor towards stairs leading back
 * to the 1ft floor, and then next to the goal room.
 *
 * TO DO: some times, in this level at the end the agent may foolishly explore the first
 * floor, and through fires :| It still survives, but not ideal.
 * We have yet to implement a fire-avoidance tactic. Todo.
 */
public class FireHazardLevel_2_Test {

    private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	//TestSettings.USE_SERVER_FOR_TEST = false ;
    	//TestSettings.USE_GRAPHICS = true ;
       	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
    	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() {
        if(labRecruitsTestServer!=null) labRecruitsTestServer.close();
    }

    void instrument(Environment env) {
    	env.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();
    }

    /**
     * This demo will tests if an agent can escape the fire hazard level.
     * First of the issues is that hazard overwrite each other, so only one
     * is visible to the agent. Working on this.
     *
     * BROKEN fix this!
     */
    @Test
    public void fireHazardDemo() throws InterruptedException{
        //Add the level to the resources and change the string in the environmentConfig on the next line from Ramps to the new level
        var config = new LabRecruitsConfig("HZRDIndirect")
        		. replaceAgentMovementSpeed(0.2f)
        		. replaceLightIntensity(0.3f);
        
    	var env = (LabRecruitsEnvironment) new LabRecruitsEnvironment(config).turnOnDebugInstrumentation();

        LabRecruitsTestAgent agent = createHazardAgent(env) ;

    	if(TestSettings.USE_GRAPHICS) {
    		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
    		new Scanner(System.in) . nextLine() ;
    	}

        // press play in Unity
        if (! env.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");

        int tick = 0;
        int health0 = 0 ;
        // run the agent until it solves its goal:
        while (!agent.success()){
        	agent.update();
        	System.out.println("*** " + tick + ": " + agent.getState().id + " @" + agent.getState().worldmodel.position) ;
            Thread.sleep(30);

            if (tick==0) {
            	health0 = agent.getState().worldmodel.health ;
            }
            /*
        	System.out.println(">>> Seen vertices:") ;
        	for (int v : agent.getState().worldmodel.visibleNavigationNodes) {
        		var v_ = agent.getState().mentalMap.pathFinder.navmesh.vertices[v] ;
        		System.out.println("    " + v_) ;
        	}
        	*/

            tick++;
            /*
            if (tick >= 20) {
            	break ;
            }*/
        }

        // check that the testing task is completed and that the agent still have 'enough'
        // health
        assertTrue(agent.success()) ;
        assertTrue(agent.getState().worldmodel.health >= health0/2) ;

        if (!env.close())
            throw new InterruptedException("Unity refuses to close the Simulation!");
    }

    /**
     * This method will create an agent which will move through the fire hazard level
     */
    public static LabRecruitsTestAgent createHazardAgent(LabRecruitsEnvironment env){
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent("0", "")
                                     . attachState(new BeliefState())
                                     . attachEnvironment(env);

        //the goals are in order
        //add move goals with GoalStructureFactory.reachPositions(new Vec3(1,0,1)) it can take either a single or multiple vec3
        //set interaction with the buttons with GoalStructureFactory.reachAndInteract("CB3") using the id of the button
        //You will probably not want to explore to prevent random behaviour. in order to do this set the waypoints close enough to each other

        agent.setGoal(SEQ(
                GoalLib.positionsVisited(new Vec3(6,0,5), new Vec3(8,0,1), new Vec3(13,4,1)),
                GoalLib.entityInteracted("b4.1"),
                GoalLib.positionsVisited(new Vec3(13,4,3)),
                GoalLib.entityInteracted("b7.1"),
                GoalLib.positionsVisited(new Vec3(9,4,9), new Vec3(8,4,6), new Vec3(5,4,7)),
                GoalLib.entityInteracted("b8.2"),
                GoalLib.positionsVisited(new Vec3(1,4,13)),
                GoalLib.entityInteracted("b5.1"),
                GoalLib.positionsVisited(new Vec3(1,4,22), new Vec3(6,0,22)),
                GoalLib.entityInteracted("b1.1"),
                GoalLib.positionsVisited(new Vec3(5,0,25))
                ));
        return agent;
    }
}
