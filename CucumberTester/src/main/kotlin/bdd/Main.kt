package bdd

import io.cucumber.core.options.CommandlineOptionsParser
import io.cucumber.core.options.CucumberProperties
import io.cucumber.core.options.CucumberPropertiesParser
import io.cucumber.core.runtime.Runtime
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    val exitStatus: Byte = run(args, Thread.currentThread().contextClassLoader)
    exitProcess(exitStatus.toInt())
}

/**
 * Launches the Cucumber-JVM command line.
 *
 * @param  argv        runtime options. See details in the
 * `cucumber.api.cli.Usage.txt` resource.
 * @param  classLoader classloader used to load the runtime
 * @return             0 if execution was successful, 1 if it was not (test
 * failures)
 */
fun run(argv: Array<String>, classLoader: ClassLoader): Byte {
    val propertiesFileOptions = CucumberPropertiesParser()
        .parse(CucumberProperties.fromPropertiesFile())
        .build()
    val environmentOptions = CucumberPropertiesParser()
        .parse(CucumberProperties.fromEnvironment())
        .build(propertiesFileOptions)
    val systemOptions = CucumberPropertiesParser()
        .parse(CucumberProperties.fromSystemProperties())
        .build(environmentOptions)
    val commandlineOptionsParser = CommandlineOptionsParser(System.out)
    val runtimeOptions = commandlineOptionsParser
        .parse(*argv)
        .addDefaultGlueIfAbsent()
        .addDefaultFeaturePathIfAbsent()
        .addDefaultSummaryPrinterIfNotDisabled()
        .enablePublishPlugin()
        .build(systemOptions)
    val exitStatus = commandlineOptionsParser.exitStatus()
    if (exitStatus.isPresent) {
        return exitStatus.get()
    }
    val runtime = Runtime.builder()
        .withRuntimeOptions(runtimeOptions)
        .withClassLoader { classLoader }
        .build()
    runtime.run()
    return runtime.exitStatus()
}
