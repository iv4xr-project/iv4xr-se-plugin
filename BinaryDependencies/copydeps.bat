
@echo off

set "source_dir=%~1"

if "%source_dir%"=="" (
    set "source_dir=C:\Program Files (x86)\Steam\steamapps\common\SpaceEngineers\Bin64"
    echo Using the default source path.
)

if not exist "%source_dir%" (
    echo Error:Source directory does not exist.
    echo.
    echo Please provide SpaceEngineers binary directory as the first argument.
    echo The location depends on your Steam settings. Look for "steamapps\common\SpaceEngineers\Bin64".
    exit /B 1
)

echo Copying files from:
echo %source_dir%

copy "%source_dir%\Sandbox.Common.dll"
copy "%source_dir%\Sandbox.Game.dll"
copy "%source_dir%\VRage.dll"
copy "%source_dir%\VRage.Game.dll"
copy "%source_dir%\VRage.Library.dll"
copy "%source_dir%\Vrage.Math.dll"
