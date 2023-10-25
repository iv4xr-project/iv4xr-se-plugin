package onlineTestCaseGenerator;

import eu.iv4xr.framework.extensions.ltl.SATVerdict;
import eu.iv4xr.framework.mainConcepts.*;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import nl.uu.cs.aplib.mainConcepts.SimpleState;
import nl.uu.cs.aplib.utils.Pair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import spaceEngineers.model.Block;
import uuspaceagent.SEBlockFunctions;
import uuspaceagent.TestUtils;
import uuspaceagent.UUSeAgentState;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static nl.uu.cs.aplib.AplibEDSL.SEQ;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.PrintInfos.showWOMAgent;
import static uuspaceagent.TestUtils.console;
import static uuspaceagent.TestUtils.loadSE;

public class Test_Experiment {

    //a door is open -->  type disjunctive
    Predicate<SimpleState> q2() {
        return S -> {
            var S_ = (UUSeAgentState) S ;
            var isOpen = false;
            WorldEntity targetBlock = S_.previousTargetBlock;
            if(targetBlock != null){
                if(targetBlock.getStringProperty("blockType").contains("LargeBlockSlideDoor")){
                    var checkIsOpen  = SEBlockFunctions.findWorldEntity(S_.wom,targetBlock.id) ;
                    isOpen = (boolean) checkIsOpen.getProperty("isOpen");
                }
            }
            return isOpen;
        };
    }

    //particular block given the position of the block -- type basic
    Predicate<SimpleState> q3() {
        return S -> {
            var S_ = (UUSeAgentState) S ;
            //Vec3 position =  new Vec3(3.75f,-3.75f,-5.0f);  // this is the location for  block in world-3 blocks
            Vec3 position =  new Vec3(17.5f,2.5f,40.0f) ;
            WorldEntity targetBlock = S_.previousTargetBlock;
            if(targetBlock != null) {;
                var entity = SEBlockFunctions.findWorldEntity(S_.wom, targetBlock.id);
                if (targetBlock.position.equals(position) && entity == null) {
                    return true;
                }
            }
            return false;
        };
    }

    //a block should be grinded --> disjunctive type
    // number of block is less than before
     Predicate<SimpleState> q4() {
         final int[] numberOfAssemblersBefore = {-1} ;
        return S -> {
            var S_ = (UUSeAgentState) S ;
            String blockType = "BasicAssembler";
            var assemblers = SEBlockFunctions.getAllBlocks(S_.wom).stream().filter(e -> blockType.equals(e.getStringProperty("blockType"))).collect(Collectors.toList()) ;
            int newNumberOfAssemblers = assemblers.size() ;
            var ok = false ;

            if (numberOfAssemblersBefore[0] >= 0) {
                int newValue = numberOfAssemblersBefore[0] - 1;
                if(newNumberOfAssemblers == newValue) ok= true;
            }
            numberOfAssemblersBefore[0] = newNumberOfAssemblers ;
            return ok ;
        };
    }

    // all blocks should be grinded --> conjunctive type
    Predicate<SimpleState> q5() {
        return S -> {
            var S_ = (UUSeAgentState) S ;
            String blockType = "BasicAssembler";
            var assemblers = SEBlockFunctions.getAllBlocks(S_.wom).stream().filter(e -> blockType.equals(e.getStringProperty("blockType"))).collect(Collectors.toList()) ;
            int newNumberOfAssemblers = assemblers.size() ;
            var ok = false ;
            if(newNumberOfAssemblers == 0) ok= true;
            return ok ;
        };
    }

