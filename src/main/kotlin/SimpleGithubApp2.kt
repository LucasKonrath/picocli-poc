import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * A completely standalone implementation of the GitHub events viewer
 * with internal data classes to avoid dependencies
 */
fun main(args: Array<String>) {
    // Parse args manually
    if (args.isEmpty() || args.contains("-h") || args.contains("--help")) {
        printGithubHelp2()
        return
    }

    if (args.contains("-V") || args.contains("--version")) {
        println("x9 1.0")
        return
    }

    val username = args.last()

    try {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.github.com/users/$username/events")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println("Error: ${response.code} - ${response.message}")
                return
            }

            val body = response.body?.string() ?: ""

            // Use TypeToken for properly deserializing the array of events
            val listType = object : TypeToken<List<GithubEvent>>() {}.type
            val events: List<GithubEvent> = Gson().fromJson(body, listType)

            val pushEvents = events.filter { it.type == "PushEvent" }
            if (pushEvents.isEmpty()) {
                println("No push events found for user: $username")
                return
            }

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
                        commit.url?.let { url ->
                            val githubUrl = url
                                .replace("https://api.github.com/repos/", "https://github.com/")
                                .replace("/commits/", "/commit/")
                            println("  Link: $githubUrl")
                        }
                        println()
                    }
                }
            }
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun printGithubHelp2() {
    println("Usage: x9 USERNAME")
    println("Prints a summary of the user's GitHub push events to STDOUT.")
    println()
    println("Options:")
    println("  -h, --help                  Show this help message and exit")
    println("  -V, --version               Print version information and exit")
}

// Internal data classes for GitHub API
data class GithubEvent(
    @SerializedName("id") val id: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("payload") val payload: EventPayload? = null
) {
    // No-args constructor required by Gson
    constructor() : this(null, null, null, null)
}

data class EventPayload(
    @SerializedName("commits") val commits: List<Commit>? = null
) {
    // No-args constructor required by Gson
    constructor() : this(null)
}

data class Commit(
    @SerializedName("message") val message: String? = null,
    @SerializedName("url") val url: String? = null
) {
    // No-args constructor required by Gson
    constructor() : this(null, null)
}
