import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.gradle.process.CommandLineArgumentProvider
import org.gradle.api.tasks.Nested
import org.jetbrains.intellij.platform.gradle.argumentProviders.SandboxArgumentProvider
import org.jetbrains.intellij.platform.gradle.tasks.RunIdeTask

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform")
    id("org.jetbrains.changelog")
}

// IPGP passes the sandbox plugin both via -Didea.plugins.path and -Dplugin.path. Plugins from -Dplugin.path are always
// part of the module graph, so the IDE can't unload them for dynamic reload ("Failed to unload modified plugins").
// -Didea.required.plugins.id is dropped for the same reason: a required plugin can't be excluded from the graph.
class WithoutPluginPath(@get:Nested val delegate: CommandLineArgumentProvider) : CommandLineArgumentProvider {
    override fun asArguments() = delegate.asArguments().filterNot { it.startsWith("-Dplugin.path=") }
}

tasks.withType<RunIdeTask>().configureEach {
    val sandbox = jvmArgumentProviders.filterIsInstance<SandboxArgumentProvider>()
    jvmArgumentProviders.removeAll { it is SandboxArgumentProvider || it.javaClass.simpleName.endsWith("PluginArgumentProvider") }
    sandbox.forEach { jvmArgumentProviders.add(WithoutPluginPath(it)) }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    implementation("com.ezylang:EvalEx:3.8.0")

    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        intellijIdea("2026.2.3")
        bundledPlugin("intellij.grid.core.plugin")
        testFramework(TestFrameworkType.Platform)
        testBundledPlugins("com.intellij.modules.ultimate")
    }
}
