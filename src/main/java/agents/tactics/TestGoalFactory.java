/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents.tactics;

import eu.iv4xr.framework.mainConcepts.TestGoal;
import helperclasses.datastructures.Vec3;
import world.BeliefState;

import static eu.iv4xr.framework.Iv4xrEDSL.testgoal;
import static nl.uu.cs.aplib.AplibEDSL.FIRSTof;

// WP:
// this class should be depracated
//
public class TestGoalFactory {
    public static TestGoal reachObjectxx(String id) {
        return testgoal("Walk to object '" + id + "'")
                .toSolve((BeliefState b) -> b.withinRange(id))
                .withTactic(FIRSTof(
                        TacticLib.navigateTo(id),
                        TacticLib.explore(),
                        TacticLib.observe())
                );
    }

    public static TestGoal reachPositionxx(Vec3 position) {
        return testgoal("Walk to position '" + position + "'")
                .toSolve((BeliefState b) ->  b.withinRange(position))
                .withTactic(FIRSTof(TacticLib.navigateTo(position), TacticLib.observe()));
    }
}
