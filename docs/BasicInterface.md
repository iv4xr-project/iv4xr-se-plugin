## Basic Interface

This document explains the basic interface to the game [_Lab Recruits_](https://github.com/iv4xr-project/labrecruits). This interface will allow you to programmatically control Lab Recruits 'player characters' (we will call them 'in-game agent') from Java. The interface is provided by the class `LabRecruitsEnvironment`. In addition, to use this interface we need a service that can launch the game and facilitate communication between Java and the game.

#### The class LabRecruitsEnvironment

More precisely, the class `environments.LabRecruitsEnvironment`. It provides the following methods to do basic control on the instance of the _Lab Recruits_ game that is currently connected to this environment. Suppose _E_ is an instance of `LabRecruitsEnvironment`, and _G_ is the instance of _Lab Recruits_ that is connected to it.

* _E_`.startSimulation()`. When _G_ is connected to _E_ for the first time, it will be in the paused state. In this state, the game will not do anything. `startSimulation()` will unpause the game.

* _E_`.pauseSimulation()` will pause the game.

* _E_`.observe(id)` will send a command to the game to send back information on what the in-game agent identified by _id_ senses. Here _id_ is a string which is the agent unique name.

  The method returns what the agent observes, represented by a so-called _World Object Model_ (WOM), which is an instance of a class called `WorldModel` (more precisely, it is an instance of a subclass called `LabWorldModel`). This observation includes the agent's position, in-game entities it sees (e.g. buttons and doors), and the part of the game's map (represented as a fragment of a navigation-graph) that it sees.

* _E_`.moveToward(id,p,q)` will command the in-game agent identified by _id_ to move from some small distance from its current position towards position _q_. We assume that _p_ is the agent's current position. The direction of the movement is a straight line following the vector from _p_ to _q_. Note that this command does not check if the path to _q_ is actually clear; this is a problem that your side of AI should solve.

* _E_`.interactWith(agentId,tid)` will command the in-game agent identified by _agentId_ to interact with an in-game entity identified by _tid_. This typically requires the agent to be in a close distance to the entity _tid_.

#### LabRecruitsTestServer

Before we can interface with _Lab Recruits_, an instance of it needs to be launched, and a communication channel with it needs to be set up. This will do that:

```java
var labRecruitsTestServer = new LabRecruitsTestServer(
        USE_GRAPHICS, // true to visually see the game, else false
        path-to-labrecruits-executable) ;

labRecruitsTestServer.waitForGameToLoad();
```

This will launch an instance of _Lab Recruits_ and a server providing communication between Java and the _Lab Recruits_ game. If we now create an instance of `LabRecruitsEnvironment`, it will be automatically connected to this server (and hence to the launched instance of _Lab Recruits_). After this, your program can control the launched game through the instance of `LabRecruitsEnvironment` that you just created.
