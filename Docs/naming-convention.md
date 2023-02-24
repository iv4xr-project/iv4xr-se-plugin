# Naming convention

The project contains the plugin in C# and a JVM client in Kotlin. Those 2 languages have different naming conventions.

- Properties and methods in C# usually begin with uppercase (`PascalCase`), but they begin with lowercase in
  Kotlin (`camelCase`).
- Interfaces in C# sometimes have "I" prefix to signal interface. We usually refer to interface without the "I" in the
  documentation unless directly referencing a class.
- If you see a reference to a variable or property, always interpret it in the context of the particular language.
- Since the server is implemented in C#, JSON-RPC protocol uses C# conventions and all method names and parameters are
  in `PascalCase`.