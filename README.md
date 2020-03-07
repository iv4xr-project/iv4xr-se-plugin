# iv4xr Demo

This is a demo for the [iv4xr Agent-based Testing Framework](https://github.com/iv4xr-project/aplib). This repository contains
a configurable game called _Lab Recruits_ that is used to demonstrate how iv4xr agents
can be used to test it. The game is included as binary; else you can build it yourself
from its [repository](https://github.com/iv4xr-project/labrecruits).

**Work in progress notice.** Keep in mind that the work here is still in progress. Things may look ugly while we are working on them, and things may change.

**What is the demo?** A set of JUnit test classes demonstrating how iv4xr test agents are used to implement a number of test scenarios for the game _Lab Recruits_. These classes are located in `src/test/java/agents/demo`. You can simply run them, or modify them yourself. When you run them you don't usually see anything because the tests run pretty fast. You can insert pause-points yourself (e.g. using console-read).

### Deploying the demo

The demo classes are in `src/test/java/agents/demo`. The demos are by default non-visual (you don't literally see the game runs). Set the variable `TestSettings.USE_GRAPHICS` in the corresponding demo-class to `true` if you want it to be visual.

* Eclipse

   This will allow you to run/modify/rerun the demo classes. Import the project into Eclipse as a **maven project**. The demo classes are in `src/test/java/agents/demo`. You can run them as junit tests.

* Maven

   This is if you just want to check that the project builds and that all its tests pass.
   Just do `mvn compile` and `mvn test` at the project root.


### Contributors

**Computer Science students from Utrecht University:**
Adam Smits,
August van Casteren,
Bram Smit,
Frank Hoogmoed,
Jacco van Mourik,
Jesse van de Berg,
Maurin Voshol,
Menno Klunder,
Stijn Hinlopen,
Tom Tanis.
**Game Artists from Mediacollege Amsterdam:**
Quinto Veldhuyzen,
Sophie Meester.
**Others:** Wishnu Prasetya.
