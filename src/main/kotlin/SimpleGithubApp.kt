package org.example.github

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request

// Import model classes from default package
import GithubEventsData
import Actor
import Repo
import Payload
import Commits

/**
 * A simplified implementation of the GitHub events viewer
 * that avoids all picocli reflection issues
 */
fun main(args: Array<String>) {
    // Parse args manually
    if (args.isEmpty() || args.contains("-h") || args.contains("--help")) {
        printGithubHelp()
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
            val gson = Gson()
            val eventsType = object : TypeToken<List<GithubEventsData>>() {}.type
            val events: List<GithubEventsData> = gson.fromJson(body, eventsType)

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

fun printGithubHelp() {
    println("Usage: x9 USERNAME")
    println("Prints a summary of the user's GitHub push events to STDOUT.")
    println()
    println("Options:")
    println("  -h, --help                  Show this help message and exit")
    println("  -V, --version               Print version information and exit")
}
