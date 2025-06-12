import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

/**
 * A completely self-contained implementation of the GitHub events viewer
 * using only the standard org.json library to avoid Gson issues
 */
fun main(args: Array<String>) {
    // Parse args manually
    if (args.isEmpty() || args.contains("-h") || args.contains("--help")) {
        println("Usage: x9 USERNAME")
        println("Prints a summary of the user's GitHub push events to STDOUT.")
        println()
        println("Options:")
        println("  -h, --help                  Show this help message and exit")
        println("  -V, --version               Print version information and exit")
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

            try {
                // Parse JSON using the built-in JSON library
                val jsonArray = JSONArray(body)

                // Find all push events
                val pushEvents = mutableListOf<JSONObject>()
                for (i in 0 until jsonArray.length()) {
                    val event = jsonArray.getJSONObject(i)
                    if (event.getString("type") == "PushEvent") {
                        pushEvents.add(event)
                    }
                }

                if (pushEvents.isEmpty()) {
                    println("No push events found for user: $username")
                    return
                }

                // Process each push event
                pushEvents.forEach { event ->
                    val date = event.getString("created_at")
                    println("\nDate: $date")

                    val payload = event.getJSONObject("payload")
                    if (!payload.has("commits") || payload.getJSONArray("commits").length() == 0) {
                        println("  No commits found")
                    } else {
                        val commits = payload.getJSONArray("commits")
                        for (i in 0 until commits.length()) {
                            val commit = commits.getJSONObject(i)
                            val message = commit.getString("message")
                            println("  Commit: $message")

                            val url = commit.getString("url")
                            val githubUrl = url
                                .replace("https://api.github.com/repos/", "https://github.com/")
                                .replace("/commits/", "/commit/")
                            println("  Link: $githubUrl")
                            println()
                        }
                    }
                }
            } catch (e: Exception) {
                println("JSON parsing error: ${e.message}")
                println("Raw JSON response:\n$body")
            }
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}
