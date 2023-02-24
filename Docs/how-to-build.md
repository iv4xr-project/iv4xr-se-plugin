# How to build

First of all, you don't *have to* build it from sources. There are also binary releases (but, of course, the code is usually more up to date).

The plug-in requires Space Engineers libraries to compile. There are two ways how to provide the libraries: as binaries (DLLs) or as sources. Both options are described below.

The resulting plug-in (a couple of .NET libraries) works with the official Steam version of Space Engineers without any modification of the game.

## How to build with game binaries

We are developing the plugin using source dependencies; therefore, it is necessary to perform a few steps to switch to binary dependencies: provide the library binaries and switch the references to those binaries. We assume you have installed the official release of the game from Steam.

1. **Obtain the Space Engineers libraries.** Locate the script `copydeps.bat` in the `BinaryDependencies` directory.
    1. If you have your Steam installation of Space Engineers in the default path, then just run the script and the binaries will be copied to the directory. The default path is `C:\Program Files (x86)\Steam\steamapps\common\SpaceEngineers\Bin64`.
    2. If you have SE in some other path, provide it as the first argument to the script. (Or copy the libraries listed in the script manually.)
2. **Switch references from project dependencies to binary ones.** You can do it manually, or you can apply (cherry-pick) a commit pointed to by the branch `binary-deps-switch`. (The particular commit changes as we rebase it on newer history.)
3. **Open the solution and build it.** Find the VS solution file `Iv4xrPluginBinaryDeps.sln` in the `Solutions` folder. It's just a solution containing only the plugin projects, not the SE projects – the switch to the binaries has to be done in each of the projects, as described in the previous step. Open the solution and build it. You can than run the game with the plugin as described above.

## How to build if you have SE sources

There's a VS solution file `SpaceEngineers_ivxr.sln` in this repository (in the `Solutions` folder) that contains the plugin projects as well as Space Engineers projects, some of which are dependencies of the plug-in. For this solution file to work, you need to checkout Space Engineers sources to a specific location relative to this Git repository – the relevant branch (such as "Major") has to be checked-out into a directory called "`se`" located next to the checkout of this Git repository. See the nested list below, which corresponds to the required directory structure:

* `se-plugin` – just a top level directory, can have any name
    * `iv4xr-se-plugin` – a checkout of this Git repository
    * `se` – a checkout of a Space Engineers branch (presumably from it's Subversion repository)

Before starting the build of the solution, make sure a correct build configuration is selected. Either **Debug** or **Release** configuration and the **x64** platform.
