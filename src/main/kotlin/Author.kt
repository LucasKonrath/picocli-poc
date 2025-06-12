import com.google.gson.annotations.SerializedName


data class Author (

  @SerializedName("email" ) var email : String? = null,
  @SerializedName("name"  ) var name  : String? = null

)