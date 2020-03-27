
### Lab Recruits Test Agent, Tactic, and Goals

For now, the overview is below.

* `LabRecruitsTestAgent` is a subclass of iv4xr `TestAgent`; it provides some convenience wrapping that mostly does type-casting.

* `BeliefState` provides a state structure of _Lab Recruits_ test agents. An instance of this class represents an agent's belief of how the game world is structured from in-game entities and its navigable parts.

   Note that a test agent can only observe the state of the world that is visible to it (e.g. it cannot see what is behind a solid wall). Recent observation will indeed be incorporated into the agent's belief, but note that this implies that some part of its belief may become obsolete (does not reflect reality). It is up to your AI how to deal with this.

* To automate testing tasks in _Lab Recruits_ there is a collection of some basic subgoals (along with their solving tactics) provided by the class `agents.tactics.GoalLib`. These subgoals can be seen as defining a DSL for testing  _Lab Recruits_.

  The underlying tactics are provided by the `agents.tactics.TacticLib`; it provides a set of basic tactics to interact with _Lab Recruits_ at the higher level than the primitive level provided by [`LabRecruitsEnvironment`](./BasicInterface.md) . E.g. these tactics incorporate automatic path finding.
