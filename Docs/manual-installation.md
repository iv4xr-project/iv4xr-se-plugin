# Manual installation

File "install.bat" downloads the dll files directly from the github latest release and copies them into your Space
Engineers game in Program Files as long as the game is in it's typical location.

You can still do this manually:

## Plugin libraries

1. Obtain a binary release of **the plugin**. Look
   for [releases](https://github.com/iv4xr-project/iv4xr-se-plugin/releases) in this repository and for Assets of the
   chosen release. Download all the DLL libraries.
2. If you want the latest changes or you'd like to edit the code, you can also build it from the sources (even if you
   don't have Space Engineers source code), see the section **How to build**.

All the dependencies need to be put into the folder with the game binaries. A common location is `C:\Program Files (x86)\Steam\steamapps\common\SpaceEngineers\Bin64`.
Tip: It's you can put the libraries into a subfolder (such as `ivxr-debug`). Or, it can be a symbolic link to the build folder of the plugin project. In that case, you must prefix the name of each library with `ivxr-debug\` in the following step.


## 3rd party libraries

Apart from the game libraries, the plugin requires three additional libraries to run:

* `AustinHarris.JsonRpc.dll`
* `Newtonsoft.Json.dll`
* `ImpromptuInterface.dll`

There are many ways how to obtain the libraries. For convenience we provide them as special releases in this repository. _They are **not** part of the official releases_ because – among other things – they may have different licenses. Look for "3rd Party Library Dependencies" among releases.

Another way how to get the libraries is the following:

* Check-out the [JSON-RPC.NET master branch on GitHub](https://github.com/Astn/JSON-RPC.NET).
    * *Side note: The binary releases are not updated (compared to NuGet packages), but the the last [release v1.1.74](https://github.com/Astn/JSON-RPC.NET/releases/tag/v1.1.74) works as well. You can try it if the master branch does not.*
* Build the solution including the test project (tested with Visual Studio 2019).
* You will find the `AustinHarris.JsonRpc.dll` library in this path:
  `Json-Rpc\bin\Debug\netstandard2.0`
* And the `Newtonsoft.Json.dll` library in this path:
  `AustinHarris.JsonRpcTestN\bin\Debug\netcoreapp3.0`

Note: If you build the project from the sources as described in the section **How to Build**, the libraries are downloaded via NuGet packages.

