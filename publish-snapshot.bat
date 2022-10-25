SET PATH=%PATH%;"C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\MSBuild\Current\Bin"
msbuild Solutions/SpaceEngineers_ivxr.sln -p:Configuration=Debug "-target:Iv4XR\Ivxr_SePlugin"
Powershell.exe -ExecutionPolicy Bypass  -File public-snapshot.ps1