## How to make a release

### Preparation

* It's important that the plugin is compatible with the latest official Steam release of the game. To ensure that it needs to be built against SE sources from similar time (probably slightly older is better than newer). Note down which revision you are using.
* Make sure all the plugin projects (currently three) have the correct version, so that the built assemblies will have it
* Rebuild the solution
* In case testing reveals some code changes are needed, create a branch, perhaps named `release-candidate`

### Testing

* Test if the game runs with the plugin (does not crash)
* Test if the client communicates 
  * Run some of the BDD tests in the JvmClient, such as JvmClient/src/jvmTest/resources/features/C197574 .feature
  * Before a major release, test all of the BDD tests
* Test the plugin with the Steam version of the game
  * Copy **all the plugin libraries** plus **the dependencies** to the directory where Steam installed the game (the usual location is C:\Program Files (x86)\Steam\steamapps\common\SpaceEngineers\Bin64)
  * Check the launch options in Steam and run the game
  * Test several BDD scenarios
  * Note down the game version you are testing, for example: **1.199.025** (released in August 2021).

### The Release

- If the 3rd party dependencies or their versions changed, make a separate "release" for the binary dependencies (such as AustinHarris.JsonRpc.dll and Newtonsoft.Json.dll).
  - It's just a convenient way to download the libraries. We don't want to include them in the release because they have separate licenses. Officially we don't distribute them.
  - The name should be **3rd Party Library Dependencies (for vX.Y.Z)**
  - Copy-paste the description from the last 3rd party binary deps release and update the versions there.
  - Create a tag using semantic versioning with the suffix `-libs`, for example `v0.4.0-libs`
- Make the plugin release.
  - Copy the libraries from the SE Steam installation that you tested (to prevent grabbing some different ones).
  - Copy-paste the description from the last plugin release and update it.
    - Update the version of SE on Steam which you used to test the plugin binaries.
    - Update the revision of SE sources which were used to build the plugin binaries.
    - Compile the list of notable changes (using Git history; the simplest way is to look for merges into the main branch)
  - Create a tag using semantic versioning, for example `v0.4.0`
- Announce the release to all partners (Slack, mail, whoever is waiting for some features being released).

### Post Release

* Merge release-candidate to main, if there was a release branch.
* Bump version of the plugin binaries in the main branch to the next minor release (prepare the branch for the **next** release).
  * *Note: I think it’s a good habit to do version bump for the next release just after release rather than just before it. That way, if you encounter a binary with a version which has not yet been released, you know it’s a development version*