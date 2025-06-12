import com.google.gson.annotations.SerializedName


data class Actor (

  @SerializedName("id"            ) var id           : Int?    = null,
  @SerializedName("login"         ) var login        : String? = null,
  @SerializedName("display_login" ) var displayLogin : String? = null,
  @SerializedName("gravatar_id"   ) var gravatarId   : String? = null,
  @SerializedName("url"           ) var url          : String? = null,
  @SerializedName("avatar_url"    ) var avatarUrl    : String? = null

)