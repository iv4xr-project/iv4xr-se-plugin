# Replication Package II
** Paper: ** _Specification-based Automated Testing in Computer Games_

This project contains the Space Engineers experiments (Section IV.B). MiniDungeon experiments (IV.A) are packaged in a different project.

This research is based on a fork of a Space Engineers plugin project. The research is referred to as **onlineTestCaseGenerator**.

You can find the algorithm presented in the paper in the package `onlineTestCaseGenerator` in `./src/jvmMain/java/onlineTestCaseGenerator directory`.

## Replication Instructions

#### Preparation

Replicating the experiments in IV.B is more complicated that those of IV.A. The SUT is a commercial game called _Space Engineers_. Due to licensing this cannot be packaged inside this package. Also note that this is a Windows game. Before we can even run the experiments you need to have this game installed and prepared to interface with the test agent used in the experiments:

   1. You will have to buy it from Steam, and additionally extensions/DLC "Space Engineers Deluxe" and "Sparks of the Future".
   2. A special 'plugin' need to be added into your Space Engineers install, which implements the Space-Engineers-side of the interface. This plugin comes in the form of several dll files. They need to built and then put manually; see the instruction in the project of this plugin: https://github.com/iv4xr-project/iv4xr-se-plugin 


#### Re-running the experiments

After the above preparation steps are done, we can re-run the experiments. The project is implemented in a mix of Kotlin and Java. To build you need Gradle.

The easiest way to build and run the experiments is by importing the project to the Intellij IDE. It supports Kotlin, Java, and Gradle. From Intellije we can rerun.

The experiments in IV.B are organized as Junit test-methods, located in  `.src/jvmTest/java/onlineTestCaseGenerator`. In particular in the class named `Test_Experiment`. There are comments in the test-methods that briefly specifies what they do.


All test-methods demonstrate how our algorithm(lets call it **Online**, as in the paper) generates a test case from a given scenario specification.
Some of the scenarios specify a specific item id or item type that the scenario has to visit. You can change them to try out different targets. These ids and types follow some convention. For instance, to run a disjunctive or conjunctive scenario, the _type_ of the item needs to be given to the test agent.
The types of assemblers and doors are represented by `BasicAssembler`, `LargeBlockSlideDoor` respectively.

A convenient way to re-run an experiment is by running the corresponding test method from the IDE.
For example, the test `test_generato_basic`  in the class `Test_Experiment`, which runs the **Online** algorithm, which in turns  generates a 'test case'.
As mentioned in the paper, generating a test case here means generating an execution that would fulfill a scenario. For `test_generator_basic` the scenario is a _basic scenario_  targeting an item with a particular id (see Table VII in the paper).
Other tests in `Test_Experiment` run the other test scenarios.