    // psi part of the specification and add that the number of block in wom is less than before
    Predicate<SimpleState> psi3() {
        return S -> {
            var S_ = (UUSeAgentState) S ;
            var isOpen = false;
            Block targetBlock = S_.env().getController().getObserver().observe().getTargetBlock() ;
            Vec3 position =  new Vec3(3.75f,-3.75f,-5.0f);
            if(targetBlock.getDefinitionId().toString().contains("BasicAssembler") && position.equals(targetBlock.getMaxPosition())){
                var entity = SEBlockFunctions.findWorldEntity(S_.wom,targetBlock.getId()) ;;
                return (Float) entity.getProperty("integrity") <=
                        (Float) S_.wom.before(entity.id,"integrity");
            }
            return false;
        };
    }
    int turn= 0 ;
    TestDataCollector dataCollector = new TestDataCollector();

    //***************************************conjunctive**********
    @Disabled
    @Test
    public void test_generator_conjunctive() throws InterruptedException, IOException {
        var agentAndState = deployAgent("new-3 islands");
        TestAgent agent = agentAndState.fst ;
        agent.setTestDataCollector(dataCollector) ;
        var specs = new Specifications();
        var psi1 = specs.scenarioSpec0();  //eventually integrity of some blocks will decrease.
        var psi2 = specs.scenarioSpec1();  //eventually some assembler blocks will disappear
        agent.addLTL( psi2);
        agent.resetLTLs();

        List<Map<String, List<Object>>>  Specifications= new ArrayList<>();
        String specificationType = "conjunctive";
          String itemType = "BasicAssembler";
          Specifications.add(new HashMap<String, List<Object>>() {{
            put("conjunctive",
                    new ArrayList<Object>() {{
                        add(q5());
                        add("BasicAssembler");
                    }}
            );
        }} );
        generatore(agentAndState,Specifications, specificationType);

        //save position in csv file: generating heat map
        dataCollector.saveTestAgentScalarsTraceAsCSV(agentAndState.fst.getId(),"visits.csv");
        List<Map<String,Number>> tracePosition = agentAndState.fst
                . getTestDataCollector()
                . getTestAgentScalarsTrace(agentAndState.fst.getId())
                . stream()
                . map(event -> event.values) . collect(Collectors.toList());

        //System.out.println("tracePosition  " + tracePosition) ;
        String newFileLocation = System.getProperty("user.dir") +  File.separator+"positions.csv" ;
        BufferedWriter br = new BufferedWriter(new FileWriter(newFileLocation));
        StringBuilder sb = new StringBuilder();
        Map<String, Number> firstRow = tracePosition.get(0);
        for (String key : firstRow.keySet()) {
            sb.append(key);
            sb.append(",");
        }
        sb.append("\n");
        for (Map<String,Number> map : tracePosition) {
            for (Map.Entry<String, Number> entry : map.entrySet()) {
                sb.append(entry.getValue().toString());
                sb.append(",");
            }
            sb.append("\n");
        }
        br.write(sb.toString());
        br.close();
    // End generating heat map
        var ok = agentAndState.fst.evaluateLTLs();
        System.out.println(">>>> LTL results: " + ok);
        //  System.out.println(">>>> psi1 : " + psi1.sat());
        //  assertTrue(psi1.sat() == SATVerdict.SAT);
        assertTrue(psi2.sat() == SATVerdict.SAT);
        TestUtils.closeConnectionToSE(agentAndState.snd);
    }

    //***************************************disjunctive********

