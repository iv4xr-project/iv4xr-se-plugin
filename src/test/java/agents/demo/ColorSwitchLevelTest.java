/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/
package agents.demo;

import agents.LabRecruitsTestAgent;
import agents.TestSettings;
import agents.tactics.GoalLib;
import agents.tactics.TacticLib;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import eu.iv4xr.framework.mainConcepts.TestGoal;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import logger.PrintColor;
import nl.uu.cs.aplib.Logging;
import nl.uu.cs.aplib.mainConcepts.Environment;
import nl.uu.cs.aplib.multiAgentSupport.ComNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import game.Platform;
import game.LabRecruitsTestServer;
import world.BeliefState;

import static agents.TestSettings.*;
import static eu.iv4xr.framework.Iv4xrEDSL.assertTrue_;
import static nl.uu.cs.aplib.AplibEDSL.SEQ;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

/**
 * This class will check if the demo for the color switch level works
 */
public class ColorSwitchLevelTest {

    private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	// TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
    	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(labRecruitsTestServer!=null) labRecruitsTestServer.close(); }
    
    void instrument(Environment env) {
    	env.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();
    }

    /**
     * This demo will tests the color switch level
     */
    @Test
    public void testColorSwitchDemo() throws InterruptedException{
        ComNode communication = new ComNode();

        var env = new LabRecruitsEnvironment(new EnvironmentConfig("CLRSWTCH"));
        if(USE_INSTRUMENT) instrument(env) ;

        QArrayList<LabRecruitsTestAgent> agents = new QArrayList<>(new LabRecruitsTestAgent[] {
                (LabRecruitsTestAgent)createButtonPressAgent(env).registerTo(communication),
                (LabRecruitsTestAgent)createColorScreenCheckAgent(env).registerTo(communication)
        });

    	if(TestSettings.USE_GRAPHICS) {
    		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
    		new Scanner(System.in) . nextLine() ;
    	}
    	
        // press play in Unity
        if (! env.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");

        int tick = 0;
        // run the agent until it solves its goal:
        while (!agents.allTrue(LabRecruitsTestAgent::success)){

            System.out.println(PrintColor.GREEN("TICK " + tick + ":"));

            // only updates in progress
            for(var agent : agents.where(agent -> !agent.success())){
                agent.update();
                if(agent.success()){
                    agent.printStatus();
                }
            }
            Thread.sleep(30);
            tick++;
        }

        //check that no verdict failed
        //assertEquals(0, agents.get(1).getTestDataCollector().getNumberOfFailVerdictsSeen());

        //check if all verdicts succeeded
        assertEquals(3, agents.get(1).getTestDataCollector().getNumberOfPassVerdictsSeen());

        Logging.getAPLIBlogger().info("DEMO END.");

        if (!env.close())
            throw new InterruptedException("Unity refuses to close the Simulation!");
    }

    /**
     * This method will define the first agent in the color switch level which will press the buttons and will return to a base position each time
     * @return The agent
     */
    public static LabRecruitsTestAgent createButtonPressAgent(LabRecruitsEnvironment env){
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent("0","")
        		                     . attachState(new BeliefState())
        		                     . attachEnvironment(env);
 
        //set the goals
        agent.setGoal(SEQ(
                GoalLib.entityIsInteracted("CB3"), //move to the red button and interact with it
                GoalLib.pingSent("0", "1").lift(), //send a ping to the other agent
                GoalLib.entityIsInteracted("CB3"), //move to the red button and interact with it
                GoalLib.entityIsInteracted("CB1"), //move to the blue button and interact with it
                GoalLib.pingSent("0", "1").lift(), //send a ping to the other agent
                GoalLib.entityIsInteracted("CB1"), //move to the blue button and interact with it
                GoalLib.entityIsInteracted("CB2"), //move to the green button and interact with it
                GoalLib.pingSent("0", "1").lift(), //send a ping to the other agent
                GoalLib.entityIsInteracted("CB2"))); //move to the green button and interact with it

        return agent;
    }

    /**
     * This method will define the second agent which will check the color screen each time a button is pressed
     * in the color switch level
     * @return The agent
     */
    public static LabRecruitsTestAgent createColorScreenCheckAgent(LabRecruitsEnvironment env){
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent("1", "")
        		                     . attachState(new BeliefState())
                                     . attachEnvironment(env);

        //wait for the ping to check the color screen with the red button
        TestGoal t1 = new TestGoal("Wait for ping").toSolve((BeliefState b) -> {
            if(b.receivedPing){
                b.receivedPing = false;//reset the ping
                return true;
            }
            return false;
        }).withTactic(
                TacticLib.receivePing()
        );

        String info1 = "Check if the color screen is red";
        var g1 = t1.oracle(agent, (BeliefState b) -> assertTrue_("", info1, b.evaluateEntity("CS1", e -> e.property.equals("CS 1 0 0")))
        ).lift();

        //wait for the ping to check the color screen with the blue button
        TestGoal t2 = new TestGoal("Wait for ping").toSolve((BeliefState b) -> {
            if(b.receivedPing){
                b.receivedPing = false;//reset the ping
                return true;
            }
            return false;
        }).withTactic(
                TacticLib.receivePing()
        );

        String info2 = "Check if the color screen is blue";
        var g2 = t2.oracle(agent, (BeliefState b) -> assertTrue_("", info2, b.evaluateEntity("CS1", e -> e.property.equals("CS 0 0 1")))
        ).lift();

        //wait for the ping to check the color screen with the green button
        TestGoal t3 = new TestGoal("Wait for ping").toSolve((BeliefState b) -> {
            if(b.receivedPing){
                b.receivedPing = false;//reset the ping
                return true;
            }
            return false;
        }).withTactic(
                TacticLib.receivePing()
        );

        String info3 = "Check if the color screen is green";
        var g3 = t3.oracle(agent, (BeliefState b) -> assertTrue_("", info3, b.evaluateEntity("CS1", e -> e.property.equals("CS 0 1 0")))).lift();

        //set the goals
        agent.setGoal(SEQ(g1,g2,g3));

        var dataCollector = new TestDataCollector();
        agent.setTestDataCollector(dataCollector);

        return agent;
    }
}
