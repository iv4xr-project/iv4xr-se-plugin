package uuspaceagent;

import eu.iv4xr.framework.extensions.ltl.LTL;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import nl.uu.cs.aplib.exampleUsages.miniDungeon.testAgent.MyAgentState;
import nl.uu.cs.aplib.mainConcepts.SimpleState;
import spaceEngineers.model.Block;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static eu.iv4xr.framework.extensions.ltl.LTL.*;

public class Specifications {
    MyAgentState cast(SimpleState S) {
        return (MyAgentState) S;
    }

    // There is an assembler block that is grinded and destroyed

    static List<WorldEntity> allAssemblers(UUSeAgentState S) {
        String blockType = "BasicAssembler";
        return SEBlockFunctions.getAllBlocks(S.wom).stream().filter(e -> blockType.equals(e.getStringProperty("blockType"))).collect(Collectors.toList()) ;

    }

    Predicate<SimpleState> q0() {

        return S -> {
            var S_ = (UUSeAgentState) S ;

            var assemblers = allAssemblers(S_) ;
            if(S_.wom != null){
                var ok = assemblers.stream().allMatch(
                        assembler -> (Float) assembler.getProperty("integrity") >= 0
                                && (Float) assembler.getProperty("integrity") <=
                                (Float) S_.wom.before(assembler.id,"integrity")
                ) ;
                return ok ;
            }else return false;

        } ;
    }
    Predicate<SimpleState> q1() {

      final int[] numberOfAssemblersBefore = {-1} ;

      return S -> {
          var S_ = (UUSeAgentState) S ;

          var assemblers = allAssemblers(S_) ;

          int newNumberOfAssemblers = assemblers.size() ;

          var ok = false ;

          if (numberOfAssemblersBefore[0] >= 0) {
              ok = newNumberOfAssemblers == numberOfAssemblersBefore[0] - 1 ;
          }
          numberOfAssemblersBefore[0] = newNumberOfAssemblers ;
          return ok ;
      } ;
    }

    Predicate<SimpleState> q2() {
        return S -> {
            var S_ = (UUSeAgentState) S ;
            var isOpen = false;
            Block targetBlock = S_.env().getController().getObserver().observe().getTargetBlock() ;
            if(targetBlock.getDefinitionId().toString().contains("LargeBlockSlideDoor")){
                var checkIsOpen  = SEBlockFunctions.findWorldEntity(S_.wom,targetBlock.getId()) ;
                isOpen = (boolean) checkIsOpen.getProperty("isOpen");
            }

            return isOpen;
        };
    }




    //eventually integrity of some blocks will decrease.
    public LTL<SimpleState> scenarioSpec0() {
        String blockType = "BasicAssembler";
        return  eventually(q0());
    }

    //eventually some assembler blocks will disappear
    public LTL<SimpleState> scenarioSpec1() {
        return  eventually(q1());
    }

    //there is door which is open
    public LTL<SimpleState> scenarioSpec2() {
        return  eventually(q2());
    }


}
