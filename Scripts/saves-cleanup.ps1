cd ..\JvmClient\src\jvmTest\resources\game-saves
get-childitem -Include Backup -Recurse -force | Remove-Item -Force -Recurse
get-childitem -Include *-saved -Recurse -force | Remove-Item -Force -Recurse