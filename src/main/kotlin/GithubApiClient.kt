import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type


class GithubApiClient {

    private val client = OkHttpClient()

    fun getEvents(username: String): List<GithubEventsData> {
        val request = Request.Builder()
            .url("https://api.github.com/users/$username/events")
            .build()

        val call = client.newCall(request);

        val response = call.execute();
        val listType: Type? = object : TypeToken<ArrayList<GithubEventsData?>?>() {}.getType()

        return Gson().fromJson(response.body?.string(), listType)
    }
}