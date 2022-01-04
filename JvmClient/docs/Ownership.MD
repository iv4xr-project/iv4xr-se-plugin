# Block ownership

## Basic necessary info about server side codebase

Blocks are represented by MySlimBlock (never subclassed).
The block class MySlimBlock has property FatBlock of type MyCubeBlock (or one of many subclasses), which can sometimes be null.
It seems, that only functional blocks have FatBlock non-null.

## OwnerId

Ownership of the MySlimBlock is done by asking:
- FatBlock.OwnerId (if FatBlock not null and FatBlock.OwnerId not 0).
- OwnershipComponent (subclass of abstract MyGridOwnershipComponentBase - seems like that class has no implementations), seems like it is always null.
- 0 returned (probably representing current user as owner or no owner - need to check what happens in multiplayer with simple blocks).


If block contains more complicated functionality, it also references "FatBlock" represented by MyCubeBlock (or a subclass).
Ownership of the block is done by asking:
- property IDModule (class MyIDModule), there's comment, that this class should be replaced by MyEntityOwnershipComponent (sharing base class MyComponentBase with MyGridOwnershipComponentBase, otherwise seems like no connection with that one). 
- 0

In reality, if you build Large armor block and Reactor in single grid and observe, Large armor block has ownerId 0 and Reactor block has a real ID (same as identityId of the player).

MyEntityOwnershipComponent doesn't seem to be used here at least normally (debugged constructor, never called).
It is being used when calling MyCubeGrid.ChangeOwner, MyIDModule being fallback, if non-existent (more likely the case).

MyCubeGrid.ChangeBlockOwner is doing things only if FatBlock of the block isn't null.

From all this, seems like ownership is bound to functionality and block without functionality has ownerId 0 and that means "no owner", not for example "current user".
TODO: Try to create non-functional blocks in multiplayer and double-check if they all have ownerId 0.

## BuiltBy

Data in the HUD "Built by: " is taken from BuiltBy property. This is part of MySlimBlock so every block including non-functional blocks has it.
It is represented by MyIDModule class, same as MyCubeBlock.OwnerId.

Side note: Hud data are in Sandbox.Game.Gui.MyHud class (in this case it's a static property BlockInfo of class MyHudBlockInfo)

## Multiple agents


TODO: we need to figure out, how to set identity ID (for now it's hardcoded to 1000). Either generate or provide as a parameter when creating.
Then it probably has to be added to some kind of character repository, because placeAt function doesn't set builtBy and ownerId correctly,
and it's most likely, because it cannot find the character somewhere in the system.
