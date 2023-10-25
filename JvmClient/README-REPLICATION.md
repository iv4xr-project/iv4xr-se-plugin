# Replication Package II 
** Paper: ** _Specification-based Automated Testing in Computer Games_

This project contains the Space Engineers experiments (Section IV.B). MiniDungeon experiments (IV.A) are packaged in a different project.


This research is based on the fork of the SE project. The research is referred to as **onlineTestCaseGenerator**.
 
you can find the algorithm presented in the paper in the package `onlineTestCaseGenerator` in `./src/jvmMain/java/onlineTestCaseGenerator directory`.

## Replication Instructions

The experiments in IV.B are organized as Junit test-methods, located in  `.src/jvmTest/java/onlineTestCaseGenerator`. In particular in the class named `Test_Experiment`. There are comments in the test-methods that briefly specifies what they do.


All test-methods demonstrate how our algorithm(lets call it **Online**, as in the paper) generates a test case from a given scenario specification.
Some of the scenarios specify a specific item id or item type that the scenario has to visit. You can change them to try out different targets. These ids and types follow some convention. For instance, to run a disjunctive or conjunctive scenario, the _type_ of the item needs to be given to the test agent.
The types of assemblers and doors are represented by `BasicAssembler`, `LargeBlockSlideDoor` respectively. 

As examples, we describe some of the experiments as well as how to run them below.
A convenient way to re-run an experiment is by running the corresponding test method from the IDE.
For example, `test_generato_basic`  runs the **Online** algorithm, which in turns  generates a 'test case'.
As mentioned in the paper, generating a test case here means generating an execution that would fulfill a scenario. For `test_generator_basic` the scenario is a _basic scenario_  targeting an item with a particular id (see Table VII in the paper).
