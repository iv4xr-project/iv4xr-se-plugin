# Automatic testing

## Overview

This document describes the idea and abstractions behind testing Space Engineers, currently implemented features and how
to run the tests.

## Basic requirements

Those requirements were specified by our testing team:

- All tests must be done in multiplayer, need to work both in the lobby and on a DS (dedicated server).
- There are 2 game instances involved.
- One game instance (lobby host or one of DS clients) is doing game actions.
- The other game instance is observing the game to check that the synchronization is correct.

## Hosting in lobby (game) vs DS

The game itself can host the game and be a player at the same time or join the game and be a player. The dedicated
server can host the game while not being involved (player).

## Role abstractions

To be flexible and allow simple test constructions in various combinations, the following role abstractions were
created:

- Admin – can run admin commands and hacks (ex. teleport). There is always only one admin, and it can be either a game
  or DS.
- Main client – is doing actions. There is always only one main client, and it is always a game.
- Observer – is observing the game state. Usually all involved instances can be observers including DS, but the other
  game client is the most important observer for our testers.

This model makes it impossible to do actions on more than one (main) client. To be able to do that, the model would be
more complicated. Each action would have to specify, which client is supposed to do it, asserts would have to specify,
which client state to verify. For our purposes, this is not necessary and the current model works well for us.

### Example setups

The setups are configured in json files. Configuration files include computer IP addresses, ports, config paths, roles,
type of game build (Steam vs built sources), etc. You can see some sample files
in [src/jvmTest/resources/connection-setup](https://github.com/iv4xr-project/iv4xr-se-plugin/tree/main/JvmClient/src/jvmTest/resources/connection-setup)
directory.

#### Single-player

- Computer 1: admin + main client + observer

#### Dedicated server

- DS computer: admin + observer
- Game computer 1: main client + observer
- Game computer 2: observer

#### Lobby game

- Game computer 1: admin + main client + observer
- Game computer 2: observer

#### Lobby game with non-admin main client

Another possibility, but we don't use it:

- Game computer 1: admin + observer
- Game computer 2: main client + observer

#### Lobby game with 3 computers

- Game computer 1: admin + main client + observer
- Game computer 2: observer
- Game computer 3: observer

## Test structure and BDD

### Writing tests

Tests are written in [cucumber](https://cucumber.io/) language. This means, test definitions are language independent.
They can be implemented in different languages (Kotlin in our case). Some of those definitions can also be transferred
to different games if viable (ex. movement tests).

For more information, read the language documentation.

### Implementing steps

Cucumber steps are of 2 types:

- Actions ([@When](https://cucumber.io/docs/gherkin/reference/#when) steps)
- Asserts ([@Given](https://cucumber.io/docs/gherkin/reference/#given)
  and [@Then](https://cucumber.io/docs/gherkin/reference/#then) steps)

JVM implementation
is [here](https://github.com/iv4xr-project/iv4xr-se-plugin/tree/main/JvmClient/src/jvmTest/kotlin/bdd), but it should be
easy to do C# implementation if necessary.

#### Actions

Actions are always executed on main client, because that is the one defined to do the action. Rarely actions can be
executed on admin, because a hack may be required.

The scenario loading action is unique, because that is very different between lobby and DS. For DS, dedicated server is
started with defined scenario and then all clients connect directly to the DS by using IP and port. DS games don't
require internet connection.

For lobby, the game is loaded through the game main menu and clients connect through join the game screen and friends
tab (so it currently requires players to be in friend list). All games need to be connected to internet and Steam.

#### Asserts

Asserts are checking if the current state is as expected. For example if the main character speed has a certain value.
This check is done on all observers to make sure, all game instances are seeing the same value. Some values are not
synchronized for performance reasons. In that case, the assert is done only on the main client, because other observers
have an empty value.

## Flexibility of roles, steps and configuration files

Role abstractions together with configuration files and step implementation give the whole project the flexibility to
run any kind of test scenario on any kind of configuration setup, which is very beneficial. Constructing scenarios can
be done on single-player setup for simplicity and then tested on multiplayer setup when finished. Many asserts and
actions can be reused in many tests.

## Code

Most of the code related to this documentation is
located [here](https://github.com/iv4xr-project/iv4xr-se-plugin/tree/main/JvmClient/src/jvmTest/kotlin/bdd).

## Running tests

There are multiple ways to
run [scenarios](https://github.com/iv4xr-project/iv4xr-se-plugin/tree/main/JvmClient/src/jvmTest/resources/features).

### Command line

To run all cucumber tests from command line, use:

```
./gradlew cucumber
```

For more information about filtering and running tests,
explore [cucumber plugin](https://github.com/tsundberg/gradle-cucumber-runner), that we use.

### IntelliJ IDEA

The IDE offers many ways of starting the scenarios, to name a few:

- Open a feature file and click on Green arrow next to a feature or a scenario line and click "Run".
- Right-click a feature file and click "Run".
- Right-click a directory containing feature files and click "Run".
