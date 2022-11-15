$hash = (git rev-parse --short HEAD 2>&1)
gh release create --generate-notes -F ../Docs/snapshot-release-notes.md  -t "SE Plugin snapshot release $hash"  "SNAPSHOT-$hash" `
..\Source\Ivxr.SePlugin\bin\Debug\netstandard2.0\Ivxr.PlugIndependentLib.dll `
..\Source\Ivxr.SePlugin\bin\Debug\netstandard2.0\Ivxr.SePlugin.dll `
..\Source\Ivxr.SePlugin\bin\Debug\netstandard2.0\Ivxr.SpaceEngineers.dll `
..\Source\Ivxr.SePlugin\bin\Debug\netstandard2.0\Ivxr.PlugIndependentLib.pdb `
..\Source\Ivxr.SePlugin\bin\Debug\netstandard2.0\Ivxr.SePlugin.pdb `
..\Source\Ivxr.SePlugin\bin\Debug\netstandard2.0\Ivxr.SpaceEngineers.pdb