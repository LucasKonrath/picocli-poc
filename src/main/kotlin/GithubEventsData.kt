import com.google.gson.annotations.SerializedName

data class GithubEventsData( @SerializedName("id"         ) var id        : String?  = null,
                              @SerializedName("type"       ) var type      : String?  = null,
                              @SerializedName("actor"      ) var actor     : Actor?   = Actor(),
                              @SerializedName("repo"       ) var repo      : Repo?    = Repo(),
                              @SerializedName("payload"    ) var payload   : Payload? = Payload(),
                              @SerializedName("public"     ) var public    : Boolean? = null,
                              @SerializedName("created_at" ) var createdAt : String?  = null)
