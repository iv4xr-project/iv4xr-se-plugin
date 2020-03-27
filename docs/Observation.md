### World Object Model: structural represention of what an agent sees.

The class [`LabRecruitsEnvironment`](./BasicInterface.md) provides basic methods, such as `observe(agentId)` and `moveToward(agentId,p,q)` to control the _Lab Recruits_ game. In addition to executing the command in the game, these commands also return what the in-game agent (identified by _agentId_) observes. This observation represented as an instance of the class `LabWorldModel`, which in turn is a subclass of `eu.iv4xr.framework.world.WorldModel`. Instances of `WorldModel` is also called _World Object Model_ (WOM) as it **structurally** (so, not visually) describes what the game-world looks like from the agent's eye.

A WOM of _Lab Recruits_ has the following structure:

* the agent's _id_.
* the agent's current _position_ (center position).
* the agent's current _velocity_.
* the agent's bounding box.
* _timestamp_ denoting when the WOM is taken.
* a collection of _in-game entities_ that the agent currently sees.
* additionally also the fragment of the in-game world that the agent current see.

   In the case of _Lab Recruits_, the game does not send full description of this, but instead it only sends the _navigable part_ of the world that the agent currently sees. In other words, the game sends back information about which part of the in-game floor is visible, but it does not send any information on the walls that surround the agent. Information about the floor in enough to allow your AI to figure out how to navigate from one place in the virtual world to another, though the AI will not know if it is surrounded by walls, or by abyss.

   The data on the visible navigable part of the world is encoded in so-called _navigation-graph_. The 'floor' is divided into adjacent polygons (usually triangles). A navigation graph consists of the corners of these polygons, and edges representing how these corners are connected to each other. It is up to your AI how to use this information.

An in-game entity is represented by an instance of the class _WorldEntity_. Each has the following information:

* an _id_ that uniquely identify the entity.
* a string naming the entity _type_ (e.g. an entity with id _d10_ could be of type _door_).
* _timestamp_.
* the entity's _position_ (center position).
* the entity's _velocity_.
* the entity's bounding box.
* a boolean indicating whether the entity is _interactable_.
* a boolean indicating whether the entity is _dynamic_. An entity is 'dynamic' if its state can change at the runtime.
* a list of _properties_, each is a pair of (_n_,_v_) where _n_ is the property name and _v_ is the property value.
* a collection of other in-game entities that is a part of this entity (e.g. if this entity is represent an in-game bag containing other stuffs).