   @Disabled
    @Test
    public void test_generator_disjunctive() throws InterruptedException, IOException {
        var agentAndState = deployAgent("world-4 blocks");
        TestAgent agent = agentAndState.fst ;
        agent.setTestDataCollector(dataCollector) ;
        var specs = new Specifications();
        var psi1 = specs.scenarioSpec0();  //eventually integrity of some blocks will decrease.
        var psi2 = specs.scenarioSpec1();  //eventually some assembler blocks will disappear
        agent.addLTL( psi2);
        agent.resetLTLs();

        List<Map<String, List<Object>>>  Specifications= new ArrayList<>();

         String specificationType = "disjunctive";
        //disjunctive scenario to check an item in the type door
//        Specifications.add(new HashMap<String, List<Object>>() {{
//            put("disjunctive",
//                    new ArrayList<Object>() {{
//                        add(q2());
//                        add("LargeBlockSlideDoor");
//                    }}
//            );
//        }} );
        Specifications.add(new HashMap<String, List<Object>>() {{
            put("disjunctive",
                    new ArrayList<Object>() {{
                        add(q4());
                        add("BasicAssembler");
                    }}
            );
        }} );
        generatore(agentAndState, Specifications, specificationType);

        //save position in csv file:
        dataCollector.saveTestAgentScalarsTraceAsCSV(agentAndState.fst.getId(),"visits.csv");
        List<Map<String,Number>> tracePosition = agentAndState.fst
                . getTestDataCollector()
                . getTestAgentScalarsTrace(agentAndState.fst.getId())
                . stream()
                . map(event -> event.values) . collect(Collectors.toList());

        String newFileLocation = System.getProperty("user.dir") +  File.separator+"positions.csv" ;
        BufferedWriter br = new BufferedWriter(new FileWriter(newFileLocation));
        StringBuilder sb = new StringBuilder();
        Map<String, Number> firstRow = tracePosition.get(0);
        for (String key : firstRow.keySet()) {
            sb.append(key);
            sb.append(",");
        }
        sb.append("\n");
        for (Map<String,Number> map : tracePosition) {
            for (Map.Entry<String, Number> entry : map.entrySet()) {
                sb.append(entry.getValue().toString());
                sb.append(",");
            }
            sb.append("\n");
        }
        br.write(sb.toString());
        br.close();
        //end saving file
        var ok = agentAndState.fst.evaluateLTLs();
        System.out.println(">>>> LTL results: " + ok);
        //  System.out.println(">>>> psi1 : " + psi1.sat());
        //  assertTrue(psi1.sat() == SATVerdict.SAT);
        assertTrue(psi2.sat() == SATVerdict.SAT);
        TestUtils.closeConnectionToSE(agentAndState.snd);
    }


    @Disabled
    @Test
    public void test_generator_basic() throws InterruptedException, IOException {
        var agentAndState = deployAgent("islanddswithdoorsWithDoors");
        TestAgent agent = agentAndState.fst ;
        agent.setTestDataCollector(dataCollector) ;
        var specs = new Specifications();
        var psi1 = specs.scenarioSpec0();  //eventually integrity of some blocks will decrease.
        var psi2 = specs.scenarioSpec1();  //eventually some assembler blocks will disappear
        agent.addLTL( psi2);
        agent.resetLTLs();

        List<Map<String, List<Object>>>  Specifications= new ArrayList<>();

        //test type basic
        String specificationType = "basic";
          String itemType = "BasicAssembler";
          Specifications.add(new HashMap<String, List<Object>>() {{
            put("basic",
                    new ArrayList<Object>() {{
                        add(q3());
                        add("BasicAssembler");
                    }}
            );
        }} );
        generatore(agentAndState, Specifications, specificationType);

        //save position in csv file:
        dataCollector.saveTestAgentScalarsTraceAsCSV(agentAndState.fst.getId(),"visits.csv");
        List<Map<String,Number>> tracePosition = agentAndState.fst
                . getTestDataCollector()
                . getTestAgentScalarsTrace(agentAndState.fst.getId())
                . stream()
                . map(event -> event.values) . collect(Collectors.toList());

        String newFileLocation = System.getProperty("user.dir") +  File.separator+"positions.csv" ;
        BufferedWriter br = new BufferedWriter(new FileWriter(newFileLocation));
        StringBuilder sb = new StringBuilder();
        Map<String, Number> firstRow = tracePosition.get(0);
        for (String key : firstRow.keySet()) {
            sb.append(key);
            sb.append(",");
        }
        sb.append("\n");
        for (Map<String,Number> map : tracePosition) {
            for (Map.Entry<String, Number> entry : map.entrySet()) {
                sb.append(entry.getValue().toString());
                sb.append(",");
            }
            sb.append("\n");
        }
        br.write(sb.toString());
        br.close();
        //end saving file
        var ok = agentAndState.fst.evaluateLTLs();
        System.out.println(">>>> LTL results: " + ok);
        //  System.out.println(">>>> psi1 : " + psi1.sat());
        //  assertTrue(psi1.sat() == SATVerdict.SAT);
        assertTrue(psi2.sat() == SATVerdict.SAT);
        TestUtils.closeConnectionToSE(agentAndState.snd);
    }


