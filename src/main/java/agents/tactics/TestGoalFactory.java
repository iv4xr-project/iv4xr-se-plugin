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

public class TestGoalFactory {
    public static TestGoal reachObject(String id) {
        return testgoal("Walk to object '" + id + "'")
                .toSolve((BeliefState b) -> b.withinRange(id))
                .withTactic(FIRSTof(
                        TacticsFactory.navigateTo(id),
                        TacticsFactory.explore(),
                        TacticsFactory.observe())
                );
    }

    public static TestGoal reachPosition(Vec3 position) {
        return testgoal("Walk to position '" + position + "'")
                .toSolve((BeliefState b) ->  b.withinRange(position))
                .withTactic(FIRSTof(TacticsFactory.navigateTo(position), TacticsFactory.observe()));
    }
}
