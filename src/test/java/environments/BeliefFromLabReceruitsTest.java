package environments;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nl.uu.cs.aplib.AplibEDSL.* ;
import nl.uu.cs.aplib.mainConcepts.* ;
import agents.LabRecruitsTestAgent;
import agents.TestSettings;
import game.LabRecruitsTestServer;
import game.Platform;
import world.BeliefState;

public class BeliefFromLabReceruitsTest {

	private LabRecruitsTestServer labRecruitsTestServer ;

    @BeforeEach
    void start() {
    	// set this to true to make the game's graphic visible:
    	var useGraphics = false ;
    	SocketReaderWriter.debug = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
    	labRecruitsTestServer =new LabRecruitsTestServer(
    			useGraphics,
                Platform.PathToLabRecruitsExecutable(labRecruitesExeRootDir));
    	labRecruitsTestServer.waitForGameToLoad();
    }

    @AfterEach
    void close() { 
    	SocketReaderWriter.debug = false ;
    	if(labRecruitsTestServer!=null) labRecruitsTestServer.close(); 
    }
    
    void hit_RETURN() {
		System.out.println("Hit RETURN to continue.") ;
    	new Scanner(System.in) . nextLine() ;
	}
    
    /**
     * Create and deploy an agent with a dummy goal.
     */
    LabRecruitsTestAgent deployAgent(String agentName, String levelName) {
    	GoalStructure goal =  goal("g0")
    			. toSolve(S -> false)
    			. withTactic(action("a").do1(S -> S).lift())
    			. lift();
    	
    	var env = new LabRecruitsEnvironment(new LabRecruitsConfig(levelName)) ;
    	
    	//hit_RETURN() ;
    	
    	var agent = new LabRecruitsTestAgent(agentName) 
    			    . attachState(new BeliefState())
    			    . attachEnvironment(env) 
    			    . setGoal(goal) ;
    	
    	return agent ;
    }
    
    /**
     * Some basic checks the BeliefState.
     */
    @Test
    public void test0() {
    	assertTrue(labRecruitsTestServer != null) ;
        // test using the "minimal" level:
    	var level = "minimal" ;
    	System.out.println(">>> level: " + level) ;
    	var agent = deployAgent("agent0",level) ;
    	
    	assertTrue(agent.getState().worldmodel != null) ;
    	assertTrue(agent.getState().worldmodel.agentId.equals("agent0")) ;
    	
    	agent.update();
    	assertTrue(agent.getState().worldmodel.getElement("button0") != null) ;
    	assertTrue(agent.getState().isOn("button0") == false) ;
    	
    	

    }
    
    @Test
    public void test1() {
    	assertTrue(labRecruitsTestServer != null) ;
        // test using the "minimal" level:
    	var level = "square2" ;
    	System.out.println(">>> level: " + level) ;
    	var agent = deployAgent("agent0",level) ;
    	
    	assertTrue(agent.getState().worldmodel != null) ;
    	assertTrue(agent.getState().worldmodel.agentId.equals("agent0")) ;
    	
    	agent.update();
    	
    	var belief = agent.getState() ;
    	var wom = agent.getState().worldmodel ;
    	
    	assertTrue(wom.getElement("button0") != null) ;
    	assertTrue(belief.isOn("button0") == false) ;
    	assertTrue(belief.isOpen("door0") == false) ;
    	
    	assertTrue(wom.getElement("button1") != null) ;
    	assertTrue(wom.getElement("button0") != null) ;
    	assertTrue(wom.getElement("escreen0") != null) ;
    	assertTrue(wom.getElement("escreen0").getStringProperty("color").equals("CS 0 0 0")) ;
    	
    	
    	//hit_RETURN()  ;
    }
}
