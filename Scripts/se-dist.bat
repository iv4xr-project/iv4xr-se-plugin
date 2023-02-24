@echo off

net use \\10.20.0.219\c$ /user:%keenuser% %keenpass%
net use \\10.20.0.221\c$ /user:%keenuser% %keenpass%
net use \\10.20.0.248\c$ /user:%keenuser% %keenpass%

set home=c$\Users\multi.keen\

set binary_source=..\Source\Ivxr.SePlugin\bin\Debug\netstandard2.0\
set dedicated_steam_binaries=c$\Program Files (x86)\Steam\steamapps\common\SpaceEngineersDedicatedServer\DedicatedServer64
set steam_binaries=c$\Program Files (x86)\Steam\steamapps\common\SpaceEngineers\Bin64



robocopy %binary_source% "C:\\Program Files (x86)\Steam\steamapps\common\SpaceEngineersDedicatedServer\DedicatedServer64" Ivxr*.dll

robocopy %binary_source% "\\10.20.0.219\%dedicated_steam_binaries%%" Ivxr*.dll
robocopy %binary_source% "\\10.20.0.221\%dedicated_steam_binaries%%" Ivxr*.dll

robocopy %binary_source% "\\10.20.0.221\%steam_binaries%%" Ivxr*.dll

robocopy %binary_source% "\\10.20.0.248\%steam_binaries%%" Ivxr*.dll
