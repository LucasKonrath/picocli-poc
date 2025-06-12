import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters

import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(name = "x9", mixinStandardHelpOptions = true, version = ["x9 1.0"],
    description = ["Prints a summary of the user's github pushes to STDOUT."])
class x9Cli : Callable<Int> {

    @Parameters(index = "0", description = ["The user whose data to summarize."])
    lateinit var username: String

    override fun call(): Int {
        val client: GithubApiClient = GithubApiClient()
        val pushEvents = client.getEvents(username)
            .filter { event -> event.type.equals("PushEvent") }

        // Group commits by date
        pushEvents.forEach { event ->
            val date = event.createdAt
            println("\nDate: $date")

            val commits = event.payload?.commits ?: emptyList()
            if (commits.isEmpty()) {
                println("  No commits found")
            } else {
                commits.forEach { commit ->
                    println("  Commit: ${commit.message}")
                    println("  Link: ${commit.url!!
                        .replace("https://api.github.com/repos/",
                            "https://github.com/")
                        .replace("/commits/", "/commit/")}")
                    println()
                }
            }
        }

        return 0
    }
}

fun main(args: Array<String>) : Unit {
    // This code enables picocli to work with GraalVM native image
    val cmd = CommandLine(x9Cli())

    // Register for reflection to make native-image aware of these classes
    cmd.commandSpec.mixins()
    cmd.commandSpec.optionsMap()
    cmd.commandSpec.positionalParameters()

    val exitCode = cmd.setCaseInsensitiveEnumValuesAllowed(true)
        .execute(*args)
    exitProcess(exitCode)
}
