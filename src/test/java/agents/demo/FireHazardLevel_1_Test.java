/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/
package agents.demo;

import agents.LabRecruitsTestAgent;
import agents.TestSettings;
import agents.tactics.GoalLib;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import game.LabRecruitsTestServer;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import logger.PrintColor;
import nl.uu.cs.aplib.mainConcepts.Environment;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import game.Platform;
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
 */
public class FireHazardLevel_1_Test {

    private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	TestSettings.USE_SERVER_FOR_TEST = false ;
    	TestSettings.USE_GRAPHICS = true ;
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
    
    @Test
    public void fireHazardDemo() throws InterruptedException{
    	
        //the goals are in order
        //add move goals with GoalStructureFactory.reachPositions(new Vec3(1,0,1)) it can take either a single or multiple vec3
        //set interaction with the buttons with GoalStructureFactory.reachAndInteract("CB3") using the id of the button
        //You will probably not want to explore to prevent random behaviour. In order to do this set the waypoints close enough to each other

    	var testingTask = SEQ(
        		GoalLib.positionInCloseRange(new Vec3(6,0,5)).lift(),
                GoalLib.positionInCloseRange(new Vec3(7,0,8)).lift(),
                GoalLib.positionInCloseRange(new Vec3(7,0,11)).lift(),
                GoalLib.positionInCloseRange(new Vec3(5,0,11)).lift(),
                GoalLib.positionInCloseRange(new Vec3(5,0,16)).lift(),
                GoalLib.positionInCloseRange(new Vec3(2,0,16)).lift(),
                GoalLib.positionInCloseRange(new Vec3(1,0,18)).lift(),
                GoalLib.positionInCloseRange(new Vec3(3,0,20)).lift(),
                GoalLib.positionInCloseRange(new Vec3(6,0,20)).lift(),
                GoalLib.entityInteracted("b1.1"),
                GoalLib.positionsVisited(new Vec3(5,0,25))
        ) ;
    	
        //Add the level to the resources and change the string in the environmentConfig on the next line from Ramps to the new level
        //var env = new LabRecruitsEnvironment(new EnvironmentConfig("HZRDDirect").replaceAgentMovementSpeed(0.2f));
        var env = new LabRecruitsEnvironment(new EnvironmentConfig("HZRDDirect")) ;
        if(USE_INSTRUMENT) instrument(env) ;
        

    	if(TestSettings.USE_GRAPHICS) {
    		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
    		new Scanner(System.in) . nextLine() ;
    	}
    	
        var agent =  new LabRecruitsTestAgent("0", "")
                     . attachState(new BeliefState())
                     . attachEnvironment(env)
                     . setGoal(testingTask) ;
        
        // press play in Unity
        if (! env.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");

        int health0 = 0  ;
        int tick = 0;
        // run the agent until it solves its goal:
        while (testingTask.getStatus().inProgress()){

            System.out.println("** " + tick + ": agent @"
            		+ agent.getState().worldmodel.position
            		+ ", V=" + agent.getState().derivedVelocity());

            agent.update();
            if (tick==0) {
            	health0 = agent.getState().worldmodel.health ;
            }
             
            if (tick>2000) {
            	break ;
            }
            Thread.sleep(30);
            tick++;
        }

        testingTask.printGoalStructureStatus(); 
        // check that the testing task is completed and that the agent still have 'enough'
        // health
        assertTrue(testingTask.getStatus().success()) ;
        assertTrue(agent.getState().worldmodel.health >= health0/2) ;
        
        if (!env.close())
            throw new InterruptedException("Unity refuses to close the Simulation!");
    }

}
