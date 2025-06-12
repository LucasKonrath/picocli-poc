import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * A completely standalone implementation of the GitHub events viewer
 * with improved JSON deserialization
 */
fun main(args: Array<String>) {
    // Parse args manually
    if (args.isEmpty() || args.contains("-h") || args.contains("--help")) {
        printImprovedGithubHelp()
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

            // Create a Gson instance with lenient parsing
            val gson = GsonBuilder()
                .setLenient()
                .create()

            try {
                // Use TypeToken for properly deserializing the array of events
                val listType = object : TypeToken<List<Map<String, Any>>>() {}.type
                val rawEvents: List<Map<String, Any>> = gson.fromJson(body, listType)

                // Manually process the events
                val pushEvents = rawEvents.filter { (it["type"] as? String) == "PushEvent" }
                if (pushEvents.isEmpty()) {
                    println("No push events found for user: $username")
                    return
                }

                // Process each event manually
                pushEvents.forEach { event ->
                    val date = event["created_at"] as? String
                    println("\nDate: $date")

                    @Suppress("UNCHECKED_CAST")
                    val payload = event["payload"] as? Map<String, Any>
                    @Suppress("UNCHECKED_CAST")
                    val commits = payload?.get("commits") as? List<Map<String, Any>> ?: emptyList()

                    if (commits.isEmpty()) {
                        println("  No commits found")
                    } else {
                        commits.forEach { commit ->
                            val message = commit["message"] as? String
                            println("  Commit: $message")

                            val url = commit["url"] as? String
                            url?.let {
                                val githubUrl = it
                                    .replace("https://api.github.com/repos/", "https://github.com/")
                                    .replace("/commits/", "/commit/")
                                println("  Link: $githubUrl")
                            }
                            println()
                        }
                    }
                }
            } catch (e: Exception) {
                println("JSON parsing error: ${e.message}")
                println("Raw JSON response: $body")
            }
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun printImprovedGithubHelp() {
    println("Usage: x9 USERNAME")
    println("Prints a summary of the user's GitHub push events to STDOUT.")
    println()
    println("Options:")
    println("  -h, --help                  Show this help message and exit")
    println("  -V, --version               Print version information and exit")
}
