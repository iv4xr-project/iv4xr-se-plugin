@echo off

net use \\10.20.0.219\c$ /user:%keenuser% %keenpass%
net use \\10.20.0.221\c$ /user:%keenuser% %keenpass%
net use \\10.20.0.248\c$ /user:%keenuser% %keenpass%

set home=c$\Users\multi.keen\

set binary_source=..\Source\Ivxr.SePlugin\bin\Debug\netstandard2.0\
set dedicated_steam_binaries=c$\Program Files (x86)\Steam\steamapps\common\SpaceEngineersDedicatedServer\DedicatedServer64
set steam_binaries=c$\Program Files (x86)\Steam\steamapps\common\SpaceEngineers\Bin64
set saves_source=..\JvmClient\src\jvmTest\resources\game-saves
set se_appdata=%home%AppData\Roaming\SpaceEngineers\Saves\%steamid%
set dedicated_se_appdata=%home%AppData\Roaming\SpaceEngineersDedicated\Saves

robocopy %binary_source% "C:\\Program Files (x86)\Steam\steamapps\common\SpaceEngineersDedicatedServer\DedicatedServer64" Ivxr*.dll

robocopy %binary_source% "\\10.20.0.219\%dedicated_steam_binaries%%" Ivxr*.dll

robocopy %binary_source% "\\10.20.0.221\%steam_binaries%%" Ivxr*.dll

robocopy %binary_source% "\\10.20.0.248\%steam_binaries%%" Ivxr*.dll

robocopy %saves_source% "\\10.20.0.219\%dedicated_se_appdata%%" /S
robocopy %saves_source% "\\10.20.0.221\%se_appdata%%" /S


rem - distribute plugin config
rem - distribute other dlls if not manually?

rem - archive test report, logs
