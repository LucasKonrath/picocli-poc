import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

/**
 * An extremely simple GitHub app that uses only Java standard library
 * with no complex parsing or regex
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
        // Create connection using only Java standard library
        val url = URL("https://api.github.com/users/$username/events")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            println("Error: $responseCode - ${connection.responseMessage}")
            return
        }

        // Read the response
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()

        // Extremely basic parsing without regex
        val jsonStr = response.toString()

        // Very basic parsing for Push events
        var eventsFound = false
        var currentPosition = 0

        while (true) {
            // Find a PushEvent
            val typeIndex = jsonStr.indexOf("\"type\":\"PushEvent\"", currentPosition)
            if (typeIndex == -1) {
                break
            }
            eventsFound = true

            // Find the created_at date
            val dateStart = jsonStr.indexOf("\"created_at\":\"", typeIndex)
            val dateEnd = jsonStr.indexOf("\"", dateStart + 14)
            val date = if (dateStart != -1 && dateEnd != -1) {
                jsonStr.substring(dateStart + 14, dateEnd)
            } else {
                "Unknown date"
            }

            println("\nDate: $date")

            // Find the commits array
            val commitsStart = jsonStr.indexOf("\"commits\":", typeIndex)
            val commitsEnd = if (commitsStart != -1) {
                findMatchingBracket(jsonStr, jsonStr.indexOf("[", commitsStart))
            } else {
                -1
            }

            if (commitsStart == -1 || commitsEnd == -1) {
                println("  No commits found")
                currentPosition = typeIndex + 10
                continue
            }

            val commitsSection = jsonStr.substring(commitsStart + 10, commitsEnd + 1)

            // Find individual commits
            if (commitsSection == "[]") {
                println("  No commits found")
            } else {
                var commitPos = 0
                while (true) {
                    // Find message
                    val messageStart = commitsSection.indexOf("\"message\":\"", commitPos)
                    if (messageStart == -1) break

                    val messageEnd = commitsSection.indexOf("\"", messageStart + 11)
                    val message = if (messageEnd != -1) {
                        commitsSection.substring(messageStart + 11, messageEnd)
                    } else {
                        "Unknown message"
                    }

                    // Find URL
                    val urlStart = commitsSection.indexOf("\"url\":\"", messageStart)
                    val urlEnd = commitsSection.indexOf("\"", urlStart + 7)
                    val url = if (urlStart != -1 && urlEnd != -1) {
                        val apiUrl = commitsSection.substring(urlStart + 7, urlEnd)
                        apiUrl.replace("https://api.github.com/repos/", "https://github.com/")
                             .replace("/commits/", "/commit/")
                    } else {
                        "Unknown URL"
                    }

                    println("  Commit: $message")
                    println("  Link: $url")
                    println()

                    commitPos = urlEnd + 1
                }
            }

            currentPosition = typeIndex + 10
        }

        if (!eventsFound) {
            println("No push events found for user: $username")
        }

    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

// Helper function to find the matching closing bracket
fun findMatchingBracket(str: String, openPos: Int): Int {
    if (openPos < 0 || openPos >= str.length || str[openPos] != '[') {
        return -1
    }

    var count = 1
    var pos = openPos + 1
    while (pos < str.length && count > 0) {
        when (str[pos]) {
            '[' -> count++
            ']' -> count--
        }
        pos++
    }

    return if (count == 0) pos - 1 else -1
}