    //***************************************chain**********
    @Test
    public void test_generator_chain() throws InterruptedException, IOException {
        var agentAndState = deployAgent("islanddswithdoorsWithDoors-second"); // world/platform name
        TestAgent agent = agentAndState.fst ;
        agent.setTestDataCollector(dataCollector) ;
        var specs = new Specifications();
        var psi1 = specs.scenarioSpec0();  //eventually integrity of some blocks will decrease.
        var psi2 = specs.scenarioSpec1();  //eventually some assembler blocks will disappear
        agent.addLTL( psi2);
        agent.resetLTLs();


        List<Map<String, List<Object>>>  Specifications= new ArrayList<>();

        //   test type chain, a specific blocker should be grind then a nearest door be open, then all blocks should be grinded
        String specificationType = "chain";

        Specifications.add(new HashMap<String, List<Object>>() {{
            put("disjunctive",
                    new ArrayList<Object>() {{
                        add(q4());
                        add("BasicAssembler");
                    }}
            );
        }} );
        Specifications.add(new HashMap<String, List<Object>>() {{
            put("basic",
                new ArrayList<Object>() {{
                    add(q3());
                    add("BasicAssembler");
                }}
                );
        }} );
        Specifications.add(new HashMap<String, List<Object>>() {{
            put("disjunctive",
                    new ArrayList<Object>() {{
                        add(q2());
                        add("LargeBlockSlideDoor");
                    }}
            );
        }} );
        Specifications.add(new HashMap<String, List<Object>>() {{
            put("disjunctive",
                    new ArrayList<Object>() {{
                        add(q4());
                        add("BasicAssembler");
                    }}
            );
        }} );
        generatore(agentAndState, Specifications, specificationType);

        //save position in csv file:
        dataCollector.saveTestAgentScalarsTraceAsCSV(agentAndState.fst.getId(),"visits.csv");
        List<Map<String,Number>> tracePosition = agentAndState.fst
                . getTestDataCollector()
                . getTestAgentScalarsTrace(agentAndState.fst.getId())
                . stream()
                . map(event -> event.values) . collect(Collectors.toList());

        String newFileLocation = System.getProperty("user.dir") +  File.separator+"positions.csv" ;
        BufferedWriter br = new BufferedWriter(new FileWriter(newFileLocation));
        StringBuilder sb = new StringBuilder();
        Map<String, Number> firstRow = tracePosition.get(0);
        for (String key : firstRow.keySet()) {
            sb.append(key);
            sb.append(",");
        }
        sb.append("\n");
        for (Map<String,Number> map : tracePosition) {
            for (Map.Entry<String, Number> entry : map.entrySet()) {
                sb.append(entry.getValue().toString());
                sb.append(",");
            }
            sb.append("\n");
        }
        br.write(sb.toString());
        br.close();
        //end saving file
        var ok = agentAndState.fst.evaluateLTLs();
        System.out.println(">>>> LTL results: " + ok);
        //  System.out.println(">>>> psi1 : " + psi1.sat());
        //  assertTrue(psi1.sat() == SATVerdict.SAT);
        assertTrue(psi2.sat() == SATVerdict.SAT);
        TestUtils.closeConnectionToSE(agentAndState.snd);
    }

    public void generatore( Pair<TestAgent, UUSeAgentState> agentAndState, List<Map<String, List<Object>>> specification, String specificationType) throws InterruptedException, IOException {
        int  budgetExhausted = 1400; //number of turns
        //get the first element of the list for the first tree types
        Map<String, List<Object>> firstSpecification = specification.get(0);

        //Type one specification

        if(specificationType.equals("basic")){
            //setting the e
            var position = new Vec3(17.5f,2.5f,40.0f) ;
            helper(agentAndState, (Predicate<SimpleState>) firstSpecification.get(specificationType).get(0), position, (String) firstSpecification.get(specificationType).get(1));
        }
        else if (specificationType.equals("conjunctive")) {
            helper(agentAndState,(Predicate<SimpleState>) firstSpecification.get(specificationType).get(0),null, (String) firstSpecification.get(specificationType).get(1));
        } else if (specificationType.equals("disjunctive")) {
            //while(budgetExhausted > 0){
                helper(agentAndState,(Predicate<SimpleState>) firstSpecification.get(specificationType).get(0),null, (String) firstSpecification.get(specificationType).get(1));
           // }
        }else { //it is chaine
                for (Map<String, List<Object>> map : specification) {
               // Map<String, Predicate<SimpleState>> tempSpecification = specification.get(j);
                String tempSpecigficationType = null;
                for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
                    tempSpecigficationType = entry.getKey();
                }
                List<Map<String, List<Object>>> tempt = new ArrayList<>();
                tempt.add(map);
                generatore(agentAndState, tempt, tempSpecigficationType);
            }
        }


    }


    public Pair<TestAgent, UUSeAgentState> deployAgent(String worldName) throws InterruptedException {
        //worlds should change based on the type --  for type basic and conjunctive : world-4 blocks
        //for disjunctive: new-3 islands
        //for chain: islanddswithdoorsWithDoors
        var agentAndState = loadSE(worldName) ;
      //  var agentAndState = loadSE("islanddswithdoorsWithDoors") ;


        TestAgent agent = agentAndState.fst ;
        UUSeAgentState state = agentAndState.snd ;
        state.navgrid.enableFlying = true ;
        Thread.sleep(1000);
        state.updateState(state.agentId);
        console(showWOMAgent(state.wom));
        return new Pair<TestAgent, UUSeAgentState>(agent,state) ;
    }

    public void test_Goal(TestAgent agent, UUSeAgentState state, GoalStructure G) throws InterruptedException, IOException {
        agent.setGoal(G) ;

       // agent . setTestDataCollector(dataCollector).setGoal(G) ;

        while(G.getStatus().inProgress()) {
            console(">> numbre of turns [" + turn + "] " + showWOMAgent(state.wom));
            if (state.wom.position != null) {
                dataCollector.registerEvent(agent.getId(),
                        new ObservationEvent.ScalarTracingEvent(
                                new Pair("posx",state.wom.position.x),
                                new Pair("posy",state.wom.position.y),
                                new Pair("posz", state.wom.position.z),
                                new Pair("turn",turn),
                                new Pair("tick",1)));
            }
            agent.update();
            //Thread.sleep(50);
            turn++ ;
            if (turn >= 1400) break ;
        }


    }



    public void helper( Pair<TestAgent, UUSeAgentState> agentAndState, Predicate<SimpleState> p, Vec3 position, String Type) throws InterruptedException, IOException {

       // TestAgent agent = agentAndState.fst ;
        var G  = new Solver();
        //agent.setTestDataCollector(new TestDataCollector()) ;
        //Solver for LTL pattern basic
        GoalStructure goal ;
        if(position != null)
             goal = G.solver(agentAndState.fst, p,  position, Type);
        else{   //Solver for LTL pattern conjunctive, disjunctive, chain  -- select nearest
            goal = G.solver(agentAndState.fst, p,  Type);
        }

        Thread.sleep(5000);
        test_Goal(agentAndState.fst, agentAndState.snd, goal ) ;
        goal.printGoalStructureStatus();
        assertTrue(goal.getStatus().success());
   }



}
